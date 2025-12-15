package content.region.misthalin.draynor.quest.anma

import content.region.misthalin.draynor.quest.anma.dialogue.AliceDialogue
import content.region.misthalin.draynor.quest.anma.dialogue.AliceHusbandDialogue
import content.region.misthalin.draynor.quest.anma.dialogue.AvaDialogue
import content.region.misthalin.draynor.quest.anma.dialogue.WitchDialogue
import content.region.misthalin.draynor.quest.anma.plugin.AnimalMagnetismPlugin
import content.region.misthalin.draynor.quest.anma.plugin.AnimalMagnetismPlugin.*
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.Quests

/**
 * The Animal magnetism quest journal.
 */
@Initializable
class AnimalMagnetism : Quest(Quests.ANIMAL_MAGNETISM, 33, 32, 1) {

    private val requirements = BooleanArray(7)

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11

        if (stage == 0) {
            line(player, "I can start this quest by talking to !!Ava?? who lives", line++)
            line(player, "in !!Draynor Manor??.", line++)
            line(player, "Minimum requirements:", line)
            drawRequirements(player)
        }

        if (stage == 10) {
            line(player, "!!Ava?? has asked me for !!undead chickens??. One will go toward", line++)
            line(player, "making her bed more comfortable, the other will be used in", line++)
            line(player, "some unexplained reward for me.", line++)
            line(player, "I need to find someone who will supply !!undead chickens?? to", line++)
            line(player, "me. Perhaps the !!farm near Port Phasmatys?? sells them...", line)
        }

        if (stage == 11) {
            line(player, "The !!ghost farmer?? wants me to talk to his !!wife?? for him. I", line++)
            line(player, "need to do this before he will sell chickens.", line)
        }

        if (stage == 12) {
            line(player, "The !!ghost farmer's wife?? needs to know bank information", line++)
            line(player, "that only the farmer can supply.", line)
        }

        if (stage == 13) {
            line(player, "The !!ghost farmer?? won't tell me the information his !!wife?? is", line++)
            line(player, "after. Perhaps I should talk to her again.", line)
        }

        if (stage == 14) {
            line(player, "The !!ghost farmer's wife?? still needs to know bank", line++)
            line(player, "information that only the farmer can supply.", line)
        }

        if (stage == 16) {
            line(player, "I should talk to the !!crone?? west of the undead farm and ask", line++)
            line(player, "about !!ghostspeak amulets??. Perhaps she can enable the", line++)
            line(player, "!!ghost farmer?? to talk to his !!wife?? directly.", line)
        }

        if (stage == 17) {
            line(player, "I need to talk to the !!crone?? while I have a !!ghostspeak amulet??", line++)
            line(player, "so that she can create a new amulet specifically for the !!ghost farmer??.", line)
        }

        if (stage == 18) {
            line(player, "I should give the !!ghost farmer?? a !!crone-made amulet?? so", line++)
            line(player, "that he can talk directly to his !!wife??.", line)
        }

        if (stage == 19) {
            line(player, "The !!ghost farmer?? seems friendlier now; I need to talk to him", line++)
            line(player, "about the !!undead chickens??.", line)
        }

        if (stage == 20) {
            line(player, "The !!ghost farmer?? has agreed to sell chickens; now he needs to", line++)
            line(player, "catch one for me.", line)
        }

        if (stage == 25) {
            line(player, "The !!ghost farmer?? caught some chickens; now I need to buy", line++)
            line(player, "2 from him and deliver them to !!Ava??.", line)
        }

        if (stage == 26) {
            line(player, "I need to talk to the !!Witch?? in !!Draynor Manor?? about", line++)
            line(player, "magically attuned magnets. Apparently, the !!undead chicken??", line++)
            line(player, "will be using magnets in my reward.", line)
        }

        if (stage == 27) {
            line(player, "I need to deliver 5 !!iron bars?? to the !!Witch?? in !!Draynor Manor??.", line++)
            line(player, "She will select one most suitable for both magnetising and mystical use.", line)
        }

        if (stage == 28) {
            line(player, "I need to make a magnet by hammering the selected !!iron bar?? while", line++)
            line(player, "facing north in !!Rimmington mines??. I then need to pass this magnet to !!Ava??.", line)
        }

        if (stage == 29) {
            line(player, "I need to chop some wood from the !!undead trees?? near !!Draynor Manor??.", line++)
            line(player, "!!Ava?? can use this wood as a source of unending arrow shafts in my reward.", line)
        }

        if (stage in 30..31) {
            line(player, "I need to collect a !!holy symbol of Saradomin?? and a !!mithril axe??.", line)
            line(player, "!!Turael??, the !!Burthorpe Slayer??, can use these to construct a new axe for my undead tree cutting.", line)
        }

