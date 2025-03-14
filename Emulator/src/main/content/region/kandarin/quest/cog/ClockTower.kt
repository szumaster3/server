package content.region.kandarin.quest.cog

import core.api.addItemOrDrop
import core.api.getAttribute
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class ClockTower : Quest(Quests.CLOCK_TOWER, 38, 37, 1, Vars.VARP_QUEST_CLOCK_TOWER_PROGRESS_10, 0, 1, 8) {
    companion object {
        const val BLUE_COG_ATTR = "/save:quest:clocktower-bluecogplaced"
        const val BLACK_COG_ATTR = "/save:quest:clocktower-blackcogplaced"
        const val WHITE_COG_ATTR = "/save:quest:clocktower-whitecogplaced"
        const val RED_COG_ATTR = "/save:quest:clocktower-redcogplaced"
        const val BLACK_COG_COOLED_ATTR = "/save:quest:clocktower-blackcogcooled"
        const val RATS_POISONED_ATTR = "/save:quest:clocktower-poisonplaced"
        const val ASK_ABOUT_RATS_ATTR = "quest:clocktower-askkojoaboutrats"
    }

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)

        var line = 12
        var stage = getStage(player)

        when (stage) {
            0 -> {
                line(player, "I can start this quest by speaking to !!Brother Kojo?? at the", line++)
                line(player, "!!Clock Tower?? which is located !!South?? of !!Ardougne??", line)
            }

            in 1..10 -> {
                line(player, "I spoke to Brother Kojo at the Clock Tower South of", line++, true)
                line(player, "Ardougne and agreed to help him repair the clock.", line++, true)
                line(player, "To repair the clock I need to find the four coloured cogs", line++, false)
                line(player, "and place them on the four correctly coloured spindles.", line++, false)
                line(
                    player,
                    if (getAttribute(
                            player!!,
                            BLUE_COG_ATTR,
                            false,
                        )
                    ) {
                        "I placed the !!Blue Cog?? on it's !!spindle??."
                    } else {
                        "I haven't placed the !!Blue Cog?? on it's !!spindle?? yet."
                    },
                    line++,
                    getAttribute(player, BLUE_COG_ATTR, false),
                )
                line(
                    player,
                    if (getAttribute(
                            player,
                            BLACK_COG_ATTR,
                            false,
                        )
                    ) {
                        "I placed the !!Black Cog?? on it's !!spindle??."
                    } else {
                        "I haven't placed the !!Black Cog?? on it's !!spindle?? yet."
                    },
                    line++,
                    getAttribute(player, BLACK_COG_ATTR, false),
                )
                line(
                    player,
                    if (getAttribute(
                            player,
                            WHITE_COG_ATTR,
                            false,
                        )
                    ) {
                        "I placed the !!White Cog?? on it's !!spindle??."
                    } else {
                        "I haven't placed the !!White Cog?? on it's !!spindle?? yet."
                    },
                    line++,
                    getAttribute(player, WHITE_COG_ATTR, false),
                )
                line(
                    player,
                    if (getAttribute(
                            player,
                            RED_COG_ATTR,
                            false,
                        )
                    ) {
                        "I placed the !!Red Cog?? on it's !!spindle??."
                    } else {
                        "I haven't placed the !!Red Cog?? on it's !!spindle?? yet."
                    },
                    line++,
                    getAttribute(player, RED_COG_ATTR, false),
                )
            }

            100 -> {
                line(player, "I spoke to Brother Kojo at the Clock Tower South of", line++, true)
                line(player, "I have placed all four cogs successfully on the spindles.", line++, true)
                line(player, "Brother Kojo was grateful for all my help and rewarded me.", line++, true)
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
            }
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed the ${Quests.CLOCK_TOWER} Quest!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.COINS_995, 240, 277, 5)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "500 coins", ln)
        addItemOrDrop(player, Items.COINS_995, 500)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
