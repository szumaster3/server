package content.global.ame.quizmaster

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import org.rs.consts.Animations
import org.rs.consts.Items

object QuizMaster {
    const val COINS = Items.COINS_995
    const val MYSTERY_BOX = Items.MYSTERY_BOX_6199
    const val SIT_ANIMATION = Animations.SITTING_IN_THERAPIST_CHAIR_FROM_RANDOM_EVENT_2378

    val EVENT_LOCATION: Location = Location.create(1952, 4764, 1)

    val CORRECT =
        arrayOf(
            "Hey, you're good at this! CORRECT!",
            "Absolutely RIGHT! Keep going for the win!",
            "Wow, you're a smart one! You're absolutely right!",
            "COR-RECT! Okay, next question!",
            "DING DING DING That's RIGHT! Good for you!",
            "YES! You're RIGHT!",
        )

    val WRONG =
        arrayOf(
            "Huh...? Didn't you know that one?",
            "No. No, that's not right at all.",
            "No, sorry, Try harder!",
            "Better luck next time!",
            "WRONG! That's just WRONG!",
            "WRONG!",
            "No, no, no... That's completely WRONG!",
            "Nope, that's not it.",
            "BZZZZZZZ! WRONG!",
        )

    fun cleanup(player: Player) {
        player.locks.unlockTeleport()
        player.locks.unlockInteraction()
        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        clearLogoutListener(player, RandomEvent.logout())
        removeAttributes(player, GameAttributes.RE_QUIZ_SCORE, RandomEvent.save())
    }

    fun init(player: Player) {
        lockTeleport(player)
        setAttribute(player, RandomEvent.save(), player.location)
        teleport(player, EVENT_LOCATION, TeleportManager.TeleportType.NORMAL)
        registerLogoutListener(player, RandomEvent.logout()) { p ->
            p.location = getAttribute(p, RandomEvent.save(), player.location)
        }
    }
}
