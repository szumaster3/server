package content.region.misthalin.quest.phoenixgang.handlers

import content.region.misthalin.quest.phoenixgang.ShieldofArrav
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.component.Component
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.global.action.PickupHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.repository.Repository
import core.plugin.Plugin
import org.rs.consts.NPCs
import org.rs.consts.Quests

class ShieldOfArravPlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        val handlers =
            mapOf(
                2402 to "option:search",
                2397 to "option:open",
                2399 to "option:open",
                2398 to "option:open",
                2403 to "option:open",
                2404 to "option:close",
                2404 to "option:search",
                24356 to "option:climb-up",
                2400 to "option:open",
                2401 to "option:search",
                2401 to "option:shut",
            )

        handlers.forEach { (id, option) -> SceneryDefinition.forId(id).handlers[option] = this }

        val itemHandlers =
            listOf(
                761 to "option:read",
                767 to "option:take",
                769 to "option:read",
                ShieldofArrav.PHOENIX_CERTIFICATE.id to "option:read",
                ShieldofArrav.BLACKARM_CERTIFICATE.id to "option:read",
            )

        itemHandlers.forEach { (id, option) -> ItemDefinition.forId(id).handlers[option] = this }

        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val quest = player.questRepository.getQuest(Quests.SHIELD_OF_ARRAV)
        val id =
            when (node) {
                is Scenery -> node.id
                is Item -> node.id
                else -> node.id
            }

        when (id) {
            769, 11173, 11174 -> {
                val componentId =
                    when (id) {
                        769 -> 526
                        11173 -> 525
                        else -> 529
                    }
                openInterface(player, Component(componentId).id)
                sendString(
                    player,
                    "The bearer of this certificate has brought both halves of the legendary Shield of Arrav to me, Halg Halen...",
                    componentId,
                    2,
                )
            }

            2400 -> SceneryBuilder.replace(node as Scenery, node.transform(org.rs.consts.Scenery.CUPBOARD_2401))

            2401 ->
                when (option) {
                    "search" -> {
                        if (quest.getStage(player) == 70 && ShieldofArrav.isBlackArm(player)) {
                            if (!player.inventory.containsItem(ShieldofArrav.BLACKARM_SHIELD) &&
                                !player.bank.containsItem(ShieldofArrav.BLACKARM_SHIELD)
                            ) {
                                if (!player.inventory.add(ShieldofArrav.BLACKARM_SHIELD)) {
                                    GroundItemManager.create(ShieldofArrav.BLACKARM_SHIELD, player)
                                }
                                sendItemDialogue(
                                    player,
                                    ShieldofArrav.BLACKARM_SHIELD.id,
                                    "You find half of a shield, which you take.",
                                )
                            } else {
                                sendDialogue(player, "The cupboard is bare.")
                            }
                        }
                    }

                    "shut" -> SceneryBuilder.replace(node as Scenery, node.transform(2400))
                }

            24356 -> {
                if (node.location.x == 3188) {
                    ClimbActionHandler.climb(player, null, Location.create(3188, 3392, 1))
                } else {
                    ClimbActionHandler.climbLadder(player, node as Scenery, option)
                }
            }

            767 -> {
                val master = RegionManager.getLocalNpcs(player).find { it.id == NPCs.WEAPONSMASTER_643 }
                if (master != null) {
                    if (!ShieldofArrav.isPhoenix(player) && !master.isInvisible) {
                        player.dialogueInterpreter.open(NPCs.WEAPONSMASTER_643, master, true)
                    } else {
                        return PickupHandler.take(player, node as GroundItem)
                    }
                }
            }

            2403 -> SceneryBuilder.replace(node as Scenery, node.transform(2404))

            2404 ->
                when (option) {
                    "close" -> SceneryBuilder.replace(node as Scenery, node.transform(2403))

                    "search" -> {
                        if (quest.getStage(player) == 70 && ShieldofArrav.isPhoenix(player)) {
                            if (!inBank(player, ShieldofArrav.PHOENIX_SHIELD.id) &&
                                !inInventory(player, ShieldofArrav.PHOENIX_SHIELD.id)
                            ) {
                                addItemOrDrop(player, ShieldofArrav.PHOENIX_SHIELD.id, 1)
                                sendItemDialogue(
                                    player,
                                    ShieldofArrav.PHOENIX_SHIELD.id,
                                    "You find half of a shield, which you take.",
                                )
                            } else {
                                sendDialogue(player, "The chest is empty.")
                            }
                        }
                    }
                }

            2398 -> {
                if (quest.getStage(player) == 60 &&
                    ShieldofArrav.isBlackArmMission(player) &&
                    !inInventory(
                        player,
                        ShieldofArrav.KEY.id,
                    )
                ) {
                    sendDialogue(player, "This is the door to the weapon stash you were looking for...")
                } else if (quest.getStage(player) == 60 && inInventory(player, ShieldofArrav.KEY.id)) {
                    DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
                } else {
                    sendMessage(player, "The door is securely locked.")
                }
            }

            2399 -> {
                if (!ShieldofArrav.isBlackArm(player)) {
                    sendMessage(player, "This door seems to be locked from the inside.")
                } else {
                    DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
                }
            }

            2397 -> {
                if (ShieldofArrav.isPhoenix(player)) {
                    sendMessage(player, "The door automatically opens for you.")
                    DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
                } else {
                    sendMessage(player, "The door is securely locked.")
                }
            }

            2402 -> {
                if (quest.getStage(player) == 10 &&
                    !inInventory(player, ShieldofArrav.BOOK.id) &&
                    !inBank(
                        player,
                        ShieldofArrav.BOOK.id,
                    )
                ) {
                    openDialogue(player, NPCs.RELDO_2660, Repository.findNPC(NPCs.RELDO_2660)!!, "book")
                } else {
                    sendMessage(player, "You already own 'The Shield of Arrav', the book that was kept here.")
                }
            }
        }
        return true
    }

    override fun isWalk(): Boolean = false

    override fun isWalk(
        player: Player,
        node: Node,
    ): Boolean = if (node is GroundItem) true else node !is Item

    override fun getDestination(
        n: Node,
        node: Node,
    ): Location? =
        if (node is Scenery && node.name.lowercase().contains("door")) {
            DoorActionHandler.getDestination(n as Player, node)
        } else {
            null
        }
}
