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
class HistorianNorthDialogue(
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
                    "Tell me about lizards.",
                    "Tell me about tortoises.",
                    "Tell me about dragons.",
                    "Tell me about wyverns.",
                    "That's enough education for one day.",
                ).also { stage++ }

            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_ASKING, "Tell me about lizards.").also { stage++ }
                    2 -> player(FaceAnim.HALF_ASKING, "Tell me about tortoises.").also { stage = 6 }
                    3 -> player(FaceAnim.HALF_ASKING, "Tell me about dragons.").also { stage = 9 }
                    4 -> player(FaceAnim.HALF_ASKING, "Tell me about wyverns.").also { stage = 12 }
                    5 -> player(FaceAnim.HALF_ASKING, "That's enough education for one day.").also { stage = 15 }
                }

            3 -> npcl(FaceAnim.HALF_GUILTY, "Ahh lizards, the scaly carnivores.").also { stage++ }
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
                teleport(player, location(1743, 4977, 0))
                runTask(player, 1) {
                    face(player, location(1742, 4980, 0))
                    PlayerCamera(player).panTo(1746, 4976, 400, 300)
                    PlayerCamera(player).rotateTo(1738, 4985, 400, 300)
                    openDialogue(player, TalkAboutLizards())
                }
            }

            6 -> npcl(FaceAnim.HALF_GUILTY, "Ahh tortoises, the armoured ancients.").also { stage++ }
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
                teleport(player, location(1753, 4977, 0))
                runTask(player, 1) {
                    face(player, location(1752, 4981, 0))
                    PlayerCamera(player).panTo(1757, 4976, 400, 300)
                    PlayerCamera(player).rotateTo(1750, 4983, 400, 300)
                    openDialogue(player, TalkAboutTortoises())
                }
            }

            9 -> npcl(FaceAnim.HALF_GUILTY, "Ahh dragons, the mighty hunters of the sky.").also { stage++ }
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
                teleport(player, location(1768, 4977, 0))
                runTask(player, 1) {
                    face(player, location(1767, 4981, 0))
                    PlayerCamera(player).panTo(1772, 4976, 400, 300)
                    PlayerCamera(player).rotateTo(1765, 4983, 400, 300)
                    openDialogue(player, TalkAboutDragons())
                }
            }

            12 -> npcl(FaceAnim.HALF_GUILTY, "Ahh, wyverns. The extinct lizards.").also { stage++ }
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
                teleport(player, location(1778, 4977, 0))
                runTask(player, 1) {
                    face(player, location(1776, 4980, 0))
                    PlayerCamera(player).panTo(1781, 4976, 400, 300)
                    PlayerCamera(player).rotateTo(1775, 4982, 400, 300)
                    openDialogue(player, TalkAboutWyverns())
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
        return intArrayOf(NPCs.NATURAL_HISTORIAN_5966)
    }
}

class TalkAboutLizards : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.NATURAL_HISTORIAN_5966)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Contrary to popular belief, even though most lizards have a yellow belly, they are not in fact cowardly. Most Gielinorian lizards will not shy away from a good fight and can be very tough.",
                ).also {
                    stage++
                }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "This is due to a very thick and tough hide, made from thousands of thick scales. Most people who claim to be hunters have a very hard time trying to dispatch lizards.",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "In fact, the only people to successfully discern how to kill these tough little squamatas are the five legendary Slayer masters, although we assume they must have some kind of natural predator.",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Interestingly enough, these scales are made from the same substance that your hair is comprised of. This substance is called keratin.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Lizards have a very well developed sense of vision and hearing. Some people think that some lizards have a third eye!",
                ).also {
                    stage++
                }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "A tiny, light-sensitive, transparent structure on top of the head that helps them regulate how long they stay in the sun.",
                ).also {
                    stage++
                }
            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "This is vital for the cold-blooded lizards who have no means to regulate their internal temperature.",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Like many cold-blooded creatures, if they are subjected to a sudden decrease in temperature, they will become sluggish and sleepy.",
                ).also {
                    stage++
                }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "And this concludes my short lecture on lizards. I hope you've enjoyed yourselves.",
                ).also {
                    stage++
                }
            9 -> {
                end()
                unlock(player!!)
                PlayerCamera(player).reset()
            }
        }
    }
}

