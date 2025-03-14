package content.region.morytania.quest.deal.dialogue

import content.region.morytania.quest.deal.cutscene.ZombiePirateProtestingCutscene
import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class CaptainBrainDeathDialogueFile(
    var qs: Int,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val check = if (!player!!.isMale) "lass" else "lad"
        qs = getQuestStage(player!!, "Rum Deal")
        npc = NPC(NPCs.CAPTAIN_BRAINDEATH_2827)
        when (qs) {
            1 ->
                when (stage) {
                    0 -> npc(FaceAnim.HALF_ASKING, "Are ye alright, $check?").also { stage++ }
                    1 -> player(FaceAnim.DISGUSTED_HEAD_SHAKE, "Ohhhh... My head...").also { stage++ }
                    2 ->
                        player(
                            FaceAnim.DISGUSTED_HEAD_SHAKE,
                            "It feels like someone has smacked me one with a bottle...",
                        ).also { stage++ }
                    3 ->
                        npc(
                            FaceAnim.SAD,
                            "Arr... Those devils gave ye a nasty knock when ye",
                            "came to aid us.",
                        ).also { stage++ }
                    4 ->
                        npc(
                            FaceAnim.HAPPY,
                            "But now yer here we'll run those evil brain-eatin' dogs",
                            "off the island fer good!",
                        ).also {
                            stage++
                        }
                    5 -> player(FaceAnim.HALF_ASKING, "What? What is going on here?").also { stage++ }
                    6 ->
                        player(
                            FaceAnim.SAD,
                            "I can't seem to remember anything beyond chatting to",
                            "a man at the docks.",
                        ).also {
                            stage++
                        }
                    7 ->
                        npc(
                            FaceAnim.SUSPICIOUS,
                            "Arr. Well, $check, that would be Pete, one of my men.",
                        ).also { stage++ }
                    8 ->
                        npc(
                            "He's been out lookin' fer heroes like yerself to aid us in",
                            "our peril.",
                        ).also { stage++ }
                    9 ->
                        npc(
                            FaceAnim.SUSPICIOUS,
                            "When ye arrived ye took a nasty knock to the head, so",
                            "ye probably don't remember agreein' to help us out.",
                            "But I swear to ye that ye did.",
                        ).also {
                            stage++
                        }
                    10 -> player("Okay... I'll buy that. It sounds like something I would", "do.").also { stage++ }
                    11 -> player(FaceAnim.CALM_TALK, "So where am I, and what is going on?").also { stage++ }
                    12 -> npc(FaceAnim.NEUTRAL, "Yer on Braindeath Island!").also { stage++ }
                    13 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "Where it lies is a secret, because ye are standin' in the",
                            "brewery of Cap'n Braindeath, purveyor of the most",
                            "vitriolic alcoholic beverages in the world!",
                        ).also {
                            stage++
                        }
                    14 -> player(FaceAnim.CALM, "Wow!").also { stage++ }
                    15 ->
                        npc(
                            FaceAnim.HAPPY,
                            "I am the notorious alchemist Cap'n Braindeath, and this",
                            "whole operation be my idea!",
                        ).also {
                            stage++
                        }
                    16 ->
                        npc(
                            FaceAnim.HAPPY,
                            "With my crew of sturdy, upright pirate brewers, we sail",
                            "the seven seas, distributing cheap 'alcohol' to all and",
                            "sundry.",
                        ).also {
                            stage++
                        }
                    17 -> npc(FaceAnim.HAPPY, "Well, fer a price, at any rate.").also { stage++ }
                    18 -> player(FaceAnim.CALM, "Oooh!").also { stage++ }
                    19 ->
                        npc(
                            FaceAnim.HALF_CRYING,
                            "These be dark times, though, $check. See, a week ago we",
                            "awoke to find ourselves beseiged.",
                        ).also {
                            stage++
                        }
                    20 ->
                        npc(
                            FaceAnim.HALF_CRYING,
                            "The lads and I have held them off so far, but 'tis only",
                            "a matter of time before they sweep through the buildin'",
                            "and put us all to the sword.",
                        ).also {
                            stage++
                        }
                    21 -> player(FaceAnim.HALF_ASKING, "Who?").also { stage++ }
                    22 -> npc(FaceAnim.ANGRY, "Them!").also { stage++ }
                    23 -> sendDialogue(player!!, "The Captain points out of the window...").also { stage++ }
                    24 -> {
                        lock(player!!, 3)
                        lockInteractions(player!!, 3)
                        ZombiePirateProtestingCutscene(player!!).start()
                    }
                }

            2 ->
                when (stage) {
                    0 -> player(FaceAnim.HALF_THINKING, "Are they...").also { stage++ }
                    1 -> player(FaceAnim.HALF_THINKING, "...protesting?").also { stage++ }
                    2 -> npc(FaceAnim.ANGRY, "Arr, $check! That they are!").also { stage++ }
                    3 ->
                        npc(
                            FaceAnim.ANGRY,
                            "Day and night they seek to break our will with their",
                            "chantin', and their singin' and their passive resistance!",
                        ).also {
                            stage++
                        }
                    4 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "Seems they lost their fightin' spirit after the first few",
                            "days. Now most of them just protest all the time.",
                        ).also {
                            stage++
                        }
                    5 -> player(FaceAnim.HALF_ASKING, "So, what do you want me to do?").also { stage++ }
                    6 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "Well, me and the lads got our heads together and",
                            "decided that if we can get their Cap'n drunk enough,",
                            "perhaps they'll stop protestin'.",
                        ).also {
                            stage++
                        }
                    7 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "If that happens, we'll slip out the back and set up shop",
                            "somewhere else.",
                        ).also {
                            stage++
                        }
                    8 -> player(FaceAnim.NEUTRAL, "Well, how can I help?").also { stage++ }
                    9 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "Well, first of all, we need someone to go out the front",
                            "and grow us some Blindweed.",
                        ).also {
                            stage++
                        }
                    10 -> npc(FaceAnim.NEUTRAL, "'Tis one of the ingredients of our 'rum'.").also { stage++ }
                    11 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "The only problem is that those rottin' fiends have torn",
                            "up and destroyed all but one of the Blindweed Patches.",
                        ).also {
                            stage++
                        }
                    12 ->
                        if (freeSlots(player!!) == 0) {
                            npc(
                                "Well, I have some Blindweed seeds fer ye. When ye have",
                                "some free space fer them, come and talk to me.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                        } else {
                            npc(
                                FaceAnim.NEUTRAL,
                                "Here, $check. I'll give ye the seed you'll need fer growin'",
                                "the herb. Help yerself to the gardenin' equipment in the",
                                "basement.",
                            ).also {
                                stage++
                            }
                            addItem(player!!, Items.BLINDWEED_SEED_6710)
                            stage++
                        }
                    13 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "I'll warn ye again that those devils are sat right on top",
                            "of the patch.",
                        ).also {
                            stage++
                        }
                    14 ->
                        npc(
                            "Try hecklin' 'em from a distance. Those Swabs may",
                            "talk a good fight, but if ye can put a scare in 'em",
                            "they'll keep out of yer way!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
    }
}
