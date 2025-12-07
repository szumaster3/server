package core.game.world.map.zone.impl

import content.data.LightSources
import content.global.skill.crafting.CraftingDefinition
import core.api.*
import core.game.component.Component
import core.game.event.EventHook
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
class DarkZone : MapZone("Dark zone", true), EventHook<UseWithEvent> {

    override fun configure() {
        register(SKAVID_CAVE_10131)
        register(FALADOR_MOLE_LAIR_6992)
        register(FALADOR_MOLE_LAIR_6993)
        register(LUMBRIDGE_DUNGEON_12949)
        register(LUMBRIDGE_DUNGEON_12693)
        register(TEARS_OF_GUTHIX_12948)
        register(DORGESHUUN_MINE_13206)
        register(MOS_LE_HARMLESS_CAVE_15251)
        register(MOS_LE_HARMLESS_CAVE_14994)
        register(MOS_LE_HARMLESS_CAVE_14995)
        register(GENIE_CAVE_13457)
        register(DORGESH_KAAN_SOUTH_DUNGEON_10833)
        register(TEMPLE_OF_IKOV_10648)
        DORGESHUUN_MINE_13206.addException(ZoneBorders(3310, 9601,3327, 9656))
    }

    override fun continueAttack(entity: Entity, target: Node, style: CombatStyle, message: Boolean): Boolean {
        return entity !is Player || entity.interfaceManager.overlay != DARKNESS_OVERLAY
    }

    override fun interact(entity: Entity, target: Node, option: Option): Boolean {
        if (target !is Item) return false
        val player = entity.asPlayer()
        val product = LightSources.forLitId(target.id) ?: return false
        val action = option.name.lowercase(Locale.getDefault())
        val itemName = getItemName(product.litId).lowercase()

        val op = action.equalsAny("drop", "extinguish", "destroy")
        if (op) {
            sendMessage(player, "Destroying the $itemName would leave you without a light source.")
            return true
        }

        return false
    }

    override fun enter(entity: Entity): Boolean {
        if (entity is Player) {
            val player = entity.asPlayer()

            updateOverlay(player)

            val source = LightSources.getAnyActiveLightSource(player)
            if (source != null) {
                checkGasExplosion(player)
            }

            entity.hook(Event.UsedWith, this)
        }
        return true
    }

    override fun leave(entity: Entity, logout: Boolean): Boolean {
        if (entity is Player) {
            entity.interfaceManager.closeOverlay()
        }
        entity.unhook(this)
        return true
    }

    /**
     * Updates the overlay.
     *
     * @param player The player.
     */
    fun updateOverlay(player: Player) {
        val source = LightSources.getAnyActiveLightSource(player)

        var overlay = -1
        if (player.interfaceManager.overlay != null) {
            overlay = player.interfaceManager.overlay!!.id
        }

        if (source == null) {
            if (overlay != DARKNESS_OVERLAY.id) {
                player.interfaceManager.openOverlay(DARKNESS_OVERLAY)
            }
            return
        }

        val pulse = player.getExtension<Pulse>(DarkZone::class.java)
        pulse?.stop()

        if (source.interfaceId == -1) {
            if (overlay == DARKNESS_OVERLAY.id) {
                player.interfaceManager.closeOverlay()
            }
            return
        }

        if (source.interfaceId != overlay) {
            player.interfaceManager.openOverlay(Component(source.interfaceId))
        }
    }

    override fun process(entity: Entity, event: UseWithEvent) {
        val isTinderbox = getItemName(event.used) == "Tinderbox" || getItemName(event.with) == "Tinderbox"

        if (isTinderbox && entity is Player) {
            runTask(entity, 2, 1) {
                checkDarkArea(entity.asPlayer())
                Unit
            }
        }
    }

