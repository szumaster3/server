package content.region.kandarin.quest.swept

import content.data.GameAttributes
import core.api.addItemOrDrop
import core.api.getAttribute
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class SweptAway : Quest(Quests.SWEPT_AWAY, 160, 159, 2, Vars.VARBIT_QUEST_SWEPT_AWAY_PROGRESS_5448, 0, 1, 50) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        if (stage == 0) {
            line(player, "This quest is available to free players during !!Hallowee'en??,", line++)
            line(player, "after which, it will become members-only.", line++)
            line++
            line(player, "I can start this quest by talking to !!Maggie??, the travelling", line++)
            line(player, "witch who is currently parked north of !!Rimmington??.", line++)
            line++
        }
        if (stage >= 1) {
            line(player, "I have agreed to help Maggie prepare a batch of the 'good stuff'", line++, stage > 4)
            line(player, "that she is concocting in her pauldron.", line++, stage > 4)
            line(player, "Maggie gave me a broom which I've had enchanted by", line++, stage > 4)
            line(
                player,
                "!!Hetty?? in !!Rimmington??,",
                line++,
                getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_HETTY_ENCH, false) ||
                    getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LABELS_COMPLETE, false) ||
                    stage > 4,
            )
            line(player, "!!Betty?? in !!Port Sarim?? and", line++, stage > 4)
            line(player, "!!Aggie?? in !!Draynor??.", line++, stage > 4)
            line++
        }

        if (stage >= 3) {
            line(player, "I've stirred Maggie's cauldron with the enchanted broom.", line++, stage > 5)
            line++
        }
        if (stage >= 4) {
            line(player, "I spoke to Maggie, who thanked me for helping her", line++, stage > 99)
            line(player, "prepare the goulash.", line++, stage > 99)
            line(player, "She has offered me 10 bowls of goulash in return for my help.", line++, stage > 99)
            line++
        }

        if (stage == 100) {
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++, false)
            line++
            line(player, "I can get further rewards if I take my broom to:", line++, false)
            line(player, "The !!Sorceress's apprentice?? in !!Al Kharid??", line++, false)
            line(player, "!!Ali the Hag?? in !!Pollnivneach??", line++, false)
            line(player, "The !!Old Crone?? near the !!Slayer Tower?? (with level 53 Magic)", line++, false)
            line(player, "!!Baba Yaga?? of !!Lunar Isle?? (with level 73 Magic)", line++, false)
            line(player, "!!Kardia?? in the !!Underground Pass?? (with level 93 Magic)", line++, false)
            line++
            line(player, "I should talk to these !!witches?? for more information.", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.BROOMSTICK_14057, 230, 277, 5)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "A broom", ln++)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "Access to 10 portions of", ln++)
        drawReward(player, "experience-giving goulash", ln)
        addItemOrDrop(player, Items.GOULASH_14058, 1)
        setVarbit(player, Vars.VARBIT_QUEST_SWEPT_AWAY_PROGRESS_5448, 50, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
