package content.region.asgarnia.dialogue.portsarim

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class PortSarimGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("HALT! Who goes there?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Don't worry, I'm not going to cause trouble.",
                    "I am Player the Mighty!",
                    "No-one, there's no-one here.",
                ).also {
                    stage++
                }

            1 ->
                when (buttonId) {
                    1 -> player("Don't worry, I'm not going to cause trouble.").also { stage++ }
                    2 -> player(FaceAnim.JOLLY, "I am Player the Mighty!").also { stage = 4 }
                    3 -> player("No-one, there's no-one here.").also { stage = 11 }
                }
            2 -> npc("But you shouldn't be here - be off with you!").also { stage++ }
            3 -> player("I was going anyway.").also { stage = END_DIALOGUE }
            4 ->
                npc(
                    "Mighty? You look like another of those silly adventurers",
                    "who thinks they're the bee's knees just because they've",
                    "done a few lousy quests!",
                ).also {
                    stage++
                }
            5 ->
                player(
                    "Well it sounds better than sitting on this rooftop all day",
                    "looking at trees!",
                ).also { stage++ }
            6 -> npc("I'll have you know it's a very important job guarding this", "jail!").also { stage++ }
            7 ->
                npc(
                    "If anyone comes sneaking in here to mess around with ",
                    "the prisoners, the lads downstairs will make mincemeat of them, and I'll be",
                    "here to pick them off if they try to escape.",
                ).also {
                    stage++
                }
            8 ->
                player(
                    "You mean people aren't meant to be able to shoot the",
                    "prisoners in the cells?",
                ).also { stage++ }
            9 -> npc(FaceAnim.NOD_YES, "Yes, that's right.").also { stage++ }
            10 -> player("Okay, it's been nice talking to you.").also { stage = END_DIALOGUE }
            11 -> npc(FaceAnim.THINKING, "What? I can see you!").also { stage++ }
            12 ->
                player(
                    "No, you're just imagining it. Perhaps you've been up here in the sun for too long?",
                ).also { stage++ }
            13 -> npc(FaceAnim.HALF_ASKING, "So who am I talking to?").also { stage++ }
            14 ->
                player(
                    "Oh dear, you've started talking to yourself. That's a common sign that you're going mad!",
                ).also {
                    stage++
                }
            15 -> npc(FaceAnim.SAD, "But... but... you're standing right there...").also { stage++ }
            16 -> sendDialogue(player, "Maybe you should leave him alone now.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARD_344)
    }
}
