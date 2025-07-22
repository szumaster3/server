package content.region.asgarnia.taverley.quest.wolf.plugin

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Scenery

class WolfWhistlePlugin : InteractionListener {
    override fun defineListeners() {
        on(Scenery.DEAD_BODY_28586, IntType.SCENERY, "search") { player, _ ->
            if (!getAttribute(player, GameAttributes.WOLF_WHISTLE_STIKKLEBRIX, false) && !isQuestComplete(
                    player, Quests.WOLF_WHISTLE
                )
            ) {
                openDialogue(player, StikkletrixDialogue())
            } else {
                animate(player, Animations.MULTI_BEND_OVER_827)
                sendDialogue(player, "You rifle the pockets of the dead druid and find nothing.")
            }
            return@on true
        }
    }

    class StikkletrixDialogue : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            when (stage) {
                0 -> player(FaceAnim.EXTREMELY_SHOCKED, "This must be Stikkletrix.").also { stage++ }
                1 -> player(FaceAnim.SAD, "The poor fool didn't stand a chance against the wolves.").also { stage++ }
                2 -> player(FaceAnim.NEUTRAL, "Let's see what he has in his pack: three unfinished", "Strength potions, two burnt lobsters, a guam leaf with a", "footprint on it...").also {
                    stage++
                }

                3 -> player(FaceAnim.NEUTRAL, "and some wolf bones!").also {
                    setAttribute(player!!, GameAttributes.WOLF_WHISTLE_STIKKLEBRIX, true)
                    addItemOrDrop(player!!, Items.WOLF_BONES_2859, 2)
                    stage++
                }

                4 -> player(FaceAnim.NEUTRAL, "It looks he got enough wolf bones for Pikkupstix and", "then died. How tragic!").also { stage++ }
                5 -> player(FaceAnim.NEUTRAL, "Well, his sacrifice will not be in vain.").also { stage = END_DIALOGUE }
            }
        }
    }
}
