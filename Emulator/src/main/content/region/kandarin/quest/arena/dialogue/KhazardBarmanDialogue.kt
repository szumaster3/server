package content.region.kandarin.quest.arena.dialogue

import core.api.addItem
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.removeItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.Items.COINS_995
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class KhazardBarmanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        if (getQuestStage(player!!, Quests.FIGHT_ARENA) in 50..100) {
            when (stage) {
                0 -> playerl(FaceAnim.HAPPY, "Hello.").also { stage++ }
                1 ->
                    npcl(
                        FaceAnim.FRIENDLY,
                        "Hi, what can I get you? We have a range of quality brews.",
                    ).also { stage++ }

                2 ->
                    options(
                        "I'll have a beer please.",
                        "I'd like a Khali brew please.",
                        "Got any news?",
                    ).also { stage++ }
                3 ->
                    when (buttonID) {
                        1 -> playerl(FaceAnim.NEUTRAL, "I'll have a beer please.").also { stage = 4 }
                        2 -> playerl(FaceAnim.NEUTRAL, "I'd like a Khali brew please.").also { stage = 7 }
                        3 -> playerl(FaceAnim.ASKING, "Got any news?").also { stage = 5 }
                    }

                4 -> npcl(FaceAnim.FRIENDLY, "There you go, that's two gold coins.").also { stage = 8 }
                5 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "If you want to see the action around here you should visit the famous Khazard fight arena. I've seen some grand battles in my time. Ogres, goblins, even dragons have fought there.",
                    ).also {
                        stage++
                    }
                6 ->
                    npcl(
                        FaceAnim.WORRIED,
                        "Although you have to feel sorry for some of the slaves sent in there.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                7 ->
                    npcl(
                        FaceAnim.FRIENDLY,
                        "There you go, that's five gold coins. I suggest lying down before you drink it. That way you have less distance to collapse.",
                    ).also {
                        stage =
                            9
                    }
                8 -> {
                    if (removeItem(player!!, Item(COINS_995, 2))) {
                        end()
                        addItem(player!!, Items.BEER_1917, 1)
                        stage = END_DIALOGUE
                    } else {
                        end()
                        playerl(FaceAnim.STRUGGLE, "Oh, I don't have enough money with me.").also {
                            stage = END_DIALOGUE
                        }
                    }
                }

                9 ->
                    if (!removeItem(player!!, Item(COINS_995, 5))) {
                        end()
                        playerl(FaceAnim.STRUGGLE, "Oh, I don't have enough money with me.").also {
                            stage = END_DIALOGUE
                        }
                    } else {
                        end()
                        addItem(player!!, Items.KHALI_BREW_77, 1)
                        setQuestStage(player!!, Quests.FIGHT_ARENA, 60)
                    }
            }
        } else {
            when (stage) {
                0 -> playerl(FaceAnim.HAPPY, "Hello. I'll have a beer please.").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "There you go, that's two gold coins.").also { stage++ }
                2 -> {
                    end()
                    if (!removeItem(player!!, Item(COINS_995, 2))) {
                        playerl(FaceAnim.STRUGGLE, "Oh, I don't have enough money with me.")
                    } else {
                        addItem(player!!, Items.BEER_1917, 1)
                    }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KHAZARD_BARMAN_259)
    }
}
