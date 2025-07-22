package content.region.karamja.quest.mm.dialogue

import core.api.addItemOrDrop
import core.api.sendItemDialogue
import core.api.setAttribute
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import org.rs.consts.Items

class MonkeyChildFirstDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int, ) {
        when (stage) {
            0 -> playerl("Hello there little monkey.").also { stage++ }
            1 -> npcl(FaceAnim.OLD_NEUTRAL, "Hello big-big. Aunty told me not to talk to strangers.").also { stage++ }
            2 -> playerl("Oh I'm not a stranger...").also { stage++ }
            3 -> npcl(FaceAnim.OLD_NEUTRAL, "You look strange to me!").also {
                end()
                setAttribute(player!!, "/save:mm:first-talk", true)
            }
        }
    }
}

class MonkeyChildSecondDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int, ) {
        when (stage) {
            0 -> playerl("Hello again little monkey.").also { stage++ }
            1 -> npcl(FaceAnim.OLD_NEUTRAL, "You're stranger. Go away!").also { stage++ }
            2 -> playerl("I'm not a stranger...!").also { stage++ }
            3 -> npcl(FaceAnim.OLD_NEUTRAL, "Then what are you?").also { stage++ }
            4 -> options("I'm a monkey brother!", "I knew your sister's mother!", "I'm just a monkey lover!", "Well,I'll be a monkey's uncle!").also { stage++ }
            5 -> when (buttonID) {
                1 -> {}
                2 -> {}
                3 -> {}
                4 -> npcl(FaceAnim.OLD_NEUTRAL, "Uh ah! You do look like my uncle!").also {
                    end()
                    setAttribute(player!!, "/save:mm:second-talk", true)
                }
            }
        }
    }
}

class MonkeyChildThirdDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int, ) {
        when (stage) {
            0 -> npcl(FaceAnim.OLD_NEUTRAL, "You look a lot bigger than last time, Uncle!").also { stage++ }
            1 -> playerl("I've been ... eating more bananas... just like you should be!").also { stage++ }
            2 -> npcl(FaceAnim.OLD_NEUTRAL, "I'm bored.").also { stage++ }
            3 -> playerl("Why are you bored?").also { stage++ }
            4 -> npcl(FaceAnim.OLD_NEUTRAL, "Aunty told me to pick loads of bananas. She said if, I got bananas, she'd give me a new toy!").also { stage++ }
            5 -> options("What kind of toy did Aunty say she'd give you?", "How many bananas did aunty want?").also { stage++ }
            6 -> when (buttonID) {
                1 -> {}
                2 -> playerl("How many bananas did aunty want?").also { stage++ }
            }
            7 -> npcl(FaceAnim.OLD_NEUTRAL, "Twenty! But I can't count! It's very mean of her, isn't it Uncle?").also { stage++ }
            8 -> playerl("Yes very mean ... do you want me to get the bananas for you?").also { stage++ }
            9 -> npcl(FaceAnim.OLD_NEUTRAL, "Ok! Ook Ook!").also { stage++ }
            10 -> playerl("But only if you promise to let me borrow your toy...").also { stage++ }
            11 -> npcl(FaceAnim.OLD_NEUTRAL, "Ok Uncle!").also {
                end()
                setAttribute(player!!, "/save:mm:third-talk", true)
            }
        }
    }
}

class MonkeyChildBananasDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npcl(FaceAnim.OLD_NEUTRAL, "Did you get any bananas, Uncle?").also { stage++ }
            1 -> playerl("Yes, I have some here.").also { stage++ }
            2 -> npcl(FaceAnim.OLD_NEUTRAL, "Wow that's a lot of bananas! Are there twenty?").also { stage++ }
            3 -> playerl("Yes, of course there are.").also { stage++ }
            4 -> sendItemDialogue(player!!, Items.BANANA_1963, "You give monkey child your bananas.").also {
                setAttribute(player!!, "/save:mm:talk-banana", true)
                stage++
            }

            5 -> npcl(FaceAnim.OLD_NEUTRAL, "aunty will be so happy!").also {
                stage++
            }
        }
    }
}

class MonkeyChildTalismanDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player("Has Aunty given you the toy yet?").also { stage++ }
            1 -> npcl(FaceAnim.OLD_NEUTRAL, "Yeah - it's really neat!").also { stage++ }
            2 -> player("Can i borrow it now then?").also { stage++ }
            3 -> npcl(FaceAnim.OLD_NEUTRAL, "But I only just got it!").also { stage++ }
            4 -> player("Please?").also { stage++ }
            5 -> npcl(FaceAnim.OLD_NEUTRAL, "Ok then...").also { stage++ }
            6 -> sendItemDialogue(player!!, Items.MONKEY_TALISMAN_4023, "The monkey child gives you some kind of talisman.").also { stage++ }
            7 -> {
                end()
                addItemOrDrop(player!!, Items.MONKEY_TALISMAN_4023)
            }
        }
    }
}
