package content.global.skill.crafting.weaving

import core.api.repositionChild
import core.api.submitIndividualPulse
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Components

/**
 * Handles weaving crafting.
 */
class WeavingListener : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles interaction with weaves.
         */

        on(IntType.SCENERY, "weave") { player, node ->
            object : SkillDialogueHandler(player,
                SkillDialogue.THREE_OPTION, Weaving.SACK.product, Weaving.BASKET.product, Weaving.CLOTH.product) {
                override fun create(amount: Int, index: Int) {
                    submitIndividualPulse(
                        entity = player,
                        pulse = WeavingPulse(player, node.asScenery(), Weaving.values()[index], amount)
                    )
                }
            }
                .open()
            repositionChild(player, Components.SKILL_MAKE_304, 2, 56, 32)
            repositionChild(player, Components.SKILL_MAKE_304, 3, 207, 32)
            return@on true
        }
    }

}