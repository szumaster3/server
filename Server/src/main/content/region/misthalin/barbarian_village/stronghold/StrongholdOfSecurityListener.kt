package content.region.misthalin.barbarian_village.stronghold

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.emote.Emotes
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery
import shared.consts.Sounds

class StrongholdOfSecurityListener : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles stronghold rewards.
         */

        on(STRONGHOLD_TREASURE, IntType.SCENERY, "open", "search") { player, node ->

            val rewardIndex = when (node.id) {
                Scenery.GIFT_OF_PEACE_16135 -> 0
                Scenery.GRAIN_OF_PLENTY_16077 -> 1
                Scenery.BOX_OF_HEALTH_16118 -> 2
                Scenery.CRADLE_OF_LIFE_16047 -> 3
                else -> return@on true
            }

            if (rewardIndex != 3) {
                if (player.getSavedData().globalData.hasStrongholdReward(rewardIndex + 1)) {
                    sendDialogueLines(player, "You have already claimed your reward from this level.")
                } else {

                    val (music, message) = when (rewardIndex) {
                        0 -> Pair(157, "The box hinges creak and appear to be forming audible words....")
                        1 -> Pair(179, "The grain shifts in the sack, sighing audible words....")
                        2 -> Pair(177, "The box hinges creak and appear to be forming audible words....")
                        else -> return@on true
                    }

                    val rewardCoins = when (rewardIndex) {
                        0 -> 2000
                        1 -> 3000
                        2 -> 5000
                        else -> return@on true
                    }

                    val emote = when (rewardIndex) {
                        0 -> Emotes.FLAP
                        1 -> Emotes.SLAP_HEAD
                        2 -> Emotes.IDEA
                        else -> return@on true
                    }

                    val emoteMessage = when (rewardIndex) {
                        0 -> "You have also unlocked the Flap emote!"
                        1 -> "You have also unlocked the Slap Head emote!"
                        2 -> "You have also unlocked the Idea emote!"
                        else -> return@on true
                    }

                    playAudio(player, Sounds.DOOR_CREAK_61)
                    playJingle(player, music)
                    sendDialogue(player, message)

                    addDialogueAction(player) { _, _ ->
                        if (!addItem(player, Items.COINS_995, rewardCoins, Container.INVENTORY)) {
                            sendMessage(player, "You don't have enough inventory space.")
                            return@addDialogueAction
                        }
                        unlockEmote(player, emote)
                        player.getSavedData().globalData.getStrongHoldRewards()[rewardIndex] = true
                        sendItemDialogue(player, Items.COINS_8898, "...congratulations adventurer, you have been deemed worthy of this reward. $emoteMessage")
                        if (rewardIndex == 2) {
                            player.fullRestore()
                            sendMessage(player, "You feel refreshed and renewed.")
                        }

                    }
                }
            } else {
                // Handle 4th reward (Cradle of Life).
                if (!player.getSavedData().globalData.hasStrongholdReward(4)) {
                    playJingle(player, 158)
                }
                playAudio(player, Sounds.SOS_CHOIR_1246)
                player.dialogueInterpreter.open(96873)
            }
            return@on true
        }

        /*
         * Handles interaction with dead explorer.
         */

        on(DEAD_EXPLORER, IntType.SCENERY, "search") { player, _ ->
            val hasNotes = hasAnItem(player, STRONGHOLD_NOTES).container != null

            animate(player, Animations.HUMAN_PICKPOCKETING_881)

            if (hasNotes) {
                sendMessage(player, "You don't find anything.")
            } else {
                sendDialogue(player, "You rummage around in the dead explorer's bag.....")
                addDialogueAction(player) { _, _ ->
                    sendItemDialogue(player, STRONGHOLD_NOTES, "You find a book of hand written notes.")
                    addItemOrDrop(player, STRONGHOLD_NOTES, 1)
                }
            }
            return@on true
        }
    }


    companion object {
        const val DEAD_EXPLORER = Scenery.DEAD_EXPLORER_16152
        const val STRONGHOLD_NOTES = Items.STRONGHOLD_NOTES_9004
        val STRONGHOLD_TREASURE = intArrayOf(Scenery.GIFT_OF_PEACE_16135, Scenery.GRAIN_OF_PLENTY_16077, Scenery.BOX_OF_HEALTH_16118, Scenery.CRADLE_OF_LIFE_16047)
    }
}