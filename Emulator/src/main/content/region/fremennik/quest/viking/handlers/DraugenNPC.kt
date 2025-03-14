package content.region.fremennik.quest.viking.handlers

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

class DraugenNPC(
    val player: Player,
) : NPC(1279, player.location?.transform(1, 0, 0)) {
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
            removeAttribute(player, "fremtrials:draugen-spawned")
            this.clear()
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        setAttribute(player, "/save:fremtrials:draugen-killed", true)
        removeAttribute(player, "fremtrials:draugen-loc")
        if (removeItem(player, Item(Items.HUNTERS_TALISMAN_3696), Container.INVENTORY)) {
            addItem(player, Items.HUNTERS_TALISMAN_3697, 1, Container.INVENTORY)
        } else {
            sendMessage(player, "Omenare imperavi emulari omg.")
        }
    }
}
