package content.region.kandarin.quest.biohazard.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class GuidorDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.GUIDOR_343)
        when (stage) {
            0 ->
                if (getQuestStage(player!!, Quests.BIOHAZARD) >= 16) {
                    player("Hello again Guidor.").also { stage = 100 }
                } else {
                    player("Hello, you must be Guidor. I understand that you are", "unwell.").also { stage++ }
                }

            1 ->
                npc(
                    FaceAnim.ANNOYED,
                    "Is my wife asking priests to visit me now? I'm a man",
                    "of science for god's sake. ",
                ).also { stage++ }

            2 ->
                npc(
                    "Ever since she heard rumors of a plague carrier",
                    "travelling from Ardougne she's kept me under house",
                    "arrest.",
                ).also { stage++ }

            3 ->
                npc(
                    "Of course she means well, and I am quite frail now...",
                    "So what brings you here?",
                ).also { stage++ }

            4 ->
                options(
                    "I've come to ask your assistance in stopping a plague.",
                    "I was just going to bless your room and I've done that now.",
                ).also { stage++ }

            5 ->
                when (buttonID) {
                    1 ->
                        player(
                            "Well it's funny you should ask actually... I've come to",
                            "ask your assistance in stopping a plague that could kill",
                            "thousands.",
                        ).also { stage++ }

                    2 ->
                        player(
                            "Oh, nothing, I was just going to bless your room",
                            "and I've done that now. Goodbye.",
                        ).also { stage = END_DIALOGUE }
                }

            6 -> npc(FaceAnim.SCARED, "So you're the plague carrier!").also { stage++ }
            7 -> options("No! Well, yes...", "I've been sent by your old pupil Elena.").also { stage++ }
            8 ->
                when (buttonID) {
                    1 ->
                        player(
                            "No! Well, yes... but not exactly. It's",
                            "contained in a sealed unit from Elena.",
                        ).also { stage++ }

                    2 ->
                        player(
                            "I've been sent by your old pupil Elena, she's trying to",
                            "halt the virus.",
                        ).also { stage++ }
                }

            9 -> npc("Elena eh?").also { stage++ }
            10 ->
                player(
                    "Yes, she wants you to analyse it. You might be the",
                    "only one who can help.",
                ).also { stage++ }

            11 -> npc("Right then, sounds like we'd better get to work!").also { stage++ }
            12 -> {
                if (!inInventory(player!!, Items.PLAGUE_SAMPLE_418)) {
                    end()
                    npc(
                        "Seems like you don't actually HAVE the plague sample.",
                        "It's a long way to come empty-handed... and",
                        "quite a long way back to.",
                    )
                } else {
                    player("I have the plague sample.").also { stage++ }
                }
            }

            13 -> npc("Now I'll be needing some liquid honey, some sulphuric", "broline, and then...").also { stage++ }
            14 -> player("... some ethenea?").also { stage++ }
            15 -> npc("Indeed!").also { stage++ }
            16 -> {
                if (!anyInInventory(player!!, Items.SULPHURIC_BROLINE_417) &&
                    !inInventory(
                        player!!,
                        Items.ETHENEA_415,
                    ) &&
                    !inInventory(player!!, Items.LIQUID_HONEY_416) &&
                    !inInventory(player!!, Items.TOUCH_PAPER_419)
                ) {
                    end()
                    npc("Look, I need all three reagents to test the plague", "sample. Come back when you've got them.")
                } else {
                    removeItem(player!!, Items.SULPHURIC_BROLINE_417)
                    removeItem(player!!, Items.ETHENEA_415)
                    removeItem(player!!, Items.LIQUID_HONEY_416)
                    removeItem(player!!, Items.TOUCH_PAPER_419)
                    sendMessage(player!!, "You give him the vials and the touch paper.")
                    npc(
                        "Now I'll just apply these to the sample and... I don't",
                        "get it... the touch paper has remained the same.",
                    )
                    stage = 17
                }
            }

            17 -> options("That's why Elena wanted you to do it.", "So what does that mean exactly?").also { stage++ }
            18 ->
                when (buttonID) {
                    1 ->
                        player(
                            "That's why Elena wanted you to do it, because she",
                            "wasn't sure what was happening.",
                        ).also { stage++ }

                    2 ->
                        player(
                            "Oh, nothing, I was just going to bless your room",
                            "and I've done that now. Goodbye.",
                        ).also { stage = END_DIALOGUE }
                }

            19 -> npc("Well that's just it, nothing has happened.").also { stage++ }
            20 -> npc("I don't know what this sample is, but it certainly isn't", "toxic.").also { stage++ }
            21 -> player("So what about the plague?").also { stage++ }
            22 -> npc(FaceAnim.ANNOYED, "Don't you understand? There is no Plague!").also { stage++ }
            23 ->
                npc(
                    FaceAnim.THINKING,
                    "I'm very sorry, I can see that you've worked very",
                    "hard for this... ... but it seems that someone has been",
                    "lying to you.",
                ).also { stage++ }

            24 -> npc(FaceAnim.THINKING, "The only question is... ... why?").also { stage++ }
            25 -> {
                end()
                setQuestStage(player!!, Quests.BIOHAZARD, 16)
            }

            100 ->
                npc(
                    "Well, hello traveller. I still can't understand",
                    "why they would lie about the plague.",
                ).also { stage++ }

            101 -> player("It's strange, anyway how are you doing?").also { stage++ }
            102 -> npc("I'm hanging in there.").also { stage++ }
            103 -> player("Good for you.").also { stage++ }
            104 -> end()
        }
    }
}
