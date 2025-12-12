package content.region.asgarnia.port_sarim.plugin

import content.region.asgarnia.falador.dialogue.AmbassadorSpanfippleDialogue
import content.region.asgarnia.falador.dialogue.ApprenticeWorkmanDialogue
import content.region.asgarnia.falador.dialogue.CassieDialogue
import content.region.asgarnia.falador.dialogue.DrogoDwarfDialogue
import content.region.asgarnia.port_sarim.dialogue.*
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.tools.RandomFunction
import shared.consts.*

class PortSarimPlugin : InteractionListener {

    companion object {
        private val DOORS = intArrayOf(Scenery.CELL_DOOR_9563, Scenery.DOOR_9565)
        private val MONKS_OF_ENTRANA = intArrayOf(NPCs.MONK_OF_ENTRANA_2728, NPCs.MONK_OF_ENTRANA_657, NPCs.MONK_OF_ENTRANA_2729, 2730, NPCs.MONK_OF_ENTRANA_2731, NPCs.MONK_OF_ENTRANA_658)
        private val SEAMAN = intArrayOf(NPCs.CAPTAIN_TOBIAS_376, NPCs.SEAMAN_LORRIS_377, NPCs.SEAMAN_THRESNOR_378)
        private val GUARD_FORCE_CHAT =
            arrayOf(
                "Hmph... heh heh heh...",
                "Mmmm... big pint of beer... kebab...",
                "Mmmmmm... donuts...",
                "Guh.. mwww... zzzzzz...",
            )
    }

    override fun defineListeners() {

        /*
         * Handles taking Ahab's beer from the ground.
         */

        on(Items.AHABS_BEER_6561, IntType.GROUND_ITEM, "take") { player, node ->
            face(player, node)
            animate(player, Animations.HUMAN_MULTI_USE_832)
            player.dialogueInterpreter.open(NPCs.AHAB_2692, findNPC(NPCs.AHAB_2692), false)
            return@on true
        }

        /*
         * Handles paying the fare to the Seaman NPCs.
         */

        on(SEAMAN, IntType.NPC, "pay-fare") { player, node ->
            val npc = node as NPC
            player.dialogueInterpreter.open(npc.id, npc, true)
            return@on true
        }

        /*
         * Handles opening the Wydin store door.
         */

        on(Scenery.DOOR_2069, IntType.SCENERY, "open") { player, node ->
            val needsApron = !inEquipment(player, Items.WHITE_APRON_1005) && player.location.x == 3012

            if (needsApron) {
                player.dialogueInterpreter.open(NPCs.WYDIN_557, true, true)
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }

            return@on true
        }

        /*
         * Handles searching the Wydin banana crate.
         */

        on(Scenery.CRATE_2071, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "There are lots of bananas in the crate.")

            if (!player.getAttribute("wydin-rum", false)) {
                setTitle(player, 2)
                sendOptions(player, "Do you want to take a banana?", "Yes.", "No.")
                addDialogueAction(player) { _, option ->
                    closeDialogue(player)
                    if (option == 2) {
                        if (freeSlots(player) == 0) {
                            sendMessage(player, "Not enough inventory space to take a banana.")
                            return@addDialogueAction
                        }
                        lock(player, 2)
                        animate(player, Animations.HUMAN_MULTI_USE_832)
                        sendMessage(player, "You take a banana.")
                        addItem(player, Items.BANANA_1963)
                    }
                }
            } else {
                if (freeSlots(player) == 0) {
                    sendMessage(player, "Not enough inventory space to take your rum.")
                } else {
                    lock(player, 2)
                    animate(player, Animations.HUMAN_MULTI_USE_832)
                    sendMessage(player, "You find your bottle of rum in amongst the bananas.")
                    addItem(player, Items.KARAMJAN_RUM_431)
                    removeAttributes(player, "wydin-rum", "stashed-rum")
                }
            }

            return@on true
        }

        /*
         * Handles interacting with doors (open or pick-lock).
         */

