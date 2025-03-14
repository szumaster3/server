package content.region.kandarin.quest.seaslug.dialogue

import core.api.addItemOrDrop
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendDoubleItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class BaileyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when {
            getQuestStage(player, Quests.SEA_SLUG) >= 15 -> player("Hello.").also { stage = 100 }

            getQuestStage(player, Quests.SEA_SLUG) >= 20 ->
                player("I've managed to light the torch.").also {
                    stage = 200
                }

            else -> player("Hello there.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.SCARED, "What? Who are you? Come inside quickly!").also { stage++ }
            1 -> npc(FaceAnim.SCARED, "What are you doing here?").also { stage++ }
            2 -> player("I'm trying to find out what happened to a boy named", "Kennith.").also { stage++ }
            3 ->
                npc(
                    "Oh you mean Kent's son. He's around somewhere, ",
                    "probably hiding if he knows what's good for him.",
                ).also {
                    stage++
                }
            4 -> player("Hiding from what? What's got you so frightened?").also { stage++ }
            5 -> npc("Haven't you seen all those things out there?").also { stage++ }
            6 -> player("The sea slugs?").also { stage++ }
            7 ->
                npc(
                    "It all began about a week ago. We pulled up a haul of",
                    "deep sea flatfish. Mixed in with them we found these",
                    "slug things, but thought nothing of it.",
                ).also {
                    stage++
                }
            8 ->
                npc(
                    "Not long after that my friends began to change, now",
                    "they spend all day pulling in hauls of fish, only to throw",
                    "back the fish and keep those nasty sea slugs.",
                ).also {
                    stage++
                }
            9 ->
                npc(
                    "What am I supposed to do with those? I haven't figured",
                    "out how to kill one yet. If I put them near the stove",
                    "they squirm and jump away.",
                ).also {
                    stage++
                }
            10 -> player("I doubt they would taste too good.").also { stage++ }
            11 -> npc(FaceAnim.ANNOYED, "This is no time for humour.").also { stage++ }
            12 -> player("I'm sorry, I didn't mean to upset you.").also { stage++ }
            13 ->
                npc(
                    "That's okay. I just can't shake the feeling that this is",
                    "the start of something... Terrible.",
                ).also {
                    stage++
                }
            14 -> {
                end()
                setQuestStage(player, Quests.SEA_SLUG, 6)
            }
            100 ->
                npc(
                    FaceAnim.SCARED,
                    "Oh, thank the gods it's you. They've all gone mad I tell",
                    "you, one of the fishermen tried to throw me into the",
                    "sea!",
                ).also {
                    stage++
                }
            101 -> player("They're all being controlled by the sea slugs.").also { stage++ }
            102 -> npc("I figured as much.").also { stage++ }
            103 ->
                player(
                    "I need to get Kennith off this platform, but I can't get",
                    "past the fishermen.",
                ).also { stage++ }
            104 ->
                npc(
                    "The sea slugs are scared of heat, I figured that out",
                    "when I tried to cook them.",
                ).also { stage++ }
            105 -> npc("Here.").also { stage++ }
            106 ->
                sendDoubleItemDialogue(player, -1, Items.UNLIT_TORCH_596, "Bailey gives you a torch.").also {
                    addItemOrDrop(player, Items.UNLIT_TORCH_596)
                    stage++
                }
            107 -> npc("I doubt the fishermen will come near you if you can", "get this torch lit.").also { stage++ }
            108 ->
                npc("The only problem is all the wood and flint are damp... I", "can't light a thing!").also {
                    stage =
                        END_DIALOGUE
                }
            200 -> npc("Well done traveller, you'd better get Kennith", "out of here soon.").also { stage++ }
            201 ->
                npc(
                    "The fishermen are becoming stranger by the minute, ",
                    "and they keep pulling up those blasted sea slugs.",
                ).also {
                    stage++
                }
            202 -> player("Don't worry I'm working on it.").also { stage++ }
            203 ->
                npc("Just be sure to watch your back. The fishermen", "seem to have taken notice of you.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BAILEY_695)
    }
}
