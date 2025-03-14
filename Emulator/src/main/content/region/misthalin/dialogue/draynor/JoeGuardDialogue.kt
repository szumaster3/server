package content.region.misthalin.dialogue.draynor

import core.api.removeItem
import core.api.sendDialogue
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class JoeGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.PRINCE_ALI_RESCUE)
        when (quest!!.getStage(player)) {
            40 -> {
                if (player.getAttribute("guard-drunk", false)) {
                    npc("Halt! Who goes there?").also { stage = 23 }
                    return true
                }
                if (player.inventory.contains(Items.BEER_1917, 3)) {
                    player("I have some beer here, fancy one?").also { stage = 10 }
                }
                npc(FaceAnim.HALF_GUILTY, "Hi, I'm Joe, door guard for Lady Keli.")
            }

            60, 100 -> npc("Halt! Who goes there? Friend or foe?")
            else -> npc(FaceAnim.HALF_GUILTY, "Hi, I'm Joe, door guard for Lady Keli.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (quest!!.getStage(player) > 50) {
            when (stage) {
                0 -> player("Hi friend, I am just checking out things here.").also { stage = 1 }
                1 ->
                    npc(
                        "The Prince got away, I am in trouble. I better not talk",
                        "to you, they are not sure I was drunk.",
                    ).also { stage = 2 }

                2 -> end()
            }
            return true
        }
        if (stage >= 10 && quest!!.getStage(player) == 40) {
            when (stage) {
                10 -> npc("Ah, that would be lovely, just one now,", "just to wet my throat.").also { stage++ }
                11 -> player("Of course, it must be tough being here without a drink.").also { stage++ }
                12 -> sendDialogue(player, "You hand a beer to the guard, he drinks it in seconds.").also { stage++ }
                13 ->
                    if (!removeItem(player, Item(Items.BEER_1917))) {
                        end()
                    } else {
                        npc("That was perfect, I can't thank you enough.").also { stage++ }
                    }

                14 -> player("How are you? Still ok? Not too drunk?").also { stage++ }
                15 -> player("Would you care for another, my friend?").also { stage++ }
                16 -> npc("I better not, I don't want to be drunk on duty.").also { stage++ }
                17 -> player("Here, just keep these for later,", "I hate to see a thirsty guard.").also { stage++ }
                18 ->
                    if (player.inventory.remove(BEER) && player.inventory.remove(BEER)) {
                        sendDialogue(
                            player,
                            "You hand two more beers to the guard. He takes a sip of one, and then he drinks the both.",
                        )
                        stage = 19
                        setAttribute(player, "/save:guard-drunk", true)
                    }

                19 ->
                    npc(
                        "Franksh, that wash just what I need to shtay on guard.",
                        "No more beersh, I don't want to get drunk.",
                    ).also { stage++ }

                20 -> sendDialogue(player, "The guard is drunk, and no longer a problem.").also { stage = END_DIALOGUE }
                23 -> player("Hello friend, I am just rescuing the prince, is that ok?").also { stage++ }
                24 ->
                    npc("Thatsh a funny joke. You are lucky I am shober. Go", "in peace, friend.").also {
                        stage = END_DIALOGUE
                    }
            }
            return true
        }
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Hi, who are you guarding here?").also { stage++ }
            1 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Can't say, all very secret. You should get out of here.",
                    "I am not suposed to talk while I guard.",
                ).also { stage++ }

            2 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return JoeGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JOE_916)
    }

    companion object {
        private val BEER = Item(Items.BEER_1917)
    }
}
