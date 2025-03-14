package content.region.misthalin.quest.surok

import content.region.misthalin.quest.surok.handlers.WhatLiesBelowListener
import core.api.*
import core.api.quest.isQuestComplete
import core.api.quest.updateQuestTab
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests

@Initializable
class WhatLiesBelow : Quest(Quests.WHAT_LIES_BELOW, 136, 135, 1) {
    private val requirements = BooleanArray(4)

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        when (stage) {
            0 ->
                line(
                    player,
                    "<blue>I can start this quest by speaking to <red>Rat Burgiss <blue>on the<n><blue>road south of <red>Varrock.<n><blue>Before I begin I will need to:<n>" +
                        getRequiredMessage(
                            player,
                        ),
                    11,
                )

            10 ->
                line(
                    player,
                    "<red>Rat<blue>, a trader in Varrock, has asked me to help him with a task.<n><blue>I need to kill<red> outlaws <blue>west of Varrock so that I can collect 5 of <n><blue>Rat's <red>papers<blue>.",
                    11,
                )

            20 ->
                line(
                    player,
                    "<red>Rat<blue>, a trader in Varrock, has asked me to help him with a task.<n><str>I need to kill outlaws west of Varrock so that I can collect<n><str>5 of Rat's papers.I have delivered Rat's folder to him. Perhaps I<n><str>should speak to him again.<n><blue>I need to deliver <red>Rat's<blue> letter to <red>Surok Magis<n><blue>in <red>Varrock.",
                    11,
                )

            30 ->
                line(
                    player,
                    "<str>Rat, a trader in Varrock, has asked me to help him with a task.<n><str>Surok, a Wizard in Varrock, has asked me to complete a task for him.<n><str>I need to kill the outlaws west of Varrock so that I can collect<n><str>5 of Rat's papers.<str>I have delivered Rat's folder to him. Perhaps I<n><str>should speak to him again.<str>I need to deliver Rat's letter to <n><str>Surok Magis in Varrock. <str>I need to talk to Surok about the<n><str>secret he has for me.<n><blue>I need to infuse the <red>metal wand <blue>with <red>chaos runes <blue>at the <red>Chaos Altar<blue>.<n><blue>I also need<blue>to find or buy an empty <red>bowl.",
                    11,
                )

            40 ->
                line(
                    player,
                    "<str>Rat, a trader in Varrock, has asked me to help him with a task.<n><str>Surok, a Wizard in Varrock, has asked me to complete a task for him.<n><str>I need to kill the outlaws west of Varrock so that I can collect<n><str>5 of Rat's papers.<str>I have delivered Rat's folder to him. Perhaps I<n><str>should speak to him again.<str>I need to deliver Rat's letter to <n><str>Surok Magis in Varrock. <str>I need to talk to Surok about the<n><str>secret he has for me.<n><blue>I need to infuse the <red>metal wand <blue>with <red>chaos runes <blue>at the <red>Chaos Altar<blue>.<n><blue>I also need<blue>to find or buy an empty <red>bowl.",
                    11,
                )

            50 ->
                line(
                    player,
                    "<str>Rat, a trader in Varrock, has asked me to help him with a task.<n><str>Surok, a Wizard in Varrock, has asked me to complete a task for him.<n><str>I need to kill the outlaws west of Varrock so that I can collect<n><str>5 of Rat's papers.<str>I have delivered Rat's folder to him. Perhaps I<n><str>should speak to him again.<str>I need to deliver Rat's letter to <n><str>Surok Magis in Varrock. <str>I need to talk to Surok about the<n><str>secret he has for me.<n><str>I need to infuse the <str>metal wand <str>with chaos runes <str>at the <str>Chaos Altar<str>.<n><str>I also need<str> to find or buy an empty <str>bowl.<n><str>I need to infuse the metal wand with chaos runes at the Chaos Altar.<n><str>I also need to find or buy an empty bowl.<n><str>I need to take the glowing wand I have created back to Surok in Varrock<n><str>with an empty bowl.<n><str>I need to deliver Surok's letter to Rat who is waiting for me south<n><blue>of Varrock. <blue>I should speak to <red>Rat<blue> again; he is waiting for me <n><blue>south of Varrock",
                    11,
                )

            60 ->
                line(
                    player,
                    "<str>Rat, a trader in Varrock, has asked me to help him with a task.<n><str>Surok, a Wizard in Varrock, has asked me to complete a task for him.<n><str>I need to kill the outlaws west of Varrock so that I can collect<n><str>5 of Rat's papers.<str>I have delivered Rat's folder to him. Perhaps I<n><str>should speak to him again.<str>I need to deliver Rat's letter to <n><str>Surok Magis in Varrock. <str>I need to talk to Surok about the<n><str>secret he has for me.<n><str>I need to infuse the <str>metal wand <str>with chaos runes <str>at the <str>Chaos Altar<str>.<n><str>I also need<str> to find or buy an empty <str>bowl.<n><str>I need to infuse the metal wand with chaos runes at the Chaos Altar.<n><str>I also need to find or buy an empty bowl.<n><str>I need to take the glowing wand I have created back to Surok in Varrock<n><str>with an empty bowl.<n><str>I need to deliver Surok's letter to Rat who is waiting for me south<n><str>of Varrock.<str>I should speak to Rat again; he is waiting for me <n><str>south of Varrock<n><blue>I need to speak to <red>Zaff <blue>of <red>Zaff's Staffs <blue>in Varrock.",
                    11,
                )

            70 ->
                line(
                    player,
                    "<str>Rat, a trader in Varrock, has asked me to help him with a task.<n><str>Surok, a Wizard in Varrock, has asked me to complete a task for him.<n><str>I need to kill the outlaws west of Varrock so that I can collect<n><str>5 of Rat's papers.<str>I have delivered Rat's folder to him. Perhaps I<n><str>should speak to him again.<str>I need to deliver Rat's letter to <n><str>Surok Magis in Varrock. <str>I need to talk to Surok about the<n><str>secret he has for me.<n><str>I need to infuse the <str>metal wand <str>with chaos runes <str>at the <str>Chaos Altar<str>.<n><str>I also need<str> to find or buy an empty <str>bowl.<n><str>I need to infuse the metal wand with chaos runes at the Chaos Altar.<n><str>I also need to find or buy an empty bowl.<n><str>I need to take the glowing wand I have created back to Surok in Varrock<n><str>with an empty bowl.<n><str>I need to deliver Surok's letter to Rat who is waiting for me south<n><str>of Varrock.<str>I should speak to Rat again; he is waiting for me <n><str>south of Varrock<n><str>I need to speak to Zaff of Zaff's Staffs in Varrock.<n><blue>I need to tell <red>Surok <blue>in Varrock that he is under arrest.",
                    11,
                )

            80 ->
                line(
                    player,
                    "<str>Rat, a trader in Varrock, has asked me to help him with a task.<n><str>Surok, a Wizard in Varrock, has asked me to complete a task for him.<n><str>I need to kill the outlaws west of Varrock so that I can collect<n><str>5 of Rat's papers.<str>I have delivered Rat's folder to him. Perhaps I<n><str>should speak to him again.<str>I need to deliver Rat's letter to <n><str>Surok Magis in Varrock. <str>I need to talk to Surok about the<n><str>secret he has for me.<n><str>I need to infuse the <str>metal wand <str>with chaos runes <str>at the <str>Chaos Altar<str>.<n><str>I also need<str> to find or buy an empty <str>bowl.<n><str>I need to infuse the metal wand with chaos runes at the Chaos Altar.<n><str>I also need to find or buy an empty bowl.<n><str>I need to take the glowing wand I have created back to Surok in Varrock<n><str>with an empty bowl.<n><str>I need to deliver Surok's letter to Rat who is waiting for me south<n><str>of Varrock.<str>I should speak to Rat again; he is waiting for me <n><str>south of Varrock<n><str>I need to speak to Zaff of Zaff's Staffs in Varrock.<n><str>I need to tell Surok in Varrock that he is under arrest.<n><str>I need to defeat King Roald in Varrock so that Zaff can remove the<n><str>mind-control spell.<n><blue>I need to tell <red>Rat <blue>what has happened; he is waiting for me<n><blue>south of Varrock.",
                    11,
                )

            90 ->
                line(
                    player,
                    "<str>Rat, a trader in Varrock, has asked me to help him with a task.<n><str>Surok, a Wizard in Varrock, has asked me to complete a task for him.<n><str>I need to kill the outlaws west of Varrock so that I can collect<n><str>5 of Rat's papers.<str>I have delivered Rat's folder to him. Perhaps I<n><str>should speak to him again.<str>I need to deliver Rat's letter to <n><str>Surok Magis in Varrock. <str>I need to talk to Surok about the<n><str>secret he has for me.<n><str>I need to infuse the <str>metal wand <str>with chaos runes <str>at the <str>Chaos Altar<str>.<n><str>I also need<str> to find or buy an empty <str>bowl.<n><str>I need to infuse the metal wand with chaos runes at the Chaos Altar.<n><str>I also need to find or buy an empty bowl.<n><str>I need to take the glowing wand I have created back to Surok in Varrock<n><str>with an empty bowl.<n><str>I need to deliver Surok's letter to Rat who is waiting for me south<n><str>of Varrock.<str>I should speak to Rat again; he is waiting for me <n><str>south of Varrock<n><str>I need to speak to Zaff of Zaff's Staffs in Varrock.<n><str>I need to tell Surok in Varrock that he is under arrest.<n><str>I need to defeat King Roald in Varrock so that Zaff can remove the<n><str>mind-control spell.<n><blue>I need to tell <red>Rat <blue>what has happened; he is waiting for me<n><blue>south of Varrock.",
                    11,
                )

            100 ->
                line(
                    player,
                    "<str>Rat, a trader in Varrock, has asked me to help him with a task.<n><str>Surok, a Wizard in Varrock, has asked me to complete a task for him.<n><str>I need to kill the outlaws west of Varrock so that I can collect<n><str>5 of Rat's papers.<str>I have delivered Rat's folder to him. Perhaps I<n><str>should speak to him again.<str>I need to deliver Rat's letter to <n><str>Surok Magis in Varrock. <str>I need to talk to Surok about the<n><str>secret he has for me.<n><str>I need to infuse the <str>metal wand <str>with chaos runes <str>at the <str>Chaos Altar<str>.<n><str>I also need<str> to find or buy an empty <str>bowl.<n><str>I need to infuse the metal wand with chaos runes at the Chaos Altar.<n><str>I also need to find or buy an empty bowl.<n><str>I need to take the glowing wand I have created back to Surok in Varrock<n><str>with an empty bowl.<n><str>I need to deliver Surok's letter to Rat who is waiting for me south<n><str>of Varrock.<str>I should speak to Rat again; he is waiting for me <n><str>south of Varrock<n><str>I need to speak to Zaff of Zaff's Staffs in Varrock.<n><str>I need to tell Surok in Varrock that he is under arrest.<n><str>I need to defeat King Roald in Varrock so that Zaff can remove the<n><str>mind-control spell.<n><str>I need to tell Rat what has happened; he is waiting for me<n><str>south of Varrock.<n><n><col=FF0000>QUEST COMPLETE!<n><blue>I have been given information about the <red>Chaos Tunnel<blue>.<n><blue>Zaff has given me the <red>Beacon ring<blue>.",
                    11,
                )
        }
    }

    override fun start(player: Player) {
        super.start(player)
        addItem(player, WhatLiesBelowListener.EMPTY_FOLDER)
    }

    override fun finish(player: Player) {
        super.finish(player)
        sendString(player, "8,000 Runecrafting XP", Components.QUEST_COMPLETE_SCROLL_277, 8 + 2)
        sendString(player, "2,000 Defence XP", Components.QUEST_COMPLETE_SCROLL_277, 9 + 2)
        sendString(player, "Beacon ring", Components.QUEST_COMPLETE_SCROLL_277, 10 + 2)
        sendString(player, "Knowledge of Chaos Tunnel", Components.QUEST_COMPLETE_SCROLL_277, 11 + 2)
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, WhatLiesBelowListener.BEACON_RING, 235)
        rewardXP(player, Skills.RUNECRAFTING, 8000.0)
        rewardXP(player, Skills.DEFENCE, 2000.0)
        updateQuestTab(player)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    private fun getRequiredMessage(player: Player): String {
        hasRequirements(player)
        var s = ""
        for (i in requirements.indices) {
            var l = REQUIREMENTS_STRING[i]
            if (requirements[i]) {
                l = l.replace("<blue>", "").replace("<red>", "").trim { it <= ' ' }
            }
            s += (if (requirements[i]) "<str>" else "") + l + "<n>"
        }
        return s
    }

    override fun hasRequirements(player: Player): Boolean {
        requirements[0] = getStatLevel(player, Skills.RUNECRAFTING) >= 35
        requirements[1] = false
        requirements[3] = getStatLevel(player, Skills.MINING) >= 42
        requirements[2] = isQuestComplete(player, Quests.RUNE_MYSTERIES)
        return requirements[0] && requirements[2] && requirements[3]
    }

    override fun getConfig(
        player: Player,
        stage: Int,
    ): IntArray {
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
        private val REQUIREMENTS_STRING =
            arrayOf(
                "<blue>Have level 35 <red>Runecrafting.",
                "<blue>Be able to defeat a <red>level 47 enemy.",
                "<blue>I need to have completed the <red>Rune Mysteries <blue>quest.",
                "<blue>Have a <red>Mining <blue>level of 42 to use the <red>Chaos Tunnel.",
            )

        @JvmField
        val BOWL: Item = Item(Items.BOWL_1923)

        @JvmField
        val SIN_KETH_DIARY: Item = Item(Items.SINKETHS_DIARY_11002)

        @JvmField
        val EMPTY_FOLDER: Item = Item(Items.AN_EMPTY_FOLDER_11003)

        @JvmField
        val USED_FOLDER: Item = Item(Items.USED_FOLDER_11006)

        @JvmField
        val FULL_FOLDER: Item = Item(Items.FULL_FOLDER_11007)

        @JvmField
        val RATS_PAPER: Item = Item(Items.RATS_PAPER_11008)

        @JvmField
        val RATS_LETTER: Item = Item(Items.RATS_LETTER_11009)

        @JvmField
        val SUROKS_LETTER: Item = Item(Items.SUROKS_LETTER_11010)

        @JvmField
        val WAND: Item = Item(Items.WAND_11012)

        @JvmField
        val INFUSED_WAND: Item = Item(Items.INFUSED_WAND_11013)

        @JvmField
        val BEACON_RING: Item = Item(Items.BEACON_RING_11014)
    }
}
