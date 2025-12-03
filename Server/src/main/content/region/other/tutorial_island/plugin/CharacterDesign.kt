package content.region.other.tutorial_island.plugin

import core.game.node.entity.player.Player
import core.game.node.entity.player.link.appearance.Gender
import core.game.node.item.Item
import core.tools.RandomFunction
import core.api.setVarp
import core.api.setVarbit

/**
 * Handles the Character Design interface and customization logic.
 *
 * @author Emperor, Vexia
 */
object CharacterDesign {
    private val MALE_HEAD_IDS = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 91, 92, 93, 94, 95, 96, 97, 261, 262, 263, 264, 265, 266, 267, 268)
    private val FEMALE_HEAD_IDS = intArrayOf(45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280)

    private val MALE_JAW_IDS = intArrayOf(10, 11, 12, 13, 14, 15, 16, 17, 98, 99, 100, 101, 102, 103, 104, 305, 306, 307, 308)
    private val FEMALE_JAW_IDS = intArrayOf(1000)

    private val MALE_TORSO_IDS = intArrayOf(18, 19, 20, 21, 22, 23, 24, 25, 111, 112, 113, 114, 115, 116)
    private val FEMALE_TORSO_IDS = intArrayOf(56, 57, 58, 59, 60, 153, 154, 155, 156, 157, 158)

    private val MALE_ARMS_IDS = intArrayOf(26, 27, 28, 29, 30, 31, 105, 106, 107, 108, 109, 110)
    private val FEMALE_ARMS_IDS = intArrayOf(61, 62, 63, 64, 65, 147, 148, 149, 150, 151, 152)

    private val MALE_HANDS_IDS = intArrayOf(33, 34, 84, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126)
    private val FEMALE_HANDS_IDS = intArrayOf(67, 68, 127, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168)

    private val MALE_LEGS_IDS = intArrayOf(36, 37, 38, 39, 40, 85, 86, 87, 88, 89, 90)
    private val FEMALE_LEGS_IDS = intArrayOf(70, 71, 72, 73, 74, 75, 76, 77, 128, 129, 130, 131, 132, 133, 134)

    private val MALE_FEET_IDS = intArrayOf(42, 43)
    private val FEMALE_FEET_IDS = intArrayOf(79, 80)

    private val MALE_LOOK_IDS = arrayOf(MALE_HEAD_IDS, MALE_JAW_IDS, MALE_TORSO_IDS, MALE_ARMS_IDS, MALE_HANDS_IDS, MALE_LEGS_IDS, MALE_FEET_IDS)
    private val FEMALE_LOOK_IDS = arrayOf(FEMALE_HEAD_IDS, FEMALE_JAW_IDS, FEMALE_TORSO_IDS, FEMALE_ARMS_IDS, FEMALE_HANDS_IDS, FEMALE_LEGS_IDS, FEMALE_FEET_IDS)

    private val HAIR_COLORS = intArrayOf(20, 19, 10, 18, 4, 5, 15, 7, 0, 6, 21, 9, 22, 17, 8, 16, 11, 24, 23, 3, 2, 1, 14, 13, 12)
    private val TORSO_COLORS = intArrayOf(24, 23, 2, 22, 12, 11, 6, 19, 4, 0, 9, 13, 25, 8, 15, 26, 21, 7, 20, 14, 10, 28, 27, 3, 5, 18, 17, 1, 16)
    private val LEG_COLORS = intArrayOf(26, 24, 23, 3, 22, 13, 12, 7, 19, 5, 1, 10, 14, 25, 9, 0, 21, 8, 20, 15, 11, 28, 27, 4, 6, 18, 17, 2, 16)
    private val FEET_COLORS = intArrayOf(0, 1, 2, 3, 4, 5)
    private val SKIN_COLORS = intArrayOf(7, 6, 5, 4, 3, 2, 1, 0)

    fun open(player: Player) {
        player.unlock()
        player.removeAttribute("char-design:accepted")
        player.packetDispatch.sendPlayerOnInterface(771, 79)
        player.packetDispatch.sendAnimationInterface(9806, 771, 79)
        player.appearance.changeGender(player.appearance.gender)
        val c = player.interfaceManager.openComponent(771)
        c?.setUncloseEvent { p, _ ->
            p.getAttribute("char-design:accepted", false)
        }
        reset(player)
        player.packetDispatch.sendInterfaceConfig(771, 22, false)
        player.packetDispatch.sendInterfaceConfig(771, 92, false)
        player.packetDispatch.sendInterfaceConfig(771, 97, false)
        setVarp(player, 1262, if (player.appearance.isMale) 1 else 0)
    }

    fun reopen(player: Player) {
        player.removeAttribute("char-design:accepted")
        player.packetDispatch.sendPlayerOnInterface(771, 79)
        player.packetDispatch.sendAnimationInterface(9806, 771, 79)
        val c = player.interfaceManager.openComponent(771)
        c?.setUncloseEvent { p, _ -> p.getAttribute("char-design:accepted", false) }
        player.packetDispatch.sendInterfaceConfig(771, 22, false)
        player.packetDispatch.sendInterfaceConfig(771, 92, false)
        player.packetDispatch.sendInterfaceConfig(771, 97, false)
        setVarp(player, 1262, if (player.appearance.isMale) 1 else 0)
    }

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
            321 -> {
                randomize(player, false)
                return true
            }

            169 -> {
                randomize(player, true)
                return true
            }

            362 -> {
                confirm(player, true)
                return true
            }
        }
        when (buttonId) {
            in 100..124 -> changeColor(player, 0, HAIR_COLORS, 100, buttonId)
            in 189..217 -> changeColor(player, 2, TORSO_COLORS, 189, buttonId)
            in 248..276 -> changeColor(player, 5, LEG_COLORS, 248, buttonId)
            in 307..312 -> changeColor(player, 6, FEET_COLORS, 307, buttonId)
            in 151..158 -> changeColor(player, 4, SKIN_COLORS, 158, buttonId)
        }
        return false
    }

    private fun changeGender(player: Player, male: Boolean) {
        player.setAttribute("male", male)
        setVarp(player, 1262, if (male) 1 else 0)
        if (male) {
            setVarbit(player, 5008, 1)
            setVarbit(player, 5009, 0)
        } else {
            setVarbit(player, 5008, 0)
            setVarbit(player, 5009, 1)
        }
        reset(player)
    }

    private fun changeLook(player: Player, index: Int, increment: Boolean) {
        if (index < 2 && !player.getAttribute("first-click:$index", false)) {
            player.setAttribute("first-click:$index", true)
            return
        }
        player.setAttribute(
            "look-val:$index", getValue(player, "look", index, player.getAttribute("look:$index", 0), increment)
        )
    }

    private fun changeColor(player: Player, index: Int, array: IntArray, startId: Int, buttonId: Int) {
        val col = array[kotlin.math.abs(buttonId - startId)]
        player.setAttribute("color-val:$index", col)
    }

    private fun reset(player: Player) {
        for (i in player.appearance.appearanceCache.indices) {
            player.removeAttribute("look:$i")
            player.removeAttribute("look-val:$i")
            player.removeAttribute("color-val:$i")
        }
        player.removeAttribute("first-click:0")
        player.removeAttribute("first-click:1")
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
            changeColor(player, 2, TORSO_COLORS, 189, RandomFunction.random(189, 217))
            changeColor(player, 5, LEG_COLORS, 248, RandomFunction.random(248, 276))
            changeColor(player, 6, FEET_COLORS, 307, RandomFunction.random(307, 312))
        }
        confirm(player, false)
    }

    private fun confirm(player: Player, close: Boolean) {
        if (close) {
            player.setAttribute("char-design:accepted", true)
            player.interfaceManager.close()
        }
        player.appearance.gender =
            if (player.getAttribute("male", player.appearance.isMale)) Gender.MALE else Gender.FEMALE
        for (i in player.appearance.appearanceCache.indices) {
            val look = player.getAttribute("look-val:$i", player.appearance.appearanceCache[i].look)
            val color = player.getAttribute("color-val:$i", player.appearance.appearanceCache[i].color)
            player.appearance.appearanceCache[i].changeLook(look)
            player.appearance.appearanceCache[i].changeColor(color)
        }
        player.appearance.sync()
    }

    private fun getValue(player: Player, key: String, index: Int, currentIndex: Int, increment: Boolean): Int {
        val array = if (player.getAttribute(
                "male", player.appearance.isMale
            )
        ) MALE_LOOK_IDS[index] else FEMALE_LOOK_IDS[index]
        var idx = currentIndex
        val valResult = when {
            increment && currentIndex + 1 > array.lastIndex -> {
                idx = 0
                array[0]
            }

            !increment && currentIndex - 1 < 0 -> {
                idx = array.lastIndex
                array[idx]
            }

            increment -> {
                idx++
                array[idx]
            }

            else -> {
                idx--
                array[idx]
            }
        }
        player.setAttribute("$key:$index", idx)
        return valResult
    }
}