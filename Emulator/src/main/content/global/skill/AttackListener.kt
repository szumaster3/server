package content.global.skill

import core.api.inEquipment
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.CombatStyle
import org.rs.consts.Items
import org.rs.consts.NPCs

class AttackListener : InteractionListener {
    override fun defineListeners() {
        flagInstant()
        on(IntType.NPC, "attack") { player, npc ->
            if (npc.id == NPCs.MAGIC_DUMMY_4474 && player.getSwingHandler(false).type != CombatStyle.MAGIC) {
                sendMessage(player, "You can only attack this with magic.")
                return@on true
            }
            if (npc.id == NPCs.MELEE_DUMMY_7891 && !inEquipment(player, Items.TRAINING_SWORD_9703)) {
                sendMessage(player, "You must use the training sword to attack this.")
            } else {
                player.attack(npc)
            }
            player.attack(npc)
            return@on true
        }
    }
}
