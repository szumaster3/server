package content.minigame.gnomeball

import core.api.*
import core.api.interaction.transformNpc
import core.game.global.action.EquipHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs

class PassGnomeBallListener : InteractionListener {
    override fun defineListeners() {
        on(IntType.PLAYER, "pass") { player, node ->
            val catcher = node.asPlayer()

            if (!inInventory(player, Items.GNOMEBALL_751)) return@on false
            if (!hasHandsFree(catcher)) {
                sendMessage(player, "This player seems to have their hands full!")
                return@on false
            }
            PlayerPlayerPassPulse(player, catcher)
            return@on true
        }

        on(NPCs.GNOME_WINGER_634, IntType.NPC, "pass") { player, node ->
            val catcher = node.asNpc()
            setAttribute(catcher, "gnomeball:in-pass", true)
            setAttribute(catcher, "gnomeball:pass-player", player)
            PlayerNPCPassPulse(player, catcher)
            return@on true
        }
    }

    internal class PlayerPlayerPassPulse(
        private val thrower: Player,
        private val catcher: Player,
    ) : Pulse() {
        override fun pulse(): Boolean {
            face(thrower, catcher)
            if (removeItem(thrower, Items.GNOMEBALL_751)) {
                animate(catcher, 3353)
                spawnProjectile(thrower.location, catcher.location, 55, 43, 40, 0, 70, catcher.index - 1)
                sendMessage(thrower, "You throw your gnomeball...")
                EquipHandler.handleEquip(catcher, Item(Items.GNOMEBALL_751))
                sendMessage(thrower, "${catcher.name} manages to get it.")
                sendMessage(catcher, "You caught ${thrower.name}'s gnomeball!")
            }
            return true
        }
    }

    internal class PlayerNPCPassPulse(
        private val player: Player,
        private val npc: NPC,
    ) : Pulse() {
        override fun pulse(): Boolean {
            player.face(npc)
            if (removeItem(player, Item(Items.GNOMEBALL_751))) {
                animate(npc, 3353)
                spawnProjectile(player.location, npc.location, 55, 43, 40, 0, 70, 10)
                transformNpc(npc, NPCs.GNOME_WINGER_633, 10)
                face(npc, player)
                animate(npc, Animations.BALLER_THROW_201)
                spawnProjectile(npc.location, player.location, 55, 43, 40, 0, 70, 10)
            }
            return true
        }
    }
}
