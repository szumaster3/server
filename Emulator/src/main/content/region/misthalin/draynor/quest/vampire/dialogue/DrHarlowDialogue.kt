package content.region.misthalin.draynor.quest.vampire.dialogue

import core.api.inBank
import core.api.inInventory
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.api.sendDialogue
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class DrHarlowDialogue(player: Player? = null) : Dialogue(player) {

    val ITEMS = arrayOf(Item(Items.STAKE_1549), Item(Items.BEER_1917, 1))

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Buy me a drrink pleassh.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 ->
                if (getQuestStage(player, Quests.VAMPIRE_SLAYER) == 10) {
                    options("No, you've had enough.", "Morgan needs your help!").also { stage = 1 }
                } else if (getQuestStage(player, Quests.VAMPIRE_SLAYER) == 20) {
                    if (inInventory(player, 1917, 1)) {
                        player("Here you go.").also { stage = 20 }
                    } else {
                        player("I'll just go and buy one.").also { stage = END_DIALOGUE }
                    }
                } else if (getQuestStage(player, Quests.VAMPIRE_SLAYER) == 30) {
                    if (!inBank(player, Items.STAKE_1549) && !inInventory(player, Items.STAKE_1549)) {
                        if (!player.inventory.add(ITEMS[0])) {
                            val item = GroundItem(ITEMS[0], npc.location, player)
                            GroundItemManager.create(item)
                        }
                        sendItemDialogue(player, Items.STAKE_1549, "Dr Harlow hands you a stake.")
                        stage = 27
                        return true
                    }
                    if (inInventory(player, Items.BEER_1917, 1)) {
                        player("Here you go.").also { stage = 20 }
                    } else {
                        player("I'll just go and buy one.").also { stage = END_DIALOGUE }
                    }
                } else if (isQuestComplete(player, Quests.VAMPIRE_SLAYER)) {
                    npc("Oh, itsh you. What do you want?").also { stage = 30 }
                } else {
                    player("No, you've had enough.").also { stage = END_DIALOGUE }
                }

            1 ->
                when (buttonId) {
                    1 -> player("No, you've had enough.").also { stage = END_DIALOGUE }
                    2 -> player("Morgan needs your help!").also { stage = 5 }
                }

            5 -> npc("Morgan you shhay..?").also { stage++ }
            6 ->
                player(
                    "His village is being terrorised by a vampire! He told me",
                    "to ask you about how I can stop it.",
                ).also { stage++ }

            7 ->
                npc(
                    FaceAnim.DRUNK,
                    "Buy me a beer... then I'll teash you what you need to",
                    "know...",
                ).also { stage++ }

            8 -> player("But this is your friend Morgan we're talking about!").also { stage++ }
            9 -> npc(FaceAnim.DRUNK, "Buy ush a drink anyway...").also { stage++ }
            10 -> {
                setQuestStage(player, Quests.VAMPIRE_SLAYER, 20)
                end()
            }

            20 ->
                if (player.inventory.remove(ITEMS[1])) {
                    interpreter.sendItemMessage(1917, "You give a beer to Dr Harlow.")
                    setQuestStage(player, Quests.VAMPIRE_SLAYER, 30)
                    stage = 21
                }

            21 -> npc("Cheersh matey...").also { stage++ }
            22 -> player("So tell me how to kill vampires then.").also { stage++ }
            23 -> npc("Yesh Yesh vampires, I was very good at", "killing em once...").also { stage++ }
            24 -> sendDialogue(player, "Dr Harlow appears to sober up slightly.").also { stage++ }
            25 ->
                npc(
                    "Well you're gonna to need a stake, otherwise he'll just",
                    "regenerate. Yes, you must have a stake to finish it off..",
                    "I just happen to have one with me.",
                ).also { stage++ }

            26 -> {
                if (!player.inventory.add(ITEMS[0])) {
                    val item = GroundItem(ITEMS[0], npc.location, player)
                    GroundItemManager.create(item)
                }
                sendItemDialogue(player, Items.STAKE_1549, "Dr Harlow hands you a stake.")
                stage = 27
            }

            27 ->
                npc(
                    "You'll need a hammer as well, to drive it in properly,",
                    "your everyday general store hammer will do. One last",
                    "thing... It's wise to carry garlic with you, vampires are",
                    "somewhat weakend if they can smell garlic. Morgan",
                ).also { stage++ }

            28 ->
                npc(
                    "alwys liked garlic, you should try his house. But",
                    "remember, a vampire is still a dangerous foe!",
                ).also { stage++ }

            29 -> player("Thank you very much!").also { stage = END_DIALOGUE }
            30 ->
                playerl(
                    FaceAnim.HAPPY,
                    "I successfully slayed the vampire! Morgan's village is now free of threat and all is well.",
                ).also { stage++ }

            31 ->
                npcl(
                    FaceAnim.DRUNK,
                    "Well, that calls for a drink to celebrate! How 'bout yoush buy me one?",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = DrHarlowDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.DR_HARLOW_756)
}
