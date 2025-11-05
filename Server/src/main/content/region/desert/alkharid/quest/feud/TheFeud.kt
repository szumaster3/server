package content.region.desert.alkharid.quest.feud

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Vars

//@Initializable
class TheFeud : Quest(Quests.THE_FEUD, 60, 59, 1, Vars.VARBIT_THE_FEUD_PROGRESS_334, 0, 1, 28) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        var stage = getVarbit(player, Vars.VARBIT_THE_FEUD_PROGRESS_334)

        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Ali Morrisane??, a", line++, false)
            line(player, "merchant in the !!Al Kharid bazaar.??", line++, false)
            line(player, "Minimum requirements:", line++, false)
            line(player, "!!Level 30 Thieving??", line++, false)
            line(player, "!!I must be able to kill a level 75 Warrior.??", line++, false)
            line++
        }

        if (stage == 1) {
            line(player, "I met with Ali Morrisane, who asked me to travel to !!Pollnivneach??,", line++, false)
            line(player, "a small town to the south of Al Kharid and look for !!his nephew??.", line++, false)
        }

        if (stage == 2) {
            line(player, "A local drunk suggested that Ali's newphew's disappearance may be", line++, false)
            line(player, "linked to the two gangs that seem to run the town.", line++, false)
        }

        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        displayQuestItem(player, Items.DESERT_DISGUISE_4611)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "15,000 Thieving XP", ln)

        rewardXP(player, Skills.THIEVING, 15000.0)

        addItemOrDrop(player, Items.MAPLE_BLACKJACKD_6421)
        addItemOrDrop(player, Items.DESERT_DISGUISE_4611)
        addItemOrDrop(player, Items.COINS_995, 500)

        setVarbit(player, Vars.VARBIT_QUEST_THE_FEUD_PROGRESS_334, 28, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

}
// Key 4589
// 1 quest point
// Blackjacks
// 15,000 Thieving experience
// Desert disguise
// 500 coins
// The ability to do another part of Rogue Trader
// Free beer when telling Faisal the Barman about Traitorous Hesham's drink being poisoned.