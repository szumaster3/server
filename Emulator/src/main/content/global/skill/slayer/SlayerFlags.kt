package content.global.skill.slayer

/**
 * Represents slayer-related progress and unlocks for a player.
 */
class SlayerFlags {

    /**
     * Bit flags for storing slayer task-related data such as master, task, task amount, and point eligibility.
     */
    var taskFlags = 0

    /**
     * Bit flags for storing slayer reward unlocks and slayer points.
     */
    var rewardFlags = 0

    /**
     * Bit flags for storing slayer equipment unlocks.
     */
    var equipmentFlags = 0

    /**
     * The number of tasks the player has completed.
     */
    var completedTasks = 0

    /**
     * The number of tasks completed in a row without breaking the streak.
     */
    var taskStreak = 0

    /**
     * The list of tasks that the player has removed or blocked.
     */
    val removed: ArrayList<Tasks> = ArrayList(4)

    /**
     * Returns the currently assigned [SlayerMaster].
     */
    fun getMaster(): SlayerMaster {
        val ordinal = taskFlags and 0xF
        return SlayerMaster.values()[ordinal]
    }

    /**
     * Sets the [SlayerMaster] the player is currently assigned to.
     */
    fun setMaster(master: SlayerMaster) {
        taskFlags = (taskFlags - (taskFlags and 0xF)) or master.ordinal
    }

    /**
     * Returns the currently assigned [Tasks] slayer task.
     */
    fun getTask(): Tasks {
        val ordinal = (taskFlags shr 4) and 0x7F
        return Tasks.values()[ordinal]
    }

    /**
     * Sets the current slayer task.
     */
    fun setTask(tasks: Tasks) {
        taskFlags = (taskFlags - (getTask().ordinal shl 4)) or (tasks.ordinal shl 4)
    }

    /**
     * Returns the amount remaining for the current task.
     */
    fun getTaskAmount(): Int = (taskFlags shr 11) and 0xFF

    /**
     * Sets the number of monsters remaining for the current task.
     */
    fun setTaskAmount(amount: Int) {
        taskFlags = (taskFlags - (getTaskAmount() shl 11)) or (amount shl 11)
    }

    /**
     * Decreases the current task amount by a specified [amount].
     */
    fun decrementTaskAmount(amount: Int) {
        setTaskAmount(getTaskAmount() - amount)
    }

    /**
     * Returns true if the player is currently eligible to earn slayer points.
     */
    fun canEarnPoints(): Boolean = (taskFlags shr 20) and 1 == 1

    /**
     * Flags the player as eligible to earn slayer points upon task completion.
     */
    fun flagCanEarnPoints() {
        taskFlags = taskFlags or (1 shl 20)
    }

    /**
     * Returns true if the player has unlocked Broad Bolts.
     */
    fun isBroadsUnlocked(): Boolean = rewardFlags and 1 == 1

    /**
     * Unlocks the ability to use or purchase Broad Bolts.
     */
    fun unlockBroads() {
        rewardFlags = rewardFlags or 1
    }

    /**
     * Returns true if the player has unlocked the slayer ring.
     */
    fun isRingUnlocked(): Boolean = (rewardFlags shr 1) and 1 == 1

    /**
     * Unlocks the ability to use or craft the slayer ring.
     */
    fun unlockRing() {
        rewardFlags = rewardFlags or (1 shl 1)
    }

    /**
     * Returns true if the player has unlocked the slayer helmet.
     */
    fun isHelmUnlocked(): Boolean = (rewardFlags shr 2) and 1 == 1

    /**
     * Unlocks the ability to craft or wear the slayer helmet.
     */
    fun unlockHelm() {
        rewardFlags = rewardFlags or (1 shl 2)
    }

    /**
     * Sets the current slayer points to the specified [amount].
     */
    fun setPoints(amount: Int) {
        rewardFlags = (rewardFlags - (getPoints() shl 15)) or (amount shl 15)
    }

    /**
     * Returns the number of slayer points the player currently has.
     */
    fun getPoints(): Int = (rewardFlags shr 15) and 0xFFFF

    /**
     * Increases the player’s slayer points by the specified [amount].
     */
    fun incrementPoints(amount: Int) {
        setPoints(getPoints() + amount)
    }

    /**
     * Clears the current slayer task.
     */
    fun clearTask() {
        setTask(Tasks.values()[0])
        setTaskAmount(0)
    }

    /**
     * Returns true if the player currently has a slayer task assigned.
     */
    fun hasTask(): Boolean = getTaskAmount() != 0

    /**
     * Clears all slayer-related progress and unlocks.
     */
    fun fullClear() {
        taskFlags = 0
        rewardFlags = 0
        equipmentFlags = 0
        completedTasks = 0
        taskStreak = 0
    }
}
