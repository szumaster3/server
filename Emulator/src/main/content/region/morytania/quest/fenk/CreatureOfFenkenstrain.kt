package content.region.morytania.quest.fenk

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class CreatureOfFenkenstrain :
    Quest(Quests.CREATURE_OF_FENKENSTRAIN, 41, 40, 2, Vars.VARP_QUEST_CREATURE_OF_FENKENSTRAIN_PROGRESS_399, 0, 1, 9) {
    companion object {
        const val attributeArms = "/save:quest:cof-arms"
        const val attributeLegs = "/save:quest:cof-legs"
        const val attributeTorso = "/save:quest:cof-torso"
        const val attributeHead = "/save:quest:cof-decaphead"
        const val attributeUnlockedMemorial = "/save:quest:cof-amuletonmemorial"
        const val attributeUnlockedShed = "/save:quest:cof-keyonsheddoor"
        const val attributeNeedle = "/save:quest:cof-needle"
        const val attributeThread = "/save:quest:cof-thread"
    }

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player, Quests.CREATURE_OF_FENKENSTRAIN) > 0

        if (!started) {
            line(player, "I can start this quest by reading the signpost in the", line++, false)
            line(player, "centre of !!Canifis??.", line++, false)
            line(player, "I must be able to defeat a !!level 51 monster??, and need the", line++, false)
            line(player, "following skill levels:", line++, false)
            line(player, "!!Level 20 Crafting??", line++, hasLevelStat(player, Skills.CRAFTING, 20))
            line(player, "!!Level 25 Thieving??", line++, hasLevelStat(player, Skills.THIEVING, 25))
            line(player, "I also need to have completed the following quests:", line++, false)
            line(player, "!!${Quests.PRIEST_IN_PERIL}??", line++, isQuestComplete(player, Quests.PRIEST_IN_PERIL))
            line(player, "!!${Quests.THE_RESTLESS_GHOST}??", line++, isQuestComplete(player, Quests.THE_RESTLESS_GHOST))
            limitScrolling(player, line, false)
        } else {
            line(player, "I read the signpost in Canifis, which tells of a butler", line++, true)
            line(player, "position that is available at the castle to the northeast.", line++, true)
            if (stage >= 2) {
                line(player, "I spoke to Fenkenstrain, who wanted me to find him some", line++, true)
                line(player, "body parts so that he could build a creature.", line++, true)
            } else if (stage >= 1) {
                line(player, "I should go up to the castle and speak to !!Dr Fenkenstrain??", line++, false)
            }
            line++
            if (stage >= 3) {
                line(player, "I gave a torso, some arms and legs, and a head to Fenkenstrain,", line++, true)
                line(player, "who then wanted a needle and 5 lots of thread, so that he could", line++, true)
                line(player, "sew the bodyparts together and create his creature.", line++, true)
            } else if (stage >= 2) {
                line(player, "I need to find these body parts for !!Fenkenstrain??:", line++, false)
                line(player, "a pair of !!arms??", line++, false)
                line(player, "a pair legs !!legs??", line++, false)
                line(player, "a !!torso??", line++, false)
                line(player, "a !!head??", line++, false)
                line++
                line(player, "Apparently the soil of !!Morytania?? has a unique quality", line++, false)
                line(player, "which preserves the bodies of the dead better than", line++, false)
                line(player, "elsewhere, so perhaps I should look at the graves in the", line++, false)
                line(player, "local area", line++, false)
            }
            line++
            if (stage >= 4) {
                line(player, "I brought Fenkenstrain a needle and 5 quantities of", line++, true)
                line(player, "thread.", line++, true)
            } else if (stage >= 3) {
                line(player, "I need to bring !!Fenkenstrain?? a !!needle?? and !!5 quantities??", line++, false)
                line(player, "!!of thread??.", line++, false)
            }
            line++
            if (stage >= 5) {
                line(player, "I repaired the lightning conductor, and Fenkenstrain", line++, true)
                line(player, "brought the Creature to life.", line++, true)
            } else if (stage >= 4) {
                line(player, "I need to repair the !!lightning conductor?? on the", line++, false)
                line(player, "!!balcony?? above.", line++, false)
            }
            line++
            if (stage == 5) {
                line(player, "!!Fenkenstrain?? wants to talk to me.", line++, false)
                line++
            }
            if (stage >= 7) {
                line(player, "The Creature went on a rampage, and Fenkenstrain sent", line++, true)
                line(player, "me up to the Tower to destroy it.", line++, true)
                line(player, "The Creature convinced me to stop Fenkenstrain's", line++, true)
                line(player, "experiments once and for all, and has told me the true", line++, true)
                line(player, "history of Fenkenstrain's treachery.", line++, true)
            } else if (stage >= 6) {
                line(player, "The !!Creature?? went on a rampage, and !!Fenkenstrain?? wants", line++, false)
                line(player, "me to go up the !!Tower?? to destroy it.", line++, false)
            }
            line++
            if (stage >= 8) {
                line(player, "I stole Fenkenstrain's Ring of Charos, and he released me from", line++, true)
                line(player, "his service.", line++, true)
            } else if (stage >= 7) {
                line(player, "I must find a way to stop Fenkenstrain's experiments.", line++, false)
            }
            if (stage >= 100) {
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
                limitScrolling(player, line, false)
            }
        }
    }

    override fun hasRequirements(player: Player): Boolean {
        return arrayOf(
            hasLevelStat(player, Skills.CRAFTING, 20),
            hasLevelStat(player, Skills.THIEVING, 25),
            isQuestComplete(player, Quests.PRIEST_IN_PERIL),
            isQuestComplete(player, Quests.THE_RESTLESS_GHOST),
        ).all { it }
    }

    override fun reset(player: Player) {
        setVarp(player, Vars.VARP_QUEST_CREATURE_OF_FENKENSTRAIN_PROGRESS_399, 0, true)
        removeAttribute(player, attributeArms)
        removeAttribute(player, attributeLegs)
        removeAttribute(player, attributeTorso)
        removeAttribute(player, attributeHead)
        removeAttribute(player, attributeUnlockedMemorial)
        removeAttribute(player, attributeUnlockedShed)
        removeAttribute(player, attributeNeedle)
        removeAttribute(player, attributeThread)
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed Creature of Fenkenstrain!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.RING_OF_CHAROS_4202, 230, 277, 5)

        drawReward(player, "2 quest points", ln++)
        drawReward(player, "Ring of Charos", ln++)
        drawReward(player, "1,000 Thieving XP", ln++)

        addItemOrDrop(player, Items.RING_OF_CHAROS_4202, 1)
        rewardXP(player, Skills.THIEVING, 1000.0)
    }

    override fun setStage(
        player: Player,
        stage: Int,
    ) {
        super.setStage(player, stage)
        this.updateVarps(player)
    }

    override fun updateVarps(player: Player) {
        if (getQuestStage(player, Quests.CREATURE_OF_FENKENSTRAIN) == 4) {
            setVarp(player, Vars.VARP_QUEST_CREATURE_OF_FENKENSTRAIN_PROGRESS_399, 3, true)
        }
        if (getQuestStage(player, Quests.CREATURE_OF_FENKENSTRAIN) >= 8) {
            setVarp(player, Vars.VARP_QUEST_CREATURE_OF_FENKENSTRAIN_PROGRESS_399, 8, true)
        }
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
