package content.region.misthalin.quest.tog

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestPoints
import core.api.quest.getQuestStage
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

/**
 * Tears of Guthix Quest
 *
 * Author: [Ovenbreado](https://gitlab.com/ovenbreado)
 */
@Initializable
class
TearsOfGuthix :
    Quest(Quests.TEARS_OF_GUTHIX, 120, 119, 1, 449, Vars.VARBIT_QUEST_TEARS_OF_GUTHIX_PROGRESS_451, 0, 1, 2) {
    companion object {
        fun daysLeft(player: Player): Int {
            val currentTime = System.currentTimeMillis()
            val previousTime = getAttribute<Long>(player, GameAttributes.QUEST_TOG_LAST_DATE, 0)

            val numberOfDaysLeft = (currentTime - previousTime) / 86400000L
            return 6 - numberOfDaysLeft.toInt()
        }

        fun xpLeft(player: Player): Int {
            val currentXP = player.skills.totalXp
            val previousXP = getAttribute(player, GameAttributes.QUEST_TOG_LAST_XP_AMOUNT, 0)
            return 100000 - (currentXP - previousXP)
        }

        fun questPointsLeft(player: Player): Int {
            val currentQuestPoints = getQuestPoints(player)
            val previousQuestPoints = getAttribute(player, GameAttributes.QUEST_TOG_LAST_QP, 0)
            return 1 - (currentQuestPoints - previousQuestPoints)
        }

        fun hasRequirements(player: Player): Boolean {
            return arrayOf(
                hasLevelStat(player, Skills.FIREMAKING, 49),
                hasLevelStat(player, Skills.CRAFTING, 20),
                hasLevelStat(player, Skills.MINING, 20),
            ).all { it }
        }
    }

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player, Quests.TEARS_OF_GUTHIX) > 0

        if (!started) {
            line(player, "I can start this quest by speaking to !!Juna the serpent?? who", line++, false)
            line(player, "lives deep in the !!Lumbridge Swamp Caves??.", line++, false)
            line(player, "I will need to have:", line++, false)
            line(player, "!!Level 49 firemaking??", line++, hasLevelStat(player, Skills.FIREMAKING, 49))
            line(player, "!!Level 20 crafting??", line++, hasLevelStat(player, Skills.CRAFTING, 20))
            line(player, "!!Level 20 mining??", line++, hasLevelStat(player, Skills.MINING, 20))
            line(player, "!!43 quest points??", line++, getQuestPoints(player) >= 43)
            line(
                player,
                "!!Level 49 crafting would be an advantage??",
                line++,
                hasLevelStat(player, Skills.CRAFTING, 49),
            )
            line(
                player,
                "!!Level 49 smithing would be an advantage??",
                line++,
                hasLevelStat(player, Skills.SMITHING, 49),
            )
        } else if (stage < 100) {
            line(player, "I met Juna the serpent in a deep chasm beneath the", line++, true)
            line(player, "Lumbridge Swamp Caves.", line++, true)
            line(player, "I told her a story and she said she would let me into the", line++, false)
            line(player, "Tears of Guthix cave if I brought her a !!bowl?? made from the", line++, false)
            line(player, "stone in !!the cave on the South side of the chasm??.", line++, false)
        } else {
            line(player, "I met Juna the serpent in a deep chasm beneath the", line++, true)
            line(player, "Lumbridge Swamp Caves. I made a bowl out of magical", line++, true)
            line(player, "stone in order to catch the Tears of Guthix.", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++)
            line++
            line(player, "Now Juna will let me into the cave to collect the Tears if I", line++, false)
            line(player, "!!tell her stories?? of my adventures.", line++, false)
            if (daysLeft(player) > 0 && xpLeft(player) > 0 && questPointsLeft(player) > 0) {
                line(
                    player,
                    "I will be able to collect the Tears of Guthix !!in ${daysLeft(player)} days??, as",
                    line++,
                    false,
                )
                line(
                    player,
                    "long as I gain either !!1 Quest Point?? or !!${xpLeft(player)} total XP??",
                    line++,
                    false,
                )
            } else if (xpLeft(player) > 0 && questPointsLeft(player) > 0) {
                line(player, "I will be able to collect the Tears of Guthix, as long as I", line++, false)
                line(player, "gain either !!1 Quest Point?? or !!${xpLeft(player)} total XP??.", line++, false)
            } else if (daysLeft(player) > 0) {
                line(
                    player,
                    "I will be able to collect the Tears of Guthix !!in ${daysLeft(player)} days??.",
                    line++,
                    false,
                )
            } else {
                line(player, "I have had enough adventures to tell Juna more stories,", line++, false)
                line(player, "and a week has passed since I last collected the Tears. I", line++, false)
                line(player, "can visit Juna again now.", line++, false)
            }
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendItemZoomOnInterface(
            Items.STONE_BOWL_4704,
            240,
            Components.QUEST_COMPLETE_SCROLL_277,
            5,
        )
        drawReward(player, "1 quest point", ln++)
        drawReward(player, "1000 Crafting XP", ln++)
        drawReward(player, "Access to the Tears of Guthix", ln++)
        drawReward(player, "cave", ln)
        rewardXP(player, Skills.CRAFTING, 1000.0)
        setVarbit(player, Vars.VARBIT_QUEST_TEARS_OF_GUTHIX_PROGRESS_451, 2, true)
    }

    override fun reset(player: Player) {
        removeAttribute(player, GameAttributes.QUEST_TOG_LAST_DATE)
        removeAttribute(player, GameAttributes.QUEST_TOG_LAST_XP_AMOUNT)
        removeAttribute(player, GameAttributes.QUEST_TOG_LAST_QP)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
