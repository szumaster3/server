package content.global.ame.mime

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

object MimeUtils {
    val MIME_EVENT_LOCATION = Location(2008, 4764, 0)
    val MIME_LOCATION = Location(2008, 4762, 0)
    val PLAYER_LOCATION = Location(2007, 4761, 0)
    val SCENERY_LOCATION = Location(2010, 4761, 0)

    const val SPOTLIGHT_ON = 3644
    const val SPOTLIGHT_OFF = 3645

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
                sendDialogue(player, "You can now use GlassProduct Wall emote!")
                addItemOrDrop(player, Items.MIME_LEGS_3059, 1)
                unlockEmote(player, 29)
            }

            (!hasBoots) && (!hasGloves) -> {
                sendDialogue(player, "You can now use GlassProduct Box emote!")
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
                        1 -> replaceScenery(Scenery(SPOTLIGHT_ON, PLAYER_LOCATION), SPOTLIGHT_OFF, -1)
                        3 -> {
                            when (emoteId) {
                                2 -> animate(npc, Emotes.THINK.animation)
                                3 -> animate(npc, Emotes.CRY.animation)
                                4 -> animate(npc, Emotes.LAUGH.animation)
                                5 -> animate(npc, Emotes.DANCE.animation)
                                6 -> animate(npc, Emotes.CLIMB_ROPE.animation)
                                7 -> animate(npc, Emotes.LEAN_ON_AIR.animation)
                                8 -> animate(npc, Emotes.GLASS_BOX.animation)
                                9 -> animate(npc, Emotes.GLASS_WALL.animation)
                            }
                            setAttribute(player, GameAttributes.RE_MIME_INDEX, emoteId)
                        }
                        10 -> npc.faceLocation(player.location)
                        11 -> animate(npc, Emotes.BOW.animation)
                        14 -> replaceScenery(Scenery(SPOTLIGHT_ON, SCENERY_LOCATION), SPOTLIGHT_OFF, -1)
                        15 -> {
                            replaceScenery(Scenery(SPOTLIGHT_OFF, PLAYER_LOCATION), SPOTLIGHT_ON, -1)
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
                                    setAttribute(player, GameAttributes.RE_MIME_EMOTE, (2..9).random())
                                    removeAttribute(player, GameAttributes.RE_MIME_WRONG)
                                    openInterface(player, Components.CHATDEFAULT_137)
                                    replaceScenery(Scenery(SPOTLIGHT_ON, PLAYER_LOCATION), SPOTLIGHT_OFF, -1)
                                    replaceScenery(Scenery(SPOTLIGHT_OFF, SCENERY_LOCATION), SPOTLIGHT_ON, -1)
                                    sendUnclosablePlainDialogue(player, true, "", "Watch the Mime.", "See what emote he performs.")
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
