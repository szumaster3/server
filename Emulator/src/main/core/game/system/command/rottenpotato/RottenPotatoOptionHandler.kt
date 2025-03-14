package core.game.system.command.rottenpotato

import core.api.removeItem
import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.system.command.CommandSystem
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.colorize
import org.rs.consts.Items

private val ROTTEN_POTATO_OPTIONS = arrayOf("RS HD", "Heal", "Extra", "Commands")

@Initializable
class RottenPotatoOptionHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (option in ROTTEN_POTATO_OPTIONS) {
            ItemDefinition.forId(Items.ROTTEN_POTATO_5733).handlers["option:${option.lowercase()}"] =
                this
        }
        return this
    }

    override fun handle(
        player: Player?,
        node: Node?,
        option: String?,
    ): Boolean {
        player ?: return false
        node ?: return false
        option ?: return false

        if (player.rights != Rights.ADMINISTRATOR) {
            removeItem(player, Items.ROTTEN_POTATO_5733)
            return false
        }

        when (option) {
            "rs hd" -> player.dialogueInterpreter.open(RottenPotatoRSHDDialogue().ID)
            "heal" -> player.fullRestore().also { player.sendMessage(colorize("%RAll healed!")) }
            "extra" -> player.dialogueInterpreter.open(RottenPotatoExtraDialogue().ID)
            "commands" -> {
                CommandSystem.commandSystem.parse(player, "commands")
            }
        }
        return true
    }
}
