package content.region.morytania.quest.hauntedmine

import core.api.addItemOrDrop
import core.api.getStatLevel
import core.api.quest.isQuestComplete
import core.api.rewardXP
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

class HauntedMine : Quest(Quests.HAUNTED_MINE, 73, 72, 2, Vars.VARP_QUEST_HAUNTED_MINE_PROGRESS_382, 0, 1, 11) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        if (stage == 0) {
            line(player, "I can start this quest by speaking to the !!Saradominist??", line++)
            line(player, "!!Zealot??' outside the !!mines of Morytania??.", line++)
            line++
            line(player, "To complete this quest I need:", line++)
            line(
                player,
                if (getStatLevel(player, Skills.AGILITY) >=
                    15
                ) {
                    "---Level 15 Agility./--"
                } else {
                    "!!Level 15 Agility.??"
                },
                line++,
            )
            line(
                player,
                if (getStatLevel(player, Skills.CRAFTING) >=
                    35
                ) {
                    "---35 Crafting./--"
                } else {
                    "!!Level 35 Crafting.??"
                },
                line++,
            )
            line(
                player,
                if (isQuestComplete(player, Quests.PRIEST_IN_PERIL) &&
                    isQuestComplete(player, Quests.NATURE_SPIRIT)
                ) {
                    "---Access to Morytania./--"
                } else {
                    "!!Access to Morytania.??"
                },
                line++,
            )
            line(player, "I must be able to !!defeat a level !!95 enemy??.", line++)
        }

        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!", line)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10

        player.packetDispatch.sendItemZoomOnInterface(Items.SALVE_AMULET_4081, 230, 277, 5)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "22,000 Strength XP", ln++)
        drawReward(player, "Acess to Tarn's lair", ln++)
        rewardXP(player, Skills.STRENGTH, 22000.0)
        addItemOrDrop(player, Items.CRYSTAL_MINE_KEY_4077)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
