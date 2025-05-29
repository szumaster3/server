package content.region.misc.quest.icemt

import core.api.*
import org.rs.consts.Vars
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests

// @Initializable
class PerilsOfIceMountain :
    Quest(Quests.PERILS_OF_ICE_MOUNTAIN, 151, 150, 1, Vars.VARBIT_QUEST_PERILS_OF_ICE_MOUNTAIN_PROGRESS_4684, 0, 1, 150) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)

        var line = 11

        if(stage == 0) {
            line(player, "I can start this quest  by speaking to !!Lakki the delivery??", line++)
            line(player, "!!dwarf??, on the road near !!Ice Mountain??.", line++)
            line(player, "Minimum requirements:", line++)
            line(player, "Level 10 Construction", line++, hasLevelStat(player, Skills.CONSTRUCTION, 10))
            line(player, "Level 10 Farming", line++, hasLevelStat(player, Skills.FARMING, 10))
            line(player, "Level 10 Hunter", line++, hasLevelStat(player, Skills.HUNTER, 10))
            line(player, "Level 11 Thieving", line++, hasLevelStat(player, Skills.THIEVING, 11))
            limitScrolling(player, line, true)
        }

        if(stage == 10) {
            line(player, "I talked to Lakki the delivery dwarf.", line++)
            line(player, "I found the delivery dwarf's missing crate in one of the bushes by the road.", line++)
            line(player, "I gave the crate to Drorkar in the power station.", line++)
            line(player, "I gave Drorkar's letter to Brother Bordiss.", line++)
            line(player, "I talked to Brother Althric about his dying roses.", line++)
            line(player, "I saved Brother Althric's roses.", line++)
            line(player, "Suddenly, a gnome parachuted out of the sky and ran off to Ice Mountain.", line++)
            line(player, "I talked to Professor Arblenap, the gnome natural historian", line++)
            line(player, "who was visiting Ice Mountain.", line++)
            line(player, "He asked me to help him by catching four baby icefiends.", line++)
            line(player, "I gave four baby icefiends to Professor Arblenap.", line++)
            line(player, "Suddenly, there was an avalanche!", line++)
            line(player, "The avalanche wrecked the Oracle's tent.", line++)
            line(player, "I fixed the Oracle's tent.", line++)
            line(player, "She had vision of the future in which civilisation was destroyed by", line++)
            line(player, "rising temperatures.", line++)
            line(player, "I told Nurmof about the damage the power station was causing.", line++)
            line(player, "Nurmof told me that Bordiss made plans for an alternative power station,", line++)
            line(player, "but he never presented them.", line++)
            line(player, "I asked Bordiss about his plans for an alternative power station.", line++)
            line(player, "He told me that he had lost the key to his chest,", line++)
            line(player, "and that was why he felt he had to become a monk.", line++)
            line(player, "I picked Drorkar's pocket and found Bordiss's key. Drorkar had stolen it!", line++)
            line(player, "I used the key to get Bordiss's plans from his chest.", line++)
            line(player, "I gave the plans to Nurmof and he agreed to knock", line++)
            line(player, "down Drorkar's coal-dragon power station and", line++)
            line(player, "build Bordiss' windmill instead.", line++)
            line++
            line(player, "Ice Mountain environment is returning to normal.", line++)

        }

        if (stage == 100) {
            line(player, "!!QUEST COMPLETE!??", line, false)
            limitScrolling(player, line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10

        sendString(player, "Perils of Ice Mountain", Components.QUEST_COMPLETE_SCROLL_277, 4)

        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.PLANS_13233)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "500 Construction, Farming,", ln++)
        drawReward(player, "Hunter and Thieving XP", ln++)
        drawReward(player, "Ability to smith pickaxes", ln++)
        drawReward(player, "Permission to use the power", ln++)
        drawReward(player, "station ladder.", ln++)

        rewardXP(player, Skills.CONSTRUCTION, 500.0)
        rewardXP(player, Skills.FARMING, 500.0)
        rewardXP(player, Skills.HUNTER, 500.0)
        rewardXP(player, Skills.THIEVING, 500.0)
        setVarbit(player, Vars.VARBIT_QUEST_PERILS_OF_ICE_MOUNTAIN_PROGRESS_4684, 150, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

}
//1 quest point
//Knowledge of Ice Mountain, granting 500 experience in Farming, Hunter, Construction and Thieving
//Permission to use the power station ladder (allows you to quickly access the mine carts and Nurmof)
//Brother Bordiss will attach sigils to blessed spirit shields for a price.
//2 Treasure Hunter keys (Ironman accounts will not receive these)
//Music unlocked
//Icy Trouble Ahead (Unlocked during the third cutscene)
//Icy a Worried Gnome (Unlocked after the second cutscene)