class TalkAboutTortoises : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.NATURAL_HISTORIAN_5966)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The tortoise is a very well defended beast. It uses an armoured shell just like its aquatic cousins the terrapin and the turtle.",
                ).also {
                    stage++
                }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Tortoises can vary in size from about as long as your hand to as big as a unicorn. Most land tortoises eat nothing but plants in the wild.",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Did you know you can tell how old a tortoise is by the number of rings in its shell, just like a tree.",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Most land-based tortoises eat plants, feeding on grazing grasses, weeds, leafy greens, flowers, and cabbages.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Tortoises generally live as long as people, and some individual ones are known to have lived longer than 300 years.",
                ).also {
                    stage++
                }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Because of this, they symbolise longevity within some cultures, such as gnomes who also breed them for battle.",
                ).also {
                    stage++
                }
            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The oldest tortoise ever recorded was Mibbiwocket, who was presented to the King Healthorg the Great, by the famous explorer Admiral Bake, shortly after its birth.",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Mibbiwocket is still in the care of the gnomish royal family.",
                ).also { stage++ }
            8 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on tortoises.").also { stage++ }
            9 -> npcl(FaceAnim.HALF_GUILTY, "I hope you've enjoyed yourselves.").also { stage++ }
            10 -> {
                end()
                unlock(player!!)
                PlayerCamera(player).reset()
            }
        }
    }
}

class TalkAboutDragons : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.NATURAL_HISTORIAN_5966)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The dragons of Gielinor are a most confusing species. Standing at approximately twelve feet tall,",
                ).also {
                    stage++
                }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "these imposing predators strike fear into the hearts and minds of most sane folk.",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "However, if you delve a little deeper into their history and lifestyle, a few things stick out as very unusual.",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "With most species there is a line, an ancestry if you will, whereby you can track how a creature has come to be in its present form.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "For instance, you can trace the ancestry of the common house cat back to the same creature that became the sabre-toothed kyatt.",
                ).also {
                    stage++
                }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "However, with the dragon, no such root ancestor can be found.",
                ).also { stage++ }
            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "There are many forms of dragon, such as the common coloured and the metallic, or ferrous, dragon.",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "They colonise many areas of ${GameWorld.settings!!.name}, though most notably, sites of ancient battles and small dank caves.",
                ).also {
                    stage++
                }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Eating habits tend to vary, with the majority of their food being meat. However, it has also been noted that they can consume",
                ).also {
                    stage++
                }
            9 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "metals just as easily, with runite being thought of as a delicacy.",
                ).also { stage++ }
            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Throughout history, dragons have appeared in myth and legend as fearsome adversaries and cunning creatures.",
                ).also {
                    stage++
                }
            11 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "However, modern evidence does not support this. Most young dragons are largely creatures of instinct with a strong vicious streak.",
                ).also {
                    stage++
                }
            12 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The lifespan of the common dragon is as yet unknown, as no dragon has ever been observed dying of old age.",
                ).also {
                    stage++
                }
            13 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Although, it has been mooted that spontaneous combustion could be considered a natural cause of death for this species.",
                ).also {
                    stage++
                }
            14 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "And this concludes my short lecture on dragons. I hope you've enjoyed yourselves.",
                ).also {
                    stage++
                }
            15 -> {
                end()
                unlock(player!!)
                PlayerCamera(player).reset()
            }
        }
    }
}

class TalkAboutWyverns : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.NATURAL_HISTORIAN_5966)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The wyverns' tale is a sad one. This now extinct species is presumed in some way to be related to the dragon,",
                ).also {
                    stage++
                }
            1 -> npcl(FaceAnim.HALF_GUILTY, "in that they are both large flying reptiles.").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The wyvern stands much shorter than the adult dragon and only has two legs as opposed to the dragon's four.",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Much of the evidence for wyvern behaviour comes from the reconstruction of old bones in the icy mountains of Asgarnia.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "As most lizards cannot maintain their own body temperature, two theories as to how they managed to survive have been proposed.",
                ).also {
                    stage++
                }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "One is that Asgarnia was at one time a much more temperate climate than it is now.",
                ).also {
                    stage++
                }
            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The other is that wyverns could generate fire internally in much the same way as dragons.",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "If they follow the dragon paradigm, then they would have been carnivores, feeding on cows, sheep and other livestock animals.",
                ).also {
                    stage++
                }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "How and why the wyverns became extinct is something of a mystery.",
                ).also { stage++ }
            9 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Though if you consider the theory that the climate of Asgarnia changed suddenly, then this could provide an explanation.",
                ).also {
                    stage++
                }
            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "There are some inconsistencies in the findings we have for the wyverns, such as the odd wear patterns of some of bones,",
                ).also {
                    stage++
                }
            11 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "which really could only have happened after the creature died.",
                ).also { stage++ }
            12 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Also the bones we have collected remain a little below room temperature wherever they are kept.",
                ).also {
                    stage++
                }
            13 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "They have also been shown to radiate a very weak magical aura.",
                ).also { stage++ }
            14 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I'm sure that in due time, these mysteries will be solved.",
                ).also { stage++ }
            15 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "And this concludes my short lecture on wyverns. I hope you've enjoyed yourselves.",
                ).also {
                    stage++
                }
            16 -> {
                end()
                unlock(player!!)
                PlayerCamera(player).reset()
            }
        }
    }
}
