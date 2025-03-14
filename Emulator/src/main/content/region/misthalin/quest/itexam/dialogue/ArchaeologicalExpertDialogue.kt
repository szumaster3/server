package content.region.misthalin.quest.itexam.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ArchaeologicalExpertDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (getQuestStage(player, Quests.DESERT_TREASURE)) {
            0 -> npcl(FaceAnim.FRIENDLY, "Hello, are you Terry Balando?")
            2 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "There you go, that book contains the sum of my translating ability.",
                ).also { stage = 11 }

            in 3..15 -> npcl(FaceAnim.FRIENDLY, "Hello again.").also { stage = 14 }
            else -> npcl(FaceAnim.FRIENDLY, "Hello. Who are you?").also { stage = 16 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "That's right. I was in the desert down by the Bedabin Camp, and I found an archaeologist who asked me to deliver this to you.",
                ).also { stage++ }

            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You spoke to the legendary Asgarnia Smith??? Quickly, let me see what he had to give you!",
                ).also { stage++ }

            2 ->
                playerl(FaceAnim.ASKING, "So what does the inscription say? Anything interesting?").also {
                    if (inInventory(player, Items.ETCHINGS_4654)) {
                        removeItem(player, Items.ETCHINGS_4654)
                    }
                    stage++
                }

            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "This... this is fascinating! These cuneiform's seem to predate even the settlement we are excavating here... Yes, yes, this is most interesting indeed!",
                ).also { stage++ }

            4 -> playerl(FaceAnim.ASKING, "Can you translate it for me?").also { stage++ }
            5 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Well, I am not familiar with this particular language, but",
                    "the similarities inherent in the pictographs seem to show",
                    "a prevalent trend towards a syllabary consistent with",
                    "the phonemes we have discovered in this excavation!",
                ).also { stage++ }

            6 -> playerl(FaceAnim.ASKING, "Um... So, can you translate it for me or not?").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, unfortunately this is the only example of this particular language I have ever seen, but I might be able to make a rough translation, of sorts...",
                ).also { stage++ }

            8 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "It might be slightly obscure on the finer details, but it should be good enough to understand the rough meaning of what was originally written.",
                ).also { stage++ }

            9 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Please, just wait a moment, I will write up what I can translate into a journal for you.",
                ).also { stage++ }

            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    " Then you can take it back to Asgarnia, I think he will be extremely interested in the translation!",
                ).also {
                    setQuestStage(player, Quests.DESERT_TREASURE, 2)
                    end()
                }

            11 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "If you would be so kind as to take that back to Asgarnia, I think it will reassure him that he is on the right track for a find of great archaeological importance!",
                ).also { stage++ }

            12 -> playerl(FaceAnim.FRIENDLY, "Wow! You write really quickly don't you?").also { stage++ }
            13 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "What can I say? It's a skill I picked up through my many years of taking field notes!",
                ).also {
                    addItemOrDrop(player, Items.TRANSLATION_4655)
                    setQuestStage(player, Quests.DESERT_TREASURE, 3)
                    end()
                }

            14 -> npcl(FaceAnim.FRIENDLY, "Was that translation any use to Asgarnia?").also { stage++ }
            15 -> playerl(FaceAnim.FRIENDLY, "I think it was, thanks!").also { stage = END_DIALOGUE }
            16 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Good day to you. My name is Terry Balando, I am an expert archaeologist. I am employed by Varrock Museum to oversee all finds at this site. Anything you find must be reported to me.",
                ).also { stage++ }

            17 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Oh, okay. If I find anything of interest I will bring it here.",
                ).also { stage++ }

            18 -> npcl(FaceAnim.FRIENDLY, "Can I help you at all?").also { stage++ }
            19 ->
                options(
                    "I have something I need checking out.",
                    "No thanks.",
                    "Can you tell me anything about the digsite?",
                    "Can you tell me more about the tools an archaeologist uses?",
                ).also { stage++ }

            20 ->
                when (stage) {
                    1 -> playerl(FaceAnim.FRIENDLY, "I have something I need checking out.").also { stage = 100 }
                    2 -> playerl(FaceAnim.FRIENDLY, "No thanks.").also { stage = 200 }
                    3 -> playerl(FaceAnim.FRIENDLY, "Can you tell me anything about the digsite?").also { stage = 300 }
                    4 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Can you tell me more about the tools an archaeologist uses?",
                        ).also { stage = 400 }
                }

            100 ->
                npcl(FaceAnim.FRIENDLY, "Okay, give it to me and I'll have a look for you.").also {
                    stage = END_DIALOGUE
                }

            200 ->
                npcl(FaceAnim.FRIENDLY, "Good, let me know if you find anything unusual.").also {
                    stage = END_DIALOGUE
                }

            300 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes, indeed! I am studying the lives of the settlers. During the end of the Third Age, there used to be a great city at the site.",
                ).also { stage++ }

            301 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Its inhabitants were humans, supporters of the god Saradomin.",
                ).also { stage++ }

            302 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "It's not recorded what happened to the community here. I suspect nobody has lived here for over a millennium!",
                ).also { stage = END_DIALOGUE }

            400 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Of course! Let's see now... Trowels are vital for fine digging work, so you can be careful to not damage or disturb any artefacts. Rock picks are for splitting rocks or scraping away soil.",
                ).also { stage++ }

            401 -> playerl(FaceAnim.FRIENDLY, "What about specimen jars and brushes?").also { stage++ }
            402 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Those are essential for carefully cleaning and storing smaller samples.",
                ).also { stage++ }

            403 -> playerl(FaceAnim.FRIENDLY, "Where can I get any of these things?").also { stage++ }
            404 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, we've come into a bit more funding of late, so there should be a stock of each of them in the Exam Centre's tools cupboard.",
                ).also { stage++ }

            405 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "We also hand out relevant tools as students complete each level of their Earth Sciences exams.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ARCHAEOLOGICAL_EXPERT_619)
    }
}
