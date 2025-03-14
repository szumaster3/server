package content.region.misthalin.dialogue.draynor

import core.api.removeAttribute
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class PrinceAliDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.PRINCE_ALI_RESCUE)
        when (quest!!.getStage(player)) {
            50 -> {
                player("Prince, I come to rescue you.")
                stage = 0
            }

            60, 100 -> {
                npc(
                    "I owe you my life for that escape. You cannot help me",
                    "this time, they know who you are. Go in peace, friend",
                    "of Al-Kharid",
                )
                stage = 100
            }

            else -> end()
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            100 -> end()
            0 -> {
                npc("That is very kind of you, how do I get out?")
                stage = 1
            }

            1 -> {
                player(
                    "With a disguise. I have removed the Lady Keli. She is",
                    "tied up, but will not stay tied up for long.",
                )
                stage = 2
            }

            2 -> {
                player("Take this disguise, and this key.")
                stage = 3
            }

            3 -> {
                for (i in DISGUISE) {
                    if (!player.inventory.containsItem(i)) {
                        player.packetDispatch.sendMessage("You don't have all the parts of the disguise.")
                        end()
                        return true
                    }
                }
                if (player.inventory.remove(*DISGUISE)) {
                    interpreter.sendDialogue("You hand the disguise and the key to the prince.")
                    stage = 4
                    quest!!.setStage(player, 60)
                    removeAttribute(player, "guard-drunk")
                    player.gameAttributes.removeAttribute("guard-drunk")
                }
            }

            4 -> {
                npc.transform(921)
                Pulser.submit(
                    object : Pulse(50) {
                        override fun pulse(): Boolean {
                            npc.transform(920)
                            return true
                        }
                    },
                )
                npc(
                    "Thank you my friend, I must leave you now. My",
                    "father will pay you well for this.",
                )
                stage = 5
            }

            5 -> {
                player("Go to Leela, she is close to here.")
                stage = 6
            }

            6 -> {
                npc.isInvisible = true
                Pulser.submit(
                    object : Pulse(20) {
                        override fun pulse(): Boolean {
                            npc.transform(920)
                            npc.isInvisible = false
                            return true
                        }
                    },
                )
                interpreter.sendDialogue(
                    "The prince has escaped, well done! You are now a friend of Al-",
                    "Kharid and may pass through the Al-Kharid toll gate for free.",
                )
                stage = 7
            }

            7 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return PrinceAliDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PRINCE_ALI_920)
    }

    companion object {
        private val DISGUISE = arrayOf(Item(2424), Item(2419), Item(2418), Item(1013))
    }
}
