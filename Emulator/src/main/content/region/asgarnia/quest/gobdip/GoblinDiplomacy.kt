package content.region.asgarnia.quest.gobdip

import content.region.asgarnia.quest.gobdip.dialogue.GrubfootDialogue
import content.region.asgarnia.quest.gobdip.handlers.GDiplomacyCutscene
import content.region.asgarnia.quest.gobdip.handlers.GoblinDiplomacyPlugin
import core.api.addItemOrDrop
import core.api.inInventory
import core.api.quest.updateQuestTab
import core.api.rewardXP
import core.api.sendItemZoomOnInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.ClassScanner.definePlugins
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class GoblinDiplomacy :
    Quest(Quests.GOBLIN_DIPLOMACY, 20, 19, 5, Vars.VARP_QUEST_GOBLIN_DIPLOMACY_QUEST_PROGRESS_62, 0, 1, 100) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        when (stage) {
            0 -> {
                line(player, "I can start this quest by speaking to !!Generals Wartface??", line++)
                line(player, "and Bentnoze in the !!Goblin Village??.", line++)
                line(player, "There are no requirements for this quest.", line)
            }

            in 1..9 -> {
                line(player, "I spoke to Generals Wartface and Bentnoze in the Goblin", line++, true)
                line(player, "Village and found that the goblins were on the bring of civil", line++, true)
                line(player, "war over the colour of their armour. I offered to help the", line++, true)
                line(player, "generals by finding another colour that they both like.", line++, true)
                if (inInventory(player, Items.ORANGE_GOBLIN_MAIL_286)) {
                    line(player, "I have some !!Orange Goblin Armour??. I should show it to the", line++, true)
                    line(player, "generals.", line)
                } else {
                    line(player, "I should bring the goblins some !!Orange Goblin Armour??", line++, true)
                    line(player, "Maybe the generals will know where to get some.", line, true)
                }
            }

            in 10..19 -> {
                line(player, "I spoke to Generals Wartface and Bentnoze in the Goblin", line++, true)
                line(player, "Village and found that the goblins were on the bring of civil", line++, true)
                line(player, "war over the colour of their armour. I offered to help the", line++, true)
                line(player, "generals by finding another colour that they both like.", line++, true)
                line(player, "I brought the goblins some orange goblin armour, but they", line++, true)
                line(player, "didn't like it.", line++, true)
                if (inInventory(player, Items.BLUE_GOBLIN_MAIL_287)) {
                    line(player, "I have some !!Blue Goblin Armour??. I should show it to the", line++, true)
                    line(player, "generals.", line, true)
                } else {
                    line(player, "I should bring the goblins some !!Blue Goblin Armour??", line++, true)
                    line(player, "Maybe the generals will know where to get some.", line, true)
                }
            }

            in 20..29 -> {
                line(player, "I spoke to Generals Wartface and Bentnoze in the Goblin", line++, true)
                line(player, "Village and found that the goblins were on the bring of civil", line++, true)
                line(player, "war over the colour of their armour. I offered to help the", line++, true)
                line(player, "generals by finding another colour that they both like.", line++, true)
                line(player, "I brought the goblins some orange goblin armour, but they", line++, true)
                line(player, "didn't like it.", line++, true)
                line(player, "I brought the goblins some blue goblin armour, but they", line++, true)
                line(player, "didn't like it.", line++, true)
                if (inInventory(player, Items.GOBLIN_MAIL_288)) {
                    line(player, "I have some !!Brown Goblin Armour??. I should show it to the", line++, true)
                    line(player, "generals.", line, true)
                } else {
                    line(player, "I should bring the goblins some !!Brown Goblin Armour??", line++, true)
                    line(player, "Maybe the generals will know where to get some.", line, true)
                }
            }

            in 30..100 -> {
                line(player, "I spoke to Generals Wartface and Bentnoze in the Goblin", line++, true)
                line(player, "Village and found that the goblins were on the bring of civil", line++, true)
                line(player, "war over the colour of their armour. I offered to help the", line++, true)
                line(player, "generals by finding another colour that they both like.", line++, true)
                line(player, "I brought the goblins some orange goblin armour, but they", line++, true)
                line(player, "didn't like it.", line++, true)
                line(player, "I brought the goblins some blue goblin armour, but they", line++, true)
                line(player, "didn't like it.", line++, true)
                line(player, "Unfortunately the goblins were very stupid, and it turned", line++, true)
                line(player, "out that they liked the original colour the most.", line++, true)
                line(player, "That's goblins for you I guess.", line - 1, true)
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
            }
        }
    }

    override fun newInstance(`object`: Any?): Quest {
        definePlugins(GDiplomacyCutscene(), GoblinDiplomacyPlugin(), GrubfootDialogue())
        return this
    }

    override fun finish(player: Player) {
        super.finish(player)
        var line = 10

        drawReward(player, "5 Quests Points", line++)
        drawReward(player, "200 Crafting XP", line++)
        drawReward(player, "A gold bar", line++)
        drawReward(player, "You have completed the Goblin Diplomacy Quest!", line)

        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.GOBLIN_MAIL_288, 230)
        rewardXP(player, Skills.CRAFTING, 200.0)
        addItemOrDrop(player, Items.GOLD_BAR_2357, 1)
        updateQuestTab(player)
    }

    override fun getConfig(
        player: Player,
        stage: Int,
    ): IntArray {
        if (stage == 0) {
            return intArrayOf(Vars.VARP_QUEST_GOBLIN_DIPLOMACY_QUEST_PROGRESS_62, 0)
        }
        return when (stage) {
            10 -> intArrayOf(Vars.VARP_QUEST_GOBLIN_DIPLOMACY_QUEST_PROGRESS_62, 1)
            20 -> intArrayOf(Vars.VARP_QUEST_GOBLIN_DIPLOMACY_QUEST_PROGRESS_62, 4)
            30 -> intArrayOf(Vars.VARP_QUEST_GOBLIN_DIPLOMACY_QUEST_PROGRESS_62, 5)
            100 -> intArrayOf(Vars.VARP_QUEST_GOBLIN_DIPLOMACY_QUEST_PROGRESS_62, 6)
            else -> intArrayOf(Vars.VARP_QUEST_GOBLIN_DIPLOMACY_QUEST_PROGRESS_62, 0)
        }
    }
}
