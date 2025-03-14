package content.region.kandarin.quest.waterfall.dialogue

import core.api.addItem
import core.api.hasAnItem
import core.api.removeItem
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class GolrieDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        player(FaceAnim.NEUTRAL, "Hello, is your name Golrie?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "That's me. I've been stuck in here for weeks, those",
                    "goblins are trying to steal my family's heirlooms. My",
                    "grandad gave me all sorts of old junk.",
                ).also {
                    stage++
                }
            1 -> player(FaceAnim.ASKING, "Do you mind if I have a look?").also { stage++ }
            2 -> npc(FaceAnim.OLD_HAPPY, "No, of course not.").also { stage++ }
            3 ->
                sendDialogue("You look amongst the junk on the floor.").also {
                    stage = if (hasAnItem(player, Items.GLARIALS_PEBBLE_294).container != null) 50 else 4
                }

            4 -> sendDialogue(player, "Mixed with the junk on the floor you find Glarial's pebble.").also { stage++ }
            5 -> player(FaceAnim.ASKING, "Could I take this old pebble?").also { stage++ }
            6 ->
                npc(FaceAnim.OLD_NORMAL, "Oh that, yes have it, it's just some old elven junk I", "believe.").also {
                    addItem(player, Items.GLARIALS_PEBBLE_294)
                    stage++
                }
            7 ->
                sendDialogue("You give the key to Golrie.").also {
                    removeItem(
                        player,
                        Item(Items.A_KEY_293, 1),
                    )
                    stage++
                }
            8 ->
                npc(
                    FaceAnim.OLD_HAPPY,
                    "Thanks a lot for the key traveller. I think I'll wait in",
                    "here until those goblins get bored and leave.",
                ).also {
                    stage++
                }
            9 -> player(FaceAnim.NEUTRAL, "OK... Take care Golrie.").also { stage = END_DIALOGUE }
            10 -> sendDialogue("You find nothing of interest.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(DialogueInterpreter.getDialogueKey("golrie_dialogue"), NPCs.GOLRIE_306)
    }
}
