package core.game.node.entity.skill

import content.data.GameAttributes
import core.api.*
import core.game.global.Skillcape
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.game.world.update.flag.context.Graphics
import core.tools.DARK_RED
import org.rs.consts.Components
import org.rs.consts.Vars

object LevelUp {
    private val SKILL_ICON =
        intArrayOf(
            67108864,
            335544320,
            134217728,
            402653184,
            201326592,
            469762048,
            268435456,
            1073741824,
            1207959552,
            1275068416,
            1006632960,
            1140850688,
            738197504,
            939524096,
            872415232,
            603979776,
            536870912,
            671088640,
            1342177280,
            1409286144,
            805306368,
            1543503872,
            1476395008,
            1610612736,
            1677721600,
        )
    private val FLASH_ICONS =
        intArrayOf(
            1,
            4,
            2,
            64,
            8,
            16,
            32,
            32768,
            131072,
            2048,
            16384,
            65536,
            1024,
            8192,
            4096,
            256,
            128,
            512,
            524288,
            1048576,
            262144,
            4194304,
            2097152,
            8388608,
            16777216,
        )
    private val ADVANCE_CONFIGS =
        intArrayOf(
            9,
            40,
            17,
            49,
            25,
            57,
            33,
            641,
            659,
            664,
            121,
            649,
            89,
            114,
            107,
            72,
            64,
            80,
            673,
            680,
            99,
            698,
            689,
            705,
        )
    private val CLIENT_ID =
        intArrayOf(1, 5, 2, 6, 3, 7, 4, 16, 18, 19, 15, 17, 11, 14, 13, 9, 8, 10, 20, 21, 12, 23, 22, 24, 24)
    private val SKILL_MILESTONES = intArrayOf(1, 50, 75, 100)
    private val COMBAT_MILESTONES = intArrayOf(3, 5, 10, 15, 25, 75, 90, 100, 110, 120, 126, 130, 138)
    private val TOTAL_LEVEL_MILESTONES =
        intArrayOf(
            50,
            75,
            100,
            150,
            200,
            300,
            400,
            500,
            600,
            700,
            800,
            900,
            1000,
            1100,
            1200,
            1300,
            1400,
            1500,
            1600,
            1700,
            1800,
            1900,
            2000,
            2100,
            2200,
            2300,
            2300,
        )
    private val GRAPHIC = 1630..1637

    @JvmStatic
    fun levelUp(
        player: Player,
        slot: Int,
        amount: Int,
    ) {
        if (!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) return

        // Play level-up fireworks and jingle.
        Graphics.send(Graphics(org.rs.consts.Graphics.FIREWORKS_WHEN_A_LVL_IS_GAINED_199, 100), player.location)
        playJingle(player, getSkillJingle(player, slot))

        // Handle skill and combat milestones.
        handleMilestones(player, slot, amount)

        // Display level-up messages.
        val skillName = Skills.SKILL_NAME[slot]
        sendString(
            player,
            "<col=00008B>Congratulations, you've just advanced a $skillName level!",
            Components.GAME_INTERFACE_740,
            0,
        )
        sendString(
            player,
            "Your $skillName level is now ${player.getSkills().getStaticLevel(slot)}.",
            Components.GAME_INTERFACE_740,
            1,
        )
        sendMessage(
            player,
            "You've just advanced a $skillName level! You have reached level ${player.getSkills().getStaticLevel(
                slot,
            )}.",
        )

        // Handle prayer skill leveling.
        if (slot == Skills.PRAYER) {
            player.getSkills().incrementPrayerPoints(1.0)
        }

        // Handle level 99 skill rewards and milestones.
        if (getStatLevel(player, slot) == 99 && !player.isArtificial) {
            Skillcape.trim(player)
            unlockEmote(player, 39)
            Graphics.send(Graphics(GRAPHIC.random(), 100, 50), player.location)
        }

        // Handle total level milestones.
        if (TOTAL_LEVEL_MILESTONES.contains(player.skills.getTotalLevel())) {
            Graphics.send(Graphics(GRAPHIC.random(), 100, 50), player.location)
            sendMessage(
                player,
                DARK_RED + "Well done! You've reached the total level ${player.skills.getTotalLevel()} milestone!",
            )
        }

        // Maximum total level reached.
        if (player.skills.getTotalLevel() == 2376) {
            Graphics.send(Graphics(GRAPHIC.random(), 100, 50), player.location)
            sendMessage(player, DARK_RED + "Congratulations! You've reached the maximum Total level possible!")
        }

        // Mark the skill as leveled up.
        setAttribute(player, "/save:levelup:$slot", true)
        sendFlashingIcons(player, slot)
    }

    private fun handleMilestones(
        player: Player,
        slot: Int,
        amount: Int,
    ) {
        var value = ADVANCE_CONFIGS[slot]

        // Check and update combat milestones.
        for (i in COMBAT_MILESTONES.indices) {
            if (player.properties.getCurrentCombatLevel() < COMBAT_MILESTONES[i]) {
                if (i > player.getSkills().combatMilestone) {
                    value = value or 0x2
                    player.getSkills().combatMilestone = i
                }
                break
            }

            // Special messages for combat milestones.
            if (i == 126 && !GameWorld.settings!!.isMembers) {
                Graphics.send(Graphics(GRAPHIC.random(), 100, 50), player.location)
                sendMessage(
                    player,
                    "Congratulations! Your Combat level is now 126 - the highest possible Combat level on free worlds!",
                )
                break
            }

            if (i == 138) {
                Graphics.send(Graphics(GRAPHIC.random(), 100, 50), player.location)
                sendMessage(
                    player,
                    "Congratulations! Your Combat level is now 138! You've achieved the highest Combat level possible!",
                )
                break
            }
        }

        // Check and update skill milestones based on total level.
        val totalLevel = player.getSkills().getTotalLevel()
        for (i in SKILL_MILESTONES.indices) {
            if (totalLevel < SKILL_MILESTONES[i]) {
                if (i > player.getSkills().skillMilestone) {
                    value = value or 0x4
                    player.getSkills().skillMilestone = i
                }
                break
            }
        }

        value = value or (player.getSkills().combatMilestone shl 23)
        value = value or (player.getSkills().skillMilestone shl 27)
        setVarp(player, Vars.VARP_STATS_TAB_1230, value)
    }

    @JvmStatic
    fun sendFlashingIcons(
        player: Player,
        slot: Int,
    ) {
        var value = 0

        // Check and add flashing icons for leveled-up skills.
        for (i in Skills.SKILL_NAME.indices) {
            if (player.getAttribute("levelup:$i", false)) {
                value = value or FLASH_ICONS[i]
            }
        }

        // Add the skill icon.
        if (slot > -1) {
            value = value or SKILL_ICON[slot]
            openChatbox(player, Components.GAME_INTERFACE_740)
        }

        // Update the flashing icons display.
        setVarp(player, Vars.VARP_IFACE_SKILL_FLASH_ICONS_1179, value)
    }
}
