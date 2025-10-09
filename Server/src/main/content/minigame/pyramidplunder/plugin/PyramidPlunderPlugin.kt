package content.minigame.pyramidplunder.plugin

import core.Util
import core.api.*
import core.api.hasRequirement
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.world.map.Location
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class PyramidPlunderPlugin : InteractionListener {

    companion object {
        val SIMON_TEMPLETON = NPCs.SIMON_TEMPLETON_3123
        val GUARDIAN_MUMMY = NPCs.GUARDIAN_MUMMY_4476
        val SCEPTRE = intArrayOf(
            Items.PHARAOHS_SCEPTRE_9044,
            Items.PHARAOHS_SCEPTRE_9046,
            Items.PHARAOHS_SCEPTRE_9048,
            Items.PHARAOHS_SCEPTRE_9050
        )
    }

    override fun defineListeners() {

        /*
         * Handles attempting to sell the pharaoh sceptre to Simon templeton NPCs.
         */

        onUseWith(IntType.NPC, SCEPTRE, SIMON_TEMPLETON) { player, used, with ->
            openDialogue(player, NPCs.SIMON_TEMPLETON_3123, with as NPC, true, false, used.id)
            return@onUseWith true
        }

        /*
         * Handles the recharging of the pharaoh sceptres.
         */

        onUseWith(IntType.NPC, Items.PHARAOHS_SCEPTRE_9050, GUARDIAN_MUMMY) { player, used, _ ->
            openDialogue(player, 999876, used)
            return@onUseWith true
        }

        /*
         * Handles interaction with pharaoh Sceptre.
         */

        on(SCEPTRE, IntType.ITEM, "teleport", "operate") { player, node ->
            if (!hasRequirement(player, Quests.ICTHLARINS_LITTLE_HELPER)) return@on true
            val sceptre = node.asItem()

            if (sceptre.id == SCEPTRE.last()) {
                sendDialogueLines(
                    player,
                    "This sceptre has no charges, talk to the Guardian Mummy in the",
                    "Jalsavrah Pyramid to recharge it."
                )
                return@on true
            }

            setTitle(player, 4)
            sendOptions(
                player,
                "Which Pyramid do you want to teleport to?",
                "Jalsavrah",
                "Jaleustrophos",
                "Jaldraocht",
                "I'm happy where I am actually."
            )

            addDialogueAction(player) { player, button ->
                when (button) {
                    2 -> teleport(player, PyramidPlunderMinigame.GUARDIAN_ROOM, TeleportManager.TeleportType.PHARAOH_SCEPTRE)
                    3 -> teleport(player, Location.create(3342, 2827, 0), TeleportManager.TeleportType.PHARAOH_SCEPTRE)
                    4 -> teleport(player, Location.create(3233, 2902, 0), TeleportManager.TeleportType.PHARAOH_SCEPTRE)
                    5 -> return@addDialogueAction
                }

                val sceptreStates = mapOf(
                    Items.PHARAOHS_SCEPTRE_9044 to Items.PHARAOHS_SCEPTRE_9046,
                    Items.PHARAOHS_SCEPTRE_9046 to Items.PHARAOHS_SCEPTRE_9048,
                    Items.PHARAOHS_SCEPTRE_9048 to Items.PHARAOHS_SCEPTRE_9050
                )

                for ((current, next) in sceptreStates) {
                    val item = Item(current)
                    if (player!!.equipment.containsItem(item) || player.inventory.containsItem(item)) {
                        if (player.equipment.containsItem(item)) {
                            player.equipment.replace(Item(next), EquipmentSlot.WEAPON.ordinal)
                        } else {
                            player.inventory.remove(item)
                            player.inventory.add(Item(next))
                        }

                        val chargesLeft = when (next) {
                            Items.PHARAOHS_SCEPTRE_9046 -> 2
                            Items.PHARAOHS_SCEPTRE_9048 -> 1
                            Items.PHARAOHS_SCEPTRE_9050 -> 0
                            else -> 0
                        }

                        val message = if (chargesLeft > 0)
                            "The sceptre has ${Util.convert(chargesLeft)} charge${if (chargesLeft != 1) "s" else ""} left."
                        else
                            "The sceptre has no charges left."

                        player.packetDispatch.sendMessage(message)
                        break
                    }
                }
            }
            return@on true
        }
    }
}