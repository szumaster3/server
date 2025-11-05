package content.region.kandarin.yanille.quest.handsand

import core.api.displayQuestItem
import core.api.hasLevelStat
import shared.consts.Vars
import core.api.rewardXP
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.Quests

@Initializable
class TheHandintheSand : Quest(Quests.THE_HAND_IN_THE_SAND, 72, 71, 1, Vars.VARBIT_QUEST_THE_HAND_IN_THE_SAND_PROGRESS_1527, 0, 1, 160) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11

        if(stage == 0) {
            line(player, "I can start this quest by speaking to !!Bert?? in !!Yanille?? in the", line++)
            line(player, "house near the !!Sandpit??.", line++)
            line(player, "Before I begin I will need to:", line++)
            line(player, "!!Have level 17 Thieving??.", line++, hasLevelStat(player, Skills.THIEVING, 17))
            line(player, "!!Have level 49 Crafting??.", line++, hasLevelStat(player, Skills.THIEVING, 49))
        }
        if(stage == 1) {
            line(player, "I should speak to the Guard Captain.", line++)
        }
        if(stage == 2) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I need to show the hand to the !!Wizards?? in !!Yanille??.", line++)
        }
        if(stage == 3) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
        }
        if(stage == 4) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
            line(player, "I have Bert's copy of the Rota.", line++, true)
        }
        if(stage == 5) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
            line(player, "I have Bert's copy of the Rota.", line++, true)
            line(player, "I have Sandy's copy of the Rota.", line++, true)
        }
        if(stage == 6) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
            line(player, "I have Bert's copy of the Rota.", line++, true)
            line(player, "I have Sandy's copy of the Rota.", line++, true)
            line(player, "I have taken the scroll to Zavistic Rarve.", line++, true)
        }
        if(stage == 7) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
            line(player, "I have Bert's copy of the Rota.", line++, true)
            line(player, "I have Sandy's copy of the Rota.", line++, true)
            line(player, "I have taken the scroll to Zavistic Rarve.", line++, true)
        }
        if(stage == 8) {
            line(player, "I have distracted Sandy successfully.", line++, true)
            line(player, "I have drugged Sandy's coffee.", line++)
            line(player, "I have interrogated Sandy.", line++)
            line(player, "I have returned the information from the orb.", line++)
            line(player, "The Sandpit has been enchanted.", line++)
            line(player, "I have retrieved the head of a wizard.", line++)
            line(player, "The dead wizard has been buried and Sandy arrested for murder.", line++)
        }

        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
            line++
            line(player, "Every day I may ask Bert to transport some sand to my bank.", line++)
            line(player, "You can collect your sand now.", line) // "You'll need to wait about X hours to collect your sand."

        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        displayQuestItem(player, Items.SANDY_HAND_6945)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "1000 Thieving XP", ln++)
        drawReward(player, "9000 Crafting XP", ln++)
        drawReward(player, "Secret reward from Bert", ln)
        rewardXP(player, Skills.THIEVING, 1000.0)
        rewardXP(player, Skills.CRAFTING, 9000.0)
        setVarbit(player, Vars.VARBIT_QUEST_THE_HAND_IN_THE_SAND_PROGRESS_1527, 160, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

}
//1 quest point
//1,000 Thieving experience
//9,000 Crafting experience
//Able to buy pink dye from Betty
//Bert will ship 84 buckets of sand to your bank if you talk to him after the quest. This number is increased to 120 after completing the elite Ardougne Tasks. This can be done once per day. Clicking on the quest's name will give you the time left before you can ask him again.
//2 Treasure Hunter keys (Ironman accounts will not receive these)
