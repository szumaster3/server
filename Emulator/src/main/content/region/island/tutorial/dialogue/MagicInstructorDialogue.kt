package content.region.island.tutorial.dialogue

import content.region.island.tutorial.plugin.TutorialStage
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.DARK_BLUE
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class MagicInstructorDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            67 -> playerl(FaceAnim.FRIENDLY, "Hello.")
            69 -> npc(
                FaceAnim.FRIENDLY,
                "Good. This is a list of your spells. Currently you can",
                "only cast one offensive spell called Wind Strike. Let's",
                "try it out on one of those chickens.",
            )

            70 -> {
                if (!inInventory(player, Items.AIR_RUNE_556) && !inInventory(player, Items.MIND_RUNE_558)) {
                    if (freeSlots(player) == 0 || freeSlots(player) < 2) {
                        GroundItemManager.create(
                            arrayOf(Item(Items.AIR_RUNE_556, 15), Item(Items.MIND_RUNE_558, 15)),
                            player.location,
                            player,
                        )
                    } else {
                        addItem(player, Items.AIR_RUNE_556, 15)
                        addItem(player, Items.MIND_RUNE_558, 15)
                    }
                    sendDoubleItemDialogue(
                        player,
                        Items.AIR_RUNE_556,
                        Items.MIND_RUNE_558,
                        "You receive some spare runes.",
                    )
                    return false
                }
            }
            71 -> npc(FaceAnim.FRIENDLY, "Well you're all finished here now. I'll give you a", "reasonable number of runes when you leave.")
            else -> return false
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            67 -> when (stage++) {
                0 -> npc(
                    FaceAnim.FRIENDLY,
                    "Good day, newcomer. My name is Terrova. I'm here",
                    "to tell you about$DARK_BLUE Magic</col>. Let's start by opening your",
                    "spell list.",
                )

                1 -> {
                    end()
                    setAttribute(player, TutorialStage.TUTORIAL_STAGE, 68)
                    TutorialStage.load(player, 68)
                }
            }

            69 -> when (stage++) {
                0 -> {
                    sendDoubleItemDialogue(
                        player,
                        Items.AIR_RUNE_556,
                        Items.MIND_RUNE_558,
                        "Terrova gives you five <col=08088A>air runes</col> and five <col=08088A>mind runes</col>!",
                    )
                    addItemOrDrop(player, Items.AIR_RUNE_556, 5)
                    addItemOrDrop(player, Items.MIND_RUNE_558, 5)
                }

                1 -> {
                    end()
                    setAttribute(player, TutorialStage.TUTORIAL_STAGE, 70)
                    TutorialStage.load(player, 70)
                }
            }

            70 -> {
                if (!inInventory(player!!, Items.AIR_RUNE_556)) {
                    sendItemDialogue(
                        player,
                        Items.AIR_RUNE_556,
                        "Terrova gives you five <col=08088A>air runes</col>!",
                    )
                    addItemOrDrop(player, Items.AIR_RUNE_556, 5)
                    TutorialStage.rollback(player)
                    return true
                }
                if (!inInventory(player!!, Items.MIND_RUNE_558)) {
                    addItemOrDrop(player, Items.MIND_RUNE_558, 5)
                    sendItemDialogue(
                        player,
                        Items.MIND_RUNE_558,
                        "Terrova gives you five <col=08088A>mind runes</col>!",
                    )
                    TutorialStage.rollback(player)
                    return true
                }
            }

            71 -> when (stage) {
                0 -> {
                    setTitle(player, 2)
                    sendDialogueOptions(
                        player,
                        "Do you want to go to the mainland?",
                        "Yes.",
                        "No.",
                    ).also { stage++ }
                }

                1 -> when (buttonId) {
                    1 -> playerl(FaceAnim.NEUTRAL, "I'm ready to go now, thank you.").also { stage++ }
                    2 -> playerl(FaceAnim.NEUTRAL, "No.").also { stage = END_DIALOGUE }
                }

                2 -> npcl(
                    FaceAnim.HAPPY,
                    "Good good. I've deactivated the protective spells around the island so now you can teleport yourself out of here",
                ).also {
                    stage++
                }

                3 -> sendNPCDialogue(
                    player,
                    npc.id,
                    "When you get to the mainland you will find yourself in the town of Lumbridge. If you want some ideas on, where to go next, talk to my friend Phileas, also known as the Lumbridge Guide. You can't miss him; he's",
                ).also {
                    stage++
                }

                4 -> npcl(
                    FaceAnim.HAPPY,
                    "holding a big staff with a question mark on the end. He also has a white beard and carries a rucksack full of scrolls. There are also tutors willing to teach you about the many skills you could learn.",
                ).also { stage++ }

                5 -> player.dialogueInterpreter.sendItemMessage(
                    Items.NULL_5079,
                    "When you get to Lumbridge, look for this icon on your minimap. The Lumbridge Guide and the other tutors will be standing near one of these. The Lumbridge Guide should be standing slightly to the north-east of",
                ).also {
                    stage++
                }

                6 -> sendItemDialogue(
                    player,
                    Items.NULL_5079,
                    "the castle's courtyard and the others you will find, scattered around Lumbridge.",
                ).also {
                    stage++
                }

                7 -> npcl(
                    FaceAnim.HAPPY,
                    "If all else fails, visit the " + GameWorld.settings!!.name + " website for a whole chestload of information on quests skills and minigames as well as a very good starter's guide.",
                ).also { stage++ }

                8 -> {
                    end()
                    setAttribute(player, TutorialStage.TUTORIAL_STAGE, 72)
                    TutorialStage.load(player, 72)
                }
            }
        }

        return true
    }

    override fun newInstance(player: Player?): Dialogue = MagicInstructorDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.MAGIC_INSTRUCTOR_946)
}
