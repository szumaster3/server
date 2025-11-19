package content.global.activity.warriors_guild.plugin

import content.global.activity.warriors_guild.dialogue.ClaimTokenDialogue
import content.global.activity.warriors_guild.plugin.rooms.CatapultRoomPlugin
import core.api.*
import core.game.component.Component
import core.game.container.impl.EquipmentContainer
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.global.action.DoorActionHandler.handleDoor
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import shared.consts.*

class WarriorsGuildPlugin: InteractionListener {
    private val warriorGuildDoors = intArrayOf(Scenery.DOOR_15653, Scenery.DOOR_1530)
    override fun defineListeners() {
        on(Items.GROUND_ASHES_8865, IntType.ITEM, "dust-hands") { player, node ->
            val inShotputArea = withinDistance(player, Location(2861, 3553, 1), 1) ||
                    withinDistance(player, Location(2861, 3547, 1), 1)

            if (!inShotputArea) {
                sendMessage(player, "You may only dust your hands while in the shotput throwing areas.")
                return@on true
            }

            val item = node as Item
            if (removeItem(player, item)) {
                sendMessage(player, "You dust your hands with the finely ground ash.")
                setAttribute(player, "hand_dust", true)
            }

            return@on true
        }

        onEquip(Items.DEFENSIVE_SHIELD_8856) { player, node ->
            if (node is Item) {
                if (player.location != CatapultRoomPlugin.TARGET) {
                    sendMessage(player,
                        "You may not equip this shield outside the target area in the Warrior's Guild.",
                    )
                    return@onEquip false
                }
                if (player.equipment[EquipmentContainer.SLOT_WEAPON] != null) {
                    sendDialogueLines(player,
                        "You will need to make sure your sword hand is free to equip this",
                        "shield.",
                    )
                    return@onEquip false
                }
                player.interfaceManager.removeTabs(2, 3, 5, 6, 7, 11, 12)
                player.interfaceManager.openTab(4, Component(Components.WARGUILD_DEFENCE_MINI_411))
                player.interfaceManager.setViewedTab(4)
                player.interfaceManager.open(Component(Components.WARGUILD_DEFENCE_410))
                return@onEquip true
            }

            return@onEquip false
        }

        onUnequip(Items.DEFENSIVE_SHIELD_8856) { player, _ ->
            player.interfaceManager.restoreTabs()
            player.interfaceManager.openTab(4, Component(Components.WORNITEMS_387))
            return@onUnequip true
        }

        on(warriorGuildDoors, IntType.SCENERY, "open") { player, node ->
            val doorScenery = node.asScenery()

            if (node.id == Scenery.DOOR_1530 && node.location != Location(2837, 3549, 0)) {
                handleDoor(player, doorScenery)
                return@on true
            }

            if (canEnter(player)) {
                player.musicPlayer.unlock(Music.WARRIORS_GUILD_634)
                handleAutowalkDoor(player, doorScenery)
                return@on true
            }

            sendNPCDialogue(player, NPCs.GHOMMAL_4285, "You not pass. You too weedy.")
            return@on false
        }

        on(NPCs.GAMFRED_4287, IntType.NPC, "claim-shield") { player, node ->
            player.dialogueInterpreter.open(NPCs.GAMFRED_4287, node, true)
            return@on true
        }

        on(IntType.NPC, "claim-tokens") { player, node ->
            openDialogue(player, ClaimTokenDialogue(node.asNpc()))
            return@on true
        }
    }

    companion object {
        private fun canEnter(player: Player): Boolean =
            getStatLevel(player, Skills.ATTACK) + getStatLevel(player, Skills.STRENGTH) >= 130 ||
            getStatLevel(player, Skills.ATTACK) == 99 || getStatLevel(player, Skills.STRENGTH) == 99
    }
}
