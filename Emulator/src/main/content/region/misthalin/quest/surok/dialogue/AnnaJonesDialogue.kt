package content.region.misthalin.quest.surok.dialogue

import core.api.addItem
import core.api.inInventory
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class AnnaJonesDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any?): Boolean {
        quest = player.getQuestRepository().getQuest(Quests.WHAT_LIES_BELOW)
        when (getQuestStage(player, Quests.WHAT_LIES_BELOW)) {
            30 -> npc("Ah. You must be " + player.username + ", right? I have a bronze", "pickaxe here for you.")
            40 ->
                if (args.size >= 2) {
                    npc("You did it! oh, well done! How exciting!").also { stage = 10 }
                } else {
                    npc("You opened the tunnel; well done!").also { stage++ }
                }

            else -> {
                if (args.size >= 2) {
                    npc("Excuse me. I am working on that statue at the moment", "Please don't touch it.").also {
                        stage = 12
                    }
                } else {
                    npc("Yes? Can I help you?")
                }
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            30 ->
                when (stage) {
                    0 ->
                        if (!inInventory(player, Items.BRONZE_PICKAXE_1265, 1)) {
                            addItem(player, Items.BRONZE_PICKAXE_1265)
                            player("Thank you very much!").also { stage++ }
                        } else {
                            player("Thanks, but I already have one.").also { stage++ }
                        }

                    1 ->
                        npc(
                            "My employer, Surok Magis, sent word to me that you",
                            "may come to use the tunnel. You will need something to",
                            "help you get in there. The pickaxe will help.",
                        ).also { stage++ }

                    2 -> player("Uh, thanks.").also { stage++ }
                    3 -> npc("Okay, then. The tunnel awaits...").also { stage++ }
                    4 ->
                        options(
                            "What tunnel?",
                            "Sorry, who are you?",
                            "Who does this statue represent?",
                            "What are you doing here?",
                            "Okay, I'd better go.",
                        ).also { stage++ }

                    5 ->
                        when (buttonId) {
                            1 -> player("What tunnel?").also { stage++ }
                            2 -> player("Sorry, who are you?").also { stage += 2 }
                            3 -> player("Who does this statue represent?").also { stage += 3 }
                            4 -> player("What are you doing here?").also { stage += 4 }
                            5 -> player("Okay, I'd better go.").also { stage = END_DIALOGUE }
                        }

                    6 ->
                        npc("Why, the Chaos Tunnel of course! I imagine Surok will", "have told you of it.").also {
                            stage = END_DIALOGUE
                        }

                    7 ->
                        npc(
                            "My name is Louisiana Jones, although most people",
                            "call me Anna. I'm an archaeologist.",
                        ).also { stage = END_DIALOGUE }

                    8 ->
                        npc(
                            "That, my dear, is the statue of the great god Saradomin",
                            "himself. Stand and admire in awe for you are in the",
                            "presence of greatness!",
                        ).also { stage = END_DIALOGUE }

                    9 ->
                        npc(
                            "Well story and rumour has it that Dagon'hai built a",
                            "tunnel under this statue of Saradomin that would allow",
                            "them to visit the Chaos Altar without having to go",
                            "through the Wilderness.",
                        ).also { stage = END_DIALOGUE }
                }

            40 ->
                when (stage) {
                    1 -> end()
                    10 -> player("Right, well, I better see what's down there, then.").also { stage = END_DIALOGUE }
                    11 -> end()
                }

            else ->
                when (stage) {
                    0 ->
                        options(
                            "Who are you?",
                            "What are you doing here?",
                            "Who does this statue represent?",
                            "Okay, I'd better go.",
                        ).also { stage++ }

                    1 ->
                        when (buttonId) {
                            1 -> player("Who are you?").also { stage++ }
                            2 -> player("What are you doing here?").also { stage = 9 }
                            3 -> player("Who does this statue represent?").also { stage = 8 }
                            4 -> player("Okay, I'd better go.").also { stage = END_DIALOGUE }
                        }

                    2 ->
                        npc(
                            "Well, now. Do you always go around asking about people",
                            "like that? It's very rude, you know.",
                        ).also { stage++ }

                    3 -> player("Sorry! I didn't mean to pry!").also { stage++ }
                    4 ->
                        npc(
                            "That's alright. My name is Louisiana Jones, although",
                            "most people call me Anna. I'm an archaeologist.",
                        ).also { stage++ }

                    5 -> player("Oh. Do you work for the Varrock Museum?").also { stage++ }
                    6 ->
                        npc(
                            "Hah! No. I used to, but now I prefer to work",
                            "freelance for independent employers.",
                        ).also { stage++ }

                    7 -> player("I see.").also { stage = END_DIALOGUE }
                    8 ->
                        npc(
                            "That, my dear, is the statue of the great god Saradomin",
                            "himself. Stand and admire in awe, for you are in the",
                            "presence of greatness!",
                        ).also { stage = END_DIALOGUE }

                    9 -> npc("I'm investigating something for someone.").also { stage++ }
                    10 -> player("That doesn't really explain anything!").also { stage++ }
                    11 -> npc("I never said it would.").also { stage = END_DIALOGUE }
                    12 -> player("You are? But you're just sitting there.").also { stage++ }
                    13 -> npc("Yes. I'm on a break.").also { stage++ }
                    14 -> player("Oh, I see. When does your break finish?").also { stage++ }
                    15 ->
                        npc(
                            "When I decide to start working again. Right now, I'm",
                            "enjoying sitting on this bench.",
                        ).also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AnnaJonesDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ANNA_JONES_5837)
    }
}
