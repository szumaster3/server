package core.game.node.entity.npc

import core.api.ContentInterface
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.RegionManager
import core.game.world.map.path.ClipMaskSupplier
import core.game.world.map.path.Pathfinder

/**
 * Defines the behavior of an NPC, allowing customization of interactions and AI logic.
 * @property ids The NPC IDs that this behavior applies to.
 */
open class NPCBehavior(
    vararg val ids: Int = intArrayOf(),
) : ContentInterface {
    companion object {
        private val idMap = HashMap<Int, NPCBehavior>()
        private val defaultBehavior = NPCBehavior()

        /**
         * Retrieves the NPCBehavior associated with a specific NPC ID.
         * @param id The ID of the NPC.
         * @return The corresponding NPCBehavior, or a default behavior if none is found.
         */
        @JvmStatic
        fun forId(id: Int): NPCBehavior = idMap[id] ?: defaultBehavior

        /**
         * Registers an NPCBehavior for specific NPC IDs.
         * @param ids The array of NPC IDs.
         * @param behavior The behavior to associate with the given IDs.
         */
        fun register(
            ids: IntArray,
            behavior: NPCBehavior,
        ) {
            ids.forEach { idMap[it] = behavior }
        }
    }

    /**
     * Standard clipping mask supplier that retrieves the clipping flag from the region manager.
     */
    object StandardClipMaskSupplier : ClipMaskSupplier {
        override fun getClippingFlag(
            z: Int,
            x: Int,
            y: Int,
        ): Int = RegionManager.getClippingFlag(z, x, y)
    }

    /**
     * Called every tick to update NPC behavior.
     * @param self The NPC instance.
     * @return Whether the tick operation should continue.
     */
    open fun tick(self: NPC): Boolean = true

    /**
     * Called before an NPC receives damage.
     * @param self The NPC receiving damage.
     * @param attacker The entity causing damage.
     * @param state The battle state containing combat information.
     */
    open fun beforeDamageReceived(
        self: NPC,
        attacker: Entity,
        state: BattleState,
    ) {
    }

    /**
     * Called after an NPC receives damage.
     * @param self The NPC receiving damage.
     * @param attacker The entity causing damage.
     * @param state The battle state containing combat information.
     */
    open fun afterDamageReceived(
        self: NPC,
        attacker: Entity,
        state: BattleState,
    ) {
    }

    /**
     * Called before an NPC's attack is finalized.
     * @param self The NPC attacking.
     * @param victim The entity being attacked.
     * @param state The battle state containing combat information.
     */
    open fun beforeAttackFinalized(
        self: NPC,
        victim: Entity,
        state: BattleState,
    ) {
    }

    /**
     * Called when an NPC is removed from the game world.
     * @param self The NPC being removed.
     */
    open fun onRemoval(self: NPC) {}

    /**
     * Called when an NPC is created in the game world.
     * @param self The NPC being created.
     */
    open fun onCreation(self: NPC) {}

    /**
     * Called when an NPC respawns in the game world.
     * @param self The NPC respawning.
     */
    open fun onRespawn(self: NPC) {}

    /**
     * Called when an NPC's death sequence starts.
     * @param self The NPC dying.
     * @param killer The entity responsible for the death.
     */
    open fun onDeathStarted(
        self: NPC,
        killer: Entity,
    ) {
    }

    /**
     * Called when an NPC's death sequence finishes.
     * @param self The NPC that has died.
     * @param killer The entity responsible for the death.
     */
    open fun onDeathFinished(
        self: NPC,
        killer: Entity,
    ) {
    }

    /**
     * Called when an NPC's drop table is rolled for loot.
     * @param self The NPC that has died.
     * @param killer The entity responsible for the death.
     * @param drops The list of items dropped.
     */
    open fun onDropTableRolled(
        self: NPC,
        killer: Entity,
        drops: ArrayList<Item>,
    ) {
    }

    /**
     * Determines if an NPC can be attacked by an entity.
     * @param self The NPC being attacked.
     * @param attacker The entity initiating the attack.
     * @param style The combat style used.
     * @param shouldSendMessage Whether a message should be sent to the player.
     * @return True if the NPC can be attacked, false otherwise.
     */
    open fun canBeAttackedBy(
        self: NPC,
        attacker: Entity,
        style: CombatStyle,
        shouldSendMessage: Boolean,
    ): Boolean {
        if (attacker is Player && !self.definition.hasAction("attack")) {
            return false
        }
        return true
    }

    /**
     * Determines if multi-combat restrictions should be ignored.
     * @param self The NPC in combat.
     * @param victim The entity the NPC is fighting.
     * @return True if multi-combat restrictions should be ignored, false otherwise.
     */
    open fun shouldIgnoreMultiRestrictions(
        self: NPC,
        victim: Entity,
    ): Boolean = false

    /**
     * Provides a custom combat swing handler override.
     * @param self The NPC performing the attack.
     * @param original The original combat swing handler.
     * @return The overridden combat swing handler.
     */
    open fun getSwingHandlerOverride(
        self: NPC,
        original: CombatSwingHandler,
    ): CombatSwingHandler = original

    /**
     * Provides a custom pathfinder override.
     * @param self The NPC for which the pathfinder is being set.
     * @return The overridden pathfinder, or null for default behavior.
     */
    open fun getPathfinderOverride(self: NPC): Pathfinder? = null

    /**
     * Provides a custom clipping supplier override.
     * @param self The NPC for which the clipping supplier is being set.
     * @return The overridden clipping supplier, or the standard one by default.
     */
    open fun getClippingSupplier(self: NPC): ClipMaskSupplier? = StandardClipMaskSupplier

    /**
     * Determines the experience multiplier for an NPC.
     * @param self The NPC providing experience.
     * @param attacker The entity receiving experience.
     * @return The experience multiplier.
     */
    open fun getXpMultiplier(
        self: NPC,
        attacker: Entity,
    ): Double = 1.0
}