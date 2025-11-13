package content.region.misthalin.barbarian_village.stronghold.npc

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
import shared.consts.NPCs

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

    override fun configure() {
        super.configure()
        super.getProperties().spell = SpellBook.MODERN.getSpell(7) as CombatSpell?
        super.getProperties().autocastSpell = SpellBook.MODERN.getSpell(7) as CombatSpell?
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = CatableponNPC(id, location)

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = combatHandler

    override fun getIds(): IntArray = intArrayOf(NPCs.CATABLEPON_4397, NPCs.CATABLEPON_4398, NPCs.CATABLEPON_4399)

    companion object {
        private val ANIMATION = Animation(4272)
    }
}
