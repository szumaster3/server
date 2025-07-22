package content.global.activity.ttrail.npc

import content.global.activity.ttrail.ClueScroll
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.map.Location
import core.game.world.map.RegionManager.getSpawnLocation
import core.plugin.Plugin
import org.rs.consts.NPCs

/**
 * Represents the Zamorak wizard NPC.
 * @author Vexia
 */
class ZamorakWizardNPC : AbstractNPC {

    /**
     * The clue scroll associated with this wizard.
     */
    var clueScroll: ClueScroll? = null
        private set

    /**
     * The player that the wizard is targeting.
     */
    var player: Player? = null

    /**
     * Default constructor.
     */
    constructor() : super(0, null)

    /**
     * Constructs a new Zamorak wizard NPC.
     *
     * @param id the NPC ID
     * @param location the spawn location of the wizard
     */
    constructor(id: Int, location: Location?) : super(id, location, false) {
        this.isRespawn = false
    }

    /**
     * Constructs a new instance of this NPC with the given parameters.
     *
     * @param id the NPC ID
     * @param location the NPC's spawn location
     * @param objects additional construction arguments
     * @return the constructed [ZamorakWizardNPC] instance
     */
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = ZamorakWizardNPC(id, location)

    /**
     * Initializes the NPC after construction.
     */
    override fun init() {
        player = getAttribute("player", null)
        clueScroll = getAttribute("clue", null)

        player?.let {
            location = getSpawnLocation(it, this) ?: it.location
        }

        super.init()
        properties.spell = SpellBook.MODERN.getSpell(43) as CombatSpell?
        properties.autocastSpell = properties.spell
    }

    /**
     * Handles the wizard's death logic.
     */
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

    /**
     * Checks if the NPC is attackable.
     */
    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean = (entity is Player && entity == player) || super.isAttackable(entity, style, message)

    /**
     * Checks whether this NPC can attack the given entity.
     */
    override fun canAttack(entity: Entity): Boolean = (entity is Player && entity == player) || super.canAttack(entity)

    /**
     * Checks if the given entity is a valid combat target.
     */
    override fun canSelectTarget(target: Entity): Boolean = target == player

    /**
     * Creates a new instance of this plugin class.
     */
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> = super.newInstance(arg)

    /**
     * Returns the NPC ids this class should be associated with.
     *
     * @return an array of NPC ids.
     */
    override fun getIds(): IntArray = intArrayOf(NPCs.ZAMORAK_WIZARD_1007)
}
