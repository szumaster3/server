package core.game.consumable

import core.api.playAudio
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations

/**
 * Represents a generic drinkable consumable.
 */
open class Drink : Consumable {

    /**
     * Creates a drink with the default drinking animation.
     *
     * @param ids Item IDs for drink variants.
     * @param effect Optional consumable effect.
     * @param messages Optional custom messages.
     */
    constructor(ids: IntArray?, effect: ConsumableEffect?, vararg messages: String?) : super(ids, effect, *messages) {
        animation = Animation(Animations.HUMAN_DRINKING_1327)
    }

    /**
     * Creates a drink with a custom animation.
     *
     * @param ids Item IDs for drink variants.
     * @param effect Optional consumable effect.
     * @param animation Custom animation to play on consumption.
     * @param messages Optional custom messages.
     */
    constructor(
        ids: IntArray?,
        effect: ConsumableEffect?,
        animation: Animation?,
        vararg messages: String?,
    ) : super(ids, effect, animation, *messages)

    /**
     * Sends the default message when drinking.
     */
    override fun sendDefaultMessages(player: Player, item: Item) {
        player.packetDispatch.sendMessage("You drink the " + getFormattedName(item) + ".")
    }

    /**
     * Sends drinking animation and sound effect.
     */
    override fun executeConsumptionActions(player: Player) {
        player.animate(animation)
        playAudio(player, DEFAULT_DRINK_SOUND_EFFECT)
    }

    /**
     * Formats the item name by removing dose indicators and trimming.
     *
     * @param item The drink item.
     * @return Formatted name without dose suffixes.
     */
    override fun getFormattedName(item: Item): String {
        val doses = listOf("(4)", "(3)", "(2)", "(1)", "(m4)", "(m3)", "(m2)", "(m1)", "(m)")
        return item.name
            .replace(Regex(doses.joinToString("|") { Regex.escape(it) }), "")
            .trim()
            .lowercase()
    }

    companion object {
        private const val DEFAULT_DRINK_SOUND_EFFECT = 4580
    }
}