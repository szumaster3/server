package content.global.travel.charter

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.tools.StringUtils
import shared.consts.Components
import shared.consts.Vars

/**
 * Represents various charter routes with their data.
 */
enum class CharterShip(val location: Location, val config: Int, val delay: Int, val destination: String) {
    PORT_SARIM_TO_ENTRANA(Location.create(2834, 3331, 1), 1, 15, "Entrana"),
    ENTRANA_TO_PORT_SARIM(Location.create(3048, 3234, 0), 2, 15, "Port Sarim"),
    PORT_SARIM_TO_CRANDOR(Location.create(2849, 3238, 0), 3, 12, "Crandor"),
    CRANDOR_TO_PORT_SARIM(Location.create(2834, 3335, 0), 4, 13, "Port Sarim"),
    PORT_SARIM_TO_KARAMJA(Location.create(2956, 3143, 1), 5, 9, "Karamja"),
    MUSA_POINT_TO_PORT_SARIM(Location.create(3029, 3217, 0), 6, 8, "Port Sarim"),
    ARDOUGNE_TO_BRIMHAVEN(Location.create(2775, 3234, 1), 7, 4, "Brimhaven"),
    BRIMHAVEN_TO_ARDOUGNE(Location.create(2683, 3268, 1), 8, 4, "Ardougne"),
    CAIRN_ISLAND_TO_PORT_KHAZARD(Location.create(2676, 3170, 0), 10, 8, "Port Khazard"),
    PORT_KHAZARD_TO_SHIPYARD(Location.create(2998, 3043, 0), 11, 23, "the Ship Yard"),
    SHIPYARD_TO_PORT_KHAZARD(Location.create(2676, 3170, 0), 12, 23, "Port Khazard"),
    PORT_SARIM(Location.create(3048, 3234, 0), 13, 17, "Port Sarim"),
    PORT_SARIM_TO_PEST_CONTROL(Location.create(2663, 2676, 1), 14, 12, "Pest Control"),
    PEST_TO_PORT_SARIM(Location.create(3041, 3198, 1), 15, 12, "Port Sarim"),
    FELDIP_TO_KARAMJA(Location.create(2763, 2956, 0), 16, 10, "Karamja"),
    KARAMJA_TO_FELDIP(Location.create(2763, 2956, 0), 17, 10, "Feldip"),
    ;

    /**
     * Starts the sailing pulse on this route.
     */
    fun sail(player: Player) {
        submitIndividualPulse(player, CharterPulse(player, this))
    }

    companion object {
        /**
         * Starts the sailing pulse.
         */
        fun sail(player: Player, charter: CharterShip) {
            submitIndividualPulse(player, CharterPulse(player, charter))
        }
    }
}

/**
 * Represents the charter pulse.
 */
private class CharterPulse(private val player: Player, private val charter: CharterShip) : Pulse(1) {
    private var counter = 0

    override fun pulse(): Boolean {
        counter++

        when {
            counter == 1 -> prepare()
            counter == 2 && charter != CharterShip.PORT_SARIM_TO_CRANDOR -> {
                player.properties.teleportLocation = charter.location
            }
            counter >= charter.delay -> {
                arrive()
                return true
            }
        }

        return false
    }

    private fun prepare() {
        lock(player, charter.delay + 1)
        openInterface(player, Components.SHIP_MAP_299)
        setMinimapState(player, 2)
        setVarp(player, Vars.VARP_IFACE_CHARTER_CONFIG_75, charter.config)
    }

    private fun arrive() {
        unlock(player)
        setVarp(player, Vars.VARP_IFACE_CHARTER_CONFIG_75, 0)
        closeInterface(player)
        setMinimapState(player, 0)

        val destination = StringUtils.formatDisplayName(charter.destination)
        if (destination != "crandor") {
            sendDialogue(player, "The ship arrives at $destination.")
        } else {
            openInterface(player, Components.SOULBANE_DARKNESS_317)
            setMinimapState(player, 2)
            openOverlay(player, Components.DRAGON_SLAYER_QIP_CR_JOURNEY_544)
            openInterface(player, Components.SOULBANE_DARKNESS_317)
        }

        when (charter) {
            CharterShip.MUSA_POINT_TO_PORT_SARIM -> finishDiaryTask(player, DiaryType.KARAMJA, 0, 3)
            CharterShip.BRIMHAVEN_TO_ARDOUGNE -> finishDiaryTask(player, DiaryType.KARAMJA, 0, 4)
            CharterShip.CAIRN_ISLAND_TO_PORT_KHAZARD -> finishDiaryTask(player, DiaryType.KARAMJA, 1, 6)
            CharterShip.PORT_SARIM_TO_ENTRANA -> finishDiaryTask(player, DiaryType.FALADOR, 0, 14)
            else -> {}
        }
    }
}