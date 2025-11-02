package content.minigame.pestcontrol.plugin

import content.global.travel.ship.CharterShip
import content.minigame.pestcontrol.npc.*
import core.api.*
import core.cache.def.impl.NPCDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.item.Item
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Regions
import shared.consts.Graphics as Gfx

class PestControlListener : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles interaction with Void seal.
         */

        on(VOID_SEAL, IntType.ITEM, "rub", "operate") { player, node ->
            val operate = getUsedOption(player) == "operate"

            if (player.viewport.region?.regionId != Regions.PEST_CONTROL_10536) {
                sendMessage(player, "You can only use the seal in Pest Control.")
                return@on true
            }

            val item = node as Item
            val isLastSeal = item.id == Items.VOID_SEAL1_11673

            if (isLastSeal) {
                sendMessage(player, "The seal dissolves as the least of its power is unleashed.")
                removeItem(player, item)
            } else {
                sendMessage(player, "You unleash the power of the Void Knights!")
                val replacement = Item(item.id + 1)
                val container = if (operate) player.equipment else player.inventory
                container.replace(replacement, item.slot)
            }

            visualize(player, Animations.HANDS_TOGETHER_709, Gfx.GREEN_CIRCLE_WAVES_1177)
            animate(player, Animations.HANDS_TOGETHER_709)

            lock(player, 1)
            getLocalNpcs(player, 2)
                .filter {
                    it is PCDefilerNPC ||
                            it is PCRavagerNPC ||
                            it is PCShifterNPC ||
                            it is PCSpinnerNPC ||
                            it is PCSplatterNPC ||
                            it is PCTorcherNPC ||
                            it is PCBrawlerNPC
                }
                .forEach { npc ->
                    npc.impactHandler.manualHit(
                        player,
                        7 + RandomFunction.randomize(5),
                        HitsplatType.NORMAL,
                        1
                    )
                    npc.graphics(Graphics.create(Gfx.RED_CIRCLE_WAVES_1176))
                }

            return@on true
        }

        /*
         * Handles opening pest control interface with rewards.
         */

        on(VOID_KNIGHT, IntType.NPC, "exchange") { player, _ ->
            PCRewardInterface.open(player)
            return@on true
        }

        /*
         * Handles Squire NPC options near boat.
         */

        on(SQUIRE, IntType.NPC, "talk-to", "leave") { player, node ->
            val session =
                node
                    .asNpc()
                    .getExtension<PestControlSession>(
                        PestControlSession::class.java,
                    )
            when (getUsedOption(player)) {
                "talk-to" -> {
                    if (session == null) {
                        val handler = NPCDefinition.getOptionHandlers()[getUsedOption(player)]
                        handler!!.handle(player, node, getUsedOption(player))
                        return@on true
                    } else {
                        openDialogue(player, SQUIRE, node, true)
                    }
                }
                "leave" -> {
                    if (session == null) {
                        CharterShip.PEST_TO_PORT_SARIM.sail(player)
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

        private val VOID_SEAL =
            intArrayOf(
                Items.VOID_SEAL8_11666,
                Items.VOID_SEAL7_11667,
                Items.VOID_SEAL6_11668,
                Items.VOID_SEAL5_11669,
                Items.VOID_SEAL4_11670,
                Items.VOID_SEAL3_11671,
                Items.VOID_SEAL2_11672,
                Items.VOID_SEAL1_11673
            )
        private val VOID_KNIGHT =
            intArrayOf(
                NPCs.VOID_KNIGHT_3786,
                NPCs.VOID_KNIGHT_3788,
                NPCs.VOID_KNIGHT_3789,
                NPCs.VOID_KNIGHT_5956,
            )
    }
}
