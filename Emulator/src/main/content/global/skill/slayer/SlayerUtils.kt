package content.global.skill.slayer

import core.api.setVarp
import core.game.node.entity.combat.BattleState
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction
import org.rs.consts.Items

/**
 * Utilities for Slayer-related helper functions.
 */
object SlayerUtils {
    /**
     * Generates a Slayer task for the given player and Slayer master.
     *
     * @param player the player to assign a task to
     * @param master the Slayer master providing the task
     * @return the assigned [Tasks] or null if no suitable task found
     */
    fun generate(
        player: Player,
        master: SlayerMaster,
    ): Tasks? {
        val tasks: MutableList<SlayerMaster.Task?> = ArrayList(10)
        val taskWeightSum = intArrayOf(0)
        master.tasks
            .stream()
            .filter { task: SlayerMaster.Task ->
                canBeAssigned(player, task.task) && task.task.combatCheck <= player.properties.currentCombatLevel
            }.forEach { task: SlayerMaster.Task ->
                taskWeightSum[0] += task.weight
                tasks.add(task)
            }
        tasks.shuffle(RandomFunction.RANDOM)
        var rnd = RandomFunction.random(taskWeightSum[0])
        for (task in tasks) {
            if (rnd < task!!.weight) return task.task
            rnd -= task.weight
        }
        return null
    }

    /**
     * Checks whether the given Slayer task can be assigned to the player.
     * @param player the player to check eligibility for
     * @param task the Slayer task to check
     * @return true if the task can be assigned, false otherwise
     */
    private fun canBeAssigned(
        player: Player,
        task: Tasks,
    ): Boolean =
        player
            .getSkills()
            .getLevel(Skills.SLAYER) >= task.levelReq &&
            !SlayerManager
                .getInstance(player)
                .flags.removed
                .contains(task) &&
            task.hasQuestRequirements(
                player,
            )

    /**
     * Assigns a Slayer task to the player from the given Slayer master.
     *
     * @param player the player to assign the task to
     * @param task the Slayer task to assign
     * @param master the Slayer master assigning the task
     */
    fun assign(
        player: Player,
        task: Tasks,
        master: SlayerMaster,
    ) {
        SlayerManager.getInstance(player).master = master
        SlayerManager.getInstance(player).task = task
        SlayerManager.getInstance(player).amount =
            RandomFunction.random(master.assignmentCount[0], master.assignmentCount[1])
        if (master == SlayerMaster.DURADEL) {
            player.achievementDiaryManager.finishTask(player, DiaryType.KARAMJA, 2, 8)
        } else if (master == SlayerMaster.VANNAKA) {
            player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 1, 14)
        }
        setVarp(player, 2502, SlayerManager.getInstance(player).flags.taskFlags shr 4)
    }

    /**
     * Checks if the player currently has a broad weapon equipped or
     * is using broad ammunition or the correct spell in the modern spellbook.
     *
     * @param player the player whose equipment is checked
     * @param state the battle state containing weapon, ammunition, and spell info
     * @return true if a broad weapon, broad ammo, or the specific spell is used; false otherwise
     */
    @JvmStatic
    fun hasBroadWeaponEquipped(
        player: Player,
        state: BattleState,
    ): Boolean =
        (
            state.weapon != null &&
                state.weapon.id == Items.LEAF_BLADED_SPEAR_4158 ||
                state.weapon != null &&
                state.weapon.id == Items.LEAF_BLADED_SWORD_13290 ||
                state.ammunition != null &&
                (
                    state.ammunition.itemId == Items.BROAD_ARROW_4160 ||
                        state.ammunition.itemId == Items.BROAD_TIPPED_BOLTS_13280
                ) ||
                state.spell != null &&
                state.spell.spellId == 31 &&
                player.spellBookManager.spellBook == SpellBook.MODERN.interfaceId
        )
}
