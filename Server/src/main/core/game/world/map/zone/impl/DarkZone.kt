package core.game.world.map.zone.impl

import content.data.LightSource
import content.data.LightSource.Companion.getActiveLightSource
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
import shared.consts.Components
import java.util.*

/**
 * Handles dark environments where light sources are required.
 *
 * Automatically applies and updates darkness overlays depending on the players active light
 * source or equipment (e.g., lamps, lanterns, or headbands).
 *
 * TODO:
 *  - [ ] Falling into the water also extinguishes the light, if a player fails an Agility shortcut.
 *  - [ ] Scarab mages can occasionally have all extinguishable light sources go out ("Your lights have been magically extinguished.").
 *  - [ ] The giant mole might extinguish any candles, torches, or oil lamps when it digs away.
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
    }

    override fun continueAttack(entity: Entity, target: Node, style: CombatStyle, message: Boolean): Boolean {
        return entity !is Player || entity.interfaceManager.overlay != DARKNESS_OVERLAY
    }

    override fun interact(entity: Entity, target: Node, option: Option): Boolean {
        if (target !is Item) return false
        val player = entity.asPlayer()
        val product = LightSource.forProductId(target.id) ?: return false
        val action = option.name.lowercase(Locale.getDefault())

        val op = action.equalsAny("drop", "extinguish", "destroy")
        if(op){
            sendMessage(player, "Destroying the ${product.name.lowercase()} would leave you without a light source.")
            return true
        }

        return false
    }

    override fun enter(entity: Entity): Boolean {
        if (entity is Player) {
            val player = entity.asPlayer()

            val source = getActiveLightSource(player)

            if (source != null && source.interfaceId > 0) {
                player.interfaceManager.openOverlay(Component(source.interfaceId))
            } else {
                player.interfaceManager.openOverlay(DARKNESS_OVERLAY)
            }

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
        val source = getActiveLightSource(player)

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
        if (source.interfaceId != overlay) {
            if (source.interfaceId == -1) {
                player.interfaceManager.closeOverlay()
                return
            }
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
        private val GENIE_CAVE_13457 = getRegionBorders(13457)
        private val FALADOR_MOLE_LAIR_6992 = getRegionBorders(6992)
        private val FALADOR_MOLE_LAIR_6993 = getRegionBorders(6993)
        private val SKAVID_CAVE_10131 = getRegionBorders(10131)
        private val LUMBRIDGE_DUNGEON_12949 = getRegionBorders(12949)
        private val LUMBRIDGE_DUNGEON_12693 = getRegionBorders(12693)
        private val TEARS_OF_GUTHIX_12948 = getRegionBorders(12948)
        private val DORGESHUUN_MINE_13206 = getRegionBorders(13206)
        private val MOS_LE_HARMLESS_CAVE_15251 = getRegionBorders(15251)
        private val MOS_LE_HARMLESS_CAVE_14994 = getRegionBorders(14994)
        private val MOS_LE_HARMLESS_CAVE_14995 = getRegionBorders(14995)
        private val DORGESH_KAAN_SOUTH_DUNGEON_10833 = getRegionBorders(10833)
        private val TEMPLE_OF_IKOV_10648 = getRegionBorders(10648)

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
            val source = LightSource.getActiveLightSource(player) ?: return
            //https://oldschool.runescape.wiki/w/Light_sources#/media/File:Lumbridge_swamp_caves_hazards.png
            val gasExplosionArea =
            inBorders(player, 3155, 9579, 3174, 9596) ||
            inBorders(player, 3202, 9548, 3213, 9562)

            if (!gasExplosionArea || !source.open) return

            sendMessage(player, core.tools.RED + "Your ${source.name.lowercase()} flares brightly!")

            // 4.2s.
            runTask(player, 7) {
                val damage = player.skills.lifepoints / 4 // 25% damage.
                impact(player, damage, HitsplatType.NORMAL)

                val slot = player.inventory.getSlot(Item(source.product))
                if (slot != -1) {
                    player.inventory.replace(Item(source.raw), slot, true)
                    sendMessage(player, "Your ${source.name.lowercase()} has gone out!")
                }

                if (!LightSource.hasActiveLightSource(player)) {
                    sendMessage(player, "Tiny insects begin to bite you in the darkness!")
                    startInsectAttack(player)
                }
            }
        }

        /**
         * Starts insect damage.
         */
        private fun startInsectAttack(player: Player) {
            Pulser.submit(object : Pulse(2, player) { // 2 tick delay
                var ticks = 0
                override fun pulse(): Boolean {
                    if (LightSource.hasActiveLightSource(player)) return true
                    ticks++
                    if (ticks >= 30) return true  // 30 ticks ~ 18s
                    impact(player, 1, HitsplatType.NORMAL)
                    return false
                }
            })
        }

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
