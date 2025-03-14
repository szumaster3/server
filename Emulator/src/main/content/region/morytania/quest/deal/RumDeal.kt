package content.region.morytania.quest.deal

import core.api.*
import core.api.quest.isQuestComplete
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

class RumDeal : Quest("Rum Deal", 107, 106, 2, Vars.VARP_QUEST_RUM_DEAL_PROGRESS_600, 0, 1, 19) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12
        player ?: return
        if (stage == 0) {
            line(player, "I need to speak to !!Pirate Pete?? in Port Phasmatys.", line++, false)
            line++
            line(player, "To complete this quest I need:", line++, false)
            line(
                player,
                if (getStatLevel(player, Skills.FARMING) >=
                    40
                ) {
                    "---40 Farming/--"
                } else {
                    "!!40 Farming??"
                },
                line++,
            )
            line(
                player,
                if (getStatLevel(player, Skills.FISHING) >=
                    50
                ) {
                    "---50 Fishing/--"
                } else {
                    "!!50 Fishing??"
                },
                line++,
            )
            line(player, if (getStatLevel(player, Skills.PRAYER) >= 47) "---47 Prayer/--" else "!!47 Prayer??", line++)
            line(
                player,
                if (getStatLevel(player, Skills.CRAFTING) >=
                    42
                ) {
                    "---42 Crafting/--"
                } else {
                    "!!42 Crafting??"
                },
                line++,
            )
            line(player, if (getStatLevel(player, Skills.SLAYER) >= 42) "---42 Slayer/--" else "!!42 Slayer??", line++)
            line(
                player,
                if (!isQuestComplete(
                        player,
                        Quests.ZOGRE_FLESH_EATERS,
                    )
                ) {
                    "---I must have completed Zogre Flesh Eaters/--"
                } else {
                    "!!I must have completed Zogre Flesh Eaters??"
                },
                line++,
            )
            line(player, "!!To be able to defeat a level 150 Monster", line++)
        }

        if (stage >= 1) {
            line(player, "I have spoken to !!Pirate Pete??, and have agreed to help", line++, stage >= 2)
            line(player, "him find his !!Family Sword??.", line++, stage >= 2)
            line++
            line(player, "I have agreed to help Pirate Pete slay !!Barrelor the??", line++, stage >= 2)
            line(player, "!!Destroyer?? so that he can reclaim his !!lands??.", line++, stage >= 2)
        }

        if (stage >= 2) {
            line(player, "I have a splitting headache. Captain Braindeath has explained", line++, stage >= 3)
            line(player, "the situation to me, and I have agreed to help brew up a batch", line++, stage >= 3)
            line(player, "of 'rum'.", line++, stage >= 3)
            line++
            line(player, "I have been given some !!Blindweed Seeds??, and told to grow them in the", line++, stage >= 3)
            line(player, "!!Herb Patch?? outside.", line++, stage >= 3)
            line++
        }

        if (stage >= 3) {
            line(player, "Captain Braindeath recommended that I try and intimidate the", line++, stage >= 4)
            line(
                player,
                "!!Swabs?? guarding the Herb Patch if I want them to !!stop attacking me??.",
                line++,
                stage >= 4,
            )
            line++
        }

        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
        }
    }

    override fun hasRequirements(player: Player?): Boolean {
        if (player != null) {
            if (getStatLevel(player, Skills.FARMING) < 40) {
                return false
            }
            if (getStatLevel(player, Skills.FISHING) < 50) {
                return false
            }
            if (getStatLevel(player, Skills.PRAYER) < 47) {
                return false
            }
            if (getStatLevel(player, Skills.CRAFTING) < 42) {
                return false
            }
            if (getStatLevel(player, Skills.SLAYER) < 42) {
                return false
            }
            return true
        }
        return false
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10
        sendMessage(player, "Congratulations!! Quest complete!")
        sendItemZoomOnInterface(player, 277, 5, Items.HOLY_WRENCH_6714, 230)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "Holy Wrench", ln++)
        drawReward(player, "7000 prayer XP, 7000 Fishing", ln++)
        drawReward(player, "XP and 7000 Farming XP", ln)
        rewardXP(player, Skills.FARMING, 7000.0)
        rewardXP(player, Skills.FISHING, 7000.0)
        rewardXP(player, Skills.PRAYER, 7000.0)
        addItemOrDrop(player, Items.HOLY_WRENCH_6714)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
