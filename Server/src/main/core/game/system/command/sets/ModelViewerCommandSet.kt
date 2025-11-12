package core.game.system.command.sets

import content.global.plugins.iface.BookInterface
import content.global.plugins.iface.BookLine
import content.global.plugins.iface.Page
import content.global.plugins.iface.PageSet
import core.api.getAttribute
import core.api.setAttribute
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.system.command.Privilege
import core.plugin.Initializable

@Initializable
class ModelViewerCommandSet : CommandSet(Privilege.ADMIN) {

    companion object {
        const val DEF_BOOK = 10216
        const val TITLE = "Model Viewer"
        const val ATTRIBUTE_MODEL_NUMBER = "modelNumber"
        const val ATTRIBUTE_ZOOM = "modelZoom"
        const val ATTRIBUTE_PITCH = "modelPitch"
        const val ATTRIBUTE_YAW = "modelYaw"

        private val buttonAdjustments = mapOf(
            114 to (ATTRIBUTE_ZOOM to -100),
            116 to (ATTRIBUTE_PITCH to -100),
            118 to (ATTRIBUTE_YAW to -100),
            122 to (ATTRIBUTE_MODEL_NUMBER to -1),
            124 to (ATTRIBUTE_MODEL_NUMBER to -10),
            126 to (ATTRIBUTE_MODEL_NUMBER to -100),
            128 to (ATTRIBUTE_MODEL_NUMBER to -1000),
            144 to (ATTRIBUTE_ZOOM to 100),
            146 to (ATTRIBUTE_PITCH to 100),
            148 to (ATTRIBUTE_YAW to 100),
            152 to (ATTRIBUTE_MODEL_NUMBER to 1),
            154 to (ATTRIBUTE_MODEL_NUMBER to 10),
            156 to (ATTRIBUTE_MODEL_NUMBER to 100),
            158 to (ATTRIBUTE_MODEL_NUMBER to 1000),
        )

        private val buttonLabels = mapOf(
            114 to "-1 zoom", 116 to "-1 pitch", 118 to "-1 yaw",
            122 to "-1", 124 to "-10", 126 to "-100", 128 to "-1000",
            144 to "+1 zoom", 146 to "+1 pitch", 148 to "+1 yaw",
            152 to "+1", 154 to "+10", 156 to "+100", 158 to "+1000"
        )

        @Suppress("UNUSED_PARAMETER")
        private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
            BookInterface.clearBookLines(player, BookInterface.FANCY_BOOK_2_27, BookInterface.FANCY_BOOK_2_27_LINE_IDS)
            BookInterface.clearButtons(player, BookInterface.FANCY_BOOK_2_27, BookInterface.FANCY_BOOK_2_27_BUTTON_IDS)
            BookInterface.setTitle(player, BookInterface.FANCY_BOOK_2_27, BookInterface.FANCY_BOOK_2_27_LINE_IDS, "Model Viewer")

            val buttonIds = buttonAdjustments.keys
            buttonIds.forEach { id ->
                player.packetDispatch.sendInterfaceConfig(BookInterface.FANCY_BOOK_2_27, id, false)
                player.packetDispatch.sendString(buttonLabels[id] ?: "", BookInterface.FANCY_BOOK_2_27, id)
            }

            buttonAdjustments[buttonID]?.let { (attr, delta) ->
                val current = getAttribute(player, attr, if (attr == ATTRIBUTE_ZOOM) 700 else if (attr == ATTRIBUTE_MODEL_NUMBER) DEF_BOOK else 0)
                val newValue = current + delta

                if (attr == ATTRIBUTE_PITCH && newValue < 0) {
                    setAttribute(player, attr, 0)
                    player.debug("Not possible.")
                } else {
                    setAttribute(player, attr, newValue)
                }
            }

            val modelNo = getAttribute(player, ATTRIBUTE_MODEL_NUMBER, DEF_BOOK)
            val zoom = getAttribute(player, ATTRIBUTE_ZOOM, 700)
            val pitch = getAttribute(player, ATTRIBUTE_PITCH, 0)
            val yaw = getAttribute(player, ATTRIBUTE_YAW, 0)

            player.packetDispatch.sendString("No: $modelNo   $zoom $pitch $yaw", BookInterface.FANCY_BOOK_2_27, 38)
            player.packetDispatch.sendString("No: ${modelNo + 1}", BookInterface.FANCY_BOOK_2_27, 53)

            BookInterface.setModelOnPage(player, 0, modelNo,
                BookInterface.FANCY_BOOK_2_27,
                BookInterface.FANCY_BOOK_2_27_IMAGE_ENABLE_DRAW_IDS[7],
                BookInterface.FANCY_BOOK_2_27_IMAGE_DRAW_IDS[7],
                zoom, pitch, yaw
            )
            BookInterface.setModelOnPage(player, 0, modelNo + 1,
                BookInterface.FANCY_BOOK_2_27,
                BookInterface.FANCY_BOOK_2_27_IMAGE_ENABLE_DRAW_IDS[22],
                BookInterface.FANCY_BOOK_2_27_IMAGE_DRAW_IDS[22],
                zoom, pitch, yaw
            )

            return true
        }

