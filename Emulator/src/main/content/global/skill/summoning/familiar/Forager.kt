package content.global.skill.summoning.familiar

import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld
import core.tools.RandomFunction

/**
 * A familiar that forages and adds items to its inventory.
 *
 * @property items the list of possible items this familiar can forage
 */
abstract class Forager : BurdenBeast {

    private val items: Array<Item>
    private var passiveDelay: Int = 0

    /**
     * Constructs a new [Forager] with a custom attack style.
     *
     * @param owner the player who owns this familiar
     * @param id the NPC id of the familiar
     * @param ticks the active duration of the familiar in game ticks
     * @param pouchId the id of the summoning pouch used
     * @param specialCost the cost of using special abilities
     * @param attackStyle the combat style of the familiar
     * @param items the list of items it may forage
     */
    constructor(
        owner: Player?,
        id: Int,
        ticks: Int,
        pouchId: Int,
        specialCost: Int,
        attackStyle: Int,
        vararg items: Item
    ) : super(owner, id, ticks, pouchId, specialCost, 30, attackStyle) {
        this.items = Array(items.size) { i -> items[i] }
        setRandomPassive()
    }

    /**
     * Constructs a new [Forager] with the default DEFENSIVE attack style.
     */
    constructor(
        owner: Player,
        id: Int,
        ticks: Int,
        pouchId: Int,
        specialCost: Int,
        vararg items: Item
    ) : this(owner, id, ticks, pouchId, specialCost, WeaponInterface.STYLE_DEFENSIVE, *items)

    override fun handleFamiliarTick() {
        super.handleFamiliarTick()
        if (items.isNotEmpty() && passiveDelay < GameWorld.ticks) {
            if (RandomFunction.random(getRandom()) < 4) {
                produceItem(items[RandomFunction.random(items.size)])
            }
            setRandomPassive()
        }
    }

    /**
     * Attempts to produce and store the given item in the familiar's container.
     *
     * @param item the item to produce
     * @return `true` if added successfully, `false` otherwise
     */
    fun produceItem(item: Item): Boolean {
        if (!container.hasSpaceFor(item)) {
            owner.packetDispatch.sendMessage("Your familiar is too full to collect items.")
            return false
        }
        owner.packetDispatch.sendMessage(
            if (item.amount == 1)
                "Your familiar has produced an item."
            else
                "Your familiar has produced items."
        )
        return container.add(item)
    }

    /**
     * Produces a random item from the familiar's item list.
     *
     * @return `true` if added successfully, `false` otherwise
     */
    fun produceItem(): Boolean {
        if (items.isEmpty()) return false
        return produceItem(items[RandomFunction.random(items.size)])
    }

    /**
     * Hook for subclasses to implement additional passive behavior.
     */
    open fun handlePassiveAction() {
        // Optional override for custom behavior.
    }

    /**
     * Returns the random threshold used for passive item production checks.
     */
    open fun getRandom(): Int = 11

    /**
     * Sets the next passive item production delay.
     */
    private fun setRandomPassive() {
        passiveDelay = GameWorld.ticks + RandomFunction.random(100, 440)
    }
}
