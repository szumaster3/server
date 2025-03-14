package content.region.asgarnia.dialogue.portsarim

import core.api.removeItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class KlarenseDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER)
        if (args.size > 1) {
            npc(FaceAnim.ANGRY, "Hey, stay off my ship! That's private property!").also { stage = END_DIALOGUE }
            return true
        }
        when (quest!!.getStage(player)) {
            100 -> {
                player("Hey, that's my ship!")
                stage = -1
            }

            else -> {
                if (player.getSavedData().questData.getDragonSlayerAttribute("ship")) {
                    npc(
                        FaceAnim.NEUTRAL,
                        "Hello, captain! Here to inspect your new ship? Just a",
                        "little work and she'll be seaworthy again.",
                    )
                    stage = 400
                    return true
                }
                npc(
                    FaceAnim.NEUTRAL,
                    "So, are you interested in buying the Lady Lumbridge?",
                    "Now, I'll be straight with you: she's not quite seaworthy",
                    "right now, but give her a bit of work and she'll be the",
                    "nippiest ship this side of Port Khazard.",
                )
                stage = 1
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (stage == 400) {
            end()
            return true
        }
        when (quest!!.getStage(player)) {
            100 ->
                when (stage) {
                    0 -> end()
                    -1 ->
                        npc(
                            FaceAnim.SUSPICIOUS,
                            "No, it's not. It may, to the untrained eye, at first",
                            "appear to be the Lady Lumbridge, but it is definitely",
                            "not. It's my new ship. It just happens to look slightly",
                            "similar, is all.",
                        ).also { stage = 1 }

                    1 ->
                        player(
                            FaceAnim.ANNOYED,
                            "It has Lady Lumbridge pained out and 'Klarense's",
                            "Cruiser' painted over it!",
                        ).also { stage = 2 }

                    2 -> npc(FaceAnim.SUSPICIOUS, "Nope, you're mistaken. It's my new boat.").also { stage = 3 }
                    3 -> end()
                }

            20 ->
                when (stage) {
                    0 -> end()
                    1 -> player(FaceAnim.ASKING, "Would you take me to Crandor when she's ready?").also { stage++ }
                    2 -> npc(FaceAnim.EXTREMELY_SHOCKED, "Crandor? You're joking, right?").also { stage++ }
                    3 -> player(FaceAnim.NEUTRAL, "No. I want to go to Crandor.").also { stage++ }
                    4 -> npc(FaceAnim.WORRIED, "Then you must be crazy.").also { stage++ }
                    5 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "That island is surrounded by reefs that would rip this",
                            "ship to shreds. Even if you found a map, you'd need",
                            "an experienced captain to stand a chance of getting",
                            "through",
                        ).also { stage++ }

                    6 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "even if you gould get to it, there's no way I'm going",
                            "any closer to that dragon than I have to. They say it",
                            "can destroy whole ships with one bite.",
                        ).also { stage++ }

                    7 -> player(FaceAnim.NEUTRAL, "I'd like to buy her.").also { stage++ }
                    8 ->
                        npc(
                            FaceAnim.HAPPY,
                            "Of course! I'm sure the work needed to do on it",
                            "wouldn't be too expensive.",
                        ).also { stage++ }

                    9 ->
                        npc(
                            FaceAnim.HAPPY,
                            "How does, 2,000 gold sound? I'll even throw in my",
                            "cabin boy, Jenkins, for free! He'll swab the decks and",
                            "splice the mainsails for you!",
                        ).also { stage++ }

                    10 -> player(FaceAnim.HAPPY, "Yep, sounds good.").also { stage++ }
                    11 -> {
                        end()
                        if (!removeItem(player, Item(995, 2000))) {
                            player(FaceAnim.HALF_GUILTY, "...except I don't have that kind of money on me...")
                        } else {
                            npc(FaceAnim.HAPPY, "Okey dokey, she's all yours!")
                            player.getSavedData().questData.setDragonSlayerAttribute("ship", true)
                        }
                    }

                    else ->
                        when (stage) {
                            0 -> end()
                            1 ->
                                options(
                                    "Do you know when she will be seaworthy?",
                                    "Why is she damaged?",
                                    "Ah, well, never mind.",
                                ).also { stage++ }

                            2 ->
                                when (buttonId) {
                                    1 ->
                                        player(FaceAnim.ASKING, "Do you know when she will be seaworthy?").also {
                                            stage =
                                                10
                                        }
                                    2 -> player(FaceAnim.ASKING, "Why is she damaged?").also { stage = 20 }
                                    3 ->
                                        player(FaceAnim.HALF_GUILTY, "Ah, well, never mind.").also {
                                            stage =
                                                END_DIALOGUE
                                        }
                                }

                            10 ->
                                npc(
                                    FaceAnim.HALF_GUILTY,
                                    "No, not really. Port Sarim's shipbuilders aren't very",
                                    "efficient so it could be quite a while.",
                                ).also { stage = END_DIALOGUE }

                            20 ->
                                npc(
                                    FaceAnim.HALF_GUILTY,
                                    "Oh, there was no particular accident. It's just years of",
                                    "wear and tear.",
                                ).also { stage++ }

                            21 ->
                                npc(
                                    FaceAnim.NEUTRAL,
                                    "The Lady Lumbridge is an old Crandorian fishing ship -",
                                    "the last one of her kind, as far as I know. That kind of",
                                    "ship was always mightily manoeuvrable, but not too",
                                    "tough.",
                                ).also { stage++ }

                            22 ->
                                npc(
                                    FaceAnim.NEUTRAL,
                                    "She happened to be somewhere else when Crandor was",
                                    "destroyed, and she's had several owners since then. Not",
                                    "all of them have looked after her too well,",
                                ).also { stage++ }

                            23 ->
                                npc(FaceAnim.NEUTRAL, "but once she's patched up, she'll be good as new!").also {
                                    stage = END_DIALOGUE
                                }

                            400 -> end()
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KLARENSE_744)
    }
}
