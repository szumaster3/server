package content.region.asgarnia.quest.zep

import core.api.*
import core.api.quest.getQuestPoints
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class EnlightenedJourney :
    Quest(Quests.ENLIGHTENED_JOURNEY, 55, 54, 1, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_PROGRESS_2866, 0, 1, 200) {
    class SkillRequirement(
        val skill: Int?,
        val level: Int?,
    )

    val requirements = arrayListOf<SkillRequirement>()

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        player ?: return
        if (stage >= 0) {
            line(player, "I can start this quest by speaking to !!Auguste?? on", line++, stage >= 1)
            line(player, "!!Entrana??.", line++, stage >= 1)
            line(player, "Minimum Requirements:", line++, stage >= 1)
            line(
                player,
                if (getStatLevel(player, Skills.CRAFTING) >=
                    36
                ) {
                    "---Level 36 Crafting/--"
                } else {
                    "!!Level 36 Crafting??"
                },
                line++,
            )
            line(
                player,
                if (getStatLevel(player, Skills.FARMING) >=
                    30
                ) {
                    "---Level 30 Farming/--"
                } else {
                    "!!Level 30 Farming??"
                },
                line++,
            )
            line(
                player,
                if (getStatLevel(player, Skills.FIREMAKING) >=
                    20
                ) {
                    "---Level 20 Firemaking/--"
                } else {
                    "!!Level 20 Firemaking??"
                },
                line++,
            )
            line(player, if (getQuestPoints(player) >= 21) "---21 Quest Points/--" else "!!21 Quest Points??", line++)
            line++
        }

        if (stage >= 1) {
            line(player, "I have agreed to help Auguste build an !!air balloon??.", line++, stage >= 2)
            line(player, "I have no idea what he's talking about.", line++, stage >= 2)
            line(player, "Auguste thinks if he pumps hot air into a sack it will rise and", line++, stage >= 2)
            line(player, "take us along with it.", line++, stage >= 2)
            line(player, "But we're going to run some tests first. Thank goodness.", line++, stage >= 2)
            line++
        }

        if (stage >= 2 &&
            inInventory(player, Items.PAPYRUS_970) &&
            inInventory(player, Items.BALL_OF_WOOL_1759) &&
            inInventory(player, Items.POTATOES10_5438) &&
            inInventory(player, Items.CANDLE_36)
        ) {
            line(player, "I gathered all the materials Auguste required:", line++, stage >= 3)
            line(player, "three sheets of papyrus, a ball of wool,", line++, stage >= 3)
            line(player, "a full sack of potatoes and one unlit candle.", line++, stage >= 3)
            line++
        }

        if (stage >= 3 && inInventory(player, Items.ORIGAMI_BALLOON_9934)) {
            line(player, "I made an !!origami balloon??.", line++, stage >= 4)
            line(player, "Auguste said I could make these any time I want if", line++, stage >= 4)
            line(player, "I have the materials.", line++, false)
            line++
        }

        if (stage >= 4) {
            line(player, "Auguste conducted the first experiment.", line++, stage >= 5)
            line(player, "There was an awful lot of fire.", line++, stage >= 5)
            line++
        }
        if (stage >= 5) {
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
        player ?: return
        var ln = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.BOMBER_CAP_9945)

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
