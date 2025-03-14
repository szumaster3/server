/*package content.kingdom.asgarnia.quest.member.slug2

import org.rs.consts.Vars
import core.api.rewardXP
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable

@Initializable
class TheSlugMenace : Quest("The Slug Menace", 110, 109, 1, Vars.VARBIT_QUEST_THE_SLUG_MENACE_2610, 0, 1, 14) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        player ?: return

        if (stage == 1) {
            line++
            line(player, "Sir Tiffy Cashien has given me a new task for the Temple Knights.", line++, true)
            line(player, "I am to report to Col. O'Niall in Witchaven, East of Ardougne.", line++, true)
            line(player, "Col. O'Niall has explained that some of the people in", line++, true)
            line++
            line(player, "Witchaven are acting very strangely.", line++, true)
            line++
            line(player, "The mayor has been buying large quantities of", line++, true)
            line(player, "items that Witchaven shouldn't need.", line++, true)
            line(player, "He recommends I speak to the following people:", line++, true)
            line(player, "Holgart, Mayour Eustace Hobb, Brother Maledict", line++, true)
            line(player, "After speaking to all these people", line++, true)
            line(player, "I now need to report back to Col. O'Niall.", line++, true)
            line(player, "After speaking to the Mayor and the others,", line++, true)
            line(player, "I have concluded that something is very wrong in Witchaven.", line++, true)
            line(player, "I think I need to take a close look at the Saradomin shrine,", line++, true)
            line(player, "west of Witchaven.", line++, true)
            line(player, "I also found a dead sea slug on the floor.", line++, true)
            line(player, "I wonder if it will come in handy?", line++, true)
            line(player, "I have been to the Saradomin temple and found a secret entrance", line++, true)
            line(player, "leading to some strange doors covered in odd glyphs.", line++, true)
            line(player, "Savant has given me a transcript of the glyphs and", line++, true)
            line(player, "directed me to Jorral north-west of Ardougne.", line++, true)
            line(player, "If I lost this copy I should be able to get", line++, true)
            line(player, "another copy through the CommOrb.", line++, true)
            line(player, "Both Horral and Savant reckon that this threat is very, very old.", line++, true)
            line(player, "Savant has been able to research the Mother Mallum", line++, true)
            line(player, "and will let me know some information in due time.", line++, true)
            line(player, "Savant has revealed that the Mother Mallum is", line++, true)
            line(player, "behind that strange door.", line++, true)
            line(player, "Col. O'Niall needs to be informed of this.", line++, true)
            line(player, "Col. O'Niall thinks that the Mother Mallum may be breaking free.", line++, true)
            line(player, "Brother Maledict may have some information on", line++, true)
            line(player, "how to strengthen her prison.", line++, true)
            line(player, "I just saw Mayor Hobb coming from the church.", line++, true)
            line(player, "This doesn't bode well at all.", line++, true)
            line(player, "Aside from acting very strangely,", line++, true)
            line(player, "Brother Maledict has informed me that", line++, true)
            line(player, "the ritual to restore the Mother Mallum's", line++, true)
            line(player, "prison is contained within his hold book.", line++, true)
            line(player, "However the relevant pages have been stolen.", line++, true)
            line(player, "I should go look for them.", line++, true)
            line(player, "One of the recovered pages has been torn into pieces.", line++, true)
            line(player, "I wonder if swamp paste can stick it back together?", line++, true)
            line(player, "Savant thinks I can make some clear glue,", line++, true)
            line(player, "from a boiled down sea slug, to fix the torn pages.", line++, true)
            line(player, "Perhaps Bailey at the Fishing Platform can help me?", line++, true)
            line(player, "Bailey has cooked up a dead sea slug into a clear paste.", line++, true)
            line(player, "I should use this to repair the page fragments.", line++, true)
            line(player, "After repairing the torn page I have discovered", line++, true)
            line(player, "that each one contains instructions on how to modify earth,", line++, true)
            line(player, "air, fire, water and mind runes to repair the doorway.", line++, true)
            line(player, "I need to place a shaped earth rune in the doorway.", line++, true)
            line(player, "I need to place a shaped air rune in the doorway.", line++, true)
            line(player, "I need to place a shaped fire rune in the doorway.", line++, true)
            line(player, "I need to place a shaped water rune in the doorway.", line++, true)
            line(player, "I need to place a shaped mind rune in the doorway.", line++, true)
            line(player, "I have been duped.", line++, true)
            line(player, "IThe modified runes actually open the prison doorway!", line++, true)
            line(player, "The Sea Slug Prince emerged and we engaged in a mighty battle.", line++, true)
            line(player, "After slaying the Prince,", line++, true)
            line(player, "the Mother Mallum appeared and announced her plans", line++, true)
            line(player, "for world domination.", line++, true)
            line(player, "Thankfully, Savant removed me to safety. However,", line++, true)
            line(player, "the Sea Slug Queen has escaped.", line++, true)
            line(player, "I wonder what her next action will be.", line++, true)
            line(player, "I need to place a shaped earth rune in the doorway.", line++, true)
            line++
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
        player.packetDispatch.sendItemZoomOnInterface(Items.OYSTER_PEARLS_413, 230, 277, 5)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "3,500 Crafting XP", ln++)
        drawReward(player, "3,500 Runecrafting XP", ln++)
        drawReward(player, "3,500 Thieving XP", ln++)
        rewardXP(player, Skills.CRAFTING, 3500.0)
        rewardXP(player, Skills.RUNECRAFTING, 3500.0)
        rewardXP(player, Skills.THIEVING, 3500.0)
        setVarbit(player, Vars.VARBIT_QUEST_THE_SLUG_MENACE_2610, 14, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

}
//1 quest point
//3,500 Crafting experience
//3,500 Runecrafting experience
//3,500 Thieving experience
//Promotion to Proselyte among the Temple Knights. This means you can buy and wear new Temple Knight armour with a greater Defence bonus than Initiate. You can buy each piece individually, or buy the armour pack which, when opened, gives you the plate, helm, and legs/skirt all at once. The pack is worth 25,000 coins.
//2 Treasure Hunter keys (Ironman accounts will not receive these)
*/
