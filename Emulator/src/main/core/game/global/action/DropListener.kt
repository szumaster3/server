package core.game.global.action

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import content.global.skill.summoning.pet.Pets
import content.minigame.mta.handlers.ProgressHat.thresholds
import core.api.*
import core.api.ui.closeDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.combat.graves.GraveController
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.info.login.PlayerParser
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.command.sets.SpawnCommandSet
import core.game.system.config.ItemConfigParser
import core.tools.SystemLogger
import org.rs.consts.Items
import org.rs.consts.Sounds

class DropListener : InteractionListener {
    override fun defineListeners() {
        on(IntType.ITEM, "drop", "destroy", "dissolve", handler = ::handleDropAction)
    }

    companion object {
        private const val DROP_COINS_SOUND = Sounds.EYEGLO_COIN_10
        private const val DROP_ITEM_SOUND = Sounds.PUT_DOWN_2739
        private const val DESTROY_ITEM_SOUND = Sounds.DESTROY_OBJECT_2381

        @JvmStatic
        fun drop(
            player: Player,
            item: Item,
        ): Boolean = handleDropAction(player, item)

        private fun handleDropAction(
            player: Player,
            node: Node,
        ): Boolean {
            val option = getUsedOption(player)
            var item = node as? Item ?: return false
            if (option == "drop") {
                if (player.rights == Rights.ADMINISTRATOR) {
                    val item = item.id
                    val location = "{1,${player.location.x},${player.location.y},${player.location.z},100}-"

                    val logs = JsonObject().apply {
                        addProperty("item_id", item.toString())
                        addProperty("loc_data", location)
                    }

                    val logPath = "data/logs/ground_spawns.json"
                    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

                    try {
                        val data = SpawnCommandSet.readJsonArrayFromFile(logPath, gson)
                        data.add(logs)
                        SpawnCommandSet.writeJsonArrayToFile(logPath, data, gson)

                        sendMessage(player, "ground spawn saved to [${core.tools.DARK_RED}$logPath</col>].")
                    } catch (e: Exception) {
                        SystemLogger.logMS("An error occurred while logging item drop: ${e.message}")
                    }
                }

                if (Pets.forId(item.id) != null) {
                    player.familiarManager.summon(item, true, true)
                    return true
                }
                if (GraveController.hasGraveAt(player.location)) {
                    sendMessage(player, "You cannot drop items on top of graves!")
                    return false
                }
                if (player.houseManager.isInHouse(player) &&
                    player.houseManager.isBuildingMode &&
                    player.rights != Rights.ADMINISTRATOR
                ) {
                    sendMessage(player, "You cannot drop items while in building mode.")
                    return false
                }
                if (getAttribute(player, "equipLock:${node.id}", 0) > getWorldTicks()) return false

                queueScript(player, strength = QueueStrength.SOFT) {
                    if (player.inventory.replace(null, item.slot) != item) return@queueScript stopExecuting(player)
                    val droppedItem = item.dropItem
                    if (droppedItem.id == Items.COINS_995) {
                        playAudio(player, DROP_COINS_SOUND)
                    } else {
                        playAudio(
                            player,
                            DROP_ITEM_SOUND,
                        )
                    }
                    GroundItemManager.create(droppedItem, player.location, player)
                    setAttribute(player, "droppedItem:${droppedItem.id}", getWorldTicks() + 2)
                    PlayerParser.save(player)
                    return@queueScript stopExecuting(player)
                }
            } else if (option == "destroy" ||
                option == "dissolve" ||
                item.definition.handlers.getOrDefault(ItemConfigParser.DESTROY, false) as Boolean
            ) {
                player.dialogueInterpreter.sendDestroyItem(item.id, item.name)
                addDialogueAction(player) { player, button ->
                    if (button == 3) {
                        if (item.name.contains("progress hat", true)) {
                            val activityData = player.getSavedData().activityData
                            thresholds.keys.forEach { activityData.decrementPizazz(it, 0) }
                            player.dialogueInterpreter.sendDialogue(
                                "The hat whispers as you destroy it. You can get another from the",
                                "Entrance Guardian.",
                            )
                            return@addDialogueAction
                        }
                        if (removeItem(player, item)) {
                            playAudio(player, DESTROY_ITEM_SOUND)
                        }
                        return@addDialogueAction
                    } else {
                        closeDialogue(player)
                    }
                }
            }
            return true
        }
    }
}
