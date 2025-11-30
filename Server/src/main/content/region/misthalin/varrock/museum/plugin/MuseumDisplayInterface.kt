package content.region.misthalin.varrock.museum.plugin

import content.data.GameAttributes
import content.region.desert.uzer.quest.golem.TheGolemListener
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.world.map.Location
import shared.consts.*

class MuseumDisplayInterface : InterfaceListener, InteractionListener {

    companion object {
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.VM_TIMELINE_534) { player, _ ->
            val model = getAttribute(player, GameAttributes.MUSEUM_INTERFACE_534_MODEL, 0)
            sendModelOnInterface(player, Components.VM_TIMELINE_534,4, model, 461)
            return@onOpen true
        }

        onClose(Components.VM_TIMELINE_534) { player, _ ->
            removeAttribute(player, GameAttributes.MUSEUM_INTERFACE_534_MODEL)
            removeAttribute(player, GameAttributes.MUSEUM_ARTIFACT_MODEL)
            return@onClose true
        }

        onOpen(Components.VM_DIGSITE_528) { _, _ ->
            return@onOpen true
        }
    }

    override fun defineListeners() {

        /*
         * Handles sending description to display cases.
         * Author: Bonesy.
         */

        MuseumData.displayCases.forEach { (sceneryId, description) ->
            on(sceneryId, IntType.SCENERY, "study") { player, node ->
                val scenery = node as core.game.node.scenery.Scenery
                val modelId = scenery.definition.modelIds?.firstOrNull() ?: return@on true
                setAttribute(player, GameAttributes.MUSEUM_INTERFACE_534_MODEL, modelId)
                openInterface(player, Components.VM_TIMELINE_534)
                sendString(player, description.joinToString("<br>"), Components.VM_TIMELINE_534, 2)
                val index = MuseumData.displayCases.keys.indexOf(sceneryId) + 1
                sendString(player, index.toString(), Components.VM_TIMELINE_534, 85)
                return@on true
            }
        }

        /*
         * Handles study the display case (number 30).
         */

        on(Scenery.DISPLAY_CASE_24550, IntType.SCENERY, "study") { player, node ->
            val n = node as core.game.node.scenery.Scenery
            val model = n.definition.modelIds!![0]
            setAttribute(player, GameAttributes.MUSEUM_INTERFACE_534_MODEL, model)
            openInterface(player, Components.VM_TIMELINE_534)
            sendString(player, "Item removed for cleaning.", Components.VM_TIMELINE_534, 2)
            return@on true
        }

        on(Scenery.DISPLAY_CASE_24627, IntType.SCENERY, "open") { player, _ ->
            if (!player.inventory.containsAtLeastOneItem(Items.DISPLAY_CABINET_KEY_4617)) {
                sendMessage(player, "The cabinet is locked.")
                return@on true
            }
            if (inInventory(player, Items.STATUETTE_4618) ||
                inBank(player, Items.STATUETTE_4618) ||
                player.getAttribute("the-golem:placed-statuette", false)
            ) {
                sendMessage(player, "You have already taken the statuette.")
                return@on true
            }
            playAudio(player, Sounds.VM_DISPLAYCASE_OPEN_3650)
            addItemOrDrop(player, Items.STATUETTE_4618, 1)
            sendItemDialogue(player, Items.STATUETTE_4618, "You open the cabinet and retrieve the statuette.")
            TheGolemListener.updateVarps(player)
            return@on true
        }

        /*
         * Handles specimen cleaning items used on display cases.
         */

        onUseWith(IntType.SCENERY, MuseumData.artifactItems, Scenery.DISPLAY_CASE_24550) { player, used, with ->
            when (with.location) {
                Location(3255, 3453, 1) -> {
                    if (used.id == Items.DISPLAY_CABINET_KEY_4617) {
                        sendMessage(player, "You have already taken the statuette.")
                        return@onUseWith true
                    }
                }
                in MuseumData.displayCasesBaseFloor.keys -> {
                    val (requiredItem, varbit) = MuseumData.displayCasesBaseFloor[with.location]!!
                    if (used.id == requiredItem) {
                        removeItem(player, used.asItem(), Container.INVENTORY)
                        setVarbit(player, varbit, 1, true)
                    }
                }
            }
            val itemName = getItemName(used.id).lowercase()
            if (used.id != Items.DISPLAY_CABINET_KEY_4617) {
                sendMessages(player, "You carefully place the $itemName in the display and update.", "the information.")
            }
            return@onUseWith true
        }

        /*
         * Handles use of Pottery on display case. (number 22)
         */

        onUseWith(IntType.SCENERY, Items.POTTERY_11178, Scenery.DISPLAY_CASE_24543) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_39_3649, 1, true)
            }
            return@onUseWith true
        }

        /*
         * Handles study the display case (number 24).
         */

        on(intArrayOf(Scenery.DISPLAY_CASE_24639, Scenery.DISPLAY_CASE_24551), IntType.SCENERY, "study") { player, node ->
            val n = node as core.game.node.scenery.Scenery
            val model = n.definition.modelIds!![0]
            val arrav = getVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_24_5394)
            setAttribute(player, GameAttributes.MUSEUM_INTERFACE_534_MODEL, model)
            openInterface(player, Components.VM_TIMELINE_534)
            sendString(
                player,
                if (arrav == 1) {
                    MuseumData.text24A.joinToString("<br>")
                } else {
                    MuseumData.text24B.joinToString("<br>")
                },
                Components.VM_TIMELINE_534,
                2,
            )
            sendString(player, "24", Components.VM_TIMELINE_534, 85)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        /*
         * Handles study the King Lathas painting.
         */
        setDest(IntType.SCENERY, intArrayOf(Scenery.PAINTING_24620), "study") { _, _ ->
            return@setDest Location(3257, 3454, 2)
        }
    }
}
