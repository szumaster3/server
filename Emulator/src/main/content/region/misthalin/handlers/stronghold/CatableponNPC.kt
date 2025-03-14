package content.region.misthalin.handlers.stronghold

import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Represents the Catablepon NPC.
 */
@Initializable
class CatableponNPC : AbstractNPC {
    private val combatHandler =
        MultiSwingHandler(
            SwitchAttack(CombatStyle.MELEE.swingHandler, null).setUseHandler(true),
            SwitchAttack(CombatStyle.MELEE.swingHandler, null).setUseHandler(true),
            SwitchAttack(CombatStyle.MELEE.swingHandler, null).setUseHandler(true),
            SwitchAttack(CombatStyle.MAGIC.swingHandler, ANIMATION).setUseHandler(true),
        )

    constructor() : super(0, null)

    private constructor(id: Int, location: Location) : super(id, location) {
        this.isAggressive = true
    }

    /**
     * NPC setup.
     */
    override fun configure() {
        super.configure()
        super.getProperties().spell = SpellBook.MODERN.getSpell(7) as CombatSpell?
        super.getProperties().autocastSpell = SpellBook.MODERN.getSpell(7) as CombatSpell?
    }

    /**
     * Constructs a new instance of the npc.
     *
     * @param id the npc id.
     * @param location the location.
     * @param objects additional objects related to the npc.
     * @return a new instance of [CatableponNPC].
     */
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return CatableponNPC(id, location)
    }

    /**
     * Get swing handler.
     *
     * @param swing boolean indicating if a swing action is occurring.
     * @return the combat swing handler associated with the NPC.
     */
    override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
        return combatHandler
    }

    /**
     * Gets the npc.
     *
     * @return the array of npc ids.
     */
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CATABLEPON_4397, NPCs.CATABLEPON_4398, NPCs.CATABLEPON_4399)
    }

    companion object {
        private val ANIMATION = Animation(4272)
    }
}
