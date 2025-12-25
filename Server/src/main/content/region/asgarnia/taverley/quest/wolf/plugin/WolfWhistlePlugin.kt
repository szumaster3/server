package content.region.asgarnia.taverley.quest.wolf.plugin

import content.data.GameAttributes
import content.region.asgarnia.taverley.quest.wolf.dialogue.StikkletrixDialogue
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Animations
import shared.consts.Quests
import shared.consts.Scenery

class WolfWhistlePlugin : InteractionListener {

    override fun defineListeners() {
        on(Scenery.DEAD_BODY_28586, IntType.SCENERY, "search") { player, _ ->
            if (!getAttribute(player, GameAttributes.WOLF_WHISTLE_STIKKLEBRIX, false) && !isQuestComplete(player, Quests.WOLF_WHISTLE)) {
                openDialogue(player, StikkletrixDialogue())
            } else {
                animate(player, Animations.HUMAN_BURYING_BONES_827)
                sendDialogue(player, "You rifle the pockets of the dead druid and find nothing.")
            }
            return@on true
        }
    }
}
