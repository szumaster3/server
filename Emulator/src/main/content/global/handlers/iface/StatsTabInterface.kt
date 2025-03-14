package content.global.handlers.iface

import content.data.GameAttributes
import content.region.misc.handlers.tutorial.TutorialStage
import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.skill.LevelUp
import core.game.node.entity.skill.Skills
import org.rs.consts.Components

class StatsTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.STATS_320) { player, _, _, buttonID, _, _ ->
            val config = skillMap[buttonID] ?: return@on true

            if (getAttribute(player, "levelup:${config.skillID}", false)) {
                removeAttributes(player, "levelup:${config.skillID}")
                LevelUp.sendFlashingIcons(player, -1)
                setVarp(player, 1230, ADVANCE_CONFIGS[config.skillID])
                openInterface(player, 741)
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

        init {
            for (skill in SkillConfig.values()) {
                skillMap[skill.buttonID] = skill
            }
        }

        enum class SkillConfig(
            val buttonID: Int,
            val configID: Int,
            val skillID: Int,
        ) {
            ATTACK(buttonID = 125, configID = 1, skillID = Skills.ATTACK),
            STRENGTH(buttonID = 126, configID = 2, skillID = Skills.STRENGTH),
            DEFENCE(buttonID = 127, configID = 5, skillID = Skills.DEFENCE),
            RANGE(buttonID = 128, configID = 3, skillID = Skills.RANGE),
            PRAYER(buttonID = 129, configID = 7, skillID = Skills.PRAYER),
            MAGIC(buttonID = 130, configID = 4, skillID = Skills.MAGIC),
            RUNECRAFT(buttonID = 131, configID = 12, skillID = Skills.RUNECRAFTING),
            HITPOINTS(buttonID = 133, configID = 6, skillID = Skills.HITPOINTS),
            AGILITY(buttonID = 134, configID = 8, skillID = Skills.AGILITY),
            HERBLORE(buttonID = 135, configID = 9, skillID = Skills.HERBLORE),
            THIEVING(buttonID = 136, configID = 10, skillID = Skills.THIEVING),
            CRAFTING(buttonID = 137, configID = 11, skillID = Skills.CRAFTING),
            FLETCHING(buttonID = 138, configID = 19, skillID = Skills.FLETCHING),
            SLAYER(buttonID = 139, configID = 20, skillID = Skills.SLAYER),
            MINING(buttonID = 141, configID = 13, skillID = Skills.MINING),
            SMITHING(buttonID = 142, configID = 14, skillID = Skills.SMITHING),
            FISHING(buttonID = 143, configID = 15, skillID = Skills.FISHING),
            COOKING(buttonID = 144, configID = 16, skillID = Skills.COOKING),
            FIREMAKING(buttonID = 145, configID = 17, skillID = Skills.FIREMAKING),
            WOODCUTTING(buttonID = 146, configID = 18, skillID = Skills.WOODCUTTING),
            FARMING(buttonID = 147, configID = 21, skillID = Skills.FARMING),
            CONSTRUCTION(buttonID = 132, configID = 22, skillID = Skills.CONSTRUCTION),
            HUNTER(buttonID = 140, configID = 23, skillID = Skills.HUNTER),
            SUMMONING(buttonID = 148, configID = 24, skillID = Skills.SUMMONING),
        }
    }
}
