package content.region.fremennik.quest.viking.dialogue

import content.region.fremennik.dialogue.YrsaDiaryDialogue
import core.api.*
import core.api.interaction.openNpcShop
import core.api.quest.isQuestComplete
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class YrsaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (inInventory(player, Items.FISCAL_STATEMENT_3708, 1)) {
            playerl(
                FaceAnim.HAPPY,
                "Hello. Can I have those boots now? Here is a written statement from Brundt outlining future tax burdens upon Fremennik merchants and shopkeepers for the year.",
            )
            stage = 15
            return true
        } else if (inInventory(player, Items.STURDY_BOOTS_3700, 1)) {
            playerl(FaceAnim.ASKING, "Hey, these shoes look pretty comfy. Think you could make me a pair like them?")
            stage = 20
            return true
        } else if (getAttribute(player, "sigmundreturning", false)) {
            playerl(FaceAnim.ASKING, "I have this trade item but I can't remember who it's for.")
            stage = 25
            return true
        }
        if (getAttribute(player, "sigmund-steps", 0) == 4) {
            playerl(
                FaceAnim.ASKING,
                "I don't suppose you have any idea where I could find a guarantee of a reduction on sales taxes, do you?",
            )
            stage = 10
            return true
        } else if (getAttribute(player, "sigmund-steps", 0) == 3) {
            playerl(
                FaceAnim.ASKING,
                "I don't suppose you have any idea where I could find some custom sturdy boots, do you?",
            )
            stage = 1
            return true
        } else if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npcl(
                FaceAnim.HAPPY,
                "Welcome to my clothes shop. I can change your shoes, or I've got a fine selection of clothes for sale.",
            )
            stage = 30
            return true
        } else {
            playerl(FaceAnim.HAPPY, "Hello!")
            stage = 35
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val hasBoots = player!!.equipment.get(EquipmentContainer.SLOT_FEET)
        when (stage) {
            1 ->
                npcl(
                    FaceAnim.THINKING,
                    "Well, I don't usually have many shoes in stock here in my little clothes shop... I will be able to make you up a pair if you are really desperate though?",
                ).also { stage++ }

            2 -> playerl(FaceAnim.NEUTRAL, "They're not for me... I need them for Olaf.").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.ANGRY,
                    "Oh, that foolish bard... Why didn't he just ask me to make him some? It is his stupid pride, I believe! I will tell you what I will do outlander.",
                ).also { stage++ }

            4 ->
                npcl(
                    FaceAnim.ASKING,
                    "I know that you must have the ear of the chieftain for him to consider you as worthy of becoming a Fremennik by trial.",
                ).also { stage++ }

            5 ->
                npcl(
                    FaceAnim.HAPPY,
                    "I will make you a pair of sturdy boots for Olaf if you will persuade him to reduce the sales tax placed upon all Fremennik shopkeepers. It does nothing but hurt my business now.",
                ).also { stage++ }

            6 ->
                playerl(FaceAnim.NEUTRAL, "Okay, I will see what I can do").also {
                    player.incrementAttribute("sigmund-steps", 1)
                    stage = END_DIALOGUE
                }

            10 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Yes I do outerlander. Only the Chieftain may permit such a thing. Talk to him.",
                ).also { stage = END_DIALOGUE }

            15 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Certainly! Let me have a look at what he has written here, just give me a moment...",
                ).also {
                    removeItem(player, Items.FISCAL_STATEMENT_3708)
                    addItemOrDrop(player, Items.STURDY_BOOTS_3700, 1)
                    stage++
                }

            16 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Yes, that all appears in order. Tell Olaf to come to me next time for shoes!",
                ).also { stage = END_DIALOGUE }

            20 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Maybe if you pass your trial and become a full fledged member of the Fremennik...",
                ).also { stage = END_DIALOGUE }

            25 -> npcl(FaceAnim.ANNOYED, "Not me, I'm afraid.").also { stage = 1000 }
            30 ->
                if (player!!.achievementDiaryManager.getDiary(DiaryType.FREMENNIK)!!.isStarted(0)) {
                    options(
                        "I'd like to buy some clothes",
                        "I'd like to change my shoes",
                        "I have question about my Achievement Diary",
                        "Nothing, thanks.",
                    ).also { stage++ }
                } else {
                    options(
                        "I'd like to buy some clothes",
                        "I'd like to change my shoes",
                        "About the Achievement Diary...",
                        "Nothing, thanks.",
                    ).also { stage++ }
                }

            31 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HAPPY, "I'd like to buy some clothes").also { stage = 36 }
                    2 ->
                        npc(
                            FaceAnim.HAPPY,
                            "It costs 500 coins to change your shoe colour. Just",
                            "select what colour you would like from this catalogue",
                            "and then give me the 500 coins.",
                        ).also { stage = 33 }

                    3 -> openDialogue(player, YrsaDiaryDialogue()).also { stage = END_DIALOGUE }
                    4 -> playerl(FaceAnim.HAPPY, "Nothing, thanks.").also { stage++ }
                }

            32 -> npcl(FaceAnim.HAPPY, "As you wish.").also { stage = END_DIALOGUE }
            33 -> {
                end()
                if (player.equipment.isEmpty) {
                    openInterface(player, Components.YRSA_SHOE_STORE_200)
                } else if (hasBoots != null) {
                    npc("You'll have to take what you're wearing on your feet off", "first.")
                } else {
                    npc(
                        "I don't feel comfortable helping you try on shoes when",
                        "you are wielding something. Please remove what you",
                        "are holding first.",
                    )
                }
            }

            35 ->
                npcl(
                    FaceAnim.HAPPY,
                    "I'm sorry outerlander, I cannot sell you any clothes. Our customs forbid it.",
                ).also { stage = END_DIALOGUE }

            36 -> {
                end()
                openNpcShop(player, NPCs.YRSA_1301)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.YRSA_1301)
    }
}
