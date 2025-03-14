package content.global.skill.cooking

import content.global.skill.cooking.data.Recipe
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.system.task.Pulse
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class CookingRecipe : UseWithHandler(*getAllowedNodes()) {
    companion object {
        private fun getAllowedNodes(): IntArray {
            return Recipe
                .values()
                .flatMap { recipe ->
                    recipe.parts.map { it.id } + recipe.base.id
                }.distinct()
                .toIntArray()
        }
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        Recipe.values().forEach { recipe ->
            recipe.ingredients.forEach { ingredient ->
                addHandler(ingredient.id, ITEM_TYPE, this)
            }
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val recipe = findMatchingRecipe(event) ?: return false

        val player = event.player
        val dialogueHandler =
            object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, recipe.product) {
                override fun create(
                    amount: Int,
                    index: Int,
                ) {
                    player.pulseManager.run(
                        object : Pulse(2) {
                            var count = 0

                            override fun pulse(): Boolean {
                                recipe.mix(player, event)
                                return ++count >= amount
                            }
                        },
                    )
                }

                override fun getAll(index: Int): Int {
                    return player.inventory.getAmount(recipe.base)
                }
            }

        if (player.inventory.getAmount(recipe.base) == 1) {
            recipe.mix(player, event)
        } else {
            dialogueHandler.open()
        }
        return true
    }

    private fun findMatchingRecipe(event: NodeUsageEvent): Recipe? {
        return Recipe.values().firstOrNull { recipe ->
            if (recipe.singular) {
                (recipe.base.id == event.usedItem.id || recipe.base.id == event.baseItem.id) &&
                    recipe.ingredients.any { it.id == event.usedItem.id || it.id == event.baseItem.id }
            } else {
                recipe.parts.any { part ->
                    recipe.ingredients.any { ingredient ->
                        (part.id == event.usedItem.id && ingredient.id == event.baseItem.id) ||
                            (part.id == event.baseItem.id && ingredient.id == event.usedItem.id)
                    }
                }
            }
        }
    }
}
