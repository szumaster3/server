package content.region.asgarnia.dialogue.burthope

import content.region.asgarnia.quest.death.dialogue.DenulthDialogueFile
import core.api.openDialogue
import core.api.quest.isQuestComplete
import core.api.quest.isQuestInProgress
import core.api.quest.setQuestStage
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
 * Represents the Denulth dialogue.
 */
@Initializable
class DenulthDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        /*
         * When Troll Stronghold is complete.
         */
        if (isQuestComplete(player!!, Quests.TROLL_STRONGHOLD)) {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hello!").also { stage++ }
                1 -> npcl(FaceAnim.HAPPY, "Welcome back friend!").also { stage++ }
                2 ->
                    showTopics(
                        Topic("How goes your fight with the trolls?", 10),
                        Topic("I thought the White Knights controlled Asgarnia?", 20),
                        Topic(FaceAnim.HAPPY, "See you about Denulth!", 30),
                    )
                10 ->
                    npcl(
                        FaceAnim.FRIENDLY,
                        "We are busy preparing for an attack by night. Godric knows of a secret entrance to the stronghold. Once we destroy the stronghold Burthorpe will be safe! Friend, we are indebted to you!",
                    ).also {
                        stage++
                    }

                11 -> playerl(FaceAnim.FRIENDLY, "Good luck!").also { stage = END_DIALOGUE }
                20 ->
                    npcl(
                        FaceAnim.ANGRY,
                        "You are right citizen. The White Knights have taken advantage of the old and weak king, they control most of Asgarnia, including Falador. However they do not control Burthorpe!",
                    ).also {
                        stage++
                    }
                21 ->
                    npcl(
                        FaceAnim.EVIL_LAUGH,
                        "We are the prince's elite troops! We keep Burthorpe secure!",
                    ).also { stage++ }
                22 ->
                    npcl(
                        FaceAnim.ANGRY,
                        "The White Knights have overlooked us until now! They are pouring money into their war against the Black Knights, They are looking for an excuse to stop our funding and I'm afraid they may have found it!",
                    ).also {
                        stage++
                    }
                23 ->
                    npcl(
                        FaceAnim.HALF_WORRIED,
                        "If we can not destroy the troll camp on Death Plateau then the Imperial Guard will be disbanded and Burthorpe will come under control of the White Knights. We cannot let this happen!",
                    ).also {
                        stage++
                    }
                24 -> npcl(FaceAnim.ASKING, "Is there anything else you'd like to know?").also { stage = 2 }
                30 -> npcl(FaceAnim.FRIENDLY, "Saradomin be with you, friend!").also { stage = END_DIALOGUE }
            }
            return true
        }

        /*
         * Troll Stronghold in progress.
         */
        if (isQuestInProgress(player!!, Quests.TROLL_STRONGHOLD, 1, 99)) {
            openDialogue(player!!, DenulthDialogue(), npc)
            return true
        }

        /*
         * When Death Plateau is completed, start Troll Stronghold.
         */
        if (isQuestComplete(player!!, Quests.DEATH_PLATEAU)) {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hello!").also { stage++ }
                1 -> npcl(FaceAnim.HAPPY, "Welcome back friend!").also { stage++ }
                2 ->
                    showTopics(
                        Topic("How goes your fight with the trolls?", 10),
                        Topic("I thought the White Knights controlled Asgarnia?", 20),
                        Topic(FaceAnim.HAPPY, "See you about Denulth!", 30),
                    )
                10 ->
                    npcl(
                        FaceAnim.HALF_WORRIED,
                        "I'm afraid I have bad news. We made our attack as planned, but we met unexpected resistance.",
                    ).also {
                        stage++
                    }
                11 -> playerl(FaceAnim.HALF_ASKING, "What happened?").also { stage++ }
                12 ->
                    npcl(
                        FaceAnim.WORRIED,
                        "We were ambushed by trolls coming from the north. They captured Dunstan's son, Godric, who we enlisted at your request; we tried to follow but we were repelled at the foot of their stronghold.",
                    ).also {
                        stage++
                    }
                13 ->
                    showTopics(
                        Topic(FaceAnim.SAD, "I'm sorry to hear that.", END_DIALOGUE),
                        Topic("Is there anything I can do to help?", 14),
                    )
                14 ->
                    npcl(
                        FaceAnim.HALF_WORRIED,
                        "The way to the stronghold is treacherous, friend. Even if you manage to climb your way up, there will be many trolls defending the stronghold.",
                    ).also {
                        stage++
                    }
                15 ->
                    showTopics(
                        Topic("I'll get Godric back!", 16),
                        Topic("I'm sorry, there's nothing I can do.'", END_DIALOGUE),
                    )
                16 ->
                    npcl(
                        FaceAnim.HAPPY,
                        "God speed friend! I would send some of my men with you, but none of them are brave enough to follow.",
                    ).also {
                        end()
                        setQuestStage(player!!, Quests.TROLL_STRONGHOLD, 1)
                    }
                20 ->
                    npcl(
                        FaceAnim.ANGRY,
                        "You are right citizen. The White Knights have taken advantage of the old and weak king, they control most of Asgarnia, including Falador. However they do not control Burthorpe!",
                    ).also {
                        stage++
                    }
                21 ->
                    npcl(
                        FaceAnim.EVIL_LAUGH,
                        "We are the prince's elite troops! We keep Burthorpe secure!",
                    ).also { stage++ }
                22 ->
                    npcl(
                        FaceAnim.ANGRY,
                        "The White Knights have overlooked us until now! They are pouring money into their war against the Black Knights, They are looking for an excuse to stop our funding and I'm afraid they may have found it!",
                    ).also {
                        stage++
                    }
                23 ->
                    npcl(
                        FaceAnim.HALF_WORRIED,
                        "If we can not destroy the troll camp on Death Plateau then the Imperial Guard will be disbanded and Burthorpe will come under control of the White Knights. We cannot let this happen!",
                    ).also {
                        stage++
                    }
                24 -> npcl(FaceAnim.ASKING, "Is there anything else you'd like to know?").also { stage = 2 }
                30 -> npcl(FaceAnim.FRIENDLY, "Saradomin be with you, friend!").also { stage = END_DIALOGUE }
            }
            return true
        }
        openDialogue(player!!, DenulthDialogueFile(), npc)
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DENULTH_1060)
    }
}
