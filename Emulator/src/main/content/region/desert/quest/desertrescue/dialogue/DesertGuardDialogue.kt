package content.region.desert.quest.desertrescue.dialogue

import content.region.desert.quest.desertrescue.TouristTrap
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayers
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.NPCs
import org.rs.consts.Quests

class DesertGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun init() {
        super.init()
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
        when (npc.id) {
            5001 ->
                when (quest!!.getStage(player)) {
                    40 -> npc("Yeah, what do you want?")
                    50, 51, 52, 53, 54 ->
                        stage =
                            if (player.inventory.containsItem(TouristTrap.TENTI_PINEAPPLE)) {
                                player("Hey... I have something for you!")
                                0
                            } else {
                                npc("Do you have my tenti pineapple yet?")
                                10
                            }

                    60, 70, 80, 90, 100 -> {
                        if (stage == 60) {
                            end()
                            return true
                        }
                        npc(
                            "That pineapple was just delicious, many thanks. I don't",
                            "suppose you could get me another? <col=08088A>-- The guard looks at",
                            "<col=08088A>you pleadingly.",
                        )
                    }

                    else -> npc("What are you looking at?")
                }

            else ->
                when (quest!!.getStage(player)) {
                    else -> npc("What are you looking at?")
                }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (npc.id) {
            5001 ->
                when (quest!!.getStage(player)) {
                    51, 52, 53, 54 ->
                        when (stage) {
                            0 -> {
                                interpreter.sendItemMessage(
                                    TouristTrap.TENTI_PINEAPPLE,
                                    "You show the Tenti pineapple to the guard.",
                                )
                                stage++
                            }

                            1 ->
                                if (player.inventory.remove(TouristTrap.TENTI_PINEAPPLE)) {
                                    quest!!.setStage(player, 60)
                                    npc(
                                        "Great! Just what I've been looking for! Mmmmmmm...",
                                        "Delicious! This is so nice! Mmmmm, *SLURP*",
                                        "Yummmm... Oh yes, this is great.",
                                    )
                                    stage = 10
                                }

                            -10 -> {
                                player("Sorry, I don't have it yet.")
                                stage = END_DIALOGUE
                            }

                            -11 -> end()
                            10 -> {
                                player("So, can I go through now please?")
                                stage++
                            }

                            11 -> {
                                npc("Yes, yes, of course... a deal's a deal!")
                                stage++
                            }

                            12 -> end()
                        }

                    50 ->
                        when (stage) {
                            0 -> {
                                interpreter.sendItemMessage(
                                    TouristTrap.TENTI_PINEAPPLE,
                                    "You show the Tenti pineapple to the guard.",
                                )
                                stage++
                            }

                            1 ->
                                if (player.inventory.remove(TouristTrap.TENTI_PINEAPPLE)) {
                                    quest!!.setStage(player, 60)
                                    npc(
                                        "Great! Just what I've been looking for! Mmmmmmm...",
                                        "Delicious!! This is so nice! Mmmmm, *SLURP*",
                                        "Yummmm... Oh yes, this is great.",
                                    )
                                    stage = 10
                                }

                            10 -> {
                                player("Sorry, I don't have it yet.")
                                stage++
                            }

                            11 -> end()
                        }

                    40 ->
                        when (stage) {
                            0 -> {
                                player("I'd like to mine in a different area.")
                                stage++
                            }

                            1 -> {
                                npc(
                                    "Oh, you want to work in another area of the mine eh?",
                                    "<col=08088A>-- The guard seems pleased with his rhetorical question.--",
                                    "Well, I can understand that! A change is as good as a",
                                    "rest they say.",
                                )
                                stage++
                            }

                            2 -> {
                                player("Yes sir, you're quite right sir.")
                                stage++
                            }

                            3 -> {
                                npc(
                                    "Of course I'm right. And what goes around comes",
                                    "around as they say. And it's been absolutely ages since",
                                    "I've had anything different to eat.",
                                )
                                stage++
                            }

                            4 -> {
                                npc(
                                    "What I wouldn't give for some whole, fresh, ripe and",
                                    "juicy pineapple for a change. And those Tenti's have the",
                                    "best pineapple in this entire area.",
                                )
                                stage++
                            }

                            5 -> {
                                interpreter.sendDialogue("The guard winks at you.")
                                stage++
                            }

                            6 -> {
                                npc("I'm sure you get my meaning...")
                                stage++
                            }

                            7 -> {
                                player("Yes sir, we understand each other perfectly.")
                                stage++
                            }

                            8 -> {
                                npc(
                                    "Okay, good the. And remember, I prefer my",
                                    "pineapples whole, not chopped up with all the juice gone.",
                                )
                                stage++
                            }

                            9 -> {
                                interpreter.sendDialogue("The guard moves back to his post and winks at you knowingly.")
                                stage++
                            }

                            10 -> {
                                quest!!.setStage(player, 50)
                                end()
                            }
                        }

                    100, 60 ->
                        when (stage) {
                            0 -> {
                                player(
                                    "You must be joking! The last one I got you cost me",
                                    "double shifts working copper ore! You should be",
                                    "grateful you got one at all.",
                                )
                                stage++
                            }

                            1 -> end()
                            10 -> {
                                player("So, can I go through now please?")
                                stage++
                            }

                            11 -> {
                                npc("Yes, yes, of course... a deal's a deal!")
                                stage++
                            }

                            12 -> end()
                        }

                    else -> end()
                }

            else ->
                when (quest!!.getStage(player)) {
                    else -> end()
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(4993, 4994, 4995, 4996, 4997, 4998, 4999, 5000, 5001)
    }

    class DesertGuardNPC : AbstractNPC {
        private var lastCheck = 0

        constructor() : super(0, null)

        constructor(id: Int, location: Location?) : super(id, location)

        override fun construct(
            id: Int,
            location: Location,
            vararg objects: Any,
        ): AbstractNPC {
            return DesertGuardNPC(id, location)
        }

        override fun tick() {
            if (lastCheck < ticks) {
                lastCheck = ticks + RandomFunction.random(50, 150)
                if (!inCombat()) {
                    warn()
                }
            }
            super.tick()
        }

        private fun warn() {
            val players = getLocalPlayers(this)
            for (player in players) {
                if (player.getAttribute(
                        "guard-warning",
                        0,
                    ) > ticks ||
                    !player.zoneMonitor.isInZone("mining camp") ||
                    player.inCombat() ||
                    !player.location.withinDistance(
                        getLocation(),
                        8,
                    )
                ) {
                    continue
                }
                if (TouristTrap.inJail(player)) {
                    continue
                }
                if (!TouristTrap.hasSlaveClothes(player)) {
                    player.setAttribute("guard-warning", ticks + 300)
                    player.lock()
                    Pulser.submit(
                        object : Pulse(1, this, player) {
                            var count = 0

                            override fun pulse(): Boolean {
                                when (++count) {
                                    1 -> {
                                        face(player)
                                        if (!TouristTrap.hasSlaveClothes(player)) {
                                            sendChat("Hey, they're interesting clothes!")
                                            sendMessage(player, "Guard: You're no slave.")
                                        } else {
                                            sendChat("Hey - you with the armour!")
                                            sendMessage(player, "Guard: Hey - You with the armour!")
                                        }
                                    }

                                    5 ->
                                        if (!TouristTrap.hasSlaveClothes(player)) {
                                            sendChat("You're no slave.")
                                            sendMessage(player, "Guard: What are you doing in here?")
                                        } else {
                                            sendChat("You're not allowed in here!")
                                            sendMessage(player, "Guard: You're not allowed in here!")
                                        }

                                    8 -> {
                                        sendChat("Intel the cell you go! I hope this teaches you a lesson.")
                                        properties.combatPulse.attack(player)
                                    }

                                    10 ->
                                        if (!TouristTrap.inJail(player)) {
                                            TouristTrap.jail(player)
                                        }
                                }
                                return false
                            }
                        },
                    )
                    break
                }
            }
        }

        override fun getIds(): IntArray {
            return intArrayOf(
                NPCs.GUARD_4993,
                NPCs.GUARD_4994,
                NPCs.GUARD_4995,
                NPCs.GUARD_4996,
                NPCs.GUARD_4997,
                NPCs.GUARD_4998,
                NPCs.GUARD_4999,
                NPCs.GUARD_5000,
                NPCs.GUARD_5001,
                NPCs.GUARD_5002,
            )
        }
    }
}
