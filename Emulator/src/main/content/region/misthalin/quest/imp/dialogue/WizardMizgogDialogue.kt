package content.region.misthalin.quest.imp.dialogue

import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.utils.PlayerCamera
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import org.rs.consts.*

class WizardMizgogDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (getQuestStage(player!!, Quests.IMP_CATCHER)) {
            0 ->
                when (stage) {
                    0 -> player("Give me a quest!").also { stage++ }
                    1 -> npc("Give me a quest what?").also { stage++ }
                    2 ->
                        options(
                            "Give me a quest please.",
                            "Give me a quest or else!",
                            "Just stop messing around and give me a quest!",
                        ).also {
                            stage++
                        }

                    3 ->
                        when (buttonID) {
                            1 -> player("Give me a quest please.").also { stage = 10 }
                            2 -> player("Give me a quest or else!").also { stage = 20 }
                            3 -> player("Just stop messing around and give me a quest!").also { stage = 30 }
                        }

                    10 -> npc("Well seeing as you asked nicely... I could do with some", "help.").also { stage++ }
                    11 ->
                        npc(
                            "The wizard Grayzag next door decided he didn't like",
                            "me so he enlisted an army of hundreds of imps.",
                        ).also { stage++ }

                    12 ->
                        npc(
                            "The imps stole all sorts of my things. Most of these",
                            "things I don't really care about, just eggs and balls of",
                            "string and things.",
                        ).also { stage++ }

                    13 ->
                        npc(
                            "But they stole my four magical beads. There was a red",
                            "one, a yellow one, a black one, and a white one.",
                        ).also { stage++ }

                    14 ->
                        npc(
                            "These imps have now spread out all over the kingdom.",
                            "Could you get my beads back for me?",
                        ).also { stage++ }

                    15 -> options("I'll try.", "I've better things to do than chase imps.").also { stage++ }
                    16 ->
                        when (buttonID) {
                            1 -> player("I'll try.").also { stage++ }
                            2 -> player("I've better things to do than chase imps.").also { stage = END_DIALOGUE }
                        }

                    17 -> npc("That's great, thank you.").also { stage++ }
                    18 -> {
                        end()
                        setQuestStage(player!!, Quests.IMP_CATCHER, 10)
                    }

                    20 -> npc("Or else what? You'll attack me?").also { stage++ }
                    21 -> npc(FaceAnim.LAUGH, "Hahaha!").also { stage = END_DIALOGUE }
                    30 -> npc("Ah now you're assuming I have one to give.").also { stage = END_DIALOGUE }
                }

            10 ->
                when (stage) {
                    0 -> npc("So how are you doing finding my beads?").also { stage++ }
                    1 ->
                        if (!player!!.inventory.containsItem(Item(1476)) &&
                            !player!!.inventory.containsItem(Item(1470)) &&
                            !player!!.inventory.containsItem(
                                Item(1472),
                            ) &&
                            !player!!.inventory.containsItem(Item(1474))
                        ) {
                            player("I've not found any yet.")
                            stage = 2
                        } else if (player!!.inventory.containItems(1474, 1470, 1472, 1476)) {
                            player("I've got all four beads. It was hard work I can tell you.")
                            stage = 5
                        } else {
                            player("I have found some of your beads.")
                            stage = 3
                        }

                    2 ->
                        npc(
                            "Well get on with it. I've lost a white bead, a read bead, a",
                            "black bead, and a yellow bead. Go kill some imps!",
                        ).also { stage = END_DIALOGUE }

                    3 ->
                        npc(
                            "Come back when you have them all. The colour of the",
                            "four beads that I need are red, yellow, black, and white.",
                            "Go chase some imps!",
                        ).also { stage = END_DIALOGUE }

                    4 ->
                        npc(
                            "Give them here and I'll check that they really are MY",
                            "beads, before I give you your reward. You'll like it, it's",
                            "an amulet of accuracy.",
                        ).also { stage++ }

                    5 -> sendDialogueLines(player!!, "You give four coloured beads to Wizard Mizgog.").also { stage++ }
                    6 -> {
                        if (player!!.inventory.remove(
                                Item(Items.RED_BEAD_1470),
                                Item(Items.YELLOW_BEAD_1472),
                                Item(Items.BLACK_BEAD_1474),
                                Item(Items.WHITE_BEAD_1476),
                            )
                        ) {
                            end()
                            lock(player!!, 100)
                            lockInteractions(player!!, 100)
                            PlayerCamera(player).panTo(3103, 3161, 750, 100)
                            PlayerCamera(player).rotateTo(3103, 3161, 4000, 100)
                            player!!.pulseManager.run(
                                object : Pulse(1) {
                                    var counter = 0

                                    override fun pulse(): Boolean {
                                        when (counter++) {
                                            1 -> {
                                                findLocalNPC(player!!, NPCs.WIZARD_MIZGOG_706)?.let {
                                                    face(it, location(3102, 3163, 2))
                                                }
                                            }

                                            2 -> {
                                                findLocalNPC(player!!, NPCs.WIZARD_MIZGOG_706)?.let {
                                                    animate(
                                                        it,
                                                        Animation(
                                                            Animations.WIZARD_MIZGOG_COLLECTS_BEADS_IMP_CATCHER_4285,
                                                        ),
                                                    )
                                                }
                                                playGlobalAudio(player!!.location, Sounds.MIZGOG_PLACEBEADS_1632, 20)
                                            }

                                            6 -> {
                                                playGlobalAudio(player!!.location, Sounds.CURSE_LIFT_1634, 50)
                                                replaceScenery(
                                                    Scenery(
                                                        16169,
                                                        Location(3102, 3163, 2),
                                                        10,
                                                        1,
                                                    ),
                                                    16170,
                                                    80,
                                                )
                                            }

                                            7 -> {
                                                sendGraphics(Graphics.MONK_CAST_HEAL_84, Location(3102, 3163, 2))
                                            }

                                            12 -> {
                                                unlock(player!!)
                                                PlayerCamera(player).reset()
                                                sendMessage(player!!, "The Wizard hands you an amulet.")
                                                finishQuest(player!!, Quests.IMP_CATCHER)
                                            }
                                        }
                                        return false
                                    }
                                },
                            )
                        }
                    }
                }
        }
    }
}
