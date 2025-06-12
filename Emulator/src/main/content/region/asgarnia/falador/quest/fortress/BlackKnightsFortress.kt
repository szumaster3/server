package content.region.asgarnia.falador.quest.fortress

import content.data.GameAttributes
import content.region.asgarnia.quest.fortress.dialogue.SirAmikVarzeDialogue
import content.region.asgarnia.quest.fortress.handlers.BlackKnightsFortressPlugin
import core.api.getAttribute
import core.api.removeAttribute
import core.api.sendItemZoomOnInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.plugin.ClassScanner.definePlugins
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class BlackKnightsFortress :
    Quest(Quests.BLACK_KNIGHTS_FORTRESS, 14, 13, 3, Vars.VARP_QUEST_BLACK_KNIGHTS_FORTRESS_PROGRESS_130, 0, 1, 4) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12

        if (stage == 0) {
            line(player, "I can start this quest by speaking to the !!Sir Amik Varze?? at the", line++)
            line(player, "!!White Knight's Castle?? in !!Falador??.", line++)
            if (player.questRepository.points > 12) {
                line(player, "---I have a total of at least 13 Quest Points/--", line++)
            } else {
                line(player, "!!I must have a total of at least 13 Quest Points??", line++)
            }
            line(player, "I would have an advantage if I could fight !!Level 33 Knights??", line++)
            line(player, "and if I had a smithing level of !!26??.", line++)
            limitScrolling(player, line, true)
        }

        if (stage >= 10) {
            line(
                player,
                "!!Sir Amik Varze?? has asked me to investigate the !!Black??",
                line++,
                getAttribute(player, GameAttributes.QUEST_BKF_DOSSIER_INTER, false),
            )
            line(
                player,
                "!!Knights' Fortress?? which is located on !!Ice Mountain??.",
                line++,
                getAttribute(player, GameAttributes.QUEST_BKF_DOSSIER_INTER, false),
            )
            line(
                player,
                "I need to disguise myself to gain entry to the !!Black??",
                line++,
                getAttribute(player, GameAttributes.QUEST_BKF_DOSSIER_INTER, false),
            )
            line(
                player,
                "!!Knights' Fortress??.",
                line++,
                getAttribute(player, GameAttributes.QUEST_BKF_DOSSIER_INTER, false),
            )
        }

        if (stage >= 20) {
            line(player, "Sir Amik Varze asked me to investigate the Black Knights'", line++, true)
            line(player, "Fortress. I sneaked inside disguised as a Guard.", line++, true)
            line(player, "I eavesdropped on a Witch and the Black Knight Captain", line++, true)
            line(player, "and discovered that their invincibility potion can be", line++, true)
            line(player, "destroyed with a normal !!cabbage??.", line++, true)
        }

        if (stage >= 30) {
            line(player, "Sir Amik Varze asked me to investigate the Black Knights'", line++, true)
            line(player, "Fortress. I sneaked inside disguised as a Guard.", line++, true)
            line(player, "I eavesdropped on a Witch and the Black Knight Captain", line++, true)
            line(player, "and discovered that their invincibility potion could be", line++, true)
            line(player, "destroyed with a normal cabbage.", line++, true)
            line(player, "Now that I have sabotaged the witch's potion, I can claim", line++, stage >= 100)
            line(player, "my !!reward?? from !!Sir Amik Varze?? in !!Falador Castle??.", line++, stage >= 100)
        }

        if (stage >= 100) {
            line(player, "Sir Amik Varze asked me to investigate the Black Knights'", line++, true)
            line(player, "Fortress. I sneaked inside disguised as a Guard.", line++, true)
            line(player, "I eavesdropped on a Witch and the Black Knight Captain", line++, true)
            line(player, "and discovered that their invincibility potion could be", line++, true)
            line(player, "destroyed with a normal cabbage.", line++, true)
            line(player, "I found a cabbage, and used it to a destroy the potion, then", line++, true)
            line(player, "claimed my reward for a job well done.", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++)
            line++
            line(player, "!!Reward:??", line++, false)
            line(player, "3 Quest Points", line++, false)
            line(player, "2500gp", line, false)
            limitScrolling(player, line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var line = 10
        player.packetDispatch.sendString(
            "You have completed the Black Knights' Fortress!",
            Components.QUEST_COMPLETE_SCROLL_277,
            4,
        )
        drawReward(player, "3 Quests Points", line++)
        drawReward(player, "2500 Coins", line)
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.BROKEN_CAULDRON_9591, 240)
        removeAttribute(player, GameAttributes.QUEST_BKF_DOSSIER_INTER)
    }

    override fun newInstance(`object`: Any?): Quest {
        definePlugins(BlackKnightsFortressPlugin(), SirAmikVarzeDialogue())
        return this
    }
}
