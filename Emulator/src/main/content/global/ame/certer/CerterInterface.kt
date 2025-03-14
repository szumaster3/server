package content.global.ame.certer

import content.data.GameAttributes
import core.api.sendString
import core.api.setAttribute
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.timer.impl.AntiMacro
import org.rs.consts.Items

class CerterInterface : InterfaceListener {
    val CERTER_INTERFACE = 184
    val OPTION_A_CHILD = 1
    val OPTION_B_CHILD = 2
    val OPTION_C_CHILD = 3
    val ITEM_CHILD = 7
    val items =
        mapOf(
            Items.BOWL_1923 to "A bowl.",
            Items.NULL_6189 to "A fish.",
            Items.NULL_6190 to "A bass.",
            Items.NULL_6191 to "A sword.",
            Items.NULL_6192 to "A battleaxe.",
            Items.NULL_6193 to "A helmet.",
            Items.NULL_6194 to "A kiteshield.",
            Items.NULL_6195 to "A pair of shears.",
            Items.NULL_6196 to "A shovel.",
            Items.NULL_6197 to "A ring.",
            Items.NULL_6198 to "A necklace.",
        )
    val falseOptions =
        arrayOf(
            "An axe.",
            "An arrow.",
            "A pair of boots.",
            "A pair of gloves.",
            "A staff.",
            "A bow.",
            "A feather.",
            "The disenfrachaised youth of 1940's Columbia.",
        )

    override fun defineInterfaceListeners() {
        on(CERTER_INTERFACE) { player, _, _, buttonID, _, _ ->
            val answer = buttonID - 7
            val correctAnswer = player.getAttribute(GameAttributes.CERTER_INDEX, 0)
            setAttribute(player, GameAttributes.CERTER_CORRECT, correctAnswer == answer)
            player.interfaceManager.close()
            player.dialogueInterpreter.open(CerterDialogue(false), AntiMacro.getEventNpc(player))
            return@on true
        }

        onOpen(CERTER_INTERFACE) { player, _ ->
            generateOptions(player)
            return@onOpen true
        }

        onClose(CERTER_INTERFACE) { player, _ ->
            setAttribute(player, GameAttributes.RE_PAUSE, false)
            return@onClose true
        }
    }

    fun generateOptions(player: Player) {
        val correct = items.keys.random()
        val indexes = arrayListOf(1, 2, 3)
        val correctIndex = indexes.random()
        val correctItem = Item(correct).definition
        val iFaceModelId = correctItem!!.interfaceModelId
        val iFaceZoom = correctItem.modelZoom
        indexes.remove(correctIndex)
        setAttribute(player, GameAttributes.CERTER_INDEX, correctIndex)

        player.packetDispatch.sendString(items[correct], CERTER_INTERFACE, optionFromIndex(correctIndex))

        val tempOptions = falseOptions.toMutableList()
        val false1 = tempOptions.random()
        tempOptions.remove(false1)
        val false2 = tempOptions.random()

        sendString(player, false1, CERTER_INTERFACE, optionFromIndex(indexes[0]))
        sendString(player, false2, CERTER_INTERFACE, optionFromIndex(indexes[1]))
        player.packetDispatch.sendModelOnInterface(iFaceModelId, CERTER_INTERFACE, ITEM_CHILD, iFaceZoom)
    }

    fun optionFromIndex(index: Int): Int {
        return when (index) {
            1 -> OPTION_A_CHILD
            2 -> OPTION_B_CHILD
            3 -> OPTION_C_CHILD
            else -> OPTION_A_CHILD
        }
    }
}
