package content.region.misthalin.quest.demon.dialogue

import content.region.misthalin.quest.demon.handlers.DemonSlayerUtils
import core.api.sendMessage
import core.api.sendNPCDialogue
import core.game.activity.ActivityManager
import core.game.activity.CutscenePlugin
import core.game.component.Component
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket
import org.rs.consts.NPCs
import org.rs.consts.Quests

class GypsyArisDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null
    private var wally: NPC? = null
    private var cutscene: CutscenePlugin? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.DEMON_SLAYER)
        when (quest!!.getStage(player)) {
            100 -> npc("Greetings young one.").also { stage = 0 }
            30, 10, 20 -> {
                if (args.size > 1) {
                    npc("Delrith will come forth from the stone circle again.").also { stage = 200 }
                    return true
                }
                npc("Greetings. How goes the quest?").also {
                    stage =
                        if (quest!!.getStage(player) != 30) {
                            1
                        } else {
                            0
                        }
                }
            }

            0 -> {
                if (args.size > 1) {
                    cutscene = args[1] as CutscenePlugin
                    npc(
                        "Wally managed to arrive at the stone circle just as",
                        "Delrith was summoned by a cult of chaos druids...",
                    ).also { stage = 200 }
                    return true
                }
                npc("Hello young one.").also { stage = 1 }
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            100 ->
                when (stage) {
                    0 -> npc("You're a hero now. That was a good bit of", "demonslaying.").also { stage = 1 }
                    1 -> player("Thanks.").also { stage = 2 }
                    2 -> end()
                }

            30 ->
                when (stage) {
                    0 -> player("I have the sword now. I just need to kill the demon, I", "think.").also { stage = 1 }
                    1 -> npc("Yep, that's right.").also { stage = 2 }
                    2 ->
                        options(
                            "What is the magical incantation?",
                            "Well I'd better press on with it",
                            "Where can I find the demon?",
                        ).also {
                            stage =
                                3
                        }
                    3 ->
                        when (buttonId) {
                            1 -> player("What is the magical incantation?").also { stage = 10 }
                            2 -> player("Well I'd better press on with it.").also { stage = 20 }
                            3 -> player("Where can I find the demon?").also { stage = 30 }
                        }

                    10 -> npc(DemonSlayerUtils.getIncantation(player) + ".").also { stage = 11 }
                    11 -> player("Well I'd better press on with it.").also { stage = 20 }
                    20 -> npc("See you anon.").also { stage = 21 }
                    21 -> end()
                    30 ->
                        npc(
                            "Just head south and you should find the stone circle",
                            "just outside the city gate.",
                        ).also {
                            stage =
                                31
                        }
                    31 -> end()
                }

            20 ->
                when (stage) {
                    0 ->
                        player(
                            "I found Sir Prysin. Unfortunately I haven't got the",
                            "sword yet. He's made it complicated for me.",
                        ).also {
                            stage =
                                1
                        }
                    1 -> npc("Ok, hurry, we haven't much time.").also { stage = 2 }
                    2 ->
                        options("What is the magical incantation?", "Well I'd better press on with it.").also {
                            stage =
                                3
                        }
                    3 ->
                        when (buttonId) {
                            1 -> player("What is the magical incantation?").also { stage = 10 }
                            2 -> player("Well I'd better press on with it.").also { stage = 20 }
                        }

                    10 -> npc(DemonSlayerUtils.getIncantation(player) + ".").also { stage = 11 }
                    11 -> player("Well I'd better press on with it.").also { stage = 20 }
                    20 -> player("See you anon.").also { stage = 21 }
                    21 -> end()
                }

            10 ->
                when (stage) {
                    1 -> player("I'm still working on it.").also { stage = 2 }
                    2 -> npc("Well if you need any advice I'm always here, young", "one.").also { stage = 3 }
                    3 ->
                        options(
                            "What is the magical incantation?",
                            "Where can I find Silverlight?",
                            "Stop calling me that!",
                            "Well I'd better press on with it.",
                        ).also {
                            stage =
                                4
                        }
                    4 ->
                        when (buttonId) {
                            1 -> player("What is the magical incantation?").also { stage = 10 }
                            2 -> player("Where can I find Silverlight?").also { stage = 20 }
                            3 -> player("Stop calling me that!").also { stage = 30 }
                            4 -> player("Well I'd better press on with it.").also { stage = 40 }
                        }
                    10 -> npc("Oh yes, let me think a second...").also { stage = 11 }
                    11 -> npc("Alright, I think I've got it now, it goes...").also { stage = 12 }
                    12 -> npc(DemonSlayerUtils.getIncantation(player) + ".", "Have you got that?").also { stage = 13 }
                    13 -> player("I think so, yes.").also { stage = 14 }
                    14 -> end()
                    20 ->
                        npc(
                            "Silverlight has been passed down through Wally's",
                            "descendants. I believe it is currently in the care of one",
                            "of the King's knights called Sir Prysin.",
                        ).also {
                            stage =
                                21
                        }
                    21 ->
                        npc(
                            "He shouldn't be too hard to find. He lives in the royal",
                            "palace in this city. Tell him Gypsy Aris sent you.",
                        ).also {
                            stage =
                                22
                        }
                    22 -> end()
                    30 -> npc("In the scheme of things you are very young.").also { stage = 31 }
                    31 -> end()
                    40 -> npc("See you anon.").also { stage = 41 }
                    41 -> end()
                    200 ->
                        npc(
                            "I would imagine an evil sorcerer is already starting on",
                            "the rituals to summon Delrith as we speak.",
                        ).also {
                            stage =
                                201
                        }

                    201 ->
                        options(
                            "How am I meant to fight a demon who can destroy cities?",
                            "Okay, where is he? I'll kill him for you!",
                            "What is the magical incantation?",
                            "Where can I find Silverlight?",
                        ).also {
                            stage =
                                202
                        }

                    202 ->
                        when (buttonId) {
                            1 ->
                                player("How am I meant to fight a demon who can destroy", "cities?!").also {
                                    stage =
                                        110
                                }
                            2 -> player("Okay, where is he? I'll kill him for you!").also { stage = 120 }
                            3 -> player("What is the magical incantation?").also { stage = 10 }
                            4 -> player("Where can I find Silverlight?").also { stage = 20 }
                        }

                    120 -> npc("Ah, the overconfidence of the young!").also { stage = 121 }
                    121 ->
                        npc(
                            "Delrith can't be harmed by ordinary weapons. You",
                            "must face him using the same weapon that Wally used.",
                        ).also {
                            stage =
                                201
                        }

                    110 ->
                        npc(
                            "If you face Delrith while he is still weak from being",
                            "summoned, and use the correct weapon, you will not",
                            "find the task too arduous.",
                        ).also {
                            stage =
                                111
                        }

                    111 ->
                        npc(
                            "Do not fear. If you follow the path of the great hero",
                            "Wally, then you are sure to defeat the demon.",
                        ).also {
                            stage =
                                201
                        }
                }

            0 ->
                when (stage) {
                    1 -> npc("Cross my palm with silver and the future will be", "revealed to you.").also { stage = 2 }
                    2 ->
                        options(
                            "Ok, here you go.",
                            "Who are you calling young one?!",
                            "No, I don't believe in that stuff.",
                            "With silver?",
                        ).also { stage = 3 }

                    3 ->
                        when (buttonId) {
                            1 -> player("Ok, here you go.").also { stage = 10 }
                            2 -> player("Who are you calling young one?!").also { stage = 20 }
                            3 -> player("No, I don't believe in that stuff.").also { stage = 30 }
                            4 -> player("With silver?").also { stage = 40 }
                        }

                    10 -> {
                        if (!player.inventory.containsItem(COINS)) {
                            sendMessage(player, "You need one gold coin.")
                            end()
                        } else {
                            if (player.inventory.remove(COINS)) {
                                npc(
                                    "Come closer, and listen carefully to what the future",
                                    "holds for you, as I peer into the swirling mists of the",
                                    "crystal ball.",
                                ).also {
                                    npc.animate(ANIMATION)
                                }
                                stage = 12
                            } else {
                                player("Sorry, I don't seem to have enough coins.")
                                stage = 11
                            }
                        }
                    }

                    11 -> end()
                    12 -> npc("I can see images forming. I can see you.").also { stage++ }
                    13 ->
                        npc(
                            "You are holding a very impressive looking sword. I'm",
                            "sure I recognize that sword...",
                        ).also { stage++ }

                    14 -> npc("There is a big dark shadow appearing now.").also { stage++ }
                    15 -> npc("Aargh!").also { stage++ }
                    16 -> player("Are you all right?").also { stage++ }
                    17 -> npc("It's Delrith! Delrith is coming!").also { stage++ }
                    18 -> player("Who's Delrith?").also { stage++ }
                    50 -> npc("Delrith...").also { stage++ }
                    51 -> npc("Delrith is a powerful demon.").also { stage++ }
                    52 ->
                        npc(
                            "Oh! I really hope he didn't see me looking at him",
                            "through my crystal ball!",
                        ).also { stage++ }
                    53 ->
                        npc(
                            "He tried to destroy this city 150 years ago. He was",
                            "stopped just in time by the great hero Wally.",
                        ).also {
                            stage++
                        }
                    54 ->
                        npc(
                            "Using his magic sword Silverlight, Wally managed to",
                            "trap the demon in the stone circle just south",
                            "of this city.",
                        ).also {
                            stage++
                        }
                    55 ->
                        npc(
                            "Ye gods! Silverlight was the sword you were holding in",
                            "my vision! You are the one destined to stop the demon",
                            "this time.",
                        ).also {
                            stage++
                        }
                    56 ->
                        options(
                            "How am I meant to fight a demon who can destroy cities?",
                            "Okay, where is he? I'll kill him for you!",
                            "Wally doesn't sound like a very heroic name.",
                        ).also {
                            stage =
                                57
                        }
                    57 ->
                        when (buttonId) {
                            1 -> player("How am I meant to fight a demon who can destroy cities?!").also { stage = 110 }
                            2 -> player("Okay, where is he? I'll kill him for you!").also { stage = 120 }
                            3 -> player("Wally doesn't sound like a very heroic name.").also { stage = 130 }
                        }
                    110 ->
                        npc(
                            "If you face Delrith while he is still weak from being",
                            "summoned, and use the correct weapon, you will not",
                            "find the task too arduous.",
                        ).also {
                            stage++
                        }
                    111 ->
                        npc(
                            "Do not fear. If you follow the path of the great hero",
                            "Wally, then you are sure to defeat the demon.",
                        ).also {
                            stage++
                        }
                    112 ->
                        options(
                            "Okay, where is he? I'll kill him for you!",
                            "Wally doesn't sound like a very heroic name.",
                            "So how did Wally kill Delrith?",
                        ).also {
                            stage =
                                113
                        }
                    113 ->
                        when (buttonId) {
                            1 -> player("Okay, where is he? I'll kill him for you!").also { stage = 120 }
                            2 -> player("Wally doesn't sound like a very heroic name.").also { stage = 130 }
                            3 -> player("So how did Wally kill Delrith?").also { stage = 180 }
                        }
                    120 -> npc("Ah, the overconfidence of the young!").also { stage++ }
                    121 ->
                        npc(
                            "Delrith can't be harmed by ordinary weapons. You",
                            "must face him using the same weapon that Wally used.",
                        ).also {
                            stage++
                        }
                    122 ->
                        options(
                            "How am I meant to fight a demon who can destroy cities?",
                            "Wally doesn't sound like a very heroic name.",
                            "So how did Wally kill Delrith?",
                        ).also {
                            stage =
                                123
                        }
                    123 ->
                        when (buttonId) {
                            1 -> player("How am I meant to fight a demon who can destroy cities?!").also { stage = 110 }
                            2 -> player("Wally doesn't sound like a very heroic name.").also { stage = 130 }
                            3 -> player("So how did Wally kill Delrith?").also { stage = 180 }
                        }
                    130 ->
                        npc(
                            "Yes I know. Maybe this is why history doesn't",
                            "remember him. However he was a very great hero.",
                        ).also {
                            stage++
                        }
                    131 ->
                        npc(
                            "Who knows how much pain and suffering Delrith would",
                            "have brought forth without Wally to stop him!",
                        ).also {
                            stage++
                        }
                    132 -> npc("It looks like you are going to need to perform similar", "heroics.").also { stage++ }
                    133 ->
                        options(
                            "How am I meant to fight a demon who can destroy cities?",
                            "Okay, where is he? I'll kill him for you!",
                            "So how did Wally kill Delrith?",
                        ).also {
                            stage =
                                134
                        }
                    134 ->
                        when (buttonId) {
                            1 -> player("How am I meant to fight a demon who can destroy cities?!").also { stage = 110 }
                            2 -> player("Okay, where is he? I'll kill him for you!").also { stage = 120 }
                            3 -> player("So how did Wally kill Delrith?").also { stage = 180 }
                        }
                    180 -> {
                        ActivityManager.start(player, "Wally cutscene", false)
                        end()
                    }
                    20 ->
                        npc(
                            "You have been on this world a relatively short time. At",
                            "least compared to me.",
                        ).also { stage++ }
                    21 -> end()
                    30 -> npc("Ok suit yourself.").also { stage++ }
                    31 -> end()
                    40 ->
                        npc(
                            "Oh, sorry, I forgot. With gold, I mean. They haven't",
                            "used silver coins since before you were born! So, do",
                            "you want your fortune told?",
                        ).also {
                            stage++
                        }
                    41 -> options("Ok, here you go.", "No, I don't believe in that stuff.").also { stage = 42 }
                    42 ->
                        when (buttonId) {
                            1 -> player("Ok, here you go.").also { stage = 10 }
                            2 -> player("No, I don't believe in that stuff.").also { stage = 30 }
                        }
                    200 -> {
                        val wally = NPC.create(4664, cutscene!!.base.transform(31, 40, 0))
                        wally.direction = Direction.WEST
                        cutscene!!.npcs.add(wally)
                        wally.init()
                        PacketRepository.send(
                            CameraViewPacket::class.java,
                            CameraContext(
                                player,
                                CameraContext.CameraType.POSITION,
                                player.location.x + 2,
                                player.location.y + 2,
                                260,
                                1,
                                10,
                            ),
                        )
                        PacketRepository.send(
                            CameraViewPacket::class.java,
                            CameraContext(
                                player,
                                CameraContext.CameraType.ROTATION,
                                player.location.x + 190,
                                player.location.y + 14,
                                260,
                                1,
                                10,
                            ),
                        )
                        interpreter.sendDialogues(wally, FaceAnim.FURIOUS, "Die, foul demon!")
                        Pulser.submit(
                            object : Pulse(5) {
                                override fun pulse(): Boolean {
                                    wally.animate(Animation(4603))
                                    return true
                                }
                            },
                        )
                        stage = 201
                    }

                    201 -> sendNPCDialogue(player, wally!!.id, "Now, what was that incantation again?").also { stage++ }
                    202 -> sendNPCDialogue(player, wally!!.id, DemonSlayerUtils.generateIncantation(player) + "!")
                    203 -> {
                        close()
                        player.interfaceManager.openOverlay(Component(115))
                        Pulser.submit(
                            object : Pulse(1) {
                                var counter = 0

                                override fun pulse(): Boolean {
                                    when (counter++) {
                                        1 -> player.properties.teleportLocation = cutscene!!.base.transform(25, 36, 0)
                                        3 -> {
                                            wally!!.direction = Direction.SOUTH_WEST
                                            wally!!.properties.teleportLocation = cutscene!!.base.transform(28, 40, 0)
                                            PacketRepository.send(
                                                CameraViewPacket::class.java,
                                                CameraContext(
                                                    player,
                                                    CameraContext.CameraType.POSITION,
                                                    player.location.x,
                                                    player.location.y,
                                                    440,
                                                    1,
                                                    100,
                                                ),
                                            )
                                            PacketRepository.send(
                                                CameraViewPacket::class.java,
                                                CameraContext(
                                                    player,
                                                    CameraContext.CameraType.ROTATION,
                                                    player.location.x + 1,
                                                    player.location.y + 1,
                                                    440,
                                                    1,
                                                    100,
                                                ),
                                            )
                                            player.interfaceManager.closeOverlay()
                                            player.interfaceManager.close()
                                            wally!!.animate(Animation(4604))
                                            sendNPCDialogue(player, wally!!.id, "I am the greatest demon slayer EVER!")
                                            stage = 204
                                        }

                                        4 -> {
                                            player.interfaceManager.closeOverlay()
                                            player.interfaceManager.close()
                                            wally!!.animate(Animation(4604))
                                            sendNPCDialogue(player, wally!!.id, "I am the greatest demon slayer EVER!")
                                            stage = 204
                                        }

                                        5 -> {
                                            wally!!.animate(Animation(4604))
                                            return true
                                        }
                                    }
                                    return false
                                }
                            },
                        )
                    }
                    204 ->
                        npc(
                            "By reciting the correct magical incantation, and",
                            "thrusting Silverlight into Delrith while he was newly",
                            "summoned, Wally was able to imprison Delrith in the",
                            "stone block in the centre of the circle.",
                        ).also {
                            stage++
                        }
                    205 -> {
                        cutscene!!.stop(true)
                        close()
                    }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GYPSY_ARIS_882)
    }

    companion object {
        private val COINS = Item(995, 1)
        private val ANIMATION = Animation(4615)
    }
}
