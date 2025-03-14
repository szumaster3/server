package content.region.kandarin.dialogue.seers

import content.region.kandarin.handlers.seers.IgnatiusVulcanNPC
import core.game.dialogue.Dialogue
import core.game.global.Skillcape.isMaster
import core.game.global.Skillcape.purchase
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class IgnatiusVulcanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Can I help you at all?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (isMaster(player, Skills.FIREMAKING)) {
                    options("Who are you?", "Could I buy a Skillcape of Firemaking?", "No, thanks.")
                    stage = 100
                } else {
                    options("Who are you?", "What is that cape you're wearing?", "No, thanks.")
                    stage = 1
                }

            100 ->
                when (buttonId) {
                    1 -> {
                        player("Who are you?")
                        stage = 10
                    }

                    2 -> {
                        player("Could I buy a Skillcape of Firemaking?")
                        stage = 101
                    }

                    3 -> {
                        player("No, thanks.")
                        stage = 30
                    }
                }

            101 -> {
                npc("Certainly! Right when you give me 99000 coins.")
                stage = 102
            }

            102 -> {
                options("Okay, here you go.", "No, thanks.")
                stage = 103
            }

            103 ->
                when (buttonId) {
                    1 -> {
                        player("Okay, here you go.")
                        stage = 104
                    }

                    2 -> end()
                }

            104 -> {
                if (purchase(player, Skills.FIREMAKING)) {
                    npc("There you go! Enjoy.")
                }
                stage = 105
            }

            105 -> end()
            1 ->
                when (buttonId) {
                    1 -> {
                        player("Who are you?")
                        stage = 10
                    }

                    2 -> {
                        player("What is that cape you're wearing?")
                        stage = 20
                    }

                    3 -> {
                        player("No, thanks.")
                        stage = 30
                    }
                }

            10 -> {
                npc(
                    "My name is Ignatius Vulcan. Once I was - like you -",
                    "an adventurer, but that was before I realised the",
                    "beauty and power of flame! Just look at this...",
                )
                stage = 11
            }

            11 -> {
                IgnatiusVulcanNPC.createFire(npc, player.location)
                player.moveStep()
                player.sendChat("Yeeouch!")
                npc(
                    "Stare into the flame and witness the purity and power",
                    "of fire! As my attraction to flame grew, so did my skills",
                    "at firelighting. I began to neglect my combat skills, my",
                    "Mining skills and my questing. Who needs such",
                )
                stage = 12
            }

            12 -> {
                npc(
                    "mundane skills when one can harness the power of fire?",
                    "After years of practice I am now the acknowledged",
                    "master of Flame! Everything must be purified by fire!",
                )
                stage = 13
            }

            13 -> {
                player("Okaaay! err, I'll be going now. Umm, get better soon.")
                stage = 14
            }

            14 -> end()
            20 -> {
                npc(
                    "This is a Skillcape of Firemaking. I was given it in",
                    "recognition of my skill as the greatest firemaker in the",
                    "lands! I AM the Master of Flame!",
                )
                stage = 21
            }

            21 -> {
                player("Hmm, I'll be going now. Keep a sharp look out for", "those men with their white jackets!")
                stage = 22
            }

            22 -> end()
            30 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return IgnatiusVulcanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.IGNATIUS_VULCAN_4946)
    }
}
