package core.game.bots.impl

import core.ServerConstants
import core.game.bots.AIRepository
import core.game.bots.Script
import core.game.bots.impl.Adventurer.Companion.lumbridge
import core.game.container.Container
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.interaction.Option._P_TRADE
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.trade.TradeModule
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.ImmerseWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.EntityFlag
import core.game.world.update.flag.context.ChatMessage
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.rs.consts.Items
import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.math.min

class DoublingMoney : Script() {
    private var scamMode = false

    private var setup = false

    var victim: Player? = null

    private var playerOwed: Player? = null

    private var sentTradeRequest = false

    private var debtOwed: Int = 0

    var ticks = 0

    private var sleepTime = 0

    private var stateString = "init"

    private val maxTicks = (400..750).random()

    enum class Effort {
        LOW,
        HIGH,
        VERY_HIGH,
    }

    private val effort =
        when (Random().nextInt(100)) {
            in 0..90 -> Effort.LOW
            in 90..98 -> Effort.HIGH
            else -> Effort.VERY_HIGH
        }

    var states =
        mapOf(
            fun(): Int {
                if (setup) return -1
                return 250
            } to {
                stateString = "Putting on my outfit.."
                var appearance = if (Math.random() < 0.5) "noob" else "intermediate"
                if (effort == Effort.VERY_HIGH && Math.random() < 0.5) appearance = "veteran"
                val appearancesArray = botAppearances[appearance] as JSONArray
                scriptAPI.loadAppearanceAndEquipment(appearancesArray.random() as JSONObject)
                setup = true
            },
            fun(): Int {
                val botTradeModule = TradeModule.getExtension(bot)
                if (botTradeModule != null) return -1
                return 0
            } to {
                stateString = "Doing nothing"
            },
            fun(): Int {
                return 1
            } to {
                stateString = "Saying Doubling money"
                when (effort) {
                    Effort.LOW -> {
                        scriptAPI.sendChat("Doubling money")
                        sleepTime = 7
                    }
                    Effort.HIGH -> {
                        val message =
                            arrayOf(
                                "Doubling Money",
                                "Doubling Money",
                                "Doubling Money!",
                                "Doubling moneyy",
                            ).random()
                        val messageEffect = arrayOf(0, 256).random()
                        val ctx = ChatMessage(bot, message, messageEffect, message.length)
                        bot.updateMasks.register(EntityFlag.Chat, ctx)
                        sleepTime = 8
                    }
                    Effort.VERY_HIGH -> {
                        val message = arrayOf("Doubling money!", "Doubling money").random()
                        val messageEffect = arrayOf(771, 2818, 2562, 768, 512, 2304, 2560, 769, 1792).random()
                        val ctx = ChatMessage(bot, message, messageEffect, message.length)
                        bot.updateMasks.register(EntityFlag.Chat, ctx)
                        sleepTime = 9
                    }
                }
            },
            fun(): Int {
                if (victim == null) return -1
                return 10
            } to {
                stateString = "Accepting trade request"
                InteractionListeners.run(-1, IntType.PLAYER, "trade with", bot, victim!!)
                victim = null
                sleepTime = 3
            },
            fun(): Int {
                if (debtOwed >= 0) return -1
                val botTradeModule = TradeModule.getExtension(bot) ?: return -1
                val playerTradeModule = TradeModule.getExtension(botTradeModule.target) ?: return -1
                if (!playerTradeModule.container!!.containsAtLeastOneItem(Items.COINS_995)) return 15
                return -1
            } to {
                val botTradeModule = TradeModule.getExtension(bot)
                stateString = "Waiting for ${botTradeModule!!.target} to post money"
                sleepTime = 5
            },
            fun(): Int {
                if (debtOwed == 0) return -1
                val botTradeModule = TradeModule.getExtension(bot) ?: return -1
                if (botTradeModule.target != playerOwed) return -1
                if (!botTradeModule.container!!.containsAtLeastOneItem(Items.COINS_995)) return 150
                return -1
            } to {
                stateString = "Paying off ${playerOwed!!.username} $debtOwed coins"
                val botTradeModule = TradeModule.getExtension(bot)
                val coinSlot = bot.inventory.getSlot(Item(Items.COINS_995))
                botTradeModule!!.container!!.offer(coinSlot, debtOwed)
                debtOwed = 0
                playerOwed = null
            },
            fun(): Int {
                val botTradeModule = TradeModule.getExtension(bot) ?: return -1
                val playerTradeModule = TradeModule.getExtension(botTradeModule.target) ?: return -1
                if (botTradeModule.container!!.containsAtLeastOneItem(Items.COINS_995)) return 200
                if (playerTradeModule.container!!.containsAtLeastOneItem(Items.COINS_995)) return 200
                return -1
            } to {
                stateString = "Accepting any trade screen that has coins put up"
                val botTradeModule = TradeModule.getExtension(bot)
                val playerTradeModule = TradeModule.getExtension(botTradeModule!!.target)
                if (botTradeModule.isAccepted) return@to

                sleepTime = (1..if (botTradeModule.getInterface() == TradeModule.MAIN_INTERFACE) 2 else 5).random()

                val coinsFromBot = botTradeModule.container!!.getAmount(Items.COINS_995)
                if (botTradeModule.getInterface() == TradeModule.ACCEPT_INTERFACE &&
                    coinsFromBot > 0 &&
                    effort == Effort.VERY_HIGH
                ) {
                    val message = "Payed ${(if (coinsFromBot < 1000) "${coinsFromBot}gp" else "${coinsFromBot / 1000}k")}"
                    val ctx = ChatMessage(bot, message, 512, message.length)
                    bot.updateMasks.register(EntityFlag.Chat, ctx)

                    sleepTime = 7
                }
                if (botTradeModule.getInterface() == TradeModule.ACCEPT_INTERFACE &&
                    Math.random() < 0.2 &&
                    effort == Effort.HIGH &&
                    coinsFromBot > 0
                ) {
                    scriptAPI.sendChat("Enjoy")
                    sleepTime += 4
                }

                if (botTradeModule.getInterface() == TradeModule.MAIN_INTERFACE && scamMode) {
                    sleepTime = maxOf(3, sleepTime)
                }

                if (botTradeModule.getInterface() == TradeModule.ACCEPT_INTERFACE) {
                    val player = botTradeModule.target
                    var playerNetPoL = player!!.getAttribute("double-money-net-pol", "0").toInt()
                    playerNetPoL += coinsFromBot
                    playerNetPoL -= playerTradeModule!!.container!!.getAmount(Items.COINS_995)
                    player.setAttribute("double-money-net-pol", playerNetPoL.toString())
                }
                if (scamMode && botTradeModule.getInterface() == TradeModule.ACCEPT_INTERFACE && coinsFromBot > 0) {
                    botTradeModule.decline()
                } else {
                    botTradeModule.setAccepted(true, update = true)
                }
                sentTradeRequest = false
            },
            fun(): Int {
                if (sentTradeRequest) return 50
                return -1
            } to {
            },
            fun(): Int {
                if (TradeModule.getExtension(bot) == null && debtOwed > 0 && playerOwed != null) return 25
                return -1
            } to {
                if (Math.random() < 0.7 || debtOwed > 200_000) {
                    stateString = "Scamming"
                    scamMode = true
                    if (effort != Effort.VERY_HIGH || Math.random() < 0.5) {
                        terminate()
                    }
                }
                stateString = "Sending trade request to ${playerOwed!!.name} to give them $debtOwed coins"
                playerOwed!!.interaction.handle(bot, _P_TRADE)
                InteractionListeners.run(-1, IntType.PLAYER, "trade with", bot, playerOwed!!)
                sentTradeRequest = true
            },
            fun(): Int {
                var minDist = 1000.0
                for (loc in doublingLocs) {
                    minDist = min(minDist, loc.getDistance(bot.location))
                }
                if (minDist > 1) return 2
                RegionManager.forId(bot.location.regionId).planes[bot.location.z].players.forEach {
                    if (AIRepository.PulseRepository[it?.username?.lowercase()]?.botScript is DoublingMoney &&
                        it != bot &&
                        it.location.getDistance(bot.location) <= 1
                    ) {
                        return 2
                    }
                }
                return -1
            } to {
                stateString = "Walking to doubling location"
                val loc = doublingLocs.random()
                scriptAPI.walkTo(loc)
                sleepTime = 5
            },
            fun(): Int {
                if (ticks > maxTicks) return 30
                return -1
            } to {
                stateString = "Logging out"
                terminate()
            },
            fun(): Int {
                if (ticks > 1000) return 1_000_000
                return -1
            } to {
                stateString = "Force terminating"
                terminate()
            },
        )

