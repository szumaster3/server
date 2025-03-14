package content.global.ame.pillory

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.api.ui.closeDialogue
import core.api.ui.restoreTabs
import core.api.ui.sendInterfaceConfig
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Sounds

object PilloryUtils {
    val INTERFACE = Components.MACRO_PILLORY_GUARD_188
    val LOCATIONS =
        arrayOf(
            Location(3226, 3407, 0),
            Location(3228, 3407, 0),
            Location(3230, 3407, 0),
            Location(2681, 3489, 0),
            Location(2683, 3489, 0),
            Location(2685, 3489, 0),
            Location(2604, 3105, 0),
            Location(2606, 3105, 0),
            Location(2608, 3105, 0),
        )

    fun cleanup(player: Player) {
        lock(player, 3)
        closeAllInterfaces(player)
        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        clearLogoutListener(player, RandomEvent.logout())
        removeAttributes(
            player,
            RandomEvent.save(),
            RandomEvent.logout(),
            GameAttributes.RE_PILLORY_KEYS,
            GameAttributes.RE_PILLORY_PADLOCK,
            GameAttributes.RE_PILLORY_CORRECT,
            GameAttributes.RE_PILLORY_SCORE,
            GameAttributes.RE_PILLORY_TARGET,
        )
        sendMessage(player, "You've escaped!")
        restoreTabs(player)
        AntiMacro.terminateEventNpc(player)
    }

    fun randomPillory(player: Player) {
        val keys =
            (0..3).toIntArray().let { keys ->
                keys.shuffle()
                return@let keys
            }
        val lock = intArrayOf(keys[1], keys[2], keys[3]).random()

        setAttribute(player, GameAttributes.RE_PILLORY_KEYS, keys)
        setAttribute(player, GameAttributes.RE_PILLORY_PADLOCK, lock)

        sendModelOnInterface(player, INTERFACE, 4, 9753 + lock)
        sendModelOnInterface(player, INTERFACE, 5, 9749 + keys[1])
        sendModelOnInterface(player, INTERFACE, 6, 9749 + keys[2])
        sendModelOnInterface(player, INTERFACE, 7, 9749 + keys[3])

        val numberToGetCorrect = getAttribute(player, GameAttributes.RE_PILLORY_CORRECT, 3)
        val correctCount = getAttribute(player, GameAttributes.RE_PILLORY_CORRECT, 0)
        for (i in 1..6) {
            if (i <= correctCount) {
                sendModelOnInterface(player, INTERFACE, 10 + i, 9758)
            } else {
                sendModelOnInterface(player, INTERFACE, 10 + i, 9757)
            }
            sendInterfaceConfig(player, INTERFACE, 10 + i, i > numberToGetCorrect)
        }
    }

    fun selectedKey(
        player: Player,
        buttonID: Int,
    ) {
        val keys = getAttribute(player, GameAttributes.RE_PILLORY_KEYS, intArrayOf(0, 0, 0))
        val lock = getAttribute(player, GameAttributes.RE_PILLORY_PADLOCK, -1)
        if (keys[buttonID] == lock) {
            setAttribute(
                player,
                GameAttributes.RE_PILLORY_SCORE,
                getAttribute(player, GameAttributes.RE_PILLORY_SCORE, 0) + 1,
            )
            if (getAttribute(player, GameAttributes.RE_PILLORY_CORRECT, 3) <=
                getAttribute(player, GameAttributes.RE_PILLORY_SCORE, -1)
            ) {
                cleanup(player)
                return
            }
            randomPillory(player)
            sendPlainDialogue(
                player,
                true,
                "",
                "Correct!",
                "" + getAttribute(player, GameAttributes.RE_PILLORY_SCORE, 0) + " down, " +
                    (
                        getAttribute(player, GameAttributes.RE_PILLORY_CORRECT, 3) -
                            getAttribute(player, GameAttributes.RE_PILLORY_SCORE, 0)
                    ) +
                    " to go!",
            )
            sendInterfaceConfig(player, INTERFACE, 16 + getAttribute(player, GameAttributes.RE_PILLORY_SCORE, 1), false)
        } else {
            closeDialogue(player)
            playAudio(player, Sounds.PILLORY_WRONG_2268)
            if (getAttribute(player, GameAttributes.RE_PILLORY_CORRECT, 0) < 6) {
                setAttribute(
                    player,
                    GameAttributes.RE_PILLORY_CORRECT,
                    getAttribute(player, GameAttributes.RE_PILLORY_CORRECT, 0) + 1,
                )
            }
            setAttribute(player, GameAttributes.RE_PILLORY_CORRECT, 0)
            closeInterface(player)
            sendNPCDialogueLines(
                player,
                NPCs.TRAMP_2794,
                FaceAnim.OLD_ANGRY1,
                false,
                "Bah, that's not right.",
                "Use the key that matches the hole",
                "in the spinning lock.",
            )
            addDialogueAction(player) { player, button ->
                if (button >= 0) {
                    openInterface(player, INTERFACE)
                }
            }
        }
    }
}
