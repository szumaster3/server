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

    /**
     * The clue scroll associated with this NPC.
     */
    private var clueScroll: ClueScroll? = null

    /**
     * The player that the wizard is targeting.
     */
    var player: Player? = null

    /**
     * Default constructor.
     */
    constructor() : super(0, null)

    /**
     * Constructs the Saradomin wizard NPC at the given location.
     *
     * @param id The NPC id.
     * @param location The spawn location.
     */
    constructor(id: Int, location: Location?) : super(id, location, false) {
        this.isRespawn = false
    }

    /**
     * Constructs a new instance of this NPC.
     *
     * @param id The NPC id.
     * @param location The spawn location.
     * @param objects Additional construction arguments.
     * @return A new instance of [SaradominWizardNPC].
     */
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = SaradominWizardNPC(id, location)

    /**
     * Initializes the NPC after it is spawned.
     */
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

    /**
     * Called when the wizard is killed. Flags the player as having completed the fight.
     *
     * @param killer The entity that killed the NPC.
     */
    override fun finalizeDeath(killer: Entity) {
        if (killer is Player && killer == player) {
            killer.setAttribute("killed-wizard", clueScroll?.clueId)
        }
        super.finalizeDeath(killer)
    }

    /**
     * Handles actions every game tick.
     */
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
     * Returns the combat swing handler for this NPC.
     *
     * @param swing Whether to return the handler for a swing action.
     * @return The combat swing handler.
     */
    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = COMBAT_HANDLER

    /**
     * Determines if this NPC can attack the given entity.
     *
     * @param entity The target entity.
     * @return True if it can attack, false otherwise.
     */
    override fun canAttack(entity: Entity): Boolean =
        entity is Player && entity == player || super.canAttack(entity)

    /**
     * Determines if the given entity can attack this NPC.
     *
     * @param entity The attacking entity.
     * @param style The combat style used.
     * @param message Whether to send a failure message.
     * @return True if attackable, false otherwise.
     */
    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean = entity is Player && entity == player || super.isAttackable(entity, style, message)

    /**
     * Determines if the given entity can be selected as a combat target.
     *
     * @param target The potential combat target.
     * @return True if this NPC can target them, false otherwise.
     */
    override fun canSelectTarget(target: Entity): Boolean = target == player

    /**
     * Creates a new instance of this plugin class.
     *
     * @param arg Optional argument for instantiation.
     * @return A new instance of this NPC plugin.
     */
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> = super.newInstance(arg)

    /**
     * Returns the NPC ids associated with this class.
     *
     * @return An array of supported NPC ids.
     */
    override fun getIds(): IntArray = intArrayOf(NPCs.SARADOMIN_WIZARD_1264)

    companion object {
        /**
         * Handles switching between melee and magic combat styles.
         */
        private val COMBAT_HANDLER =
            MultiSwingHandler(
                SwitchAttack(CombatStyle.MELEE).setUseHandler(true),
                SwitchAttack(CombatStyle.MAGIC).setUseHandler(true),
            )
    }
}
