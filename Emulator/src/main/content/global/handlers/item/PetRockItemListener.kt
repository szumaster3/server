package content.global.handlers.item

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.impl.Projectile.getLocation
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class PetRockItemListener : InteractionListener {
    override fun defineListeners() {
        on(Items.PET_ROCK_3695, IntType.ITEM, "interact") { player, _ ->
            if (player.inCombat()) {
                sendMessage(player, "You can't interact with pet rock while being in combat.")
                return@on true
            }
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 -> options("Talk", "Stroke", "Feed", "Fetch", "Stay").also { stage++ }
                            1 ->
                                when (buttonID) {
                                    1 -> {
                                        val randomDialogue = RandomFunction.random(0, 6)
                                        when (randomDialogue) {
                                            0 ->
                                                when (stage) {
                                                    0 -> playerl(FaceAnim.FRIENDLY, "Good day, rock!").also { stage++ }
                                                    1 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    2 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Oooh, I love jokes! Go on then!",
                                                        ).also { stage++ }

                                                    3 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    4 -> playerl(FaceAnim.FRIENDLY, "Who's there?").also { stage++ }
                                                    5 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    6 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Interrupting cow wh",
                                                        ).also { stage++ }
                                                    7 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    8 -> playerl(FaceAnim.FRIENDLY, "Haha, good one!").also { stage++ }
                                                    9 -> end().also { stage = END_DIALOGUE }
                                                }

                                            1 ->
                                                when (stage) {
                                                    0 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Hey there, rock! How are you settling into your new home?",
                                                        ).also { stage++ }

                                                    1 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    2 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "I'm Glad to hear it!",
                                                        ).also { stage++ }
                                                    3 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Erm, this is kind of awkward, but... one of the neighbours found a pile of pebbles on their lawn.",
                                                        ).also { stage++ }

                                                    4 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Now, I'm not saying it WAS you...",
                                                        ).also { stage++ }

                                                    5 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    6 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Alright, alright, I believe you! There's no need for that kind of language!",
                                                        ).also { stage++ }

                                                    7 -> end().also { stage = END_DIALOGUE }
                                                }

                                            2 ->
                                                when (stage) {
                                                    0 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Hello there, rock. How are things?",
                                                        ).also { stage++ }

                                                    1 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    2 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Hmmm, I don't know. Have you tried swamp tar? I hear that's good at clearing rashes.",
                                                        ).also { stage++ }

                                                    3 -> npc("...").also { stage++ }
                                                    4 -> end().also { stage = END_DIALOGUE }
                                                }

                                            3 ->
                                                when (stage) {
                                                    0 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Hello there, rock. How are things?",
                                                        ).also { stage++ }

                                                    1 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    2 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Oh, what a lovely song! That was a nice surprise, rock!",
                                                        ).also { stage++ }

                                                    3 -> end().also { stage = END_DIALOGUE }
                                                }

                                            4 ->
                                                when (stage) {
                                                    0 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Hey there, rock! How are you settling into your new home?",
                                                        ).also { stage++ }

                                                    1 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    2 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Oh, I'm sorry to hear that.",
                                                        ).also { stage++ }

                                                    3 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    4 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "I'll be sure to complain to the housing association on your behalf, and petition for them to be moved out of the neighbourhood!",
                                                        ).also { stage++ }

                                                    5 -> end().also { stage = END_DIALOGUE }
                                                }

                                            5 ->
                                                when (stage) {
                                                    0 -> playerl(FaceAnim.FRIENDLY, "Good day, rock!").also { stage++ }
                                                    1 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    2 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Oooh, I love jokes! Go on then!",
                                                        ).also { stage++ }

                                                    3 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    4 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "I don't know, what is the difference between a cow and a goblin?",
                                                        ).also { stage++ }

                                                    5 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    6 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "Rock! How could you! That's awful!",
                                                        ).also { stage++ }

                                                    7 ->
                                                        sendItemDialogue(
                                                            player,
                                                            Items.PET_ROCK_3695,
                                                            "...",
                                                        ).also { stage++ }
                                                    8 ->
                                                        playerl(
                                                            FaceAnim.FRIENDLY,
                                                            "I don't care if the other rocks think it's funny, you're not to talk like that again!",
                                                        ).also { stage++ }

                                                    9 -> end().also { stage = END_DIALOGUE }
                                                }
                                        }
                                    }

                                    2 -> {
                                        end()
                                        sendMessage(player, "You stroke your pet rock.")
                                        animate(player, Animations.HUMAN_STROKE_PET_ROCK_1333, false)
                                        queueScript(
                                            player,
                                            animationDuration(Animation(Animations.HUMAN_STROKE_PET_ROCK_1333)),
                                            QueueStrength.SOFT,
                                        ) {
                                            sendMessage(player, "Your rock seems much happier.")
                                            return@queueScript stopExecuting(player)
                                        }
                                    }

                                    3 -> {
                                        sendMessage(player, "You try and feed the rock.")
                                        sendMessage(player, "Your rock doesn't seem hungry.")
                                        end()
                                    }

                                    4 -> {
                                        playerl(FaceAnim.FRIENDLY, "Want to fetch the stick, rock? Of course you do...")
                                        stage = 2
                                    }

                                    5 -> {
                                        playerl(FaceAnim.FRIENDLY, "Be a good rock...")
                                        sendMessageWithDelay(
                                            player,
                                            "You wait a few seconds and pick your rock back up and pet it.",
                                            6,
                                        )
                                        visualize(player, 6664, 1156)
                                        end()
                                    }
                                }

                            2 -> {
                                end()
                                val animDuration = animationDuration(Animation(Animations.HUMAN_THROW_STICK_6665))
                                lock(player, duration = animDuration)
                                lockInteractions(player, duration = animDuration)
                                face(player, Location.getRandomLocation(getLocation(player), 2, true))
                                playAudio(player, Sounds.THROW_STICK_1942)
                                visualize(player, Animations.HUMAN_THROW_STICK_6665, 1157)
                                spawnProjectile(
                                    getLocation(player),
                                    Location.getRandomLocation(getLocation(player), 5, true),
                                    1158,
                                    40,
                                    0,
                                    150,
                                    250,
                                    25,
                                )
                            }
                        }
                    }
                },
            )
            return@on true
        }
    }
}
