package content.region.desert.dialogue.pollnivneach

import core.api.interaction.openNpcShop
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class AliTheBarmanDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.ALI_THE_BARMAN_1864)
        when (stage) {
            0 -> player("Hello there.").also { stage++ }
            1 -> npc("Good day. Can I help you with anything?").also { stage++ }
            2 ->
                options(
                    "Yes, I'd like a drink please.",
                    "What's going on in town?",
                    "The 'Asp and Snake'? What a strange name for a bar.",
                ).also { stage++ }

            3 ->
                when (buttonID) {
                    1 -> player("Yes, I'd like a drink please.").also { stage++ }
                    2 -> player("What's going on in town?").also { stage = 5 }
                    3 -> player("The 'Asp and Snake'? What a strange name for a bar.").also { stage = 6 }
                }

            4 -> {
                end()
                openNpcShop(player!!, NPCs.ALI_THE_BARMAN_1864)
            }
            5 -> sendDialogue(player!!, "Transcript missing.").also { stage = END_DIALOGUE }
            6 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "I know what you're thinking, asps are a type of snake, right?",
                ).also { stage++ }
            7 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "I admit it is a little confusing, but neither 'The Asp' nor 'The Snake' have quite the same ring to them.",
                ).also {
                    stage++
                }
            8 -> player("I get your point. I quite like the name.").also { stage = END_DIALOGUE }
        }
    }
}
