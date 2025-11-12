package content.global.bots

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import core.ServerConstants
import core.game.bots.AIRepository
import core.game.bots.CombatBotAssembler
import core.game.bots.Script
import core.game.interaction.DestinationFlag
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.interaction.MovementPulse
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomUtils
import java.io.File
import java.io.FileReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class Adventurer(val style: CombatStyle) : Script() {
    private var city: Location = lumbridgeTown
    private var pointOfInterest: Location = karamjaIsland
    private var grandExchangeRandomLocation: Location = getRandomGrandExchangeLocation()
    private var grandExchangeBankersNPCLocation: Location = grandExchangeNorthEastClerkLocation
    var freshSpawn = true
    var sold = false
    private var poiActive = false
    val chance: Int =
        if (grandExchangeLocations.contains(city)) AdventurerConfig.CHANCE_GE_CITY
        else AdventurerConfig.CHANCE_CITY
    var ticks = 0
    var counter = 0
    private var returnToAdventure = 0
    private var geWait = 0
    private var geLongWait = 0
    val type = when (style) {
        CombatStyle.MELEE -> CombatBotAssembler.Type.MELEE
        CombatStyle.MAGIC -> CombatBotAssembler.Type.MAGE
        CombatStyle.RANGE -> CombatBotAssembler.Type.RANGE
    }
    var state = State.START

    init {
        skills[Skills.AGILITY] = 99
        inventory.add(Item(1359))
        skills[Skills.WOODCUTTING] = 95
        inventory.add(Item(590))
        skills[Skills.FISHING] = 90
        inventory.add(Item(1271))
        skills[Skills.MINING] = 90
        skills[Skills.SLAYER] = 90
    }

    override fun toString(): String =
        "${bot.username} is an Adventurer bot at ${bot.location}! " +
                "State: $state | City: $city | Ticks: $ticks | Freshspawn: $freshSpawn | Sold: $sold | Counter: $counter"

    private fun getRandomCity(): Location = cities.random()

    private fun getRandomLocation(): Location = pointsOfInterest.random()

    private fun getRandomGrandExchangeLocation(): Location = grandExchangeRandomLocations.random()

    private fun otherPlayersNearby(): Boolean =
        RegionManager.getLocalPlayers(bot).any { it.name != bot.name }

    private fun checkNearBank() {
        bankZones[city]?.let {
            if (it.insideBorder(bot)) {
                state = State.FIND_BANK
            } else {
                scriptAPI.walkTo(it.randomLoc)
            }
        } ?: run { scriptAPI.teleport(getRandomCity().also { city = it }) }
    }

    private fun checkCounter(max: Int) {
        if (counter++ >= max) state = State.TELEPORTING
    }

    private fun teleportToRandomCity() {
        city = getRandomCity()
        val loc =
            when (city) {
                grandExchangeNorthEastClerkLocation -> scriptAPI.randomizeLocationInRanges(city, -3, 2, 0, 1, 0)
                grandExchangeSouthWestClerkLocation -> scriptAPI.randomizeLocationInRanges(city, -2, 3, -1, 0, 0)
                grandExchangeNorthWestClerkLocation -> scriptAPI.randomizeLocationInRanges(city, -2, 0, -3, 2, 0)
                grandExchangeSouthEastClerkLocation -> scriptAPI.randomizeLocationInRanges(city, 0, 2, -2, 3, 0)
                else -> scriptAPI.randomizeLocationInRanges(city, -1, 1, -1, 1, 0)
            }
        scriptAPI.teleport(loc)
    }

    private val resources = listOf("Rocks", "Tree", "Oak", "Willow", "Maple tree", "Yew", "Magic tree", "Teak", "Mahogany")

    fun immerse() {
        if (
            counter++ >=
            Random.nextInt(AdventurerConfig.LOOT_RANDOM_MIN, AdventurerConfig.LOOT_RANDOM_MAX)
        )
            state = State.TELEPORTING
        val items = AIRepository.groundItems[bot]
        when {
            Random.nextBoolean() -> {
                if (items.isNullOrEmpty()) {
                    scriptAPI.attackNpcsInRadius(bot, 8)
                    state = State.LOOT_DELAY
                }
                if (bot.inventory.isFull) checkNearBank()
            }
            else -> {
                if (bot.inventory.isFull) checkNearBank()
                else
                    scriptAPI.getNearestNodeFromList(resources, true)?.let { resource ->
                        val action = if (resource.name.contains("ocks")) "mine" else "chop down"
                        InteractionListeners.run(
                            resource.id,
                            IntType.SCENERY,
                            action,
                            bot,
                            resource
                        )
                    }
            }
        }
    }

    fun refresh() {
        scriptAPI.teleport(lumbridgeTown)
        state = State.START
    }

    override fun tick() {
        ticks++
        if (ticks >= AdventurerConfig.MAX_TICKS) {
            ticks = 0
            refresh()
            return
        }

        /*
         * Check for common stuck locations every 30 ticks.
         */

        if (ticks % 30 == 0) {
            for ((zone, resolution) in commonStuckLocations) {
                if (zone.insideBorder(bot)) {
                    resolution(this)
                    return
                }
            }
        }

        when (state) {
            State.START -> handleStart()
            State.TELEPORTING -> handleTeleporting()
            State.ADVENTURE -> handleAdventure()
            State.IDLE_GE -> handleIdleGE()
            State.FIND_GE -> handleFindGE()
            State.GE -> handleGE()
            State.FIND_BANK -> handleFindBank()
            State.FIND_CITY -> handleFindCity()
            State.LOOT -> handleLoot()
            State.LOOT_DELAY -> handleLootDelay()
        }
    }

    private fun handleStart() {
        if (freshSpawn) {
            freshSpawn = false
            scriptAPI.randomWalkTo(
                lumbridgeTown,
                RandomUtils.random(AdventurerConfig.START_RANDOM_WALK)
            )
        } else {
            state = State.TELEPORTING
        }
    }

    private fun handleTeleporting() {
        if (freshSpawn) freshSpawn = false
        teleportToRandomCity()
        poiActive = false
        sold = false
        ticks = 0
        counter = 0
        state = State.ADVENTURE
    }

    private fun handleAdventure() {
        checkCounter(AdventurerConfig.COUNTER_ADVENTURE_LIMIT)

        if (RandomUtils.random(chance) <= AdventurerConfig.DIALOGUE_NEARBY_CHANCE && otherPlayersNearby()) {
            ticks = 0
            dialogue()
        }

        /*
         * POI exploration.
         */

        if (!poiActive && RandomUtils.random(1000) <= AdventurerConfig.POI_START_CHANCE) {
            pointOfInterest = getRandomLocation()
            city = teakTrees
            poiActive = true
            scriptAPI.teleport(pointOfInterest)
            return
        }

        if (poiActive) {
            if (RandomUtils.random(1000) <= AdventurerConfig.POI_ROAM_CHANCE) immerse()
            if (RandomUtils.random(1000) <= AdventurerConfig.POI_DIALOGUE_CHANCE) dialogue()
            val roamDistancePoi = determinePoiRoamDistance()
            scriptAPI.randomWalkTo(pointOfInterest, roamDistancePoi)
        }

        /*
         * Random GE state.
         */

        if (grandExchangeLocations.contains(city) && RandomUtils.random(1000) <= AdventurerConfig.RANDOM_GE_WALK_CHANCE) {
            grandExchangeRandomLocation =
                scriptAPI.randomizeLocationInRanges(getRandomGrandExchangeLocation(), -1, 1, -1, 1, 0)
            scriptAPI.randomWalkTo(grandExchangeRandomLocation, RandomUtils.random(5))
        }

        /*
         * Random POI teleporting.
         */

        if (!poiActive && RandomUtils.random(1000) <= AdventurerConfig.POI_START_CHANCE) {
            pointOfInterest = getRandomLocation()
            city = teakTrees
            poiActive = true
            scriptAPI.teleport(pointOfInterest)
        }

        /*
         * Teleporting logic if counter exceeded.
         */

        if (counter++ >= AdventurerConfig.COUNTER_RANDOM_CITY_CHANGE && RandomUtils.random(100) <= AdventurerConfig.RANDOM_CITY_CHANGE_CHANCE) {
            city = getRandomCity()
            if (RandomUtils.random(100) % 2 == 0) {
                state = State.TELEPORTING
            } else {
                city = if (cityGroupA.contains(city)) cityGroupA.random() else cityGroupB.random()
                counter = 0
                ticks = 0
                state = State.FIND_CITY
            }
        }
    }

    private fun determinePoiRoamDistance(): Int =
        when (pointOfInterest) {
            gemRocks,
            chaosNPC,
            magicTrees,
            coalTrucks -> 7
            miningGuild,
            teakFarm,
            slayerTower -> 5
            lumberYard -> 20
            keldagrimExit,
            teakTrees -> 30
            eaglesPeek,
            isafdarForest -> 40
            treeGnome -> 50
            else -> 60
        }

    private fun handleLoot() {
        val items = AIRepository.groundItems[bot]
        if (!bot.inventory.isFull && items?.isNotEmpty() == true) {
            items.toTypedArray().forEach { scriptAPI.takeNearestGroundItem(it.id) }
        } else {
            state = State.ADVENTURE
        }
    }

    private fun handleLootDelay() {
        bot.pulseManager.run(
            object : Pulse() {
                var counter1 = 0

                override fun pulse(): Boolean {
                    if (counter1++ >= AdventurerConfig.LOOT_DELAY_PULSES) state = State.LOOT
                    return false
                }
            }
        )
    }

    private fun handleIdleGE() {
        returnToAdventure =
            Random.nextInt(AdventurerConfig.GE_IDLE_RETURN_MIN, AdventurerConfig.GE_IDLE_RETURN_MAX)
        if (counter++ >= returnToAdventure) {
            if (RandomUtils.random(100) <= 25) {
                pointOfInterest = getRandomLocation()
                city = teakTrees
                poiActive = true
                scriptAPI.teleport(pointOfInterest)
                state = State.ADVENTURE
            } else {
                state = State.TELEPORTING
            }
            counter = 0
            ticks = 0
        }

        if (grandExchangeLocations.contains(city) && RandomUtils.random(1000) <= AdventurerConfig.DIALOGUE_CHANCE_GE_NEARBY && otherPlayersNearby()) {
            ticks = 0
            dialogue
        }
    }

    private fun handleFindGE() {
        sold = false
        val ge: Scenery? = scriptAPI.getNearestNode("Desk", true) as Scenery?
        if (ge == null || bot.bank.isEmpty) {
            state = State.ADVENTURE
            return
        }

        class GEPulse : MovementPulse(bot, ge, DestinationFlag.OBJECT) {
            override fun pulse(): Boolean {
                bot.faceLocation(ge?.location)
                state = State.GE
                return true
            }
        }
        GameWorld.Pulser.submit(GEPulse())
        checkCounter(AdventurerConfig.COUNTER_FIND_GE_LIMIT)
    }

    private fun handleGE() {
        grandExchangeBankersNPCLocation = grandExchangeBankNPCLocations.random()
        geWait =
            Random.nextInt(AdventurerConfig.GE_SHORT_WAIT_MIN, AdventurerConfig.GE_SHORT_WAIT_MAX)
        geLongWait =
            Random.nextInt(AdventurerConfig.GE_LONG_WAIT_MIN, AdventurerConfig.GE_LONG_WAIT_MAX)

        if (!sold) {
            if (RandomUtils.random(500) <= AdventurerConfig.GE_WALK_CHANCE) {
                scriptAPI.randomWalkTo(grandExchangeBankersNPCLocation, RandomUtils.random(4))
            }
            if (counter++ >= geWait) {
                scriptAPI.randomWalkTo(grandExchangeBankersNPCLocation, RandomUtils.random(1))
                sold = true
                counter = 0
                ticks = 0
                scriptAPI.sellAllOnGeAdv()
                state = State.TELEPORTING
                return
            }
        } else if (counter++ >= geLongWait) {
            state = State.TELEPORTING
        }
        checkCounter(AdventurerConfig.COUNTER_GE_LIMIT)
    }

    private fun handleFindBank() {
        val bank: Scenery? = scriptAPI.getNearestNode("Bank booth", true) as Scenery?
        if (bank == null) {
            state = State.TELEPORTING
            return
        }
        if (RandomUtils.random(100) <= AdventurerConfig.DIALOGUE_CHANCE_GE_NEARBY) {
            scriptAPI.depositAtBank()
        } else {
            scriptAPI.randomWalkTo(bank.location, 3)
        }
        checkCounter(AdventurerConfig.COUNTER_FIND_BANK_LIMIT)
    }

    private fun handleFindCity() {
        if (
            counter++ >= AdventurerConfig.COUNTER_FIND_CITY_LIMIT || grandExchangeLocations.contains(city)
        ) {
            scriptAPI.teleport(getRandomCity().also { city = it })
            state = State.ADVENTURE
            return
        }
        if (bot.location == city) {
            state = State.ADVENTURE
        } else {
            scriptAPI.randomWalkTo(city, RandomUtils.random(10))
        }
        checkCounter(AdventurerConfig.COUNTER_FIND_CITY_LIMIT)
    }

    fun dialogue() {
        val until = 1225 - dateCode
        val lineStd = dialogue.getLines("standard")?.rand().orEmpty()

        val lineAlt =
            when {
                dateCode == 1031 -> dialogue.getLines("halloween")?.rand()
                until in 2..23 -> dialogue.getLines("approaching_christmas")?.rand()
                dateCode == 1225 -> dialogue.getLines("christmas_day")?.rand()
                dateCode == 1224 -> dialogue.getLines("christmas_eve")?.rand()
                dateCode == 1231 -> dialogue.getLines("new_years_eve")?.rand()
                dateCode == 101 -> dialogue.getLines("new_years")?.rand()
                dateCode == 214 -> dialogue.getLines("valentines")?.rand()
                dateCode == 404 -> dialogue.getLines("easter")?.rand()
                else -> null
            }.orEmpty()

        val localPlayer =
            RegionManager.getLocalPlayers(bot).filter { it.name != bot.name }.randomOrNull()

        val baseChat = if (lineAlt.isNotEmpty() && Random.nextBoolean()) lineAlt else lineStd
        val chat =
            baseChat
                .replace("@name", localPlayer?.username ?: "")
                .replace("@timer", until.toString())

        if (chat.isNotEmpty()) {
            scriptAPI.sendChat(chat)
        }
    }

    enum class State {
        START,
        ADVENTURE,
        FIND_BANK,
        FIND_CITY,
        IDLE_GE,
        GE,
        TELEPORTING,
        LOOT,
        LOOT_DELAY,
        FIND_GE
    }

    override fun newInstance(): Script {
        val script = Adventurer(style)
        script.state = State.START
        val tier = CombatBotAssembler.Tier.MED
        script.bot =
            when (type) {
                CombatBotAssembler.Type.RANGE ->
                    CombatBotAssembler().rangeAdventurer(tier, bot.startLocation)
                else -> CombatBotAssembler().meleeAdventurer(tier, bot.startLocation)
            }
        return script
    }

    companion object {
        /*
         * Cities.
         */

        private val yanilleCity: Location = Location.create(2615, 3104, 0)
        private val ardougneCity: Location = Location.create(2662, 3304, 0)
        private val seersVillage: Location = Location.create(2726, 3485, 0)
        private val edgevilleLocation: Location = Location.create(3088, 3486, 0)
        private val catherbyTown: Location = Location.create(2809, 3435, 0)
        private val faladorCity: Location = Location.create(2965, 3380, 0)
        private val varrockCity: Location = Location.create(3213, 3428, 0)
        private val draynorVillage: Location = Location.create(3080, 3250, 0)
        private val rimmingtonVillage: Location = Location.create(2977, 3239, 0)
        private val lumbridgeTown: Location = Location.create(3222, 3219, 0)
        private val karamjaIsland: Location = Location.create(2849, 3033, 0)
        private val alKharidCity: Location = Location.create(3297, 3219, 0)

        /*
         * Random locations.
         */

        private val feldipHills: Location = Location.create(2535, 2919, 0)
        private val isafdarForest: Location = Location.create(2241, 3217, 0)
        private val eaglesPeek: Location = Location.create(2333, 3579, 0)
        private val canifisVillage: Location = Location.create(3492, 3485, 0)
        private val treeGnome: Location = Location.create(2437, 3441, 0)
        private val teakTrees: Location = Location.create(2334, 3048, 0)
        private val teakFarm: Location = Location.create(2825, 3085, 0)
        private val keldagrimExit: Location = Location.create(2724, 3692, 0)
        private val miningGuild: Location = Location.create(3046, 9740, 0)
        private val magicTrees: Location = Location.create(2285, 3146, 0)
        private val coalTrucks: Location = Location.create(2581, 3481, 0)
        private val slayerTower: Location = Location.create(3422, 3548, 0)
        private val gemRocks: Location = Location.create(2825, 2997, 0)
        private val chaosNPC: Location = Location.create(2612, 9484, 0)
        private val lumberYard: Location = Location.create(3289, 3482, 0)
        private val taverleyVillage: Location = Location.create(2909, 3436, 0)

        private val grandExchangeSouthWestClerkLocation: Location = Location.create(3164, 3487, 0)
        private val grandExchangeNorthEastClerkLocation: Location = Location.create(3165, 3492, 0)
        private val grandExchangeNorthWestClerkLocation: Location = Location.create(3162, 3490, 0)
        private val grandExchangeSouthEastClerkLocation: Location = Location.create(3167, 3489, 0)

        private var cityGroupA = listOf(faladorCity, varrockCity, draynorVillage, rimmingtonVillage, lumbridgeTown, edgevilleLocation)
        private var cityGroupB = listOf(yanilleCity, ardougneCity, seersVillage, catherbyTown)

        private val cities = listOf(yanilleCity, ardougneCity, seersVillage, catherbyTown, faladorCity, varrockCity, draynorVillage, rimmingtonVillage, lumbridgeTown, edgevilleLocation)
        private val pointsOfInterest = listOf(karamjaIsland, alKharidCity, feldipHills, isafdarForest, eaglesPeek, canifisVillage, treeGnome, teakTrees, teakFarm, keldagrimExit, miningGuild, coalTrucks, slayerTower, magicTrees, gemRocks, chaosNPC, taverleyVillage, lumberYard)
        private val grandExchangeLocations = listOf(grandExchangeSouthWestClerkLocation, grandExchangeNorthEastClerkLocation, grandExchangeNorthWestClerkLocation, grandExchangeSouthEastClerkLocation)
        private val grandExchangeRandomLocations = listOf(Location.create(3158, 3483, 0), Location.create(3165, 3480, 0), Location.create(3172, 3483, 0), Location.create(3174, 3489, 0), Location.create(3171, 3497, 0), Location.create(3164, 3499, 0), Location.create(3157, 3497, 0), Location.create(3155, 3489, 0), Location.create(3167, 3492, 0), Location.create(3162, 3492, 0), Location.create(3162, 3487, 0), Location.create(3167, 3487, 0))
        private val grandExchangeBankNPCLocations = listOf(Location.create(3165, 3492, 0), Location.create(3164, 3492, 0), Location.create(3164, 3487, 0), Location.create(3165, 3487, 0))
        private var bankZones =
            mapOf<Location, ZoneBorders>(
                faladorCity to ZoneBorders(2950, 3374, 2943, 3368),
                varrockCity to ZoneBorders(3182, 3435, 3189, 3446),
                draynorVillage to ZoneBorders(3092, 3240, 3095, 3246),
                edgevilleLocation to ZoneBorders(3093, 3498, 3092, 3489),
                yanilleCity to ZoneBorders(2610, 3089, 2613, 3095),
                ardougneCity to ZoneBorders(2649, 3281, 2655, 3286),
                seersVillage to ZoneBorders(2729, 3493, 2722, 3490),
                catherbyTown to ZoneBorders(2807, 3438, 2811, 3441),
            )

        private val whiteWolfTopLocation = Location(2850, 3496, 0)
        private val whiteWolfTopCatherby = arrayOf(Location(2856, 3442, 0), Location(2848, 3455, 0), Location(2848, 3471, 0), Location(2848, 3487, 0))
        private val whiteWolfTopTaverley = arrayOf(Location(2872, 3425, 0), Location(2863, 3440, 0), Location(2863, 3459, 0), Location(2854, 3475, 0), Location(2859, 3488, 0))

        val commonStuckLocations =
            mapOf(
                ZoneBorders(2878, 3386, 2884, 3395) to
                        { it: Adventurer ->
                            it.scriptAPI.walkArray(
                                whiteWolfTopTaverley +
                                        whiteWolfTopLocation +
                                        whiteWolfTopCatherby.reversedArray()
                            )
                        },
                ZoneBorders(2874, 3390, 2880, 3401) to
                        { it: Adventurer ->
                            it.scriptAPI.walkArray(
                                whiteWolfTopTaverley +
                                        whiteWolfTopLocation +
                                        whiteWolfTopCatherby.reversedArray()
                            )
                        },
                ZoneBorders(2865, 3408, 2874, 3423) to
                        { it: Adventurer ->
                            it.scriptAPI.walkArray(
                                whiteWolfTopTaverley +
                                        whiteWolfTopLocation +
                                        whiteWolfTopCatherby.reversedArray()
                            )
                        },
                ZoneBorders(2855, 3454, 2852, 3450) to
                        { it: Adventurer ->
                            it.scriptAPI.walkArray(
                                whiteWolfTopTaverley +
                                        whiteWolfTopLocation +
                                        whiteWolfTopCatherby.reversedArray(),
                            )
                        },
                ZoneBorders(2861, 3425, 2867, 3432) to
                        { it: Adventurer ->
                            it.scriptAPI.walkArray(
                                whiteWolfTopCatherby +
                                        whiteWolfTopLocation +
                                        whiteWolfTopTaverley.reversedArray(),
                            )
                        },
                ZoneBorders(2863, 3441, 2859, 3438) to
                        { it: Adventurer ->
                            it.scriptAPI.walkArray(
                                whiteWolfTopCatherby +
                                        whiteWolfTopLocation +
                                        whiteWolfTopTaverley.reversedArray(),
                            )
                        },
                ZoneBorders(2937, 3356, 2936, 3353) to
                        { it: Adventurer ->
                            val wall = it.scriptAPI.getNearestNode("Crumbling wall", true)
                            if (wall == null) {
                                it.refresh()
                                it.ticks = 0
                                return@to
                            }
                            it.scriptAPI.interact(it.bot, wall, "Climb-over")
                        },
                ZoneBorders(3092, 3246, 3091, 3247) to
                        { it: Adventurer ->
                            it.scriptAPI.walkTo(Location(3093, 3243, 0))
                        },
                ZoneBorders(3140, 3468, 3140, 3468) to
                        { it: Adventurer ->
                            it.scriptAPI.walkArray(
                                arrayOf(
                                    Location.create(3135, 3516, 0),
                                    Location.create(3103, 3489, 0),
                                    Location.create(3082, 3423, 0),
                                ),
                            )
                        },
            )

        val dialogue: JsonObject
        val dateCode: Int

        init {
            val reader =
                FileReader(ServerConstants.BOT_DATA_PATH + File.separator + "bot_dialogue.json")
            val gson = Gson()
            dialogue = gson.fromJson(reader, JsonObject::class.java)
            reader.close()

            val formatter = DateTimeFormatter.ofPattern("MMdd")
            val current = LocalDateTime.now()
            val formatted: String = current.format(formatter)
            dateCode = formatted.toInt()
        }

        private fun JsonObject.getLines(category: String): JsonArray? =
            this.getAsJsonArray(category)

        private fun JsonArray.rand(): String? {
            if (this.size() == 0) return null
            val index = (0 until this.size()).random()
            val element = this.get(index)
            return if (element.isJsonPrimitive && element.asJsonPrimitive.isString) element.asString
            else element.toString()
        }
    }
}
