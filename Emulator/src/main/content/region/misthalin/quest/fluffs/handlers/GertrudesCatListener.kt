package content.region.misthalin.quest.fluffs.handlers

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.path.Path
import core.game.world.map.path.Pathfinder
import core.tools.RandomFunction
import org.rs.consts.*

class GertrudesCatListener : InteractionListener {
    private val CRATE = intArrayOf(Scenery.CRATE_767, Scenery.CRATE_2620)

    override fun defineListeners() {
        on(CRATE, IntType.SCENERY, "search") { player, node ->
            if (getQuestStage(player, Quests.GERTRUDES_CAT) == 50 &&
                hasAnItem(
                    player,
                    Items.THREE_LITTLE_KITTENS_13236,
                ).container != null
            ) {
                setQuestStage(player, Quests.GERTRUDES_CAT, 40)
            }

            if (node is NPC) {
                sendMessage(player, "You search the crate.")
                sendMessage(player, "You find nothing.")
            }

            if (getQuestStage(player, Quests.GERTRUDES_CAT) == 40) {
                if (getAttribute(player, "findkitten", false) && freeSlots(player) > 0) {
                    setQuestStage(player, Quests.GERTRUDES_CAT, 50)
                    sendDialogue(player, "You find a kitten! You carefully place it in your backpack.")
                    addItem(player, Items.THREE_LITTLE_KITTENS_13236)
                }

                sendMessage(player, "You search the crate.")
                sendMessage(player, "You find nothing.")
                if (RandomFunction.random(0, 3) == 1) {
                    sendMessage(player, "You can hear kittens mewing close by...")
                    setAttribute(player, "findkitten", true)
                }
            } else {
                sendMessage(player, "You search the crate.")
                sendMessage(player, "You find nothing.")
            }
            return@on true
        }

        onUseWith(IntType.NPC, Items.BUCKET_OF_MILK_1927, NPCs.GERTRUDES_CAT_2997) { player, used, with ->
            if (getQuestStage(player, Quests.GERTRUDES_CAT) == 20 && removeItem(player, used.asItem())) {
                addItem(player, Items.BUCKET_1925)
                animate(player, Animations.MULTI_BEND_OVER_827)
                sendChat(with.asNpc(), "Mew!")
                setQuestStage(player, Quests.GERTRUDES_CAT, 30)
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, Items.DOOGLE_SARDINE_1552, NPCs.GERTRUDES_CAT_2997) { player, used, with ->
            if (getQuestStage(player, Quests.GERTRUDES_CAT) == 30 && removeItem(player, used.asItem())) {
                animate(player, Animations.MULTI_BEND_OVER_827)
                sendChat(with.asNpc(), "Mew!")
                playAudio(player, Sounds.KITTENS_MEW_339)
                setQuestStage(player, Quests.GERTRUDES_CAT, 40)
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, Items.RAW_SARDINE_327, NPCs.GERTRUDES_CAT_2997) { player, _, _ ->
            sendMessage(player, "The cat doesn't seem interested in that.")
            return@onUseWith true
        }

        onUseWith(IntType.NPC, Items.THREE_LITTLE_KITTENS_13236, NPCs.GERTRUDES_CAT_2997) { player, used, with ->
            if (removeItem(player, used.asItem())) {
                setQuestStage(player, Quests.GERTRUDES_CAT, 60)
                Pulser.submit(
                    object : Pulse(1) {
                        var count = 0
                        val kitten =
                            core.game.node.entity.npc.NPC
                                .create(NPCs.KITTEN_761, player.location)

                        override fun pulse(): Boolean {
                            when (count) {
                                0 -> {
                                    kitten.init()
                                    kitten.face(with.asNpc())
                                    with.asNpc().face(kitten)
                                    with.asNpc().sendChat("Pur...")
                                    kitten.sendChat("Pur...")
                                    playAudio(player, Sounds.PURR_340)
                                    val firstPath: Path = Pathfinder.find(with.asNpc(), Location(3310, 3510, 1))
                                    firstPath.walk(with.asNpc())
                                    val secondPath = Pathfinder.find(kitten, Location(3310, 3510, 1))
                                    secondPath.walk(kitten)
                                }

                                5 -> {
                                    kitten.clear()
                                    setAttribute(player, "hidefluff", System.currentTimeMillis() + 60000)
                                }
                            }
                            count++
                            return count == 6
                        }
                    },
                )
            }

            return@onUseWith true
        }
    }
}
