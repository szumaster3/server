package content.region.asgarnia.quest.troll

import core.api.addItemOrDrop
import core.api.hasAnItem
import core.api.hasLevelStat
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
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
class TrollStronghold :
    Quest(Quests.TROLL_STRONGHOLD, 128, 127, 1, Vars.VARP_QUEST_TROLL_SRONGHOLD_PROGRESS_317, 0, 1, 50) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)
        var started = getQuestStage(player, Quests.TROLL_STRONGHOLD) > 0

        val hasBoots = hasAnItem(player, Items.CLIMBING_BOOTS_3105).container != null

        if (!started) {
            line(player, "I can start this quest by speaking to !!Denulth?? in his tent at", line++)
            line(player, "the !!Imperial Guard camp?? in !!Burthorpe?? after completing the", line++)
            line(player, "!!Death Plateau Quest??", line++, isQuestComplete(player, Quests.DEATH_PLATEAU))
            line++
            line(player, "To complete this quest I need:", line++)
            line(player, "Level 15 Agility.", line++, hasLevelStat(player, Skills.AGILITY, 15))
            line(
                player,
                "I also need to be able to defeat a !!level 113 Troll??.",
                line++,
                player.properties.combatLevel >= 100,
            )
            line(player, "Level 30 Thieving might be useful.", line++, hasLevelStat(player, Skills.THIEVING, 30))
            if (isQuestComplete(player, Quests.DEATH_PLATEAU) &&
                hasLevelStat(player, Skills.AGILITY, 15) &&
                hasLevelStat(player, Skills.THIEVING, 30)
            ) {
                line(player, "I have all the requirements to start this quest.", line)
            }
        } else {
            line(
                player,
                "I promised !!Denulth?? that I would rescue !!Godric?? from the !!Troll??",
                line++,
                stage == 100,
            )
            line(player, "!!Stronghold??", line++, stage == 100)
            line++
            if (stage >= 5 || hasBoots) {
                line(player, "I got some !!climbing boots?? from !!Tenzing??", line++, true)
            } else {
                line(player, "I have to get some !!climbing boots?? from !!Tenzing??", line++)
            }
            line++
            if (stage >= 5) {
                line(player, "I have defeated the !!Troll Champion??", line++, true)
            } else if (stage >= 3) {
                line(player, "I have accepted the !!Troll Champion's?? challenge.", line++)
            }
            if (stage in 5..7) {
                line++
                line(player, "I have to find a way to get into the !!Troll Stronghold??", line++)
            }
            line++
            if (stage >= 8) {
                line(player, "I found my way into the !!Prison??", line++, true)
            } else if (stage >= 5) {
                line(player, "I have to find a way to get into the !!prison??", line++)
            }
            line++
            if (stage >= 11) {
                line(player, "I've rescued !!Godric?? and !!Mad Eadgar??", line++, true)
            } else if (stage >= 8) {
                line(player, "I have to rescue !!Godric?? and !!Mad Eadgar??", line++)
            }
            if (stage >= 100) {
                line++
                line(player, "I talked to Dunstan and he rewarded me.", line++, true)
            } else if (stage >= 11) {
                line(player, "I should return and tell !!Dunstan?? his son is safe.", line++)
            }
            if (stage >= 100) {
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
            }
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.MYSTERIOUS_LAMP_13227, 240)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "2 Reward lamps giving 10,000", ln++)
        drawReward(player, "XP each", ln)

        addItemOrDrop(player, Items.MYSTERIOUS_LAMP_13227, 2)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
