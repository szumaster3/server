package core.game.worldevents.events

import core.game.worldevents.events.christmas.randoms.*
import core.game.worldevents.events.halloween.randoms.*

enum class HolidayRandomEvents(
    val npc: HolidayRandomEventNPC,
    val type: String,
) {
    BLACK_CAT(
        npc = BlackCatHolidayRandomNPC(),
        "halloween",
    ),

    SPIDER(
        npc = SpiderHolidayRandomNPC(),
        "halloween",
    ),

    GHOST(
        npc = GhostHolidayRandomNPC(),
        "halloween",
    ),

    ZOMBIE(
        npc = ZombieHolidayRandomNPC(),
        "halloween",
    ),

    WITCH(
        npc = WitchHolidayRandomNPC(),
        "halloween",
    ),

    DEATH(
        npc = DeathHolidayRandomNPC(),
        "halloween",
    ),

    VAMPIRE(
        npc = VampireHolidayRandomNPC(),
        "halloween",
    ),

    CHOIR(
        npc = ChoirHolidayRandomNPC(),
        "christmas",
    ),

    SANTA(
        npc = SantaHolidayRandomNPC(),
        "christmas",
    ),

    SNOWMAN_FIGHT(
        npc = SnowmanFightHolidayRandom(),
        "christmas",
    ),

    JACK_FROST(
        npc = JackFrostHolidayRandomNPC(),
        "christmas",
    ),

    SNOWMAN(
        npc = SnowmanHolidayRandomNPC(),
        "christmas",
    ),

    SNOWSTORM(
        npc = SnowStormHolidayRandomNPC(),
        "christmas",
    ),

    COOK(
        npc = CookHolidayRandomNPC(),
        "christmas",
    ),
    ;

    companion object {
        // List to hold Halloween events
        @JvmField
        val halloweenEventsList = ArrayList<HolidayRandomEvents>()

        // List to hold Christmas events
        val christmasEventsList = ArrayList<HolidayRandomEvents>()

        // List of holiday random event IDs
        val holidayRandomIDs = HolidayRandomEvents.values().map { it.npc.id }.toList()

        init {
            // Populate the event lists with corresponding events
            populateMappings()
        }

        fun getHolidayRandom(type: String): HolidayRandomEvents {
            return when (type) {
                "halloween" -> halloweenEventsList.random() // Return a random Halloween event
                "christmas" -> christmasEventsList.random() // Return a random Christmas event
                else -> throw Exception("Invalid event type!") // Throw an exception for invalid types
            }
        }

        // Populates the Halloween and Christmas event lists
        private fun populateMappings() {
            for (event in values()) {
                when (event.type) {
                    "halloween" -> halloweenEventsList.add(event) // Add Halloween events to the list
                    "christmas" -> christmasEventsList.add(event) // Add Christmas events to the list
                }
            }
        }
    }
}
