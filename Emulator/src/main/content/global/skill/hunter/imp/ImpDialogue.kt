package content.global.skill.hunter.imp

import core.api.openInterface
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class ImpDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.IMP_708)
        when (stage) {
            0 -> playerl(FaceAnim.HALF_ASKING, "Hey imp, are you still there?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Of course I can hear ya, ya great big ape. You know, 'veres not even enuf space to swing Bob about in 'ere. How about a breather? You know, stretch me pins for a bit?",
                ).also { stage++ }

            2 ->
                options(
                    "No, I'm going to keep you in there.",
                    "It's not that bad.",
                    "Don't I get three wishes?",
                ).also { stage++ }

            3 ->
                when (buttonID) {
                    1 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "No, I'm going to keep you in there. I might keep you as a pet.",
                        ).also { stage++ }

                    2 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "It's not that bad. You've got four big windows, charming company...er...",
                        ).also { stage = 11 }

                    3 -> playerl(FaceAnim.ASKING, "Don't I get three wishes?").also { stage = 13 }
                }

            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Pet!! Nah mate. I fink you'd find dat you'd be my pet!! We is not makin good pets.",
                ).also { stage++ }

            5 -> playerl(FaceAnim.HALF_GUILTY, "Really? Why not?").also { stage++ }
            6 -> npcl(FaceAnim.HALF_GUILTY, "Coz...errr...").also { stage++ }
            7 -> npcl(FaceAnim.HALF_GUILTY, "We bite! Yeah we is biting and...and...er...").also { stage++ }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "We is fire risk! Yeah dat's it! We be burning down your housey and stealin' all ya shiny gems. Oh, and da beads!! Mmmm, beads.",
                ).also { stage++ }

            9 -> playerl(FaceAnim.HALF_GUILTY, "Fire risk? How does that work?").also { stage++ }
            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Is those wizzies. Dey don't like de imps so dey make us go BOOOM!!",
                ).also { stage = END_DIALOGUE }

            11 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Yeah, we's love tiny, crampt space. It be magical. But I is a busy imp, innit? Dragons needin' ticklin', shiny relics needin' stealin', you know how it goes.",
                ).also { stage++ }

            12 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "So, if you's know whas good for ya, you'd be lettin' me go, right?",
                ).also { stage = 2 }

            13 -> npcl(FaceAnim.HALF_GUILTY, "Nah, mate. Dunno what you're chirpin' about.").also { stage++ }
            14 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Well, you're a magical creature aren't you? Surely I get some wishes for capturing you, or releasing you, or something?",
                ).also { stage++ }

            15 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I'm finking dat you be a bit confoosed. I is an imp, not some namby-pamby genie or some kinda fairy. Ye can tell by the horns.",
                ).also { stage++ }

            16 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Sayin' dat, I don't fancy being cooped up like one of me uncle's pigeons. Tell you what, is there anything you need deliverin' to the bank?",
                ).also { stage++ }

            17 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I may not be no cunjerer or sommink like dat, but I can get about nice",
                ).also { stage++ }

            18 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "an quick like. If you let me scarper, I'll take a couple of fings to the bank for ya. You game?",
                ).also { stage++ }

            19 ->
                options(
                    "Okay, that sounds fair.",
                    "Surely it should be three items?",
                    "I've got nothing I need banking right now.",
                ).also { stage++ }

            20 ->
                when (buttonID) {
                    1 ->
                        playerl(FaceAnim.HALF_GUILTY, "Okay, that sounds fair.").also {
                            end()
                            openInterface(
                                player!!,
                                478,
                            )
                        }

                    2 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "Surely it should be three items? Then it's one item per wish.",
                        ).also { stage++ }

                    3 -> playerl(FaceAnim.ASKING, "I've got nothing I need banking right now.").also { stage = 22 }
                }

            21 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I've already told ya, I ain't no bloomin' fairy. Besides, you know wot dey say, three's a crowd innit? I don't fink I can hop about carryin' more dan 2 fings.",
                ).also { stage = 19 }

            22 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Great, just blinkin great, dat is. I'll just sit about countin' zombie sheep then. One...two...two and a bit...three and a bit more... I don't fink sheep 'ave dat many legs...",
                ).also { stage = END_DIALOGUE }
        }
    }
}

class ImpDialogueExtension : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.IMP_708)
        when (stage) {
            0 -> playerl(FaceAnim.HALF_ASKING, "Hey imp, can you hear me?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "An who's fault is that, eh? EH? I got betta fings to do dan sit ere waitin' for ya. 'Ave ya made up ya mind what else I'm luggin about?",
                ).also { stage++ }

            2 -> options("Yes I have.", "No, I'm afraid you're going to have to wait a bit longer.").also { stage++ }
            3 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.HAPPY, "Yes I have.").also { stage = 5 }
                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "No, I'm afraid you're going to have to wait a bit longer.",
                        ).also { stage++ }
                }

            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Dat's alright guv'nor. I'll get back to me luvverly dream about beads. Big shiny beads. Love it!",
                ).also { stage = END_DIALOGUE }

            5 -> end().also { openInterface(player!!, 478) }
        }
    }
}
