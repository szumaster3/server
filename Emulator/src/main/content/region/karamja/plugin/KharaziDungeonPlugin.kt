package content.region.karamja.plugin

import content.data.items.SkillingTool
import core.api.*
import core.game.dialogue.DialogueFile
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

        on(Scenery.MOSSY_ROCK_24300, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You try to crawl through...")
            player.dialogueInterpreter.sendDialogue(
                "You see that there is a small crevice that you may be able to crawl",
                "through."
            )
            addDialogueAction(player) { p, button ->
                setTitle(player, 2)
                sendDialogueOptions(
                    p,
                    "Crawl though hole?",
                    "Yes, I'll crawl through. I'm very athletic.",
                    "No, I'm a bit claustrophobic."
                )
                if (button >= 2) {
                    teleport(player, Location.create(2772, 9341, 0), TeleportManager.TeleportType.INSTANT)
                    sendMessage(player, "You contort your body to fit the crevice.")
                    sendMessage(player, "You adroitly squeeze, serpent-like, into the crevice.", 1)
                    sendMessage(player, "You find a small narrow tunnel that goes for some distance.", 1)
                    sendMessage(player, "After some time, you find a small cave opening... and walk through.", 2)
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


        on(Scenery.CREVICE_2918, IntType.SCENERY, "look-at", "search") { player, _ ->
            teleport(player, Location.create(2794, 9339, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.CREVICE_2918), "look-at", "search") { _, _ ->
            return@setDest Location.create(2800, 9340, 0)
        }
    }

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
            sendMessage(player, "You only scratch the rock.")
            delayClock(player, Clocks.SKILLING, 3)
            return delayScript(player, 2)
        }

        replaceScenery(boulder.asScenery(), 2918, 5)
        sendMessage(player, "You smash the rock to bits.")
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