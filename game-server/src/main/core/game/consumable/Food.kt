package core.game.consumable

import core.api.playAudio
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Sounds

/**
 * Represents a generic food item that can be consumed.
 */
open class Food : Consumable {

    /**
     * Creates food with default eating animation.
     *
     * @param ids Item IDs for food variants.
     * @param effect Optional consumable effect.
     * @param messages Optional custom messages.
     */
    constructor(ids: IntArray?, effect: ConsumableEffect?, vararg messages: String?) : super(ids, effect, *messages) {
        animation = Animation(Animations.HUMAN_EATING_829)
    }

    /**
     * Creates food with custom animation.
     *
     * @param ids Item IDs for food variants.
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
     * Sends the default message when food is eaten.
     */
    override fun sendDefaultMessages(player: Player, item: Item) {
        sendMessage(player, "You eat the " + getFormattedName(item) + ".")
    }

    /**
     * Sends eating animation and sound on consumption.
     */
    override fun executeConsumptionActions(player: Player) {
        player.animate(animation)
        playEatingSound(player)
    }

    /**
     * Sends the eating sound effect.
     */
    private fun playEatingSound(player: Player) {
        playAudio(player, Sounds.EAT_2393)
    }
}
