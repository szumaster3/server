package content.region.karamja.quest.totem.handlers

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.world.map.Location

class CombinationLockDoor : InterfaceListener {
    override fun defineInterfaceListeners() {
        val LETTERONEBACK = 17
        val LETTERONEFORWARD = 18
        val LETTERTWOBACK = 19
        val LETTERTWOFORWARD = 20
        val LETTERTHREEBACK = 21
        val LETTERTHREEFORWARD = 22
        val LETTERFOURBACK = 23
        val LETTERFOURFORWARD = 24
        val ENTER = 27
        val EXIT = 28
        val DOORLOCKINTERFACE = 369
        val LETTERS =
            arrayOf(
                "A",
                "B",
                "C",
                "D",
                "E",
                "F",
                "G",
                "H",
                "I",
                "J",
                "K",
                "L",
                "M",
                "N",
                "O",
                "P",
                "Q",
                "R",
                "S",
                "T",
                "U",
                "V",
                "W",
                "X",
                "Y",
                "Z",
            )

        onOpen(DOORLOCKINTERFACE) { player, _ ->
            setAttribute(player, "tt-letter-one", 0)
            setAttribute(player, "tt-letter-two", 0)
            setAttribute(player, "tt-letter-three", 0)
            setAttribute(player, "tt-letter-four", 0)
            return@onOpen true
        }

        onClose(DOORLOCKINTERFACE) { player, _ ->
            removeAttribute(player, "tt-letter-one")
            removeAttribute(player, "tt-letter-two")
            removeAttribute(player, "tt-letter-three")
            removeAttribute(player, "tt-letter-four")
            return@onClose true
        }

        on(DOORLOCKINTERFACE) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                LETTERONEBACK -> {
                    if (player.getAttribute("tt-letter-one", 0) == 0) {
                        setAttribute(player, "tt-letter-one", 25)
                        sendString(player, LETTERS[player.getAttribute("tt-letter-one", 0)], DOORLOCKINTERFACE, 13)
                    } else {
                        (player.incrementAttribute("tt-letter-one", -1))
                        sendString(player, LETTERS[player.getAttribute("tt-letter-one", 0)], DOORLOCKINTERFACE, 13)
                    }
                }

                LETTERONEFORWARD -> {
                    if (player.getAttribute("tt-letter-one", 0) == 25) {
                        setAttribute(player, "tt-letter-one", 0)
                        sendString(player, LETTERS[player.getAttribute("tt-letter-one", 0)], DOORLOCKINTERFACE, 13)
                    } else {
                        (player.incrementAttribute("tt-letter-one", 1))
                        sendString(player, LETTERS[player.getAttribute("tt-letter-one", 0)], DOORLOCKINTERFACE, 13)
                    }
                }

                LETTERTWOBACK -> {
                    if (player.getAttribute("tt-letter-two", 0) == 0) {
                        setAttribute(player, "tt-letter-two", 25)
                        sendString(player, LETTERS[player.getAttribute("tt-letter-two", 0)], DOORLOCKINTERFACE, 14)
                    } else {
                        (player.incrementAttribute("tt-letter-two", -1))
                        sendString(player, LETTERS[player.getAttribute("tt-letter-two", 0)], DOORLOCKINTERFACE, 14)
                    }
                }

                LETTERTWOFORWARD -> {
                    if (player.getAttribute("tt-letter-two", 0) == 25) {
                        setAttribute(player, "tt-letter-two", 0)
                        sendString(player, LETTERS[player.getAttribute("tt-letter-two", 0)], DOORLOCKINTERFACE, 14)
                    } else {
                        (player.incrementAttribute("tt-letter-two", 1))
                        sendString(player, LETTERS[player.getAttribute("tt-letter-two", 0)], DOORLOCKINTERFACE, 14)
                    }
                }

                LETTERTHREEBACK -> {
                    if (player.getAttribute("tt-letter-three", 0) == 0) {
                        setAttribute(player, "tt-letter-three", 25)
                        sendString(player, LETTERS[player.getAttribute("tt-letter-three", 0)], DOORLOCKINTERFACE, 15)
                    } else {
                        (player.incrementAttribute("tt-letter-three", -1))
                        sendString(player, LETTERS[player.getAttribute("tt-letter-three", 0)], DOORLOCKINTERFACE, 15)
                    }
                }

                LETTERTHREEFORWARD -> {
                    if (player.getAttribute("tt-letter-three", 0) == 25) {
                        setAttribute(player, "tt-letter-three", 0)
                        sendString(player, LETTERS[player.getAttribute("tt-letter-three", 0)], DOORLOCKINTERFACE, 15)
                    } else {
                        (player.incrementAttribute("tt-letter-three", 1))
                        sendString(player, LETTERS[player.getAttribute("tt-letter-three", 0)], DOORLOCKINTERFACE, 15)
                    }
                }

                LETTERFOURBACK -> {
                    if (player.getAttribute("tt-letter-four", 0) == 0) {
                        setAttribute(player, "tt-letter-four", 25)
                        sendString(player, LETTERS[player.getAttribute("tt-letter-four", 0)], DOORLOCKINTERFACE, 16)
                    } else {
                        (player.incrementAttribute("tt-letter-four", -1))
                        sendString(player, LETTERS[player.getAttribute("tt-letter-four", 0)], DOORLOCKINTERFACE, 16)
                    }
                }

                LETTERFOURFORWARD -> {
                    if (player.getAttribute("tt-letter-four", 0) == 25) {
                        setAttribute(player, "tt-letter-four", 0)
                        sendString(player, LETTERS[player.getAttribute("tt-letter-four", 0)], DOORLOCKINTERFACE, 16)
                    } else {
                        (player.incrementAttribute("tt-letter-four", 1))
                        sendString(player, LETTERS[player.getAttribute("tt-letter-four", 0)], DOORLOCKINTERFACE, 16)
                    }
                }

                ENTER -> {
                    val letterOne = LETTERS[player.getAttribute("tt-letter-one", 0)]
                    val letterTwo = LETTERS[player.getAttribute("tt-letter-two", 0)]
                    val letterThree = LETTERS[player.getAttribute("tt-letter-three", 0)]
                    val letterFour = LETTERS[player.getAttribute("tt-letter-four", 0)]

                    if (letterOne == "K" && letterTwo == "U" && letterThree == "R" && letterFour == "T") {
                        setAttribute(player, "/save:TT:DoorUnlocked", true)
                        sendMessage(player, "You hear a satisfying click, signifying that the door has been unlocked")
                        closeInterface(player)
                    } else {
                        sendMessage(player, "You hear a satisfying click, and then a worrying thunk.")
                        sendMessage(player, "The floor opens up beneath you sending you plummeting down")
                        sendMessage(player, "to the sewers.")
                        player.teleport(Location.create(2641, 9721, 0))
                        closeInterface(player)
                    }
                }
            }
            return@on true
        }
    }
}
