package content.region.other.entrana.quest.zep

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Vars

@Initializable
class EnlightenedJourney : Quest(Quests.ENLIGHTENED_JOURNEY, 55, 54, 1, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_PROGRESS_2866, 0, 1, 200) {

    class SkillRequirement(val skill: Int?, val level: Int?)
    val requirements = arrayListOf<SkillRequirement>()

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Auguste?? on", line++, true)
            line(player, "!!Entrana??.", line++, true)
            line(player, "Minimum Requirements:", line++, true)

            val requirements = listOf(
                Triple(Skills.CRAFTING, 36, "Crafting"),
                Triple(Skills.FARMING, 30, "Farming"),
                Triple(Skills.FIREMAKING, 20, "Firemaking")
            )

            for ((skill, level, displayName) in requirements) {
                val text = if (getStatLevel(player, skill) >= level) {
                    "---Level $level $displayName/--"
                } else {
                    "!!Level $level $displayName??"
                }
                line(player, text, line++, true)
            }

            val questPointsText = if (getQuestPoints(player) >= 21) "---21 Quest Points/--" else "!!21 Quest Points??"
            line(player, questPointsText, line++, true)
            line++
        }

        if (stage == 1) {
            line(player, "I have agreed to help Auguste build an !!air balloon??.", line++, true)
            line(player, "I have no idea what he's talking about.", line++, true)
            line(player, "Auguste thinks if he pumps hot air into a sack it will rise and", line++, true)
            line(player, "take us along with it.", line++, true)
            line(player, "But we're going to run some tests first. Thank goodness.", line++, true)
            line++
        }

        if (stage == 2 &&
            amountInInventory(player, Items.PAPYRUS_970) >= 3 &&
            inInventory(player, Items.BALL_OF_WOOL_1759) &&
            inInventory(player, Items.POTATOES10_5438) &&
            inInventory(player, Items.CANDLE_36)
        ) {
            line(player, "I gathered all the materials Auguste required:", line++, true)
            line(player, "three sheets of papyrus, a ball of wool,", line++, true)
            line(player, "a full sack of potatoes and one unlit candle.", line++, true)
        }
        else
        {
            line(player, "I need to gather the materials Auguste required:", line++)
            line(player, "three sheets of papyrus", line++, amountInInventory(player, Items.PAPYRUS_970) >= 3)
            line(player, "a ball of wool", line++, inInventory(player, Items.BALL_OF_WOOL_1759))
            line(player, "a full sack of potatoes", line++, inInventory(player, Items.POTATOES10_5438))
            line(player, "one unlit candle.", line++, inInventory(player, Items.CANDLE_36))
            line++
        }

        if (stage == 3) {
            line(player, "I need to gather the materials Auguste required:", line++, true)
            line(player, "three sheets of papyrus", line++, true)
            line(player, "a ball of wool", line++, true)
            line(player, "a full sack of potatoes", line++, true)
            line(player, "one unlit candle.", line++, true)
            line++

            line(player, if(!inInventory(player, Items.ORIGAMI_BALLOON_9934)) "I need to make an !!origami balloon??." else "I made an !!origami balloon??.", line++)
            line(player, "Auguste said I could make these any time I want if", line++)
            line(player, "I have the materials.", line++)
            line++
        }

        if (stage == 4) {
            line(player, "I need to gather the materials Auguste required:", line++, true)
            line(player, "three sheets of papyrus", line++, true)
            line(player, "a ball of wool", line++, true)
            line(player, "a full sack of potatoes", line++, true)
            line(player, "one unlit candle.", line++, true)
            line++

            line(player, if(!inInventory(player, Items.ORIGAMI_BALLOON_9934)) "I need to make an !!origami balloon??." else "I made an !!origami balloon??.", line++, true)
            line(player, "Auguste said I could make these any time I want if", line++, true)
            line(player, "I have the materials.", line++, true)
            line++
            line(player, "Auguste conducted the first experiment.", line++)
            line(player, "There was an awful lot of fire.", line++)
            line++
        }
        if (stage >= 5) {
            line(player, "I need to gather the materials Auguste required:", line++, true)
            line(player, "three sheets of papyrus", line++, true)
            line(player, "a ball of wool", line++, true)
            line(player, "a full sack of potatoes", line++, true)
            line(player, "one unlit candle.", line++, true)
            line++

            line(player, if(!inInventory(player, Items.ORIGAMI_BALLOON_9934)) "I need to make an !!origami balloon??." else "I made an !!origami balloon??.", line++)
            line(player, "Auguste said I could make these any time I want if", line++, true)
            line(player, "I have the materials.", line++, true)
            line++
            line(player, "Auguste conducted the first experiment.", line++, true)
            line(player, "There was an awful lot of fire.", line++, true)
            line++
            line(player, "Auguste conducted the second experiment.", line++, stage >= 6)
            line(player, "A flash mob appeared. They seem to have a grudge against science.", line++, stage >= 6)
            line++
        }
        if (stage >= 6) {
            line(player, "I gave Auguste all the supplies and made the basket for the balloon.", line++, stage >= 7)
            line++
        }
        if (stage >= 7) {
            line(player, "The balloon is all made and looks impressive!", line++, stage >= 8)
            line(player, "Let's hope it doesn't end the way the experiments did.", line++, stage >= 8)
            line++
        }
        if (stage >= 8) {
            line(player, "Whew! We survived our first balloon flight.", line++, stage >= 9)
            line++
        }
        if (stage >= 9) {
            line(player, "We successfully flew the first balloon to Taverley!", line++, stage >= 10)
            line++
        }
        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++, false)
            line++
            line(player, "I can now make !!Origami balloons??.", line++, false)
            line(player, "I can also use the !!balloon transport system??.", line++, false)
            line(player, "To go to new locations I should speak to !!Auguste?? on !!Entrana??.", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        displayQuestItem(player, Items.BOMBER_CAP_9945)
        drawReward(player, "1 Quest Point, 2K Crafting, 3k", ln++)
        drawReward(player, "Farming, 1,5k Woodcutting, 4k", ln++)
        drawReward(player, "Firemaking,", ln++)
        drawReward(player, "Balloon Transport System,", ln++)
        drawReward(player, "Origami Balloons", ln)

        rewardXP(player, Skills.CRAFTING, 2000.0)
        rewardXP(player, Skills.FARMING, 3000.0)
        rewardXP(player, Skills.WOODCUTTING, 1500.0)
        rewardXP(player, Skills.FIREMAKING, 4000.0)

        addItemOrDrop(player, Items.BOMBER_JACKET_9944)
        addItemOrDrop(player, Items.BOMBER_CAP_9945)

        setVarbit(player, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_PROGRESS_2866, 200, true)
        setVarbit(player, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_ENTRANA_BALLOON_2867, 2, true)
        setVarbit(player, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_TAVERLEY_BALLOON_2868, 1, true)
        setVarbit(player, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_CASTLE_WARS_BALLOON_2869, 1, true)
        setVarbit(player, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_GRAND_TREE_BALLOON_2870, 1, true)
        setVarbit(player, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_CRAFTING_GUILD_BALLOON_2871, 1, true)
        setVarbit(player, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_VARROCK_BALLOON_2872, 1, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        requirements.add(SkillRequirement(Skills.CRAFTING, 36))
        requirements.add(SkillRequirement(Skills.FARMING, 30))
        requirements.add(SkillRequirement(Skills.FIREMAKING, 20))
        return this
    }
}
