package content.minigame.pestcontrol.handlers

import content.global.travel.charter.Charter
import content.minigame.pestcontrol.handlers.npc.*
import core.api.*
import core.cache.def.impl.NPCDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.item.Item
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs.consts.*

class PCListener : InteractionListener {
    override fun defineListeners() {
        on(VOID_SEAL, IntType.ITEM, "rub", "operate") { player, node ->
            val operate = getUsedOption(player) == "operate"

            if (player.viewport.region.regionId != 10536) {
                sendMessage(player, "You can only use the seal in Pest Control.")
                return@on true
            }

            lock(player, 1)

            val item = node as Item

            var replace: Item? = null
            if (item.id != Items.VOID_SEAL1_11673) {
                replace = Item(item.id + 1)
                sendMessage(player, "You unleash the power of the Void Knights!")
            } else {
                sendMessage(player, "The seal dissolves as the least of its power is unleashed.")
                removeItem(player, item)
            }

            if (operate) {
                player.equipment.replace(replace, item.slot)
            } else {
                player.inventory.replace(replace, item.slot)
            }

            animate(player, Animations.HANDS_TOGETHER_709)
            player.graphics(Graphics.create(org.rs.consts.Graphics.GREEN_CIRCLE_WAVES_1177))

            for (npc in getLocalNpcs(player, 2)) {
                if (npc is PCDefilerNPC ||
                    npc is PCRavagerNPC ||
                    npc is PCShifterNPC ||
                    npc is PCSpinnerNPC ||
                    npc is PCSplatterNPC ||
                    npc is PCTorcherNPC ||
                    npc is PCBrawlerNPC
                ) {
                    npc.impactHandler.manualHit(player, 7 + RandomFunction.randomize(5), HitsplatType.NORMAL, 1)
                    npc.graphics(Graphics.create(org.rs.consts.Graphics.RED_CIRCLE_WAVES_1176))
                }
            }

            return@on true
        }

        on(VOID_KNIGHT, IntType.NPC, "exchange") { player, _ ->
            PCRewardInterface.open(player)
            return@on true
        }

        on(SQUIRE, IntType.NPC, "talk-to", "leave") { player, node ->
            val session =
                node.asNpc().getExtension<PestControlSession>(
                    PestControlSession::class.java,
                )
            when (getUsedOption(player)) {
                "talk-to" -> {
                    if (session == null) {
                        val handler = NPCDefinition.optionHandlers[getUsedOption(player)]
                        handler!!.handle(player, node, getUsedOption(player))
                        return@on true
                    } else {
                        openDialogue(player, SQUIRE, node, true)
                    }
                }

                "leave" -> {
                    if (session == null) {
                        Charter.PEST_TO_PORT_SARIM.sail(player)
                        return@on true
                    }
                    player.properties.teleportLocation = session.activity.leaveLocation
                }
            }
            return@on true
        }
    }

    companion object {
        const val SQUIRE = NPCs.SQUIRE_3781

        val VOID_SEAL =
            intArrayOf(
                Items.VOID_SEAL8_11666,
                Items.VOID_SEAL7_11667,
                Items.VOID_SEAL6_11668,
                Items.VOID_SEAL5_11669,
                Items.VOID_SEAL4_11670,
                Items.VOID_SEAL3_11671,
                Items.VOID_SEAL2_11672,
                Items.VOID_SEAL1_11673,
            )
        val VOID_KNIGHT =
            intArrayOf(
                NPCs.VOID_KNIGHT_3786,
                NPCs.VOID_KNIGHT_3788,
                NPCs.VOID_KNIGHT_3789,
                NPCs.VOID_KNIGHT_5956,
            )
        val LADDER =
            intArrayOf(
                Scenery.LADDER_14314,
                Scenery.LADDER_25629,
                Scenery.LADDER_25630,
            )
    }
}
