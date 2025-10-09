package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.BurdenBeast
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.item.WeightedChanceItem
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs

@Initializable
class FruitBatFamiliar(owner: Player? = null, id: Int = FRUIT_BAT) :
    Forager(owner, id, 4500, Items.FRUIT_BAT_POUCH_12033, 6, *FRUIT_FORAGE), InteractionListener {

    override fun construct(owner: Player, id: Int): Familiar = FruitBatFamiliar(owner, id)

    override fun isHidden(player: Player?): Boolean = super.isHidden(player)

    override fun getIds(): IntArray = intArrayOf(FRUIT_BAT)

    override fun specialMove(special: FamiliarSpecial?): Boolean {
        val cooldown = owner.getAttribute("fruit-bat", 0)
        if (cooldown > GameWorld.ticks) return false

        performSpecialMove()

        owner.setAttribute("fruit-bat", GameWorld.ticks + 5)
        lock(4)

        Pulser.submit(object : Pulse(4, this) {
            override fun pulse(): Boolean {
                dropFruits()
                return true
            }
        })

        return true
    }

    private fun performSpecialMove() {
        animate(FLY_UP_ANIMATION)
        graphics(FRUIT_FALL_GFX)
        animate(FLY_DOWN_ANIMATION, 3)
    }

    private fun dropFruits() {
        val coords = shuffledOffsets().iterator()
        val loc = owner.location

        // Guaranteed fruit (Papaya)
        repeat(2) {
            coords.nextOrNull()?.let { (dx, dy) ->
                spawnFruit(Item(Items.PAPAYA_FRUIT_5972), loc, dx, dy)
            }
        }

        // Random fruits
        val count = calculateFruitAmount()
        repeat(count) {
            coords.nextOrNull()?.let { (dx, dy) ->
                val item = RandomFunction.rollWeightedChanceTable(*FRUIT_FALL)
                if (item.id != 0) {
                    spawnFruit(item, loc, dx, dy)
                }
            }
        }
    }

    private fun spawnFruit(item: Item, loc: Location, dx: Int, dy: Int) {
        val dropLoc = loc.transform(dx, dy, 0)
        GroundItemManager.create(item, dropLoc, owner)
        sendGraphics(FRUIT_SPLASH_GFX, dropLoc)
    }

    private fun shuffledOffsets(): List<Pair<Int, Int>> =
        listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1).shuffled()

    private fun Iterator<Pair<Int, Int>>.nextOrNull(): Pair<Int, Int>? = if (hasNext()) next() else null

    private fun calculateFruitAmount(): Int {
        val good = RandomFunction.random(100) <= 2
        return if (!good && RandomFunction.random(10) == 1) {
            RandomFunction.random(0, 1)
        } else {
            RandomFunction.random(0, if (good) 7 else 3)
        }
    }

    override fun defineListeners() {
        on(FRUIT_BAT, IntType.NPC, "Interact") { player, node ->
            val familiar = node as? BurdenBeast ?: return@on false
            sendOptions(player, "Select an Option", "Chat", "Fly", "Withdraw")
            addDialogueAction(player) { _, button ->
                when (button) {
                    2 -> openDialogue(player, node.id, node.asNpc())
                    3 -> handleFly(player, familiar)
                    4 -> handleWithdraw(player, familiar)
                }
            }
            return@on true
        }
    }

    private fun handleFly(player: Player, familiar: BurdenBeast) {
        when {
            inZone(player, "Dark zone") -> {
                sendMessage(player, "You cannot use this ability here.")
            }
            !inZone(player, "karamja") -> {
                sendMessage(
                    player,
                    "You need to be in Karamja, close to the fruit-bearing jungle trees, for this to work.",
                )
            }
            else -> {
                player.lock(13)
                familiar.lock(13)
                familiar.animate(FLY_UP_ANIMATION)
                sendMessage(player, "The fruit bat flies away to look for fruit...")
                Pulser.submit(object : Pulse(1, player, familiar) {
                    var counter = 0
                    override fun pulse(): Boolean {
                        counter++
                        when (counter) {
                            3 -> familiar.isInvisible = true
                            10 -> {
                                familiar.isInvisible = false
                                familiar.animate(FLY_DOWN_ANIMATION)
                            }
                            12 -> {
                                val item = RandomFunction.rollWeightedChanceTable(*FRUIT_FLY)
                                familiar.container.add(item, true)
                                sendMessage(player, "Your fruit bat has returned with fruit!")
                                return true
                            }
                        }
                        return false
                    }
                })
            }
        }
    }

    private fun handleWithdraw(player: Player, familiar: BurdenBeast) {
        if (!familiar.isBurdenBeast) {
            sendMessage(player, "This is not a beast of burden.")
        } else {
            familiar.openInterface()
        }
    }

    companion object {
        private const val FRUIT_BAT = NPCs.FRUIT_BAT_6817

        private val FLY_UP_ANIMATION = Animation(8320)
        private val FLY_DOWN_ANIMATION = Animation(8321)
        private val FRUIT_FALL_GFX = Graphics(1332, 200)
        private val FRUIT_SPLASH_GFX = Graphics(1331)

        private val FRUIT_FORAGE = arrayOf(
            Item(Items.PAPAYA_FRUIT_5972), Item(Items.ORANGE_2108), Item(Items.PINEAPPLE_2114),
            Item(Items.LEMON_2102), Item(Items.LIME_2120), Item(Items.STRAWBERRY_5504),
            Item(Items.WATERMELON_5982), Item(Items.COCONUT_5974)
        )

        private val FRUIT_FALL = arrayOf(
            WeightedChanceItem(Items.ORANGE_2108, 1, 4),
            WeightedChanceItem(Items.PINEAPPLE_2114, 1, 3),
            WeightedChanceItem(Items.LEMON_2102, 1, 2),
            WeightedChanceItem(Items.LIME_2120, 1, 2),
            WeightedChanceItem(Items.BANANA_1963, 1, 2),
            WeightedChanceItem(0, 1, 4)
        )

        private val FRUIT_FLY = arrayOf(
            WeightedChanceItem(Items.ORANGE_2108, 1, 4),
            WeightedChanceItem(Items.PINEAPPLE_2114, 1, 3),
            WeightedChanceItem(Items.LEMON_2102, 1, 2),
            WeightedChanceItem(Items.LIME_2120, 1, 2),
            WeightedChanceItem(Items.BANANA_1963, 1, 2)
        )
    }
}
