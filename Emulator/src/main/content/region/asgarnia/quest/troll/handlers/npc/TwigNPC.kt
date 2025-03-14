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
class TwigNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return TwigNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TWIG_1126, NPCs.TWIG_1128)
    }

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        val attackable = super.isAttackable(entity, style, message)
        if (this.id == NPCs.TWIG_1128) {
            val prevLifePoints = this.skills.lifepoints
            this.transform(NPCs.TWIG_1126)
            this.skills.lifepoints = prevLifePoints
        }
        return attackable
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (getAttribute("return-to-spawn", false) && this.id == NPCs.TWIG_1126) {
            val prevLifePoints = this.skills.lifepoints
            this.transform(NPCs.TWIG_1128)
            this.skills.lifepoints = prevLifePoints
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (isQuestInProgress(killer as Player, Quests.TROLL_STRONGHOLD, 8, 10) &&
            !inInventory(
                killer,
                Items.CELL_KEY_1_3136,
            )
        ) {
            produceGroundItem(killer, Items.CELL_KEY_1_3136, 1, this.location)
        }
        super.finalizeDeath(killer)
    }
}
