package content.region.karamja.quest.mm.dialogue

import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import org.rs.consts.Quests

class MonkeyDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> playerl("Hello there, little money.").also { stage++ }
            1 -> npcl("Hello there!").also { stage++ }
            2 -> playerl("How would you like to get out of here?").also { stage++ }
            3 -> npcl("Escape!? It's all I ever think about!").also { stage++ }
            4 -> playerl("That's convenient. When would you like to leave?").also { stage++ }
            5 -> npcl("Where will you be taking me?").also { stage++ }
            6 -> playerl("Erm ... to happy, sunny jungle of Karamja...").also { stage++ }
            7 -> npcl("Wowee! I was born there you know!").also { stage++ }
            8 -> playerl("That's nice. Are you ready to go?").also { stage++ }
            9 -> npcl("Yes. Actually, can I bring some of my friends?").also { stage++ }
            10 -> playerl("No. I only have space for one.").also { stage++ }
            11 -> npcl("Pleeeeease?").also { stage++ }
            12 -> playerl("No!").also { stage++ }
            13 -> npcl("Pretty pleeeeease?").also { stage++ }
            14 -> playerl("No!!").also { stage++ }
            15 -> npcl("Pretty please with a banana on top?").also { stage++ }
            16 -> playerl("Look, I already said no. If you want to come then jump into my backpack.").also { stage++ }
            17 -> npcl("Ook!").also { stage++ }
            18 -> {
                end()
                setQuestStage(player!!, Quests.MONKEY_MADNESS, 40)
            }
        }
    }
}
