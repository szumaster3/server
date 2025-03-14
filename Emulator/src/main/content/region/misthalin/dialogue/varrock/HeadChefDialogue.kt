package content.region.misthalin.dialogue.varrock

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.Skillcape.purchase
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class HeadChefDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        var door = false
        if (args.size == 2) door = args[1] as Boolean
        if (door) {
            interpreter.sendDialogues(
                847,
                FaceAnim.NEUTRAL,
                "You can't come in here unless you're wearing a chef's",
                "hat or something like that.",
            )
            stage = 0
            return true
        }
        if (player.getSkills().getStaticLevel(Skills.COOKING) >= 99) {
            interpreter.sendDialogues(
                847,
                FaceAnim.HAPPY,
                "Hello, welcome to the Cooking Guild. It's always great to",
                "have such an accomplished chef visit. Say would you be",
                "interested in a Skillcape of Cooking? They're only available",
                "to master chefs.",
            )
            stage = 100
            return true
        }
        interpreter.sendDialogues(
            847,
            FaceAnim.HAPPY,
            "Hello, welcome to the Cooking Guild. Only accomplished",
            "chefs and cooks are allowed in here. Feel free to use any",
            "of our facilities.",
        )
        stage = 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> end()
            1 -> {
                options("Nice cape you're wearing!", "Thanks, bye.")
                stage = 2
            }

            2 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HAPPY, "Nice cape, you're wearing!")
                        stage = 10
                    }

                    2 -> {
                        player(FaceAnim.FRIENDLY, "Thanks, bye.")
                        stage = 3
                    }
                }

            3 -> end()
            10 -> {
                interpreter.sendDialogues(
                    847,
                    FaceAnim.HAPPY,
                    "Thank you! It's my most prized possession, it's a Skillcape",
                    "of Cooking; it shows that I've achieved level 99 Cooking",
                    "and am one of the best chefs in the land!",
                )
                stage = 11
            }

            11 -> end()
            100 -> {
                options("No thanks.", "Yes please.")
                stage = 101
            }

            101 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.FRIENDLY, "No thanks.")
                        stage = 110
                    }

                    2 -> {
                        player("Yes, please.")
                        stage = 150
                    }
                }

            110 -> {
                interpreter.sendDialogues(847, FaceAnim.HAPPY, "Okay, come back to me if you change your mind.")
                stage = 111
            }

            111 -> end()
            150 -> {
                interpreter.sendDialogues(
                    847,
                    FaceAnim.HALF_GUILTY,
                    "Most certainly, just as soon as you give me 99000 gold",
                    "coins.",
                )
                stage = 151
            }

            151 -> {
                options("That's much too expensive.", "Sure.")
                stage = 152
            }

            152 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.EXTREMELY_SHOCKED, "That's much too expensive.")
                        stage = 160
                    }

                    2 -> {
                        player(FaceAnim.HAPPY, "Sure.")
                        stage = 200
                    }
                }

            160 -> {
                interpreter.sendDialogues(847, FaceAnim.HALF_GUILTY, "I'm sorry you feel that way.")
                stage = 161
            }

            161 -> end()
            200 -> {
                if (purchase(player, Skills.COOKING)) {
                    interpreter.sendDialogues(847, FaceAnim.HAPPY, "Now you can use the title Master Chef.")
                }
                stage = 202
            }

            202 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return HeadChefDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HEAD_CHEF_847)
    }
}
