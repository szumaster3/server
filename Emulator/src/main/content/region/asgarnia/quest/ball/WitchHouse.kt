package content.region.asgarnia.quest.ball

import core.api.rewardXP
import core.api.sendItemZoomOnInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class WitchHouse : Quest(Quests.WITCHS_HOUSE, 124, 123, 4, Vars.VARP_QUEST_WTICHS_HOUSE_PROGRESS_226, 0, 1, 7) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        if (stage == 0) {
            line(player, "I can start this quest by speaking to the !!little boy??", line++)
            line(player, "standing by the long garden just !!north of Taverly??.", line++)
            line(player, "I must be able to defeat a !!level 53 enemy??", line++)
        } else {
            line(player, "A small boy kicked his ball over the fence into the nearby", line++, true)
            line(player, "garden, and I have agreed to retrieve it for him.", line++, true)
            if (stage == 10) {
                line(player, "I should find a way into the !!garden?? where the !!ball?? is.", line++)
            }
            if (stage >= 100) {
                line(player, "After puzzling through the strangely elaborate security", line++, true)
                line(player, "system, and defeating a very strange monster, I returned", line++, true)
                line(player, "the child's ball to him, and he thanked me for my help.", line++, true)
                line++;
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
            }
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var line = 10
        drawReward(player, "4 Quest Points", line++)
        drawReward(player, "6325 Hitpoints XP", line)
        rewardXP(player, Skills.HITPOINTS, 6325.0)
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.BALL_2407, 240)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
