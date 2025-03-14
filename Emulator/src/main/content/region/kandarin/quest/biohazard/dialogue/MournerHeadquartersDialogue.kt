package content.region.kandarin.quest.biohazard.dialogue

import core.api.inBorders
import core.api.inEquipment
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class MournerHeadquartersDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.MOURNER_348)
        when (stage) {
            0 -> {
                if (inBorders(player!!, 2547, 3321, 2555, 3327)) {
                    player("Hello there.").also { stage = 100 }
                } else {
                    playerl(FaceAnim.FRIENDLY, "Hi.").also { stage = 0 }
                }
            }

            1 -> npcl(FaceAnim.SAD, "What are you up to?").also { stage++ }
            2 -> playerl(FaceAnim.NEUTRAL, "Just sight-seeing.").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "This is no place for sight-seeing. Don't you know there's been a plague outbreak?",
                ).also { stage++ }

            4 -> playerl(FaceAnim.FRIENDLY, "Yes, I had heard.").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY, "Then I suggest you leave as soon as you can.").also { stage++ }
            6 -> playerl(FaceAnim.FRIENDLY, "Thanks for the advice.").also { stage = END_DIALOGUE }

            100 ->
                if (npc!!.id == NPCs.MOURNER_370) {
                    npc(
                        "You're here at last! I don't know what I've",
                        "eaten but I feel like I'm on death's door.",
                    ).also { stage = 104 }
                } else {
                    npc("Oh dear oh dear. I feel terrible, I", "think it was the stew.").also { stage++ }
                }

            101 -> player("You should be more careful with your", "ingredients.").also { stage++ }
            102 ->
                if (!inEquipment(player!!, Items.DOCTORS_GOWN_430)) {
                    npc(
                        "I need a doctor. The nurse's hut is to the south west.",
                        "Go now and bring us a doctor, that's an order.",
                    ).also { stage = END_DIALOGUE }
                } else {
                    npc(
                        "There is one mourner, who's really sick, ",
                        "resting upstairs. You should see to him first.",
                    ).also { stage++ }
                }

            103 -> player("Ok, I'll see what I can do.").also { stage = END_DIALOGUE }
            104 -> player("Hmm... interesting, sounds like food poisoning.").also { stage++ }
            105 -> npc("Yes, I'd figured that out already.", "What can you give me to help.").also { stage++ }
            106 ->
                options(
                    "Just hold your breath and count to ten.",
                    "The best I can do is pray for you.",
                    "There's nothing I can do, it's fatal.",
                ).also { stage++ }

            107 ->
                when (buttonID) {
                    1 -> player("Just hold your breath and count to ten.").also { stage++ }
                    2 -> player("The best I can do is pray for you.").also { stage = 112 }
                    3 -> player("There's nothing I can do, it's fatal.").also { stage = 113 }
                }

            108 -> npc("What? How will that help? What kind of doctor are you?").also { stage++ }
            109 -> player("Erm... I'm new, I just started.").also { stage++ }
            110 -> npc("You're no doctor!").also { stage++ }
            111 -> {
                end()
                npc!!.attack(player)
            }

            112 -> npc("Pray for me? You're no doctor...", "You're an imposter!").also { stage = 111 }
            113 -> npc("No, I'm too young to die! I've never", "even had a girlfriend.").also { stage++ }
            114 -> player("That's life for you.").also { stage++ }
            115 -> npc("Wait a minute, where's your equipment?").also { stage++ }
            116 -> player("It's erm... at home.").also { stage++ }
            117 -> npc("You're no doctor!").also { stage = 111 }
        }
    }
}
