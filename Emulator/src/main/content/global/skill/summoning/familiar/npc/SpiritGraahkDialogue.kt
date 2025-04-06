package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.world.map.Location
import core.game.world.map.zone.impl.WildernessZone
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Spirit graahk dialogue.
 */
@Initializable
class SpiritGraahkDialogue : Dialogue {
    /**
     * Instantiates a new Spirit graahk dialogue.
     */
    constructor()

    /**
     * Instantiates a new Spirit graahk dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun newInstance(player: Player): Dialogue {
        return SpiritGraahkDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (npc !is Familiar) {
            return false
        }
        val fam = npc as Familiar
        if (fam.owner !== player) {
            player.packetDispatch.sendMessage("This is not your familiar.")
            return true
        } else {
            interpreter.sendOptions("Select an Option", "Chat", "Teleport")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (buttonId) {
            1 -> {
                val randomIndex = (Math.random() * 4).toInt()
                when (randomIndex) {
                    0 -> {
                        playerl(FaceAnim.FRIENDLY, "Your spikes are looking particularly spiky today.")
                        stage = 10
                    }

                    1 -> {
                        npcl(FaceAnim.OLD_DEFAULT, "My spikes hurt, could you pet them for me?")
                        stage = 16
                    }

                    2 -> {
                        npcl(FaceAnim.OLD_DEFAULT, "Hi!")
                        stage = 17
                    }

                    3 -> {
                        playerl(FaceAnim.FRIENDLY, "How's your day going?")
                        stage = 24
                    }
                }
            }

            2 -> if (!WildernessZone.checkTeleport(player, 20)) {
                player.sendMessage("You cannot teleport with the Graahk above level 20 wilderness.")
                end()
            } else {
                player.teleporter.send(Location(2786, 3002), TeleportType.NORMAL)
                end()
            }

            10 -> {
                npcl(FaceAnim.OLD_DEFAULT, "Really, you think so?")
                stage++
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "Yes. Most pointy, indeed.")
                stage++
            }

            12 -> {
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "That's really kind of you to say. I was going to spike you but I won't now..."
                )
                stage++
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "Thanks?")
                stage++
            }

            14 -> {
                npcl(FaceAnim.OLD_DEFAULT, "...I'll do it later instead.")
                stage++
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "*sigh!*")
                stage = END_DIALOGUE
            }

            16 -> {
                playerl(FaceAnim.FRIENDLY, "Aww, of course I can I'll just... Oww! I think you drew blood that time.")
                stage = END_DIALOGUE
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "Hello. Are you going to spike me again?")
                stage++
            }

            18 -> {
                npcl(FaceAnim.OLD_DEFAULT, "No, I got a present to apologise for last time.")
                stage++
            }

            19 -> {
                playerl(FaceAnim.FRIENDLY, "That's really sweet, thank you.")
                stage++
            }

            20 -> {
                npcl(FaceAnim.OLD_DEFAULT, "Here you go, it's a special cushion to make you comfortable.")
                stage++
            }

            21 -> {
                playerl(FaceAnim.FRIENDLY, "It's made of spikes!")
                stage++
            }

            22 -> {
                npcl(FaceAnim.OLD_DEFAULT, "Yes, but they're therapeutic spikes.")
                stage++
            }

            23 -> {
                playerl(FaceAnim.FRIENDLY, "...")
                stage = END_DIALOGUE
            }

            24 -> {
                npcl(FaceAnim.OLD_DEFAULT, "It's great! Actually I've got something to show you!")
                stage++
            }

            25 -> {
                playerl(FaceAnim.FRIENDLY, "Oh? What's that?")
                stage++
            }

            26 -> {
                npcl(FaceAnim.OLD_DEFAULT, "You'll need to get closer!")
                stage++
            }

            27 -> {
                playerl(FaceAnim.FRIENDLY, "I can't see anything...")
                stage++
            }

            28 -> {
                npcl(FaceAnim.OLD_DEFAULT, "It's really small - even closer.")
                stage++
            }

            29 -> {
                playerl(FaceAnim.FRIENDLY, "Oww! I'm going to have your spikes trimmed!")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_GRAAHK_7363, NPCs.SPIRIT_GRAAHK_7364)
    }
}