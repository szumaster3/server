package content.region.kandarin.quest.elementalquest2

import content.region.kandarin.quest.elementalquest2.handlers.EW2Utils
import core.api.*
import core.api.quest.isQuestComplete
import org.rs.consts.Items
import org.rs.consts.Vars
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills

// @Initializable
class EW2 : Quest("Elemental Workshop II", 53, 52, 1, Vars.VARBIT_QUEST_ELEMENTAL_WORKSHOP_II_PROGRESS_2639, 0, 1, 11) {

    class SkillRequirement(val skill: Int?, val level: Int?)

    val requirements = arrayListOf<SkillRequirement>()

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        player ?: return
        if (stage == 0) {
            line(player, "I can start this quest by reading a", line++)
            line(player, "!!book?? found in !!Dig Site??.", line++)
            line++
            line(player, "To start this quest, I need to complete:", line++, false)
            if (isQuestComplete(player, "Elemental Workshop I")) {
                line(player, "---Elemental Workshop I/--", line++, false)
                line++
            } else {
                line(player, "!!Elemental Workshop I??", line++, false)
                line++
            }
            line(player, "Minimum requirements:", line++)
            line(player, if (hasLevelStat(player, Skills.MAGIC, 20)) "---Level 20 Magic/--" else "!!Level 20 Magic??", line++)
            line(player, if (hasLevelStat(player, Skills.SMITHING, 30)) "---Level 30 Smithing/--" else "!!Level 30 Smithing??", line++)
            line++
        } else if (stage == 1) {
            line(player, "---I have found a book at the Dig site./--", line++)
            line++
        }
        if (stage >= 2) {
            line++
            line(player, "The book tells of a way to !!make??", line++, stage > 3)
            line(player, "!!elemental items?? even more !!powerful?? than before.", line++, stage > 3)
            line++
        }
        if (stage >= 3) {
            line(player, "The !!note?? found with the !!book?? has", line++, stage > 3)
            line(player, "a !!strange code?? written on it.", line++, stage > 3)
            line++
        }
        if (stage >= 4) {
            line(player, "By following the code on the !!note??", line++, stage > 5)
            line(player, "I was able to !!find a key??.", line++, stage > 5)
        }
        if (stage >= 5) {
            line(player, "The !!key?? I've found opens the !!large hatch?? in the !!workshop??; there are !!4 machines?? on this level to fix:", line++, false)
            line++
        }
        if (stage >= 6) {
            line(player, "The crane - now the claw has been replaced the crane looks like it's in good working order.", line++, false)
            line(player, "The pipes - the pipes have now been fitted and it should work as long as it's powered.", line++, false)
            line(player, "The tank - this should work fine now I have replaced the pipe.", line++, false)
            line(player, "A wind tunnel - I have fitted all the cogs on to the pins. ", line++, false)
            line(player, "The win tunnel should now work as long as it's powered.", line++, false)
            line(player, "Once I have fixed all the equipment I'll need to work out how to use it.", line++, false)
            line(player, "I have found a track-mounted jig on which I was able to clamp an elemental bar.", line++, false)
            line(player, "I was able to operate the crane and dip the elemental bar into the lava.", line++, false)
            line(player, "The bar is now very hot.", line++, false)
            line(player, "I used the press to flatten the hot elemental bar.", line++, false)
            line(player, "The water tank was used to cool the elemental bar down.", line++, false)
            line(player, "The wind tunnel proved a great way to dry off the wet bar.", line++, false)
            line(player, "I have primed a bar of elemental ore.", line++, false)
            line(player, "I placed the primed bar on the device found in the room with the mind symbols on the door.", line++, false)
            line(player, "Operating the device charged the primed bar with some of my own mind power.", line++, false)
            line(player, "I have made an elemental mind bar, now I just have to make one of the helms from it.", line++, false)
            line(player, "Creating the elemental mind helm was easy with the book for instruction.", line++, false)
        }
        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10

        player.packetDispatch.sendItemZoomOnInterface(Items.ELEMENTAL_HELMET_9729, 230, 277, 5)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "7,500 Smithing XP,", ln++)
        drawReward(player, "7,500 Crafting XP,", ln++)
        drawReward(player, "Ability to make and equip", ln++)
        drawReward(player, "elemental mind equipment", ln)
        rewardXP(player, Skills.SMITHING, 7500.0)
        rewardXP(player, Skills.CRAFTING, 7500.0)
        setVarbit(player, Vars.VARBIT_QUEST_ELEMENTAL_WORKSHOP_II_PROGRESS_2639, 11, true)
        removeAttributes(player, EW2Utils.foundBook, EW2Utils.questProgress)
    }

    override fun newInstance(`object`: Any?): Quest {
        requirements.add(SkillRequirement(Skills.MAGIC, 20))
        requirements.add(SkillRequirement(Skills.SMITHING, 30))
        return this
    }

}