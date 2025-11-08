package content.global.skill.smithing.smelting

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Sounds

class CannonballPulse(
    player: Player?,
    val item: Item?,
    private var amount: Int,
) : SkillPulse<Item?>(player, null) {
    private var tick = 0

    override fun checkRequirements(): Boolean {
        if (!isQuestComplete(player, Quests.DWARF_CANNON)) {
            sendDialogue(player, "You need to complete the ${Quests.DWARF_CANNON} quest in order to do this.")
            return false
        }
        if (getDynLevel(player, Skills.SMITHING) < 35) {
            sendDialogue(player, "You need a Smithing level of at least 35 in order to do this.")
            return false
        }
        if (!inInventory(player, Items.AMMO_MOULD_4)) {
            sendDialogue(player, "You need an ammo mould in order to make a cannon ball.")
            return false
        }
        return true
    }

    override fun animate() {
    }

    override fun reward(): Boolean {
        when (tick++) {
            0 -> {
                playAudio(player, Sounds.FURNACE_2725)
                sendMessage(player, "You heat the steel bar into a liquid state.")
                animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
            }

            1 -> {
                sendMessage(player, "You pour the molten metal into your cannonball mould.")
                animate(player, Animations.HUMAN_BURYING_BONES_827)
            }

            3 -> {
                sendMessage(player, "The molten metal cools slowly to form 4 cannonballs.")
            }

            5 -> {
                if (removeItem(player, item)) {
                    addItem(player, Items.CANNONBALL_2, 4)
                    rewardXP(player, Skills.SMITHING, 25.6)
                }
                animate(player, Animations.HUMAN_BURYING_BONES_827)
            }

            6 -> {
                if (--amount == 0 || !inInventory(player, Items.STEEL_BAR_2353)) {
                    return true
                }
                tick = 0
            }
        }
        return false
    }
}
