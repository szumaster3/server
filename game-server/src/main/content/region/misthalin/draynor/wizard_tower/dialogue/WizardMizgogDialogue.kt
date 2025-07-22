package content.region.misthalin.draynor.wizard_tower.dialogue

import content.region.misthalin.draynor.wizard_tower.quest.imp.ImpCatcher
import core.api.*
import core.api.openNpcShop
import core.api.allInInventory
import core.api.*
import core.api.utils.PlayerCamera
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.*

/**
 * # Relations [Imp Catcher quest][content.region.misthalin.quest.imp.ImpCatcher]
 */
@Initializable
class WizardMizgogDialogue(player: Player? = null) : Dialogue(player) {


    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.IMP_CATCHER) == 1) {
            npc(FaceAnim.ASKING, "So how are you doing finding my beads?").also { stage = 15 }
        } else {
            npc(FaceAnim.CALM_TALK, "What can I do for you, adventurer?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> showTopics(
                IfTopic(FaceAnim.HAPPY, "Give me a quest!", 1, getQuestStage(player, Quests.IMP_CATCHER) == 0),
                IfTopic(FaceAnim.HALF_ASKING, "Got any more quests?", 13, isQuestComplete(player, Quests.IMP_CATCHER)),
                IfTopic(FaceAnim.HALF_ASKING, "Do you have any more amulets of accuracy?", 21, isQuestComplete(player, Quests.IMP_CATCHER), true),
                Topic(FaceAnim.HAPPY,"Do you know any interesting spells you could teach me?", 14, false)
            )

            1 -> npc(FaceAnim.ASKING, "Give me a quest what?").also { stage++ }
            2 -> showTopics(
                Topic(FaceAnim.HALF_GUILTY, "Give me a quest please.", 3),
                Topic(FaceAnim.FRIENDLY, "Give me a quest or else!", 10),
                Topic(FaceAnim.HAPPY, "Just stop messing around and give me a quest!", 12),
            )

            3 -> npc(FaceAnim.CALM, "Well seeing as you asked nicely... I could do with some", "help.").also { stage++ }
            4 -> npc(FaceAnim.HALF_GUILTY, "The wizard Grayzag next door decided he didn't like", "me so he enlisted an army of hundreds of imps.").also { stage++ }
            5 -> npc(FaceAnim.HALF_GUILTY, "The imps stole all sorts of my things. Most of these", "things I don't really care about, just eggs and balls of", "string and things.").also { stage++ }
            6 -> npc(FaceAnim.HALF_GUILTY, "But they stole my four magical beads. There was a red", "one, a yellow one, a black one, and a white one.").also { stage++ }
            7 -> npc(FaceAnim.HALF_GUILTY, "These imps have now spread out all over the kingdom.", "Could you get my beads back for me?").also { stage++ }

            8 -> showTopics(
                Topic(FaceAnim.HAPPY,"I'll try.", 9),
                Topic(FaceAnim.NEUTRAL,"I've better things to do than chase imps.", END_DIALOGUE)
            )

            9 -> {
                end()
                npc(FaceAnim.FRIENDLY, "That's great, thank you.")
                setQuestStage(player, Quests.IMP_CATCHER, 1)
            }

            10 -> npc(FaceAnim.LAUGH, "Or else what? You'll attack me?").also { stage++ }
            11 -> npc(FaceAnim.LOUDLY_LAUGHING, "Hahaha!").also { stage = END_DIALOGUE }
            12 -> npc(FaceAnim.ROLLING_EYES, "Ah now you're assuming I have one to give.").also { stage = END_DIALOGUE }
            13 -> npc(FaceAnim.NOD_NO,"No, everything is good with the world today.").also { stage = END_DIALOGUE }
            14 -> npc(FaceAnim.HALF_THINKING, "I don't think so, the type of magic I study involves", "years of meditation and research.").also { stage = END_DIALOGUE }
            15 -> {
                when {
                    anyInInventory(player, *ImpCatcher.beads) && !allInInventory(player, *ImpCatcher.beads) -> {
                        player(FaceAnim.HALF_GUILTY, "I have found some of your beads.").also { stage = 17 }
                    }
                    allInInventory(player, *ImpCatcher.beads) -> {
                        player(FaceAnim.FRIENDLY, "I've got all four beads. It was hard work I can tell you.").also { stage = 18 }
                    }
                    else -> {
                        player(FaceAnim.SAD, "I've not found any yet.").also { stage++ }
                    }
                }
            }
            16 -> npc(FaceAnim.HALF_GUILTY, "Well get on with it. I've lost a white bead, a read bead, a", "black bead, and a yellow bead. Go kill some imps!").also { stage = END_DIALOGUE }
            17 -> npc(FaceAnim.NEUTRAL,"Come back when you have them all. The colour of the", "four beads that I need are red, yellow, black, and white.", "Go chase some imps!").also { stage = END_DIALOGUE }
            18 -> npc(FaceAnim.HAPPY, "Give them here and I'll check that they really are MY", "beads, before I give you your reward. You'll like it, it's", "an amulet of accuracy.").also { stage++ }
            19 -> sendDialogueLines(player!!, "You give four coloured beads to Wizard Mizgog.").also { stage++ }
            20 -> {
                end()
                if (!player.inventory.removeAll(ImpCatcher.beads))
                    return false

                lock(player, 12)
                PlayerCamera(player).panTo(3103, 3161, 750, 100)
                PlayerCamera(player).rotateTo(3103, 3161, 4000, 100)
                player!!.pulseManager.run(
                    object : Pulse(1) {
                        var counter = 0
                        override fun pulse(): Boolean {
                            when (counter++) {
                                1 -> {
                                    face(findLocalNPC(player!!, NPCs.WIZARD_MIZGOG_706)!!, location(3102, 3163, 2))
                                }
                                2 -> {
                                    animate(findLocalNPC(player!!, NPCs.WIZARD_MIZGOG_706)!!, Animations.WIZARD_MIZGOG_COLLECTS_BEADS_IMP_CATCHER_4285)
                                    playAudio(player!!, Sounds.MIZGOG_PLACEBEADS_1632, 0, 0, player.location)
                                }
                                6 -> {
                                    playAudio(player!!, Sounds.CURSE_LIFT_1634, 0, 0, player.location)
                                    replaceScenery(
                                        toReplace = Scenery(
                                            org.rs.consts.Scenery.TABLE_16169,
                                            Location(3102, 3163, 2),
                                            10,
                                            1
                                        ),
                                        with = org.rs.consts.Scenery.TABLE_16170,
                                        forTicks = 80
                                    )
                                }
                                7 -> sendGraphics(Graphics.MONK_CAST_HEAL_84, Location(3102, 3163, 2))
                                12 -> {
                                    PlayerCamera(player).reset()
                                    sendMessage(player!!, "The Wizard hands you an amulet.")
                                    finishQuest(player!!, Quests.IMP_CATCHER)
                                }
                            }
                            return false
                        }
                    }
                )
            }

            21 -> {
                end()
                openNpcShop(player, npc.id)
            }

        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = WizardMizgogDialogue(player)
    override fun getIds(): IntArray = intArrayOf(NPCs.WIZARD_MIZGOG_706)
}
