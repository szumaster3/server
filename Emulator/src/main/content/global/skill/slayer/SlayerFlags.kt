package content.global.skill.slayer

class SlayerFlags {
    var taskFlags = 0
    var rewardFlags = 0
    var equipmentFlags = 0
    var completedTasks = 0
    var taskStreak = 0
    val removed: ArrayList<Tasks> = ArrayList(4)

    fun getMaster(): SlayerMaster {
        val ordinal = taskFlags and 0xF
        return SlayerMaster.values()[ordinal]
    }

    fun setMaster(master: SlayerMaster) {
        taskFlags = (taskFlags - (taskFlags and 0xF)) or master.ordinal
    }

    fun getTask(): Tasks {
        val ordinal = (taskFlags shr 4) and 0x7F
        return Tasks.values()[ordinal]
    }

    fun setTask(tasks: Tasks) {
        taskFlags = (taskFlags - (getTask().ordinal shl 4)) or (tasks.ordinal shl 4)
    }

    fun getTaskAmount(): Int {
        return (taskFlags shr 11) and 0xFF
    }

    fun setTaskAmount(amount: Int) {
        taskFlags = (taskFlags - (getTaskAmount() shl 11)) or (amount shl 11)
    }

    fun decrementTaskAmount(amount: Int) {
        setTaskAmount(getTaskAmount() - amount)
    }

    fun canEarnPoints(): Boolean {
        return (taskFlags shr 20) and 1 == 1
    }

    fun flagCanEarnPoints() {
        taskFlags = taskFlags or (1 shl 20)
    }

    fun isBroadsUnlocked(): Boolean {
        return rewardFlags and 1 == 1
    }

    fun unlockBroads() {
        rewardFlags = rewardFlags or 1
    }

    fun isRingUnlocked(): Boolean {
        return (rewardFlags shr 1) and 1 == 1
    }

    fun unlockRing() {
        rewardFlags = rewardFlags or (1 shl 1)
    }

    fun isHelmUnlocked(): Boolean {
        return (rewardFlags shr 2) and 1 == 1
    }

    fun unlockHelm() {
        rewardFlags = rewardFlags or (1 shl 2)
    }

    fun setPoints(amount: Int) {
        rewardFlags = (rewardFlags - (getPoints() shl 15)) or (amount shl 15)
    }

    fun getPoints(): Int {
        return (rewardFlags shr 15) and 0xFFFF
    }

    fun incrementPoints(amount: Int) {
        setPoints(getPoints() + amount)
    }

    fun clearTask() {
        setTask(Tasks.values()[0])
        setTaskAmount(0)
    }

    fun hasTask(): Boolean {
        return getTaskAmount() != 0
    }

    fun fullClear() {
        taskFlags = 0
        rewardFlags = 0
        equipmentFlags = 0
        completedTasks = 0
        taskStreak = 0
    }
}
