package content.region.karamja.quest.roots

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Vars

@Initializable
class BackToMyRoots : Quest(Quests.BACK_TO_MY_ROOTS, 143, 142, 1, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 0, 1, 65) {

    private val requirements = BooleanArray(8)

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Horacio?? in !!East??", line++)
            line(player, "!!Ardougne?? in the !!Handelmort Mansion?? garden.", line++)
            line(player, "Before I begin I will need to:", line++)
            line(player, if (hasLevelStat(player, Skills.WOODCUTTING, 72)) "---Have at least level 72 Woodcutting./--" else "!!Have at least level 72 Woodcutting.??", line++)
            line(player, if (hasLevelStat(player, Skills.FIREMAKING, 53)) "---Have at least level 53 Firemaking./--" else "!!Have at least level 53 Firemaking.??", line++)
            line(player, if (hasLevelStat(player, Skills.SLAYER, 59)) "---Have at least level 59 Slayer./--" else "!!Have at least level 59 Slayer.??", line++)
            line(player, if (hasLevelStat(player, Skills.AGILITY, 55)) "---Have at least level 55 Agility./--" else "!!Have at least 55 Agility.??", line++)
            line++
            line(player, "I also need to have completed:", line++)
            line(player, "!!${Quests.ONE_SMALL_FAVOUR}??.", line++, hasRequirement(player, Quests.ONE_SMALL_FAVOUR, false))
            line(player, "!!${Quests.TRIBAL_TOTEM}??.", line++, isQuestComplete(player, Quests.TRIBAL_TOTEM))
            line(player, "!!${Quests.THE_HAND_IN_THE_SAND}??.", line++, isQuestComplete(player, Quests.THE_HAND_IN_THE_SAND))
            line(player, "!!${Quests.FAIRYTALE_I_GROWING_PAINS}??.", line++, hasRequirement(player, Quests.FAIRYTALE_I_GROWING_PAINS, false))
            line++
        }

        if (stage == 2) {
            line(player, "I have spoken to Wizard Cromperty and found out about", line++, true)
            line(player, "his missing parcel.", line++, true)
            line++
        }
        if (stage == 3) {
            line(player, "I have spoken to an R.P.D.T. worker.", line++, true)
            line++
        }

        if (stage == 4) {
            line(player, "I have returned the R.P.D.T. to normal - parcels will now be delivered", line++, true)
            line(player, "I also acquired a severed wizards hand.", line++, true)
            line++
            line(player, "I found out about Wizard Cromperty's special", line++, true)
            line(player, "preservation magic - !!a sealed pot??.", line++, true)
            line++
        }

        if(stage == 6) {
            line(player, "I have spoken to Garth the farmer about root", line++, true)
            line(player, "cuttings and getting to the roots of the !!Jade Vine??", line++, true)
            line(player, "which is located east of !!Shilo Village??.", line++, true)
            line++
        }

        if(stage == 7) {
            line(player, "My potted cutting took successfully and I", line++, true)
            line(player, "sealed it in the airtight pot.", line++, true)
            line++
        }

        if(stage == 8) {
            line(player, "I returned the cutting to Horacio.", line++, true)
            line(player, "I agreed to grow the cutting for Horacio in the prepared patch", line++, true)
            line(player, "at Handelmort Mansion.", line++, true)
            line++
            line(player, "Horacio asked me to kill the wild vine.", line++, true)
            line(player, "I have found out how to care for my own Jade Vine.", line++, true)
        }

        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
            line++
            line(player, "I can now grow my own !!Jade Vine?? in the patch", line++, false)
            line(player, "outside !!Handelmort Mansion?? in !!East Ardougne??.", line++, false)
            line(player, "If I ever lose the seed or need help with it, !!Horacio?? will help.", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        // 16.01.2009
        displayQuestItem(player, Items.JADE_VINE_SEED_11778)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "24K Farming XP", ln++)
        drawReward(player, "40K Woodcutting XP", ln++)
        drawReward(player, "23K Slayer XP", ln++)
        drawReward(player, "15K Agility XP", ln++)
        drawReward(player, "New vine Farming patch", ln++)
        drawReward(player, "Jade vine seed", ln)
        rewardXP(player, Skills.FIREMAKING, 24000.0)
        rewardXP(player, Skills.WOODCUTTING, 40000.0)
        rewardXP(player, Skills.SLAYER, 15000.0)
        rewardXP(player, Skills.AGILITY, 15000.0)
        setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 65, true)
        addItemOrBank(player, Items.JADE_VINE_SEED_11778, 1)
        updateQuestTab(player)
    }

    override fun hasRequirements(player: Player): Boolean {
        requirements[0] = hasRequirement(player, Quests.ONE_SMALL_FAVOUR, false)
        requirements[1] = isQuestComplete(player, Quests.TRIBAL_TOTEM)
        requirements[2] = isQuestComplete(player, Quests.THE_HAND_IN_THE_SAND)
        requirements[3] = hasRequirement(player, Quests.FAIRYTALE_I_GROWING_PAINS, false)
        requirements[4] = getStatLevel(player, Skills.WOODCUTTING) >= 72
        requirements[5] = getStatLevel(player, Skills.FIREMAKING) >= 53
        requirements[6] = getStatLevel(player, Skills.SLAYER) >= 59
        requirements[7] = getStatLevel(player, Skills.AGILITY) >= 55
        for (bool in requirements) {
            if (!bool) {
                return false
            }
        }
        return true
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

}
//1 quest point
//24,000 Farming experience
//40,000 Woodcutting experience
//23,000 Slayer experience
//15,000 Agility experience
//Vine Farming Patch in East Ardougne
//Jade vine seed
//Access to jade vine maze and ability to kill giant ant soldiers, giant ant workers, tenacious toucans, giant wasps, frogs and pernicious parrots within it.
//Transportation between Eagles' Peak and Karamja using the Karamjan Jungle eagle.
//Ability to slay the wild jade vine for extra Slayer and Farming experience once every 15 hours.
//2 Treasure Hunter keys (Ironman accounts will not receive these)