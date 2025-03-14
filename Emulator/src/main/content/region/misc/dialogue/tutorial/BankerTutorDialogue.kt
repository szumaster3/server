package content.region.misc.dialogue.tutorial

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class BankerTutorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Good day, would you like to access your bank account?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "How do I use the bank?",
                    "I'd like to access my bank account please.",
                    "I'd like to check my PIN settings.",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 ->
                        options(
                            "Using the bank itself.",
                            "Using Bank deposit boxes.",
                            "What's this PIN thing that people keep talking about?",
                            "Goodbye.",
                        ).also {
                            stage =
                                9
                        }

                    2 -> {
                        end()
                        player.bank.open()
                    }

                    3 -> {
                        end()
                        player.bankPinManager.openSettings()
                    }
                }
            9 ->
                when (buttonId) {
                    1 -> player("Using the bank itself. I'm not sure how....?").also { stage++ }
                    2 -> player("Using Bank deposit boxes.... what are they?").also { stage = 20 }
                    3 ->
                        playerl(FaceAnim.HALF_ASKING, "What's this PIN thing that people keep talking about?").also {
                            stage =
                                30
                        }
                    4 -> player("Goodbye.").also { stage = END_DIALOGUE }
                }
            10 ->
                npc(
                    "Speak to any banker and ask to see your bank",
                    "account. If you have set a PIN you will be asked for",
                    "it, then all the belongings you have placed in the bank",
                    "will appear in the window. To withdraw one item, left-",
                ).also {
                    stage++
                }
            11 -> npc("click on it once.").also { stage++ }
            12 ->
                npc(
                    "To withdraw many, right-click on the item and select",
                    "from the menu. The same for depositing, left-click on",
                    "the item in your inventory to deposit it in the bank.",
                    "Right-click on it to deposit many of the same items.",
                ).also {
                    stage++
                }
            13 ->
                npc(
                    "To move things around in your bank: firstly select",
                    "Swap or Insert as your default moving mode, you can",
                    "find these buttons on the bank window itself. Then click",
                    "and drag an item to where you want it to appear.",
                ).also {
                    stage++
                }
            14 ->
                npc(
                    "You may withdraw 'notes' or 'certificates' when the",
                    "items you are trying to withdraw do not stack in your",
                    "inventory. This will only work for items which are",
                    "tradeable.",
                ).also {
                    stage++
                }
            15 ->
                npc(
                    "For instance, if you wanted to sell 100 logs to another",
                    "player, they would not fit in one inventory and you",
                    "would need to do multiple trades. Instead, click the",
                    "Note button to do withdraw the logs as 'certs' or 'notes',",
                ).also {
                    stage++
                }
            16 -> npc("then withdraw the items you need.").also { stage = END_DIALOGUE }
            20 ->
                npc(
                    "They look like grey pillars, there's one just over there,",
                    "near the desk. Bank deposit boxes save so much time.",
                    "If you're simply wanting to deposit a single item, 'Use'",
                    "it on the deposit box.",
                ).also {
                    stage++
                }
            21 ->
                npc(
                    "Otherwise, simply click once on the box and it will give",
                    "you a choice of what to deposit in an interface very",
                    "similar to the bank itself. Very quick for when you're",
                    "simply fishing or mining etc.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            30 ->
                npc(
                    "The PIN - Personal Identification Number - can be",
                    "set on your bank account to protect the items there in",
                    "case someone finds out your account password. It",
                    "consists of four numbers that you remember and tell",
                ).also {
                    stage++
                }
            31 -> npc("no one.").also { stage++ }
            32 ->
                npc(
                    "So if someone did manage to get your password they",
                    "couldn't steal your items if they were in the bank.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BankerTutorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BANK_TUTOR_4907)
    }
}
