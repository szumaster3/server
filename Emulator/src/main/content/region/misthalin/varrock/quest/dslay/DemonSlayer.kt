package content.region.misthalin.varrock.quest.dslay

import content.region.misthalin.varrock.quest.dslay.cutscene.DemonSlayerCutscenePlugin
import content.region.misthalin.varrock.quest.dslay.cutscene.WallyCutscenePlugin
import content.region.misthalin.varrock.quest.dslay.dialogue.CaptainRovinDialogue
import content.region.misthalin.varrock.quest.dslay.dialogue.GypsyArisDialogue
import content.region.misthalin.varrock.quest.dslay.dialogue.SirPyrsinDialogue
import content.region.misthalin.varrock.quest.dslay.dialogue.TraibornDialogue
import content.region.misthalin.varrock.quest.dslay.plugin.DemonSlayerDrainPlugin
import content.region.misthalin.varrock.quest.dslay.plugin.DemonSlayerPlugin
import content.region.misthalin.varrock.quest.dslay.plugin.DemonSlayerUtils
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import core.plugin.ClassScanner
import core.plugin.Initializable
import shared.consts.Components
import shared.consts.Quests
import shared.consts.Vars

/**
 * Represents the Demon slayer quest journal.
 */
@Initializable
class DemonSlayer : Quest(Quests.DEMON_SLAYER, 16, 15, 3, Vars.VARP_QUEST_DEMON_SLAYER_PROGRESS_222, 0, 1, 3) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        val items = intArrayOf(DemonSlayerUtils.FIRST_KEY.id, DemonSlayerUtils.SECOND_KEY.id, DemonSlayerUtils.THIRD_KEY.id)

        if (stage >= 0) {
            line(player, "I can start this quest by speaking to the !!Gypsy?? in the !!tent??", line++, stage>=1)
            line(player, "in !!Varrock's main square??.", line++, stage>=1)
            line++
            line(player, "I must be able to defeat a level 27 !!apocalyptic demon??!", line++, stage>=1)
            limitScrolling(player, line, true)
        }

        if (stage >= 10) {
            line(player, "I spoke to the Gypsy in Varrock Square who saw my future.", line++, stage>=20)
            line(player, "Unfortunately it involved killing a demon who nearly", line++, stage>=20)
            line(player, "destroyed Varrock over 150 years ago.", line++, stage>=20)
            line++
            line(player, "To defeat the !!demon?? I need the magical sword !!Silverlight??.", line++, stage>=20)
            line(player, "I should ask !!Sir Prysin?? in !!Varrock Palace?? where it is.", line++, stage>=20)
        }

        if (stage >= 20) {
            if (allInInventory(player, *items)) {
                line(player, "Now I have !!all 3 keys?? I should go and speak to !!Sir Prysin??", line++, stage>=30)
                line(player, "and collect the magical sword !!Silverlight?? from him.", line++, stage>=30)
            } else {
                val names = arrayOf("1st Key", "2nd Key", "3rd Key")
                val message = arrayOf(
                    "was dropped down the palace kitchen drains.",
                    "is with Captain Rovin in Varrock Palace.",
                    "is with the Wizard Traiborn at the Wizards' Tower."
                )

                for (i in items.indices) {
                    val msg = if (player.hasItem(Item(items[i]))) {
                        "I have the ${names[i]} with me."
                    } else {
                        "The !!${names[i]}?? ${message[i]}"
                    }
                    line(player, msg, line++, stage>=30)
                }
                if (player.getAttribute("demon-slayer:traiborn", false)) {
                    line(player, "The !!3rd Key?? is with Wizard Traiborn at the Wizards' Tower.", line++, stage>=30)
                    line(player, "!!Traiborn?? needs !!25?? more !!bones??.", line++, stage>=30)
                }
            }
        }

        if (stage >= 30) {
            line++
            line(player, "I reclaimed the magical sword Silverlight from Sir Prysin.", line++, stage>=100)
            line(player, "Now I should go to the stone circle south of the city and", line++, stage>=100)
            line(player, "destroy !!Delrith?? using !!Silverlight??!", line++, stage>=100)
            line++
        }

        if (stage == 100) {
            line(player, "Using its power I managed to destroy the demon Delrith", line++, true)
            line(player, "like the great hero Wally did many years before.", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!", line)
            limitScrolling(player, line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var line = 10
        drawReward(player, "3 Quests Points", line++)
        drawReward(player, "Silverlight", line)
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, DemonSlayerUtils.SILVERLIGHT.id, 230)
        removeAttributes(player, "demon-slayer:traiborn", "demon-slayer:incantation", "demon-slayer:poured", "demon-slayer:received")
        updateQuestTab(player)
    }

    override fun newInstance(`object`: Any?): Quest {
        ClassScanner.definePlugins(
            DemonSlayerPlugin(),
            DemonSlayerDrainPlugin(),
            DemonSlayerCutscenePlugin(),
            WallyCutscenePlugin(),
            GypsyArisDialogue(),
            SirPyrsinDialogue(),
            TraibornDialogue(),
            CaptainRovinDialogue(),
        )
        return this
    }
}
