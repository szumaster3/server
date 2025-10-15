package content.global.bots

import content.global.skill.magic.spells.ModernSpells
import core.game.bots.CombatBotAssembler
import core.game.bots.Script
import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import shared.consts.Items

class LumbridgeGoblinKiller : Script() {
    var state = State.KILLING
    var spawnZone = ZoneBorders(3243, 3244, 3263, 3235)
    var goblinZone = ZoneBorders(3240, 3228, 3264, 3254)
    val bankZone = ZoneBorders(3208, 3217, 3210, 3220)
    var delay = 0

    val forceChat = arrayOf(
        "WHY I CANT USE SPELLS",
        "movin' that cornflakes",
        "handle melee",
        "I got runes in inventory and nothin",
        "autocast not working",
        "who needs magic anyway?",
        "bad code keeps me stuck",
        "autocast not working",
        "i got runes but idk",
        "stop stealing my kills",
        "why u run from goblins?",
        "cant use spells lol",
        "lol low level smh",
        "run from goblins? lmao"
    )

    init {
        goblinZone.addException(ZoneBorders(3240, 3228, 3264, 3254))
    }

    override fun tick() {

        dialogue()

        when (state) {
            State.KILLING -> {
                scriptAPI.attackNpcInRadius(bot, "Goblin", 10)
                state = State.LOOTING
            }

            State.LOOTING -> {
                bot.pulseManager.run(
                    object : Pulse(4) {
                        override fun pulse(): Boolean {
                            scriptAPI.takeNearestGroundItem(Items.BONES_526)
                            state =
                                if (bot.inventory.getAmount(Items.BONES_526) > 22) {
                                    State.TO_BANK
                                } else {
                                    State.KILLING
                                }
                            return true
                        }
                    },
                )
            }

            State.TO_BANK -> {
                if (goblinZone.insideBorder(bot)) {
                    scriptAPI.walkTo(Location.create(3246, 3243, 0))
                } else {
                    val doors = scriptAPI.getNearestNode(36846, true)
                    if (doors != null && doors.location.withinDistance(bot.location, 2)) {
                        doors.interaction.handle(bot, doors.interaction[0])
                    } else {
                        when (bot.location) {
                            Location.create(3212, 3227, 0) -> {
                                val stairs = scriptAPI.getNearestGameObject(bot.location, 36776)
                                stairs?.interaction?.handle(bot, stairs.interaction[0])
                            }

                            Location.create(3206, 3229, 1) -> {
                                val stairs = scriptAPI.getNearestNode(36777, true)
                                stairs?.interaction?.handle(bot, stairs.interaction[1])
                            }

                            Location.create(3206, 3229, 2) -> {
                                scriptAPI.walkTo(bankZone.randomLoc)
                                state = State.BANKING
                            }

                            else -> scriptAPI.walkTo(Location.create(3212, 3227, 0))
                        }
                    }
                }
            }

            State.BANKING -> {
                if (bankZone.insideBorder(bot)) {
                    val bank = scriptAPI.getNearestNode(36786, true)
                    bot.pulseManager.run(
                        object : MovementPulse(bot, bank, DestinationFlag.OBJECT) {
                            override fun pulse(): Boolean {
                                scriptAPI.bankItem(Items.BONES_526)
                                if (bot.bank.getAmount(Items.BONES_526) > 75) {
                                    scriptAPI.teleportToGE()
                                    state = State.TELE_GE
                                } else {
                                    state = State.BACK_TO_GOBLINS
                                }
                                return true
                            }
                        },
                    )
                }
            }

            State.BACK_TO_GOBLINS -> {
                if (bankZone.insideBorder(bot)) {
                    scriptAPI.walkTo(Location.create(3248, 3240, 0))
                } else {
                    when (bot.location) {
                        Location.create(3206, 3229, 2) -> {
                            val stairs = scriptAPI.getNearestNode(36778, true)
                            stairs?.interaction?.handle(bot, stairs.interaction[0])
                        }

                        Location.create(3206, 3229, 1) -> {
                            val stairs = scriptAPI.getNearestNode(36777, true)
                            stairs?.interaction?.handle(bot, stairs.interaction[2])
                        }

                        Location.create(3246, 3243, 0) -> {
                            val doors = scriptAPI.getNearestNode(36846, true)
                            if (doors != null && doors.location.withinDistance(bot.location, 2)) {
                                doors.interaction.handle(bot, doors.interaction[0])
                            } else {
                                scriptAPI.walkTo(goblinZone.randomLoc)
                                state = State.KILLING
                            }
                        }

                        else -> scriptAPI.walkTo(Location.create(3251, 3243, 0))
                    }
                }
            }

            State.TELE_GE -> {
                if (bot.location == Location.create(3165, 3482, 0)) {
                    state = State.SELL_GE
                } else {
                    scriptAPI.walkTo(Location.create(3165, 3482, 0))
                }
            }

            State.SELL_GE -> {
                state = State.TELE_LUM
                scriptAPI.sellOnGE(Items.BONES_526)
            }

            State.TELE_LUM -> {
                state = State.BACK_TO_GOBLINS
                scriptAPI.teleport(Location.create(3222, 3218, 0))
            }
        }
    }

    enum class State {
        KILLING,
        LOOTING,
        BANKING,
        TO_BANK,
        BACK_TO_GOBLINS,
        SELL_GE,
        TELE_GE,
        TELE_LUM,
    }

    private fun dialogue() {
        if (delay-- <= 0) {
            scriptAPI.sendChat(forceChat.random())
            delay = RandomFunction.random(10, 30)
        }
    }

    override fun newInstance(): Script {
        val script = LumbridgeGoblinKiller()
        val bot = CombatBotAssembler().produce(
            CombatBotAssembler.Type.MAGE,
            CombatBotAssembler.Tier.LOW,
            spawnZone.randomLoc,
        )

        inventory.add(Item(Items.AIR_RUNE_556, 9000))
        inventory.add(Item(Items.MIND_RUNE_558, 9000))

        bot.properties.spell!!.spellId = ModernSpells.AIR_STRIKE

        script.bot = bot
        script.state = State.KILLING
        return script
    }
}
