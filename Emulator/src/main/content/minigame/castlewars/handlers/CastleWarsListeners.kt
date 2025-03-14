package content.minigame.castlewars.handlers

import content.global.skill.agility.AgilityHandler
import content.global.skill.summoning.familiar.BurdenBeast
import content.minigame.castlewars.handlers.areas.CastleWarsWaitingArea
import core.api.*
import core.api.interaction.openNpcShop
import core.cache.def.impl.ItemDefinition
import core.game.container.Container
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

@Suppress("unused")
class CastleWarsListeners : InteractionListener {
    override fun defineListeners() {
        on(NPCs.LANTHUS_1526, IntType.NPC, "trade-with") { player, _ ->
            openNpcShop(player, NPCs.LANTHUS_1526)
            return@on true
        }

        on(CastleWars.joinSaradominTeamPortal, IntType.SCENERY, "Enter") { player, _ ->
            if (GameWorld.settings?.enable_castle_wars != true) return@on false
            if (joinError(player)) return@on true
            player.properties.teleportLocation = CastleWarsWaitingArea.saradominWaitingRoom.randomWalkableLoc
            player.setAttribute(CastleWars.portalAttribute, CastleWars.saradominName)
            return@on true
        }

        on(CastleWars.joinZamorakTeamPortal, IntType.SCENERY, "Enter") { player, _ ->
            if (GameWorld.settings?.enable_castle_wars != true) return@on false
            if (joinError(player)) return@on true
            player.properties.teleportLocation = CastleWarsWaitingArea.zamorakWaitingRoom.randomWalkableLoc
            player.setAttribute(CastleWars.portalAttribute, CastleWars.zamorakName)
            return@on true
        }

        on(CastleWars.saradominLeaveLobbyPortal, IntType.SCENERY, "Exit") { player, _ ->
            player.properties.teleportLocation = CastleWars.lobbyBankArea.randomWalkableLoc
            return@on true
        }

        on(CastleWars.zamorakLeaveLobbyPortal, IntType.SCENERY, "Exit") { player, _ ->
            player.properties.teleportLocation = CastleWars.lobbyBankArea.randomWalkableLoc
            return@on true
        }

        on(CastleWars.joinGuthixTeamPortal, IntType.SCENERY, "Enter") { player, _ ->
            if (GameWorld.settings?.enable_castle_wars != true) return@on false
            if (joinError(player)) return@on true

            if (CastleWarsWaitingArea.waitingSaradominPlayers.size < CastleWarsWaitingArea.waitingZamorakPlayers.size) {
                player.properties.teleportLocation = CastleWarsWaitingArea.saradominWaitingRoom.randomWalkableLoc
            } else if (CastleWarsWaitingArea.waitingSaradominPlayers.size >
                CastleWarsWaitingArea.waitingZamorakPlayers.size
            ) {
                player.properties.teleportLocation = CastleWarsWaitingArea.zamorakWaitingRoom.randomWalkableLoc
            } else {
                if (Math.random() < 0.5) {
                    player.properties.teleportLocation = CastleWarsWaitingArea.saradominWaitingRoom.randomWalkableLoc
                } else {
                    player.properties.teleportLocation = CastleWarsWaitingArea.zamorakWaitingRoom.randomWalkableLoc
                }
            }
            player.setAttribute(CastleWars.portalAttribute, CastleWars.guthixName)
            return@on true
        }

        on(CastleWars.cwTableItemRewardMap.keys.toIntArray(), SCENERY, "take-from") { player, node ->
            val rewardItem: Int = CastleWars.cwTableItemRewardMap.getValue(node.id)

            if (addItem(player, rewardItem)) {
                playAudio(player, Sounds.PICK2_2582)
            } else {
                val formattedItemName =
                    (ItemDefinition.forId(rewardItem).name.lowercase() + "s.")
                        .replace("pes.", "pe.")
                        .replace("bronze ", "")
                if (rewardItem == Items.TOOLKIT_4051) {
                    sendDialogue(player, "Your inventory is too full to hold a toolkit.")
                } else {
                    sendDialogue(player, (CastleWars.invFullMessage + formattedItemName))
                }
            }
            return@on true
        }

        onUseWith(
            SCENERY,
            CastleWars.cwClimbingRope,
            *CastleWars.cwCastleBattlementsMap.keys.toIntArray(),
        ) { player, rope, wall ->
            removeItem(player, rope)
            replaceScenery(
                wall.asScenery(),
                CastleWars.cwCastleBattlementsMap.getValue(wall.id),
                CastleWars.ropeAliveTicks,
            )
            val toAdd = Scenery(CastleWars.cwCastleClimbingRope, wall.location, 4, wall.direction.toInteger())
            SceneryBuilder.add(toAdd, CastleWars.ropeAliveTicks)
            toAdd.isActive = true
            return@onUseWith true
        }

        on(CastleWars.cwCastleClimbingRope, SCENERY, "climb") { player, rope ->
            val dir = rope.asScenery().direction.opposite
            teleport(
                player,
                Location(dir.stepY + player.location.x, -dir.stepX + player.location.y),
                TeleportManager.TeleportType.INSTANT,
            )
            return@on true
        }

        onUseWith(SCENERY, Items.BUCKET_1925, CastleWars.castleWaterTap) { player, used, _ ->
            lock(player, 1)
            animate(player, 832)
            playAudio(player, Sounds.TAP_FILL_2609)
            replaceSlot(player, used.asItem().slot, Item(Items.BUCKET_OF_WATER_1929))
            return@onUseWith true
        }

        on(CastleWars.cwSteppingStones, SCENERY, "jump-to") { player, stone ->
            lock(player, 3)
            AgilityHandler.forceWalk(player, -1, player.location, stone.location, Animation(741), 10, 0.0, null, 1)
            runTask(player, 1) {
                playAudio(player, Sounds.JUMP_2461)
            }
            return@on true
        }
    }

    private fun hasNonCombatItems(container: Container): Boolean {
        for (item in container.toArray()) {
            if (item?.id != null && (item.id == Items.COINS_995 || item.definition?.noteId == item.id)) {
                return true
            }
        }
        return false
    }

    private fun capeOrHelmetError(player: Player): String? {
        val wornCape = getItemFromEquipment(player, EquipmentSlot.CAPE)?.id ?: -1
        val wornHelmet = getItemFromEquipment(player, EquipmentSlot.HEAD)?.id ?: -1

        if (wornCape != -1 || wornHelmet != -1) return "You can't wear hats, capes, or helms in the arena."
        return null
    }

    private fun nonCombatItemsCheck(player: Player): String? {
        if (hasNonCombatItems(player.inventory)) return "You can't take non-combat items into the arena."

        return null
    }

    private fun familiarCheck(player: Player): String? {
        val familiar: BurdenBeast = player.familiarManager.familiar as? BurdenBeast ?: return null

        if (hasNonCombatItems(familiar.container)) return "Your familiar can't take non-combat items into the arena."

        return null
    }

    private fun joinError(player: Player): Boolean {
        val errorMessage =
            capeOrHelmetError(player) ?: nonCombatItemsCheck(player) ?: familiarCheck(player) ?: return false
        player.sendMessage(errorMessage).also { return true }
    }
}
