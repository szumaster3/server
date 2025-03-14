package core.game.bots

import content.data.consumables.Consumables
import content.data.consumables.effects.HealingEffect
import core.ServerConstants
import core.ServerConstants.Companion.SERVER_GE_NAME
import core.api.item.itemDefinition
import core.api.log
import core.api.sendNews
import core.api.utils.Vector
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.consumable.Consumable
import core.game.consumable.Food
import core.game.ge.GrandExchange
import core.game.ge.GrandExchangeOffer
import core.game.interaction.*
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.config.ItemConfigParser
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import core.game.world.repository.Repository
import core.game.world.update.flag.EntityFlag
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.ChatMessage
import core.game.world.update.flag.context.Graphics
import core.tools.Log
import core.tools.RandomFunction
import core.tools.colorize
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs.consts.Items
import java.util.concurrent.CountDownLatch
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class ScriptAPI(
    private val bot: Player,
) {
    val GRAPHICSUP = Graphics(1576)
    val ANIMATIONUP = Animation(8939)
    val GRAPHICSDOWN = Graphics(1577)
    val ANIMATIONDOWN = Animation(8941)

    fun distance(
        n1: Node,
        n2: Node,
    ): Double {
        return sqrt(
            (n1.location.x - n2.location.x.toDouble()).pow(2.0) +
                (n2.location.y - n1.location.y.toDouble()).pow(
                    2.0,
                ),
        )
    }

    fun interact(
        bot: Player,
        node: Node?,
        option: String,
    ) {
        if (node == null) return

        val type =
            when (node) {
                is Scenery -> IntType.SCENERY
                is NPC -> IntType.NPC
                is Item -> IntType.ITEM
                else -> null
            } ?: return
        val opt: Option? =
            node.interaction.options
                .filter { it != null && it.name.equals(option, true) }
                .firstOrNull()

        if (opt == null) {
            log(this::class.java, Log.WARN, "Invalid option name provided: $option")
            return
        }

        if (!InteractionListeners.run(node.id, type, option, bot, node)) node.interaction.handle(bot, opt)
    }

    fun useWith(
        bot: Player,
        itemId: Int,
        node: Node?,
    ) {
        if (node == null) return

        val type =
            when (node) {
                is Scenery -> IntType.SCENERY
                is NPC -> IntType.NPC
                is Item -> IntType.ITEM
                else -> null
            } ?: return

        val item = bot.inventory.getItem(Item(itemId))

        val childNode = node.asScenery()?.getChild(bot)

        if (InteractionListeners.run(item, node, type, bot)) {
            return
        }
        if (childNode != null && childNode.id != node.id) {
            if (InteractionListeners.run(item, childNode, type, bot)) {
                return
            }
        }
        val flipped = type == IntType.ITEM && item.id < node.id
        val event =
            if (flipped) {
                NodeUsageEvent(bot, 0, node, item)
            } else {
                NodeUsageEvent(bot, 0, item, childNode ?: node)
            }
        if (PluginInteractionManager.handle(bot, event)) {
            return
        }
        UseWithHandler.run(event)
    }

    fun sendChat(message: String) {
        bot.sendChat(message)
        bot.updateMasks.register(EntityFlag.Chat, ChatMessage(bot, message, 0, 0))
    }

    fun getNearestNodeFromList(
        acceptedNames: List<String>,
        isObject: Boolean,
    ): Node? {
        if (isObject) {
            return processEvaluationList(
                RegionManager.forId(bot.location.regionId).planes[bot.location.z].objectList,
                acceptedName = acceptedNames,
            )
        } else {
            return processEvaluationList(
                RegionManager.forId(bot.location.regionId).planes[bot.location.z].entities,
                acceptedName = acceptedNames,
            )
        }
    }

    fun getNearestNode(
        id: Int,
        isObject: Boolean,
    ): Node? {
        if (isObject) {
            return processEvaluationList(
                RegionManager.forId(bot.location.regionId).planes[bot.location.z].objectList,
                acceptedId = id,
            )
        } else {
            return processEvaluationList(
                RegionManager.forId(bot.location.regionId).planes[bot.location.z].entities,
                acceptedId = id,
            )
        }
    }

    fun getNearestNode(entityName: String): Node? {
        return processEvaluationList(
            RegionManager.forId(bot.location.regionId).planes[bot.location.z].entities,
            acceptedName = listOf(entityName),
        )
    }

    fun getNearestNode(
        name: String,
        isObject: Boolean,
    ): Node? {
        if (isObject) {
            return processEvaluationList(
                RegionManager.forId(bot.location.regionId).planes[bot.location.z].objectList,
                acceptedName = listOf(name),
            )
        } else {
            return processEvaluationList(
                RegionManager.forId(bot.location.regionId).planes[bot.location.z].entities,
                acceptedName = listOf(name),
            )
        }
    }

    fun getNearestObjectByPredicate(predicate: (Node?) -> Boolean): Node? {
        return processEvaluationList(
            RegionManager.forId(bot.location.regionId).planes[bot.location.z].objectList,
            acceptedPredicate = predicate,
        )
    }

    fun evaluateViability(
        e: Node?,
        minDistance: Double,
        maxDistance: Double,
        acceptedNames: List<String>? = null,
        acceptedId: Int = -1,
        acceptedPredicate: ((Node?) -> Boolean)? = null,
    ): Boolean {
        if (e == null || !e.isActive) {
            return false
        }
        if (acceptedId != -1 && e.id != acceptedId) {
            return false
        }

        val dist = distance(bot, e)
        if (dist > maxDistance || dist > minDistance) {
            return false
        }

        if (acceptedPredicate != null) {
            return acceptedPredicate(e) && !Pathfinder.find(bot, e).isMoveNear
        } else {
            val name = e?.name
            return (
                acceptedNames?.stream()?.anyMatch({ s -> s.equals(name, true) }) ?: true &&
                    !Pathfinder
                        .find(
                            bot,
                            e,
                        ).isMoveNear
            )
        }
    }

    fun processEvaluationList(
        list: List<Node>,
        acceptedName: List<String>? = null,
        acceptedId: Int = -1,
        acceptedPredicate: ((Node?) -> Boolean)? = null,
    ): Node? {
        var entity: Node? = null
        var minDistance = Double.MAX_VALUE
        val maxDistance = ServerConstants.MAX_PATHFIND_DISTANCE.toDouble()
        for (e in list) {
            if (evaluateViability(e, minDistance, maxDistance, acceptedName, acceptedId, acceptedPredicate)) {
                entity = e
                minDistance = distance(bot, e)
            }
        }
        return entity
    }

    private fun getNearestGroundItem(id: Int): GroundItem? {
        var distance = 11.0
        var closest: GroundItem? = null
        if (AIRepository.getItems(bot) != null) {
            for (item in AIRepository.getItems(bot)!!.filter { it: GroundItem -> it.distance(bot.location) < 10 }) {
                if (item.id == id) {
                    closest = item
                }
            }
            if (!GroundItemManager.getItems().contains(closest)) {
                AIRepository.getItems(bot)?.remove(closest)
                return null
            }
        } else {
            val items: ArrayList<GroundItem>? = bot.getAttribute("botting:drops", null)
            if (items != null) {
                for (item in items.filter { it.distance(bot.location) < 10 }) {
                    if (item.id == id) {
                        return item.also {
                            items.remove(item)
                            bot.setAttribute("botting:drops", items)
                        }
                    }
                }
            }
        }
        return closest
    }

    fun takeNearestGroundItem(id: Int): Boolean {
        val item = getNearestGroundItem(id)
        if (item != null) {
            item.interaction?.handle(bot, item.interaction[2])
            return true
        } else {
            return false
        }
    }

    fun getNearestGameObject(
        loc: Location,
        objectId: Int,
    ): Scenery? {
        var nearestObject: Scenery? = null
        val minDistance = Double.MAX_VALUE
        for (o in RegionManager.forId(loc.regionId).planes[0].objects) {
            for (obj in o) {
                if (obj != null) {
                    if (distance(loc, obj) < minDistance && obj.id == objectId) {
                        nearestObject = obj
                    }
                }
            }
        }
        return nearestObject
    }

    private fun findTargets(
        entity: Entity,
        radius: Int,
        name: String? = null,
    ): List<Entity>? {
        val targets: MutableList<Entity> = ArrayList()
        val localNPCs: Array<Any> = RegionManager.getLocalNpcs(entity, radius).toTypedArray()
        var length = localNPCs.size
        if (length > 5) {
            length = 5
        }
        for (i in 0 until length) {
            val npc = localNPCs[i] as NPC
            run { if (checkValidTargets(npc, name)) targets.add(npc) }
        }
        return if (targets.size == 0) null else targets
    }

    private fun checkValidTargets(
        target: NPC,
        name: String?,
    ): Boolean {
        if (!target.isActive) {
            return false
        }
        if (!target.properties.isMultiZone && target.inCombat()) {
            return false
        }
        if (name != null) {
            if (target.name != name) {
                return false
            }
        }
        return target.definition.hasAction("attack")
    }

    fun attackNpcsInRadius(
        bot: Player,
        radius: Int,
    ): Boolean {
        if (bot.inCombat()) return true
        var creatures: List<Entity>? = findTargets(bot, radius) ?: return false
        bot.attack(creatures!![RandomFunction.getRandom(creatures.size - 1)])
        return if (creatures.isNotEmpty()) {
            true
        } else {
            creatures = findTargets(bot, radius)
            if (!creatures!!.isEmpty()) {
                bot.attack(creatures[RandomFunction.getRandom(creatures.size - 1)])
                return true
            }
            false
        }
    }

    fun walkTo(loc: Location) {
        if (!bot.walkingQueue.isMoving) {
            walkToIterator(loc)
        }
    }

    fun walkArray(steps: Array<Location>) {
        bot.pulseManager.run(
            object : Pulse() {
                var stepIndex = 0

                override fun pulse(): Boolean {
                    if (stepIndex >= steps.size) return true
                    if (bot.location.withinDistance(steps[steps.size - 1], 2)) {
                        return true
                    }
                    if (!bot.location.withinDistance(steps[stepIndex], 5)) {
                        walkTo(steps[stepIndex])
                        return false
                    }
                    stepIndex++

                    return false
                }
            },
        )
    }

    fun randomWalkTo(
        loc: Location,
        radius: Int,
    ) {
        if (!bot.walkingQueue.isMoving) {
            var newloc =
                loc.transform(RandomFunction.random(radius, -radius), RandomFunction.random(radius, -radius), 0)
            walkToIterator(newloc)
        }
    }

    fun randomizeLocationInRanges(
        location: Location,
        xMin: Int,
        xMax: Int,
        yMin: Int,
        yMax: Int,
        staticZ: Int,
    ): Location {
        val newX = location.x + Random.nextInt(xMin, xMax)
        val newY = location.y + Random.nextInt(yMin, yMax)
        return Location(newX, newY, staticZ)
    }

    private fun walkToIterator(loc: Location) {
        var diffX = loc.x - bot.location.x
        var diffY = loc.y - bot.location.y

        val vec = Vector.betweenLocs(bot.location, loc)
        val norm = vec.normalized()
        val tiles = kotlin.math.min(kotlin.math.floor(vec.magnitude()).toInt(), ServerConstants.MAX_PATHFIND_DISTANCE - 1)
        val loc = bot.location.transform(norm * tiles)
        bot.pulseManager.run(
            object : MovementPulse(bot, loc) {
                override fun pulse(): Boolean {
                    return true
                }
            },
        )
    }

    fun attackNpcInRadius(
        bot: Player,
        name: String,
        radius: Int,
    ): Boolean {
        if (bot.inCombat()) return true
        var creatures: List<Entity>? = findTargets(bot, radius, name) ?: return false
        bot.attack(creatures!![RandomFunction.getRandom(creatures.size - 1)])
        return if (creatures.isNotEmpty()) {
            true
        } else {
            creatures = findTargets(bot, radius, name)
            if (!creatures!!.isEmpty()) {
                bot.attack(creatures.random())
                return true
            }
            false
        }
    }

    fun GroundItem.distance(loc: Location): Double {
        return location.getDistance(loc)
    }

    fun teleportToGE(): Boolean {
        if (bot.isTeleBlocked) {
            return false
        }
        bot.lock()
        bot.visualize(ANIMATIONUP, GRAPHICSUP)
        bot.impactHandler.disabledTicks = 4
        val location = Location.create(3165, 3482, 0)
        bot.pulseManager.run(
            object : Pulse(4, bot) {
                override fun pulse(): Boolean {
                    bot.unlock()
                    bot.properties.teleportLocation = location
                    bot.pulseManager.clear()
                    bot.animator.reset()
                    return true
                }
            },
        )
        return true
    }

    fun sellOnGE(id: Int) {
        class toCounterPulse : MovementPulse(bot, Location.create(3165, 3487, 0)) {
            override fun pulse(): Boolean {
                var actualId = id
                val itemAmt = bot.bank.getAmount(id)
                if (ItemDefinition.forId(id).noteId == id) {
                    actualId = Item(id).noteChange
                }
                val canSell = GrandExchange.addBotOffer(actualId, itemAmt)
                if (canSell && saleIsBigNews(actualId, itemAmt)) {
                    Repository.sendNews(
                        SERVER_GE_NAME + " just offered " + itemAmt + " " +
                            ItemDefinition
                                .forId(
                                    actualId,
                                ).name
                                .toLowerCase() + " on the GE.",
                    )
                }
                bot.bank.remove(Item(id, itemAmt))
                bot.bank.refresh()
                return true
            }
        }
        bot.pulseManager.run(toCounterPulse())
    }

    fun sellAllOnGe() {
        class toCounterPulseAll : MovementPulse(bot, Location.create(3165, 3487, 0)) {
            override fun pulse(): Boolean {
                for (item in bot.bank.toArray()) {
                    item ?: continue
                    if (item.id == Items.LOBSTER_379) continue
                    if (item.id == Items.SWORDFISH_373) continue
                    if (item.id == Items.SHARK_385) continue
                    if (!item.definition.isTradeable) {
                        continue
                    }
                    val itemAmt = item.amount
                    var actualId = item.id
                    if (ItemDefinition.forId(actualId).noteId == actualId) {
                        actualId = Item(actualId).noteChange
                    }
                    val canSell = GrandExchange.addBotOffer(actualId, itemAmt)
                    if (canSell && saleIsBigNews(actualId, itemAmt)) {
                        Repository.sendNews(
                            SERVER_GE_NAME + " just offered " + itemAmt + " " +
                                ItemDefinition
                                    .forId(
                                        actualId,
                                    ).name
                                    .toLowerCase() + " on the GE.",
                        )
                    }
                    bot.bank.remove(item)
                    bot.bank.refresh()
                }
                return true
            }
        }
        bot.pulseManager.run(toCounterPulseAll())
    }

    fun sellAllOnGeAdv() {
        val ge: Scenery? = getNearestNode("Desk", true) as Scenery?

        class toCounterPulseAll : MovementPulse(bot, ge, DestinationFlag.OBJECT) {
            override fun pulse(): Boolean {
                for (item in bot.bank.toArray()) {
                    item ?: continue
                    if (!item.definition.isTradeable) {
                        continue
                    }
                    val itemAmt = item.amount
                    var actualId = item.id
                    if (ItemDefinition.forId(actualId).noteId == actualId) {
                        actualId = Item(actualId).noteChange
                    }
                    val canSell = GrandExchange.addBotOffer(actualId, itemAmt)
                    if (canSell && saleIsBigNews(actualId, itemAmt)) {
                        when (actualId) {
                            1511 -> continue
                            1513 -> continue
                            1515 -> continue
                            1517 -> continue
                            1519 -> continue
                            1521 -> continue
                            else ->
                                sendNews(
                                    SERVER_GE_NAME + " just offered " + itemAmt + " " +
                                        ItemDefinition
                                            .forId(
                                                actualId,
                                            ).name
                                            .lowercase() + " on the GE.",
                                )
                        }
                    }
                    bot.bank.remove(item)
                    bot.bank.refresh()
                }
                return true
            }
        }
        if (ge != null) {
            bot.pulseManager.run(toCounterPulseAll())
        }
    }

    fun depositAtBank() {
        val bank: Scenery? = getNearestNode("Bank booth", true) as Scenery?

        class BankingPulse : MovementPulse(bot, bank, DestinationFlag.OBJECT) {
            override fun pulse(): Boolean {
                bot.faceLocation(bank?.location)
                for (item in bot.inventory.toArray()) {
                    item ?: continue
                    when (item.id) {
                        Items.RUNE_AXE_1359, Items.TINDERBOX_590, Items.ADAMANT_PICKAXE_1271, Items.COINS_995 -> continue
                    }
                    bot.bank.add(item)
                    bot.inventory.remove(item)
                }
                return true
            }
        }
        if (bank != null) {
            bot.pulseManager.run(BankingPulse())
        }
    }

    fun saleIsBigNews(
        itemID: Int,
        amount: Int,
    ): Boolean {
        return ItemDefinition.forId(itemID).getAlchemyValue(true) * amount >= (
            GameWorld.settings?.ge_announcement_limit
                ?: 500
        )
    }

    fun teleport(loc: Location): Boolean {
        if (bot.isTeleBlocked) {
            return false
        }
        bot.lock()
        bot.visualize(ANIMATIONUP, GRAPHICSUP)
        bot.impactHandler.disabledTicks = 4
        val location = loc
        GameWorld.Pulser.submit(
            object : Pulse(4, bot) {
                override fun pulse(): Boolean {
                    bot.unlock()
                    bot.properties.teleportLocation = location
                    bot.pulseManager.clear()
                    bot.animator.reset()
                    return true
                }
            },
        )
        return true
    }

    fun bankItem(item: Int) {
        class BankingPulse : Pulse(20) {
            override fun pulse(): Boolean {
                val logs = bot.inventory.getAmount(item)
                bot.inventory.remove(Item(item, logs))
                bot.bank.add(Item(item, logs))
                return true
            }
        }
        bot.pulseManager.run(BankingPulse())
    }

    fun bankAll(onComplete: (() -> Unit)? = null) {
        class BankingPulse : Pulse(20) {
            override fun pulse(): Boolean {
                for (item in bot.inventory.toArray()) {
                    if (item != null) {
                        var itemAmount = bot.inventory.getAmount(item)

                        if (bot.inventory.remove(item)) {
                            bot.bank.add(item)
                        }
                    }
                }
                if (onComplete != null) {
                    onComplete?.invoke()
                }
                return true
            }
        }
        bot.pulseManager.run(BankingPulse())
    }

    fun eat(foodId: Int) {
        val foodItem = Item(foodId)
        if (bot.skills.getStaticLevel(Skills.HITPOINTS) *
            RandomFunction.random(
                0.5,
                0.75,
            ) >= bot.skills.lifepoints &&
            bot.inventory.containsItem(foodItem)
        ) {
            bot.lock(3)
            val food = bot.inventory.getItem(foodItem)
            var consumable: Consumable? = Consumables.getConsumableById(foodId)?.consumable
            if (consumable == null) {
                consumable = Food(intArrayOf(food.id), HealingEffect(1))
            }
            consumable.consume(food, bot)
            bot.properties.combatPulse.delayNextAttack(3)
        }
    }

    fun forceEat(foodId: Int) {
        val foodItem = Item(foodId)
        if (bot.inventory.containsItem(foodItem)) {
            bot.lock(3)
            val food = bot.inventory.getItem(foodItem)
            var consumable: Consumable? = Consumables.getConsumableById(foodId)?.consumable
            if (consumable == null) {
                consumable = Food(intArrayOf(foodId), HealingEffect(1))
            }
            consumable.consume(food, bot)
            bot.properties.combatPulse.delayNextAttack(3)
        }
    }

    fun buyFromGE(
        bot: Player,
        itemID: Int,
        amount: Int,
    ) {
        GlobalScope.launch {
            val offer = GrandExchangeOffer()
            offer.itemID = itemID
            offer.sell = false
            offer.offeredValue = checkPriceOverrides(itemID) ?: ItemDefinition.forId(itemID).value
            offer.amount = amount
            offer.player = bot
            AIRepository.addOffer(bot, offer)
            var bought: Boolean = false
            val latch = CountDownLatch(1)
            bot.pulseManager.run(
                object : Pulse(5) {
                    override fun pulse(): Boolean {
                        bought = offer.completedAmount == offer.amount
                        latch.countDown()
                        return true
                    }
                },
            )
            latch.await()
            if (bought) {
                bot.bank.add(Item(offer.itemID, offer.completedAmount))
                bot.bank.refresh()
            }
        }
    }

    fun withdraw(
        itemID: Int,
        amount: Int,
    ) {
        var item: Item? = null
        if (bot.bank.containsItem(Item(itemID, amount))) {
            item = Item(itemID, amount)
        } else {
            item = Item(itemID, bot.bank.getAmount(itemID))
        }
        if (item.amount == 0) return
        if (!bot.inventory.hasSpaceFor(item)) {
            item.amount = bot.inventory.getMaximumAdd(item)
        }
        bot.bank.remove(item)
        bot.inventory.add(item)
    }

    fun equipAndSetStats(items: List<Item>?) {
        if (items == null) return
        for (item in items) {
            equipAndSetStats(item)
        }
    }

    fun equipAndSetStats(item: Item) {
        val configs = item.definition.handlers
        val slot = configs["equipment_slot"] ?: return
        bot.equipment.add(
            item,
            slot as Int,
            false,
            false,
        )
        val reqs = configs["requirements"]
        if (reqs != null) {
            for (req in configs["requirements"] as HashMap<Int, Int>) {
                bot.skills.setStaticLevel(req.key, req.value)
            }
        }
        bot.skills.updateCombatLevel()
    }

    fun loadAppearanceAndEquipment(json: JSONObject?) {
        if (json == null) return
        bot.equipment.clear()
        bot.appearance.parse(json["appearance"] as JSONObject)
        val equipment = json["equipment"] as JSONArray
        bot.equipment.parse(equipment)
        bot.appearance.sync()
        for (i in 0 until bot.equipment.capacity()) {
            val item = bot.equipment.get(i)
            if (item != null) {
                equipAndSetStats(item)
            }
        }
        val highestCombatSkill = bot.skills.getStaticLevel(bot.skills.highestCombatSkillId)
        for (i in 0 until 7) {
            bot.skills.setStaticLevel(i, max((highestCombatSkill * 0.75).toInt(), bot.skills.getStaticLevel(i)))
        }
        bot.skills.updateCombatLevel()
    }

    fun getOverlay(): BottingOverlay {
        return BottingOverlay(bot)
    }

    fun checkPriceOverrides(id: Int): Int? {
        return when (id) {
            else -> itemDefinition(id).getConfiguration(ItemConfigParser.GE_PRICE)
        }
    }

    class BottingOverlay(
        val player: Player,
    ) {
        fun init() {
            player.interfaceManager.openOverlay(Component(195))
            player.packetDispatch.sendInterfaceConfig(195, 5, true)
        }

        fun setTitle(title: String) {
            player.packetDispatch.sendString(colorize("%B$title"), 195, 7)
        }

        fun setTaskLabel(label: String) {
            player.packetDispatch.sendString(colorize("%B$label"), 195, 8)
        }

        fun setAmount(amount: Int) {
            player.packetDispatch.sendString(colorize("%B$amount"), 195, 9)
        }
    }
}
