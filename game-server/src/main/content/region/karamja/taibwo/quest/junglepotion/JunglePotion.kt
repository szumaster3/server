package content.region.karamja.taibwo.quest.junglepotion

import content.global.skill.herblore.HerbItem
import content.region.karamja.taibwo.quest.junglepotion.dialogue.JogreCavernDialogue
import content.region.karamja.taibwo.quest.junglepotion.dialogue.TrufitusDialogue
import content.region.karamja.taibwo.quest.junglepotion.plugin.JungleObjective
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class JunglePotion : Quest(Quests.JUNGLE_POTION, 81, 80, 1, Vars.VARP_QUEST_JUNGLE_POTION_PROGRESS_175, 0, 1, 12) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12

        if(stage == 0) {
            line(player, "I can start this quest by speaking to !!Trufitus Shakaya??", line++)
            line(player, "who lives in the main hut in !!Tai Bwo Wannai??", line++)
            line(player, "village on the island of !!Karamja??.", line)
        }

        when (stage) {
            10, 20, 30, 40, 50 -> {
                val objective = JungleObjective.forStage(stage)
                val herbName = getItemName(objective!!.herb.product.id)
                if (objective.herb.product.id.let { inInventory(player, it) }) {
                    line(player, "I spoke to Trufitus, he needs to commune with the", line++, true)
                    line(player, "gods, he's asked me to help him by collecting herbs.", line++, true)
                    line(player, "I picked some fresh $herbName for Trufitus.", line++, true)
                    line(player, "I need to give the !!$herbName?? to !!Trufitus??.", line)
                    return
                }
                line(player, "I spoke to Trufitus, he needs to commune with the", line++, true)
                line(player, "gods, he's asked me to help him by collecting herbs.", line++, true)
                line(player, "I need to pick some fresh !!$herbName?? for !!Trufitus??.", line)
            }
            60 -> {
                line(player, "I spoke to Trufitus, he needs to commune with the", line++, true)
                line(player, "gods, he's asked me to help him by collecting herbs.", line++, true)
                line(player, "I have given Trufitus Snakeweed, Ardrigal,", line++, true)
                line(player, "Sito Foil, Volencia moss and Rogues purse.", line++, true)
                line(player, "Trufitus needs to commune with the gods.", line++, true)
                line(player, "I should speak to !!Trufitus??.", line)
            }
            100 -> {
                line(player, "Trufitus Shakaya of the Tai Bwo Wannai village needed", line++, true)
                line(player, "some jungle herbs in order to make a potion which would", line++, true)
                line(player, "help him commune with the gods. I collected five lots", line++, true)
                line(player, "of jungle herbs for him and he was able to", line++, true)
                line(player, "commune with the gods.", line++, true)
                line(player, "As a reward he showed me some herblore techniques.", line++, true)
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
            }
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, HerbItem.VOLENCIA_MOSS.product.id)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "775 Herblore XP", ln)
        rewardXP(player, Skills.HERBLORE, 775.0)
        updateQuestTab(player)
        setVarbit(player, 897, 2)
        setVarbit(player, 898, 2)
        setVarbit(player, 899, 2)
        setVarbit(player, 900, 2)
        setVarbit(player, 896, 2, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        definePlugin(TrufitusDialogue())
        definePlugin(JogreCavernDialogue())
        return this
    }

    override fun getConfig(player: Player, stage: Int): IntArray {
        if (stage == 0) return intArrayOf(175, 0)
        if (stage == 100) return intArrayOf(175, 15)
        if (stage > 0) return intArrayOf(175, 1)
        return intArrayOf(175, 15)
    }
}
