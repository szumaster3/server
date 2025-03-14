package content.global.travel.charter

import core.api.submitIndividualPulse
import core.game.node.entity.player.Player
import core.game.world.map.Location

enum class Charter(
    @JvmField val location: Location,
    val config: Int,
    val delay: Int,
    val destination: String,
) {
    PORT_SARIM_TO_ENTRANA(
        location = Location.create(2834, 3331, 1),
        config = 1,
        delay = 15,
        destination = "Entrana",
    ),
    ENTRANA_TO_PORT_SARIM(
        location = Location.create(3048, 3234, 0),
        config = 2,
        delay = 15,
        destination = "Port Sarim",
    ),
    PORT_SARIM_TO_CRANDOR(
        location = Location.create(2849, 3238, 0),
        config = 3,
        delay = 12,
        destination = "Crandor",
    ),
    CRANDOR_TO_PORT_SARIM(
        location = Location.create(2834, 3335, 0),
        config = 4,
        delay = 13,
        destination = "Port Sarim",
    ),
    PORT_SARIM_TO_KARAMJA(
        location = Location.create(2956, 3143, 1),
        config = 5,
        delay = 9,
        destination = "Karamja",
    ),
    MUSA_POINT_TO_PORT_SARIM(
        location = Location.create(3029, 3217, 0),
        config = 6,
        delay = 8,
        destination = "Port Sarim",
    ),
    ARDOUGNE_TO_BRIMHAVEN(
        location = Location.create(2775, 3234, 1),
        config = 7,
        delay = 4,
        destination = "Brimhaven",
    ),
    BRIMHAVEN_TO_ARDOUGNE(
        location = Location.create(2683, 3268, 1),
        config = 8,
        delay = 4,
        destination = "Ardougne",
    ),
    CAIRN_ISLAND_TO_PORT_KHAZARD(
        location = Location.create(2676, 3170, 0),
        config = 10,
        delay = 8,
        destination = "Port Khazard",
    ),
    PORT_KHAZARD_TO_SHIPYARD(
        location = Location.create(2998, 3043, 0),
        config = 11,
        delay = 23,
        destination = "the Ship Yard",
    ),
    SHIPYARD_TO_PORT_KHAZARD(
        location = Location.create(2676, 3170, 0),
        config = 12,
        delay = 23,
        destination = "Port Khazard",
    ),
    PORT_SARIM(
        location = Location.create(3048, 3234, 0),
        config = 13,
        delay = 17,
        destination = "Port Sarim",
    ),
    PORT_SARIM_TO_PEST_CONTROL(
        location = Location.create(2663, 2676, 1),
        config = 14,
        delay = 12,
        destination = "Pest Control",
    ),
    PEST_TO_PORT_SARIM(
        location = Location.create(3041, 3198, 1),
        config = 15,
        delay = 12,
        destination = "Port Sarim",
    ),
    FELDIP_TO_KARAMJA(
        location = Location.create(2763, 2956, 0),
        config = 16,
        delay = 10,
        destination = "Karamja",
    ),
    KARAMJA_TO_FELDIP(
        location = Location.create(2763, 2956, 0),
        config = 17,
        delay = 10,
        destination = "Feldip",
    ),
    ;

    fun sail(player: Player) {
        submitIndividualPulse(player, CharterPulse(player, this))
    }

    companion object {
        fun sail(
            player: Player,
            charter: Charter,
        ) {
            submitIndividualPulse(player, CharterPulse(player, charter))
        }
    }
}