    companion object {
        private val GENIE_CAVE_13457 = getRegionBorders(Regions.GENIE_CAVE_13457)
        private val FALADOR_MOLE_LAIR_6992 = getRegionBorders(Regions.FALADOR_MOLE_LAIR_6992)
        private val FALADOR_MOLE_LAIR_6993 = getRegionBorders(Regions.FALADOR_MOLE_LAIR_6993)
        private val SKAVID_CAVE_10131 = getRegionBorders(Regions.SKAVID_CAVE_10131)
        private val LUMBRIDGE_DUNGEON_12949 = getRegionBorders(Regions.LUMBRIDGE_DUNGEON_12949)
        private val LUMBRIDGE_DUNGEON_12693 = getRegionBorders(Regions.LUMBRIDGE_DUNGEON_12693)
        private val TEARS_OF_GUTHIX_12948 = getRegionBorders(Regions.TEARS_OF_GUTHIX_12948)
        private val DORGESHUUN_MINE_13206 = getRegionBorders(Regions.DORGESHUUN_MINE_13206)
        private val MOS_LE_HARMLESS_CAVE_15251 = getRegionBorders(Regions.MOS_LE_HARMLESS_CAVE_15251)
        private val MOS_LE_HARMLESS_CAVE_14994 = getRegionBorders(Regions.MOS_LE_HARMLESS_CAVE_14994)
        private val MOS_LE_HARMLESS_CAVE_14995 = getRegionBorders(Regions.MOS_LE_HARMLESS_CAVE_14995)
        private val DORGESH_KAAN_SOUTH_DUNGEON_10833 = getRegionBorders(Regions.DORGESH_KAAN_SOUTH_DUNGEON_10833)
        private val TEMPLE_OF_IKOV_10648 = getRegionBorders(Regions.TEMPLE_OF_IKOV_10648)

        /**
         * Checks if the player has any items that provide lit light source.
         *
         * @param player The player whose equipment is checked.
         * @return Boolean indicating whether the player has an unlimited light source.
         */
        private fun alwaysLit(player: Player): Boolean =
            player.equipment.containsAtLeastOneItem(Item(DiaryManager(player).headband))

        /**
         * Checks if the players active light source causes a gas explosion
         * in the Lumbridge Swamp Caves.
         */
        private fun checkGasExplosion(player: Player) {
            val source = LightSources.getActiveLightSource(player) ?: return
            val gasExplosionArea =
                inBorders(player, 3155, 9579, 3174, 9596) ||
                        inBorders(player, 3202, 9548, 3213, 9562)

            if (!gasExplosionArea || !source.open) return

            sendMessage(player, core.tools.RED + "Your ${source.name.lowercase()} flares brightly!")

            runTask(player, 7) {
                val damage = player.skills.lifepoints / 4
                playGlobalAudio(player.location, Sounds.LANTERN_EXPLODES_1583)
                impact(player, damage, HitsplatType.NORMAL)

                val slot = player.inventory.getSlot(Item(source.litId))
                if (slot != -1) {
                    player.inventory.replace(Item(source.emptyId), slot, true)
                    sendMessage(player, "Your ${source.name.lowercase()} has gone out!")
                }

                if (!LightSources.hasActiveLightSource(player)) {
                    sendMessage(player, "Tiny insects begin to bite you in the darkness!")
                    startInsectAttack(player)
                }
            }
        }

        /**
         * Starts insect damage.
         */
        private fun startInsectAttack(player: Player) {
            Pulser.submit(object : Pulse(2, player) {
                var ticks = 0
                override fun pulse(): Boolean {
                    if (LightSources.hasActiveLightSource(player)) return true
                    ticks++
                    if (ticks >= 30) return true
                    impact(player, 1, HitsplatType.NORMAL)
                    return false
                }
            })
        }

        /*
         * fun updateOverlay(player: Player) {
         *     val localPlayers = RegionManager.getLocalPlayers(player, 15)
         *     val activeLightCount = localPlayers.count { LightSource.hasActiveLightSource(it) || alwaysLit(it) }
         *
         *     val overlayId = when {
         *         activeLightCount >= 3 -> Components.DARKNESS_LIGHT_97
         *         activeLightCount == 2 -> Components.DARKNESS_MEDIUM_98
         *         activeLightCount == 1 -> Components.DARKNESS_DARK_96
         *         else -> DARKNESS_OVERLAY.id
         *     }
         *
         *     player.interfaceManager.openOverlay(Component(overlayId))
         * }
         */

        /**
         * The darkness overlay component shown when no light is active.
         */
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

        /**
         * Checks if the player is within any DarkZone and updates their overlay.
         */
        fun checkDarkArea(player: Player): Boolean {
            player.zoneMonitor.zones.forEach {
                val zone = it.zone
                if (zone is DarkZone && !alwaysLit(player)) {
                    zone.updateOverlay(player)
                    return true
                }
            }
            return false
        }
    }

    /**
     * Helper for string comparison ignoring case.
     */
    private fun String.equalsAny(vararg options: String): Boolean =
        options.any { equals(it, ignoreCase = true) }
}
