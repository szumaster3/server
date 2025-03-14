package core.game.node.item

import core.tools.RandomFunction

class WeightedChanceItem(
    val id: Int,
    val minimumAmount: Int,
    val maximumAmount: Int = minimumAmount,
    val weight: Int,
) {
    constructor(id: Int, amount: Int, weight: Int) : this(id, amount, amount, weight)

    fun getItem(): Item {
        return Item(id, RandomFunction.random(minimumAmount, maximumAmount))
    }
}
