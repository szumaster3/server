package content.global.activity.shootingstar

import core.ServerStore.Companion.getBoolean
import core.ServerStore.Companion.getInt
import core.ServerStore.Companion.getString
import core.api.sendDialogueLines
import core.api.sendNews
import core.game.bots.impl.ShootingStarBot
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location

class ShootingStar(
    var level: ShootingStarType = ShootingStarType.values().random(),
) {
    val crashLocations =
        mapOf(
            "East of Dark Wizards' Tower" to Location.create(2925, 3339, 0),
            "Crafting Guild" to Location.create(2940, 3280, 0),
            "Falador East Bank" to Location.create(3030, 3349, 0),
            "Rimmington mining site" to Location.create(2974, 3240, 0),
            "Karamja northwestern mining site" to Location.create(2737, 3223, 0),
            "Brimhaven mining site" to Location.create(2743, 3143, 0),
            "South Crandor mining site" to Location.create(2822, 3239, 0),
            "Karamja mining site" to Location.create(2854, 3032, 0),
            "Shilo Village mining site" to Location.create(2826, 2997, 0),
            "Relleka mining site" to Location.create(2682, 3700, 0),
            "Jatizso mine" to Location.create(2393, 3815, 0),
            "Lunar Isle mine" to Location.create(2140, 3939, 0),
            "Miscellania coal mine" to Location.create(2529, 3887, 0),
            "Neitiznot runite mine" to Location.create(2376, 3835, 0),
            "Ardougne mining site" to Location.create(2600, 3232, 0),
            "Ardougne eastern mine" to Location.create(2706, 3334, 0),
            "Kandarin Coal trucks" to Location.create(2589, 3485, 0),
            "Yanille Bank" to Location.create(2602, 3086, 0),
            "Port Khazard mine" to Location.create(2626, 3140, 0),
            "Al Kharid bank" to Location.create(3276, 3176, 0),
            "Al Kharid mining site" to Location.create(3296, 3297, 0),
            "Duel Arena bank chest" to Location.create(3342, 3267, 0),
            "Kharidian Desert clay mine" to Location.create(3426, 3159, 0),
            "Nardah mining site" to Location.create(3320, 2872, 0),
            "Nardah bank" to Location.create(3434, 2888, 0),
            "Granite and sandstone quarry" to Location.create(3170, 2913, 0),
            "South-east Varrock mine" to Location.create(3292, 3353, 0),
            "South-west Varrock mine" to Location.create(3176, 3362, 0),
            "Varrock east bank" to Location.create(3259, 3407, 0),
            "Lumbridge Swamp south-east mine" to Location.create(3227, 3150, 0),
            "Burgh de Rott bank" to Location.create(3500, 3219, 0),
            "Canifis Bank" to Location.create(3504, 3487, 0),
            "Mos Le'Harmless bank" to Location.create(3687, 2969, 0),
            "Gnome stronghold Bank" to Location.create(2460, 3432, 0),
            "Lletya bank" to Location.create(2329, 3163, 0),
            "Piscatoris mining site" to Location.create(2336, 3636, 0),
            "North Edgeville mining site" to Location.create(3101, 3569, 0),
            "Southern wilderness mine" to Location.create(3025, 3591, 0),
            "Wilderness Volcano bank" to Location.create(3188, 3695, 0),
            "Wilderness hobgoblin mine" to Location.create(3020, 3809, 0),
            "Pirates' Hideout mine" to Location.create(3059, 3940, 0),
            "Lava Maze mining site" to Location.create(3062, 3885, 0),
            "Mage Arena bank" to Location.create(3093, 3962, 0),
        )
    val starSprite = NPC(8091)
    var location = "Canifis Bank"
    var maxDust = level.totalStardust
    var dustLeft = level.totalStardust
    var starScenery = Scenery(level.objectId, crashLocations[location])
    var isDiscovered = false
    var ticks = 0
    var isSpawned = false
    var spriteSpawned = false
    var firstStar = true
    var selfBots = ArrayList<ShootingStarBot>()
    var activePlayers = HashSet<Player>()

    fun degrade() {
        if (level.ordinal == 0) {
            selfBots.filter { it.isMining() }.forEach { it.sleep() }
            SceneryBuilder.remove(starScenery)
            isSpawned = false
            starSprite.location = starScenery.location
            starSprite.init()
            spriteSpawned = true
            ShootingStarPlugin.getStoreFile().clear()
            return
        }
        level = getNextType()
        maxDust = level.totalStardust
        dustLeft = level.totalStardust

        ShootingStarPlugin.getStoreFile()["level"] = level.ordinal
        ShootingStarPlugin.getStoreFile()["isDiscovered"] = isDiscovered

        val newStar = Scenery(level.objectId, starScenery.location)
        SceneryBuilder.replace(starScenery, newStar)
        starScenery = newStar
    }

    private fun getNextType(): ShootingStarType {
        return ShootingStarType.values()[level.ordinal - 1]
    }

    fun fire() {
        SceneryBuilder.remove(starScenery)
        rebuildVars()
        clearSprite()
        SceneryBuilder.add(starScenery)
        if (!isSpawned) {
            (0..2).forEach { _ ->
                selfBots.add(ShootingStarBot.new())
            }
        }
        if (level.ordinal + 1 > 5) {
            selfBots.filter { it.isIdle() }.forEach { it.activate(true) }
        }
        isSpawned = true
        sendNews("A shooting star level ${level.ordinal + 1} just crashed near $location!")
    }

    fun rebuildVars() {
        if (firstStar && ShootingStarPlugin.getStoreFile().isNotEmpty()) {
            level = ShootingStarType.values()[ShootingStarPlugin.getStoreFile().getInt("level")]
            location = ShootingStarPlugin.getStoreFile().getString("location")
            isDiscovered = ShootingStarPlugin.getStoreFile().getBoolean("isDiscovered")
        } else {
            level = ShootingStarType.values().random()
            location = crashLocations.entries.random().key
            isDiscovered = false
        }

        maxDust = level.totalStardust
        dustLeft = level.totalStardust
        starScenery = Scenery(level.objectId, crashLocations[location])

        ShootingStarPlugin.getStoreFile()["level"] = level.ordinal
        ShootingStarPlugin.getStoreFile()["location"] = location
        ShootingStarPlugin.getStoreFile()["isDiscovered"] = false

        ticks = 0
        firstStar = false
    }

    fun clearSprite() {
        starSprite.clear()
        spriteSpawned = false
    }

    fun decDust() {
        if (--dustLeft <= 0) degrade()
    }

    fun mine(player: Player) {
        player.pulseManager.run(
            ShootingStarMiningPulse(player, starScenery, this),
        )
    }

    fun prospect(player: Player) {
        sendDialogueLines(
            player,
            "This is a size " + (level.ordinal + 1) + " star. A Mining level of at least " + miningLevel + " is",
            "required to mine this layer. There is $dustLeft stardust remaining",
            "until the next layer.",
        )
    }

    fun notifyNewPlayer(player: Player) {
        if (activePlayers.size < 3) {
            val leavingBot = selfBots.firstOrNull { it.isMining() }
            leavingBot?.sleep()
        }
        activePlayers.add(player)
    }

    fun notifyPlayerLeave(player: Player) {
        activePlayers.remove(player)
        if (activePlayers.size < 3) {
            val newBot = selfBots.firstOrNull { it.isIdle() }
            newBot?.activate(true)
        }
    }

    val miningLevel: Int
        get() = (level.ordinal + 1) * 10
}

enum class ShootingStarType(
    val objectId: Int,
    val exp: Int,
    val totalStardust: Int,
    val rate: Double,
) {
    LEVEL_1(38668, 14, 1200, 0.05),
    LEVEL_2(38667, 25, 700, 0.1),
    LEVEL_3(38666, 29, 439, 0.3),
    LEVEL_4(38665, 32, 250, 0.4),
    LEVEL_5(38664, 47, 175, 0.5),
    LEVEL_6(38663, 71, 80, 0.70),
    LEVEL_7(38662, 114, 40, 0.80),
    LEVEL_8(38661, 145, 25, 0.85),
    LEVEL_9(38660, 210, 15, 0.95),
}