        private const val MALE_NPC_ID = 1
        private const val FEMALE_NPC_ID = 5

        private val MALE_IMAGE_ENABLE_ID = BookInterface.FANCY_BOOK_3_49_IMAGE_ENABLE_DRAW_IDS[4]
        private val MALE_IMAGE_DRAW_ID = BookInterface.FANCY_BOOK_3_49_IMAGE_DRAW_IDS[4]

        private val FEMALE_IMAGE_ENABLE_ID = BookInterface.FANCY_BOOK_3_49_IMAGE_ENABLE_DRAW_IDS[15]
        private val FEMALE_IMAGE_DRAW_ID = BookInterface.FANCY_BOOK_3_49_IMAGE_DRAW_IDS[15]

        private val EXPRESSIONS = enumValues<FaceAnim>()
            .filterNot { it.name.startsWith("OLD_") }
            .filterNot { it.name.startsWith("CHILD_") }
            .filterNot { it.name.startsWith("NEW_") }
            .toTypedArray()

        private val CONTENTS: Array<PageSet> = EXPRESSIONS.map { anim ->
            PageSet(
                Page(BookLine("${anim.animationId} | ${anim.name.lowercase()}", 65))
            )
        }.toTypedArray()

        @Suppress("UNUSED_PARAMETER")
        fun show(player: Player, pageNum: Int, buttonID: Int): Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, "Expressions", CONTENTS)

            val anim = EXPRESSIONS.getOrNull(pageNum)
            if (anim != null) {
                BookInterface.setNpcOnPage(
                    player,
                    pageNum,
                    MALE_NPC_ID,
                    BookInterface.FANCY_BOOK_3_49,
                    MALE_IMAGE_ENABLE_ID,
                    MALE_IMAGE_DRAW_ID,
                    anim.animationId,
                    1200
                )
                BookInterface.setNpcOnPage(
                    player,
                    pageNum,
                    FEMALE_NPC_ID,
                    BookInterface.FANCY_BOOK_3_49,
                    FEMALE_IMAGE_ENABLE_ID,
                    FEMALE_IMAGE_DRAW_ID,
                    anim.animationId,
                    1200
                )
            }

            return true
        }
    }

    override fun defineCommands() {
        define("models", Privilege.ADMIN) { player, args ->
            if (args.size > 2) {
                reject(player, "Usage: ::models")
                return@define
            }
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_2_27, ::display)
        }

        define("showexpression", Privilege.ADMIN) { player, args ->
            if (args.size > 1) {
                reject(player, "Usage: ::showexpression")
                return@define
            }
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
        }
    }
}