        if (stage == 32) {
            line(player, "I need to chop some undead wood with the silver-edged !!mithril axe??.", line)
            line(player, "Then !!Ava?? will want the wood for constructing my reward.", line)
        }

        if (stage == 33) {
            line(player, "I should ask !!Ava?? for the garbled research notes that she cannot translate.", line)
            line(player, "When translated, these notes will tell her how to combine the !!undead wood??,", line++)
            line(player, "!!undead chicken?? and !!magnet?? into some bizarre device.", line)
        }

        if (stage == 34) {
            line(player, "Almost finished! I must combine the !!pattern?? which !!Ava?? gave to me", line++)
            line(player, "with some !!polished buttons?? and a bit of !!hard leather??.", line)
            line(player, "!!Ava?? wants the completed container. She can then combine it with", line++)
            line(player, "the !!undead chicken??, !!undead wood?? and !!magnet??.", line)
        }

        if (stage == 100) {
            line(player, "Ava has asked me for undead chickens. One will go", line++, true)
            line(player, "towards making her bed more comfortable, the other will", line++, true)
            line(player, "be used in some unexplained reward for me.", line++, true)
            line(player, "I need to find someone who will supply undead chickens to", line++, true)
            line(player, "me. Perhaps the farm near Port Phasmatys sells them...", line++, true)
            line(player, "The ghost farmer wants me to talk to his wife for him.", line++, true)
            line(player, "I need to do this before he will sell the chickens.", line++, true)
            line(player, "I should talk to the crone west of the undead farm and ask", line++, true)
            line(player, "about ghostspeak amulets. Perhaps she can enable the", line++, true)
            line(player, "ghost farmer to talk to his wife directly.", line++, true)
            line(player, "I need to talk to the crone while I have a ghostspeak amulet", line++, true)
            line(player, "so that she can create a new amulet specifically for the ghost farmer.", line++, true)
            line(player, "I should give the ghost farmer a crone-made amulet so", line++, true)
            line(player, "that he can talk directly to his wife.", line++, true)
            line(player, "The ghost farmer seems friendlier now; I need to talk to him", line++, true)
            line(player, "about the undead chickens.", line++, true)
            line(player, "The ghost farmer has agreed to sell chickens; now he needs to", line++, true)
            line(player, "catch one for me.", line++, true)
            line(player, "The ghost farmer caught some chickens; now I need to buy", line++, true)
            line(player, "2 and deliver them to Ava.", line++, true)
            line(player, "I need to talk to the Witch in Draynor Manor about", line++, true)
            line(player, "magically attuned magnets. Apparently, the undead chicken", line++, true)
            line(player, "will be using magnets in my reward.", line++, true)
            line(player, "I need to deliver 5 iron bars to the Witch in Draynor Manor.", line++, true)
            line(player, "She will select one most suitable for both magnetising", line++, true)
            line(player, "and mystical use.", line++, true)
            line(player, "I need to make a magnet by hammering the selected iron bar", line++, true)
            line(player, "while facing north in Rimmington mines.", line++, true)
            line(player, "I then need to pass this magnet to Ava.", line++, true)
            line(player, "I need to find some way of chopping the undead trees", line++, true)
            line(player, "near Draynor Manor so that Ava can use this wood as a source", line++, true)
            line(player, "of unending arrow shafts. Ava suspects that Turael,", line++, true)
            line(player, "the Slayer Master in Burthorpe, might be able to help.", line++, true)
            line(player, "I need to collect a holy symbol of Saradomin and a mithril axe.", line++, true)
            line(player, "Turael can use these to construct a new axe for", line++, true)
            line(player, "my undead tree cutting.", line++, true)
            line(player, "I need to chop some undead wood with the silver-edged mithril axe.", line++, true)
            line(player, "Then Ava will want the wood for constructing my reward.", line++, true)
            line(player, "I should ask Ava for the garbled research notes that", line++, true)
            line(player, "she cannot translate.", line++, true)
            line(player, "When translated, these notes will tell her how to combine the", line++, true)
            line(player, "undead wood, undead chicken and magnet into some bizarre device.", line++, true)
            line(player, "The research notes must be translated.", line++, true)
            line(player, "I should try to decipher them even though they look like total", line++, true)
            line(player, "gibberish to me. The notes look less confusing now.", line++, true)
            line(player, "Ava will want to see these translated research notes.", line++, true)
            line(player, "Almost finished! I must combine the pattern which Ava gave to me", line++, true)
            line(player, "with some polished buttons and a bit of hard leather.", line++, true)
            line(player, "Ava wants the completed container. She can then combine it with the", line++, true)
            line(player, "undead chicken, undead wood and magnet.", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++)
            line(player, "!!Ava??'s reward for me is an arrow attracting and creating backpack.", line++)
            line(player, "The method is this: the !!undead chicken?? can attract lost, stray", line++)
            line(player, "arrowheads with a magnet, add wood from the !!undead twigs?? and", line++)
            line(player, "then finish the arrows using its own feathers.", line++)
            line(player, "This will give me an unending source of arrows.", line++)
            line(player, "The cunning bird will also attract some of the arrows which I", line++)
            line(player, "have fired, preventing these arrows from falling upon the ground.", line++)
            line(player, "If I lost my device, I can talk to !!Ava?? for a new one, although it", line++)
            line(player, "will cost me around 1000 gold.", line++)
            line(player, "Once I achieve a Ranger level of 50 or more,", line++)
            line(player, "I can upgrade the attractor if I give !!Ava?? 75 steel arrows.", line)
        }
    }

    private fun drawRequirements(player: Player) {
        hasRequirements(player)
        var line = 8 + 7
        for (i in requirements.indices) {
            line(player, "!!" + REQS[i], line++, requirements[i])
        }
    }

    override fun hasRequirements(player: Player): Boolean {
        requirements[0] = isQuestComplete(player, Quests.THE_RESTLESS_GHOST)
        requirements[1] = isQuestComplete(player, Quests.ERNEST_THE_CHICKEN)
        requirements[2] = isQuestComplete(player, Quests.PRIEST_IN_PERIL)
        requirements[3] = getStatLevel(player, Skills.RANGE) >= 30
        requirements[4] = getStatLevel(player, Skills.SLAYER) >= 18
        requirements[5] = getStatLevel(player, Skills.CRAFTING) >= 19
        requirements[6] = getStatLevel(player, Skills.WOODCUTTING) >= 35
        for (bool in requirements) {
            if (!bool) {
                return false
            }
        }
        return true
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        val item = if (getStatLevel(player, Skills.RANGE) >= 50) AVAS_ACCUMULATOR else AVAS_ATTRACTOR

        displayQuestItem(player,  item)
        drawReward(player, "1000 XP in each of Crafting,", ln++)
        drawReward(player, "Fletching and Slayer", ln++)
        drawReward(player, "2500 Woodcutting XP", ln++)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "Ava's device", ln)

        rewardXP(player, Skills.CRAFTING, 1000.0)
        rewardXP(player, Skills.SLAYER, 1000.0)
        rewardXP(player, Skills.FLETCHING, 1000.0)
        rewardXP(player, Skills.WOODCUTTING, 2500.0)

        addItem(player, item)

        updateQuestTab(player)
    }

    /*
      stage < 1     -> 0
      stage 1..27   -> 10
      stage 28..99  -> 150
      stage >= 100  -> 240
     */

    override fun getConfig(player: Player, stage: Int): IntArray {
        if (getStage(player) >= 28 && getStage(player) != 100) {
            return intArrayOf(939, 150)
        }
        val `val` = if (stage < 100 && stage > 0) 10 else if (stage >= 100) 240 else 0
        return intArrayOf(939, `val`)
    }

    override fun newInstance(`object`: Any?): Quest {
        definePlugin(AvaDialogue())
        definePlugin(AliceDialogue())
        definePlugin(WitchDialogue())
        definePlugin(ContainerHandler())
        definePlugin(UndeadTreePlugin())
        definePlugin(HammerMagnetPlugin())
        definePlugin(ResearchNoteHandler())
        definePlugin(AliceHusbandDialogue())
        definePlugin(AnimalMagnetismPlugin())
        return this
    }

    companion object {
        const val CRONE_AMULET = Items.CRONE_MADE_AMULET_10500
        const val SELECTED_IRON  = Items.SELECTED_IRON_10488
        const val RESEARCH_NOTES =  Items.RESEARCH_NOTES_10492
        const val TRANSLATED_NOTES = Items.TRANSLATED_NOTES_10493
        const val PATTERN = Items.A_PATTERN_10494
        const val CONTAINER = Items.A_CONTAINER_10495
        const val POLISHED_BUTTONS = Items.POLISHED_BUTTONS_10496
        const val HARD_LEATHER = Items.HARD_LEATHER_1743
        const val AVAS_ATTRACTOR = Items.AVAS_ATTRACTOR_10498
        const val AVAS_ACCUMULATOR = Items.AVAS_ACCUMULATOR_10499
        private val REQS = arrayOf("I must have completed Restless Ghost.", "I must have completed Ernest the Chicken", "I must have completed Priest in Peril.", "Level 30 Ranged", "Level 18 Slayer", "Level 19 Crafting", "Level 35 Woodcutting")
    }
}
