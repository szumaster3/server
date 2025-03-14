package content.global.ame.mime

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.api.ui.restoreTabs
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

object MimeUtils {
    val MIME_EVENT_LOCATION = Location(2008, 4764, 0)
    val MIME_LOCATION = Location(2008, 4762, 0)
    val PLAYER_LOCATION = Location(2007, 4761, 0)
    val SCENERY_LOCATION = Location(2010, 4761, 0)

    const val LIGH_ON = 3644
    const val LIGHT_OFF = 3645

    fun cleanup(player: Player) {
        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        clearLogoutListener(player, RandomEvent.logout())
        removeAttributes(
            player,
            RandomEvent.logout(),
            RandomEvent.save(),
            GameAttributes.RE_MIME_EMOTE,
            GameAttributes.RE_MIME_INDEX,
            GameAttributes.RE_MIME_CORRECT,
            GameAttributes.RE_MIME_WRONG,
        )
        restoreTabs(player)
        closeInterface(player)
        unlock(player)
    }

    fun reward(player: Player) {
        val hasMask = hasAnItem(player, Items.MIME_MASK_3057).container != null
        val hasTop = hasAnItem(player, Items.MIME_TOP_3058).container != null
        val hasLegs = hasAnItem(player, Items.MIME_LEGS_3059).container != null
        val hasBoots = hasAnItem(player, Items.MIME_BOOTS_3061).container != null
        val hasGloves = hasAnItem(player, Items.MIME_GLOVES_3060).container != null
        when {
            (!hasMask) -> {
                sendDialogue(player, "You can now use Lean on air emote!")
                addItemOrDrop(player, Items.MIME_MASK_3057, 1)
                unlockEmote(player, 28)
            }

            (!hasTop) -> {
                sendDialogue(player, "You can now use Climb Rope emote!")
                addItemOrDrop(player, Items.MIME_TOP_3058, 1)
                unlockEmote(player, 27)
            }

            (!hasLegs) -> {
                sendDialogue(player, "You can now use Glass Wall emote!")
                addItemOrDrop(player, Items.MIME_LEGS_3059, 1)
                unlockEmote(player, 29)
            }

            (!hasBoots) && (!hasGloves) -> {
                sendDialogue(player, "You can now use Glass Box emote!")
                addItemOrDrop(player, Items.MIME_GLOVES_3060, 1)
                addItemOrDrop(player, Items.MIME_BOOTS_3061, 1)
                unlockEmote(player, 26)
            }

            else -> addItemOrDrop(player, Items.COINS_995, 500)
        }
    }

    fun getEmote(player: Player) {
        val npc = findNPC(NPCs.MIME_1056)
        var emoteId = getAttribute(player, GameAttributes.RE_MIME_EMOTE, -1)
        sendUnclosablePlainDialogue(player, true, "", "Watch the Mime.", "See what emote he performs.")
        submitIndividualPulse(
            npc!!,
            object : Pulse() {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> npc.faceLocation(location(2011, 4759, 0))
                        1 ->
                            replaceScenery(
                                Scenery(
                                    LIGH_ON,
                                    PLAYER_LOCATION,
                                ),
                                LIGHT_OFF,
                                -1,
                            )

                        3 -> {
                            when (emoteId) {
                                2 -> animate(npc, Emotes.THINK)
                                3 -> animate(npc, Emotes.CRY)
                                4 -> animate(npc, Emotes.LAUGH)
                                5 -> animate(npc, Emotes.DANCE)
                                6 -> animate(npc, Emotes.CLIMB_ROPE)
                                7 -> animate(npc, Emotes.LEAN_ON_AIR)
                                8 -> animate(npc, Emotes.GLASS_BOX)
                                9 -> animate(npc, Emotes.GLASS_WALL)
                            }
                            setAttribute(player, GameAttributes.RE_MIME_INDEX, emoteId)
                        }

                        10 -> npc.faceLocation(location(2008, 4762, 0))
                        11 -> animate(npc, Emotes.BOW)
                        14 ->
                            replaceScenery(
                                Scenery(
                                    LIGH_ON,
                                    SCENERY_LOCATION,
                                ),
                                LIGHT_OFF,
                                -1,
                            )

                        15 -> {
                            replaceScenery(
                                Scenery(
                                    LIGHT_OFF,
                                    PLAYER_LOCATION,
                                ),
                                LIGH_ON,
                                -1,
                            )
                            openInterface(player, Components.MACRO_MIME_EMOTES_188)
                            return true
                        }
                    }
                    return false
                }
            },
        )
    }

    fun getContinue(player: Player) {
        submitIndividualPulse(
            player,
            object : Pulse() {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        4 -> {
                            if (getAttribute(player, GameAttributes.RE_MIME_CORRECT, -1) == 2) {
                                cleanup(player)
                                openInterface(player, Components.CHATDEFAULT_137)
                                queueScript(player, 2, QueueStrength.STRONG) {
                                    reward(player)
                                    return@queueScript stopExecuting(player)
                                }
                            } else {
                                if (getAttribute(player, GameAttributes.RE_MIME_WRONG, -1) == 1) {
                                    setAttribute(player, GameAttributes.RE_MIME_EMOTE, RandomFunction.random(2, 9))
                                    removeAttribute(player, GameAttributes.RE_MIME_WRONG)
                                    openInterface(player, Components.CHATDEFAULT_137)
                                    replaceScenery(
                                        Scenery(
                                            LIGH_ON,
                                            PLAYER_LOCATION,
                                        ),
                                        LIGHT_OFF,
                                        -1,
                                    )
                                    replaceScenery(
                                        Scenery(
                                            LIGHT_OFF,
                                            SCENERY_LOCATION,
                                        ),
                                        LIGH_ON,
                                        -1,
                                    )
                                    sendUnclosablePlainDialogue(
                                        player,
                                        true,
                                        "",
                                        "Watch the Mime.",
                                        "See what emote he performs.",
                                    )
                                    getEmote(player)
                                }
                            }
                        }
                    }
                    return false
                }
            },
        )
    }
}
