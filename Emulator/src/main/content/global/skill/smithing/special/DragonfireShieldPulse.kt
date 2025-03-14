package content.global.skill.smithing.special

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items

class DragonfireShieldPulse(
    player: Player?,
    val item: Item,
) : SkillPulse<Item>(player, null) {
    private var tick = 0

    override fun checkRequirements(): Boolean {
        if (!inInventory(player, Items.ANTI_DRAGON_SHIELD_1540)) {
            sendDialogue(player, "You need to have an anti-dragon-shield to attach the visage onto.")
            return false
        }
        if (!inInventory(player, Items.DRACONIC_VISAGE_11286)) {
            sendDialogue(player, "You need to have a draconic visage so it can be attached on a shield.")
            return false
        }
        return true
    }

    override fun start() {
        super.start()
    }

    override fun animate() {
    }

    override fun reward(): Boolean {
        when (tick++) {
            0 -> {
                lock(player, 10)
                animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)
                tick++
            }

            5 -> {
                animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)
                tick++
            }

            9 -> {
                sendPlainDialogue(
                    player,
                    false,
                    "Even for an experienced armourer it is not an easy task, but",
                    "eventually it is ready. You have crafted the",
                    "draconic visage and anti-dragonbreath shield into a",
                    "dragonfire shield.",
                )
                addDialogueAction(player) { player, button ->
                    if (button >= 2) {
                        if (removeItem(player, Items.ANTI_DRAGON_SHIELD_1540) &&
                            removeItem(
                                player,
                                Items.DRACONIC_VISAGE_11286,
                            )
                        ) {
                            addItem(player, Items.DRAGONFIRE_SHIELD_11284, 1)
                            rewardXP(player, Skills.SMITHING, 2000.0)
                        }
                    }
                    return@addDialogueAction
                }
            }
        }
        return false
    }

    override fun message(type: Int) {}
}
