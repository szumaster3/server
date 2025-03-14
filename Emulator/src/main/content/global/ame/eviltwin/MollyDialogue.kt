package content.global.ame.eviltwin

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.world.GameWorld
import core.tools.END_DIALOGUE
import core.tools.RandomFunction

class MollyDialogue(
    var type: Int,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(EvilTwinUtils.mollyNPC!!.originalId)
        when (type) {
            0 ->
                when (stage) {
                    0 -> npc(FaceAnim.HAPPY, "Well done! You managed to catch my sister!").also { stage++ }
                    1 -> npc(FaceAnim.HAPPY, "Come next door and talk to me.").also { stage = END_DIALOGUE }
                }

            1 ->
                when (stage) {
                    0 ->
                        npc(
                            FaceAnim.ANGRY,
                            "Such incompetence! I should never have asked a",
                            "baboon like you to do a complex task like this!",
                            "Get out of my sight!",
                        ).also {
                            stage++
                        }

                    1 -> player(FaceAnim.SAD, "Err, sorry. I seem to have messed it up a little.").also { stage++ }
                    2 -> {
                        end()
                        EvilTwinUtils.cleanup(player!!)
                    }
                }

            2 ->
                when (stage) {
                    0 -> player("Well, I've managed to get her into the cage.").also { stage++ }
                    1 ->
                        npc(
                            FaceAnim.HAPPY,
                            "Fantastic! For so many years I've had to put up with",
                            "her and now she's locked up for good.",
                        ).also {
                            stage++
                        }

                    2 -> npc("Thank you for all your help. Take this as a reward.").also { stage++ }
                    3 -> {
                        queueScript(player!!, 2, QueueStrength.SOFT) {
                            closeInterface(player!!)
                            EvilTwinUtils.cleanup(player!!)
                            val randomReward = RandomFunction.getRandomElement(EvilTwinUtils.rewards)
                            val randAmount = RandomFunction.random(randomReward.amount)
                            addItemOrBank(player!!, randomReward.id, randAmount)
                            sendItemDialogue(
                                player!!,
                                randomReward,
                                "Molly's given you $randAmount ${getItemName(randomReward.id).lowercase()}s.",
                            )
                            if (freeSlots(player!!) == 0) {
                                sendMessage(player!!, "Your reward gift has been transferred to the bank.")
                            }

                            return@queueScript stopExecuting(player!!)
                        }
                        stage = END_DIALOGUE
                    }
                }

            3 -> {
                val startDialogue = !getAttribute(player!!, GameAttributes.RE_TWIN_DIAL, false)

                when (stage) {
                    0 ->
                        if (!startDialogue) {
                            npc(
                                FaceAnim.SAD,
                                "I'm sorry for abducting you like that, but I really need",
                                "your help ${player!!.username}.",
                            )
                            setAttribute(player!!, GameAttributes.RE_TWIN_OBJ_DIAL, true)
                            stage = 4
                        } else {
                            npc(FaceAnim.ASKING, "Wait! Do you know what you're doing here?")
                            stage++
                        }

                    1 -> options("Yes, I know.", "Erm, no I don't actually.").also { stage++ }
                    2 ->
                        when (buttonID) {
                            1 -> player("Yes, I know, I've been here before.").also { stage = 19 }
                            2 -> player("Erm, no I don't actually.").also { stage = 20 }
                        }

                    4 -> player(FaceAnim.NEUTRAL, "What's the problem then?").also { stage++ }
                    5 ->
                        npc(
                            FaceAnim.ANGRY,
                            "It's my evil twin sister! She's been galavanting around",
                            "${GameWorld.settings!!.name} committing crimes and now I'm getting the",
                            "blame!",
                        ).also {
                            stage++
                        }

                    6 -> player(FaceAnim.HALF_ASKING, "Well what's all this got to do with me then?").also { stage++ }
                    7 ->
                        npc(
                            "Through that door is a room with a cage and a control",
                            "panel that operates a giant mechanical claw.",
                        ).also {
                            stage++
                        }

                    8 ->
                        npc(
                            "I lured my sister into the room so I could imprison her",
                            "in the cage by using the claw. The problem is my sister",
                            "managed to herd some innocent civilians in there with",
                            "her.",
                        ).also {
                            stage++
                        }

                    9 -> player(FaceAnim.HALF_ASKING, "So what do you need me to do?").also { stage++ }
                    10 -> npc("I need you to go next door and use the claw to catch", "my sister.").also { stage++ }
                    11 ->
                        npc(
                            FaceAnim.ANGRY,
                            "Once she's in prison, she won't be causing me anymore",
                            "bother!",
                        ).also { stage++ }

                    12 -> player("Sounds easy enough to me.").also { stage++ }
                    13 ->
                        npc(
                            FaceAnim.HAPPY,
                            "Fabulous! Now take a good long look at me because the",
                            "door will be locked behind you. My twin looks exactly",
                            "like me, even her clothes!",
                        ).also {
                            stage++
                        }

                    14 ->
                        npc(
                            FaceAnim.HAPPY,
                            "One more thing to make your life difficult; the magic",
                            "powering the claw is running low so you'll only have",
                            "two attempts to catch her.",
                        ).also {
                            stage++
                        }

                    15 -> player("I'll do my best!").also { stage++ }
                    16 ->
                        npc(
                            "By the way, would you like me to run through the",
                            "controls for you, or do you think you'll manage?",
                        ).also {
                            stage++
                        }

                    17 -> options("Yes please.", "No thanks.").also { stage++ }
                    18 ->
                        when (buttonID) {
                            1 ->
                                player("Yes please. I mean, it's always best to be prepared,", "right?").also {
                                    stage = 20
                                }

                            2 -> player("No thanks, I should be able to work it out.").also { stage++ }
                        }

                    19 -> npc(FaceAnim.HAPPY, "Good luck!").also { stage = END_DIALOGUE }
                    20 ->
                        npc(
                            "Ok, when you turn the machine on you'll see the",
                            "glowing mark on the floor where the claw is currently",
                            "aiming, and you'll see a lever and button on the right",
                            "hand side of your screen.",
                        ).also {
                            stage++
                        }

                    21 ->
                        npc(
                            "To move the claw's current location, click on the",
                            "direction you want it to move in, as indicated on the",
                            "right hand panel.",
                        ).also {
                            stage++
                        }

                    22 ->
                        npc(
                            "You'll know which way the claw will go as the lever will",
                            "point that way to show you.",
                        ).also { stage++ }

                    23 ->
                        npc(
                            "When you see someone on top of the glowing mark,",
                            "then hit the button on the right hand panel, as this will",
                            "send the claw down to pick them up.",
                        ).also {
                            stage++
                        }

                    24 ->
                        npc(
                            "Do be careful and make sure that there is someone on",
                            "the mark, and not just walking past it.",
                        ).also {
                            stage++
                        }

                    25 ->
                        npc(
                            "Oh, and make sure the person is my sister, so you",
                            "don't end up catching a random civilian.",
                        ).also {
                            stage++
                        }

                    26 -> npc("Does that help?").also { stage++ }
                    27 -> player("Yes, that covers everything. Thanks!").also { stage = 19 }
                }
            }
        }
    }
}
