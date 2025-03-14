package content.region.kandarin.quest.phoenix

import content.data.GameAttributes
import content.region.kandarin.quest.phoenix.handlers.allTwigs
import core.api.*
import core.api.item.allInInventory
import core.api.quest.isQuestComplete
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class InPyreNeed : Quest(Quests.IN_PYRE_NEED, 162, 161, 1, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 0, 1, 30) {
    // https://runescape.wiki/w/Phoenix_eggling
    // https://runescape.wiki/w/Cracked_phoenix_egg
    // https://runescape.wiki/w/Phoenix_eggling?oldid=1105267

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12
        val inPyreNeedProgress = getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761)

        if (stage == 0) {
            line(player, "I can start this quest by talking to the !!Priest of Guthix??,", line++)
            line(player, "near the cave south of the !!Piscatoris Fishing Colony??.", line++)
            line++
            line(player, "This quest has the following requirements:", line++)
            line(
                player,
                "Level 53 Fletching",
                line++,
                getStatLevel(player, Skills.FLETCHING) >= 53 || inPyreNeedProgress > 1,
            )
            line(
                player,
                "Level 52 Crafting",
                line++,
                getStatLevel(player, Skills.CRAFTING) >= 52 || inPyreNeedProgress > 1,
            )
            line(
                player,
                "Level 55 Firemaking",
                line++,
                getStatLevel(player, Skills.FIREMAKING) >= 55 || inPyreNeedProgress > 1,
            )
            line++
        }

        if (stage >= 1) {
            line++
            line(player, "I spoke to the Priest of Guthix, near the cave south of", line++, inPyreNeedProgress >= 2)
            line(player, "the Piscatoris Fishing Colony.", line++, inPyreNeedProgress >= 2)
            line(player, "He wants me to enter a cave that is apparently the lair", line++, inPyreNeedProgress >= 2)
            line(player, "of the legendary phoenix, which was attacked and mortally", line++, inPyreNeedProgress >= 2)
            line(
                player,
                "wounded on its way back to its roost. I must complete a ritual, which",
                line++,
                inPyreNeedProgress >= 2,
            )
            line(player, "he believes will ensure the phoenix's survival.", line++, inPyreNeedProgress >= 2)
            line++
        }

        if (inPyreNeedProgress > 1) {
            line(
                player,
                "I could enter the cave and try to figure it out for myself, but",
                line++,
                getAttribute(player, GameAttributes.TALK_WITH_PRIEST, 0) >= 3 || stage > 99 || inPyreNeedProgress > 5,
            )
            line(
                player,
                "I should ask the Priest of Guthix for more information.",
                line++,
                getAttribute(player, GameAttributes.TALK_WITH_PRIEST, 0) >= 3 || stage > 99 || inPyreNeedProgress > 5,
            )
            line++
        }

        if (inPyreNeedProgress >= 4) {
            line(
                player,
                "I have gathered the !!cinnamon twigs??.",
                line++,
                inInventory(player, Items.CINNAMON_TWIGS_14606) || stage > 99 || inPyreNeedProgress > 5,
            )
            line(
                player,
                "I have fletched the !!cinnamon twigs?? into !!weaving ribbon??.",
                line++,
                inInventory(player, Items.CINNAMON_WEAVING_RIBBON_14611) || stage > 99 || inPyreNeedProgress > 5,
            )
            line++
            line(
                player,
                "I have gathered the !!sassafras twigs??.",
                line++,
                inInventory(player, Items.SASSAFRAS_TWIGS_14607) || stage > 99 || inPyreNeedProgress > 5,
            )
            line(
                player,
                "I have fletched the !!sassafras twigs?? into !!weaving ribbon??.",
                line++,
                inInventory(player, Items.SASSAFRAS_WEAVING_RIBBON_14612) || stage > 99 || inPyreNeedProgress > 5,
            )
            line++
            line(
                player,
                "I have gathered the !!ailanthus twigs??.",
                line++,
                inInventory(player, Items.AILANTHUS_TWIGS_14608) || stage > 99 || inPyreNeedProgress > 5,
            )
            line(
                player,
                "I have fletched the !!ailanthus twigs into !!weaving ribbon??.",
                line++,
                inInventory(player, Items.AILANTHUS_WEAVING_RIBBON_14613) || stage > 99 || inPyreNeedProgress > 5,
            )
            line++
            line(
                player,
                "I have gathered the !!cedar twigs??.",
                line++,
                inInventory(player, Items.CEDAR_TWIGS_14609) || stage > 99 || inPyreNeedProgress > 5,
            )
            line(
                player,
                "I have fletched the !!cedar twigs into !!weaving ribbon??.",
                line++,
                inInventory(player, Items.CEDAR_WEAVING_RIBBON_14614) || stage > 99 || inPyreNeedProgress > 5,
            )
            line++
            line(
                player,
                "I have gathered the !!mastic twigs??.",
                line++,
                inInventory(player, Items.MASTIC_TWIGS_14610) || stage > 99 || inPyreNeedProgress > 5,
            )
            line(
                player,
                "I have fletched the !!mastic twigs?? into !!weaving ribbon??.",
                line++,
                inInventory(player, Items.MASTIC_WEAVING_RIBBON_14615) || stage > 99 || inPyreNeedProgress > 5,
            )
            line++
        }

        if (inPyreNeedProgress > 5) {
            line(
                player,
                "I have gathered all the twigs, and made my way to the phoenix's roost.",
                line++,
                allInInventory(player, *allTwigs) || stage > 99 || inPyreNeedProgress > 6,
            )
            line(
                player,
                "The phoenix is here. It is gravely wounded. I should look around for",
                line++,
                allInInventory(player, *allTwigs) || stage > 99 || inPyreNeedProgress > 6,
            )
            line(
                player,
                "clues as to what to do next.",
                line++,
                allInInventory(player, *allTwigs) || stage > 99 || inPyreNeedProgress > 6,
            )
            line++
        }

        if (inPyreNeedProgress >= 6) {
            line(
                player,
                "I should hurry and fletch all the twigs into weaving ribbon,",
                line++,
                inPyreNeedProgress >= 7,
            )
            line(player, "construct the funeral pyre and light it.", line++, inPyreNeedProgress >= 7)
            line++
        }

        if (inPyreNeedProgress >= 7) {
            line(player, "I have constructed and lit the funeral pyre. The phoenix", line++, inPyreNeedProgress >= 10)
            line(player, "managed to limp over and throw itself into the fire.", line++, inPyreNeedProgress >= 10)
            line++
            line(player, "It burned to ashes, and in a flash of flame was reborn.", line++, inPyreNeedProgress >= 10)
            line++
            line(
                player,
                "I should speak to the phoenix and ensure all is okay before",
                line++,
                inPyreNeedProgress >= 10,
            )
            line(player, "returning to the Priest of Guthix.", line++, inPyreNeedProgress >= 10)
            line++
        }

        if (inPyreNeedProgress >= 10) {
            line(
                player,
                "The reborn phoenix spoke to me, thanking me for helping it its",
                line++,
                inPyreNeedProgress >= 10,
            )
            line(player, "time of need.", line++, inPyreNeedProgress >= 10)
            line(
                player,
                "As a reward, the phoenix gave me 5 phoenix quills, and also gave me",
                line++,
                inPyreNeedProgress >= 10,
            )
            line(
                player,
                "permission to challenge it to combat in its lair, once per day",
                line++,
                inPyreNeedProgress >= 10,
            )
            line(player, "once my combat skills are proven.", line++, inPyreNeedProgress >= 10)
            line++
        }

        if (inPyreNeedProgress >= 10) {
            line(
                player,
                "I should meet the Priest of Guthix at the entrance to the phoenix's lair",
                line++,
                isQuestComplete(player, Quests.IN_PYRE_NEED) || inPyreNeedProgress >= 10,
            )
            line(
                player,
                "to claim my reward from him, too.",
                line++,
                isQuestComplete(player, Quests.IN_PYRE_NEED) || inPyreNeedProgress >= 10,
            )
            line++
            line(
                player,
                "I have spoken to the Priest of Guthix, who",
                line++,
                isQuestComplete(player, Quests.IN_PYRE_NEED) || inPyreNeedProgress >= 10,
            )
            line(
                player,
                "thanked and rewarded me for my help.",
                line++,
                isQuestComplete(player, Quests.IN_PYRE_NEED) || inPyreNeedProgress >= 10,
            )
        }

        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10

        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.PHOENIX_QUILL_14616, 240)
        sendString(player, "You have completed In Pyre Need!", Components.QUEST_COMPLETE_SCROLL_277, 4)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "14,400 Firemaking xp", ln++)
        drawReward(player, "12,500 Fletching xp", ln++)
        drawReward(player, "11,556 Crafting xp", ln++)
        drawReward(player, "Access to the pheonix lair", ln++)
        drawReward(player, "once per day", ln)

        rewardXP(player, Skills.FIREMAKING, 14400.0)
        rewardXP(player, Skills.FLETCHING, 12500.0)
        rewardXP(player, Skills.CRAFTING, 11556.0)
        addItemOrDrop(player, Items.PHOENIX_QUILL_14616, 5)
        setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 30, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
/*
 * 1 quest point
 * 14,400 Firemaking experience
 * 12,500 Fletching experience
 * 11,556 Crafting experience
 * 5 phoenix quills worth 126,065 coins.
 * Access to the Phoenix Lair once per day; which is a Distraction and Diversion.
 * The chance to receive a Phoenix eggling pet while doing the Phoenix Lair.
 * Raising the pet requires level 72 Summoning. Players without level 72 Summoning
 * will receive a phoenix egg instead, which they can later hatch at the required level.
 * 2 Treasure Hunter keys (Ironman accounts will not receive these)
 */
