package content.region.asgarnia.falador.quest.squire

import core.api.displayQuestItem
import core.api.rewardXP
import core.api.updateQuestTab
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import shared.consts.Components
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Vars

/**
 * The knight's sword quest journal.
 */
@Initializable
class TheKnightsSword : Quest(Quests.THE_KNIGHTS_SWORD, 22, 21, 1, Vars.VARP_QUEST_KNIGHTS_SWORD_PROGRESS_122, 0, 1, 7) {

    override fun newInstance(`object`: Any): Quest {
        return this
    }

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        when (stage) {
            0 -> {
                player.packetDispatch.sendString(BLUE + "I can start this quest by speaking to the " + RED + "Squire " + BLUE + "in the", 275, 4 + 7)
                player.packetDispatch.sendString(BLUE + "courtyard of the " + RED + "White Knights' Castle " + BLUE + "in " + RED + "southern Falador", 275, 5 + 7)
                player.packetDispatch.sendString(BLUE + "To complete this quest I need:", 275, 6 + 7)
                player.packetDispatch.sendString(RED + "Level 10 Mining", 275, 7 + 7)
                player.packetDispatch.sendString(BLUE + "and to be unafraid of " + RED + "Level 57 Ice Warriors.", 275, 8 + 7)
            }

            10 -> {
                line(player, "<str>I told the Squire I would help him to replace the sword he", 4 + 7)
                line(player, "<str>has lost. It could only be made by an Imcando Dwarf.", 5 + 7)
                line(player, BLUE + "The Squire suggests I speak to " + RED + "Reldo " + BLUE + "in the " + RED + " Varrock Palace", 6 + 7)
                line(player, RED + "Library " + BLUE + "for information about the " + RED + "Imcando Dwarves", 7 + 7)
            }

            20 -> {
                line(player, "<str>I told the Squire I would help him to replace the sword he", 4 + 7)
                line(player, "<str>has lost. It could only be made by an Imcando Dwarf.", 5 + 7)
                line(player, BLUE + "Reldo couldn't give me much information about the", 6 + 7)
                line(player, RED + "Imcando " + BLUE + "except a few live on the " + RED + "southern peninsula of", 7 + 7)
                line(player, RED + "Asgarnia, " + BLUE + "they dislike strangers, and LOVE " + RED + "redberry pies.", 8 + 7)
            }

            30 -> {
                line(player, "<str>I told the Squire I would help him to replace the sword he", 4 + 7)
                line(player, "<str>has lost. It could only be made by an Imcando Dwarf.", 5 + 7)
                line(player, "<str>I found an Imcando Dwarf named Thurgo thanks to", 6 + 7)
                line(player, "<str>information provided by Reldo. He wasn't very talkative", 7 + 7)
                line(player, "<str>until I gave him a Redberry pie, which he gobbled up.", 8 + 7)
                line(player, BLUE + "He will help me now I have gained his trust through " + RED + "pie", 9 + 7)
            }

            40 -> {
                line(player, "<str>I told the Squire I would help him to replace the sword he", 4 + 7)
                line(player, "<str>has lost. It could only be made by an Imcando Dwarf.", 5 + 7)
                line(player, "<str>I found an Imcando Dwarf named Thurgo thanks to", 6 + 7)
                line(player, "<str>information provided by Reldo. He wasn't very talkative", 7 + 7)
                line(player, "<str>until I gave him a Redberry pie, which he gobbled up.", 8 + 7)
                line(player, RED + "Thurgo " + BLUE + "needs a " + RED + "picture of the sword " + BLUE + "before he can help.", 9 + 7)
                line(player, BLUE + "I should probably ask the " + RED + "Squire " + BLUE + "about obtaining one", 10 + 7)
            }

            50 -> {
                line(player, "<str>I told the Squire I would help him to replace the sword he", 4 + 7)
                line(player, "<str>has lost. It could only be made by an Imcando Dwarf.", 5 + 7)
                line(player, "<str>I found an Imcando Dwarf named Thurgo thanks to", 6 + 7)
                line(player, "<str>information provided by Reldo. He wasn't very talkative", 7 + 7)
                line(player, "<str>until I gave him a Redberry pie, which he gobbled up.", 8 + 7)
                line(player, "<str>Thurgo needed a picture of the sword to replace.", 9 + 7)
                if (!player.inventory.containsItem(PORTRAIT)) {
                    line(player, BLUE + "The Squire told me about a " + RED + "portrait ", 10 + 7)
                    line(player, BLUE + "which has a " + RED + "picture of the sword " + BLUE + "in " + RED + "Sir Vyvin's room", 11 + 7)
                } else {
                    line(player, BLUE + "I now have a picture of the " + RED + "Knight's Sword " + BLUE + "- I should take it", 10 + 7)
                    line(player, BLUE + "to " + RED + "Thurgo " + BLUE + "so that he can duplicate it.", 11 + 7)
                }
            }

            60 -> {
                line(player, "<str>I told the Squire I would help him to replace the sword he", 4 + 7)
                line(player, "<str>has lost. It could only be made by an Imcando Dwarf.", 5 + 7)
                line(player, "<str>I found an Imcando Dwarf named Thurgo thanks to", 6 + 7)
                line(player, "<str>information provided by Reldo. He wasn't very talkative", 7 + 7)
                line(player, "<str>until I gave him a Redberry pie, which he gobbled up.", 8 + 7)
                line(player, "<str>Thurgo needed a picture of the sword before he could", 9 + 7)
                line(player, "<str>start work on a replacement. I took him a portrait of it.", 10 + 7)
                if (player.inventory.contains(Items.BLURITE_SWORD_667, 1) || player.equipment.contains(Items.BLURITE_SWORD_667, 1) || player.bank.contains(Items.BLURITE_SWORD_667, 1)) {
                    line(player, "<str>Thurgo has now smithed me a replica of Sir Vyvin's sword.", 11 + 7)
                    line(player, BLUE + "I should return it to the " + RED + "Squire " + BLUE + "for my " + RED + "reward", 13 + 7)
                } else {
                    line(player, BLUE + "according to " + RED + "Thurgo " + BLUE + "to make a " + RED + "replica sword " + BLUE + "he will need", 11 + 7)
                    line(player, RED + "two Iron Bars " + BLUE + "and some " + RED + "Blurite Ore. Blurite Ore " + BLUE + "can only be", 12 + 7)
                    line(player, BLUE + "found " + RED + "deep in the caves below Thurgo's house", 13 + 7)
                }
            }

            100 -> {
                line(player, "<str>Thurgo needed a picture of the sword before he could", 4 + 7)
                line(player, "<str>start work on a replacement. I took him a portrait of it.", 5 + 7)
                line(player, "<str>After bringing Thurgo two iron bars and some blurite ore", 6 + 7)
                line(player, "<str>he made me a fine replica of Sir Vyvin's Sword, which I", 7 + 7)
                line(player, "<str>returned to the Squire for a reward.", 8 + 7)
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", 10 + 7)
            }
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var line = 10

        displayQuestItem(player, Items.BLURITE_SWORD_667)
        drawReward(player, "1 Quest Point", line++)
        drawReward(player, "12,725 Smithing XP", line)
        rewardXP(player, Skills.SMITHING, 12725.0)
        updateQuestTab(player)
    }

    companion object {
        private val PORTRAIT = Item(Items.PORTRAIT_666)
    }
}