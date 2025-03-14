package content.global.skill.slayer

import content.global.handlers.item.equipment.gloves.FOGGlovesListener
import core.api.*
import core.cache.def.impl.NPCDefinition
import core.game.event.EventHook
import core.game.event.NPCKillEvent
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs.consts.Items

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
        save: JSONObject,
    ) {
        val slayer = JSONObject()
        val slayerManager = getInstance(player)
        if (slayerManager.removed.isNotEmpty()) {
            val removedTasks = JSONArray()
            slayerManager.removed.map {
                removedTasks.add(it.ordinal.toString())
            }
            slayer["removedTasks"] = removedTasks
        }
        slayer["taskStreak"] = slayerManager.flags.taskStreak.toString()
        slayer["totalTasks"] = slayerManager.flags.completedTasks.toString()
        slayer["equipmentFlags"] = slayerManager.flags.equipmentFlags
        slayer["taskFlags"] = slayerManager.flags.taskFlags
        slayer["rewardFlags"] = slayerManager.flags.rewardFlags
        save["slayer"] = slayer
    }

    override fun parsePlayer(
        player: Player,
        data: JSONObject,
    ) {
        val slayerData = data["slayer"] as JSONObject
        val m = slayerData["master"]
        val flags = getInstance(player).flags
        if (m != null) {
            flags.setMaster(SlayerMaster.forId(m.toString().toInt()))
        }
        val t = slayerData["taskId"]
        if (t != null) flags.setTask(Tasks.values()[t.toString().toInt()])
        val a = slayerData["taskAmount"]
        if (a != null) flags.setTaskAmount(a.toString().toInt())
        val points = slayerData["points"]
        if (points != null) {
            flags.setPoints(points.toString().toInt())
        }
        val taskStreak = slayerData["taskStreak"]
        if (taskStreak != null) {
            flags.taskStreak = taskStreak.toString().toInt()
        }
        val la = slayerData["learned_rewards"]
        if (la != null) {
            val learnedArray = slayerData["learned_rewards"] as JSONArray?
            for (i in learnedArray!!.indices) {
                val unlocked = learnedArray[i] as Boolean
                when (i) {
                    0 -> if (unlocked) flags.unlockBroads()
                    1 -> if (unlocked) flags.unlockRing()
                    2 -> if (unlocked) flags.unlockHelm()
                    else -> {}
                }
            }
        }
        val removedTasks = slayerData["removedTasks"] as JSONArray?
        if (removedTasks != null) {
            for (i in removedTasks.indices) {
                flags.removed.add(Tasks.values()[removedTasks[i].toString().toInt()])
            }
        }
        val completedTasks: Any = slayerData["totalTasks"].toString()
        flags.completedTasks = completedTasks.toString().toInt()
        if (flags.completedTasks >= 4) flags.flagCanEarnPoints()

        if (slayerData.containsKey("equipmentFlags")) {
            flags.equipmentFlags =
                slayerData["equipmentFlags"].toString().toInt()
        }
        if (slayerData.containsKey("taskFlags")) flags.taskFlags = slayerData["taskFlags"].toString().toInt()
        if (slayerData.containsKey("rewardFlags")) flags.rewardFlags = slayerData["rewardFlags"].toString().toInt()
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

    fun hasTask(): Boolean {
        return amount > 0
    }

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

    fun hasStarted(): Boolean {
        return flags.completedTasks > 0 || flags.getTaskAmount() > 0
    }

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
        fun getInstance(player: Player): SlayerManager {
            return getAttribute(
                player,
                "slayer-manager",
                SlayerManager(),
            )
        }
    }
}
