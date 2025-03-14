package content.region.asgarnia.dialogue.burthope

import core.api.openDialogue
import core.api.quest.isQuestComplete
import core.api.quest.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Dunstan dialogue.
 */
@Initializable
class DunstanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        /*
         * When Troll Stronghold is complete
         */
        if (isQuestComplete(player!!, Quests.TROLL_STRONGHOLD)) {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "Hi! What can I do for you?").also { stage++ }
                2 ->
                    showTopics(
                        Topic(FaceAnim.THINKING, "Is it OK if I use your anvil?", 10),
                        Topic(FaceAnim.FRIENDLY, "Nothing, thanks.", END_DIALOGUE),
                        Topic(FaceAnim.FRIENDLY, "How is your son getting on?", 15),
                        /*
                         * Sleds topic when Troll Romance is implemented.
                         */
                    )

                10 -> npcl(FaceAnim.FRIENDLY, "So you're a smithy are you?").also { stage++ }
                11 -> playerl(FaceAnim.FRIENDLY, "I dabble.").also { stage++ }
                12 -> npcl(FaceAnim.FRIENDLY, "A fellow smith is welcome to use my anvil!").also { stage++ }

                13 -> playerl(FaceAnim.FRIENDLY, "Thanks!").also { stage++ }
                14 -> npcl(FaceAnim.FRIENDLY, "Anything else before I get on with my work?").also { stage = 2 }

                15 ->
                    npcl(
                        FaceAnim.FRIENDLY,
                        "He is getting on fine! He has just been promoted to Sergeant! I'm really proud of him!",
                    ).also {
                        stage++
                    }
                16 -> playerl(FaceAnim.FRIENDLY, "I'm happy for you!").also { stage++ }
                17 -> npcl(FaceAnim.FRIENDLY, "Anything else before I get on with my work?").also { stage = 2 }
            }
            return true
        }

        /*
         * Troll Stronghold in progress.
         */
        if (isQuestInProgress(player!!, Quests.TROLL_STRONGHOLD, 1, 99)) {
            openDialogue(player!!, DunstanDialogue(), npc)
            return true
        }

        /*
         * When Death Plateau is complete.
         */
        if (isQuestComplete(player!!, Quests.DEATH_PLATEAU)) {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "Hi! What can I do for you?").also { stage++ }
                2 ->
                    showTopics(
                        Topic(FaceAnim.THINKING, "Is it OK if I use your anvil?", 10),
                        Topic(FaceAnim.FRIENDLY, "Nothing, thanks.", END_DIALOGUE),
                        Topic(FaceAnim.FRIENDLY, "How is your son getting on?", 15),
                    )
                10 -> npcl(FaceAnim.FRIENDLY, "So you're a smithy are you?").also { stage++ }
                11 -> playerl(FaceAnim.FRIENDLY, "I dabble.").also { stage++ }
                12 -> npcl(FaceAnim.FRIENDLY, "A fellow smith is welcome to use my anvil!").also { stage++ }
                13 -> playerl(FaceAnim.FRIENDLY, "Thanks!").also { stage++ }
                14 -> npcl(FaceAnim.FRIENDLY, "Anything else before I get on with my work?").also { stage = 2 }
                15 ->
                    npcl(
                        FaceAnim.SAD,
                        "He was captured by those cursed trolls! I don't know what to do. Even the imperial guard are too afraid to go rescue him.",
                    ).also {
                        stage++
                    }
                16 -> playerl(FaceAnim.ASKING, "What happened?").also { stage++ }
                17 ->
                    npcl(
                        FaceAnim.SAD,
                        "Talk to Denulth, he can tell you all about it. Anything else before I get on with my work?",
                    ).also {
                        stage =
                            2
                    }
            }
            return true
        }

        /*
         * Death Plateau in progress.
         */
        if (isQuestInProgress(player!!, Quests.DEATH_PLATEAU, 21, 24)) {
            openDialogue(player!!, DunstanDialogue(), npc)
            return true
        }

        /*
         * Default.
         */
        when (stage) {
            START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "Hi! Did you want something?").also { stage++ }
            2 ->
                showTopics(
                    Topic(FaceAnim.THINKING, "Is it OK if I use your anvil?", 10),
                    Topic(FaceAnim.FRIENDLY, "Nothing, thanks.", END_DIALOGUE),
                )
            10 -> npcl(FaceAnim.FRIENDLY, "So you're a smithy are you?").also { stage++ }
            11 -> playerl(FaceAnim.FRIENDLY, "I dabble.").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "A fellow smith is welcome to use my anvil!").also { stage++ }
            13 -> playerl(FaceAnim.FRIENDLY, "Thanks!").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "Anything else before I get on with my work?").also { stage = 2 }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DUNSTAN_1082)
    }
}
