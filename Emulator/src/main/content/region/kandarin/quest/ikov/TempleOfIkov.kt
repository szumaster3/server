package content.region.kandarin.quest.ikov

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestStage
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class TempleOfIkov : Quest(Quests.TEMPLE_OF_IKOV, 121, 120, 1, Vars.VARP_QUEST_TEMPLE_OF_IKOV_PROGRESS_26, 0, 1, 80) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player, Quests.TEMPLE_OF_IKOV) > 0

        if (!started) {
            line(player, "I can start this quest at the !!Flying Horse Inn?? in !!Ardougne??", line++, false)
            line(player, "by speaking to !!Lucien??", line++, false)
            line++
            line(player, "To start this quest I will need:", line++, false)
            line(player, "Level 42 !!Thieving??", line++, hasLevelStat(player, Skills.THIEVING, 42))
            line(player, "Level 40 !!Ranged??", line++, hasLevelStat(player, Skills.RANGE, 40))
            line(player, "!!Ability to defeat a level 84 enemy with Ranged??.", line++, false)
            limitScrolling(player, line, true)
        } else {
            if (stage >= 2) {
                line(player, "Lucien has asked me to retrieve the !!Staff of Armadyl?? from", line++, true)
                line(player, "from the !!Temple of Ikov??. The entrance is near !!Hemenster??. He has", line++, true)
                line(player, "given me a !!pendant?? so I can enter the !!chamber of fear??.", line++, true)
            } else if (stage >= 1) {
                line(player, "Lucien has asked me to retrieve the !!Staff of Armadyl?? from", line++, false)
                line(player, "from the !!Temple of Ikov??. The entrance is near !!Hemenster??. He has", line++, false)
                line(player, "given me a !!pendant?? so I can enter the !!chamber of fear??.", line++, false)
            }

            if (stage == 2) {
                line(player, "I have entered the chamber of fear.", line++, true)
            }
            if (stage == 3) {
                line(player, "I have entered the chamber of fear. I found a trap on a", line++, true)
                line(player, "lever and have disabled it. I pulled the lever.", line++, true)
            }

            if (stage < 4) {
                if (getAttribute(player, GameAttributes.QUEST_IKOV_ICE_ARROWS, false)) {
                    line(player, "I have found some boots that make me lighter. I made it", line++, true)
                    line(player, "across the lava bridge and found a lever. I fit the lever", line++, true)
                    line(player, "into the bracket and pulled the lever. I found arrows", line++, true)
                    line(player, "made of ice in a chest.", line++, true)
                } else if (getAttribute(player, GameAttributes.QUEST_IKOV_ICE_CHAMBER_ACCESS, false)) {
                    line(player, "I have found some boots that make me lighter. I made it", line++, true)
                    line(player, "across the lava bridge and found a lever. I fit the lever", line++, true)
                    line(player, "into the bracket and pulled the lever.", line++, true)
                } else if (getAttribute(player, GameAttributes.QUEST_IKOV_BRIDGE_INTER, false)) {
                    line(player, "I have found some boots that make me lighter. I made it", line++, true)
                    line(player, "across the lava bridge and found a lever.", line++, true)
                }
            }

            if (stage in 2..3) {
                line++
                line(player, "I need to find the entrance to the !!Temple of Ikov??", line++, false)
            }

            if (stage == 4) {
                line(player, "I have entered the chamber of fear. I found a trap on a", line++, true)
                line(player, "lever and have disabled it. I pulled the lever. I went into", line++, true)
                line(player, "another chamber and was attacked by a Fire Warrior! I", line++, true)
                line(player, "killed it using arrows made of ice and my trusty bow.", line++, true)
            }
            if (stage == 4 && getAttribute(player, GameAttributes.QUEST_IKOV_WINELDA_INTER, false)) {
                line++

                line(player, "My path is blocked by lava. !!Winelda?? will teleport me across", line++, false)
                line(player, "if I get her !!twenty limpwurt roots??.", line++, false)
            }
            if (stage == 5) {
                line(player, "I have entered the chamber of fear. I found a trap on a", line++, true)
                line(player, "lever and have disabled it. I pulled the lever. I went into", line++, true)
                line(player, "another chamber and was attacked by a Fire Warrior! I", line++, true)
                line(player, "killed it using arrows made of ice and my trusty bow.", line++, true)
            }

            if (stage == 6 && getAttribute(player, GameAttributes.QUEST_IKOV_SELECTED_END, 0) == 1) {
                line(player, "I have entered the chamber of fear. I found a trap on a", line++, true)
                line(player, "lever and have disabled it. I pulled the lever. I went into", line++, true)
                line(player, "another chamber and was attacked by a Fire Warrior! I", line++, true)
                line(player, "killed it using arrows made of ice and my trusty bow.", line++, true)
                line++
                line(player, "I agreed to help the !!Guardians of Armadyl??, I will kill !!Lucien??.", line++, false)
                line(player, "The guardians gave me a !!pendant?? that I will need to enable", line++, false)
                line(player, "me to attack him.", line++, false)
            }

            if (stage == 6 && getAttribute(player, GameAttributes.QUEST_IKOV_SELECTED_END, 0) == 2) {
                line(player, "I have entered the chamber of fear. I found a trap on a", line++, true)
                line(player, "lever and have disabled it. I pulled the lever. I went into", line++, true)
                line(player, "another chamber and was attacked by a Fire Warrior! I", line++, true)
                line(player, "killed it using arrows made of ice and my trusty bow.", line++, true)
                line++

                line(player, "I recovered the !!Staff of Armadyl?? from the !!Temple of Ikov??.", line++, false)
                line(player, "!!Lucien?? is staying at his house west of the !!Grand Exchange??", line++, false)
                line(player, "in !!Varrock??.", line++, false)
            }

            if (stage >= 100) {
                if (getAttribute(player, GameAttributes.QUEST_IKOV_SELECTED_END, 0) == 1) {
                    line++
                    line(player, "I agreed to help the Guardians of Armadyl, I killed Lucien", line++, true)
                    line(player, "and banished him from this plane!", line++, true)
                }

                if (getAttribute(player, GameAttributes.QUEST_IKOV_SELECTED_END, 0) == 2) {
                    line++
                    line(player, "I recovered the Staff of Armadyl from the Temple of Ikov.", line++, true)
                    line(player, "Lucien was staying at his house west of the Grand Exchange", line++, true)
                    line(player, "in Varrock. He said that the staff had made him more", line++, true)
                    line(player, "powerful!", line++, true)
                }

                line++
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
                limitScrolling(player, line, false)
            }
        }
    }

    override fun reset(player: Player) {
        removeAttribute(player, GameAttributes.QUEST_IKOV_SELECTED_END)
        removeAttribute(player, GameAttributes.QUEST_IKOV_DISABLED_TRAP)
        removeAttribute(player, GameAttributes.QUEST_IKOV_WINELDA_INTER)
        removeAttribute(player, GameAttributes.QUEST_IKOV_BRIDGE_INTER)
        removeAttribute(player, GameAttributes.QUEST_IKOV_ICE_ARROWS)
        removeAttribute(player, GameAttributes.QUEST_IKOV_ICE_CHAMBER_ACCESS)
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        when (getAttribute(player, GameAttributes.QUEST_IKOV_SELECTED_END, 0)) {
            1 ->
                sendString(
                    player,
                    "${Quests.TEMPLE_OF_IKOV} Quest completed for Armadyl!",
                    Components.QUEST_COMPLETE_SCROLL_277,
                    4,
                )
            2 ->
                sendString(
                    player,
                    "${Quests.TEMPLE_OF_IKOV} Quest completed for Lucien!",
                    Components.QUEST_COMPLETE_SCROLL_277,
                    4,
                )
            else ->
                sendString(
                    player,
                    "${Quests.TEMPLE_OF_IKOV} Quest completed!!",
                    Components.QUEST_COMPLETE_SCROLL_277,
                    4,
                )
        }
        player.packetDispatch.sendItemZoomOnInterface(
            Items.YEW_LONGBOW_855,
            230,
            Components.QUEST_COMPLETE_SCROLL_277,
            5,
        )

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "10,500 Ranged XP", ln++)
        drawReward(player, "8,000 Fletching XP", ln++)

        rewardXP(player, Skills.RANGE, 10500.0)
        rewardXP(player, Skills.FLETCHING, 8000.0)
        setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_28_3650, 1, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