    override fun toString(): String {
        return "stateString: $stateString, sleepTime: $sleepTime, ticks: $ticks, maxTicks: $maxTicks, debtOwed: $debtOwed, playerOwed: $playerOwed, victim: $victim, effort: $effort, scamMode: $scamMode"
    }

    override fun tick() {
        if (!bot.isActive) {
            running = false
            return
        }
        ticks++
        if (sleepTime > 0) {
            sleepTime--
            return
        }

        states.maxByOrNull { it.key() }?.value?.invoke()
    }

    fun tradeReceived(p: Player) {
        victim = p
    }

    fun itemsReceived(
        p: Player,
        container: Container?,
    ) {
        if (container == null) return
        val coins: Item = container.get(Item(Items.COINS_995)) ?: return
        playerOwed = p
        debtOwed = coins.amount * 2
        if (effort == Effort.VERY_HIGH) {
            val message = "Received ${(if (coins.amount < 1000) "${coins.amount}gp" else "${coins.amount / 1000}k")}"
            val ctx = ChatMessage(bot, message, 256, message.length)
            bot.updateMasks.register(EntityFlag.Chat, ctx)
        }
    }

    fun terminate() {
        scriptAPI.teleport(lumbridge)
        debtOwed = 0
        bot.isActive = false
        sleepTime = 500
        AIRepository.PulseRepository.remove(bot.username.lowercase())

        ImmerseWorld.spawnDoubleMoneyBot(true)
    }

    override fun newInstance(): Script? {
        return null
    }

    init {
        skills[Skills.AGILITY] = 99
        inventory.add(Item(Items.COINS_995, 1_000_000))
    }

    companion object {
        val startLocations =
            arrayOf(
                Location.create(3184, 3488, 0),
                Location.create(3167, 3461, 0),
                Location.create(3170, 3444, 0),
                Location.create(3165, 3487, 0),
                Location.create(3167, 3489, 0),
                Location.create(3162, 3491, 0),
            )

        val doublingLocs =
            arrayOf(
                Location.create(3159, 3492, 0),
                Location.create(3164, 3484, 0),
                Location.create(3169, 3486, 0),
                Location.create(3172, 3489, 0),
                Location.create(3168, 3483, 0),
                Location.create(3166, 3493, 0),
                Location.create(3157, 3498, 0),
                Location.create(3157, 3485, 0),
            )

        private val jsonFilePath = "${ServerConstants.BOT_DATA_PATH}/ge_bot_appearances_and_equipment.json"
        private val jsonFile = File(jsonFilePath)
        private val parser = JSONParser()
        val botAppearances = parser.parse(FileReader(jsonFile)) as JSONObject
    }
}
