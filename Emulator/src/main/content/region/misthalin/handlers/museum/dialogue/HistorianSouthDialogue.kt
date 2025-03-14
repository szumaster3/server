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
class HistorianSouthDialogue(
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
                    "Tell me about natural history.",
                    "Tell me about terrorbirds.",
                    "Tell me about the kalphite queen.",
                    "That's enough education for one day.",
                ).also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_ASKING, "Tell me about natural history.").also { stage++ }
                    2 -> player(FaceAnim.HALF_ASKING, "Tell me about terrorbirds.").also { stage = 7 }
                    3 -> player(FaceAnim.HALF_ASKING, "Tell me about the kalphite queen.").also { stage = 10 }
                    4 -> player(FaceAnim.HALF_ASKING, "That's enough education for one day.").also { stage = 13 }
                }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, the field of natural history covers a wide range of sciences.",
                ).also { stage++ }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "So we use biology, the study of living things, botany, the study of plants and zoology, the study of animals.",
                ).also {
                    stage++
                }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Though the field is growing all the time and we're also using techniques from magic, astrology and numerology.",
                ).also {
                    stage++
                }
            6 ->
                npcl(FaceAnim.HALF_GUILTY, "A person interested in natural history is known as a naturalist.").also {
                    stage =
                        END_DIALOGUE
                }
            7 -> npcl(FaceAnim.HALF_GUILTY, "Ahh terrorbirds, the fastest bird on two legs.").also { stage++ }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "If you just follow me to the display case I shall explain all about them.",
                ).also {
                    stage++
                }
            9 -> {
                end()
                lock(player, 100000)
                lockInteractions(player, 10000)
                teleport(player, location(1756, 4940, 0))
                runTask(player, 1) {
                    face(player, location(1753, 4939, 0))
                    PlayerCamera(player).panTo(1757, 4943, 400, 300)
                    PlayerCamera(player).rotateTo(1749, 4936, 400, 300)
                    openDialogue(player, TalkAboutTerrorBirds())
                }
            }

            10 -> npcl(FaceAnim.HALF_GUILTY, "Ahh kalphites, the insectoid eating machines.").also { stage++ }
            11 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "If you just follow me to the display case I shall explain all about them.",
                ).also {
                    stage++
                }

            12 -> {
                end()
                lock(player, 100000)
                lockInteractions(player, 10000)
                teleport(player, location(1761, 4938, 0))
                runTask(player, 1) {
                    face(player, location(1766, 4939, 0))
                    PlayerCamera(player).panTo(1760, 4944, 400, 300)
                    PlayerCamera(player).rotateTo(1767, 4937, 400, 300)
                    openDialogue(player, TalkAboutKalphiteQueen())
                }
            }

            13 ->
                npc("Nonsense! There's always room for more.", " And remember, science isn't dull!").also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.NATURAL_HISTORIAN_5968)
    }
}

class TalkAboutTerrorBirds : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.NATURAL_HISTORIAN_5968)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Terrorbirds live in nomadic groups of between five and fifty birds that often travel together with other grazing animals.",
                ).also {
                    stage++
                }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "They mainly feed on seeds and other plants. They also eat insects such as locusts if they can catch them.",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "They have no teeth to chew with, so they swallow pebbles that help to grind the swallowed foods in the gizzard.",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "They can go without water for a long time, exclusively living off the water in the plants. However, they enjoy water and frequently take baths when they can.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Terrorbirds are known to eat almost anything, particularly in captivity, where opportunity is increased.",
                ).also {
                    stage++
                }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Terrorbirds usually weigh a little less than a small unicorn. The feathers of adult males are mostly green, with some white on the wings and tail.",
                ).also {
                    stage++
                }
            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "There are claws on two of the wings' fingers and their strong legs have no feathers. The bird stands on two toes, with the bigger one resembling a hoof. Its feet have no claws.",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "This is an adaptation unique to terrorbirds that appears to aid in running. Their legs are powerful enough to kill even large animals.",
                ).also {
                    stage++
                }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The gnomes in particular, prize the terrorbird for its fast running speed, using them as mounts whenever possible.",
                ).also {
                    stage++
                }
            9 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "There are a number of recorded incidents of people being attacked and killed.",
                ).also {
                    stage++
                }
            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Big males can be very territorial and aggressive, and can attack and kick very powerfully with their legs.",
                ).also {
                    stage++
                }
            11 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "A terrorbird is so fast, it can easily outrun any human athlete.",
                ).also { stage++ }
            12 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "And this concludes my short lecture on terrorbirds. I hope you've enjoyed yourselves.",
                ).also {
                    stage++
                }
            13 -> {
                end()
                unlock(player!!)
                PlayerCamera(player).reset()
            }
        }
    }
}

class TalkAboutKalphiteQueen : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.NATURAL_HISTORIAN_5968)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The kalphites, otherwise known as the kalphiscarabeinae, are perhaps the largest species of insect on the face of ${GameWorld.settings!!.name}.",
                ).also {
                    stage++
                }
            1 -> npcl(FaceAnim.HALF_GUILTY, "Their queen is called kalphiscarabeinae pasha.").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Most of the early documentation and research on this fearsome predatory species was performed by the noted bug hunter Iqbar Ali-Abdula.",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "This, of course, was before he was driven insane by his research and ran off into the desert, screaming.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Kalphites are related to beetles and scorpions; they are mainly green in colour. Some have remarkable antennae which can detect the slightest movement.",
                ).also {
                    stage++
                }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Their carapace is composed of armoured plates called lamellae.",
                ).also { stage++ }
            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "This shell can be compressed into a ball or fanned out like leaves, in order to sense odours.",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The front legs are adapted for digging the enormous tunnel systems that serve as their nests.",
                ).also {
                    stage++
                }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "They exist in a caste-based society, with the soft shelled larvae at the bottom, up through the workers, soldiers and finally the queen.",
                ).also {
                    stage++
                }
            9 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Voracious carnivores, a pack of adult workers can strip the flesh from a full grown camel in a matter of seconds, leaving nothing but a",
                ).also {
                    stage++
                }
            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "few bones and strips of fur for other scavengers to pick over.",
                ).also { stage++ }
            11 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "They typically live in large nests marked by the rock hard pillars found in hot, arid deserts, such as the one south-west of Al Kharid,",
                ).also {
                    stage++
                }
            12 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "which rise out of the sands like the tombs of desert pharaohs.",
                ).also { stage++ }
            13 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Indeed, there is some relationship between the Kalphite Queen and the desert god Scabaras, but no one is really sure what.",
                ).also {
                    stage++
                }
            14 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "During the early part of the fourth age, Scabaras proclaimed himself omnipotent and outlawed worship of all other gods save him.",
                ).also {
                    stage++
                }
            15 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "When the people eventually revolted against his repressive rule and banished Scabaras, it is said his blood washed over the scarabs",
                ).also {
                    stage++
                }
            16 -> npcl(FaceAnim.HALF_GUILTY, "and transformed them into the kalphites we know today.").also { stage++ }
            17 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Of course, any right-minded scientist discounts these myths as mere stories, with no historical basis in fact.",
                ).also {
                    stage++
                }
            18 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "And this concludes my short lecture on kalphites. I hope you've enjoyed yourselves.",
                ).also {
                    stage++
                }
            19 -> {
                end()
                unlock(player!!)
                PlayerCamera(player).reset()
            }
        }
    }
}
