package content.region.kandarin.miniquest.barcrawl

import content.region.kandarin.miniquest.barcrawl.BarcrawlManager.Companion.getInstance
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.plugin.Initializable

@Initializable
class BarcrawlDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npcId = args[0] as Int
        type = args[1] as BarcrawlType
        player("I'm doing Alfred Grimhand's Barcrawl.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                interpreter.sendDialogues(npcId, null, *type!!.dialogue[0])
                stage = if (type!!.dialogue.size > 1) 1 else 2
            }

            1 -> {
                interpreter.sendDialogues(npcId, null, *type!!.dialogue[1])
                stage++
            }

            2 -> {
                end()
                if (!player.inventory.containsItem(type!!.coins)) {
                    return true
                }
                type!!.message(player, true)
                player.inventory.remove(type!!.coins)
                getInstance(player).complete(type!!.ordinal)
                player.lock(6)
                Pulser.submit(
                    object : Pulse(6, player) {
                        override fun pulse(): Boolean {
                            type!!.message(player, false)
                            type!!.effect(player)
                            return true
                        }
                    },
                )
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return BarcrawlDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(DialogueInterpreter.getDialogueKey("barcrawl dialogue"))
    }

    companion object {
        private var type: BarcrawlType? = null
        private var npcId = 0
    }
}
