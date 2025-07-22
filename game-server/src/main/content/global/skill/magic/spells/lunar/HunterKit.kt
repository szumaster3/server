package content.global.skill.magic.spells.lunar

import core.api.addItemOrDrop
import core.api.freeSlots
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class HunterKit : InteractionListener {
    private val HunterKitItems =
        intArrayOf(
            Items.NOOSE_WAND_10150,
            Items.BUTTERFLY_NET_10010,
            Items.BIRD_SNARE_10006,
            Items.RABBIT_SNARE_10031,
            Items.TEASING_STICK_10029,
            Items.UNLIT_TORCH_596,
            Items.BOX_TRAP_10008,
        )

    override fun defineListeners() {
        on(Items.HUNTER_KIT_11159, IntType.ITEM, "open") { player, _ ->
            if (freeSlots(player) < 6) {
                sendMessage(
                    player,
                    "You need at least six free inventory space to do this.",
                ).also { return@on false }
            }
            if (removeItem(player, Items.HUNTER_KIT_11159)) {
                for (item in HunterKitItems) {
                    addItemOrDrop(player, item)
                }
            }
            return@on true
        }
    }
}
