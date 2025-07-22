package content.global.plugin.iface

import content.data.GameAttributes
import content.region.island.tutorial.plugin.TutorialStage
import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.skill.LevelUp
import core.game.node.entity.skill.Skills
import org.rs.consts.Components

/**
 * Represents the plugin used to handle the skilling tab.
 * @author Vexia, Splinter
 */
class StatsTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.STATS_320) { player, _, _, buttonID, _, _ ->
            val config = skillMap[buttonID] ?: return@on true

            if (getAttribute(player, "levelup:${config.skillID}", false)) {
                removeAttributes(player, "levelup:${config.skillID}")
                LevelUp.sendFlashingIcons(player, -1)
                setVarp(player, 1230, ADVANCE_CONFIGS[config.skillID])
                openInterface(player, Components.STATS_TAB_741)
            } else {
                openInterface(player, Components.SKILL_GUIDE_V2_499)
                setVarp(player, 965, config.configID)
                setAttribute(player, "skillMenu", config.configID)
            }
            if (!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) {
                TutorialStage.rollback(player)
            }
            return@on true
        }

        on(Components.SKILL_GUIDE_V2_499) { player, _, _, buttonID, _, _ ->
            setVarbit(player, 3288, getAttribute(player, "skillMenu", -1))
            setVarbit(player, 3289, buttonID - 10)
            if (!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) {
                TutorialStage.rollback(player)
            }
            return@on true
        }

        on(Components.GAME_INTERFACE_740) { player, _, _, buttonID, _, _ ->
            if (buttonID == 3) {
                closeInterface(player)
            }
            return@on true
        }
    }

    companion object {
        val skillMap = HashMap<Int, SkillConfig>()
        val ADVANCE_CONFIGS =
            intArrayOf(9, 40, 17, 49, 25, 57, 33, 641, 659, 664, 121, 649, 89, 114, 107, 72, 64, 80, 673, 680, 99, 698, 689, 705)

        init {
            for (skill in SkillConfig.values()) {
                skillMap[skill.buttonID] = skill
            }
        }

        enum class SkillConfig(val buttonID: Int, val configID: Int, val skillID: Int, ) {
            ATTACK(125, 1, Skills.ATTACK),
            STRENGTH(126, 2, Skills.STRENGTH),
            DEFENCE(127, 5, Skills.DEFENCE),
            RANGE(128, 3, Skills.RANGE),
            PRAYER(129, 7, Skills.PRAYER),
            MAGIC(130, 4, Skills.MAGIC),
            RUNECRAFT(131, 12, Skills.RUNECRAFTING),
            HITPOINTS(133, 6, Skills.HITPOINTS),
            AGILITY(134, 8, Skills.AGILITY),
            HERBLORE(135, 9, Skills.HERBLORE),
            THIEVING(136, 10, Skills.THIEVING),
            CRAFTING(137, 11, Skills.CRAFTING),
            FLETCHING(138, 19, Skills.FLETCHING),
            SLAYER(139, 20, Skills.SLAYER),
            MINING(141, 13, Skills.MINING),
            SMITHING(142, 14, Skills.SMITHING),
            FISHING(143, 15, Skills.FISHING),
            COOKING(144, 16, Skills.COOKING),
            FIREMAKING(145, 17, Skills.FIREMAKING),
            WOODCUTTING(146, 18, Skills.WOODCUTTING),
            FARMING(147, 21, Skills.FARMING),
            CONSTRUCTION(132, 22, Skills.CONSTRUCTION),
            HUNTER(140, 23, Skills.HUNTER),
            SUMMONING(148, 24, Skills.SUMMONING),
        }
    }
}
