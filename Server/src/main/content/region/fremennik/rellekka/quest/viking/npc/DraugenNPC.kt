package content.region.fremennik.rellekka.quest.viking.npc

import content.data.GameAttributes
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Draugen NPC.
 */
class DraugenNPC(val player: Player) : NPC(NPCs.THE_DRAUGEN_1279, player.location?.transform(1, 0, 0)) {

    override fun init() {
        super.init()
        isRespawn = false
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (!properties.combatPulse.isAttacking) {
            properties.combatPulse.attack(player)
        }
        if (!player.isActive) {
            removeAttribute(player, GameAttributes.QUEST_VIKING_SIGLI_DRAUGEN_SPAWN)
            this.clear()
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        setAttribute(player, GameAttributes.QUEST_VIKING_SIGLI_DRAUGEN_KILL, true)
        removeAttribute(player, GameAttributes.QUEST_VIKING_SIGLI_DRAUGEN_LOCATION)

        val oldTalisman = Items.HUNTERS_TALISMAN_3696
        val newTalisman = Items.HUNTERS_TALISMAN_3697

        /*
         * Remove all old talismans from inventory and bank.
         */
        removeAll(player, oldTalisman, Container.INVENTORY)
        removeAll(player, oldTalisman, Container.BANK)

        /*
         * Add new talisman to inventory
         */
        addItem(player, newTalisman, 1, Container.INVENTORY)
    }
}
