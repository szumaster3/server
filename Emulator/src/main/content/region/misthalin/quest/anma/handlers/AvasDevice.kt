package content.region.misthalin.quest.anma.handlers

import content.data.GameAttributes
import core.api.*
import core.api.EquipmentSlot
import core.api.Event
import core.api.quest.isQuestComplete
import core.game.container.impl.EquipmentContainer
import core.game.event.EventHook
import core.game.event.TickEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.tools.colorize
import core.tools.secondsToTicks
import org.rs.consts.Items
import org.rs.consts.Quests

class AvasDevice :
    InteractionListener,
    EventHook<TickEvent> {
    override fun defineListeners() {
        onEquip(devices) { player, _ ->
            if (!isQuestComplete(player, Quests.ANIMAL_MAGNETISM)) {
                sendMessage(player, "You need to complete Animal Magnetism to equip this.")
                return@onEquip false
            }

            if (attractEnabled(player)) {
                player.hook(Event.Tick, this)
            }

            setAttribute(player, LAST_TICK, getWorldTicks())
            return@onEquip true
        }
        onUnequip(devices) { player, _ ->
            if (attractEnabled(player)) {
                player.unhook(this)
            }
            return@onUnequip true
        }
        on(devices, IntType.ITEM, "operate") { player, _ ->
            val attract = !attractEnabled(player)
            setAttribute(player, ATTRACT_ENABLED, attract)
            sendMessage(
                player,
                colorize(
                    "Ava's device will ${if (attract) "now" else "no longer"} randomly collect loot for you.",
                    "990000",
                ),
            )
            if (attract) {
                player.hook(Event.Tick, this)
            } else {
                player.unhook(this)
            }
            return@on true
        }
    }

    override fun process(
        entity: Entity,
        event: TickEvent,
    ) {
        if (entity !is Player) {
            entity.unhook(this)
            return
        }

        if (getWorldTicks() - getLastTick(entity) < attractDelay) {
            return
        } else {
            setAttribute(entity, LAST_TICK, getWorldTicks())
        }

        if (isInterfered(entity)) {
            sendMessage(entity, "Your armour interferes with Ava's device.")
            return
        }

        val wornId = getItemFromEquipment(entity, EquipmentSlot.CAPE)?.id ?: -1

        val reward =
            when (wornId) {
                Items.AVAS_ACCUMULATOR_10499 -> ACCUMULATOR_REWARDS
                Items.AVAS_ATTRACTOR_10498 -> ATTRACTOR_REWARDS
                else -> {
                    entity.unhook(this)
                    return
                }
            }.random()

        if (equipSlot(reward) == EquipmentSlot.AMMO) {
            val equippedId = getItemFromEquipment(entity, EquipmentSlot.AMMO)?.id ?: -1
            if (reward == equippedId || equippedId == -1) {
                entity.equipment.add(reward.asItem(), true, false)
                return
            }
        }

        if (!getAttribute(entity, GameAttributes.ITEM_AVA_DEVICE, false) &&
            entity.houseManager.isInHouse(entity) &&
            entity.houseManager.isBuildingMode &&
            entity.equipment[EquipmentContainer.SLOT_ARROWS] != null &&
            freeSlots(entity) == 0
        ) {
            sendMessage(entity, "Ava's contraption makes an odd burping sound.")
            setAttribute(entity, GameAttributes.ITEM_AVA_DEVICE, true)
        }

        addItemOrDrop(entity, reward)
    }

    private fun attractEnabled(entity: Entity): Boolean = getAttribute(entity, ATTRACT_ENABLED, true)

    private fun getLastTick(entity: Entity): Int = getAttribute(entity, LAST_TICK, 0)

    private fun isInterfered(player: Player): Boolean {
        val chestPiece = getItemFromEquipment(player, EquipmentSlot.CHEST)
        val modelId = chestPiece?.definition?.maleWornModelId1 ?: -1
        return modelId != -1 && modelId in metalBodies
    }

    companion object {
        const val ATTRACT_ENABLED = "/save:avadevice:attract"
        const val LAST_TICK = "avadevice:tick"
        val devices = intArrayOf(Items.AVAS_ACCUMULATOR_10499, Items.AVAS_ATTRACTOR_10498)
        val metalBodies = intArrayOf(301, 306, 3379)
        val attractDelay = secondsToTicks(180)

        // https://runescape.wiki/w/Ava%27s_attractor?oldid=1976444
        val ATTRACTOR_REWARDS =
            arrayOf(
                Items.IRON_ORE_440,
                Items.IRON_MED_HELM_1137,
                Items.IRON_DART_807,
                Items.IRON_KNIFE_863,
                Items.IRON_BAR_2351,
                Items.IRON_BOLTS_9140,
                Items.IRON_ARROWTIPS_40,
                Items.TOY_MOUSE_7767,
            )

        // https://runescape.wiki/w/Ava%27s_accumulator?oldid=1965161
        val ACCUMULATOR_REWARDS =
            arrayOf(
                Items.IRON_ORE_440,
                Items.STEEL_MED_HELM_1141,
                Items.STEEL_DART_808,
                Items.STEEL_KNIFE_865,
                Items.STEEL_BAR_2353,
                Items.STEEL_NAILS_1539,
                Items.STEEL_ARROWTIPS_41,
                Items.STEEL_ARROW_886,
                Items.STEEL_BOLTS_9141,
                Items.STEEL_AXE_1353,
                Items.STEEL_2H_SWORD_1311,
                Items.TOY_MOUSE_7767,
            )
    }
}
