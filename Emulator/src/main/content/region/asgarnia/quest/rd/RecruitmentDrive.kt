package content.region.asgarnia.quest.rd

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests

@Initializable
class RecruitmentDrive : Quest(Quests.RECRUITMENT_DRIVE, 103, 102, 1, 496, 0, 1, 2) {
    companion object {
        const val stagePass = "rd:passedstage"
        const val stageFail = "rd:failedstage"

        const val stage = "/save:rd:currentstage"
        const val stage0 = "/save:rd:stage:0"
        const val stage1 = "/save:rd:stage:1"
        const val stage2 = "/save:rd:stage:2"
        const val stage3 = "/save:rd:stage:3"
        const val stage4 = "/save:rd:stage:4"

        const val makeoverTicket = "/save:rd:makeover-mage"

        val stageArray = arrayOf(stage0, stage1, stage2, stage3, stage4)
    }

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        var stage = getStage(player)

        var started = getQuestStage(player, Quests.RECRUITMENT_DRIVE) > 0

        if (!started) {
            line(player, "I can start this quest by speaking to !!Sir Amik Varze??,", line++)
            line(player, "upstairs in !!Falador Castle??.", line++)
            if (isQuestComplete(player, Quests.DRUIDIC_RITUAL)) {
                line(
                    player,
                    "with the ${Quests.DRUIDIC_RITUAL} Quest completed,",
                    line++,
                    isQuestComplete(player, Quests.DRUIDIC_RITUAL),
                )
            } else {
                line(player, "I have to completed the !!${Quests.DRUIDIC_RITUAL} Quest??,", line++)
            }
            if (isQuestComplete(player, Quests.BLACK_KNIGHTS_FORTRESS)) {
                line(
                    player,
                    "and since I have completed the ${Quests.BLACK_KNIGHTS_FORTRESS}.",
                    line++,
                    isQuestComplete(player, Quests.BLACK_KNIGHTS_FORTRESS),
                )
            } else {
                line(player, "and I have to completed the !!${Quests.BLACK_KNIGHTS_FORTRESS}??.", line++)
            }
            line++
        } else {
            line(player, "!!Sir Amik Varze?? told me that he had put my name forward as", line++, true)
            line(player, "a potential member of some mysterious organisation.", line++, true)
            line++
        }

        if (stage >= 3) {
            line(player, "I went to !!Falador Park??, and met a strange old man named !!Tiffy??.", line++, true)
            line++
            line(player, "He sent me to a secret training ground,", line++, true)
            line(player, "where my wits were thoroughly tested.", line++, true)
            line++
            line(player, "Luckily, I was too smart to fall for any of their little tricks,", line++, true)
            line(player, "and passed the test with flying colours.", line++, true)
            line++
        } else if (stage >= 1) {
            line(player, "I should head to !!Falador Park?? to meet my !!Contact?? so that I", line++, false)
            line(player, "can begin my !!testing for the job??.", line++, false)
            line++
        }

        if (stage >= 4) {
            line(player, "I am now an official member of the Temple Knights,", line++, true)
            line(player, "although I have to wait for the paperwork to go through before", line++, true)
            line(player, "I can commence working for them.", line++, true)
        } else if (stage >= 3) {
            line(player, "I should talk to !!Tiffy??.", line++, true)
            line++
        }
        if (stage >= 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        sendString(player, "You have passed the Recruitment Drive!", Components.QUEST_COMPLETE_SCROLL_277, 4)
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.INITIATE_SALLET_5574, 230)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "1000 Prayer, Herblore and", ln++)
        drawReward(player, "Agility XP", ln++)
        drawReward(player, "Gaze Of Saradomin", ln++)
        drawReward(player, "Temple Knight's Initiate Helm", ln)

        rewardXP(player, Skills.PRAYER, 1000.0)
        rewardXP(player, Skills.HERBLORE, 1000.0)
        rewardXP(player, Skills.AGILITY, 1000.0)
        addItem(player, Items.INITIATE_SALLET_5574)
        addItem(player, Items.MAKEOVER_VOUCHER_5606)
        if (!player.isMale && getAttribute(player, makeoverTicket, false)) {
            addItem(player, Items.COINS_995, 3000)
            removeAttribute(player, makeoverTicket)
        }
        removeAttributes(player, stagePass, stageFail, stage, stage0, stage1, stage2, stage3, stage4)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
