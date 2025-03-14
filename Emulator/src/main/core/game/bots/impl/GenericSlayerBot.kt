package core.game.bots.impl

import core.game.bots.AIRepository
import core.game.bots.Script
import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import org.rs.consts.Items

class GenericSlayerBot : Script() {
    var state = State.GETTING_TASK
    var task = Task.CAVE_CRAWLER
    var assignment = Assignment("", 0)
    var teleportFlag = false
    val FOOD_ID = Items.LOBSTER_379
    val varrockBankBorders = ZoneBorders(3184, 3435, 3187, 3441)

    override fun tick() {
        scriptAPI.eat(FOOD_ID)
        when (state) {
            State.GETTING_TASK -> {
                populateTaskAndAssignment()
                state = State.GOING_TO_HUB
            }

            State.GOING_TO_HUB -> {
                when (task.hub) {
                    TaskHub.FREMENNIK_CAVE -> {
                        if (!teleportFlag) {
                            scriptAPI.teleport(Location.create(2781, 3615, 0))
                            teleportFlag = !teleportFlag
                        } else {
                            val entrance =
                                scriptAPI.getNearestNode(
                                    4499,
                                    true,
                                )
                            if (entrance == null || !entrance.location.withinDistance(bot.location, 2)
                            ) {
                                scriptAPI.walkTo(Location.create(2796, 3615, 0))
                            } else {
                                entrance.interaction.handle(bot, entrance.interaction[0])
                                teleportFlag = !teleportFlag
                                state = State.KILLING_ENEMY
                            }
                        }
                    }

                    else -> {}
                }
            }

            State.KILLING_ENEMY -> {
                val items = AIRepository.groundItems[bot]
                if (!items.isNullOrEmpty()) {
                    if (bot.inventory.freeSlots() == 0) {
                        if (bot.inventory.contains(FOOD_ID, 1)) {
                            scriptAPI.forceEat(FOOD_ID)
                            return
                        }
                    }
                    scriptAPI.takeNearestGroundItem(items.get(0).id)
                    if (bot.inventory.getAmount(Item(FOOD_ID)) == 0) {
                        state = State.GOING_TO_BANK
                    }
                    return
                }

                if (assignment.amount > 0) {
                    if (task.borders.insideBorder(bot)) {
                        scriptAPI.attackNpcInRadius(bot, task.npc_name, 20)
                        assignment.amount--
                    } else {
                        scriptAPI.walkTo(task.borders.randomLoc)
                    }
                } else {
                    state = State.GOING_TO_BANK
                }
            }

            State.GOING_TO_BANK -> {
                if (!teleportFlag) {
                    scriptAPI.teleport(Location.create(3213, 3426, 0))
                    teleportFlag = !teleportFlag
                } else {
                    if (!varrockBankBorders.insideBorder(bot)) {
                        scriptAPI.walkTo(varrockBankBorders.randomLoc)
                    } else {
                        val bank = scriptAPI.getNearestNode("Bank Booth", true)
                        bank ?: return
                        bot.pulseManager.run(
                            object : MovementPulse(bot, bank, DestinationFlag.OBJECT) {
                                override fun pulse(): Boolean {
                                    bot.faceLocation(bank.location)
                                    state = State.BANKING
                                    teleportFlag = !teleportFlag
                                    return true
                                }
                            },
                        )
                    }
                }
            }

            State.BANKING -> {
                bot.pulseManager.run(
                    object : Pulse(10) {
                        override fun pulse(): Boolean {
                            for (item in bot.inventory.toArray()) {
                                item ?: continue
                                if (item.checkIgnored()) continue
                                bot.bank.add(item)
                            }
                            bot.inventory.clear()
                            for (item in inventory) {
                                bot.inventory.add(item)
                            }
                            scriptAPI.withdraw(Items.LOBSTER_379, 10)
                            bot.fullRestore()

                            if (assignment.amount <= 0) {
                                state = State.GOING_TO_GE
                            } else {
                                state = State.GOING_TO_HUB
                            }

                            return true
                        }
                    },
                )
            }

            State.GOING_TO_GE -> {
                if (bot.location != Location.create(3165, 3487, 0)) {
                    scriptAPI.walkTo(Location.create(3165, 3487, 0))
                } else {
                    state = State.SELLING
                }
            }

            State.SELLING -> {
                scriptAPI.sellAllOnGe()
                state = State.GETTING_TASK
            }
        }
    }

    override fun newInstance(): Script {
        TODO("Not yet implemented")
    }

    fun populateTaskAndAssignment() {
        task = Task.values().random()
        assignment = Assignment(task.npc_name, RandomFunction.random(task.minAmt, task.maxAmt + 1))
    }

    fun Item.checkIgnored(): Boolean {
        if (name.toLowerCase().contains("charm")) return true
        if (name.toLowerCase().contains("lobster")) return true
        if (name.toLowerCase().contains("clue")) return true
        if (!definition.isTradeable) return true
        return when (id) {
            Items.BONES_2530 -> true
            995 -> true
            else -> false
        }
    }

    enum class State {
        GETTING_TASK,
        GOING_TO_HUB,
        KILLING_ENEMY,
        GOING_TO_BANK,
        BANKING,
        GOING_TO_GE,
        SELLING,
    }

    init {
        skills[Skills.SLAYER] = 99
        inventory.add(Item(Items.LOBSTER_379, 10))
    }

    enum class Task(
        val npc_name: String,
        val minAmt: Int,
        val maxAmt: Int,
        val hub: TaskHub,
        val borders: ZoneBorders,
    ) {
        CAVE_CRAWLER("Cave crawler", 20, 100, TaskHub.FREMENNIK_CAVE, ZoneBorders(2778, 9988, 2798, 10002)),
    }

    enum class TaskHub {
        FREMENNIK_CAVE,
        SLAYER_TOWER,
        TAVERLY_DUNGEON,
    }

    class Assignment(
        val npc_name: String,
        var amount: Int,
    )
}
