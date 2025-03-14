package content.region.desert.quest.desertrescue.dialogue

import content.region.desert.quest.desertrescue.TouristTrap
import core.game.dialogue.Dialogue
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.world.map.Location
import org.rs.consts.Quests

class MercenaryCaptainDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
        when (quest!!.getStage(player)) {
            11 -> interpreter.sendDialogue("You approach the Mercenary Captain.")
            else -> npc("What are you doing here?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            11 ->
                when (stage) {
                    0 -> {
                        player("Wow! A real captain!")
                        stage++
                    }

                    1 -> {
                        npc("Be off effendi, you are not wanted around here.")
                        stage++
                    }

                    2 -> {
                        player("I'd love to work for a tough guy like you!")
                        stage++
                    }

                    3 -> {
                        npc("Hmmm, oh yes, what can you do?")
                        stage++
                    }

                    4 -> {
                        player("Can't I do something for a strong Captain like you?")
                        stage++
                    }

                    5 -> {
                        interpreter.sendDialogue("The Captain ponders a moment and then looks at you critically.")
                        stage++
                    }

                    6 -> {
                        npc("You could bring me the head of Al Zaba Bhasim.")
                        stage++
                    }

                    7 -> {
                        npc(
                            "He is the leader of the notorious desert bandits, they",
                            "plague us daily. You should find them west of here.",
                            "You should have no problem in finishing them all off.",
                            "Do this for me and maybe I will consider helping you.",
                        )
                        stage++
                    }

                    8 -> {
                        player("Sorry Sir, I don't think I can do that.")
                        stage++
                    }

                    9 -> {
                        npc(
                            "Hmm, well yes, I did consider that you might not be",
                            "right for the job. be off with you then before I turn",
                            "my men loose on you.",
                        )
                        stage++
                    }

                    10 -> {
                        player("It's a funny captain who can't fight his own battles!")
                        stage++
                    }

                    11 -> {
                        interpreter.sendDialogue(
                            "The men around you fall silent and the Captain silently fumes. All",
                            "eyes turn to the Captain...",
                        )
                        stage++
                    }

                    12 -> {
                        npc("Very well, if you're challenging me, let's get on with it!")
                        stage++
                    }

                    13 -> {
                        interpreter.sendDialogue("The guards gather around to watch the fight.")
                        stage++
                    }

                    14 -> {
                        end()
                        npc.properties.combatPulse.attack(player)
                    }
                }

            else ->
                when (stage) {
                    0 -> {
                        player("Nothing, just passing by.")
                        stage++
                    }

                    1 -> end()
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(830)
    }

    class MercenaryCaptain : AbstractNPC {
        constructor() : super(0, null)

        constructor(id: Int, location: Location?) : super(id, location)

        override fun construct(
            id: Int,
            location: Location,
            vararg objects: Any,
        ): AbstractNPC {
            return MercenaryCaptain(id, location)
        }

        override fun finalizeDeath(killer: Entity) {
            super.finalizeDeath(killer)
            if (killer is Player) {
                val player = killer
                val quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
                when (quest.getStage(player)) {
                    0, 10 -> {}
                    else -> {
                        if (!player.inventory.containsItem(TouristTrap.METAL_KEY) &&
                            !player.bank.containsItem(
                                TouristTrap.METAL_KEY,
                            )
                        ) {
                            player.inventory.add(TouristTrap.METAL_KEY, player)
                            player.dialogueInterpreter.sendItemMessage(
                                TouristTrap.METAL_KEY,
                                "The mercenary captain drops a metal key on the floor.",
                                "You quickly grab the key and add it to your inventory.",
                            )
                        }
                        quest.setStage(player, 20)
                    }
                }
            }
        }

        override fun getIds(): IntArray {
            return intArrayOf(830)
        }
    }
}
