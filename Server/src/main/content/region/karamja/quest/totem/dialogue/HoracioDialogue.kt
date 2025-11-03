package content.region.karamja.quest.totem.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Vars

/**
 * Represents the Horacio dialogue.
 */
@Initializable
class HoracioDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        if(getQuestStage(player, Quests.BACK_TO_MY_ROOTS) >= 1) {
            npc("How goes the hunt, brave gardener?").also { stage = 27 }
        } else {
            npcl(FaceAnim.HAPPY, "It's a fine day to be out in a garden, isn't it?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val afterTribalTotemQuest = isQuestComplete(player, Quests.TRIBAL_TOTEM)
        val quest = player.getQuestRepository().getQuest(Quests.BACK_TO_MY_ROOTS)
        when (stage) {
            0 -> showTopics(
                Topic("Yes it's very nice.", 1, false),
                Topic("So... who are you?",2, false)
            )
            1 -> npcl(FaceAnim.HAPPY, "Days like these make me glad to be alive!").also { stage = END_DIALOGUE }
            2 -> {
                npc(FaceAnim.FRIENDLY, "My name is Horacio Dobson. I'm the gardener to Lord", "Handlemort. Take a look around this beautiful garden,", "all of this is my handiwork.").also{
                    stage = if(afterTribalTotemQuest) 3 else END_DIALOGUE
                }
            }
            3 -> {
                showTopics(
                    Topic("So... do you garden round the back too?", 4, false),
                    Topic("Do you need any help?", 10, false),
                )
            }
            4 -> npcl(FaceAnim.HAPPY, "That I do!").also { stage++ }
            5 -> playerl(FaceAnim.ASKING, "Doesn't all of the security around the house get in your way then?").also { stage++ }
            6 -> npcl(FaceAnim.HAPPY, "Ah. I'm used to all that. I have my keys, the guard dogs know me, and I know the combination to the door lock.").also { stage++ }
            7 -> npcl(FaceAnim.HAPPY, "It's rather easy, it's his middle name.").also { stage++ }
            8 -> playerl(FaceAnim.ASKING, "Whose middle name?").also { stage++ }
            9 -> npcl(FaceAnim.ANNOYED, "Hum. I probably shouldn't have said that. Forget I mentioned it.").also { stage = END_DIALOGUE }
            10 -> if(!quest.hasRequirements(player)) {
                npc(FaceAnim.HAPPY, "Actually, now you mention it, yes... but you're not", "experienced enough to help me just yet.").also { stage = END_DIALOGUE }
                sendMessage(player, "Check your quest journal for the requirements to start the Back to my Roots quest.")
            } else if (isQuestComplete(player, Quests.BACK_TO_MY_ROOTS)) {
                npc(FaceAnim.HAPPY, "You've done more than enough to help.", "Hope you're enjoying your vine patch!").also { stage = END_DIALOGUE }
            } else {
                npc(FaceAnim.HAPPY, "Actually, now you mention it, yes... I'm going to", "improve the garden around the house. Would you be", "willing to help me?").also { stage++ }
            }
            11 -> showTopics(
                Topic("Sure, I enjoy a bit of gardening.", 12, false),
                Topic("No thanks, I don't like getting my hands dirty.", END_DIALOGUE, false),
            )
            12 -> npc("Well, let's see now. I'm reworking the beds and have", "marked out two special patches as you can see, they're", "a bit weedy at the moment, though. I'm sure Lord", "Handlemort will appreciate the beauty of what I have").also { stage++ }
            13 -> npc("planned there. It may even cheer him up a little.").also { stage++ }
            14 -> player(FaceAnim.HALF_ASKING, "What's wrong with him?").also { stage++ }
            15 -> npc(FaceAnim.SAD, "One of his treasures was stolen...").also { stage++ }
            16 -> player(FaceAnim.NOD_NO, "Oh...err...I see. There are some...nasty people around", "these days.").also { stage++ }
            17 -> npc(FaceAnim.FRIENDLY, "Indeed there are. Still, life isn't always a bed of roses is", "it? Back to the root of the problem: I need a very rare", "plant... and I think you can get it for me.").also { stage++ }
            18 -> player(FaceAnim.HALF_ASKING, "Oh? What plant would that be? A magic tree?").also { stage++ }
            19 -> npc(FaceAnim.NEUTRAL, "Oh no, no, no. Nothing so mundane! It's a vine, you", "see...").also { stage++ }
            20 -> player(FaceAnim.HALF_ASKING, "What sort of vine?").also { stage++ }
            21 -> npc(FaceAnim.NEUTRAL, "One that only grows wild in one place on Karamja just", "east of Shilo Village... at least, that's what I've heard from", "other gardeners. It's called the Jade Vine.").also { stage++ }
            22 -> player(FaceAnim.HALF_ASKING, "Oh, right. So what's the problem? Why don't you just", "go and get it?").also { stage++ }
            23 -> npc(FaceAnim.SAD, "I tried... and failed lots of times. So has Garth - the", "farmer on Karamja - he knows quite a bit about the", "vine. You see, because it's so delicate, the cutting is", "very difficult to keep alive for very long. I have an").also { stage++ }
            24 -> npc(FaceAnim.SAD, "idea, though: go talk to that mad Wizard Cromperty. He", "has been boasting recently that he has discovered", "preservation magic. I'm not sure I believe him, though.").also { stage++ }
            25 -> player(FaceAnim.HAPPY, "Okay, I'm off to see the wizard... so long as he's not", "going to teleport me places again, we should be fine!").also { stage++ }
            26 -> npc(FaceAnim.FRIENDLY, "That would be excellent!").also {
                // Unlocks: Crmoperty dialogue
                setQuestStage(player, Quests.BACK_TO_MY_ROOTS, 1)
                setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 1, true)
                stage = END_DIALOGUE
            }

            27 -> playerl(FaceAnim.FRIENDLY, "Well... simply de vine... I haven't got the Jade Vine cutting yet. In fact... I've forgotten what I'm doing.").also { stage++ }
            28 -> npcl(FaceAnim.FRIENDLY, "Oh dear! You were getting a special Jade Vine plant for me, but to protect it you'll need to talk to the Wizard Cromperty first.").also { stage++ }
            29 -> player(FaceAnim.HAPPY, "Aha! I remember now.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.HORACIO_845)
}
