package content.region.misthalin.handlers.museum.dialogue

import core.api.*
import core.api.utils.PlayerCamera
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HistorianWestDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.ASKING,
            "Hello again, " + if (player.isMale) "sir" else "madam" + ", how can I help you on this fine day?",
        )
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.ASKING, "I was hoping you could tell me about something.").also { stage++ }
            1 ->
                options(
                    "Tell me about camels",
                    "Tell me about leeches.",
                    "Tell me about moles.",
                    "Tell me about penguins.",
                    "That's enough education for one day.",
                ).also { stage++ }

            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_ASKING, "Tell me about camels").also { stage++ }
                    2 -> player(FaceAnim.HALF_ASKING, "Tell me about leeches.").also { stage = 6 }
                    3 -> player(FaceAnim.HALF_ASKING, "Tell me about moles.").also { stage = 9 }
                    4 -> player(FaceAnim.HALF_ASKING, "Tell me about penguins.").also { stage = 12 }
                    5 -> player(FaceAnim.HALF_ASKING, "That's enough education for one day.").also { stage = 15 }
                }

            3 -> npcl(FaceAnim.HALF_GUILTY, "Ahh camels, the ships of the desert.").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "If you just follow me to the display case I shall explain all about them.",
                ).also {
                    stage++
                }

            5 -> {
                end()
                lock(player, 100000)
                lockInteractions(player, 10000)
                teleport(player, location(1737, 4962, 0))
                runTask(player, 1) {
                    face(player, location(1736, 4965, 0))
                    PlayerCamera(player).panTo(1740, 4961, 400, 300)
                    PlayerCamera(player).rotateTo(1733, 4969, 400, 300)
                    openDialogue(player, TalkAboutCamels())
                }
            }

            6 -> npcl(FaceAnim.HALF_GUILTY, "Ahh leeches, the haemophagic parasites.").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "If you just follow me to the display case I shall explain all about them.",
                ).also {
                    stage++
                }

            8 -> {
                end()
                lock(player, 100000)
                lockInteractions(player, 10000)
                teleport(player, location(1744, 4962, 0))
                runTask(player, 1) {
                    face(player, location(1743, 4965, 0))
                    PlayerCamera(player).panTo(1747, 4961, 400, 300)
                    PlayerCamera(player).rotateTo(1740, 4969, 400, 300)
                    openDialogue(player, TalkAboutLeeches())
                }
            }

            9 -> npcl(FaceAnim.HALF_GUILTY, "Ahh moles, the mammalian mountain makers.").also { stage++ }
            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "If you just follow me to the display case I shall explain all about them.",
                ).also {
                    stage++
                }

            11 -> {
                end()
                lock(player, 100000)
                lockInteractions(player, 10000)
                teleport(player, location(1735, 4958, 0))
                runTask(player, 1) {
                    face(player, location(1736, 4955, 0))
                    PlayerCamera(player).panTo(1732, 4959, 400, 300)
                    PlayerCamera(player).rotateTo(1739, 4951, 400, 300)
                    openDialogue(player, TalkAboutMoles())
                }
            }

            12 -> npcl(FaceAnim.HALF_GUILTY, "Ahh penguins, the cunning birds of the sea.").also { stage++ }
            13 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "If you just follow me to the display case I shall explain all about them.",
                ).also {
                    stage++
                }

            14 -> {
                end()
                lock(player, 100000)
                lockInteractions(player, 10000)
                teleport(player, location(1742, 4958, 0))
                runTask(player, 1) {
                    face(player, location(1736, 4955, 0))
                    PlayerCamera(player).panTo(1747, 4959, 400, 300)
                    PlayerCamera(player).rotateTo(1740, 4951, 400, 300)
                    openDialogue(player, TalkAboutPenguins())
                }
            }

            15 ->
                npc("Nonsense! There's always room for more.", " And remember, science isn't dull!").also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.NATURAL_HISTORIAN_5969)
    }
}

class TalkAboutCamels : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.NATURAL_HISTORIAN_5969)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The term camel refers to either of the two known species of camelid.",
                ).also { stage++ }

            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "There is the bactrian, which has two humps and can be found throughout Al Kharid. It also refers to the Ugthanki, or one-humped camel.",
                ).also { stage++ }

            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Camels are ill-tempered beasts at the best of times, requiring a great deal of time and effort to tame.",
                ).also { stage++ }

            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "This would explain why used camel salesmen are in such a hurry to be rid of them.",
                ).also { stage++ }

            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "In the wild, camels have been known to lie in wait and ambush hapless travellers, before devouring them.",
                ).also { stage++ }

            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "This is quite surprising, as most domestic camels are happy eating nothing but vegetables.",
                ).also { stage++ }

            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Camel milk is much more nutritious than cow milk and goes well in the strong desert drink akin to tea.",
                ).also { stage++ }

            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Another useful camel by-product is dung. Their dung is very dry, due to the highly efficient metabolism of the camel.",
                ).also { stage++ }

            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Scientific research has also shown that chilli has a disastrous effect on a camel's digestive system, which produces toxic dung.",
                ).also { stage++ }

            9 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "And this concludes my short lecture on camels. I hope you've enjoyed yourselves.",
                ).also { stage++ }

            10 -> {
                end()
                unlock(player!!)
                PlayerCamera(player).reset()
            }
        }
    }
}

