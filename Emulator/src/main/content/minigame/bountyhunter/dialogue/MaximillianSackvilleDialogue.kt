package content.minigame.bountyhunter.dialogue

import core.api.*
import core.api.interaction.hasAwaitingGrandExchangeCollections
import core.api.interaction.openBankAccount
import core.api.interaction.openBankPinSettings
import core.api.interaction.openGrandExchangeCollectionBox
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

class MaximillianSackvilleDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE ->
                when {
                    hasIronmanRestriction(player, IronmanMode.ULTIMATE) -> {
                        npcl(
                            FaceAnim.NEUTRAL,
                            "My apologies, dear ${if (player.isMale) "sir" else "madam"}, " +
                                "our services are not available for Ultimate ${if (player.isMale) "Ironman" else "Ironwoman"}.",
                        ).also { stage = END_DIALOGUE }
                    }

                    else -> {
                        npcl(FaceAnim.NEUTRAL, "Good day, how may I help you?").also {
                            if (hasAwaitingGrandExchangeCollections(player)) {
                                stage++
                            } else {
                                stage += 2
                            }
                        }
                    }
                }

            1 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Before we go any further, I should inform you that you " +
                        "have items ready for collection from the Grand Exchange.",
                ).also { stage++ }

            2 -> playerl(FaceAnim.ASKING, "Who are you?").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "How inconsiderate of me, dear ${if (player.isMale) "sir" else "madam"}. " +
                        "My name is Maximillian Sackville and I conduct operations here on behalf " +
                        "of The Bank of Gielinor.",
                ).also { stage++ }

            4 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "I'd like to access my bank account.", 10),
                    Topic(FaceAnim.NEUTRAL, "I'd like to check my PIN settings.", 12),
                    Topic(FaceAnim.NEUTRAL, "I'd like to collect items.", 13),
                    Topic(FaceAnim.ASKING, "Aren't you afraid of working in the Wilderness?", 5),
                )

            5 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "While the Wilderness is quite a dangerous place, The Bank of Gielinor offers " +
                        "us - roving bankers - extraordinary benefits for our hard work in hazardous environments.",
                ).also { stage++ }

            6 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "This allows us to provide our services to customers regardless of their current " +
                        "whereabouts. Our desire to serve is stronger than our fear of the Wilderness.",
                ).also {
                    stage =
                        END_DIALOGUE
                }

            10 -> {
                openBankAccount(player)
                end()
            }

            12 -> {
                openBankPinSettings(player)
                end()
            }

            13 -> {
                openGrandExchangeCollectionBox(player)
                end()
            }
        }
        return true
    }

    override fun getIds() = intArrayOf(NPCs.BANKER_6538)
}
