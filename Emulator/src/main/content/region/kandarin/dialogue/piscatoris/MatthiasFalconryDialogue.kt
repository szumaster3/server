package content.region.kandarin.dialogue.piscatoris

import content.global.skill.hunter.falconry.FalconryActivityPlugin
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class MatthiasFalconryDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        val quickOption = args.size == 2

        when {
            inEquipment(player, Items.FALCONERS_GLOVE_10023, 1) -> {
                npcl(FaceAnim.FRIENDLY, "Oh, it looks like you've lost your falcon. Would you like a new one?")
                stage = 22
            }
            inEquipment(player, Items.FALCONERS_GLOVE_10024, 1) -> {
                player(FaceAnim.FRIENDLY, "Hello again.")
                stage = 25
            }
            quickOption -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "If you wish to try falconry, I'd have to request a small fee, mind you; how does 500 gold pieces sound?",
                )
                stage = 5
            }
            else -> {
                player(FaceAnim.FRIENDLY, "Hello there.")
                stage = 0
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "Greetings. Can I help you at all?").also { stage++ }
            1 ->
                showTopics(
                    Topic("Do you have any quests I could do?", 13, false),
                    Topic("What is this place?", 10, false),
                    Topic(FaceAnim.HALF_ASKING, "Could I have a go with your bird?", 2, false),
                )

            2 ->
                npc(
                    FaceAnim.HAPPY,
                    "Training falcons is a lot of work and I doubt you're up",
                    "to the task. However, I suppose I could let you try",
                    "hunting with one.",
                ).also {
                    stage++
                }

            3 ->
                npc(
                    FaceAnim.HAPPY,
                    "I have some tamer birds that I occasionally lend to rich",
                    "noblemen who consider it a sufficiently refined sport for",
                    "their tastes, and you look like the kind who might",
                    "appreciate a good hunt.",
                ).also {
                    stage++
                }

            4 ->
                npc(
                    FaceAnim.HAPPY,
                    "I'd have to request a small fee, mind you; how does",
                    "500 gold pieces sound?",
                ).also {
                    stage++
                }

            5 -> options("Ok, that seems reasonable.", "I'm not interested then, thanks.").also { stage++ }
            6 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "Ok, that seems reasonable.").also { stage += 2 }
                    2 -> player(FaceAnim.FRIENDLY, "I'm not interested then, thanks.").also { stage++ }
                }

            7 ->
                npcl(FaceAnim.FRIENDLY, "Well, you're welcome to come back if you change your mind.").also {
                    stage = END_DIALOGUE
                }

            8 -> {
                if (getStatLevel(player, Skills.HUNTER) < 43) {
                    npc(
                        "Try coming back when you're more experienced",
                        "I wouldn't want my birds being injured.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                    return true
                }

                if (!inInventory(player, Items.COINS_995, 500)) {
                    npc(FaceAnim.HALF_GUILTY, "Sorry, you don't have enough money.").also { stage = END_DIALOGUE }
                    return true
                }
                if (hasFalcon(player)) {
                    npcl(FaceAnim.NEUTRAL, "You already have a falcon!").also { stage = END_DIALOGUE }
                    return true
                }

                if (!hasHandsFree(player)) {
                    npcl(
                        FaceAnim.HALF_GUILTY,
                        "Sorry, you really need both hands free for falconry. I'd suggest that you put away your weapons and gloves before we start.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                    return true
                }

                player.inventory.remove(Item(Items.COINS_995, 500))
                player.equipment.add(Item(Items.FALCONERS_GLOVE_10024), true, false)
                interpreter.sendDialogue(
                    "The falconer gives you a large leather glove and brings one of the",
                    "smaller birds over to land on it.",
                )
                stage = 21
            }

            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "A good question; straight and to the point. My name is, Matthias, I am a falconer, and this is where I train my, birds.",
                ).also {
                    stage++
                }

            11 ->
                options(
                    "Do you have any quests I could do?",
                    "That sounds like fun; could I have a go?",
                    "That doesn't sound like my sort of thing.",
                    "What's this falconry thing all about then?",
                ).also {
                    stage++
                }

            12 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_ASKING, "Do you have any quests I could do?").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "That sounds like fun; could I have a go?").also { stage++ }
                    3 -> player(FaceAnim.FRIENDLY, "That doesn't sound like my sort of thing.").also { stage = 16 }
                    4 -> player(FaceAnim.HALF_ASKING, "What's this falconry thing all about then?").also { stage = 17 }
                }

            13 ->
                npc(
                    FaceAnim.THINKING,
                    "A quest? What a strange notion.",
                    "Do you normally go around asking",
                    "complete strangers for quests?",
                ).also {
                    stage++
                }

            14 -> player(FaceAnim.STRUGGLE, "Er, yes, now you come to mention it.").also { stage++ }
            15 -> npc(FaceAnim.HALF_GUILTY, "Oh, ok then. Well, no, I don't; sorry.").also { stage = 1 }
            16 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Fair enough; it does require a great deal of patience",
                    "and skill, so I can understand if you might feel",
                    "intimidated.",
                ).also {
                    stage =
                        END_DIALOGUE
                }

            17 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, some people see it as a sport, although such a term does not really convey the amount of patience and dedication required to be proficient at the task.",
                ).also {
                    stage++
                }

            18 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Putting it simply, it is the training and use of birds of prey in hunting quarry.",
                ).also {
                    stage++
                }

            19 -> player(FaceAnim.THINKING, "So it's like keeping a pet then?").also { stage++ }
            20 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Not exactly, no. Such a bird can never really be considered tame in the same way that a dog can. They can be trained to associate people or places with food though, and, as such, a good falconer can get a trained bird to do as he wishes.",
                ).also {
                    stage =
                        1
                }

            21 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Don't worry; I'll keep an eye on you to make sure you don't upset it too much.",
                ).also {
                    stage =
                        END_DIALOGUE
                }

            22 -> options("Yes, please.", "No thank you.").also { stage++ }
            23 ->
                when (buttonId) {
                    1 -> player("Yes, please.").also { stage++ }
                    2 -> player("I think I'll leave it for now.").also { stage = END_DIALOGUE }
                }

            24 -> {
                player.bank.remove(Item(Items.FALCONERS_GLOVE_10024), Item(Items.FALCONERS_GLOVE_10023))
                player.inventory.remove(Item(Items.FALCONERS_GLOVE_10023), Item(Items.FALCONERS_GLOVE_10024))
                player.equipment.add(Item(Items.FALCONERS_GLOVE_10024), true, false)
                sendDialogue(
                    "The falconer gives you a large leather glove and brings one of the",
                    "smaller birds over to land on it.",
                )
                stage = 21
            }

            25 -> npcl(FaceAnim.HAPPY, "Ah, you're back. How are you getting along with her then?").also { stage++ }
            26 -> player(FaceAnim.FRIENDLY, "It's certainly harder than it looks.").also { stage++ }
            27 ->
                npcl(
                    FaceAnim.STRUGGLE,
                    "Sorry, but I was talking to the falcon, not you. But yes it is. Have you had enough yet?",
                ).also {
                    stage++
                }

            28 ->
                options(
                    "Actually, I'd like to keep trying a little longer.",
                    "I think I'll leave it for now.",
                ).also { stage++ }

            29 ->
                when (buttonId) {
                    1 -> player("Actually, I'd like to keep trying a little longer.").also { stage++ }
                    2 -> player("I think I'll leave it for now.").also { stage = 31 }
                }

            30 ->
                npcl(FaceAnim.NOD_YES, "Ok then, just come talk to me when you're done.").also {
                    stage = END_DIALOGUE
                }

            31 -> {
                end()
                FalconryActivityPlugin.removeItems(player)
                sendMessage(player, "You give the falcon and glove back to Matthias.")
            }
        }
        return true
    }

    fun hasFalcon(player: Player): Boolean {
        val falconItem = Item(Items.FALCONERS_GLOVE_10024)
        return player.bank.containsItem(falconItem) ||
            player.equipment.containsItem(falconItem) ||
            player.inventory.containsItem(
                falconItem,
            )
    }

    override fun newInstance(player: Player): Dialogue {
        return MatthiasFalconryDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MATTHIAS_5093)
    }
}
