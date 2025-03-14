package content.region.kandarin.quest.zogre

import content.region.kandarin.quest.zogre.handlers.ZUtils
import core.api.*
import core.api.quest.isQuestComplete
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class ZogreFleshEaters :
    Quest(Quests.ZOGRE_FLESH_EATERS, 40, 39, 1, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 0, 1, 13) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11

        if (stage == 0) {
            line(player, "I can !!start?? this quest by talking to !!Grish?? at the Ogrish", line++)
            line(player, "ceremonial dance place called !!Jiggig??.", line++)
            line++
            line(player, "To start this !!quest?? I should complete these quests:-", line++)
            line(
                player,
                if (isQuestComplete(
                        player,
                        Quests.JUNGLE_POTION,
                    )
                ) {
                    "---${Quests.JUNGLE_POTION}./--"
                } else {
                    "!!${Quests.JUNGLE_POTION}.??"
                },
                line++,
            )
            line(
                player,
                if (isQuestComplete(
                        player,
                        Quests.BIG_CHOMPY_BIRD_HUNTING,
                    )
                ) {
                    "---${Quests.BIG_CHOMPY_BIRD_HUNTING}./--"
                } else {
                    "!!${Quests.BIG_CHOMPY_BIRD_HUNTING}.??"
                },
                line++,
            )
            line(player, "It would help if I had the following skill levels:-", line++, false)
            line(
                player,
                if (getStatLevel(player, Skills.RANGE) >= 30) "---Ranged level : 30/--" else "!!Ranged level : 30??",
                line++,
            )
            line(
                player,
                if (getStatLevel(
                        player,
                        Skills.FLETCHING,
                    ) >= 30
                ) {
                    "---Fletching level : 30/--"
                } else {
                    "!!Fletching level : 30??"
                },
                line++,
            )
            line(
                player,
                if (getStatLevel(
                        player,
                        Skills.SMITHING,
                    ) >= 4
                ) {
                    "---Smithing level : 4/--"
                } else {
                    "!!Smithing level : 4??"
                },
                line++,
            )
            line(
                player,
                if (getStatLevel(
                        player,
                        Skills.HERBLORE,
                    ) >= 8
                ) {
                    "---Herblore level : 8/--"
                } else {
                    "!!Herblore level : 8??"
                },
                line++,
            )
            line(player, "Must be able to defeat a !!level 111?? foe.", line++)
            line++
        }

        if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 1) {
            line(
                player,
                "I talked to !!Grish?? in the !!Jiggig?? area which is swarming with",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZOGRE_COFFIN_TRANSFORM_488) >= 3,
            )
            line(
                player,
                "Zombie Ogres (Zogres) These disgusting creatures carry",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZOGRE_COFFIN_TRANSFORM_488) >= 3,
            )
            line(
                player,
                "disease and are quite dangerous so the Ogres weren't",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZOGRE_COFFIN_TRANSFORM_488) >= 3,
            )
            line(
                player,
                "too keen to try and sort them out",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZOGRE_COFFIN_TRANSFORM_488) >= 3,
            )
            line++
        }

        if (getVarbit(player, Vars.VARBIT_QUEST_ZOGRE_COFFIN_TRANSFORM_488) == 3) {
            line(
                player,
                "I talked to an ogre called !!Grish?? who asked me to",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 6,
            )
            line(
                player,
                "look into the problem. After !!some searching around in a??",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 6,
            )
            line(
                player,
                "!!tomb??, I found some clues which pointed me to the !!human??",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 6,
            )
            line(
                player,
                "!!habitation of Yannile??.",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 6,
            )
            line++
        }

        if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 6) {
            line(
                player,
                "With the help of Zavistic Rarve, the grand secretary of the",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 8,
            )
            line(
                player,
                "Wizards guild I was able to piece the clues together and",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 8,
            )
            line(
                player,
                "discover that a Wizard named 'Sithik Ints' was responsible",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 8,
            )
            line++
        }

        if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 8) {
            line(
                player,
                "Unfortunately I couldn't remove the curse from the area, ",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 12,
            )
            line(
                player,
                "however, I was able to return some important artefacts to",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 12,
            )
            line(
                player,
                "Grish, who can now set up a new ceremonial dance area for",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 12,
            )
            line(
                player,
                "the ogres of Gu' Tanoth.",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 12,
            )
            line++
        }

        if (getAttribute(player, ZUtils.TALK_WITH_SITHIK_OGRE_DONE, false) ||
            getVarbit(
                player,
                Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487,
            ) >= 12
        ) {
            line(
                player,
                "Sithik Ints also told me how to make Brutal arrows which are",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 13,
            )
            line(
                player,
                "more effective against Zogres, and he also told me how to",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 13,
            )
            line(
                player,
                "make a disease balm",
                line++,
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 13,
            )
            line++
        }

        if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 13 || stage == 100) {
            line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10

        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.OGRE_ARTEFACT_4818, 240)

        drawReward(player, "1 Quest Point.", ln++)
        drawReward(player, "Can now make Brutal Arrows", ln++)
        drawReward(player, "and cure disease potions.", ln++)
        drawReward(player, "2000 Ranged, Fletching and", ln++)
        drawReward(player, "Herblore XP.", ln)
        rewardXP(player, Skills.FLETCHING, 2000.0)
        rewardXP(player, Skills.RANGE, 2000.0)
        rewardXP(player, Skills.HERBLORE, 2000.0)
        addItemOrDrop(player, Items.OURG_BONES_4835, 3)
        addItemOrDrop(player, Items.ZOGRE_BONES_4813, 2)
        setVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 13, true)
        removeAttributes(
            player,
            ZUtils.TORN_PAGE_ON_NECRO_BOOK,
            ZUtils.TALK_WITH_SITHIK_OGRE_DONE,
            ZUtils.TALK_WITH_ZAVISTIC_DONE,
            ZUtils.RECEIVED_KEY_FROM_GRISH,
            ZUtils.ASK_SITHIK_AGAIN,
        )
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
