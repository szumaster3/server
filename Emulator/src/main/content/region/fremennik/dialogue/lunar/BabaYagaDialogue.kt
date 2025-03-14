package content.region.fremennik.dialogue.lunar

import content.data.GameAttributes
import content.region.kandarin.quest.swept.handlers.SweptUtils
import core.api.*
import core.api.interaction.openNpcShop
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class BabaYagaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, Quests.SWEPT_AWAY) &&
            inInventory(player, Items.BROOMSTICK_14057) &&
            getDynLevel(player, Skills.MAGIC) >= 73 &&
            getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_BETTY_ENCH, false)
        ) {
            options("Talk about brooms.", "Talk about something else...")
            stage = 20
        }
        player(FaceAnim.FRIENDLY, "Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.ASKING, "Ah, a stranger to our island. How can I help?").also { stage++ }
            1 ->
                options(
                    "Have you got anything to trade?",
                    "It's a very interesting house you have here.",
                    "I'm good thanks, bye.",
                ).also {
                    stage++
                }

            2 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.BABA_YAGA_4513)
                    }
                    2 ->
                        playerl(
                            FaceAnim.ASKING,
                            "It's a very interesting house you have here. Does he have a name?",
                        ).also {
                            stage =
                                10
                        }
                    3 -> end()
                }

            10 -> npc(FaceAnim.FRIENDLY, "Why of course. It's Berty.").also { stage++ }
            11 -> player(FaceAnim.THINKING, "Berty? Berty the Chicken leg house?").also { stage++ }
            12 -> npc(FaceAnim.LAUGH, "Yes.").also { stage++ }
            13 -> player(FaceAnim.ASKING, "May I ask why?").also { stage++ }
            14 ->
                npcl(
                    FaceAnim.LAUGH,
                    "It just has that certain ring to it, don't you think? Beeerteeee!",
                ).also { stage++ }
            15 -> player(FaceAnim.HALF_WORRIED, "You're ins...").also { stage++ }
            16 -> npc("Insane? Very.").also { stage = END_DIALOGUE }
            20 ->
                when (buttonId) {
                    1 -> player("Could you enchant this broom for me?").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "Hello there.").also { stage = 0 }
                }
            21 ->
                npc(
                    "Ah, this looks like Maggie's broom. Of course I can",
                    "enchant it for you. Just one moment.",
                ).also {
                    stage++
                }
            22 -> {
                end()
                lock(player, 1)
                visualize(player, -1, SweptUtils.BROOM_ENCHANTMENT_GFX)
                removeAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_BETTY_ENCH)
                sendDoubleItemDialogue(player, -1, Items.BROOMSTICK_14057, "You receive 10,338 Magic experience.")
                rewardXP(player, Skills.MAGIC, 10338.0)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BabaYagaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BABA_YAGA_4513)
    }
}
