package content.region.kandarin.quest.drunkmonk

import core.api.addItemOrDrop
import core.api.rewardXP
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class MonksFriend : Quest(Quests.MONKS_FRIEND, 89, 88, 1, Vars.VARP_QUEST_MONKS_FRIEND_PROGRESS_30, 0, 1, 80) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        player ?: return
        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Brother Omad?? in the", line++)
            line(player, "!!Monastery?? south of !!Ardougne??.", line++)
            line++
        }
        if (stage == 10) {
            line(player, "Brother Omad asked me to recover a child's blanket.", line++)
            line++
        }
        if (stage >= 20) {
            line(player, "Brother Omad asked me to recover a child's blanket, I ", line++, true)
            line(player, "found the secret cave and gave back the blanket.", line++, true)
            line++
        }
        if (stage == 30) {
            line(player, "I agreed to find !!Brother Cedric??. He is somewhere in the", line++)
            line(player, "!!forest?? south of !!Ardougne??", line++)
            line++
        }
        if (stage >= 50) {
            line(player, "I found Brother Cedric in the forest south of Ardougne.", line++, true)
            line(player, "I sobered him up and I helped him fix his cart.", line++, true)
            line++
        }
        if (stage == 100) {
            line(player, "I had a party with the Monks.", line++)
            line(player, "There were party balloons and we danced the night away!", line++)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.LAW_RUNE_563, 230, 277, 5)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "8 Law Runes", ln++)
        drawReward(player, "2000 Woodcutting XP", ln)
        rewardXP(player, Skills.WOODCUTTING, 2000.0)
        addItemOrDrop(player, 563, 8)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
