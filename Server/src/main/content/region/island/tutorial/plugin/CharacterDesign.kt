package content.region.island.tutorial.plugin

import content.global.plugins.interfaces.player_kit.PlayerKit
import core.api.*
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.appearance.Gender
import core.tools.RandomFunction
import kotlin.math.abs

/**
 * Handles the Character Design interface and customization logic.
 *
 * @author Emperor, Vexia
 */
object CharacterDesign {

    @JvmStatic
    fun open(player: Player) {
        player.unlock()
        removeAttribute(player, "char-design:accepted")
        sendPlayerOnInterface(player, PlayerKit.START_APPEARANCE_INTERFACE_ID, 79)
        sendAnimationOnInterface(player, 9806, PlayerKit.START_APPEARANCE_INTERFACE_ID, 79)
        player.appearance.changeGender(player.appearance.gender)
        Component(PlayerKit.START_APPEARANCE_INTERFACE_ID).setUncloseEvent { p, _ ->
            getAttribute(p,"char-design:accepted", false)
        }
        reset(player)
        sendInterfaceConfig(player, PlayerKit.START_APPEARANCE_INTERFACE_ID, 22, false)
        sendInterfaceConfig(player, PlayerKit.START_APPEARANCE_INTERFACE_ID, 92, false)
        sendInterfaceConfig(player, PlayerKit.START_APPEARANCE_INTERFACE_ID, 97, false)
        setVarp(player, 1262, if (player.appearance.isMale) 1 else 0)
    }

    @JvmStatic
    fun handleButtons(player: Player, buttonId: Int): Boolean {
        when (buttonId) {
            37, 40 -> player.settings.toggleMouseButton()
            92, 93 -> changeLook(player, 0, buttonId == 93)
            97, 98 -> changeLook(player, 1, buttonId == 98)
            341, 342 -> changeLook(player, 2, buttonId == 342)
            345, 346 -> changeLook(player, 3, buttonId == 346)
            349, 350 -> changeLook(player, 4, buttonId == 350)
            353, 354 -> changeLook(player, 5, buttonId == 354)
            357, 358 -> changeLook(player, 6, buttonId == 358)
            49, 52 -> changeGender(player, buttonId == 49)
            321 -> { randomize(player, false); return true }
            169 -> { randomize(player, true); return true }
            362 -> { confirm(player, true); return true }
        }
        PlayerKit.COLOR_MAPPINGS.find { (_, _, range) -> buttonId in range }?.let { (index, colors, range) ->
            val startId = if (index == 4) range.last else range.first
            changeColor(player, index, colors, startId, buttonId)
        }
        return false
    }

    private fun changeGender(player: Player, male: Boolean) {
        setVarp(player, 1262, if (male) 1 else 0)
        setVarbit(player, 5008, if (male) 1 else 0)
        setVarbit(player, 5009, if (male) 0 else 1)
        reset(player)
    }

    private fun changeLook(player: Player, index: Int, increment: Boolean) {
        if (index < 2 && !player.getAttribute("first-click:$index", false)) {
            setAttribute(player, "first-click:$index", true)
            return
        }
        val currentIndex = getAttribute(player, "look:$index", 0)
        val appearanceIds = if (getVarp(player, 1262) == 1) PlayerKit.MALE_LOOK_IDS else PlayerKit.FEMALE_LOOK_IDS
        val values = appearanceIds[index]
        val nextIndex = when {
            increment && currentIndex + 1 >= values.size -> 0
            !increment && currentIndex - 1 < 0 -> values.size - 1
            increment -> currentIndex + 1
            else -> currentIndex - 1
        }
        setAttribute(player, "look:$index", nextIndex)
        setAttribute(player, "look-val:$index", values[nextIndex])
    }

    private fun changeColor(player: Player, index: Int, array: IntArray, startId: Int, buttonId: Int) {
        val color = array[abs(buttonId - startId)]
        setAttribute(player, "color-val:$index", color)
    }

    private fun reset(player: Player) {
        for (i in player.appearance.appearanceCache.indices) {
            removeAttribute(player, "look:$i")
            removeAttribute(player, "look-val:$i")
            removeAttribute(player, "color-val:$i")
        }
        removeAttribute(player, "first-click:0")
        removeAttribute(player, "first-click:1")
    }

    @JvmStatic
    fun randomize(player: Player, head: Boolean) {
        if (head) {
            changeLook(player, 0, RandomFunction.random(2) == 1)
            changeLook(player, 1, RandomFunction.random(2) == 1)
            changeColor(player, 0, PlayerKit.HAIR_COLORS, 100, RandomFunction.random(100, 124))
            changeColor(player, 4, PlayerKit.SKIN_COLORS, 158, RandomFunction.random(151, 158))
        } else {
            for (i in player.appearance.appearanceCache.indices) {
                changeLook(player, i, RandomFunction.random(2) == 1)
            }
            listOf(1, 2, 3).forEach {
                val (index, colors, range) = PlayerKit.COLOR_MAPPINGS[it]
                changeColor(player, index, colors, range.first, RandomFunction.random(range.first, range.last))
            }
        }
        confirm(player, false)
    }

    private fun confirm(player: Player, close: Boolean) {
        if (close) {
            setAttribute(player, "char-design:accepted", true)
            closeInterface(player)
        }
        player.appearance.gender = if (getAttribute(player, "male", player.appearance.isMale)) Gender.MALE else Gender.FEMALE
        for (i in player.appearance.appearanceCache.indices) {
            val cache = player.appearance.appearanceCache[i]
            cache.changeLook(getAttribute(player,"look-val:$i", cache.look))
            cache.changeColor(getAttribute(player,"color-val:$i", cache.color))
        }
        refreshAppearance(player)
    }
}
