package content.global.handlers.item

import content.minigame.pyramidplunder.handlers.PyramidPlunderMinigame.Companion.GUARDIAN_ROOM
import core.api.EquipmentSlot
import core.api.openDialogue
import core.api.quest.hasRequirement
import core.api.sendMessage
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Items
import org.rs.consts.Quests

class PharaohSceptreListener : InteractionListener {
    private val sceptreIDs =
        intArrayOf(
            Items.PHARAOHS_SCEPTRE_9044,
            Items.PHARAOHS_SCEPTRE_9046,
            Items.PHARAOHS_SCEPTRE_9048,
            Items.PHARAOHS_SCEPTRE_9050,
        )

    override fun defineListeners() {
        on(sceptreIDs, IntType.ITEM, "teleport", "operate") { player, node ->
            if (!hasRequirement(player, Quests.ICTHLARINS_LITTLE_HELPER)) return@on true
            val sceptre = node.asItem()

            if (sceptre.id == sceptreIDs.last()) {
                sendMessage(player, "You have used up all the charges on this sceptre.")
                return@on true
            }

            openDialogue(player, SceptreDialogueFile())
            return@on true
        }
    }

    class SceptreDialogueFile : DialogueFile() {
        fun teleport(
            location: Location?,
            player: Player,
        ) {
            player.lock()
            player.visualize(Animation(714), Graphics(715))
            player.impactHandler.disabledTicks = 4
            Pulser.submit(
                object : Pulse(4, player) {
                    override fun pulse(): Boolean {
                        player.unlock()
                        player.properties.teleportLocation = location
                        player.animator.reset()
                        return true
                    }
                },
            )
        }

        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            if (player!!.isTeleBlocked) {
                player!!.sendMessage("A magical force has stopped you from teleporting.")
                return
            }
            when (stage) {
                0 -> options("Jalsavrah", "Jaleustrophos", "Jaldraocht", "Nowhere").also { stage++ }
                1 -> {
                    end()
                    when (buttonID) {
                        1 -> {
                            teleport(GUARDIAN_ROOM, player!!)
                        }

                        2 -> {
                            teleport(Location.create(3342, 2827, 0), player!!)
                        }

                        3 -> {
                            teleport(Location.create(3233, 2902, 0), player!!)
                        }

                        4 -> return
                    }

                    if (player!!.equipment.containsItem(Item(Items.PHARAOHS_SCEPTRE_9044))) {
                        player!!.equipment.replace(Item(Items.PHARAOHS_SCEPTRE_9046), EquipmentSlot.WEAPON.ordinal)
                        player!!.packetDispatch.sendMessage(
                            "<col=7f03ff>Your Pharoah's Sceptre has 2 charges remaining.",
                        )
                    } else if (player!!.equipment.containsItem(Item(Items.PHARAOHS_SCEPTRE_9046))) {
                        player!!.equipment.replace(Item(Items.PHARAOHS_SCEPTRE_9048), EquipmentSlot.WEAPON.ordinal)
                        player!!.packetDispatch.sendMessage(
                            "<col=7f03ff>Your Pharoah's Sceptre has 1 charge remaining.",
                        )
                    } else if (player!!.equipment.containsItem(Item(Items.PHARAOHS_SCEPTRE_9048))) {
                        player!!.equipment.replace(Item(Items.PHARAOHS_SCEPTRE_9050), EquipmentSlot.WEAPON.ordinal)
                        player!!.packetDispatch.sendMessage(
                            "<col=7f03ff>Your Pharoah's Sceptre has no charges remaining.",
                        )
                    } else if (player!!.inventory.containsItem(Item(Items.PHARAOHS_SCEPTRE_9044))) {
                        if (player!!.inventory.remove(Item(Items.PHARAOHS_SCEPTRE_9044))) {
                            player!!.inventory.add(Item(Items.PHARAOHS_SCEPTRE_9046))
                            player!!.packetDispatch.sendMessage(
                                "<col=7f03ff>Your Pharoah's Sceptre has 2 charges remaining.",
                            )
                        }
                    } else if (player!!.inventory.containsItem(Item(Items.PHARAOHS_SCEPTRE_9046))) {
                        if (player!!.inventory.remove(Item(Items.PHARAOHS_SCEPTRE_9046))) {
                            player!!.inventory.add(Item(Items.PHARAOHS_SCEPTRE_9048))
                            player!!.packetDispatch.sendMessage(
                                "<col=7f03ff>Your Pharoah's Sceptre has 1 charge remaining.",
                            )
                        }
                    } else if (player!!.inventory.containsItem(Item(Items.PHARAOHS_SCEPTRE_9048))) {
                        if (player!!.inventory.remove(Item(Items.PHARAOHS_SCEPTRE_9048))) {
                            player!!.inventory.add(Item(Items.PHARAOHS_SCEPTRE_9050))
                            player!!.packetDispatch.sendMessage(
                                "<col=7f03ff>Your Pharoah's Sceptre has no charges remaining.",
                            )
                        }
                    }
                }
            }
        }
    }
}
