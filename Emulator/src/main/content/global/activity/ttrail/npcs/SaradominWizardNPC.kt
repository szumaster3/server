package content.global.activity.ttrail.npcs

import content.global.activity.ttrail.ClueScroll
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.map.Location
import core.game.world.map.RegionManager.getSpawnLocation
import core.plugin.Plugin
import org.rs.consts.NPCs

/**
 * Represents the Saradomin wizard NPC.
 */
class SaradominWizardNPC : AbstractNPC {
    private var clueScroll: ClueScroll? = null

    var player: Player? = null

    constructor() : super(0, null)

    constructor(id: Int, location: Location?) : super(id, location, false) {
        this.isRespawn = false
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = SaradominWizardNPC(id, location)

    override fun init() {
        player = getAttribute("player", null)
        clueScroll = getAttribute("clue", null)

        player?.let {
            location = getSpawnLocation(it, this) ?: it.location
        }

        super.init()
        properties.spell = SpellBook.MODERN.getSpell(41) as CombatSpell?
        properties.autocastSpell = properties.spell
    }

    override fun finalizeDeath(killer: Entity) {
        if (killer is Player && killer == player) {
            killer.setAttribute("killed-wizard", clueScroll?.clueId)
        }
        super.finalizeDeath(killer)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        player?.let {
            if (it.location.getDistance(getLocation()) > 10 || !it.isActive) {
                clear()
            } else if (!properties.combatPulse.isAttacking) {
                properties.combatPulse.attack(it)
            }
        }
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = COMBAT_HANDLER

    override fun canAttack(entity: Entity): Boolean = entity is Player && entity == player || super.canAttack(entity)

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean = entity is Player && entity == player || super.isAttackable(entity, style, message)

    override fun canSelectTarget(target: Entity): Boolean = target == player

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> = super.newInstance(arg)

    override fun getIds(): IntArray = intArrayOf(NPCs.SARADOMIN_WIZARD_1264)

    companion object {
        private val COMBAT_HANDLER =
            MultiSwingHandler(
                SwitchAttack(CombatStyle.MELEE).setUseHandler(true),
                SwitchAttack(CombatStyle.MAGIC).setUseHandler(true),
            )
    }
}
