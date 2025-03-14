package content.region.desert.dialogue.pollnivneach

import core.api.sendDialogueOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AliTheKebabSellerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hello")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("Hello. What can I do for you?").also { stage++ }
            1 -> player("I don't know, what can you do for me?").also { stage++ }
            2 -> npc("Well, that depends.").also { stage++ }
            3 -> player("Depends on what?").also { stage++ }
            4 -> npc("It depends on whether you like kebabs or not.").also { stage++ }
            5 -> player("Why is that?").also { stage++ }
            6 ->
                npc(
                    "Seeing as I'm in the kebab construction industry.",
                    "I mainly help people who have want of a kebab.",
                ).also {
                    stage++
                }
            7 -> player(FaceAnim.THINKING, "Well then, what kind of kebabs do you, erm, construct?").also { stage++ }
            8 ->
                npc(
                    "I offer two different types of kebabs: the standard",
                    "run-of-the-mill kebab seen throughout Gielinor and",
                    "enjoyed by many a intoxicated dwarf, and my specialty,",
                    "the extra-hot kebab. So which shall it be?",
                ).also {
                    stage++
                }
            9 -> npc("Or are my services even required?").also { stage++ }
            10 -> interpreter.sendOptions("Select one", "Yes, thanks.", "No thanks, I'm good").also { stage++ }
            11 ->
                when (buttonId) {
                    1 -> player("Yes, thanks.").also { stage++ }
                    2 -> player("No thanks, I'm good.").also { stage = END_DIALOGUE }
                }
            12 ->
                sendDialogueOptions(
                    player,
                    "Select one.",
                    "I want a kebab, please.",
                    "Could I have one of those crazy hot kebabs of yours?",
                    "Would you sell me that bottle of special kebab sauce?",
                    "What is the difference between the standard and extra hot?",
                    "I need some information.",
                ).also { stage++ }

            13 ->
                when (buttonId) {
                    1 -> player("I want a kebab, please.").also { stage = 20 }
                    2 -> player("Could I have one of those crazy hot kebabs of yours?").also { stage = 30 }
                    3 -> player("Would you sell me that bottle of special kebab sauce?").also { stage = 40 }
                    4 -> player("What is the difference between the standard and extra hot?").also { stage = 50 }
                    5 -> player("I need some information.").also { stage = 60 }
                }

            20 -> {
                npc("That will be 3 coins please.")
                stage =
                    if (player.inventory.contains(995, 3)) {
                        21
                    } else {
                        25
                    }
            }

            21 -> {
                npc("Here you go.")
                player.inventory.remove(Item(995, 3))
                player.inventory.add(Item(1971))
                stage = 22
            }

            22 -> npc("Is there anything else I can do for you?").also { stage = 10 }
            25 -> player("I seem to have not brought enough money with me. Sorry.").also { stage = 22 }
            30 ->
                npc(
                    "A super kebab it is so. Be careful, they really are as hot as",
                    "they are made out to be.",
                ).also { stage++ }
            31 -> player("Sure, sure.").also { stage++ }
            32 -> {
                npc("Here you go.")
                player.inventory.add(Item(4608))
                stage = 33
            }

            33 -> player("Thanks.").also { stage = 22 }
            40 ->
                if (!player.inventory.containsItem(Item(4610))) {
                    npc("Well, that depends on what you have in mind.")
                    stage = 41
                } else {
                    npc("I already gave it to you!").also { stage = 22 }
                }
            41 ->
                player(
                    "Set yourself at east, I have no intention of setting up a",
                    "rival kebab shop.",
                ).also { stage++ }
            42 -> npc("Well, then, what do you want it for?").also { stage++ }
            43 -> player("I don't know, but I think I could have a little fun with it.").also { stage++ }
            44 ->
                npc(
                    "You're not going to put it in drunken Ali's drink",
                    "now, are you? That's what happened last time",
                    "I gave someone the sauce.",
                ).also {
                    stage++
                }
            45 ->
                player(
                    "No, that would be a bit cliche, I think I'll come up with",
                    "something more original.",
                ).also { stage++ }
            46 -> {
                npc("Just be careful with it, it's potent enough to give a camel", "the runs.")
                player.inventory.add(Item(4610))
                stage = 47
            }

            47 -> player("Thank you very much.").also { stage = 22 }
            50 ->
                npc(
                    "If I told you that I would have to kill you. Kebab sales is",
                    "a cut-throat industry. The extra-hot kebab gives",
                    "me the competitive edge on the rest and if I were to",
                    "divulge my secrets to every passing adventurer, well it",
                ).also {
                    stage++
                }
            51 -> npc("would soon disappear.").also { stage++ }
            52 -> player("So there's no difference except the sauce you use then!").also { stage++ }
            53 ->
                npc("Shh! Keep your voice down. You should have told me you", "were from the union.").also {
                    stage =
                        22
                }
            60 ->
                npc(
                    "If information is what you need, there are many better",
                    "people to ask than myself who could help you. One of the",
                    "town's street urchins or perhaps the local drunk.",
                    "I may be many things to many people, but a",
                ).also {
                    stage++
                }
            61 -> npc("gossip I am not.").also { stage = 22 }
            100 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AliTheKebabSellerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_THE_KEBAB_SELLER_1865)
    }
}
