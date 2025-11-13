package content.region.kandarin.gnome_stronghold.quest.grandtree

import content.region.kandarin.gnome_stronghold.quest.grandtree.plugin.TheGrandTreeUtils
import core.api.displayQuestItem
import core.api.hasLevelStat
import core.api.removeAttributes
import core.api.rewardXP
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Vars

/**
 * Represents the Grand Tree quest.
 *
 * Author: [downthecrop](https://gitlab.com/downthecrop)
 */
@Initializable
class TheGrandTree : Quest(Quests.THE_GRAND_TREE, 71, 70, 5, Vars.VARP_QUEST_THE_GRAND_TREE_PROGRESS_150, 0, 1, 160) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        line(player, "I can start this quest at the !!Grand Tree?? in the !!Gnome??", line++, stage > 0)
        line(player, "!!Stronghold?? by speaking to !!King Narnode Shareen??.", line++, stage > 0)
        line++
        if (stage == 0) {
            line(player, "I must have:", line++)
            line(player, if (hasLevelStat(player, Skills.AGILITY, 25)) "---Level 25 Agility./--" else "!!Level 25 Agility.??", line++)
            line(player, "!!High enough combat to defeat a level 172 demon.??", line++)
            line++
        }
        if (stage >= 10) {
            line(player, "!!King Narnode Shareen?? suspects sabotage on the !!Grand Tree?? and", line++, stage > 10)
            line(player, "wants to confirm it by consulting with !!Hazelmere??.", line++, stage > 10)
            line++
            line(player, "!!Hazelmere's dwelling?? is located on a !!towering hill??, on an !!island??", line++, stage > 10)
            line(player, "!!east?? of !!Yanille??.", line++, stage > 10)
            line++
        }
        if (stage >= 20) {
            line(player, "Report back to !!King Narnode Shareen??.", line++, stage > 20)
            line++
        }
        if (stage >= 40) {
            line(player, "!!King Narnode?? suspects that someone has forged his !!royal seal??.", line++, stage > 40)
            line(player, "Find !!Glough?? and notify him about the situation.", line++, stage > 40)
            line++
        }
        if (stage >= 45) {
            line(player, "!!Glough?? says the humans are going to !!invade!?? Report back to !!the king??.", line++, stage > 45)
            line++
        }
        if (stage >= 46) {
            line(player, "Talk to the !!prisoner?? at the top of the tree", line++, stage > 46)
            line++
        }
        if (stage >= 47) {
            line(player, "Search !!Glough's home?? for clues.", line++, stage > 47)
            line++
        }
        if (stage >= 55) {
            line(player, "!!Glough?? has placed guards on the !!front gate?? to stop you escaping!", line++, stage > 55)
            line(player, "Use !!King Narnode's glider?? pilot fly you away until things calm down.", line++, stage > 55)
            if (player.hasItem(Item(Items.LUMBER_ORDER_787))) {
                line(player, "Bring the !!lumber order?? to !!Charlie??", line++, stage > 55)
                line++
            }
            line++
        }
        if (stage >= 60) {
            line(player, "Search !!Glough's girlfriend's house?? for his !!chest keys??.", line++, stage > 60)
            line++
        }
        if (stage >= 60) {
            if (player.hasItem(Item(Items.GLOUGHS_KEY_788))) {
                line(player, "Find a use for !!Gloughs key?? and report back to !!the king??.", line++, stage > 60)
                line++
            }
        }
        if (stage >= 70) {
            line(player, "Find a use for the !!strange twigs?? from !!King Narnode Shareen??.", line++, stage > 70)
            line++
        }
        if (stage == 100) {
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        displayQuestItem(player, Items.DACONIA_ROCK_793)
        drawReward(player, "5 Quest Points", ln++)
        drawReward(player, "7900 Agility XP", ln++)
        drawReward(player, "18,400 Attack XP", ln++)
        drawReward(player, "2150 Magic XP", ln)
        rewardXP(player, Skills.AGILITY, 7900.0)
        rewardXP(player, Skills.ATTACK, 18400.0)
        rewardXP(player, Skills.MAGIC, 2150.0)
        removeAttributes(player, TheGrandTreeUtils.DRACONIA_ROCK, TheGrandTreeUtils.FEMI_HELP_TRUE, TheGrandTreeUtils.FEMI_TALK, TheGrandTreeUtils.TWIG_0, TheGrandTreeUtils.TWIG_1, TheGrandTreeUtils.TWIG_2, TheGrandTreeUtils.TWIG_3)
    }

    override fun newInstance(`object`: Any?): Quest = this
}
