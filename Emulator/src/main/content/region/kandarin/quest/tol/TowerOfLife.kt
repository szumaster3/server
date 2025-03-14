package content.region.kandarin.quest.tol

import core.api.hasLevelStat
import core.api.rewardXP
import core.api.sendItemOnInterface
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

class TowerOfLife : Quest(Quests.TOWER_OF_LIFE, 134, 133, 2, Vars.VARBIT_QUEST_TOWER_OF_LIFE_PROGRESS_3337, 0, 1, 18) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        player ?: return
        if (stage == 0) {
            line(player, "Minimum requirements:", line++, false)
            line++
            line(
                player,
                if (hasLevelStat(
                        player,
                        Skills.CONSTRUCTION,
                        10,
                    )
                ) {
                    "---Level 10 Construction/--"
                } else {
                    "!!Level 10 Construction??"
                },
                line++,
            )
            line++
            line(player, "I can start this quest by talking to !!Effigy?? at the !!tower??", line++, false)
            line(player, "!!south-east of Ardougne??.", line++, false)
            line++
        }
        if (stage >= 1) {
            line(player, "Effigy told me the tower has ceased construction because", line++, false)
            line(player, "of the !!builders'?? strike.", line++, false)
            line++
        }
        if (stage == 2) {
            line(player, "Hopefully, talking to !!Bonafido??, the head builder, will sort things out.", line++, false)
            line(player, "!!Bonafido?? says I can go into the tower if I dress up like !!a builder??.", line++, false)
            line(player, "", line++, false)
            line++
        }
        if (stage >= 2) {
            line(player, "I should be able to find some clothing around the tower.", line++, false)
            line++
        }
        if (stage >= 3) {
            line(player, "I got my kit together and passed", line++, false)
            line(player, "an initiation ritual.", line++, false)
            line++
        }
        if (stage >= 18) {
            line(player, "Time to venture into the tower!", line++, false)
            line(player, "This place is amazing! Time to fix things and tell Effigy.", line++, false)
            line++
        }
        if (stage == 100) {
            line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10
        sendItemOnInterface(player, Components.QUESTJOURNAL_SCROLL_275, 5, Items.BUILDERS_SHIRT_10863, 1)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "1,000 Construction XP", ln++)
        drawReward(player, "500 Crafting", ln++)
        drawReward(player, "500 Thieving", ln)
        rewardXP(player, Skills.CONSTRUCTION, 1000.0)
        rewardXP(player, Skills.CRAFTING, 500.0)
        rewardXP(player, Skills.THIEVING, 500.0)
        setVarbit(player, Vars.VARBIT_QUEST_TOWER_OF_LIFE_PROGRESS_3337, 18, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
