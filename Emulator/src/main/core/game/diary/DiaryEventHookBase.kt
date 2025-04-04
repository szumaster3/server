package core.game.diary

import core.api.*
import core.api.Event
import core.game.event.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.zone.ZoneBorders

/**
 * Base class for handling diary-related event hooks.
 * This class implements [MapArea] and [LoginListener], allowing it to track events related to diaries.
 *
 * @property diaryType The type of diary associated with this event hook.
 */
abstract class DiaryEventHookBase(
    private val diaryType: DiaryType,
) : MapArea, LoginListener {

    protected companion object {
        /**
         * Executes the given handler if the entity is an eligible [Player].
         *
         * @param entity The entity to check.
         * @param event The event associated with the entity.
         * @param handler The handler function to execute if the entity is a valid player.
         */
        private fun <T> forEligibleEntityDo(
            entity: Entity,
            event: T,
            handler: (Player, T) -> Unit,
        ) {
            if (entity !is Player || entity.isArtificial) return
            handler(entity, event)
        }
    }

    /**
     * Handles event processing for diary-related events.
     *
     * @param T The type of event being handled.
     * @property owner The [DiaryEventHookBase] instance that owns this handler.
     * @property handler The function to process the event.
     */
    class EventHandler<T : core.game.event.Event>(
        private val owner: DiaryEventHookBase,
        private val handler: (Player, T) -> Unit,
    ) : EventHook<T> {
        override fun process(entity: Entity, event: T) {
            forEligibleEntityDo(entity, event, handler)
        }
    }

    /**
     * The list of tasks associated with this diary area.
     */
    open val areaTasks get() = arrayOf<DiaryAreaTask>()

    /**
     * Defines the borders of the area associated with this diary event hook.
     *
     * @return An array of [ZoneBorders] defining the area.
     */
    final override fun defineAreaBorders(): Array<ZoneBorders> = areaTasks.map { it.zoneBorders }.toTypedArray()

    /**
     * Called when an entity enters the area.
     *
     * @param entity The entity entering the area.
     */
    final override fun areaEnter(entity: Entity) {
        if (entity !is Player || entity.isArtificial) return
        onAreaVisited(entity)
    }

    /**
     * Called when an entity leaves the area.
     *
     * @param entity The entity leaving the area.
     * @param logout Whether the entity is leaving due to logout.
     */
    final override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity !is Player || entity.isArtificial) return
        onAreaLeft(entity)
    }

    /**
     * Called when a player logs in, hooking all relevant diary-related events.
     *
     * @param player The player logging in.
     */
    final override fun login(player: Player) {
        player.hook(Event.ResourceProduced, EventHandler(this, ::onResourceProduced))
        player.hook(Event.NPCKilled, EventHandler(this, ::onNpcKilled))
        player.hook(Event.Teleported, EventHandler(this, ::onTeleported))
        player.hook(Event.FireLit, EventHandler(this, ::onFireLit))
        player.hook(Event.LightSourceLit, EventHandler(this, ::onLightSourceLit))
        player.hook(Event.Interacted, EventHandler(this, ::onInteracted))
        player.hook(Event.ButtonClicked, EventHandler(this, ::onButtonClicked))
        player.hook(Event.DialogueOpened, EventHandler(this, ::onDialogueOpened))
        player.hook(Event.DialogueOptionSelected, EventHandler(this, ::onDialogueOptionSelected))
        player.hook(Event.DialogueClosed, EventHandler(this, ::onDialogueClosed))
        player.hook(Event.UsedWith, EventHandler(this, ::onUsedWith))
        player.hook(Event.PickedUp, EventHandler(this, ::onPickedUp))
        player.hook(Event.InterfaceOpened, EventHandler(this, ::onInterfaceOpened))
        player.hook(Event.AttributeSet, EventHandler(this, ::onAttributeSet))
        player.hook(Event.AttributeRemoved, EventHandler(this, ::onAttributeRemoved))
        player.hook(Event.VarbitSet, EventHandler(this, ::onVarbitSet))
        player.hook(Event.VarbitRemoved, EventHandler(this, ::onVarbitRemoved))
        player.hook(Event.SpellCast, EventHandler(this, ::onSpellCast))
        player.hook(Event.ItemAlchemized, EventHandler(this, ::onItemAlchemized))
        player.hook(Event.ItemEquipped, EventHandler(this, ::onItemEquipped))
        player.hook(Event.ItemUnequipped, EventHandler(this, ::onItemUnequipped))
        player.hook(Event.ItemPurchased, EventHandler(this, ::onItemPurchasedFromShop))
        player.hook(Event.ItemSold, EventHandler(this, ::onItemSoldToShop))
        player.hook(Event.JobAssigned, EventHandler(this, ::onJobAssigned))
        player.hook(Event.FairyRingDialed, EventHandler(this, ::onFairyRingDialed))
        player.hook(Event.SummoningPointsRecharged, EventHandler(this, ::onSummoningPointsRecharged))
        player.hook(Event.PrayerPointsRecharged, EventHandler(this, ::onPrayerPointsRecharged))
    }

    /**
     * Marks a task as completed if it hasn't been completed yet.
     *
     * @param player The player completing the task.
     * @param level The diary level associated with the task.
     * @param task The task ID.
     * @param attribute The attribute name tracking the task's progress.
     */
    protected fun fulfillTaskRequirement(
        player: Player,
        level: DiaryLevel,
        task: Int,
        attribute: String,
    ) {
        if (getAttribute(player, attribute, false)) return
        player.achievementDiaryManager.updateTask(player, diaryType, findIndexFor(level), task, false)
        setAttribute(player, "/save:$attribute", true)
    }

    /**
     * Executes a given action when a player's attribute condition is fulfilled.
     *
     * @param player The player whose attribute is being checked.
     * @param attribute The name of the attribute to check.
     * @param then The action to execute when the attribute condition is met.
     */
    protected fun whenTaskRequirementFulfilled(
        player: Player,
        attribute: String,
        then: () -> Unit,
    ) {
        if (getAttribute(player, attribute, false)) {
            then()
            removeAttribute(player, attribute)
        }
    }

    /**
     * Increments progress on a task for the given player, checking if the task is completed or not.
     *
     * @param player The player whose task progress is being updated.
     * @param level The diary level the task belongs to.
     * @param task The specific task to progress.
     * @param attribute The attribute representing the progress of the task.
     * @param maxProgress The maximum progress value for the task.
     */
    protected fun progressIncrementalTask(
        player: Player,
        level: DiaryLevel,
        task: Int,
        attribute: String,
        maxProgress: Int,
    ) {
        if (isTaskCompleted(player, level, task)) return

        val newValue = getAttribute(player, attribute, 0) + 1

        setAttribute(player, "/save:$attribute", newValue)

        if (newValue < maxProgress) {
            player.achievementDiaryManager.updateTask(player, diaryType, findIndexFor(level), task, false)
        } else {
            finishTask(player, level, task)
            removeAttribute(player, attribute)
        }
    }

    /**
     * Updates progress on a flagged task for the given player, where progress is based on bitwise operations.
     *
     * @param player The player whose task progress is being updated.
     * @param level The diary level the task belongs to.
     * @param task The specific task to progress.
     * @param attribute The attribute representing the current progress of the task.
     * @param bit The bit value that represents the current progress state.
     * @param targetValue The target value that indicates task completion.
     */
    protected fun progressFlaggedTask(
        player: Player,
        level: DiaryLevel,
        task: Int,
        attribute: String,
        bit: Int,
        targetValue: Int,
    ) {
        if (isTaskCompleted(player, level, task)) {
            return
        }

        val oldValue = getAttribute(player, attribute, 0)
        val newValue = oldValue or bit

        if (newValue != targetValue) {
            if ((oldValue and bit) != 0) return

            setAttribute(player, "/save:$attribute", newValue)

            player.achievementDiaryManager.updateTask(player, diaryType, findIndexFor(level), task, false)
        } else {
            finishTask(player, level, task)
            removeAttribute(player, attribute)
        }
    }

    /**
     * Marks a task as finished for a player.
     *
     * @param player The player whose task is being marked as finished.
     * @param level The diary level the task belongs to.
     * @param task The specific task to finish.
     */
    protected fun finishTask(
        player: Player,
        level: DiaryLevel,
        task: Int,
    ) {
        player.achievementDiaryManager.finishTask(player, diaryType, findIndexFor(level), task)
    }

    /**
     * Checks if a task is already completed for a given player and diary level.
     *
     * @param player The player whose task completion status is being checked.
     * @param level The diary level the task belongs to.
     * @param task The specific task to check.
     * @return `true` if the task is completed, otherwise `false`.
     */
    private fun isTaskCompleted(
        player: Player,
        level: DiaryLevel,
        task: Int,
    ): Boolean = player.achievementDiaryManager.hasCompletedTask(diaryType, findIndexFor(level), task)

    /**
     * Finds the index of a specific diary level in the diary type.
     *
     * @param level The diary level to find the index for.
     * @return The index of the level in the diary.
     * @throws IllegalArgumentException If the level cannot be found in the diary type.
     */
    private fun findIndexFor(level: DiaryLevel): Int {
        val levelName = level.name.lowercase().replaceFirstChar { c -> c.uppercase() }
        val levelIndex = diaryType.levelNames.indexOf(levelName)

        if (levelIndex < 0) {
            throw IllegalArgumentException("'$levelName' was not found in diary '$diaryType'.")
        }

        return levelIndex
    }

    /**
     * Called when a player visits a certain area.
     * Completes any tasks that are satisfied by this action.
     *
     * @param player The player who visited the area.
     */
    protected open fun onAreaVisited(player: Player) {
        areaTasks.forEach {
            it.whenSatisfied(player) {
                finishTask(player, it.diaryLevel, it.taskId)
            }
        }
    }

    protected open fun onAreaLeft(player: Player) {}
    protected open fun onResourceProduced(player: Player, event: ResourceProducedEvent, ) {}
    protected open fun onNpcKilled(player: Player, event: NPCKillEvent, ) {}
    protected open fun onTeleported(player: Player, event: TeleportEvent, ) {}
    protected open fun onFireLit(player: Player, event: LitFireEvent, ) {}
    protected open fun onLightSourceLit(player: Player, event: LitLightSourceEvent, ) {}
    protected open fun onInteracted(player: Player, event: InteractionEvent, ) {}
    protected open fun onButtonClicked(player: Player, event: ButtonClickEvent, ) {}
    protected open fun onDialogueOpened(player: Player, event: DialogueOpenEvent, ) {}
    protected open fun onDialogueClosed(player: Player, event: DialogueCloseEvent, ) {}
    protected open fun onDialogueOptionSelected(player: Player, event: DialogueOptionSelectionEvent, ) {}
    protected open fun onUsedWith(player: Player, event: UseWithEvent, ) {}
    protected open fun onPickedUp(player: Player, event: PickUpEvent, ) {}
    protected open fun onInterfaceOpened(player: Player, event: InterfaceOpenEvent, ) {}
    protected open fun onInterfaceClosed(player: Player, event: InterfaceCloseEvent, ) {}
    protected open fun onAttributeSet(player: Player, event: AttributeSetEvent, ) {}
    protected open fun onAttributeRemoved(player: Player, event: AttributeRemoveEvent, ) {}
    protected open fun onVarbitSet(player: Player, event: VarbitSetEvent, ) {}
    protected open fun onVarbitRemoved(player: Player, event: VarbitRemoveEvent, ) {}
    protected open fun onSpellCast(player: Player, event: SpellCastEvent, ) {}
    protected open fun onItemAlchemized(player: Player, event: ItemAlchemizationEvent, ) {}
    protected open fun onItemEquipped(player: Player, event: ItemEquipEvent, ) {}
    protected open fun onItemUnequipped(player: Player, event: ItemUnequipEvent, ) {}
    protected open fun onItemPurchasedFromShop(player: Player, event: ItemShopPurchaseEvent, ) {}
    protected open fun onItemSoldToShop(player: Player, event: ItemShopSellEvent, ) {}
    protected open fun onJobAssigned(player: Player, event: JobAssignmentEvent, ) {}
    protected open fun onFairyRingDialed(player: Player, event: FairyRingDialEvent, ) {}
    protected open fun onSummoningPointsRecharged(player: Player, event: SummoningPointsRechargeEvent, ) {}
    protected open fun onPrayerPointsRecharged(player: Player, event: PrayerPointsRechargeEvent, ) {}
}
