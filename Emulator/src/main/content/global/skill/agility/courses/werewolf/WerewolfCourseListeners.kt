package content.global.skill.agility.courses.werewolf

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class WerewolfCourseListeners : InteractionListener {
    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, ZIP_LINE, "teeth-grip") { _, _ ->
            return@setDest Location(3528, 9910, 0)
        }
    }

    override fun defineListeners() {
        on(TRAPDOOR, IntType.SCENERY, "open") { player, node ->
            sendMessage(player, "The trapdoor opens...")
            replaceScenery(node.asScenery(), OPEN_TRAPDOOR, -1)
            return@on true
        }

        on(OPEN_TRAPDOOR, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node.asScenery(), TRAPDOOR, -1)
            return@on true
        }

        on(OPEN_TRAPDOOR, IntType.SCENERY, "climb-down") { player, _ ->
            findLocalNPC(player, NPCs.WEREWOLF_1665)?.let { face(player, it) }
            findLocalNPC(player, NPCs.WEREWOLF_1665)?.let { face(it, player, 1) }
            if (!anyInEquipment(player, Items.RING_OF_CHAROS_4202, Items.RING_OF_CHAROSA_6465) ||
                getStatLevel(
                    player,
                    Skills.AGILITY,
                ) < 60
            ) {
                sendNPCDialogueLines(
                    player,
                    NPCs.WEREWOLF_1665,
                    FaceAnim.CHILD_NORMAL,
                    false,
                    "You can't go down there human. If it wasn't my duty",
                    "to guard this trapdoor, I would be relieving you of the",
                    "burden of your life right now.",
                )
            } else {
                sendNPCDialogueLines(
                    player,
                    NPCs.WEREWOLF_1665,
                    FaceAnim.CHILD_NORMAL,
                    false,
                    "Good luck down there, my friend. Remember, to the",
                    "west is the main agility course, while to the east is a",
                    "skullball course.",
                )
                addDialogueAction(player) { player, button ->
                    if (button >= 1) {
                        sendMessage(player!!, "You climb down through the trapdoor.")
                        ClimbActionHandler.climb(
                            player,
                            Animation(Animations.MULTI_BEND_OVER_827),
                            Location(3549, 9865, 0),
                        )
                    }
                }
            }
            return@on true
        }

        on(AGILITY_TRAINER, IntType.NPC, "Give-Stick") { player, _ ->
            if (getAttribute(player, "werewolf-agility-course", false)) return@on true

            if (!inInventory(player, Items.STICK_4179)) {
                openDialogue(player, AgilityTrainerStickDialogue())
                return@on true
            }

            if (amountInInventory(player, Items.STICK_4179) >= 1 || player.inventory.containItems(Items.STICK_4179)) {
                player.inventory.removeAll(Items.STICK_4179)
                rewardXP(player, Skills.AGILITY, 190.0)
                sendMessage(player, "You give the stick to the werewolf.")
            }

            removeAttribute(player, "werewolf-agility-course")
            return@on true
        }
    }

    companion object {
        private const val TRAPDOOR = Scenery.TRAPDOOR_5131
        private const val OPEN_TRAPDOOR = Scenery.TRAPDOOR_5132
        private const val AGILITY_TRAINER = NPCs.AGILITY_TRAINER_1664
        private val ZIP_LINE = intArrayOf(Scenery.ZIP_LINE_5139, Scenery.ZIP_LINE_5140, Scenery.ZIP_LINE_5141)
    }
}
