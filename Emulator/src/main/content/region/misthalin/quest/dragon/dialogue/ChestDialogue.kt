package content.region.misthalin.quest.dragon.dialogue

import content.region.misthalin.quest.dragon.DragonSlayer
import core.api.addItemOrDrop
import core.api.sendDialogueLines
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player

class ChestDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        sendDialogueLines(player, "As you open the chest, you notice an inscription on the lid:")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                sendDialogueLines(
                    player,
                    "Here I rest the map to my beloved home. To whoever finds it, I beg",
                    "of you, let it be. I was honour-bound not to destroy the map piece,",
                    "but I have used all my magical skill to keep it from being recovered.",
                ).also {
                    stage++
                }
            1 ->
                sendDialogueLines(
                    player,
                    "This map leads to the lair of the beast that destroyed my home,",
                    "devoured my family, and burned to a cinder all that I love. But",
                    "revenge would not benefit me now, and to disturb this beast is to risk",
                    "bringing its wrath down upon another land.",
                ).also {
                    stage++
                }
            2 ->
                sendDialogueLines(
                    player,
                    "I cannot stop you from taking this map piece now, but think on this:",
                    "if you can slay the Dragon of Crandor, you are a greater hero than",
                    "my land ever produced. There is no shame in backing out now.",
                ).also {
                    stage++
                }
            3 -> {
                end()
                addItemOrDrop(player, DragonSlayer.MAGIC_PIECE.id)
                sendItemDialogue(player, DragonSlayer.MAGIC_PIECE, "You find a map piece in the chest.")
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(3802875)
    }
}
