package content.region.desert.quest.desertrescue.dialogue

import content.region.desert.quest.desertrescue.TouristTrap
import core.api.sendDialogueLines
import core.api.sendNPCDialogue
import core.api.sendNPCDialogueLines
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getNpc
import core.plugin.Plugin
import org.rs.consts.Quests

class AnaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun init() {
        super.init()
    }

    override fun open(vararg args: Any): Boolean {
        quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
        if ((quest!!.getStage(player) == 71 || quest!!.getStage(player) == 72) && args.size > 1) {
            sendDialogueLines(
                player,
                "You see a barrel coming to the surface. Before too long you haul it",
                "onto the side. The barrel seems quite heavy and you hear a muffled",
                "sound coming from inside.",
            )
            stage = 400
            return true
        }
        when (quest!!.getStage(player)) {
            98 ->
                when (stage) {
                    0 -> {
                        npc(
                            "Great! Thanks for getting me out of that mine! And",
                            "that barrel wasn't too bad anyway! Pop by again",
                            "sometime, I'm sure we'll have a barrel of laughs!",
                        )
                        stage++
                    }
                }

            60 -> player("Hello!")
            61 -> {
                if (args.size > 1) {
                    npc("Hey, what do you think you're doing?")
                    stage = 20
                    return true
                }
                player("Hello again!")
            }

            else -> {
                player.packetDispatch.sendMessage("This slave does not appear interested in talking to you.")
                end()
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            98 ->
                when (stage) {
                    1 -> {
                        npc(
                            "Oh! I nearly forgot! Here's a key I found in the",
                            "tunnels. It might be some use to you, not sure what",
                            "it opens. Sorry, but I have to go now.",
                        )
                        stage++
                    }

                    2 -> {
                        interpreter.sendItemMessage(TouristTrap.WROUGHT_IRON_KEY, "Ana gives you a wrought iron key...")
                        stage++
                    }

                    3 -> {
                        player.inventory.add(TouristTrap.WROUGHT_IRON_KEY, player)
                        player.packetDispatch.sendMessage("Ana spots Irena and waves...")
                        val irena = getNpc(player, 4986)
                        irena!!.sendChat("Hi Ana!")
                        quest!!.setStage(player, 98)
                        end()
                        player.dialogueInterpreter.open(4986, irena)
                    }
                }

            71 ->
                when (stage) {
                    400 -> {
                        sendNPCDialogue(player, 823, "Get me OUT OF HERE!")
                        stage++
                    }

                    401 -> {
                        quest!!.setStage(player, 72)
                        end()
                    }
                }

            61 ->
                when (stage) {
                    0 -> {
                        npc("Hello there, how's it going? Do you have a plan to get", "out of here yet?")
                        stage++
                    }

                    1 -> {
                        player("Well, I'm working on it, have you got any suggestions?")
                        stage++
                    }

                    2 -> {
                        npc("Hmmm.")
                        stage++
                    }

                    3 -> {
                        npc("No, sorry...")
                        stage++
                    }

                    4 -> {
                        npc("The only thing that gets out of here is the rock that we", "mine.")
                        stage++
                    }

                    5 -> {
                        npc(
                            "Not even the dead get a decent funeral. Bodies are",
                            "just thrown down disused mine holes. It's very",
                            "disrespectful...",
                        )
                        stage++
                    }

                    6 -> {
                        player("How does the rock get out?")
                        stage++
                    }

                    7 -> {
                        npc(
                            "Well, we mine it in this section, then someone scoops it",
                            "into a barrel. The barrels are loaded onto a mine cart.",
                            "Then they're deposited near the surface lift.",
                        )
                        stage++
                    }

                    8 -> {
                        npc("I have no idea where they go from there. But that's", "not going to help us, is it?")
                        stage++
                    }

                    9 -> {
                        player("Where would I get one of those barrels from?")
                        stage++
                    }

                    10 -> {
                        npc(
                            "Well, you would get one from around by the life area.",
                            "But why would you want one of those?",
                        )
                        stage++
                    }

                    11 -> {
                        player("I could try to sneak you out if you were in a barrel!")
                        stage++
                    }

                    12 -> {
                        npc("There is no way you are getting me into a barrel. No", "WAY! Do you understand?")
                        stage++
                    }

                    13 -> {
                        player("Well, we'll see, it might be the only way.")
                        stage++
                    }

                    14 -> end()
                    20 ->
                        if (player.inventory.remove(TouristTrap.BARREL)) {
                            player.sendChat("Shush... It's for your own good!")
                            player.inventory.add(TouristTrap.ANNA_BARREL)
                            close()
                            player.lock(3)
                            Pulser.submit(
                                object : Pulse(3, player) {
                                    override fun pulse(): Boolean {
                                        sendNPCDialogueLines(
                                            player,
                                            823,
                                            FaceAnim.HALF_GUILTY,
                                            false,
                                            "<col=08088A>-- You manage to squeeze Ana into the barrel, --",
                                            "<col=08088A>-- despite her many complaints. --",
                                            "I djont fit in dis bawwel... Wet me out!!",
                                        )
                                        stage++
                                        return true
                                    }
                                },
                            )
                        }

                    21 -> end()
                }

            60 ->
                when (stage) {
                    0 -> {
                        npc("Hello there, I don't think I've seen you before.")
                        stage++
                    }

                    1 -> {
                        player("What's your name?")
                        stage++
                    }

                    2 -> {
                        npc(
                            "My name? Oh, how sweet, my name is Ana. I come from",
                            "Al-Kharid though we've only recently moved there. I was",
                            "born, and did most of my growing up, in Varrock. I",
                            "thought the desert might be interesting. What a surprise",
                        )
                        stage++
                    }

                    3 -> {
                        npc("I got!")
                        stage++
                    }

                    4 -> {
                        player("Do you want to go back to Al-Kharid?")
                        stage++
                    }

                    5 -> {
                        npc(
                            "Sure, I miss my Mum, her name is Irena and she is",
                            "probably waiting for me. How do you propose we get out",
                            "of here though?",
                        )
                        stage++
                    }

                    6 -> {
                        npc(
                            "I'm sure you've noticed the many square jawed guards",
                            "around here. You look like you can handle yourself",
                            "but I have my doubts that you can take them all on!",
                        )
                        stage++
                    }

                    7 -> {
                        quest!!.setStage(player, 61)
                        end()
                    }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(822)
    }

    class AnaBarrelHandler : UseWithHandler(TouristTrap.BARREL.id) {
        override fun newInstance(arg: Any?): Plugin<Any> {
            addHandler(822, NPC_TYPE, this)
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            val quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
            when (quest.getStage(player)) {
                61 -> player.dialogueInterpreter.open(822, event.usedWith, true)
                else -> return false
            }
            return true
        }
    }

    class AnaNPC : AbstractNPC {
        constructor() : super(0, null)

        constructor(id: Int, location: Location?) : super(id, location)

        override fun construct(
            id: Int,
            location: Location,
            vararg objects: Any,
        ): AbstractNPC {
            return AnaNPC(id, location)
        }

        override fun isHidden(player: Player): Boolean {
            val quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
            return if (quest.getStage(player) > 61) {
                true
            } else {
                player.inventory.containsItem(TouristTrap.ANNA_BARREL) ||
                    player.bank.containsItem(
                        TouristTrap.ANNA_BARREL,
                    )
            }
        }

        override fun getIds(): IntArray {
            return intArrayOf(822)
        }
    }
}
