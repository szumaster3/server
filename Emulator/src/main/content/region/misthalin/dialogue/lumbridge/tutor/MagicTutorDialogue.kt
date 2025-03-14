package content.region.misthalin.dialogue.lumbridge.tutor

import core.api.freeSlots
import core.api.inBank
import core.api.inInventory
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MagicTutorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (args.isEmpty() || args[0] !is NPC) {
            throw IllegalArgumentException("Invalid arguments provided. Expected an NPC as the first argument.")
        }
        npc = args[0] as NPC
        if (player == null) {
            throw IllegalStateException("Player cannot be null.")
        }

        if (args.size == 2) {
            if (player.getSavedData().globalData.getTutorClaim() > System.currentTimeMillis()) {
                npc(
                    "I work with the Ranged Combat tutor to give out",
                    "consumable items that you may need for combat such",
                    "as arrows and runes. However we have had some",
                    "cheeky people try to take both!",
                )
                stage = 200
                return true
            }
            stage = 99
            for (x in 0..15) {
                for (y in 0..15) {
                    val ground = GroundItemManager.get(MIND_RUNE.id, player.location.transform(x, y, 0), player)
                    val second = GroundItemManager.get(AIR_RUNE.id, player.location.transform(x, y, 0), player)
                    if ((ground != null && ground.droppedBy(player)) || (second != null && second.dropper == player)) {
                        npc(
                            "Someone seems to have dropped some " + (if (ground == null) "air" else "mind") +
                                " runes on",
                            "the floor, perhaps you should pick them up to use them.",
                        )
                        return true
                    }
                }
            }
            if (freeSlots(player) < 2) {
                end()
                sendMessage(player, "Not enough inventory space.")
                return true
            }
            if (inInventory(player, MIND_RUNE.id) || inBank(player, MIND_RUNE.id)) {
                npc("You have some mind runes already!")
                stage = 69
                return true
            }
            if (inInventory(player, AIR_RUNE.id) || inBank(player, AIR_RUNE.id)) {
                npc("You have some air runes already!")
                stage = 70
                return true
            }
            add(true)
            add(false)
            stage = 99
            return true
        }

        options(
            "Can you teach me the basics please?",
            "Teach me about making runes.",
            "Goodbye.",
        )
        stage = 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            69 -> {
                if (inInventory(player, AIR_RUNE.id) || inBank(player, AIR_RUNE.id)) {
                    npc("You have some air runes already!")
                    stage = 99
                    return true
                }
                add(false)
                stage = 99
                end()
            }

            70 -> {
                if (inInventory(player, MIND_RUNE.id) || inBank(player, MIND_RUNE.id)) {
                    npc("You have some mind runes already!")
                    stage = 99
                    return true
                }
                add(true)
                stage = 99
                end()
            }

            99 -> end()
            300 -> add(false)
            301 -> end()

            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Can you teach me the basics please?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Teach me about making runes.").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Goodbye.").also { stage = END_DIALOGUE }
                }
            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You can cast different spells according to what runes",
                    "you have in your inventory. To start off with you'll",
                    "need mind runes and air runes. These will allow you to",
                    "cast Wind Strike like you did in the tutorial.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Use the spell book icon in the top right of the control",
                    "panel to see what spells you can cast. If you have the",
                    "correct runes, the spell will light up.",
                ).also {
                    stage++
                }
            12 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Nemarti, the Ranged Combat tutor and I both give out",
                    "items every 30 minutes, however you must choose",
                    "whether you want runes or ranged equipment. To",
                    "claim runes, right-click on me and choose Claim, to claim",
                ).also {
                    stage++
                }
            13 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "ranged equipment right-click on the Ranged Combat",
                    "tutor and select Claim.",
                ).also {
                    stage++
                }
            14 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "When you have the spell available, click on it once, then",
                    "click on your target. A good target would be a monster",
                    "that is below your combat level.",
                ).also {
                    stage++
                }
            15 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Try rats in the castle or if you're feeling brave, the",
                    "goblins to the west of here have been causing a",
                    "nuisance of themselves.",
                ).also {
                    stage++
                }
            16 ->
                options(
                    "Can you teach me the basics please?",
                    "Teach me about making runes.",
                    "Goodbye.",
                ).also { stage = 1 }
            20 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'd talk to the Duke of Lumbridge if I were you. I",
                    "hear he has an interesting artifact that might just have",
                    "something to do with Runecrafting. I expect there wil",
                    "be a quest involved too!",
                ).also {
                    stage++
                }
            21 ->
                options(
                    "Can you teach me the basics please?",
                    "Teach me about making runes.",
                    "Goodbye.",
                ).also { stage = 1 }
            200 ->
                npc(
                    "So, every half an hour, you may come back and claim",
                    "either arrows OR runes, but not both. Come back in a",
                    "while for runes, or simply make your own.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    fun add(mind: Boolean) {
        if (mind) {
            if (player.inventory.add(Item(MIND_RUNE.id, 30))) {
                player.dialogueInterpreter.sendItemMessage(MIND_RUNE.id, "Mikasi gives you 30 mind runes.")
                stage = 300
                player.getSavedData().globalData.setTutorClaim(System.currentTimeMillis() + 1200000)
            }
        } else {
            if (player.inventory.add(Item(AIR_RUNE.id, 30))) {
                player.dialogueInterpreter.sendItemMessage(AIR_RUNE.id, "Mikasi gives you 30 mind runes.")
                stage = 301
                player.getSavedData().globalData.setTutorClaim(System.currentTimeMillis() + 1200000)
            }
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAGIC_TUTOR_4707)
    }

    companion object {
        private val MIND_RUNE = Item(558)
        private val AIR_RUNE = Item(556)
    }
}
