package content.global.random.event.pinball

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.api.utils.CameraShakeType
import core.api.utils.PlayerCamera
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import shared.consts.*
import shared.consts.Scenery as Objects

/**
 * Utils for pinball random event.
 */
object PinballUtils {
    /**
     * The random event npc.
     */
    val PINBALL_EVENT_MYSTERIOUS_OLD_MAN = NPC(NPCs.MYSTERIOUS_OLD_MAN_410, Location.create(1971, 5046, 0))

    /**
     * The varbit storing the current score.
     */
    const val VARBIT_PINBALL_SCORE = Vars.VARBIT_RE_PINBALL_SCORE_2121

    /**
     * The zone borders of the event area.
     */
    val PINBALL_EVENT_ZONE_BORDERS = ZoneBorders(1961, 5033, 1982, 5054)

    /**
     * The pillar object ids.
     */
    val PILLAR_OBJECTS = intArrayOf(
        Objects.PINBALL_POST_15001,
        Objects.PINBALL_POST_15003,
        Objects.PINBALL_POST_15005,
        Objects.PINBALL_POST_15007,
        Objects.PINBALL_POST_15009
    )

    /**
     * The reward item ids for this event.
     */
    private val PINBALL_REWARD = intArrayOf(
        Items.UNCUT_DIAMOND_1618,
        Items.UNCUT_RUBY_1620,
        Items.UNCUT_EMERALD_1622,
        Items.UNCUT_SAPPHIRE_1624,
    )

    /**
     * The list of pillars used in the event.
     */
    val PINBALL_PILLARS = listOf(
        Scenery(Objects.PINBALL_POST_15001, Location(1967, 5046, 0)),
        Scenery(Objects.PINBALL_POST_15003, Location(1969, 5049, 0)),
        Scenery(Objects.PINBALL_POST_15005, Location(1972, 5050, 0)),
        Scenery(Objects.PINBALL_POST_15007, Location(1975, 5049, 0)),
        Scenery(Objects.PINBALL_POST_15009, Location(1977, 5046, 0))
    )

    /**
     * Randomly selects the next pillar for the player to tag.
     *
     * @param player The player.
     * @return The new pillar.
     */
    fun getTag(player: Player): Int {
        val oldIndex = getAttribute(player, GameAttributes.RE_PINBALL_INTER, -1)
        val newIndex = PINBALL_PILLARS.indices.random()

        if (oldIndex in PINBALL_PILLARS.indices) {
            animateScenery(PINBALL_PILLARS[oldIndex], Animations.RESET_PILLAR_ANIMATION_4006)
        }

        animateScenery(PINBALL_PILLARS[newIndex], Animations.ANIMATE_PILLAR_4005)
        setAttribute(player, GameAttributes.RE_PINBALL_INTER, newIndex)
        setAttribute(player, GameAttributes.RE_PINBALL_OBJ, newIndex)

        PlayerCamera(player).shake(CameraShakeType.values().random(), 0, 0, 128, 2)
        return newIndex
    }

    /**
     * Cleanup.
     */
    fun cleanup(player: Player) {
        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        clearLogoutListener(player, RandomEvent.logout())
        PlayerCamera(player).reset()
        openInterface(player, Components.CHATDEFAULT_137)
        closeDialogue(player)
        closeOverlay(player)
        closeInterface(player)
        restoreTabs(player)
        removeAttributes(player, RandomEvent.save(), GameAttributes.RE_PINBALL_START, GameAttributes.RE_PINBALL_OBJ, GameAttributes.RE_PINBALL_INTER)
        setMinimapState(player, 0)
    }

    /**
     * Give the reward for the player.
     */
    fun reward(player: Player) {
        queueScript(player, 2, QueueStrength.SOFT) {
            AntiMacro.terminateEventNpc(player)
            setVarbit(player, VARBIT_PINBALL_SCORE, 0)
            addItemOrDrop(
                player,
                PINBALL_REWARD.random(),
                if (PINBALL_REWARD.contains(Items.UNCUT_DIAMOND_1618)) {
                    2
                } else {
                    (3..5).random()
                },
            )
            return@queueScript stopExecuting(player)
        }
    }

    /**
     * Resets the player's Pinball score to zero.
     *
     * @param player The player whose score is being reset.
     */
    fun resetScore(player: Player) {
        setVarbit(player, VARBIT_PINBALL_SCORE, 0)
    }

    /**
     * Increments the score by one.
     *
     * @param player The player.
     */
    fun incrementScore(player: Player) {
        val points = getVarbit(player, VARBIT_PINBALL_SCORE) + 1
        setVarbit(player, VARBIT_PINBALL_SCORE, points)
    }

    /**
     * Checks if player tag 10 pillars.
     *
     * @param player The player.
     * @return True if the score is 10 or more, false otherwise.
     */
    fun isComplete(player: Player) = getVarbit(player, VARBIT_PINBALL_SCORE) >= 10
}