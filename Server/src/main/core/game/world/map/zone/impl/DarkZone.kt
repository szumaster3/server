package core.game.world.map.zone.impl

import content.data.LightSources
import core.api.*
import core.game.component.Component
import core.game.event.EventHook
import core.game.event.ItemEquipEvent
import core.game.event.UseWithEvent
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import shared.consts.Components
import shared.consts.Regions
import shared.consts.Sounds
import java.util.*


/**
 * Handles dark environments where light sources are required.
 *
 * Automatically applies and updates darkness overlays depending on the players active light
 * source or equipment (e.g., lamps, lanterns, or headbands).
 *
 * TODO:
 *  - [x] Falling into the water also extinguishes the light, if a player fails an Agility shortcut.
 *  - [ ] Scarab mages can occasionally have all extinguishable light sources go out ("Your lights have been magically extinguished.").
 *  - [x] The giant mole might extinguish any candles, torches, or oil lamps when it digs away.
 *  - [ ] In the Sophanem Dungeon, falling into a scarab trap puts out every extinguishable light source.
 *  - [x] Gas explosions will extinguish candles, torches, and oil lamps while dealing damage to the player.
 *
 * @author Emperor
 */
class DarkZone : MapZone("Dark zone", true), EventHook<core.game.event.Event> {

    override fun configure() {
        register(SKAVID_CAVE_10131)
        register(FALADOR_MOLE_LAIR_6992)
        register(FALADOR_MOLE_LAIR_6993)
        register(LUMBRIDGE_DUNGEON_12949)
        register(LUMBRIDGE_DUNGEON_12950)
        register(LUMBRIDGE_DUNGEON_12693)
        register(TEARS_OF_GUTHIX_12948)
        register(DORGESHUUN_MINE_13206)
        register(MOS_LE_HARMLESS_CAVE_15251)
        register(MOS_LE_HARMLESS_CAVE_14994)
        register(MOS_LE_HARMLESS_CAVE_14995)
        register(GENIE_CAVE_13457)
        register(DORGESH_KAAN_SOUTH_DUNGEON_10833)
        register(TEMPLE_OF_IKOV_10648)
        DORGESHUUN_MINE_13206.addException(ZoneBorders(3310, 9601, 3327, 9656))
        LUMBRIDGE_DUNGEON_12950.addException(ZoneBorders(3208, 9615, 3220, 9626))
    }

    override fun continueAttack(entity: Entity, target: Node, style: CombatStyle, message: Boolean): Boolean =
        entity !is Player || entity.interfaceManager.overlay != DARKNESS_OVERLAY


