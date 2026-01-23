package content.region.misthalin.varrock.quest.surok

import content.region.misthalin.varrock.quest.surok.plugin.WhatLiesBelowListener
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import shared.consts.Components
import shared.consts.Items
import shared.consts.Quests

/**
 * Represents the What lie below quest.
 * @author Vexia
 */
@Initializable
class WhatLiesBelow : Quest(Quests.WHAT_LIES_BELOW, 136, 135, 1) {
    private val requirements = BooleanArray(4)

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        when (stage) {
            0 -> {
                line(player, "I can start this quest by speaking to !!Rat Burgiss?? on the", line++)
                line(player, "road south of !!Varrock??.", line++)
                line(player, "Before I begin I will need to:", line++)
                line(player, "Have level 35 !!Runecrafting??.", line++, hasLevelStat(player, Skills.RUNECRAFTING, 35))
                line(player, "Be able to defeat a !!level 47 enemy??.", line++)
                line(player, "I need to have completed the !!Rune Mysteries?? quest.", line++, isQuestComplete(player, Quests.RUNE_MYSTERIES))
                line(player, "Have a !!Mining?? level of 42 to use the !!Chaos Tunnel??.", line, hasLevelStat(player, Skills.MINING, 42))
            }

            10 -> {
                line(player, "!!Rat??, a trader in Varrock, has asked me to help him with a task.", line++)
                line(player, "I need to kill !!outlaws?? west of Varrock so that I can collect", line++, true)
                line(player, "5 of !!Rat's papers??.", line++, true)
            }

            20 -> {
                line(player, "!!Rat??, a trader in Varrock, has asked me to help him with a task.", line++)
                line(player, "I need to kill outlaws west of Varrock so that", line++, true)
                line(player, "I can collect 5 of Rat's papers.", line++, true)
                line(player, "I have delivered Rat's folder to him. Perhaps I should speak to him again.", line++, true)
                line(player, "I need to deliver !!Rat's letter?? to !!Surok Magis?? in !!Varrock??.", line++)
            }

            30 -> {
                line(player, "Rat, a trader in Varrock, has asked me to help him with a task.", line++, true)
                line(player, "Surok, a Wizard in Varrock, has asked me to complete a task for him.", line++, true)
                line(player, "I need to kill the outlaws west of Varrock so that", line++, true)
                line(player, "I can collect 5 of Rat's papers.", line++, true)
                line(player, "I have delivered Rat's folder to him.", line++, true)
                line(player, "Perhaps I should speak to him again.", line++, true)
                line(player, "I need to deliver Rat's letter to !!Surok Magis?? in Varrock.", line++, true)
                line(player, "I need to talk to Surok about the secret he has for me.", line++, true)
                line(player, "I need to infuse the !!metal wand?? with !!chaos runes?? at the !!Chaos Altar??.", line++)
                line(player, "I also need to find or buy an empty !!bowl??.", line++)
            }

            40 -> {
                line(player, "Rat, a trader in Varrock, has asked me to help him with a task.", line++, true)
                line(player, "Surok, a Wizard in Varrock, has asked me to complete a task for him.", line++, true)
                line(player, "I need to kill the outlaws west of Varrock so that", line++, true)
                line(player, "I can collect 5 of Rat's papers.", line++, true)
                line(player, "I have delivered Rat's folder to him.", line++, true)
                line(player, "Perhaps I should speak to him again.", line++, true)
                line(player, "I need to deliver Rat's letter to !!Surok Magis?? in Varrock.", line++, true)
                line(player, "I need to talk to Surok about the secret he has for me.", line++, true)
                line(player, "I need to infuse the !!metal wand?? with !!chaos runes?? at the !!Chaos Altar??.", line++)
                line(player, "I also need to find or buy an empty !!bowl??.", line++)
            }

            50 -> {
                line(player, "Rat, a trader in Varrock, has asked me to help him with a task.", line++, true)
                line(player, "Surok, a Wizard in Varrock, has asked me to complete a task for him.", line++, true)
                line(player, "I need to kill the outlaws west of Varrock so that", line++, true)
                line(player, "I can collect 5 of Rat's papers.", line++, true)
                line(player, "I have delivered Rat's folder to him.", line++, true)
                line(player, "Perhaps I should speak to him again.", line++, true)
                line(player, "I need to deliver Rat's letter to !!Surok Magis?? in Varrock.", line++, true)
                line(player, "I need to talk to Surok about the secret he has for me.", line++, true)
                line(player, "I need to infuse the metal wand with chaos runes at the Chaos Altar.", line++, true)
                line(player, "I also need to find or buy an empty bowl.", line++, true)
                line(player, "I need to take the glowing wand I have created back to Surok in Varrock", line++, true)
                line(player, "with an empty bowl.", line++, true)
                line(player, "I need to deliver Surok's letter to !!Rat?? who is waiting for me south", line++, true)
                line(player, "of Varrock.", line++, true)
                line(player, "I should speak to !!Rat?? again;", line++)
                line(player, "he is waiting for me south of Varrock.", line++)
            }

            60 -> {
                line(player, "Rat, a trader in Varrock, has asked me to help him with a task.", line++, true)
                line(player, "Surok, a Wizard in Varrock, has asked me to complete a task for him.", line++, true)
                line(player, "I need to kill the outlaws west of Varrock so that", line++, true)
                line(player, "I can collect 5 of Rat's papers.", line++, true)
                line(player, "I have delivered Rat's folder to him.", line++, true)
                line(player, "Perhaps I should speak to him again.", line++, true)
                line(player, "I need to deliver Rat's letter to !!Surok Magis?? in Varrock.", line++, true)
                line(player, "I need to talk to Surok about the secret he has for me.", line++, true)
                line(player, "I need to infuse the metal wand with chaos runes at the Chaos Altar.", line++, true)
                line(player, "I also need to find or buy an empty bowl.", line++, true)
                line(player, "I need to take the glowing wand I have created back to Surok in Varrock", line++, true)
                line(player, "with an empty bowl.", line++, true)
                line(player, "I need to deliver Surok's letter to !!Rat?? who is waiting for me south", line++, true)
                line(player, "of Varrock.", line++, true)
                line(player, "I should speak to !!Rat?? again; he is waiting for me south of Varrock.", line++, true)
                line(player, "I need to speak to !!Zaff?? of !!Zaff's Staffs?? in Varrock.", line++)
            }

            70 -> {
                line(player, "Rat, a trader in Varrock, has asked me to help him with a task.", line++, true)
                line(player, "Surok, a Wizard in Varrock, has asked me to complete a task for him.", line++, true)
                line(player, "I need to kill the outlaws west of Varrock so that", line++, true)
                line(player, "I can collect 5 of Rat's papers.", line++, true)
                line(player, "I have delivered Rat's folder to him.", line++, true)
                line(player, "Perhaps I should speak to him again.", line++, true)
                line(player, "I need to deliver Rat's letter to !!Surok Magis?? in Varrock.", line++, true)
                line(player, "I need to talk to Surok about the secret he has for me.", line++, true)
                line(player, "I need to infuse the metal wand with chaos runes at the Chaos Altar.", line++, true)
                line(player, "I also need to find or buy an empty bowl.", line++, true)
                line(player, "I need to take the glowing wand I have created back to Surok in Varrock", line++, true)
                line(player, "with an empty bowl.", line++, true)
                line(player, "I need to deliver Surok's letter to !!Rat?? who is waiting for me south", line++, true)
                line(player, "of Varrock.", line++, true)
                line(player, "I should speak to !!Rat?? again; he is waiting for me south of Varrock.", line++, true)
                line(player, "I need to speak to !!Zaff?? of !!Zaff's Staffs?? in Varrock.", line++, true)
                line(player, "I need to tell !!Surok?? in Varrock that he is under arrest.", line++)
            }

            80, 90 -> {
                line(player, "Rat, a trader in Varrock, has asked me to help him with a task.", line++, true)
                line(player, "Surok, a Wizard in Varrock, has asked me to complete a task for him.", line++, true)
                line(player, "I need to kill the outlaws west of Varrock so that", line++, true)
                line(player, "I can collect 5 of Rat's papers.", line++, true)
                line(player, "I have delivered Rat's folder to him.", line++, true)
                line(player, "Perhaps I should speak to him again.", line++, true)
                line(player, "I need to deliver Rat's letter to !!Surok Magis?? in Varrock.", line++, true)
                line(player, "I need to talk to Surok about the secret he has for me.", line++, true)
                line(player, "I need to infuse the metal wand with chaos runes at the Chaos Altar.", line++, true)
                line(player, "I also need to find or buy an empty bowl.", line++, true)
                line(player, "I need to take the glowing wand I have created back to Surok in Varrock", line++, true)
                line(player, "with an empty bowl.", line++, true)
                line(player, "I need to deliver Surok's letter to !!Rat?? who is waiting for me south", line++, true)
                line(player, "of Varrock.", line++, true)
                line(player, "I should speak to !!Rat?? again; he is waiting for me south of Varrock.", line++, true)
                line(player, "I need to speak to !!Zaff?? of !!Zaff's Staffs?? in Varrock.", line++, true)
                line(player, "I need to tell !!Surok?? in Varrock that he is under arrest.", line++, true)
                line(player, "I need to defeat !!King Roald?? in Varrock so that !!Zaff?? can remove", line++, true)
                line(player, "the mind-control spell.", line++, true)
                line(player, "I need to tell !!Rat?? what has happened; he is waiting for me", line++)
                line(player, "south of Varrock.", line++)
            }

            100 -> {
                line(player, "Rat, a trader in Varrock, has asked me to help him with a task.", line++, true)
                line(player, "Surok, a Wizard in Varrock, has asked me to complete a task for him.", line++, true)
                line(player, "I need to kill the outlaws west of Varrock so that", line++, true)
                line(player, "I can collect 5 of Rat's papers.", line++, true)
                line(player, "I have delivered Rat's folder to him.", line++, true)
                line(player, "Perhaps I should speak to him again.", line++, true)
                line(player, "I need to deliver Rat's letter to !!Surok Magis?? in Varrock.", line++, true)
                line(player, "I need to talk to Surok about the secret he has for me.", line++, true)
                line(player, "I need to infuse the metal wand with chaos runes at the Chaos Altar.", line++, true)
                line(player, "I also need to find or buy an empty bowl.", line++, true)
                line(player, "I need to take the glowing wand I have created back to Surok in Varrock", line++, true)
                line(player, "with an empty bowl.", line++, true)
                line(player, "I need to deliver Surok's letter to !!Rat?? who is waiting for me south", line++, true)
                line(player, "of Varrock.", line++, true)
                line(player, "I should speak to !!Rat?? again; he is waiting for me south of Varrock.", line++, true)
                line(player, "I need to speak to !!Zaff?? of !!Zaff's Staffs?? in Varrock.", line++, true)
                line(player, "I need to tell !!Surok?? in Varrock that he is under arrest.", line++, true)
                line(player, "I need to defeat !!King Roald?? in Varrock so that !!Zaff?? can remove", line++, true)
                line(player, "the mind-control spell.", line++, true)
                line(player, "I need to tell !!Rat?? what has happened; he is waiting for me", line++, true)
                line(player, "south of Varrock.", line++, true)
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++)
                line(player, "I have been given information about the !!Chaos Tunnel??.", line++)
                line(player, "!!Zaff?? has given me the !!Beacon ring??.", line++)
            }
        }
    }

    override fun start(player: Player) {
        super.start(player)
        addItem(player, WhatLiesBelowListener.EMPTY_FOLDER)
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        displayQuestItem(player, WhatLiesBelowListener.BEACON_RING)
        drawReward(player, "8,000 Runecrafting XP", ln++)
        drawReward(player, "2,000 Defence XP", ln++)
        drawReward(player, "Beacon ring", ln++)
        drawReward(player, "Knowledge of Chaos Tunnel", ln)

        rewardXP(player, Skills.RUNECRAFTING, 8000.0)
        rewardXP(player, Skills.DEFENCE, 2000.0)

        addItemOrDrop(player, WhatLiesBelowListener.BEACON_RING)
        updateQuestTab(player)
    }

    override fun newInstance(`object`: Any?): Quest = this

    override fun hasRequirements(player: Player): Boolean {
        requirements[0] = getStatLevel(player, Skills.RUNECRAFTING) >= 35
        requirements[1] = false
        requirements[3] = getStatLevel(player, Skills.MINING) >= 42
        requirements[2] = isQuestComplete(player, Quests.RUNE_MYSTERIES)
        return requirements[0] && requirements[2] && requirements[3]
    }

    override fun getConfig(player: Player, stage: Int): IntArray {
        val id = 992
        if (stage >= 40 && stage != 100) {
            return intArrayOf(id, (1 shl 8) + 1)
        }
        if (stage == 0) {
            return intArrayOf(id, 0)
        } else if (stage in 1..99) {
            return intArrayOf(id, 1)
        }
        setVarp(player, 1181, (1 shl 8) + (1 shl 9), true)
        return intArrayOf(id, 502)
    }

    companion object {
        const val BOWL           = Items.BOWL_1923
        const val SIN_KETH_DIARY = Items.SINKETHS_DIARY_11002
        const val EMPTY_FOLDER   = Items.AN_EMPTY_FOLDER_11003
        const val USED_FOLDER    = Items.USED_FOLDER_11006
        const val FULL_FOLDER    = Items.FULL_FOLDER_11007
        const val RATS_LETTER    = Items.RATS_LETTER_11009
        const val SUROKS_LETTER  = Items.SUROKS_LETTER_11010
        const val WAND           = Items.WAND_11012
        const val INFUSED_WAND   = Items.INFUSED_WAND_11013
        const val BEACON_RING    = Items.BEACON_RING_11014
    }
}
