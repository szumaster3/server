package content.region.feldiphills.quest.chompybird.plugin

import core.api.getAttribute
import core.api.removeItem
import core.api.sendDialogue
import core.api.sendItemDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class ChompyBirdPlugin : InteractionListener {
    override fun defineListeners() {
        ChompyBird.values().forEach { hat ->
            onEquip(hat.id) { player, node ->
                val kills = getAttribute(player, "chompy-kills", 0)
                if (kills < hat.kills) {
                    sendItemDialogue(player, node.id, "You haven't earned this!")
                    removeItem(player, node.asItem())
                    return@onEquip false
                }
                return@onEquip true
            }
        }

        on(Items.COMP_OGRE_BOW_4827, IntType.ITEM, "check kills", "operate") { player, _ ->
            val kills = getAttribute(player, "chompy-kills", 0)
            sendDialogue(player, "You kill $kills chompy " + (if (kills == 1) "bird" else "birds") + ".")
            return@on true
        }
    }
}