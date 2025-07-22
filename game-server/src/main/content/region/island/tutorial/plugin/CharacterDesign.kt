package content.region.island.tutorial.plugin

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.appearance.Gender
import core.tools.RandomFunction
import org.rs.consts.Components
import kotlin.math.abs

/**
 * Handles the Character Design interface and customization logic.
 *
 * @author Emperor, Vexia
 */
object CharacterDesign {
    private val MALE_LOOK_IDS = arrayOf(
        // Male head component ids.
        intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 91, 92, 93, 94, 95, 96, 97, 261, 262, 263, 264, 265, 266, 267, 268),
        // Male jaw component ids.
        intArrayOf(10, 11, 12, 13, 14, 15, 16, 17, 98, 99, 100, 101, 102, 103, 104, 305, 306, 307, 308),
        // Male torso component ids.
        intArrayOf(18, 19, 20, 21, 22, 23, 24, 25, 111, 112, 113, 114, 115, 116),
        // Male arms component ids.
        intArrayOf(26, 27, 28, 29, 30, 31, 105, 106, 107, 108, 109, 110),
        // Male hand component ids.
        intArrayOf(33, 34, 84, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126),
        // Male legs component ids.
        intArrayOf(36, 37, 38, 39, 40, 85, 86, 87, 88, 89, 90),
        // Male feet component ids.
        intArrayOf(42, 43)
    )

    private val FEMALE_LOOK_IDS = arrayOf(
        // Female head component ids.
        intArrayOf(45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280),
        // Female jaw component ids.
        intArrayOf(1000),
        // Female torso component ids.
        intArrayOf(56, 57, 58, 59, 60, 153, 154, 155, 156, 157, 158),
        // Female arms component ids.
        intArrayOf(61, 62, 63, 64, 65, 147, 148, 149, 150, 151, 152),
        // Female hand component ids.
        intArrayOf(67, 68, 127, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168),
        // Female legs component ids.
        intArrayOf(70, 71, 72, 73, 74, 75, 76, 77, 128, 129, 130, 131, 132, 133, 134),
        // Female feet component ids.
        intArrayOf(79, 80)
    )

    // Global appearance components.
    private val HAIR_COLORS = intArrayOf(20, 19, 10, 18, 4, 5, 15, 7, 0, 6, 21, 9, 22, 17, 8, 16, 11, 24, 23, 3, 2, 1, 14, 13, 12)
    private val TORSO_COLORS = intArrayOf(24, 23, 2, 22, 12, 11, 6, 19, 4, 0, 9, 13, 25, 8, 15, 26, 21, 7, 20, 14, 10, 28, 27, 3, 5, 18, 17, 1, 16)
    private val LEG_COLORS = intArrayOf(26, 24, 23, 3, 22, 13, 12, 7, 19, 5, 1, 10, 14, 25, 9, 0, 21, 8, 20, 15, 11, 28, 27, 4, 6, 18, 17, 2, 16)
    private val FEET_COLORS = intArrayOf(0, 1, 2, 3, 4, 5)
    private val SKIN_COLORS = intArrayOf(7, 6, 5, 4, 3, 2, 1, 0)

    // Color map.
    private val COLOR_MAPPINGS = listOf(
        Triple(0, HAIR_COLORS, 100..124),
        Triple(2, TORSO_COLORS, 189..217),
        Triple(5, LEG_COLORS, 248..276),
        Triple(6, FEET_COLORS, 307..312),
        Triple(4, SKIN_COLORS, 151..158)
    )

    @JvmStatic
    fun open(player: Player) {
        player.unlock()
        removeAttribute(player, "char-design:accepted")
        sendPlayerOnInterface(player, Components.APPEARANCE_771, 79)
        sendAnimationOnInterface(player, 9806, Components.APPEARANCE_771, 79)
        player.appearance.changeGender(player.appearance.gender)
        player.interfaceManager.openComponent(Components.APPEARANCE_771)?.setUncloseEvent { p, _ ->
            p.getAttribute("char-design:accepted", false)
        }
        reset(player)
        listOf(22, 92, 97).forEach {
            sendInterfaceConfig(player, Components.APPEARANCE_771, it, false)
        }
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
        COLOR_MAPPINGS.find { (_, _, range) -> buttonId in range }?.let { (index, colors, range) ->
            changeColor(player, index, colors, range.first, buttonId)
        }
        return false
    }

    private fun changeGender(player: Player, male: Boolean) {
        player.setAttribute("male", male)
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
        val appearanceIds = if (getAttribute(player,"male", player.appearance.isMale)) MALE_LOOK_IDS else FEMALE_LOOK_IDS
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
            changeColor(player, 0, HAIR_COLORS, 100, RandomFunction.random(100, 124))
            changeColor(player, 4, SKIN_COLORS, 158, RandomFunction.random(151, 158))
        } else {
            for (i in player.appearance.appearanceCache.indices) {
                changeLook(player, i, RandomFunction.random(2) == 1)
            }
            listOf(1, 2, 3).forEach {
                val (index, colors, range) = COLOR_MAPPINGS[it]
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
