package content.region.misthalin.quest.demon

import content.region.misthalin.quest.demon.cutscene.DemonSlayerCutscenePlugin
import content.region.misthalin.quest.demon.cutscene.WallyCutscenePlugin
import content.region.misthalin.quest.demon.dialogue.CaptainRovinDialogue
import content.region.misthalin.quest.demon.dialogue.GypsyArisDialogue
import content.region.misthalin.quest.demon.dialogue.SirPyrsinDialogue
import content.region.misthalin.quest.demon.dialogue.TraibornDialogue
import content.region.misthalin.quest.demon.handlers.DemonSlayerDrainPlugin
import content.region.misthalin.quest.demon.handlers.DemonSlayerPlugin
import content.region.misthalin.quest.demon.handlers.DemonSlayerUtils
import core.api.inInventory
import core.api.quest.updateQuestTab
import core.api.removeAttributes
import core.api.sendItemZoomOnInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.plugin.ClassScanner
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class DemonSlayer : Quest(Quests.DEMON_SLAYER, 16, 15, 3, Vars.VARP_QUEST_DEMON_SLAYER_PROGRESS_222, 0, 1, 3) {
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

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12
        if (stage >= 0) {
            line(player, "I can start this quest by speaking to the !!Gypsy?? in the !!tent??", line++)
            line(player, "in !!Varrock's?? main square", line++)
            line(player, "I must be able to defeat a level 27 !!apocalyptic demon!", line++)
        }

        if (stage >= 10) {
            line(player, "I spoke to the Gypsy in Varrock Square who saw my future.", line++, stage >= 20)
            line(player, "Unfortunately it involved killing a demon who nearly", line++, stage >= 20)
            line(player, "destroyed Varrock over 150 years ago.", line++, stage >= 20)
            line(player, "To defeat the !!demon?? I need the magical sword !!Silverlight??.", line++, stage >= 20)
            line(player, "I should ask !!Sir Prysin?? in !!Varrock Palace?? where it is.", line++, stage >= 20)
        }

        if (stage >= 20) {
            line(player, "I spoke to the Gypsy in Varrock Square who saw my future.", line++, stage >= 30)
            line(player, "Unfortunately it involved killing a demon who nearly", line++, stage >= 30)
            line(player, "destroyed Varrock over 150 years ago.", line++, stage >= 30)
            line(player, "To defeat the !!demon?? I need the magical sword !!Silverlight??.", line++, stage >= 30)
            line(player, "!!Sir Prysin?? needs !!3 keys?? before he can give me !!Silverlight??.", line++, stage >= 30)
            if (inInventory(player, DemonSlayerUtils.FIRST_KEY.id) && inInventory(
                    player, DemonSlayerUtils.SECOND_KEY.id
                ) && inInventory(player, DemonSlayerUtils.THIRD_KEY.id)
            ) {
                line(player, "Now I have !!all 3 keys?? I should go and speak to !!Sir Prysin??", line++, stage >= 30)
                line(player, "and collect the magical sword !!Silverlight?? from him.", line++, stage >= 30)
            } else {
                line(
                    player,
                    if (player.hasItem(
                            DemonSlayerUtils.FIRST_KEY,
                        )
                    ) {
                        "I have the 1st Key with me."
                    } else {
                        "The !!1st Key?? was dropped down the palace kitchen drains."
                    },
                    line++,
                    stage >= 30,
                )
                line(
                    player,
                    if (player.hasItem(
                            DemonSlayerUtils.SECOND_KEY,
                        )
                    ) {
                        "I have the 2nd Key with me."
                    } else {
                        "The !!2nd Key?? is with Captain Rovin in Varrock Palace."
                    },
                    line++,
                    stage >= 30,
                )
                line(
                    player,
                    if (player.hasItem(
                            DemonSlayerUtils.THIRD_KEY,
                        )
                    ) {
                        "I Have the 3rd key with me."
                    } else {
                        "The !!3rd Key?? is with the Wizard Traiborn at the Wizards' Tower."
                    },
                    line++,
                    stage >= 30,
                )
                if (player.getAttribute("demon-slayer:traiborn", false)) {
                    line(player, "The !!3rd Key?? is with Wizard Traiborn at the Wizards' Tower.", line++, stage >= 30)
                    line(player, "!!Traiborn?? needs !!25?? more !!bones??.", line++, stage >= 30)
                }
            }
        }

        if (stage >= 30) {
            line(player, "I spoke to the Gypsy in Varrock Square who saw my future.", line++, stage >= 100)
            line(player, "Unfortunately it involved killing a demon who nearly", line++, stage >= 100)
            line(player, "destroyed Varrock over 150 years ago.", line++, stage >= 100)
            line(player, "I reclaimed the magical sword Silverlight from Sir Prysin.", line++, stage >= 100)
            line(player, "Now I should go to the stone circle south of the city and", line++, stage >= 100)
            line(player, "destroy !!Delrith?? using !!Silverlight??!", line++, stage >= 100)
        }

        if (stage >= 100) {
            line(player, "I spoke to the Gypsy in Varrock Square who saw my future.", line++, true)
            line(player, "Unfortunately it involved killing a demon who nearly", line++, true)
            line(player, "destroyed Varrock over 150 years ago.", line++, true)
            line(player, "I reclaimed the magical sword Silverlight from Sir Prysin.", line++, true)
            line(player, "Using its power I managed to destroy the demon Delrith", line++, true)
            line(player, "like the great hero Wally did many years before.", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!", line)
        }
    }


    override fun finish(player: Player) {
        super.finish(player)
        var line = 10
        drawReward(player, "3 Quests Points", line++)
        drawReward(player, "Silverlight", line)
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, DemonSlayerUtils.SILVERLIGHT.id, 230)
        removeAttributes(
            player,
            "demon-slayer:traiborn",
            "demon-slayer:incantation",
            "demon-slayer:poured",
            "demon-slayer:received",
        )
        updateQuestTab(player)
    }
}