        on(DOORS, IntType.SCENERY, "open", "pick-lock") { player, _ ->
            when (getUsedOption(player)) {
                "open" -> sendMessage(player, "The door is securely locked.")
                "pick-lock" -> {
                    if (player.location.y <= 3187) {
                        sendMessage(player, "You simply cannot find a way to pick the lock from this side.")
                    } else {
                        sendMessage(player, "The door is securely locked.")
                    }
                }
            }
            return@on true
        }

        /*
         * Handles taking a boat from the Monks of Entrana.
         */

        on(MONKS_OF_ENTRANA, IntType.NPC, "take-boat") { player, node ->
            openDialogue(player, (node as NPC).id, node)
            return@on true
        }

        /*
         * Handles talking to the sleeping guard NPC.
         */

        on(NPCs.GUARD_2704, IntType.NPC, "talk-to") { player, node ->
            sendChat((node as NPC), GUARD_FORCE_CHAT[RandomFunction.random(GUARD_FORCE_CHAT.size)])
            lock(player, 2)
            runTask(player, 1) {
                sendPlayerDialogue(player, "Maybe I should let him sleep.")
            }
            return@on true
        }

        /*
         * Handles attacking Wormbrain NPC.
         */

        on(NPCs.WORMBRAIN_745, IntType.NPC, "attack") { player, node ->
            if (getQuestStage(player, Quests.DRAGON_SLAYER) != 20) {
                sendDialogue(player, "The goblin is already in prison. You have no reason to attack him.")
            } else {
                player.properties.combatPulse.attack(node)
            }
            return@on true
        }

        /*
         * Handles entering the icy cavern.
         */

        on(Scenery.ICY_CAVERN_33174, IntType.SCENERY, "enter") { player, _ ->
            queueScript(player, 1, QueueStrength.SOFT) {
                player.properties.teleportLocation = Location(3056, 9562, 0)
                sendMessage(player, "You leave the icy cavern.")
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        /*
         * Handles exiting the icy cavern.
         */

        on(Scenery.CAVE_33173, IntType.SCENERY, "exit") { player, _ ->
            openDialogue(player, EntranceDialogue())
            return@on true
        }

        /*
         * Handles talking to the npc around port.
         */

        on(NPCs.BELLEMORDE_2942, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, CatBellemordeDialogue(), node.id)
            return@on true
        }

        on(NPCs.GRUM_556, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, GrumDialogue(), node.id)
            return@on true
        }

        on(NPCs.GERRANT_558, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, GerrantDialogue(), node.id)
            return@on true
        }

        on(NPCs.JACK_SEAGULL_2690, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, JackSeagullDialogue(), node.id)
            return@on true
        }

        on(NPCs.LONGBOW_BEN_2691, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, LongbowBenDialogue(), node.id)
            return@on true
        }

        on(NPCs.MALIGNIUS_MORTIFER_2713, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, MaligniusMortiferDialogue(), node.id)
            return@on true
        }

        on(intArrayOf(
            NPCs.MONK_OF_ENTRANA_657,
            NPCs.MONK_OF_ENTRANA_658,
            NPCs.MONK_OF_ENTRANA_2728,
            NPCs.MONK_OF_ENTRANA_2729,
            NPCs.MONK_OF_ENTRANA_2730,
            NPCs.MONK_OF_ENTRANA_2731,
        ), IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, MonkOfEntranaDialogue(), node.id)
            return@on true
        }

        on(NPCs.THAKI_THE_DELIVERY_DWARF_7115, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, ThakiTheDeliveryDwarfDialogue(), node.id)
            return@on true
        }

    }

    private class EntranceDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> {
                    sendDialogueLines(player!!, "STOP! The creatures in this cave are VERY Dangerous. Are you", "sure you want to enter?")
                    stage++
                }
                1 -> {
                    options("Yes, I'm not afraid of death!", "No thanks, I don't want to die!")
                    stage++
                }
                2 -> {
                    end()
                    if (buttonID == 1) {
                        val LOCATION = Location.create(3056, 9555, 0)
                        player!!.properties.teleportLocation = LOCATION
                        sendMessage(player!!, "You venture into the icy cavern.")
                    }
                }
            }

        }
    }
}
