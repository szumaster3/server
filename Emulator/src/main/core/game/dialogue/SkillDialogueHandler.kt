package core.game.dialogue

import core.api.sendItemZoomOnInterface
import core.api.sendString
import core.api.ui.repositionChild
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ChildPositionContext
import core.net.packet.out.RepositionChild
import core.tools.StringUtils
import org.rs.consts.Components

open class SkillDialogueHandler(
    val player: Player,
    val type: SkillDialogue?,
    vararg data: Any,
) {
    val data: Array<Any>

    fun open() {
        player.dialogueInterpreter.open(SKILL_DIALOGUE, this)
    }

    fun display() {
        if (type == null) {
            player.debug("Error! Type is null.")
            return
        }
        type.display(player, this)
    }

    open fun create(
        amount: Int,
        index: Int,
    ) {}

    open fun getAll(index: Int): Int {
        return player.inventory.getAmount(data[0] as Item)
    }

    protected open fun getName(item: Item): String {
        return StringUtils.formatDisplayName(item.name.replace("Unfired", ""))
    }

    enum class SkillDialogue(
        val interfaceId: Int,
        private val baseButton: Int,
        private val length: Int,
    ) {
        ONE_OPTION(Components.SKILL_MULTI1_309, 5, 1) {
            override fun display(
                player: Player,
                handler: SkillDialogueHandler,
            ) {
                val item = handler.data[0] as Item
                repositionChild(player, Components.SKILL_MULTI1_309, 7, 8, 12)
                sendString(player, "<br><br><br><br>${item.name}", 309, 6)
                sendItemZoomOnInterface(player, Components.SKILL_MULTI1_309, 2, item.id, 160)
                repositionChild(player, Components.SKILL_MULTI1_309, 0, 12, 15)
                repositionChild(player, Components.SKILL_MULTI1_309, 1, 431, 15)
                repositionChild(player, Components.SKILL_MULTI1_309, 2, 210, 32)
                repositionChild(player, Components.SKILL_MULTI1_309, 3, 60, 35)
                repositionChild(player, Components.SKILL_MULTI1_309, 4, 60, 35)
                repositionChild(player, Components.SKILL_MULTI1_309, 5, 60, 35)
                repositionChild(player, Components.SKILL_MULTI1_309, 6, 60, 35)
            }

            override fun getAmount(
                handler: SkillDialogueHandler,
                buttonId: Int,
            ): Int {
                return when (buttonId) {
                    5 -> 1
                    4 -> 5
                    3 -> -1
                    else -> handler.getAll(getIndex(handler, buttonId))
                }
            }
        },

        MAKE_SET_ONE_OPTION(Components.SKILL_MULTI1_SMALL_582, 4, 1) {
            override fun display(
                player: Player,
                handler: SkillDialogueHandler,
            ) {
                val item = handler.data[0] as Item
                sendItemZoomOnInterface(player, Components.SKILL_MULTI1_SMALL_582, 2, item.id, 160)
                sendString(player, "<br><br><br><br>${item.name}", 582, 5)
                repositionChild(player, Components.SKILL_MULTI1_SMALL_582, 0, 12, 15)
                repositionChild(player, Components.SKILL_MULTI1_SMALL_582, 1, 431, 15)
                repositionChild(player, Components.SKILL_MULTI1_SMALL_582, 6, 6, 16)
                repositionChild(player, Components.SKILL_MULTI1_SMALL_582, 2, 207, 23)
                repositionChild(player, Components.SKILL_MULTI1_SMALL_582, 3, 60, 35)
                repositionChild(player, Components.SKILL_MULTI1_SMALL_582, 4, 60, 35)
                repositionChild(player, Components.SKILL_MULTI1_SMALL_582, 5, 60, 35)
            }

            override fun getAmount(
                handler: SkillDialogueHandler,
                buttonId: Int,
            ): Int {
                return when (buttonId) {
                    5 -> 1
                    4 -> 1
                    3 -> 5
                    2 -> 10
                    else -> 10
                }
            }
        },

        TWO_OPTION(Components.SKILL_MAKE_303, 7, 2) {
            override fun display(
                player: Player,
                handler: SkillDialogueHandler,
            ) {
                var item: Item
                for (i in handler.data.indices) {
                    item = handler.data[i] as Item
                    repositionChild(player, Components.SKILL_MAKE_303, 0, 12, 15)
                    repositionChild(player, Components.SKILL_MAKE_303, 1, 431, 15)

                    if (i == 0) {
                        sendString(player, "<br><br><br><br><br>" + handler.getName(item), 303, 7)
                    } else if (i == 1) {
                        sendString(player, "<br><br><br><br><br>" + handler.getName(item), 303, 11)
                    }

                    player.packetDispatch.sendItemZoomOnInterface(item.id, 180, Components.SKILL_MAKE_303, 2 + i)
                }
            }

            override fun getIndex(
                handler: SkillDialogueHandler?,
                buttonId: Int,
            ): Int {
                when (buttonId) {
                    6, 5, 4, 3 -> return 0
                    10, 9, 8, 7 -> return 1
                }
                return 1
            }

            override fun getAmount(
                handler: SkillDialogueHandler,
                buttonId: Int,
            ): Int {
                when (buttonId) {
                    6, 10 -> return 1
                    5, 9 -> return 5
                    4, 8 -> return 10
                    3, 7 -> return -1
                }
                return 1
            }
        },

        THREE_OPTION(Components.SKILL_MAKE_304, 8, 3) {
            override fun display(
                player: Player,
                handler: SkillDialogueHandler,
            ) {
                var item: Item? = null
                for (i in 0..2) {
                    item = handler.data[i] as Item
                    repositionChild(player, Components.SKILL_MAKE_304, 0, 12, 13)
                    repositionChild(player, Components.SKILL_MAKE_304, 1, 431, 13)
                    player.packetDispatch.sendItemZoomOnInterface(item.id, 170, Components.SKILL_MAKE_304, 2 + i)
                    player.packetDispatch.sendString(
                        "<br><br><br><br>" + item.name,
                        Components.SKILL_MAKE_304,
                        304 - 296 + i * 4,
                    )
                }
            }

            override fun getIndex(
                handler: SkillDialogueHandler?,
                buttonId: Int,
            ): Int {
                when (buttonId) {
                    7, 6, 5, 4 -> return 0
                    11, 10, 9, 8 -> return 1
                    15, 14, 13, 12 -> return 2
                }
                return 1
            }

            override fun getAmount(
                handler: SkillDialogueHandler,
                buttonId: Int,
            ): Int {
                when (buttonId) {
                    7, 11, 15 -> return 1
                    6, 10, 14 -> return 5
                    5, 9, 13 -> return 10
                    4, 8, 12 -> return -1
                }
                return 1
            }
        },

        FOUR_OPTION(Components.SKILL_MAKE_305, 9, 4) {
            override fun display(
                player: Player,
                handler: SkillDialogueHandler,
            ) {
                var item: Item? = null
                for (i in 0..3) {
                    item = handler.data[i] as Item
                    repositionChild(player, Components.SKILL_MAKE_305, 0, 12, 15)
                    repositionChild(player, Components.SKILL_MAKE_305, 1, 431, 15)
                    player.packetDispatch.sendItemZoomOnInterface(item.id, 160, Components.SKILL_MAKE_305, 2 + i)
                    sendString(player, "<br><br><br><br>" + item.name, Components.SKILL_MAKE_305, 305 - 296 + i * 4)
                }
            }

            override fun getIndex(
                handler: SkillDialogueHandler?,
                buttonId: Int,
            ): Int {
                when (buttonId) {
                    5, 8, 6, 7 -> return 0
                    9, 10, 11, 12 -> return 1
                    13, 14, 15, 16 -> return 2
                    17, 18, 19, 20 -> return 3
                }
                return 0
            }

            override fun getAmount(
                handler: SkillDialogueHandler,
                buttonId: Int,
            ): Int {
                when (buttonId) {
                    8, 12, 16, 20 -> return 1
                    7, 11, 15, 19 -> return 5
                    6, 10, 14, 18 -> return 10
                    5, 9, 13, 17 -> return -1
                }
                return 1
            }
        },

        FIVE_OPTION(Components.SKILL_MAKE_306, 7, 5) {
            private val positions =
                arrayOf(
                    intArrayOf(10, 30),
                    intArrayOf(117, 10),
                    intArrayOf(217, 20),
                    intArrayOf(317, 15),
                    intArrayOf(408, 15),
                )

            override fun display(
                player: Player,
                handler: SkillDialogueHandler,
            ) {
                var item: Item
                player.interfaceManager.openChatbox(Components.SKILL_MAKE_306)
                for (i in handler.data.indices) {
                    item = handler.data[i] as Item
                    sendString(
                        player,
                        "<br><br><br><br>" + handler.getName(item),
                        Components.SKILL_MAKE_306,
                        10 + 4 * i,
                    )
                    player.packetDispatch.sendItemZoomOnInterface(item.id, 160, Components.SKILL_MAKE_306, 2 + i)
                    PacketRepository.send(
                        RepositionChild::class.java,
                        ChildPositionContext(
                            player,
                            Components.SKILL_MAKE_306,
                            2 + i,
                            positions[i][0],
                            positions[i][1],
                        ),
                    )
                }
                repositionChild(player, Components.SKILL_MAKE_306, 0, 12, 15)
                repositionChild(player, Components.SKILL_MAKE_306, 1, 431, 15)
            }

            override fun getIndex(
                handler: SkillDialogueHandler?,
                buttonId: Int,
            ): Int {
                when (buttonId) {
                    9, 8, 7, 6 -> return 0
                    13, 12, 11, 10 -> return 1
                    17, 16, 15, 14 -> return 2
                    21, 20, 19, 18 -> return 3
                    25, 24, 23, 22 -> return 4
                }
                return 0
            }

            override fun getAmount(
                handler: SkillDialogueHandler,
                buttonId: Int,
            ): Int {
                when (buttonId) {
                    9, 13, 17, 21, 25 -> return 1
                    8, 12, 16, 20, 24 -> return 5
                    7, 11, 15, 19, 23 -> return 10
                    6, 10, 14, 18, 22 -> return -1
                }
                return 1
            }
        }, ;

        open fun display(
            player: Player,
            handler: SkillDialogueHandler,
        ) {}

        open fun getAmount(
            handler: SkillDialogueHandler,
            buttonId: Int,
        ): Int {
            for (k in 0..3) {
                for (i in 0 until length) {
                    val `val` = baseButton - k + 4 * i
                    if (`val` == buttonId) {
                        return if (k == 13) {
                            1
                        } else if (k == 8) {
                            5
                        } else if (k == 7) {
                            10
                        } else {
                            6
                        }
                    }
                }
            }
            return -1
        }

        open fun getIndex(
            handler: SkillDialogueHandler?,
            buttonId: Int,
        ): Int {
            var index = 0
            for (k in 0..3) {
                for (i in 1 until length) {
                    val `val` = baseButton + k + 4 * i
                    if (`val` == buttonId) {
                        return index + 1
                    } else if (`val` <= buttonId) {
                        index++
                    }
                }
                index = 0
            }
            return index
        }

        companion object {
            fun forLength(length2: Int): SkillDialogue? {
                for (dial in values()) {
                    if (dial.length == length2) {
                        return dial
                    }
                }
                return null
            }
        }
    }

    companion object {
        const val SKILL_DIALOGUE = 3 shl 16
    }

    init {
        this.data = data as Array<Any>
    }
}
