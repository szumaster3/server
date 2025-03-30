package content.global.skill.smithing.special

import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Items

/**
 * The type Dragon shield dialogue.
 */
@Initializable
class DragonShieldDialogue : Dialogue {

    constructor()

    constructor(player: Player?) : super(player)

    override fun newInstance(player: Player): Dialogue {
        return DragonShieldDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        if (!inInventory(player, Items.HAMMER_2347, 1)) {
            sendDialogue(player, "You need a hammer to work the metal with.")
            return false
        }

        val type = args[0] as Int
        if (type == 1) {
            if (!(inInventory(player, Items.SHIELD_RIGHT_HALF_2368, 1) && inInventory(
                    player,
                    Items.SHIELD_LEFT_HALF_2366,
                    1
                ))
            ) {
                sendDialogue(player, "You need the other half of the shield.")
                return false
            }
            sendDialogueLines(
                player,
                "You set to work trying to fix the ancient shield. It's seen some",
                "heavy action and needs some serious work doing to it."
            )
            stage = 0
        } else {
            if (!inInventory(player, Items.ANTI_DRAGON_SHIELD_1540, 1)) {
                sendDialogue(player, "You need an anti-dragon shield to attach the visage to.")
                return false
            }
            if (!inInventory(player, Items.DRACONIC_VISAGE_11286, 1)) {
                sendDialogue(player, "You don't have anything you could attach to the shield.")
                return false
            }

            sendDialogueLines(
                player,
                "You set to work, trying to attach the ancient draconic",
                "visage to your anti-dragonbreath shield. It's not easy to",
                "work with the ancient artifact and it takes all of your",
                "skills as a master smith."
            )
            stage = 10
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                lock(player, 5)
                animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)
                if (removeItem(player, Items.SHIELD_RIGHT_HALF_2368, Container.INVENTORY) && removeItem(
                        player,
                        Items.SHIELD_LEFT_HALF_2366,
                        Container.INVENTORY
                    )
                ) {
                    sendDialogueLines(
                        player,
                        "Even for an experienced armourer it is not an easy task, but",
                        "eventually it is ready. You have restored the dragon square shield to",
                        "its former glory."
                    )
                    addItem(player, Items.DRAGON_SQ_SHIELD_1187, 1, Container.INVENTORY)
                    rewardXP(player, Skills.SMITHING, 75.0)
                }
                stage = 1
            }

            1 -> end()

            10 -> {
                lock(player, 5)
                animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)
                if (removeItem(player, Items.ANTI_DRAGON_SHIELD_1540, Container.INVENTORY) && removeItem(
                        player,
                        Items.DRACONIC_VISAGE_11286,
                        Container.INVENTORY
                    )
                ) {
                    sendDialogueLines(
                        player,
                        "Even for an experienced armourer it is not an easy task, but",
                        "eventually it is ready. You have crafted the",
                        "draconic visage and anti-dragonbreath shield into a",
                        "dragonfire shield."
                    )
                    addItem(player, Items.DRAGONFIRE_SHIELD_11284, 1, Container.INVENTORY)
                    rewardXP(player, Skills.SMITHING, 2000.0)
                }
                stage = 1
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(82127843)
    }
}