class TalkAboutLeeches : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.NATURAL_HISTORIAN_5969)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Leeches are fascinating creatures and are very similar to worms in most respects.",
                ).also { stage++ }

            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "They like to inhabit streams, rivers and seas, but their preference is for stagnant pools of water.",
                ).also { stage++ }

            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "One of the most common misconceptions is that all leeches feed on blood.",
                ).also { stage++ }

            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "In fact, very, very few leeches are parasitic bloodsucking animals.",
                ).also { stage++ }

            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Most leeches are meat eaters, feeding on a variety of invertebrates such as worms, snails, insect larvae and snails.",
                ).also { stage++ }

            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Those that do feed on blood have developed an amazing method of doing so.",
                ).also { stage++ }

            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Firstly they latch onto the skin using a ring of tiny teeth, before injecting their prey with an anaesthetic.",
                ).also { stage++ }

            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Then they bite into the skin using a Y-shaped mouthpiece and introducing a chemical that stops the blood from clotting.",
                ).also { stage++ }

            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "They will then feed until they are completely full, sometimes doubling in size!",
                ).also { stage++ }

            9 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Most leeches are very small, measuring no more than the length of your middle finger.",
                ).also { stage++ }

            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "An exception to these are the leeches of Morytania, which can reach the size of a dog.",
                ).also { stage++ }

            11 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "They are much more mobile than their smaller cousins and are able to jump rather high when attacking.",
                ).also { stage++ }

            12 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Quite how these leeches came to be so big is something of a mystery.",
                ).also { stage++ }

            13 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "All we can assume is that there is some kind of environmental influence, which has governed their immense growth.",
                ).also { stage++ }

            14 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "And this concludes my short lecture on leeches. I hope you've enjoyed yourselves.",
                ).also { stage++ }

            15 -> {
                end()
                unlock(player!!)
                PlayerCamera(player).reset()
            }
        }
    }
}

class TalkAboutMoles : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.NATURAL_HISTORIAN_5969)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Now, moles are small mammals of the talpidae family. These subterranean burrowers mainly live on a diet of slugs, snails and insects.",
                ).also { stage++ }

            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "They vary greatly in habitat and can be found in almost every part of ${GameWorld.settings!!.name}. Some species have even known to be aquatic.",
                ).also { stage++ }

            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Male moles are known as boars with the females called sows. Should you come across a group of moles, you would call them a labour.",
                ).also { stage++ }

            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Moles are considered to be an agricultural pest in most places, digging up the ground and leaving molehills all over the place.",
                ).also { stage++ }

            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "This has been highlighted in Falador by Wyson the Gardener who, after using some Malignus Mortifer's Super Ultra Flora Growth",
                ).also { stage++ }

            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Potion, managed to create Gielinor's only known species of giant mole.",
                ).also { stage++ }

            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "This fearsome beast has huge claws, wicked teeth and a penchant for shiny objects.",
                ).also { stage++ }

            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "It is a very tough animal with a thick protective hide and an ill temperament.",
                ).also { stage++ }

            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "That said, they do benefit the soil by aerating and tilling it, adding to its fertility. Contrary to popular belief, moles don't eat plant roots.",
                ).also { stage++ }

            9 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on moles.").also { stage++ }
            10 -> npcl(FaceAnim.HALF_GUILTY, "I hope you've enjoyed yourselves.").also { stage++ }
            11 -> {
                end()
                unlock(player!!)
                PlayerCamera(player).reset()
            }
        }
    }
}

class TalkAboutPenguins : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.NATURAL_HISTORIAN_5969)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "This often-maligned aquatic bird is known to be native to the ice fields of Etceteria, although they have been known to live as far as the fields of Lumbridge.",
                ).also { stage++ }

            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "These aquatic birds seem to spend the majority of their time eating and fostering their young.",
                ).also { stage++ }

            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Unlike most animals that prefer cold climes, these sphenisciformes work very well together in large groups, by watching for predators and caring for each other's young.",
                ).also { stage++ }

            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "For creatures with such small brains, they do seem to have a disproportionate capacity for forward thinking and planning.",
                ).also { stage++ }

            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "As this serves no natural purpose, scholars are divided as to how this evolved.",
                ).also { stage++ }

            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Their diet consists mainly of fish, squid and a small shrimp-like creature called krill.",
                ).also { stage++ }

            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "However, some have developed a taste for the mushrooms that grow around fairy rings.",
                ).also { stage++ }

            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Penguins primarily rely on their vision while hunting. What we don't know is how penguins locate prey in the darkness, or at great depths.",
                ).also { stage++ }

            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Some theories suggest that penguins are helped by some sort of extra sensory perception; perhaps even precognition.",
                ).also { stage++ }

            9 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Penguins spend a long time going without food when they are breeding. In fact, they won't even leave their nests if they can help it.",
                ).also { stage++ }

            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Fortunately, most penguins build up a layer of fat to keep them warm and provide energy until the moult is over.",
                ).also { stage++ }

            11 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "And this concludes my short lecture on penguins. I hope you've enjoyed yourselves.",
                ).also { stage++ }

            12 -> {
                end()
                unlock(player!!)
                PlayerCamera(player).reset()
            }
        }
    }
}
