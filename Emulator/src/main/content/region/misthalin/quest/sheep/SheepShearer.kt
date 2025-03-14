package content.region.misthalin.quest.sheep

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars
import kotlin.math.min

@Initializable
class SheepShearer : Quest(Quests.SHEEP_SHEARER, 28, 27, 1, Vars.VARP_QUEST_SHEEP_SHEARER_PROGRESS_179, 0, 20, 21) {
    companion object {
        val ATTR_NUM_BALLS_OF_WOOL_DELIVERED = "/save:sheep-shearer:num-balls-of-wool-delivered"
        val ATTR_IS_PENGUIN_SHEEP_SHEARED = "/save:sheep-shearer:is-penguin-sheep-sheared"

        fun getBallsOfWoolDelivered(player: Player): Int {
            return getAttribute(player, ATTR_NUM_BALLS_OF_WOOL_DELIVERED, 0)
        }

        fun getBallsOfWoolRequired(player: Player): Int {
            return 20 - getBallsOfWoolDelivered(player)
        }

        fun getBallsOfWoolToRemove(player: Player): Int {
            return min(getBallsOfWoolRequired(player), amountInInventory(player, Items.BALL_OF_WOOL_1759))
        }

        fun getBallsOfWoolToCollect(player: Player): Int {
            return getBallsOfWoolRequired(player) - getBallsOfWoolToRemove(player)
        }

        fun deliverBallsOfWool(player: Player): Int {
            val removeAmount = getBallsOfWoolToRemove(player)
            if (removeItem(player, Item(Items.BALL_OF_WOOL_1759, removeAmount))) {
                setAttribute(player, ATTR_NUM_BALLS_OF_WOOL_DELIVERED, getBallsOfWoolDelivered(player) + removeAmount)
                return removeAmount
            }
            return 0
        }
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)

        var ln = 11

        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Farmer Fred?? at his", ln++)
            line(player, "!!farm?? just a little way !!North West of Lumbridge??", ln)
        } else if (stage == 10 || stage == 90) {
            line(player, "<col=000000>I asked Farmer Fred, near Lumbridge, for a quest. Fred</col>", ln++, true)
            line(player, "<col=000000>said he'd pay me for shearing his sheep for him!</col>", ln++, true)
            ln++

            val ballsOfWoolToCollect = getBallsOfWoolToCollect(player)
            if (ballsOfWoolToCollect == 0) {
                line(player, "I have enough !!balls of wool?? to give !!Fred?? and get my !!reward??", ln++)
                line(player, "!!money!??", ln)
            } else {
                line(player, "I need to collect $ballsOfWoolToCollect more !!balls of wool.??", ln++)
            }
        } else if (stage == 100) {
            line(player, "<col=000000>I brought Farmer Fred 20 balls of wool, and he paid me for</col>", ln++, true)
            line(player, "<col=000000>it!</col>", ln++, true)
            ln++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", ln)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.SHEARS_1735, 240)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "150 Crafting XP", ln++)
        drawReward(player, "60 coins", ln)
        rewardXP(player, Skills.CRAFTING, 150.0)
        addItemOrDrop(player, Items.COINS_995, 60)
        removeAttributes(player, ATTR_NUM_BALLS_OF_WOOL_DELIVERED, ATTR_IS_PENGUIN_SHEEP_SHEARED)
    }
}
