package content.global.skill.slayer

import com.google.gson.JsonObject
import content.global.plugins.item.equipment.gloves.FOGGlovesListener
import core.api.*
import core.cache.def.impl.NPCDefinition
import core.game.event.EventHook
import core.game.event.NPCKillEvent
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import shared.consts.Items

class SlayerManager(
    val player: Player? = null,
) : LoginListener,
    PersistPlayer,
    EventHook<NPCKillEvent> {
    override fun login(player: Player) {
        val instance = SlayerManager(player)
        player.hook(Event.NPCKilled, instance)
        setAttribute(player, "slayer-manager", instance)
    }

    @JvmField
    val flags: SlayerFlags = SlayerFlags()

    override fun savePlayer(
        player: Player,
        save: JsonObject,
    ) {
        val slayer = JsonObject()
        val slayerManager = getInstance(player)

        if (slayerManager.removed.isNotEmpty()) {
            val removedTasks = com.google.gson.JsonArray()
            slayerManager.removed.forEach {
                removedTasks.add(it.ordinal)
            }
            slayer.add("removedTasks", removedTasks)
        }

        slayer.addProperty("taskStreak", slayerManager.flags.taskStreak)
        slayer.addProperty("totalTasks", slayerManager.flags.completedTasks)
        slayer.addProperty("equipmentFlags", slayerManager.flags.equipmentFlags)
        slayer.addProperty("taskFlags", slayerManager.flags.taskFlags)
        slayer.addProperty("rewardFlags", slayerManager.flags.rewardFlags)

        save.add("slayer", slayer)
    }

    override fun parsePlayer(
        player: Player,
        data: JsonObject,
    ) {
        val slayerData = data.getAsJsonObject("slayer") ?: return
        val m = slayerData.get("master")
        val flags = getInstance(player).flags

        if (m != null && !m.isJsonNull) {
            flags.setMaster(SlayerMaster.forId(m.asInt))
        }

        val t = slayerData.get("taskId")
        if (t != null && !t.isJsonNull) {
            flags.setTask(Tasks.values()[t.asInt])
        }

        val a = slayerData.get("taskAmount")
        if (a != null && !a.isJsonNull) {
            flags.setTaskAmount(a.asInt)
        }

        val points = slayerData.get("points")
        if (points != null && !points.isJsonNull) {
            flags.setPoints(points.asInt)
        }

        val taskStreak = slayerData.get("taskStreak")
        if (taskStreak != null && !taskStreak.isJsonNull) {
            flags.taskStreak = taskStreak.asInt
        }

        val learnedArray = slayerData.getAsJsonArray("learned_rewards")
        if (learnedArray != null) {
            for (i in 0 until learnedArray.size()) {
                val unlocked = learnedArray.get(i).asBoolean
                when (i) {
                    0 -> if (unlocked) flags.unlockBroads()
                    1 -> if (unlocked) flags.unlockRing()
                    2 -> if (unlocked) flags.unlockHelm()
                }
            }
        }

        val removedTasks = slayerData.getAsJsonArray("removedTasks")
        if (removedTasks != null) {
            for (i in 0 until removedTasks.size()) {
                val taskOrdinal = removedTasks.get(i).asInt
                flags.removed.add(Tasks.values()[taskOrdinal])
            }
        }

        val completedTasks = slayerData.get("totalTasks")
        if (completedTasks != null && !completedTasks.isJsonNull) {
            flags.completedTasks = completedTasks.asInt
        }
        if (flags.completedTasks >= 4) flags.flagCanEarnPoints()

        slayerData.get("equipmentFlags")?.takeIf { !it.isJsonNull }?.let {
            flags.equipmentFlags = it.asInt
        }

        slayerData.get("taskFlags")?.takeIf { !it.isJsonNull }?.let {
            flags.taskFlags = it.asInt
        }

        slayerData.get("rewardFlags")?.takeIf { !it.isJsonNull }?.let {
            flags.rewardFlags = it.asInt
        }
    }

    override fun process(
        entity: Entity,
        event: NPCKillEvent,
    ) {
        val npc = event.npc
        val player = entity as? Player ?: return
        val slayer = getInstance(player)
        val flags = slayer.flags

        if (slayer.hasTask() && npc.id in slayer.task!!.npcs) {
            var xp = npc.skills.maximumLifepoints.toDouble()
            if (slayer.task!!.dragon && inEquipment(player, Items.DRAGON_SLAYER_GLOVES_12862)) {
                xp *= 1.15
                FOGGlovesListener.updateCharges(player)
            }
            rewardXP(player, Skills.SLAYER, xp)
            slayer.decrementAmount(1)
            if (slayer.hasTask()) return
            flags.taskStreak = flags.taskStreak + 1
            flags.completedTasks = flags.completedTasks + 1
            if ((flags.completedTasks > 4 || flags.canEarnPoints()) &&
                flags.getMaster() != SlayerMaster.TURAEL &&
                flags.getPoints() < 64000
            ) {
                var points = flags.getMaster().taskPoints[0]
                if (flags.taskStreak % 50 == 0) {
                    points = flags.getMaster().taskPoints[2]
                } else if (flags.taskStreak % 10 == 0) {
                    points = flags.getMaster().taskPoints[1]
                }
                flags.incrementPoints(points)
                if (flags.getPoints() > 64000) {
                    flags.setPoints(64000)
                }
                player.sendMessages(
                    "You've completed " + flags.taskStreak + " tasks in a row and received " + points +
                        " points, with a total of " +
                        flags.getPoints(),
                    "You have completed " + flags.completedTasks + " tasks in total. Return to a Slayer master.",
                )
            } else if (flags.completedTasks == 4) {
                sendMessage(player, "You've completed your task; you will start gaining points on your next task!")
                flags.flagCanEarnPoints()
            } else if (flags.getMaster() == SlayerMaster.TURAEL) {
                player.sendMessages(
                    "You've completed your task; Tasks from Turael do not award points.",
                    "Return to a Slayer master.",
                )
            } else {
                player.sendMessages(
                    "You've completed your task; Complete " + (4 - flags.completedTasks) +
                        " more task(s) to start gaining points.",
                    "Return to a Slayer master.",
                )
            }
        }
    }

    fun generate(master: SlayerMaster) {
        val task = SlayerUtils.generate(player!!, master) ?: return
        SlayerUtils.assign(player, task, master)
    }

    fun clear() {
        amount = 0
    }

    val taskName: String
        get() {
            val task = flags.getTask()
            if (task.npcs == null) {
                return "no npcs report me"
            }
            return if (task.npcs.isEmpty()) {
                "npc length too small report me"
            } else {
                NPCDefinition.forId(task.npcs[0]).name.lowercase()
            }
        }

    var task: Tasks?
        get() = flags.getTask()
        set(task) {
            flags.setTask(task!!)
        }

    var activeTask: Tasks? = null
        get() {
            if (hasTask()) return flags.getTask()
            return null
        }

    var master: SlayerMaster?
        get() = flags.getMaster()
        set(master) {
            flags.setMaster(master!!)
        }

    fun hasTask(): Boolean = amount > 0

    val isCompleted: Boolean
        get() = flags.getTaskAmount() <= 0

    var amount: Int
        get() = flags.getTaskAmount()
        set(amount) {
            flags.setTaskAmount(amount)
        }

    fun decrementAmount(amount: Int) {
        flags.decrementTaskAmount(amount)
        setVarp(player!!, 2502, flags.taskFlags shr 4)
    }

    fun hasStarted(): Boolean = flags.completedTasks > 0 || flags.getTaskAmount() > 0

    var slayerPoints: Int
        get() = flags.getPoints()
        set(slayerPoints) {
            flags.setPoints(slayerPoints)
        }

    val removed: List<Tasks>
        get() = flags.removed

    val isCanEarnPoints: Boolean
        get() = flags.canEarnPoints()

    companion object {
        @JvmStatic
        fun getInstance(player: Player): SlayerManager =
            getAttribute(
                player,
                "slayer-manager",
                SlayerManager(),
            )
    }
}
