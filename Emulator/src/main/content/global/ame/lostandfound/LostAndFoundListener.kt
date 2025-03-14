package content.global.ame.lostandfound

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction
import org.rs.consts.Items

class LostAndFoundListener : InteractionListener {
    private val sceneryIDs = (8998..9006).toIntArray()

    override fun defineListeners() {
        on(sceneryIDs, IntType.SCENERY, "operate") { player, node ->
            var magicLevel = getDynLevel(player, Skills.MAGIC)
            val randomRewardAmount = RandomFunction.random(8, 36)
            if (LostAndFoundUtils.isOddAppendage(player, node.asScenery())) {
                player.locks.unlockTeleport()
                LostAndFoundUtils.cleanup(player)
                queueScript(player, 4, QueueStrength.SOFT) {
                    sendPlainDialogue(
                        player,
                        false,
                        "The Abyssal Services Department apologises for the inconvenience.",
                    )
                    if (getAttribute(player, LostAndFoundUtils.essenceMineKey, false)) {
                        addItem(
                            player,
                            if (getStatLevel(
                                    player,
                                    Skills.MINING,
                                ) > 30
                            ) {
                                Items.PURE_ESSENCE_7936
                            } else {
                                Items.RUNE_ESSENCE_1436
                            },
                            randomRewardAmount,
                        )
                    }
                    return@queueScript stopExecuting(player)
                }
            } else {
                LostAndFoundUtils.setRandomAppendage(player)
                sendMessage(player, "That was not the correct appendage!")

                if (magicLevel >= 10) {
                    magicLevel -= (1..10).random()
                    adjustLevel(player, Skills.MAGIC, magicLevel)
                } else {
                    adjustLevel(player, Skills.MAGIC, -1)
                }
            }
            return@on true
        }
    }
}
