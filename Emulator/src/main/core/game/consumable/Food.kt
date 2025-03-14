package core.game.consumable

import core.api.playAudio
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Sounds

open class Food : Consumable {
    constructor(ids: IntArray?, effect: ConsumableEffect?, vararg messages: String?) : super(ids, effect, *messages) {
        animation = Animation(Animations.HUMAN_EATING_829)
    }

    constructor(ids: IntArray?, effect: ConsumableEffect?, animation: Animation?, vararg messages: String?) : super(
        ids,
        effect,
        animation,
        *messages,
    )

    override fun sendDefaultMessages(
        player: Player,
        item: Item,
    ) {
        sendMessage(player, "You eat the " + getFormattedName(item) + ".")
    }

    override fun executeConsumptionActions(player: Player) {
        player.animate(animation)
        playEatingSound(player)
    }

    private fun playEatingSound(player: Player) {
        playAudio(player, Sounds.EAT_2393)
    }
}
