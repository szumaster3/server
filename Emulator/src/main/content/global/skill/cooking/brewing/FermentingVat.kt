package content.global.skill.cooking.brewing

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.tools.Log
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items

/**
 * Handles the fermenting process.
 *
 * @author GregF, Makar
 */
class FermentingVat(
    var player: Player,
    private val brewingVat: BrewingVat,
    var theStuff: Boolean,
    var nextBrew: Long,
    var stage: BrewingStage,
    var brewingItem: Brewable?
) {

    private var timeToBrew = cycleTime
    private var barrelItem: Brewable? = null

    constructor(player: Player, brewingVat: BrewingVat) : this(player, brewingVat, false, 0, BrewingStage.EMPTY, null)

    fun addWater() {
        animate(player, Animations.POUR_BUCKET_OVER_GROUND_2283)
        stage = BrewingStage.WATER
        updateVat()
    }

    fun addMalt() {
        stage = BrewingStage.MALT
        updateVat()
    }

    fun addIngredient(item: Item): Boolean {
        val ingredient = Brewable.getBrewable(item.id) ?: return false
        if (getDynLevel(player, Skills.COOKING) < ingredient.level) {
            sendMessage(player, "You need to be level ${ingredient.level} to brew ${ingredient.displayName}")
            return false
        }
        if (!removeItem(player, Item(item.id, ingredient.ingredientAmount))) {
            sendMessage(player, "You do not have enough ${item.name}.")
            return false
        }
        animate(player, Animation(Animations.BEND_AND_PICK_2292))
        sendMessage(player, "You add some ${item.name} to the vat.")
        if (brewingItem != null) {
            log(this.javaClass, Log.ERR, "$player already had an ingredient in ${brewingVat.name}")
        }
        brewingItem = ingredient
        stage = BrewingStage.MIXED
        updateVat()
        return true
    }

    fun addYeast() {
        stage = BrewingStage.BREWING1
        updateVat()
    }

    fun brew(forceStep: Boolean = false, forceMature: Boolean = false, forceBad: Boolean = false): Boolean {
        val resp = internalBrew(forceStep, forceMature, forceBad)
        updateVat()
        return resp
    }

    fun canBrew(): Boolean {
        return listOf(BrewingStage.BREWING1, BrewingStage.BREWING2).any { it == stage }
    }

    private fun internalBrew(forceStep: Boolean, forceMature: Boolean, forceBad: Boolean): Boolean {
        nextBrew = System.currentTimeMillis() + timeToBrew
        if (listOf(BrewingStage.BREWING1, BrewingStage.BREWING2).all { it != stage }) {
            return false
        }
        // No one has clearly documented how this should work. Docs from the time mention 'sometimes' often
        // https://x.com/JagexAsh/status/994867369460813824
        // Ash states that it is challenging to follow when he was trying to do it in OSRS
        // Also unclear if cooking level actually matters

        // Get me a good source and I will implement it here
        // 20% chance of going to the next stage (or failure
        if (RandomFunction.random(5) == 0 || forceStep || forceBad) {
            // Cooking does not appear to matter here
            if ((RandomFunction.random(2) == 0 || forceBad) && !forceMature) {
                stage = BrewingStage.BAD
                return false
            }
            stage = if (stage == BrewingStage.BREWING1) BrewingStage.BREWING2 else BrewingStage.DONE
        }

        if (stage == BrewingStage.DONE) {
            sendMessage(player, "Perhaps I should have a look and see if my ${brewingItem!!.displayName} has brewed...")
            // mature 1/20 chance but 50/50 if you have the stuff
            val roll = if (theStuff) 1 else 20
            if (RandomFunction.random(roll) == 0 || forceMature) {
                stage = BrewingStage.MATURE
            }
            return false
        }
        return true
    }

    fun turnValve() {
        if (stage == BrewingStage.EMPTY) {
            queueScript(player, 1) {
                sendMessage(player, "Nothing interesting happens.")
                return@queueScript stopExecuting(player)
            }
            return
        }
        if (stage == BrewingStage.MATURE || stage == BrewingStage.DONE) {
            rewardXP(player, Skills.COOKING, brewingItem!!.levelXP)
            val amount = if (barrelItem == Brewable.KELDA_STOUT) 1 else 8
            queueScript(player, 1) {
                sendMessage(player, "The barrel now contains $amount pints of ${barrelItem!!.displayName}.")
                return@queueScript stopExecuting(player)
            }
        }
        fillBarrel()
        emptyVat()
        updateVat()
    }

    private fun emptyVat() {
        brewingItem = null
        stage = BrewingStage.EMPTY
        updateVat()
    }

    private fun fillBarrel() {
        var varbitValue = 0
        when (stage) {
            BrewingStage.EMPTY -> Unit
            BrewingStage.WATER, BrewingStage.MALT, BrewingStage.MIXED, BrewingStage.BREWING1, BrewingStage.BREWING2 -> {
                varbitValue = 4
            }

            BrewingStage.DONE, BrewingStage.MATURE -> {
                barrelItem = brewingItem
                val barrelAmount = if (brewingItem == Brewable.KELDA_STOUT) 1 else 8

                varbitValue = barrelItem?.barrelVarBitOffset ?: 0

                if (barrelItem != null) {
                    varbitValue += (barrelAmount - 1)
                    if (stage == BrewingStage.MATURE) {
                        varbitValue += 128
                    }
                }
            }

            BrewingStage.BAD -> {
                varbitValue = if (brewingItem == Brewable.CIDER) 2 else 1
            }
        }
        setVarbit(player, this.brewingVat.varbit + 2, varbitValue, true)
    }

    fun levelBarrel(container: Int): Boolean {
        var value = getVarbit(player, this.brewingVat.varbit + 2)
        if (value == 0) return false
        var servingsLeft = if (value == 3) 1 else (value % 8) + 1

        val product = if (value == 3) Brewable.KELDA_STOUT.product
        else Brewable.values()[(value / 8) - 1].product

        if (container == Items.BEER_GLASS_1919) {
            if (removeItem(player, container)) {
                value--
                servingsLeft--
                addItem(player, product[0])
                animate(player, Animation(Animations.FILL_BEER_GLASS_2285))
            }
        } else if (barrelItem == Brewable.KELDA_STOUT) {
            sendMessage(player, "You must use a glass to level Kelda Stout.")
            return false
        } else if (container == Items.CALQUAT_KEG_5769 && servingsLeft > 3) {
            if (removeItem(player, container)) {
                animate(player, Animation(Animations.FILL_CALQUAT_KEG_2284))
                value -= 4
                servingsLeft -= 4
                addItem(player, product[1])
            }
        }
        if (servingsLeft == 0) {
            value = 0
        }
        setVarbit(player, this.brewingVat.varbit + 2, value, true)
        return servingsLeft != 0
    }

    fun drainBarrel() {
        setVarbit(player, this.brewingVat.varbit + 2, 0, true)

    }

    fun isVatEmpty(): Boolean {
        return stage == BrewingStage.EMPTY
    }

    fun isBarrelEmpty(): Boolean {
        return getVarbit(player, (this.brewingVat.varbit + 2)) == 0
    }


    private fun getVatDisplay(): Int {
        return when (stage) {
            BrewingStage.EMPTY -> 0
            BrewingStage.WATER -> 1
            BrewingStage.MALT -> 2
            BrewingStage.MIXED -> brewingItem?.vatVarBitOffset ?: run {
                log(this.javaClass, Log.ERR, "$player has mixed ${brewingVat.name} with no ingredient.")
                0
            }

            BrewingStage.BREWING1 -> (brewingItem?.vatVarBitOffset?.plus(1)) ?: run {
                log(this.javaClass, Log.ERR, "$player has brewed to stage 1 ${brewingVat.name} with no ingredient.")
                0
            }

            BrewingStage.BREWING2 -> (brewingItem?.vatVarBitOffset?.plus(2)) ?: run {
                log(this.javaClass, Log.ERR, "$player has brewed to stage 2 ${brewingVat.name} with no ingredient.")
                0
            }

            BrewingStage.DONE -> (brewingItem?.vatVarBitOffset?.plus(3)) ?: run {
                log(
                    this.javaClass, Log.ERR, "$player has finished ${brewingVat.name} with no ingredient."
                )
                0
            }

            BrewingStage.MATURE -> (brewingItem?.vatVarBitOffset?.plus(4)) ?: run {
                log(this.javaClass, Log.ERR, "$player has finished (mature) ${brewingVat.name} with no ingredient.")
                0
            }

            BrewingStage.BAD -> if (brewingItem == Brewable.CIDER) 65 else 64
        }
    }

    fun updateVat() {
        log(this.javaClass, Log.DEBUG, "Updating ${this.brewingVat.name}@${this.stage.name}")
        setVarbit(player, this.brewingVat.varbit, this.getVatDisplay(), true)
    }

}

enum class BrewingStage {
    EMPTY, WATER, MALT, MIXED, BREWING1, BREWING2, DONE, MATURE, BAD;
}

enum class BrewingVat(val varbit: Int) {
    KELDAGRIM(736),
    PORT_PHAS(737);

    fun getVat(player: Player): FermentingVat {
        val vat = getOrStartTimer<BrewGrowth>(player)
        return vat.getVat(this, true)
    }

}