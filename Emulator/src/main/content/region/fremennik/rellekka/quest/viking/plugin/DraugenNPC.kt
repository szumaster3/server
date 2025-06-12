package content.region.fremennik.rellekka.quest.viking.plugin

import content.data.GameAttributes
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Draugen NPC, a hostile spirit encountered during the Fremennik Trials quest.
 *
 * @property player The player who summoned and is meant to fight the Draugen.
 */
class DraugenNPC(
    val player: Player,
) : NPC(NPCs.THE_DRAUGEN_1279, player.location?.transform(1, 0, 0)) {

    /**
     * Initializes the Draugen NPC instance.
     *
     * Disables respawn and sets up its default behavior.
     */
    override fun init() {
        super.init()
        isRespawn = false
    }

    /**
     * Called every game tick to manage the Draugen's behavior.
     *
     * Ensures the Draugen keeps attacking the player and despawns
     * if the player logs out or becomes inactive.
     */
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

    /**
     * Finalizes the Draugen's death.
     *
     * Updates the quest state and upgrades the Hunter's Talisman in the player's inventory.
     *
     * @param killer The entity responsible for the Draugen's death.
     */
    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        setAttribute(player, GameAttributes.QUEST_VIKING_SIGLI_DRAUGEN_KILL, true)
        removeAttribute(player, GameAttributes.QUEST_VIKING_SIGLI_DRAUGEN_LOCATION)
        if (removeItem(player, Item(Items.HUNTERS_TALISMAN_3696), Container.INVENTORY)) {
            addItem(player, Items.HUNTERS_TALISMAN_3697, 1, Container.INVENTORY)
        }
    }
}
