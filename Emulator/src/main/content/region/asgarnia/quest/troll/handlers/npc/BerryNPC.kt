package content.region.asgarnia.quest.troll.handlers.npc

import core.api.inInventory
import core.api.item.produceGroundItem
import core.api.quest.isQuestInProgress
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class BerryNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return BerryNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BERRY_1127, NPCs.BERRY_1129)
    }

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        val attackable = super.isAttackable(entity, style, message)
        if (this.id == NPCs.BERRY_1129) {
            val prevLifePoints = this.skills.lifepoints
            this.transform(NPCs.BERRY_1127)
            this.skills.lifepoints = prevLifePoints
        }
        return attackable
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (getAttribute("return-to-spawn", false) && this.id == NPCs.BERRY_1127) {
            val prevLifePoints = this.skills.lifepoints
            this.transform(NPCs.BERRY_1129)
            this.skills.lifepoints = prevLifePoints
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (isQuestInProgress(killer as Player, Quests.TROLL_STRONGHOLD, 8, 10) &&
            !inInventory(
                killer,
                Items.CELL_KEY_2_3137,
            )
        ) {
            produceGroundItem(killer, Items.CELL_KEY_2_3137, 1, this.location)
        }
        super.finalizeDeath(killer)
    }
}
