package content.global.ame.drilldemon

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

object DrillDemonUtils {
    val DD_SIGN_VARP = 531
    val DD_SIGN_RUN = 0
    val DD_SIGN_SITUP = 1
    val DD_SIGN_PUSHUP = 2
    val DD_SIGN_JUMP = 3
    val DD_AREA = ZoneBorders(3158, 4817, 3168, 4823)
    val DD_NPC = NPCs.SERGEANT_DAMIEN_2790

    @JvmStatic
    fun teleport(player: Player) {
        setAttribute(player, RandomEvent.save(), player.location)
        teleport(player, Location.create(3163, 4819, 0), TeleportManager.TeleportType.NORMAL)
        player.interfaceManager.closeDefaultTabs()
        setComponentVisibility(player, Components.TOPLEVEL_548, 69, true)
        setComponentVisibility(player, Components.TOPLEVEL_FULLSCREEN_746, 12, true)
    }

    @JvmStatic
    fun changeSignsAndAssignTask(player: Player) {
        setVarp(player, DD_SIGN_VARP, 0)
        val tempList = arrayListOf(DD_SIGN_RUN, DD_SIGN_JUMP, DD_SIGN_PUSHUP, DD_SIGN_SITUP).shuffled().toMutableList()
        val tempOffsetList = arrayListOf(1335, 1336, 1337, 1338).shuffled().toMutableList()
        val task = tempList.random()
        val taskOffset = tempOffsetList.random()

        setAttribute(player, GameAttributes.DRILL_TASK, task)
        setAttribute(player, GameAttributes.DRILL_OFFSET, taskOffset)

        tempList.remove(task)
        tempOffsetList.remove(taskOffset)

        setVarbit(player, taskOffset, task)
        for (i in 0 until tempList.size) {
            setVarbit(player, tempOffsetList[i], tempList[i], true)
        }
    }

    @JvmStatic
    fun getVarbitForId(id: Int): Int {
        return when (id) {
            10076 -> 1335
            10077 -> 1336
            10078 -> 1337
            10079 -> 1338
            else -> 0
        }
    }

    @JvmStatic
    fun getMatTask(
        id: Int,
        player: Player,
    ): Int {
        return getVarbit(player, getVarbitForId(id))
    }

    @JvmStatic
    fun cleanup(player: Player) {
        player.locks.unlockTeleport()
        unlock(player)
        teleport(
            player,
            getAttribute(player, RandomEvent.save(), Location.create(3222, 3218, 0)),
            TeleportManager.TeleportType.NORMAL,
        )
        removeAttribute(player, RandomEvent.save())
        removeAttribute(player, GameAttributes.DRILL_TASK)
        removeAttribute(player, GameAttributes.DRILL_OFFSET)
        removeAttribute(player, GameAttributes.DRILL_COUNTER)
        player.interfaceManager.openDefaultTabs()
        setComponentVisibility(player, Components.TOPLEVEL_548, 69, false)
        setComponentVisibility(player, Components.TOPLEVEL_FULLSCREEN_746, 12, false)
    }

    @JvmStatic
    fun animationForTask(task: Int): Animation {
        return when (task) {
            DD_SIGN_SITUP -> Animation(Animations.SIT_UPS_FROM_DRILL_DEMON_EVENT_2763)
            DD_SIGN_PUSHUP -> Animation(Animations.PUSH_UPS_DRILL_DEMON_EVENT_2762)
            DD_SIGN_JUMP -> Animation(Animations.JUMPS_FROM_DRILL_DEMON_EVENT_2761)
            DD_SIGN_RUN -> Animation(Animations.RUNNING_IN_PLACE_FROM_DRILL_DEMON_EVENT_2764)
            else -> Animation(-1)
        }
    }

    @JvmStatic
    fun reward(player: Player) {
        queueScript(player, 2, QueueStrength.SOFT) {
            val hasHat = hasAnItem(player, Items.CAMO_HELMET_6656).container != null
            val hasShirt = hasAnItem(player, Items.CAMO_TOP_6654).container != null
            val hasPants = hasAnItem(player, Items.CAMO_BOTTOMS_6655).container != null
            when {
                !hasHat -> addItemOrDrop(player, Items.CAMO_HELMET_6656)
                !hasShirt -> addItemOrDrop(player, Items.CAMO_TOP_6654)
                !hasPants -> addItemOrDrop(player, Items.CAMO_BOTTOMS_6655)
                else -> addItemOrDrop(player, Items.COINS_995, 500)
            }
            return@queueScript stopExecuting(player)
        }
    }
}
