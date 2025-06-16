package content.region.karamja.tzhaar.quest.dillo

import core.api.rewardXP
import core.api.sendItemZoomOnInterface
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

/**
 * Represents the Obsidian Armadillo Guard quest.
 */
// @Initializable
class TokTzKetDill : Quest(Quests.TOKTZ_KET_DILL, 152, 151, 1, Vars.VARBIT_QUEST_TOKTZ_KET_DRILL_PROGRESS_4700, 0, 1, 63) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!one of the three??", line++)
            line(player, "!!TzHaar?? gathered outside the collapsed tunnel in the", line++)
            line(player, "TzHaar city.", line++)
            line++
            line(player, "Minimum requirements:", line++)
            line(player, "!!Level 40 Attack??.", line++, player.skills?.getStaticLevel(Skills.ATTACK)!! >= 40)
            line(player, "!!Level 50 Construction??.", line++, player.skills?.getStaticLevel(Skills.CONSTRUCTION)!! >= 50)
            line(player, "!!Level 43 Crafting??.", line++, player.skills?.getStaticLevel(Skills.CRAFTING)!! >= 43)
            line(player, "!!Level 41 Mining??.", line++, player.skills?.getStaticLevel(Skills.MINING)!! >= 41)
            line(player, "!!Level 45 Strength??.", line++, player.skills?.getStaticLevel(Skills.STRENGTH)!! >= 45)
            line++
            line++
            line(player, "I must be able to defeat a !!level 100 monster??.", line++)
            line++
            line++
            line(player, "I must be able to defeat a !!level 47 monsters?? using !!Magic??", line++)
            line(player, "and !!Ranged??.", line++)
        }
        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.PILLAR_13246)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "5,000 Attack XP", ln++)
        drawReward(player, "5,000 Strength XP", ln++)
        drawReward(player, "10,000 Crafting XP", ln++)
        drawReward(player, "15,000 Mining XP", ln++)
        drawReward(player, "20,000 Construction XP", ln++)
        drawReward(player, "Access to new Mining area", ln)

        rewardXP(player, Skills.ATTACK, 5000.0)
        rewardXP(player, Skills.STRENGTH, 5000.0)
        rewardXP(player, Skills.CRAFTING, 10000.0)
        rewardXP(player, Skills.MINING, 15000.0)
        rewardXP(player, Skills.CONSTRUCTION, 20000.0)

        setVarbit(player, Vars.VARBIT_QUEST_TOKTZ_KET_DRILL_PROGRESS_4700, 63, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

}
// 1 quest point
// 5,000 Attack experience
// 5,000 Strength experience
// 10,000 Crafting experience
// 15,000 Mining experience
// 20,000 Construction experience
// Access to the TokTz-Ket-Dill mine with 1 silver rock, 8 coal rocks, 3 mithril rocks and 2 adamantite rocks (access is obtained after reaching the third dungeon; you don't have to kill the TokTz-Ket-Dill).
// Ability to mine stone slabs (unsmithable obsidian) in the TzHaar obsidian mine
// The possibility of watching TzHaar-Hur-Brekt's play
// 2 Treasure Hunter keys (Ironman accounts will not receive these)