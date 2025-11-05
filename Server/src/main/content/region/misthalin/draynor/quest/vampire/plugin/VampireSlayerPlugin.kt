package content.region.misthalin.draynor.quest.vampire.plugin

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import shared.consts.*

class VampireSlayerPlugin: InteractionListener {
    override fun defineListeners() {
        on(Scenery.CUPBOARD_33502, IntType.SCENERY, "open") { player, node ->
            animate(player, Animations.HUMAN_OPEN_WARDROBE_542)
            playAudio(player, Sounds.CUPBOARD_OPEN_58)
            sendMessage(player, "You open the cupboard.")
            replaceScenery(node.asScenery(),  Scenery.CUPBOARD_33503, -1)
            return@on true
        }

        on(Scenery.CUPBOARD_33503, IntType.SCENERY, "search") { player, _ ->
            when {
                freeSlots(player) == 0 -> {
                    sendMessage(player, "Not enough inventory space.")
                }
                !inInventory(player, Items.GARLIC_1550) -> {
                    sendMessage(player, "The cupboard contains garlic. You take a clove.")
                    addItem(player, Items.GARLIC_1550, 1)
                }
                else -> {
                    sendMessage(player, "You search the cupboard but find nothing.")
                }
            }
            return@on true
        }

        on(Scenery.COFFIN_2614, IntType.SCENERY, "open") { player, node ->
            if (isQuestComplete(player, Quests.VAMPIRE_SLAYER)) {
                sendPlayerDialogue(player, "I should tell Morgan that I've killed the vampire!")
                return@on true
            }
            if (getQuestStage(player, Quests.VAMPIRE_SLAYER) != 30) {
                sendMessage(player, "The coffin is sealed shut.")
                return@on true
            }
            animate(player, Animations.HUMAN_MULTI_USE_832)
            playAudio(player, Sounds.OPEN_HEAVY_54)
            if (getQuestStage(player, Quests.VAMPIRE_SLAYER) == 30) {
                replaceScenery(node.asScenery(), Scenery.COFFIN_11208, 6)
                runTask(player, 3) {
                    val o = player.getAttribute<NPC>("count", null)
                    if (o == null || !o.isActive) {
                        val vampire =
                            core.game.node.entity.npc.NPC
                                .create(NPCs.COUNT_DRAYNOR_757, Location.create(3078, 9775))
                        vampire.isRespawn = false
                        vampire.setAttribute("player", player)
                        vampire.init()
                        setAttribute(player, "vs-exit", true)
                        runTask(player, 5) {
                            teleport(vampire, Location(3078, 9774, 0))
                            vampire.animator.reset()
                            vampire.properties.combatPulse.attack(player)
                            removeAttribute(player, "vs-exit")
                        }
                        setAttribute(player, "count", vampire)
                        if (inInventory(player, Items.STAKE_1549)) {
                            sendMessage(player, "The vampire seems to weaken.")
                        }
                    }
                }
            }
            return@on true
        }

        on(Scenery.STAIRS_32835, IntType.SCENERY, "walk-down") { player, _ ->
            player.properties.teleportLocation = Location.create(3077, 9770, 0)
            sendMessage(player, "You walk down the stairs...")
            return@on true
        }

        on(Scenery.STAIRS_32836, IntType.SCENERY, "walk-up") { player, _ ->
            if (getAttribute(player, "vs-exit", false)) {
                sendMessage(player, "You can't do that right now.")
                return@on true
            }
            player.properties.teleportLocation = Location.create(3115, 3356, 0)
            return@on true
        }
    }
}
