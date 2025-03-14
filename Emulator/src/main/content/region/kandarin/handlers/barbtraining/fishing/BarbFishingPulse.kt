package content.region.kandarin.handlers.barbtraining.fishing

import content.region.kandarin.handlers.barbtraining.BarbarianTraining
import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs

class BarbFishingPulse(
    player: Player,
) : SkillPulse<NPC>(player, NPC(NPCs.FISHING_SPOT_1176)) {
    private val fishingBait =
        anyInInventory(
            player,
            Items.FISHING_BAIT_313,
            Items.FEATHER_314,
            Items.ROE_11324,
            Items.FISH_OFFCUTS_11334,
            Items.CAVIAR_11326,
        )

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.FISHING) < 48) {
            sendMessage(player, "You need a fishing level of at least 48 to fish here.")
            return false
        }
        if (getStatLevel(player, Skills.AGILITY) < 15 || getStatLevel(player, Skills.STRENGTH) < 15) {
            player.sendMessages(
                "You need a ",
                if (getStatLevel(player, Skills.AGILITY) < 15 &&
                    getStatLevel(
                        player,
                        Skills.STRENGTH,
                    ) < 15
                ) {
                    "agility and strength"
                } else if (getStatLevel(player, Skills.AGILITY) < 15) {
                    "agility"
                } else {
                    "strength"
                },
                " level of at least 15 to fish here.",
            )
            return false
        }
        if (!inInventory(player, Items.BARBARIAN_ROD_11323)) {
            sendMessage(player, "You need a barbarian fishing rod to fish here.")
            return false
        }

        if (player.inventory.isFull) {
            sendMessage(player, "You don't have enough space in your inventory.")
            return false
        }

        if (!fishingBait) {
            sendMessage(player, "You don't have any bait with which to fish.")
            return false
        }
        return true
    }

    override fun animate() {
        animate(player, Animations.ROD_FISHING_622, true)
    }

    override fun reward(): Boolean {
        val stragiXP = arrayOf(5, 6, 7)
        val fishXP = arrayOf(50, 70, 80)
        val reward = getRandomFish()
        val success =
            rollSuccess(
                when (reward.id) {
                    Items.LEAPING_TROUT_11328 -> 48
                    Items.LEAPING_SALMON_11330 -> 58
                    Items.LEAPING_STURGEON_11332 -> 70
                    else -> 99
                },
            )
        val index = (
            when (reward.id) {
                Items.LEAPING_TROUT_11328 -> 0
                Items.LEAPING_SALMON_11330 -> 1
                Items.LEAPING_STURGEON_11332 -> 2
                else -> 0
            }
        )
        sendMessage(player, "You attempt to catch fish.")
        if (success) {
            if (!removeItem(player, Items.FISH_OFFCUTS_11334)) {
                removeItem(player, Items.FEATHER_314)
            }
            player.inventory.add(reward)
            rewardXP(player, Skills.FISHING, fishXP[index].toDouble())
            rewardXP(player, Skills.AGILITY, stragiXP[index].toDouble())
            rewardXP(player, Skills.STRENGTH, stragiXP[index].toDouble())
            sendMessage(player, "You catch a ${reward.name.lowercase()}.")

            if (getAttribute(player, BarbarianTraining.FISHING_BASE, false)) {
                removeAttribute(player, BarbarianTraining.FISHING_BASE)
                setAttribute(player, BarbarianTraining.FISHING_FULL, true)
                sendDialogueLines(
                    player,
                    "You feel you have learned more of barbarian ways. Otto might wish",
                    "to talk to you more.",
                )
            }
        }
        super.setDelay(5)
        return player.inventory.freeSlots() == 0
    }

    private fun rollSuccess(fish: Int): Boolean {
        val level = 1 + player.skills.getLevel(Skills.FISHING) + player.familiarManager.getBoost(Skills.FISHING)
        val hostRatio: Double = Math.random() * fish
        val clientRatio: Double = Math.random() * (level * 3.0 - fish)
        return hostRatio < clientRatio
    }

    private fun getRandomFish(): Item {
        val fish = arrayOf(Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330, Items.LEAPING_STURGEON_11332)
        val fishing = player.skills.getLevel(Skills.FISHING)
        val strength = player.skills.getLevel(Skills.STRENGTH)
        val agility = player.skills.getLevel(Skills.AGILITY)

        var possibleIndex = 0
        if (fishing >= 58 && (strength >= 30 && agility >= 30)) possibleIndex++
        if (fishing >= 70 && (strength >= 45 && agility >= 45)) possibleIndex++
        return Item(fish[RandomFunction.random(possibleIndex + 1)])
    }
}
