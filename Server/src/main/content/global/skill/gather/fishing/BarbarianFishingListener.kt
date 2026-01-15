package content.global.skill.gather.fishing

import content.region.kandarin.baxtorian.BarbarianTraining
import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import core.tools.colorize
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs

class BarbarianFishingListener : InteractionListener {

    override fun defineListeners() {

        defineInteraction(
            IntType.NPC,
            intArrayOf(NPCs.FISHING_SPOT_1176),
            "Fish",
            persistent = true,
            allowedDistance = 1,
            handler = ::handleFishing
        )
    }

    private fun handleFishing(player: Player, node: Node, state: Int): Boolean {
        val npc = node as? NPC ?: return clearScripts(player)

        if (!finishedMoving(player))
            return restartScript(player)

        if (state == 0) {
            if (!checkRequirements(player, node))
                return clearScripts(player)

            sendMessage(player, "You cast out your line...")
        }

        if (clockReady(player, Clocks.SKILLING)) {
            anim(player)

            val bait = hasBait(player) ?: return restartScript(player)

            val fish = getRandomFish(player)
            val success = rollSuccess(player, fish.id)

            if(!hasSpaceFor(player, Item(fish.id))) return restartScript(player)

            if (success) {
                removeItem(player, bait)
                addItem(player, fish.id)

                val fishXP = when (fish.id) {
                    Items.LEAPING_TROUT_11328 -> 50.0
                    Items.LEAPING_SALMON_11330 -> 70.0
                    Items.LEAPING_STURGEON_11332 -> 80.0
                    else -> 0.0
                }

                val strAgiXP = when (fish.id) {
                    Items.LEAPING_TROUT_11328 -> 5.0
                    Items.LEAPING_SALMON_11330 -> 6.0
                    Items.LEAPING_STURGEON_11332 -> 7.0
                    else -> 0.0
                }

                rewardXP(player, Skills.FISHING, fishXP)
                rewardXP(player, Skills.AGILITY, strAgiXP)
                rewardXP(player, Skills.STRENGTH, strAgiXP)

                sendMessage(player, "You catch a ${fish.name.lowercase()}.")

                if (!getAttribute(player, BarbarianTraining.FISHING_BASE, false)) {
                    sendDialogueLines(
                        player,
                        "You feel you have learned more of barbarian ways. Otto might wish",
                        "to talk to you more."
                    )
                    setAttribute(player, BarbarianTraining.FISHING_BASE, true)
                    player.savedData.activityData.isBarbarianFishingRod = true
                }
            } else {
                sendMessage(player, "You fail to catch any fish.")
            }

            player.dispatch(ResourceProducedEvent(fish.id, fish.amount, node))
            delayClock(player, Clocks.SKILLING, 5)
        }

        return keepRunning(player)
    }

    private fun anim(player: Player) {
        if (player.animator.isAnimating) return
        player.animate(Animation(Animations.ROD_FISHING_622))
    }

    private fun checkRequirements(player: Player, node : Node): Boolean {
        val fishing = getStatLevel(player, Skills.FISHING)
        val agility = getStatLevel(player, Skills.AGILITY)
        val strength = getStatLevel(player, Skills.STRENGTH)

        if (fishing < 48) {
            sendMessage(player, "You need a Fishing level of at least 48 to fish here.")
            return false
        }

        if (agility < 15 || strength < 15) {
            val stat = when {
                agility < 15 && strength < 15 -> "agility and strength"
                agility < 15 -> "agility"
                else -> "strength"
            }
            sendMessage(player, "You need a $stat level of at least 15 to fish here.")
            return false
        }

        if(!getAttribute(player, BarbarianTraining.FISHING_START, false)){
            sendDialogue(player, "You must begin the relevant section of Otto Godblessed's barbarian training.")
            return false
        }

        if (!anyInInventory(
                player,
                Items.FISHING_BAIT_313,
                Items.FEATHER_314,
                Items.ROE_11324,
                Items.FISH_OFFCUTS_11334,
                Items.CAVIAR_11326
            )
        ) {
            sendMessage(player, "You don't have any bait to fish with.")
            return false
        }

        if (player.inventory.isFull) {
            sendMessage(player, "You don't have enough space in your inventory.")
            return false
        }

        return node.isActive && node.location.withinDistance(player.location, 1)
    }

    private fun rollSuccess(player: Player, fishId: Int): Boolean {
        val level = 1 + player.skills.getLevel(Skills.FISHING) +
                player.familiarManager.getBoost(Skills.FISHING)
        val host = Math.random() * fishId
        val client = Math.random() * (level * 3.0 - fishId)
        return host < client
    }

    private fun getRandomFish(player: Player): Item {
        val fish = arrayOf(
            Items.LEAPING_TROUT_11328,
            Items.LEAPING_SALMON_11330,
            Items.LEAPING_STURGEON_11332
        )

        var max = 0
        if (player.skills.getLevel(Skills.FISHING) >= 58 &&
            player.skills.getLevel(Skills.STRENGTH) >= 30 &&
            player.skills.getLevel(Skills.AGILITY) >= 30
        ) max++

        if (player.skills.getLevel(Skills.FISHING) >= 70 &&
            player.skills.getLevel(Skills.STRENGTH) >= 45 &&
            player.skills.getLevel(Skills.AGILITY) >= 45
        ) max++

        return Item(fish[RandomFunction.random(max + 1)])
    }

    private val baitItems = listOf(
        Items.FISHING_BAIT_313,
        Items.FEATHER_314,
        Items.FISH_OFFCUTS_11334,
        Items.ROE_11324,
        Items.CAVIAR_11326
    )

    private fun hasBait(player: Player): Int? {
        return baitItems.firstOrNull { inInventory(player, it) }
    }
}
