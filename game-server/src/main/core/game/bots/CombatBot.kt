package core.game.bots

import content.data.consumables.Consumables
import content.data.consumables.effects.HealingEffect
import core.game.consumable.Consumable
import core.game.consumable.Food
import core.game.node.entity.player.link.appearance.Gender
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.tools.RandomFunction
import kotlin.random.Random

class CombatBot(
    location: Location,
) : AIPlayer(location) {
    var tick = 0

    override fun updateRandomValues() {
        appearance.gender = if (RandomFunction.random(5) == 1) Gender.FEMALE else Gender.MALE
        setDirection(Direction.values()[Random.nextInt(Direction.values().size)])
        skills.updateCombatLevel()
        appearance.sync()
    }

    override fun tick() {
        super.tick()
        this.tick++

        if (skills.lifepoints == 0) {
            deregister(uid)
        }
    }

    fun CheckPrayer(type: Array<PrayerType?>) {
        for (i in type.indices) prayer.toggle(type[i])
    }

    fun eat(foodId: Int) {
        val foodItem = Item(foodId)
        if (skills.getStaticLevel(Skills.HITPOINTS) >= skills.lifepoints * 3 && inventory.containsItem(foodItem)) {
            this.lock(3)
            val food = inventory.getItem(foodItem)
            var consumable: Consumable? = Consumables.getConsumableById(food.id)?.consumable
            if (consumable == null) {
                consumable = Food(IntArray(food.id), HealingEffect(1))
            }
            consumable.consume(food, this)
            properties.combatPulse.delayNextAttack(3)
        }
        if (!checkVictimIsPlayer()) {
            if (!inventory.contains(foodId, 1)) {
                inventory.add(Item(foodId, 5))
            }
        }
    }
}
