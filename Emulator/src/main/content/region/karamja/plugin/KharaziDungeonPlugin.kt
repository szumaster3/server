package content.region.karamja.plugin

import content.data.items.SkillingTool
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.global.action.DoorActionHandler
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class KharaziDungeonPlugin : InteractionListener {

    val BOULDER = intArrayOf(Scenery.BOULDER_2919, Scenery.BOULDER_2920, Scenery.BOULDER_2921)

    override fun defineListeners() {

        /*
         * Handles enter to the Kharazi dungeon.
         */

        on(intArrayOf(Scenery.MOSSY_ROCK_24300,Scenery.ROCKS_2902), IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the rocks but you see nothing significant...")
            runTask(player, 3) {
                sendDialogueLines(
                    player,
                    "You see that there is a small crevice that you may be able to crawl",
                    "through. Would you like to try to crawl through, it looks quite an",
                    "enclosed area?"
                )
                sendMessage(player, "...at first.", 1)
                addDialogueAction(player) { player, _ ->
                    player.dialogueInterpreter.actions.clear().also {
                        openDialogue(player, SearchRock())
                    }
                }
            }
            return@on true
        }

        /*
         * Handles searching the bookcase (Clue scenery?)
         */

        on(Scenery.BOOKCASE_393, IntType.SCENERY, "search") { player, _ ->
            sendDialogue(player, "This bamboo book shelf doesn't house many books, it doesn't look as if the Shaman has much opportunity to read at the moment in any case.")
            return@on true
        }

        on(Scenery.TABLE_2906, IntType.SCENERY, "look-at", "search") { player, _ ->
            val option = getUsedOption(player)
            when (option) {
                "look-at" -> sendDialogue(player, "A crudely constructed makeshift table made from various pieces of wood. You see a piece of screwed up paper on the table top.")
                else -> {
                    sendMessage(player, "You start searching the table...")
                    if (inInventory(player, Items.A_SCRIBBLED_NOTE_718)) {
                        sendItemDialogue(player, Items.A_SCRIBBLED_NOTE_718, "After some time you find a scrumpled up piece of paper. It looks like rubbish.")
                    } else {
                        sendDialogue(player,"You find a scrap of paper with what looks like nonsense written on it.")
                        addItemOrDrop(player, Items.A_SCRIBBLED_NOTE_718, 1)
                    }
                }
            }
            return@on true
        }

        /*
         * Handles shortcut via bookcase near old gate.
         */

        on(Scenery.BOOKCASE_2911, IntType.SCENERY, "search", "look") { player, n ->
            val option = getUsedOption(player)
            when (option) {
                "look" -> sendDialogue(player, "This bamboo book shelf doesn't house many books, it doesn't look as if the Shaman has much opportunity to read at the moment in any case.")
                else -> openDialogue(player, NorthEasternBookcase())
            }
            return@on true
        }

        /*
         * Handles mining the boulders to get to the death wing spawn.
         */

        defineInteraction(
            IntType.SCENERY,
            BOULDER,
            "smash-to-bits",
            persistent = true,
            allowedDistance = 1,
            handler = ::handleSmash,
        )
        /*
         * Handles exit from old gate location.
         */

        on(Scenery.JAGGED_WALL_2926, IntType.SCENERY, "look") { player, _ ->
            sendDialogue(player, "It looks like this room was once sealed, but the top wall fell down. If you're feeling particularly agile, you could try jumping over it.")
            return@on true
        }

        /*
         * Handles reading the scribbled note.
         */

        on(Items.A_SCRIBBLED_NOTE_718, IntType.ITEM, "read") { player, _ ->
            openDialogue(player, ScribbledNote())
            return@on true
        }

        /*
         * Handles exit from old gate location.
         */

        on(Scenery.CREVICE_2918, IntType.SCENERY, "look-at", "search") { player, _ ->
            teleport(player, Location.create(2794, 9339, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles interaction with ancient gates.
         */

        on(intArrayOf(Scenery.ANCIENT_GATE_2922,Scenery.ANCIENT_GATE_2923), IntType.SCENERY, "open") { player, node ->
            if(player.location.y <= 9313)
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            else
                openDialogue(player, AncientGate(), node)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.CREVICE_2918), "look-at", "search") { _, _ ->
            return@setDest Location.create(2800, 9340, 0)
        }
    }

    /**
     * Searching the north-eastern bookcase (Legends' Quests).
     */
    inner class NorthEasternBookcase : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> {
                    player!!.dialogueInterpreter.sendDialogue("After perusing the empty bookcase for a while, you decide to check", "behind it. There's a crevice in the wall! You might just be able to", "force your way through if you were in any way athletic.")
                    stage++
                }

                1 -> {
                    setTitle(player!!, 2)
                    sendDialogueOptions(player!!, "Would you like to squeeze into this crevice?", "Yes please!", "No thanks!")
                    stage++
                }

                2 -> {
                    end()
                    if (buttonID == 1) {
                        animate(player!!, Animations.HUMAN_CRAWLS_844)
                        teleport(player!!, Location.create(2800, 9340, 0), TeleportManager.TeleportType.INSTANT, 1)
                        sendMessage(player!!, "You successfully squeeze through the crevice into a small tunnel.")
                    } else {
                        sendMessage(player!!, "You decide not to squeeze yourself into that ridiculously small crevice.")
                    }
                }
            }
        }
    }

    /**
     * Handles opening the second ancient gate (base for Legends' Quests).
     */
    inner class AncientGate : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> {
                    sendDialogueLines(player!!,"Two huge metal doors bar the way further... There is an intense and", "unpleasant feeling from this place and as you peer through the", "cracks in the door you can see why.")
                    stage++
                }
                1 -> {
                    sendDialogueLines(player!!,"You see dark, shadowy shapes flapping around in the", "still, dark air...")
                    stage++
                }
                2 -> {
                    sendDialogueLines(player!!,"You push the doors... They're quite stiff... They won't budge with a ", "normal push. Do you want to try to force them open with brute", "strength?")
                    stage++
                }
                3 -> {
                    setTitle(player!!, 2)
                    sendDialogueOptions(player!!, "Use brute strength on the gates?", "Yes, I'm very strong, I'll force them open.", "No, I'm having second thoughts.")
                    stage++
                }
                4 -> {
                    if (buttonID == 1) {
                        sendDialogue(player!!, "You ripple your muscles and prepare to exert yourself...")
                        stage++
                    } else {
                        sendMessage(player!!, "You decide against forcing the doors.")
                    }
                }
                5 -> {
                    sendChat(player!!, "Hup!")
                    sendDialogue(player!!, "You brace yourself against the doors...")
                    stage++
                }
                6 -> {
                    sendChat(player!!, "Urghhhhh!")
                    sendDialogue(player!!, "You start to force the doors open...")
                    stage++
                }
                7 -> {
                    sendChat(player!!, "Arghhhhhhh!")
                    sendDialogue(player!!, "You push and push...")
                    stage++
                }
                8 -> {
                    sendChat(player!!, "Shhhhhhhshshehshsh")
                    sendDialogue(player!!, "...and you just manage to force the doors open slightly, just enough to force yourself through.")
                    stage++
                }
                9 -> {
                    end()
                    val node = getScenery(Location(2810, 9314, 0))
                    DoorActionHandler.handleAutowalkDoor(player!!, node!!.asScenery())
                }
            }
        }
    }

    inner class ScribbledNote : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> {
                    sendDialogueLines(player!!,"You try your best to decode the writing.", "This is what you make out...")
                    stage++
                }
                1 -> {
                    sendItemDialogue(player!!, Items.A_SCRIBBLED_NOTE_718 , "I fear that the spirit of an ancient one resides within me and uses me... I am too weak to cast the curse myself and fight the beast within.")
                    stage++
                }
                2 -> {
                    sendItemDialogue(player!!, Items.A_SCRIBBLED_NOTE_718 , "Day 3 ...my last hope is that someone will read this and aid me... I am undone and I fear....")
                    stage++
                }
                3 -> {
                    sendDialogueLines(player!!,"The writing trails off at this point.")
                    stage++
                }
                4 -> {
                    end()
                }
            }
        }
    }

    inner class SearchRock : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> {
                    setTitle(player!!, 2)
                    sendDialogueOptions(player!!, "Crawl into hole?", "Yes, I'll crawl through. I'm very athletic.", "No, I'm pretty scared of enclosed areas.")
                    stage++
                }
                1 -> {
                    end()
                    sendMessage(player!!, "You try to crawl through...")
                    if (buttonID == 1) {
                        /* Fail:
                         * sendMessage(player, "You get cramped into a tiny space and start to suffocate.")
                         * sendMessage(player, "You wriggle and wriggle but you cannot get out.")
                         * sendMessage(player, "Eventually you manage to break free.")
                         * sendMessage(player, "But you scrape yourself very badly as you force your way out.")
                         * sendMessage(player, "... and you're totally exhausted from the experience.")
                         */

                        // Success:
                        teleport(player!!, Location.create(2772, 9341, 0), TeleportManager.TeleportType.INSTANT)
                        sendMessage(player!!, "You contort your body to fit the crevice.")
                        sendMessage(player!!, "You adroitly squeeze serpent like into the crevice.", 1)
                        sendMessage(player!!, "You find a small narrow tunnel that goes for some distance.", 1)
                        sendMessage(player!!, "After some time, you find a small cave opening... and walk through.", 2)
                    } else {
                        sendDialogue(player!!, "You decide against forcing yourself into the tiny crevice. And realise that you have much better things to do. Like visit inns and mine ore.")
                    }
                }
            }
        }
    }

    fun handleSmash(player: Player, boulder: Node, state: Int): Boolean {
        val pickaxe = SkillingTool.getPickaxe(player)

        if (pickaxe == null) {
            sendMessage(player, "You do not have a pickaxe to use.")
            return true
        }

        if (!finishedMoving(player) || !clockReady(player, Clocks.SKILLING)) {
            clearScripts(player)
            return true
        }

        if (freeSlots(player) == 0) {
            sendMessage(player, "Your inventory is too full to hold any more rocks.")
            return true
        }


        if (state == 0) {
            sendMessage(player, "You swing your pickaxe at the rock.")
            animate(player, pickaxe.animation)
            return delayScript(player, 2)
        }

        animate(player, pickaxe.animation)
        if (!checkReward(player, pickaxe)) {
            sendMessage(player, "You only succeed in scratching the rock.")
            sendMessage(player, "The pick clangs heavily against the rock face and the vibrations rattle your nerves.")
            delayClock(player, Clocks.SKILLING, 3)
            return delayScript(player, 2)
        }

        replaceScenery(boulder.asScenery(), 2918, 5)
        sendMessage(player, "You manage to smash the rock to bits.")
        sendMessage(player, "You get some rock.")
        addItem(player, Items.ROCK_1480, 1)
        rewardXP(player, Skills.MINING, 35.0)
        resetAnimator(player)

        val target = if (player.location.y > boulder.location.y)
            boulder.location.transform(0, -1, 0)
        else
            boulder.location.transform(0, 1, 0)

        forceMove(player, player.location, target, 0, 90, null, Animations.WALK_819) {
            sendMessage(player, "Another boulder drops down behind you.", 1)
            delayClock(player, Clocks.SKILLING, 3)
            clearScripts(player)
        }

        return delayScript(player, 2)
    }

    /**
     * Checks whether the boulder strike was successful.
     */
    private fun checkReward(player: Player, tool: SkillingTool): Boolean {
        val level = 1 + getDynLevel(player, Skills.MINING) + getFamiliarBoost(player, Skills.MINING)
        val hostRatio = Math.random() * (100.0 * 0.1)
        var toolRatio = tool.ratio
        val clientRatio = Math.random() * ((level - 1) * (1.0 + toolRatio))
        return hostRatio < clientRatio
    }

}