package content.global.handlers.item

import content.data.Lamps
import core.api.*
import core.game.component.Component
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.BLUE
import org.rs.consts.Components
import org.rs.consts.Items

class ExperienceLampListener : InteractionListener {
    private val xpGainItems = Lamps.values().map { it.item }.toIntArray()

    override fun defineListeners() {
        on(xpGainItems, IntType.ITEM, "rub", "read") { player, node ->
            setAttribute(player, "caller") { skill: Int, _: Player ->
                setAttribute(player, "xp_reward_item", node)
                val lamp = Lamps.forItem(player.getAttribute("xp_reward_item", Item(Items.LAMP_2528)))
                if (getStatLevel(player, skill) < lamp!!.requiredLevel) {
                    sendMessage(
                        player,
                        "You need at least " + lamp.requiredLevel + " " + Skills.SKILL_NAME[skill] + " to do this.",
                    )
                } else {
                    if (removeItem(player, player.getAttribute<Any>("xp_reward_item") as Item)) {
                        if (lamp == Lamps.GENIE_LAMP) {
                            rewardXP(player, skill, (getStatLevel(player, skill) * 10).toDouble())
                        } else {
                            rewardXP(player, skill, lamp.experience.toDouble())
                            if (lamp.item in
                                intArrayOf(
                                    Items.TOME_OF_XP_3_9656,
                                    Items.TOME_OF_XP_2_9657,
                                    Items.TOME_OF_XP_1_9658,
                                    Items.TOME_OF_XP_2ND_ED_3_13160,
                                    Items.TOME_OF_XP_2ND_ED_2_13161,
                                    Items.TOME_OF_XP_2ND_ED_1_13162,
                                )
                            ) {
                                player.dialogueInterpreter.sendPlainMessage(
                                    false,
                                    BLUE + "You read a fascinating chapter and earn experience!",
                                    "You have been awarded " + lamp.experience + " " + Skills.SKILL_NAME[skill] +
                                        " experience!",
                                )
                            } else {
                                player.dialogueInterpreter.sendPlainMessage(
                                    false,
                                    BLUE + "Your wish has been granted!",
                                    "You have been awarded " + lamp.experience + " " + Skills.SKILL_NAME[skill] +
                                        " experience!",
                                )
                            }
                        }
                    }
                }
            }
            player.interfaceManager.open(
                Component(Components.STATS_ADVANCEMENT_134).setCloseEvent { _: Player?, _: Component? ->
                    player.interfaceManager.openDefaultTabs()
                    removeAttribute(player, "xp_reward_item")
                    unlock(player)
                    return@setCloseEvent true
                },
            )
            removeTabs(player, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13)
            return@on true
        }
    }
}
