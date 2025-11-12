package content.global.bots

import core.api.produceGroundItem
import core.game.bots.Script
import core.game.bots.SkillingBotAssembler
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import shared.consts.Items
import shared.consts.Scenery
import kotlin.random.Random

class ArdougneStallThief : Script() {

    private var state = State.INIT
    private val stealZone = ZoneBorders(2652, 3295, 2669, 3316)
    private val stallId = Scenery.BAKER_S_STALL_34384

    private val foodItems = listOf(
        Items.CAKE_1891, Items.BREAD_2309, Items.CHOCOLATE_SLICE_1901
    ).map { Item(it) }

    private var nextStealTick: Long = 0
    private var lastWalkTick: Long = 0
    private var walkDelay: Long = Random.nextLong(1000, 2000)
    private var targetStallLoc: Location? = null
    private var currentDestination: Location? = null

    override fun tick() {
        val now = System.currentTimeMillis()

        val hpThreshold = bot.skills.lifepoints * 0.1
        val low = bot.skills.lifepoints <= hpThreshold
        if (low || foodItems.any { bot.inventory.containsItem(it) }) {
            foodItems.firstOrNull { bot.inventory.containsItem(it) }?.let { scriptAPI.eat(it.id) }
        }

        when (state) {
            State.INIT -> handleStealState(now)
            State.RETURN -> handleReturnState(now)
        }
    }

    private fun handleStealState(now: Long) {
        if (!stealZone.insideBorder(bot)) {
            walkToRandomLocation(stealZone, now)
            return
        }

        if (targetStallLoc == null) {
            val stallNode = scriptAPI.getNearestNode(stallId, true) ?: return
            targetStallLoc = stallNode.location
        }

        if (now < nextStealTick) return

        if (!bot.location.withinDistance(targetStallLoc!!, 1)) {
            walkTo(targetStallLoc!!, now)
        } else {
            val stallNode = scriptAPI.getNearestNode(stallId, true) ?: return
            InteractionListeners.run(stallId, IntType.SCENERY, "steal-from", bot, stallNode)
            nextStealTick = now + Random.nextLong(2000, 4000)
        }

        if (bot.inventory.isFull) {
            val stealable = bot.inventory.toArray()
                .filterNotNull()
                .filter { it.id in foodItems.map { f -> f.id } }

            stealable.forEach { item ->
                repeat(item.amount) {
                    bot.inventory.remove(Item(item.id, 1))
                    produceGroundItem(bot, item.id, 1, bot.location)
                }
            }
        }
    }

    private fun handleReturnState(now: Long) {
        if (!stealZone.insideBorder(bot)) {
            walkToRandomLocation(stealZone, now)
        } else {
            currentDestination = null
            state = State.INIT
        }
    }

    private fun walkToRandomLocation(zone: ZoneBorders, now: Long) {
        walkTo(zone.randomLoc, now)
    }

    private fun walkTo(loc: Location, now: Long) {
        if (currentDestination == loc) return
        if (now - lastWalkTick < walkDelay) return
        scriptAPI.walkTo(loc)
        currentDestination = loc
        lastWalkTick = now
        walkDelay = Random.nextLong(1000, 2000)
    }

    override fun newInstance(): Script {
        val script = ArdougneStallThief()
        script.bot =
            SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.AVERAGE, bot.startLocation)
        return script
    }

    init {
        skills[Skills.ATTACK] = 25
        skills[Skills.STRENGTH] = 45
        skills[Skills.DEFENCE] = 35
        skills[Skills.THIEVING] = 25
    }

    enum class State {
        INIT,
        RETURN
    }
}
