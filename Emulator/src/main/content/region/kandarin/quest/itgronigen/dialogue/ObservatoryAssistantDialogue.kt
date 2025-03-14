package content.region.kandarin.quest.itgronigen.dialogue

import core.api.inInventory
import core.api.quest.setQuestStage
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class ObservatoryAssistantDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.OBSERVATORY_ASSISTANT_6118)
        when (stage) {
            0 -> playerl(FaceAnim.HALF_ASKING, "Hi, are you busy?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Me? I'm always busy. See that man there? That's the professor. If he had his way, I think he'd never let me sleep! Anyway, how might I help you?",
                ).also { stage++ }

            2 ->
                options(
                    "I was wondering what you do here.",
                    "Just looking around, thanks.",
                    "Can I have a look through that telescope?",
                ).also { stage++ }

            3 ->
                when (buttonID) {
                    1 -> player("I was wondering what you do here.").also { stage++ }
                    2 -> player("Just looking around, thanks.").also { stage = 8 }
                    3 -> player("Can I have a look through that telescope?").also { stage = 10 }
                }

            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Glad you ask. This is the Observatory reception. Up on the cliff is the Observatory dome, from which you can view the heavens. Usually...",
                ).also { stage++ }

            5 -> player("What do you mean, 'usually'?").also { stage++ }
            6 -> npc("*Ahem*. Back to work, please.").also { stage++ }
            7 -> npc("I'd speak with the professor. He'll explain.").also { stage = 2 }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Okay, just don't break anything. If you need any help, let me know.",
                ).also { stage++ }

            9 -> {
                end()
                sendDialogue(player!!, "The assistant continues with his work.")
            }

            10 -> npcl(FaceAnim.HALF_GUILTY, "You can. You won't see much though.").also { stage++ }
            11 -> player("And that's because?").also { stage++ }
            12 -> npc("Just talk to the professor. He'll fill you in.").also { stage++ }
            13 -> {
                end()
                setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 1)
            }
        }
    }
}

class AssistantTalkAboutPlanksDialogue(
    val planks: Int,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.OBSERVATORY_ASSISTANT_6118)
        when (planks) {
            0 ->
                when (stage) {
                    0 -> player("Hello there.").also { stage++ }
                    1 -> npc("Yes?").also { stage++ }
                    2 ->
                        playerl(
                            "Where can I find planks of wood? I need some for the telescope's base.",
                        ).also { stage++ }
                    3 -> npcl("I understand planks can be found at Port Khazard, to the east of here.").also { stage++ }
                    4 ->
                        npcl(
                            "There are some at the Barbarian Outpost, too. Failing that, you could always ask the Sawmill Operator. He's to the north-east of Varrock, by the Lumber Yard.",
                        ).also {
                            stage = END_DIALOGUE
                        }
                }

            1, 2 ->
                when (stage) {
                    0 -> player("Might I have a word?").also { stage++ }
                    1 -> npcl("Sure, how can I help you?").also { stage++ }
                    2 -> player("I've got a plank.").also { stage++ }
                    3 -> npcl("That's nice.").also { stage++ }
                    4 -> playerl("You know, for the telescope's base.").also { stage++ }
                    5 -> npcl("Well done. Remember that you'll need three in total.").also { stage = END_DIALOGUE }
                }

            3 ->
                when (stage) {
                    0 -> player("Can I speak with you?").also { stage++ }
                    1 -> npcl("Why, of course. What is it?").also { stage++ }
                    2 -> playerl("I've got some planks for the telescope's base.").also { stage++ }
                    3 -> npcl("Good work! The professor has been eagerly waiting them.").also { stage++ }
                    4 -> {
                        end()
                        setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 3)
                    }
                }
        }
    }
}

class AssistantTalkAboutBronzeBarDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.OBSERVATORY_ASSISTANT_6118)
        when (stage) {
            0 -> player("Can I speak with you?").also { stage++ }
            1 -> npc("Why, of course. What is it?").also { stage++ }
            2 ->
                if (!inInventory(player!!, Items.BRONZE_BAR_2349)) {
                    playerl("Can you help me? How do I go about getting a bronze bar?").also { stage++ }
                } else {
                    playerl("The bronze bar is ready, and waiting for the professor.").also { stage = 5 }
                }

            3 -> npcl("You'll need to use tin and copper ore on a furnace to produce this metal.").also { stage++ }
            4 -> player("Right you are.").also { stage = END_DIALOGUE }
            5 -> npc("He'll surely be pleased. Go ahead and give", "it to him.").also { stage++ }
            6 -> {
                end()
                setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 5)
            }
        }
    }
}

class AssistantTalkAboutMoltenGlassDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.OBSERVATORY_ASSISTANT_6118)
        when (stage) {
            0 -> player("Can I speak with you?").also { stage++ }
            1 -> npc("Why, of course. What is it?").also { stage++ }
            2 ->
                if (!inInventory(player!!, Items.MOLTEN_GLASS_1775)) {
                    playerl("What's the best way for me to get molten glass?").also { stage++ }
                } else {
                    playerl("I managed to get hold of some molten glass.").also { stage = 7 }
                }

            3 ->
                npcl(
                    "There are many ways, but I'd suggest making it yourself. Get yourself a bucket of sand and some soda ash, which you can get from using seaweed with a furnace.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    "Use the soda ash and sand together in a furnace and bang - molten glass is all yours.",
                ).also { stage++ }
            5 -> npcl("There's a book about it on the table if you want to know more.").also { stage++ }
            6 -> playerl("Thank you!").also { stage = END_DIALOGUE }
            7 -> npcl("I suggest you have a word with the professor, in that case.").also { stage++ }
            8 -> {
                end()
                setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 7)
            }
        }
    }
}

class AssistantTalkAboutTheMouldDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.OBSERVATORY_ASSISTANT_6118)
        when (stage) {
            0 -> player("Can I speak with you?").also { stage++ }
            1 -> npc("Why, of course. What is it?").also { stage++ }
            2 ->
                if (!inInventory(player!!, Items.LENS_MOULD_602)) {
                    playerl("Where can I find this lens mould you mentioned?").also { stage++ }
                } else {
                    playerl("I have the lens mould.").also { stage = 11 }
                }

            3 ->
                npc(
                    "I'm sure I heard one of those goblins talking about it. I",
                    "bet they've hidden it somewhere. Probably using it",
                    "for some strange purpose, I'm sure.",
                ).also { stage = 5 }

            5 -> player(FaceAnim.HALF_ASKING, "What makes you say that?").also { stage++ }
            6 ->
                npc(
                    "I had a nice new star chart, until recently. I went out",
                    "to do an errand for the professor the other day, only",
                    "to see a goblin using it...",
                ).also { stage++ }

            7 -> npcl("...as some kind of makeshift hankey to blow his nose!").also { stage++ }
            8 -> player("Oh dear.").also { stage++ }
            9 ->
                npc(
                    "You may want to look through the dungeon they have under",
                    "their little village.",
                ).also { stage++ }

            10 -> player("Thanks for the advice.").also { stage = END_DIALOGUE }
            11 ->
                npcl(
                    "Well done on finding that! I am honestly quite impressed. Make sure you take it straight to the professor.",
                ).also {
                    stage++
                }
            12 -> npcl("Will do.").also { stage++ }
            13 -> {
                end()
                setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 9)
            }
        }
    }
}

class AssistantTalkAboutObservatoryLensDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.OBSERVATORY_ASSISTANT_6118)
        when (stage) {
            0 -> player("Can I speak with you?").also { stage++ }
            1 -> npc("Why, of course. What is it?").also { stage++ }
            2 ->
                if (!inInventory(player!!, Items.OBSERVATORY_LENS_603)) {
                    playerl("How should I make this lens?").also { stage++ }
                } else {
                    playerl("Do you like this lens? Good, huh?").also { stage = 5 }
                }

            3 -> npcl("Just use the molten glass with the mould. Simple.").also { stage++ }
            4 -> player("Thanks!").also { stage = END_DIALOGUE }
            5 -> npc("Nice. What's that scratch?").also { stage++ }
            6 ->
                playerl(
                    "Oh, erm, that's a feature. Yes, that's it! Indubitably, it facilitates the triangulation of photonic illumination to the correct...",
                ).also {
                    stage++
                }
            7 ->
                npcl("Stop! You can't confuse me with big words. Just pray the professor doesn't notice.").also {
                    stage = END_DIALOGUE
                }
        }
    }
}

class AssistantTalkAboutObservatoryDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.OBSERVATORY_ASSISTANT_6118)
        when (stage) {
            0 -> player("Hello again.").also { stage++ }
            1 -> npcl("Ah, it's the telescope repairman!").also { stage++ }
            2 -> npcl("The professor is waiting for you in the Observatory.").also { stage++ }
            3 -> player(FaceAnim.HALF_ASKING, "How can I get to the Observatory?").also { stage++ }
            4 ->
                npcl(
                    "Well, since the bridge was ruined, you will have to travel through the dungeon under the goblins' settlement.",
                ).also {
                    stage = END_DIALOGUE
                }
        }
    }
}
