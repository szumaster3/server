package content.region.asgarnia.handlers.partyroom

import content.region.asgarnia.handlers.partyroom.PartyRoomOptionHandler.Companion.balloonManager
import content.region.asgarnia.handlers.partyroom.PartyRoomOptionHandler.Companion.isDancing
import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs

object PartyRoomUtils {
    private val isCluttered: Boolean
        get() = balloonManager.isCluttered

    fun handleLever(
        player: Player,
        scenery: Scenery,
    ) {
        lock(player, 3)
        face(player, scenery.location)
        animate(player, Animations.HUMAN_PARTY_ROOM_LEVER_6933)
        sendDialogueOptions(
            player,
            "Select an Option",
            "Ballon Bonanza (1000 coins).",
            "Nightly Dance (500 coins).",
            "No reward.",
        )
        addDialogueAction(player) { player, buttonId ->
            when (buttonId) {
                2 ->
                    if (isCluttered) {
                        sendDialogue(player, "The floor is too cluttered at the moment.")
                    } else if (balloonManager.isCountingDown) {
                        sendDialogue(player, "A count down has already begun.")
                    } else if (inInventory(player, Items.COINS_995, 1000)) {
                        balloonManager.start()
                        removeItem(player, Item(Items.COINS_995, 1000))
                    } else {
                        sendDialogue(player, "Balloon Bonanza costs 1000 coins.")
                    }

                3 ->
                    if (isDancing) {
                        sendDialogue(player, "The party room knights are already here!")
                    } else if (inInventory(player, Items.COINS_995, 500)) {
                        commenceDance()
                        removeItem(player, Item(Items.COINS_995, 500))
                    } else {
                        sendDialogue(player, "Nightly Dance costs 500 coins.")
                    }

                else -> {}
            }
        }
    }

    private fun commenceDance() {
        isDancing = true
        val npcs: MutableList<NPC> = ArrayList()
        for (i in 0..5) {
            val npc = NPC.create(NPCs.KNIGHT_660, Location.create(3043 + i, 3378, 0))
            npc.init()
            npcs.add(npc)
        }
        Pulser.submit(
            object : Pulse(1) {
                var count: Int = 0

                override fun pulse(): Boolean {
                    when (count) {
                        3 -> sendChat(npcs[3], "We're Knights of the Party Room")
                        6 -> sendChat(npcs[3], "We dance round and round like a loon")
                        8 -> sendChat(npcs[3], "Quite often we like to sing")
                        11 -> sendChat(npcs[3], "Unfortunately we make a din")
                        13 -> sendChat(npcs[3], "We're Knights of the Party Room")
                        16 -> sendChat(npcs[3], "Do you like our helmet plumes?")
                        18 -> sendChat(npcs[3], "Everyone's happy now we can move")
                        20 -> sendChat(npcs[3], "Like a party animal in the groove")
                        24 -> {
                            isDancing = false
                            var i = 0
                            while (i < npcs.size) {
                                npcs[i].clear()
                                i++
                            }
                        }
                    }
                    count++
                    return false
                }
            },
        )
    }
}
