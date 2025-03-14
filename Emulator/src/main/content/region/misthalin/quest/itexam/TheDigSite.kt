package content.region.misthalin.quest.itexam

import core.api.*
import core.api.quest.getQuestStage
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class TheDigSite : Quest(Quests.THE_DIG_SITE, 47, 46, 2, 131, 0, 1, 9) {
    companion object {
        const val attrGreenExam1Talked = "/save:digsite:greenexam1talked"
        const val attrGreenExam1ObtainAnswer = "/save:digsite:greenexam1answer"
        const val attrPurpleExam1Talked = "/save:digsite:purpleexam1talked"
        const val attrPurpleExam1ObtainAnswer = "/save:digsite:purplexam1answer"
        const val attrBrownExam1Talked = "/save:digsite:brownexam1talked"
        const val attrBrownExam1ObtainAnswer = "/save:digsite:brownexam1answer"
        const val attributePanningGuideTea = "/save:quest:thedigsite-panningguidetea"
        const val attrGreenExam2ObtainAnswer = "/save:digsite:greenexam2answer"
        const val attrPurpleExam2ObtainAnswer = "/save:digsite:purplexam2answer"
        const val attrBrownExam2ObtainAnswer = "/save:digsite:brownexam2answer"
        const val attrGreenExam3ObtainAnswer = "/save:digsite:greenexam3answer"
        const val attrPurpleExam3ObtainAnswer = "/save:digsite:purplexam3answer"
        const val attrPurpleExam3Talked = "/save:digsite:purpleexam3talked"
        const val attrBrownExam3ObtainAnswer = "/save:digsite:brownexam3answer"

        const val attributeRopeNorthEastWinch = "/save:digsite-ropenortheastwinch"
        const val attributeRopeWestWinch = "/save:digsite-ropewestwinch"

        const val attributeFirstQuestion = "digsite:firstquestion"
        const val attributeSecondQuestion = "digsite:secondquestion"
        const val attributeThirdQuestion = "digsite:thirdquestion"

        const val barrelVarbit = 2547
        const val tabletVarbit = 2548
    }

    override fun reset(player: Player) {
        removeAttribute(player, attrGreenExam1Talked)
        removeAttribute(player, attrGreenExam1ObtainAnswer)
        removeAttribute(player, attrPurpleExam1Talked)
        removeAttribute(player, attrPurpleExam1ObtainAnswer)
        removeAttribute(player, attrBrownExam1Talked)
        removeAttribute(player, attrBrownExam1ObtainAnswer)
        removeAttribute(player, attributePanningGuideTea)
        removeAttribute(player, attrGreenExam2ObtainAnswer)
        removeAttribute(player, attrPurpleExam2ObtainAnswer)
        removeAttribute(player, attrBrownExam2ObtainAnswer)
        removeAttribute(player, attrGreenExam3ObtainAnswer)
        removeAttribute(player, attrPurpleExam3Talked)
        removeAttribute(player, attrPurpleExam3ObtainAnswer)
        removeAttribute(player, attrBrownExam3ObtainAnswer)
        removeAttribute(player, attributeRopeNorthEastWinch)
        removeAttribute(player, attributeRopeWestWinch)
    }

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        var stage = getStage(player)

        var started = getQuestStage(player, Quests.THE_DIG_SITE) > 0

        if (!started) {
            line++
            line(player, "I can start this quest by speaking to the !!Examiner?? at the", line++, false)
            line(player, "!!Digsite Exam Centre.??", line++, false)
            line(player, "I need the following skill levels:", line++, false)
            line(player, "!!Level 10 Agility??", line++, hasLevelStat(player, Skills.AGILITY, 10))
            line(player, "!!Level 10 Herblore??", line++, hasLevelStat(player, Skills.HERBLORE, 10))
            line(player, "!!Level 25 Thieving??", line++, hasLevelStat(player, Skills.THIEVING, 25))
            limitScrolling(player, line, true)
        } else {
            line(player, "I should speak to an examiner about taking Earth Science", line++, true)
            line(player, "Exams.", line++, true)

            if (stage >= 2) {
                line(player, "I should take the letter the Examiner has given me to the", line++, true)
                line(player, "Curator of Varrock Museum, for his approval.", line++, true)
            } else if (stage >= 1) {
                line(player, "I should take the !!letter?? the !!Examiner?? has given me to the", line++)
                line(player, "!!Curator?? of !!Varrock Museum??, for his approval.", line++)
            }

            if (stage >= 3) {
                line(player, "I need to return the letter of recommendation from the", line++, true)
                line(player, "Curator of Varrock Museum to the Examiner at the Exam", line++, true)
                line(player, "Centre for inspection.", line++, true)
            } else if (stage >= 2) {
                line(player, "I need to return the !!letter of recommendation?? from the", line++)
                line(player, "!!Curator?? of !!Varrock Museum?? to the !!Examiner?? at the !!Exam??", line++)
                line(player, "!!Centre?? for inspection.", line++)
            }
            if (stage >= 4) {
                line(player, "I need to study for my first exam. Perhaps the students", line++, true)
                line(player, "on the site can help?", line++, true)
            } else if (stage >= 3) {
                line(player, "I need to study for my first exam. Perhaps the students", line++)
                line(player, "on the site can help?", line++)
            }
            if (stage >= 4 || getAttribute(player, attrGreenExam1Talked, false)) {
                line(player, "I need to speak to the student in the green top about the", line++, true)
                line(player, "exams.", line++, true)
            } else if (stage >= 3) {
                line(player, "I need to speak to the student in the green top about the", line++)
                line(player, "exams.", line++)
            }
            if (stage >= 4 || getAttribute(player, attrPurpleExam1Talked, false)) {
                line(player, "I need to speak to the student in the purple shirt about", line++, true)
                line(player, "the exams.", line++, true)
            } else if (stage >= 3) {
                line(player, "I need to speak to the student in the purple shirt about", line++)
                line(player, "the exams.", line++)
            }
            if (stage >= 4 || getAttribute(player, attrBrownExam1Talked, false)) {
                line(player, "I need to speak to the student in the brown top about the", line++, true)
                line(player, "exams.", line++, true)
            } else if (stage >= 3) {
                line(player, "I need to speak to the student in the brown top about the", line++)
                line(player, "exams.", line++)
            }

            if (stage >= 4 || getAttribute(player, attrGreenExam1ObtainAnswer, false)) {
                line(player, "I have agreed to help the student in the green top.", line++, true)
                line(player, "He has lost his animal skull and thinks he may have", line++, true)
                line(player, "dropped it around the site. I need to find it and return", line++, true)
                line(player, "it to him. Maybe one of the workmen has picked it up?", line++, true)
            } else if (stage >= 3 && getAttribute(player, attrGreenExam1Talked, false)) {
                line(player, "I have agreed to help the student in the green top.", line++)
                line(player, "He has lost his animal skull and thinks he may have", line++)
                line(player, "dropped it around the site. I need to find it and return", line++)
                line(player, "it to him. Maybe one of the workmen has picked it up?", line++)
            }
            if (stage >= 4) {
                line(player, "I should talk to him to see if he can help with my exams.", line++, true)
                line(player, "He gave me an answer to one of the questions on the first", line++, true)
                line(player, "exam.", line++, true)
            } else if (stage >= 3 && getAttribute(player, attrGreenExam1ObtainAnswer, false)) {
                line(player, "I should talk to him to see if he can help with my exams.", line++)
                line(player, "He gave me an answer to one of the questions on the first", line++)
                line(player, "exam.", line++)
            }

            if (stage >= 4 || getAttribute(player, attrPurpleExam1ObtainAnswer, false)) {
                line(player, "I have agreed to help the student in the purple skirt.", line++, true)
                line(player, "She has lost her lucky teddy bear mascot and thinks she", line++, true)
                line(player, "may have dropped it by the strange relic at the centre of", line++, true)
                line(player, "the campus, maybe in a bush. I need to find it and return", line++, true)
                line(player, "it to her.", line++, true)
            } else if (stage >= 3 && getAttribute(player, attrPurpleExam1Talked, false)) {
                line(player, "I have agreed to help the student in the purple skirt.", line++)
                line(player, "She has lost her lucky teddy bear mascot and thinks she", line++)
                line(player, "may have dropped it by the strange relic at the centre of", line++)
                line(player, "the campus, maybe in a bush. I need to find it and return", line++)
                line(player, "it to her.", line++)
            }
            if (stage >= 4) {
                line(player, "I should talk to her to see if she can help with my exams.", line++, true)
                line(player, "She gave me an answer to one of the questions on the first", line++, true)
                line(player, "exam.", line++, true)
            } else if (stage >= 3 && getAttribute(player, attrPurpleExam1ObtainAnswer, false)) {
                line(player, "I should talk to her to see if she can help with my exams.", line++)
                line(player, "She gave me an answer to one of the questions on the first", line++)
                line(player, "exam.", line++)
            }

            if (stage >= 4 || getAttribute(player, attrBrownExam1ObtainAnswer, false)) {
                line(player, "I have agreed to help the student in the brown top.", line++, true)
                line(player, "He has lost his special cup and thinks he may have dropped", line++, true)
                line(player, "it while he was near the panning site, possibly in the", line++, true)
                line(player, "water. I need to find it and return it.", line++, true)
            } else if (stage >= 3 && getAttribute(player, attrBrownExam1Talked, false)) {
                line(player, "I have agreed to help the student in the brown top.", line++)
                line(player, "He has lost his special cup and thinks he may have dropped", line++)
                line(player, "it while he was near the panning site, possibly in the", line++)
                line(player, "water. I need to find it and return it.", line++)
            }
            if (stage >= 4) {
                line(player, "I should talk to him to see if he can help with my exams.", line++, true)
                line(player, "He gave me an answer to one of the questions on the first", line++, true)
                line(player, "exam.", line++, true)
            } else if (stage >= 3 && getAttribute(player, attrBrownExam1ObtainAnswer, false)) {
                line(player, "I should talk to him to see if he can help with my exams.", line++)
                line(player, "He gave me an answer to one of the questions on the first", line++)
                line(player, "exam.", line++)
            }

            if (stage >= 4) {
                line(player, "I should talk to an examiner to take my first exam. If I", line++, true)
                line(player, "have forgotten anything, I can always ask the students", line++, true)
                line(player, "again.", line++, true)
                line(player, "I have passed my first Earth Science exam.", line++, true)
            } else if (stage >= 3 &&
                getAttribute(player, attrGreenExam1ObtainAnswer, false) &&
                getAttribute(player, attrPurpleExam1ObtainAnswer, false) &&
                getAttribute(player, attrBrownExam1ObtainAnswer, false)
            ) {
                line(player, "I should talk to an examiner to take my first exam. If I", line++)
                line(player, "have forgotten anything, I can always ask the students", line++)
                line(player, "again.", line++)
            }

            if (stage >= 5) {
                line(player, "I need to study for my second exam. Perhaps the students", line++, true)
                line(player, "on the site can help?", line++, true)
            } else if (stage >= 4) {
                line(player, "I need to study for my second exam. Perhaps the students", line++)
                line(player, "on the site can help?", line++)
            }
            if (stage >= 5 || getAttribute(player, attrGreenExam2ObtainAnswer, false)) {
                line(player, "I need to speak to the student in the green top about the", line++, true)
                line(player, "exams.", line++, true)
            } else if (stage >= 4) {
                line(player, "I need to speak to the student in the green top about the", line++)
                line(player, "exams.", line++)
            }
            if (stage >= 5 || getAttribute(player, attrPurpleExam2ObtainAnswer, false)) {
                line(player, "I need to speak to the student in the purple skirt about", line++, true)
                line(player, "the exams. 2 ", line++, true)
            } else if (stage >= 4) {
                line(player, "I need to speak to the student in the purple skirt about", line++)
                line(player, "the exams.", line++)
            }
            if (stage >= 5 || getAttribute(player, attrBrownExam2ObtainAnswer, false)) {
                line(player, "I need to speak to the student in the brown top about the", line++, true)
                line(player, "exams.", line++, true)
            } else if (stage >= 4) {
                line(player, "I need to speak to the student in the brown top about the", line++)
                line(player, "exams.", line++)
            }
            if (stage >= 5) {
                line(player, "I should talk to an examiner to take my second exam. If I", line++, true)
                line(player, "have forgotten anything, I can always ask the students", line++, true)
                line(player, "again.", line++, true)
                line(player, "I have passed my second Earth Science exam.", line++, true)
            } else if (stage >= 4 &&
                getAttribute(player, attrGreenExam2ObtainAnswer, false) &&
                getAttribute(player, attrPurpleExam2ObtainAnswer, false) &&
                getAttribute(player, attrBrownExam2ObtainAnswer, false)
            ) {
                line(player, "I should talk to an examiner to take my second exam. If I", line++)
                line(player, "have forgotten anything, I can always ask the students", line++)
                line(player, "again.", line++)
            }

            if (stage >= 6) {
                line(player, "I need to study for my third exam. Perhaps the students", line++, true)
                line(player, "on the site can help?", line++, true)
            } else if (stage >= 5) {
                line(player, "I need to study for my third exam. Perhaps the students", line++)
                line(player, "on the site can help?", line++)
            }
            if (stage >= 6 || getAttribute(player, attrGreenExam3ObtainAnswer, false)) {
                line(player, "I need to speak to the student in the green top about the", line++, true)
                line(player, "exams.", line++, true)
            } else if (stage >= 5) {
                line(player, "I need to speak to the student in the green top about the", line++)
                line(player, "exams.", line++)
            }
            if (stage >= 6 || getAttribute(player, attrPurpleExam3Talked, false)) {
                line(player, "I need to speak to the student in the purple skirt about", line++, true)
                line(player, "the exams. 3", line++, true)
            } else if (stage >= 5) {
                line(player, "I need to speak to the student in the purple skirt about", line++)
                line(player, "the exams.", line++)
            }
            if (stage >= 6 || getAttribute(player, attrPurpleExam3ObtainAnswer, false)) {
                line(player, "I need to bring her an Opal.", line++, true)
            } else if (stage >= 5 && getAttribute(player, attrPurpleExam3Talked, false)) {
                line(player, "I need to bring her an Opal.", line++)
            }
            if (stage >= 6 || getAttribute(player, attrBrownExam3ObtainAnswer, false)) {
                line(player, "I need to speak to the student in the brown top about the", line++, true)
                line(player, "exams.", line++, true)
            } else if (stage >= 5) {
                line(player, "I need to speak to the student in the brown top about the", line++)
                line(player, "exams.", line++)
            }
            if (stage >= 6) {
                line(player, "I should talk to an examiner to take my third exam. If I", line++, true)
                line(player, "have forgotten anything, I can always ask the students", line++, true)
                line(player, "again.", line++, true)
                line(player, "I have passed my third and final Earth Science exam.", line++, true)
            } else if (stage >= 5 &&
                getAttribute(player, attrPurpleExam3ObtainAnswer, false) &&
                getAttribute(player, attrGreenExam3ObtainAnswer, false) &&
                getAttribute(player, attrBrownExam3ObtainAnswer, false)
            ) {
                line(player, "I should talk to an examiner to take my third exam. If I", line++)
                line(player, "have forgotten anything, I can always ask the students", line++)
                line(player, "again.", line++)
            }

            if (stage >= 7) {
                line(player, "I need a find from the digsite to impress the Expert.", line++, true)
            } else if (stage >= 6) {
                line(player, "I need a find from the digsite to impress the Expert.", line++)
            }
            if (stage >= 8) {
                line(player, "I need to take the letter to a workman near a winch.", line++, true)
            } else if (stage >= 7) {
                line(player, "I need to take the letter to a workman near a winch.", line++)
            }
            if (stage >= 9) {
                line(player, "I need to investigate the dig shafts.", line++, true)
                line(player, "I found a secret passageway under the site.", line++, true)
            } else if (stage >= 8) {
                line(player, "I need to !!investigate the dig shafts??.", line++)
            }
            if (stage >= 10) {
                line(player, "I need to find a way to move the rocks blocking the way in", line++, true)
                line(player, "the shaft. Perhaps someone can help me.", line++, true)
                line(player, "I covered the rocks in the cave with an explosive", line++, true)
                line(player, "compound.", line++, true)
            } else if (stage >= 9) {
                line(player, "I need to find a way to move the rocks blocking the way in", line++)
                line(player, "the shaft. Perhaps someone can help me.", line++)
            }
            if (stage >= 11) {
                line(player, "I need to ignite the explosive compound and blow up the", line++, true)
                line(player, "rocks blocking the way.", line++, true)
            } else if (stage >= 10) {
                line(player, "I need to ignite the explosive compound and blow up the", line++)
                line(player, "rocks blocking the way.", line++)
            }
            if (stage >= 12) {
                line(player, "I should look for something interesting in the secret room I", line++, true)
                line(player, "found, and show it to the Expert at the Exam Centre.", line++, true)
                line(player, "The expert was impressed with the Zarosian tablet that I", line++, true)
                line(player, "found, and I also discovered an ancient altar!", line++, true)
                line(player, "I was rewarded for my findings.", line++, true)
                line(player, "My work here is done.", line++, true)
                line(player, "I should also talk to the expert about any other finds.", line++, true)
            } else if (stage >= 11) {
                line(player, "I should look for something interesting in the secret room I", line++)
                line(player, "found, and show it to the !!Expert?? at the Exam Centre.", line++)
            }
            if (stage >= 100) {
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
            }
            limitScrolling(player, line, false)
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed Digsite Quest!", 277, 4)

        player.packetDispatch.sendModelOnInterface(17343, 277, 5, 0)
        player.packetDispatch.sendAngleOnInterface(277, 5, 1020, 0, 0)

        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "15,300 Mining XP", ln++)
        drawReward(player, "2,000 Herblore XP", ln++)
        drawReward(player, "2 Gold Bars", ln)

        rewardXP(player, Skills.MINING, 15300.0)
        rewardXP(player, Skills.HERBLORE, 2000.0)
        addItemOrDrop(player, Items.GOLD_BAR_2357, 2)
        setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_43_3643, 1, true)
        setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_42_3644, 1, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
