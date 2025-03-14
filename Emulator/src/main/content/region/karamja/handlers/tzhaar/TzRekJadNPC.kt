package content.region.karamja.handlers.tzhaar

import content.region.karamja.dialogue.tzhaar.TzhaarMejJalDialogue
import core.cache.def.impl.NPCDefinition
import core.game.dialogue.DialogueInterpreter
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class TzRekJadNPC : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        NPCDefinition.forId(NPCs.TZHAAR_MEJ_JAL_2617).handlers["option:exchange fire cape"] = this
        ClassScanner.definePlugins(TzhaarMejJalDialogue())
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        when (option) {
            "exchange fire cape" ->
                player.dialogueInterpreter.open(
                    DialogueInterpreter.getDialogueKey(
                        if (player.inventory.containsItem(
                                Item(Items.FIRE_CAPE_6570),
                            )
                        ) {
                            "firecape-exchange"
                        } else {
                            "tzhaar-mej"
                        },
                    ),
                    node.asNpc(),
                )
        }
        return true
    }
}
