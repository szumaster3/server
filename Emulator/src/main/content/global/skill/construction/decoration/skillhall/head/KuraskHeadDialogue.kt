package content.global.skill.construction.decoration.skillhall.head

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class KuraskHeadDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.KURASK_4229)
        when (stage) {
            0 -> npcl(FaceAnim.CHILD_FRIENDLY, "I KILL YOU!!!").also { stage++ }
            1 ->
                if (!player!!.houseManager.isInHouse(player!!)) {
                    player(FaceAnim.FRIENDLY, "No, House owner kill you!").also { stage = END_DIALOGUE }
                } else {
                    player(FaceAnim.FRIENDLY, "No, I kill you!").also { stage++ }
                }

            2 -> npcl(FaceAnim.CHILD_NORMAL, "UUUHRG! Now I kill you!").also { stage++ }
            3 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "How are you going to do that? You're just a head on a wall!",
                ).also { stage++ }

            4 -> npcl(FaceAnim.CHILD_FRIENDLY, "Uhhhhrrr...").also { stage++ }
            5 ->
                options(
                    "Why are you so violent?",
                    "What do you think about up there?",
                    "I killed you really easily!",
                ).also { stage++ }

            6 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Why are you so violent?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "What do you think about up there?").also { stage = 23 }
                    3 -> playerl(FaceAnim.HAPPY, "I killed you really easily!").also { stage = 18 }
                }

            7 -> npcl(FaceAnim.CHILD_FRIENDLY, "You kill me! Uuurgh! That make me angry!").also { stage++ }
            8 ->
                options(
                    "You seemed pretty angry before I killed you.",
                    "I'm sorry I killed you.",
                    "I killed you really easily!",
                ).also { stage++ }

            9 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY, "You seemed pretty angry before I killed you.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "I'm sorry I killed you.").also { stage = 11 }
                    3 -> playerl(FaceAnim.HAPPY, "I killed you really easily!").also { stage = 18 }
                }

            10 -> npcl(FaceAnim.CHILD_FRIENDLY, "I like angry!").also { stage = END_DIALOGUE }
            11 -> npcl(FaceAnim.CHILD_FRIENDLY, "I hate sorry! Makes me more angry! WANT TO KILL YOU!").also { stage++ }
            12 -> options("Please try to calm down.", "I'm not really sorry.").also { stage++ }
            13 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Please try to calm down!").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "I'm not really sorry!").also { stage = 15 }
                }

            14 -> npcl(FaceAnim.CHILD_FRIENDLY, "Hate calm! Smash it! Hur hur hur!").also { stage = END_DIALOGUE }
            15 -> npcl(FaceAnim.CHILD_FRIENDLY, "That make me more angry! Uuuurgh!").also { stage++ }
            16 -> playerl(FaceAnim.FRIENDLY, "Is there anything that doesn't make you angry?").also { stage++ }
            17 -> npcl(FaceAnim.CHILD_FRIENDLY, "No! I like angry! Hur hur hur!").also { stage = END_DIALOGUE }
            18 -> npcl(FaceAnim.CHILD_FRIENDLY, "Uhhhhrrr...").also { stage++ }
            19 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Yeah! I could kill you again in my sleep! I think I might go off and kill some other kurask!",
                ).also { stage++ }

            20 -> npcl(FaceAnim.CHILD_FRIENDLY, "Uuuurrrrh! Hate you!").also { stage++ }
            21 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "What are you going to do about it? Eh? I totally owned you!",
                ).also { stage++ }

            22 -> npcl(FaceAnim.CHILD_FRIENDLY, "Hate you hate you hate you!!!").also { stage = END_DIALOGUE }
            23 -> playerl(FaceAnim.FRIENDLY, "What do you think about up there?").also { stage++ }
            24 -> npcl(FaceAnim.CHILD_FRIENDLY, "Think?").also { stage++ }
            25 -> playerl(FaceAnim.FRIENDLY, "You know, what goes through your tiny stuffed head?").also { stage++ }
            26 -> npcl(FaceAnim.CHILD_FRIENDLY, "Little bugs...").also { stage++ }
            27 -> playerl(FaceAnim.FRIENDLY, "You have bugs living in you? Eww!").also { stage++ }
            28 -> npcl(FaceAnim.CHILD_FRIENDLY, "Little bugs! Stomp and crush and stomp!").also { stage++ }
            29 ->
                options(
                    "Yeah! Stomp the bugs!",
                    "What have the bugs done to you?",
                    "You can't, you've got no feet!",
                ).also { stage++ }

            30 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yeah! Stomp the bugs!").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "What have the bugs done to you?").also { stage = 38 }
                    3 -> player(FaceAnim.FRIENDLY, "You can't, you've got no feet!").also { stage = 40 }
                }

            31 -> npcl(FaceAnim.CHILD_FRIENDLY, "Stomp crush splat!").also { stage++ }
            32 ->
                npcl(
                    FaceAnim.CHILD_FRIENDLY,
                    "Smash! Destroy! Crunch break tear destroy splunch! Hurt wound kill hit punch stab slash kill!",
                ).also { stage++ }

            33 ->
                options(
                    "'Splunch'? That's not a word!",
                    "You said 'kill' twice.",
                    "Yeah! Kill smash destroy!",
                ).also { stage++ }

            34 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY, "'Splunch'? That's not a word!").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "You said 'kill' twice.").also { stage = 36 }
                    3 -> player(FaceAnim.FRIENDLY, "Yeah! Kill smash destroy!").also { stage = 37 }
                }

            35 -> npcl(FaceAnim.CHILD_FRIENDLY, "I HATE WORDS! Kill all words!").also { stage = END_DIALOGUE }
            36 -> npcl(FaceAnim.CHILD_FRIENDLY, "I like kill! Hur hur hur hur!").also { stage = END_DIALOGUE }
            37 -> npcl(FaceAnim.CHILD_FRIENDLY, "Kill smash destroy! Hur hur hur!").also { stage = END_DIALOGUE }
            38 ->
                npcl(
                    FaceAnim.CHILD_FRIENDLY,
                    "Skitter skitter through head noise in ears behind eyes.",
                ).also { stage++ }

            39 -> npcl(FaceAnim.CHILD_FRIENDLY, "HATE THEM! Kill kill kill!").also { stage = END_DIALOGUE }
            40 -> playerl(FaceAnim.FRIENDLY, "You can't, you've got no feet!").also { stage++ }
            41 -> npcl(FaceAnim.CHILD_FRIENDLY, "No feet...").also { stage++ }
            42 ->
                npcl(
                    FaceAnim.CHILD_FRIENDLY,
                    "Hate lack of feet! Stomp lack of feet! Kill crush destroy smash!",
                ).also { stage++ }

            43 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "That makes no sense! You can't destroy the absence of something!",
                ).also { stage++ }

            44 ->
                npcl(
                    FaceAnim.CHILD_FRIENDLY,
                    "Hate requirement to make sense! Smash it kill it destroy kill kill!",
                ).also { stage++ }

            45 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "You can't physically destroy an abstract concept! It's impossible!",
                ).also { stage++ }

            46 ->
                npcl(
                    FaceAnim.CHILD_FRIENDLY,
                    "Hate abstract concepts! Hate impossible! Kill kill kill destroy smash!",
                ).also { stage++ }

            47 ->
                playerl(FaceAnim.FRIENDLY, "This is getting both surreal and repetitive.").also {
                    stage = END_DIALOGUE
                }
        }
    }
}
