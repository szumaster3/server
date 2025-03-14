package content.region.asgarnia.dialogue.taverley

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class LordDaquariusDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("What want you, with the Lord of the Kinshra?", "Speak!").also { stage++ }
            1 -> player("I am here on behalf of the White Knights...").also { stage++ }
            2 -> npc("Pah!", "Begone, fool, or prepare to taste my blade!").also { stage++ }
            3 -> player("Now wait a minute, this concerns a friend of yours...").also { stage++ }
            4 ->
                npc(
                    "A friend of mine?",
                    "What friend of mine would the Temple Knights show",
                    "any intere-",
                ).also { stage++ }

            5 -> npc("Oh.", "This...", "This is about Solus, isn't it?").also { stage++ }
            6 -> player("Bingo.").also { stage++ }
            7 ->
                npc(
                    "Then let me assure you, that monster is no 'friend' of",
                    "mine - if indeed such a creature even has 'friends'!",
                ).also { stage++ }

            8 -> npc("No, that man is certainly no friend of mine nor of the", "Kinshra.").also { stage++ }
            9 -> player("So you will help me find him?").also { stage++ }
            10 ->
                npc(
                    "No fool...",
                    "You and your White Knight cronies are no friend of",
                    "the Kinshra either.",
                ).also { stage++ }

            11 -> npc("I will help neither of you, and I wish you all a slow and", "painful death.").also { stage++ }
            12 -> player("You know...", "I'm sure I could MAKE you tell me where he is...").also { stage++ }
            13 ->
                npc(
                    "Is that a threat, whelp?",
                    "Do your worst.",
                    "I care not for my own life, I stand only for the",
                    "protection of the Kinshra!",
                ).also { stage++ }

            14 ->
                player.dialogueInterpreter
                    .sendDialogues(
                        NPCs.SAVANT_2748,
                        FaceAnim.NEUTRAL,
                        "${player.username}?",
                        "I've been monitoring you.",
                        "He's telling the truth, so don't try to bluff him by",
                        "starting a fight.",
                    ).also { stage++ }

            15 ->
                player.dialogueInterpreter
                    .sendDialogues(
                        NPCs.SAVANT_2748,
                        FaceAnim.NEUTRAL,
                        "All of our records show that the only thing he will make",
                        "a stand for is the protection of his men, and he has little",
                        "regard for his own safety in comparison.",
                    ).also { stage++ }

            16 ->
                player.dialogueInterpreter
                    .sendDialogues(
                        NPCs.SAVANT_2748,
                        FaceAnim.NEUTRAL,
                        "You're going to have to find some other way to",
                        "intimidate him into giving you the information we need.",
                    ).also { stage++ }

            17 -> player("Understood.", "${player.username} out.").also { stage = END_DIALOGUE }

            18 ->
                npc(
                    FaceAnim.SAD,
                    "Stop!",
                    "I will tell you what you want!",
                    "Please... leave my men be...",
                ).also { stage = END_DIALOGUE }

            19 -> npc("What want you, with the Lord of the Kinshra?", "Speak!").also { stage++ }
            20 ->
                player(
                    "Okay Daquarius, you tell me the whereabouts of Solus",
                    "Dellagar right now, or I will put every Black Knight",
                    "here to their death in front of you!",
                ).also { stage++ }

            21 ->
                npc(
                    FaceAnim.SAD,
                    "*sigh*",
                    "I should have known the White Knights would be my",
                    "ruin once again...",
                ).also { stage++ }

            22 ->
                npc(
                    FaceAnim.SAD,
                    "I do not know his exact whereabouts, and when last we",
                    "met we did not leave...",
                    "on the best of terms.",
                ).also { stage++ }

            23 ->
                npc(
                    "All I know is that he left behind some fur when he left,",
                    "I would expect him to be in an area with furred",
                    "creatures of some sort.",
                ).also { stage++ }

            24 -> player("What kind of fur?", "Dog? Bear? Wolf?").also { stage++ }
            25 ->
                npc(
                    "I do not know, I am a warrior, not a zookeeper.",
                    "It was not bear fur, I know that much.",
                ).also { stage++ }

            26 ->
                player(
                    FaceAnim.ANGRY,
                    "That's it?",
                    "That's your help?",
                    "You'd better not be lying Daquarius, or...",
                ).also { stage++ }

            27 ->
                player.dialogueInterpreter
                    .sendDialogues(
                        NPCs.SAVANT_2748,
                        FaceAnim.NEUTRAL,
                        "Calm yourself Player, he is telling the truth.",
                    ).also { stage++ }

            28 -> player("How do you know?").also { stage++ }
            29 ->
                player.dialogueInterpreter
                    .sendDialogues(
                        NPCs.SAVANT_2748,
                        FaceAnim.NEUTRAL,
                        "I am monitoring your conversation through the",
                        "CommOrb, his physiology shows none of the changes we",
                        "usually get when someone lies to us.",
                    ).also { stage++ }

            30 -> player("So this CommOrb thing can work as a lie-detector?", "That's pretty useful!").also { stage++ }
            31 ->
                player.dialogueInterpreter
                    .sendDialogues(
                        NPCs.SAVANT_2748,
                        FaceAnim.NEUTRAL,
                        "Yes, it is, but back to the task at hand.",
                        "I suggest you head to the next subject of our",
                        "observations, he might be more help in locating Solus.",
                    ).also { stage++ }

            32 -> player("You mean that Zamorakian mage in Varrock?").also { stage++ }
            33 ->
                player.dialogueInterpreter
                    .sendDialogues(
                        NPCs.SAVANT_2748,
                        FaceAnim.NEUTRAL,
                        "Yes, he would seem a likely bet.",
                        "Savant out.",
                    ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LORD_DAQUARIUS_200)
    }
}