    override fun interact(e: Entity, target: Node?, option: Option): Boolean {
        if (target is Item) {
            val s = LightSources.forLitId(target.id)
            if (s != null) {
                val name: String = option.name.lowercase()
                val itemName = getItemName(s.litId).lowercase()
                if (name == "drop") {
                    (e as Player).packetDispatch.sendMessage("Dropping the $itemName would leave you without a light source.")
                    return true
                }
                if (name == "extinguish") {
                    (e as Player).packetDispatch.sendMessage("Extinguishing the $itemName would leave you without a light source.")
                    return true
                }
                if (name == "destroy") {
                    (e as Player).packetDispatch.sendMessage("Destroying the $itemName would leave you without a light source.")
                    return true
                }
                if (name == "remove") {
                    (e as Player).packetDispatch.sendMessage("Removing the $itemName would leave you without a light source.")
                    return true
                }

            }
        }
        return false
    }

    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            val player = e
            val source = LightSources.getActiveLightSource(player)
            if (source == null) {
                player.interfaceManager.openOverlay(DARKNESS_OVERLAY)
            } else if (source.interfaceId > 0) {
                player.interfaceManager.openOverlay(Component(source.interfaceId))
            }
        }
        e.hook(Event.UsedWith, this)
        e.hook(Event.ItemEquipped, this)
        return true
    }


    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (e is Player) e.interfaceManager.closeOverlay()
        e.unhook(this)
        return true
    }

    fun updateOverlay(player: Player) {
        val source = LightSources.getActiveLightSource(player)
        val overlayId = player.interfaceManager.overlay?.id ?: -1

        if (source == null) {
            if (overlayId != DARKNESS_OVERLAY.id) {
                player.interfaceManager.openOverlay(DARKNESS_OVERLAY)
            }
            if (player.getExtension<Pulse>(DarkZone::class.java) == null) startInsectPulse(player)
        } else {
            player.getExtension<Pulse>(DarkZone::class.java)?.stop()
            player.removeExtension(DarkZone::class.java)

            if (source.interfaceId != overlayId) {
                if (source.interfaceId == -1) {
                    player.interfaceManager.closeOverlay()
                } else {
                    player.interfaceManager.openOverlay(Component(source.interfaceId))
                }
            }
            handleGasExplosion(player)
        }
    }

    override fun process(entity: Entity, event: core.game.event.Event) {
        when (event) {
            is UseWithEvent -> handleUseWith(entity, event)
            is ItemEquipEvent -> handleEquip(entity, event)
        }
    }

    private fun handleUseWith(entity: Entity, event: UseWithEvent) {
        if (entity !is Player) return

        val isTinderbox = getItemName(event.used) == "Tinderbox" || getItemName(event.with) == "Tinderbox"
        if (isTinderbox) {
            runTask(entity, 2, 1) { checkDarkArea(entity) }
        }
    }

    private fun handleEquip(entity: Entity, event: ItemEquipEvent) {
        if (entity !is Player) return
        if (event.slotId != EquipmentSlot.HEAD.ordinal) return
        val headbandId = DiaryManager(entity).headband
        if (event.itemId != headbandId) return
        runTask(entity, 2, 1) { checkDarkArea(entity) }
    }

    companion object {
        private val GENIE_CAVE_13457 = getRegionBorders(Regions.GENIE_CAVE_13457)
        private val FALADOR_MOLE_LAIR_6992 = getRegionBorders(Regions.FALADOR_MOLE_LAIR_6992)
        private val FALADOR_MOLE_LAIR_6993 = getRegionBorders(Regions.FALADOR_MOLE_LAIR_6993)
        private val SKAVID_CAVE_10131 = getRegionBorders(Regions.SKAVID_CAVE_10131)
        private val LUMBRIDGE_DUNGEON_12949 = getRegionBorders(Regions.LUMBRIDGE_DUNGEON_12949)
        private val LUMBRIDGE_DUNGEON_12950 = getRegionBorders(12950)
        private val LUMBRIDGE_DUNGEON_12693 = getRegionBorders(Regions.LUMBRIDGE_DUNGEON_12693)
        private val TEARS_OF_GUTHIX_12948 = getRegionBorders(Regions.TEARS_OF_GUTHIX_12948)
        private val DORGESHUUN_MINE_13206 = getRegionBorders(Regions.DORGESHUUN_MINE_13206)
        private val MOS_LE_HARMLESS_CAVE_15251 = getRegionBorders(Regions.MOS_LE_HARMLESS_CAVE_15251)
        private val MOS_LE_HARMLESS_CAVE_14994 = getRegionBorders(Regions.MOS_LE_HARMLESS_CAVE_14994)
        private val MOS_LE_HARMLESS_CAVE_14995 = getRegionBorders(Regions.MOS_LE_HARMLESS_CAVE_14995)
        private val DORGESH_KAAN_SOUTH_DUNGEON_10833 = getRegionBorders(Regions.DORGESH_KAAN_SOUTH_DUNGEON_10833)
        private val TEMPLE_OF_IKOV_10648 = getRegionBorders(Regions.TEMPLE_OF_IKOV_10648)

        val DARKNESS_OVERLAY = object : Component(Components.DARKNESS_DARK_96) {
            override fun open(player: Player) {
                if (player.getExtension<Pulse>(DarkZone::class.java)?.isRunning == true) return
                val pulse = object : Pulse(2, player) {
                    var count = 0
                    override fun pulse(): Boolean {
                        when (count++) {
                            0 -> sendMessage(player, "You hear tiny insects skittering over the ground...")
                            5 -> sendMessage(player, "Tiny biting insects swarm all over you!")
                            in 6..Int.MAX_VALUE -> impact(player, 1, HitsplatType.NORMAL)
                        }
                        return false
                    }
                }
                Pulser.submit(pulse)
                player.addExtension(DarkZone::class.java, pulse)
                super.open(player)
            }

            override fun close(player: Player): Boolean {
                player.getExtension<Pulse>(DarkZone::class.java)?.stop()
                return super.close(player)
            }
        }

        private fun startInsectPulse(player: Player) {
            val pulse = object : Pulse(2, player) {
                var ticks = 0
                override fun pulse(): Boolean {
                    if (LightSources.hasActiveLightSource(player)) return true
                    ticks++
                    if (ticks >= 30) return true
                    impact(player, 1, HitsplatType.NORMAL)
                    return false
                }
            }
            Pulser.submit(pulse)
            player.addExtension(DarkZone::class.java, pulse)
        }

        private fun handleGasExplosion(player: Player) {
            val source = LightSources.getActiveLightSource(player) ?: return
            val gasArea = inBorders(player, 3155, 9579, 3174, 9596) || inBorders(player, 3202, 9548, 3213, 9562)
            if (!gasArea || !source.open) return
            val itemName = getItemName(source.litId).lowercase()

            sendMessage(player, core.tools.RED + "Your $itemName flares brightly!")
            runTask(player, 7) {
                val damage = player.skills.lifepoints / 4
                playGlobalAudio(player.location, Sounds.LANTERN_EXPLODES_1583)
                impact(player, damage, HitsplatType.NORMAL)

                player.inventory.getSlot(Item(source.litId)).takeIf { it != -1 }?.let { slot ->
                    player.inventory.replace(Item(source.emptyId), slot, true)
                    sendMessage(player, "Your $itemName has gone out!")
                }

                if (!LightSources.hasActiveLightSource(player)) {
                    sendMessage(player, "Tiny insects begin to bite you in the darkness!")
                    startInsectPulse(player)
                }
            }
        }

        @JvmStatic
        fun checkDarkArea(p: Player): Boolean {
            for (r in p.zoneMonitor.zones) {
                if (r.zone is DarkZone) {
                    r.zone.updateOverlay(p)
                    return true
                }
            }
            return false
        }
    }
}
