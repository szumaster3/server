package content.global.skill.crafting

import core.api.*
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Sounds

class WeavingPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles interaction with weaves.
         */

        on(IntType.SCENERY, "weave") { player, node ->
            object : SkillDialogueHandler(player, SkillDialogue.THREE_OPTION, CraftingDefinition.Weaving.SACK.product, CraftingDefinition.Weaving.BASKET.product, CraftingDefinition.Weaving.CLOTH.product) {
                override fun create(amount: Int, index: Int) {

                    val type = CraftingDefinition.Weaving.values()[index]
                    val required = type.required
                    val product = type.product
                    var remaining = amount

                    queueScript(player, 0, QueueStrength.NORMAL) {
                        if (remaining <= 0) return@queueScript stopExecuting(player)

                        if (getStatLevel(player, Skills.CRAFTING) < type.level) {
                            sendMessage(player, "You need a Crafting level of at least ${type.level} to do this.")
                            return@queueScript stopExecuting(player)
                        }

                        if (!inInventory(player, required.id, required.amount)) {
                            val reqName = required.name.lowercase().replace("ball", "balls")
                            val suffix = when (type) {
                                CraftingDefinition.Weaving.SACK -> "s"
                                CraftingDefinition.Weaving.CLOTH -> ""
                                else -> "es"
                            }
                            val article = if (product.name.lowercase().matches(Regex("^[aeiou].*"))) "an" else "a"
                            sendMessage(player, "You need ${required.amount} ${reqName}${suffix} to weave $article ${product.name.lowercase()}.")
                            return@queueScript stopExecuting(player)
                        }

                        if (!clockReady(player, Clocks.SKILLING)) return@queueScript stopExecuting(player)

                        animate(player, Animations.PULLING_ROPE_2270)
                        playAudio(player, Sounds.LOOM_WEAVE_2587)
                        delayClock(player, Clocks.SKILLING, 5)

                        if (removeItem(player, required)) {
                            addItem(player, product.id)
                            rewardXP(player, Skills.CRAFTING, type.experience)

                            val reqName = required.name.lowercase().replace("ball", "balls")
                            val suffix = when (type) {
                                CraftingDefinition.Weaving.SACK -> "s"
                                CraftingDefinition.Weaving.CLOTH -> ""
                                else -> "es"
                            }
                            val article = if (product.name.lowercase().matches(Regex("^[aeiou].*"))) "an" else "a"
                            sendMessage(player, "You weave the ${reqName}${suffix} into $article ${product.name.lowercase()}.")

                            // Falador diary.
                            if (type == CraftingDefinition.Weaving.BASKET &&
                                node.id == 8717 &&
                                withinDistance(player, Location(3039, 3287, 0)) &&
                                !hasDiaryTaskComplete(player, DiaryType.FALADOR, 1, 0)
                            ) {
                                finishDiaryTask(player, DiaryType.FALADOR, 1, 0)
                            }
                        }

                        remaining--

                        if (remaining > 0 && inInventory(player, required.id, required.amount)) {
                            delayClock(player, Clocks.SKILLING, 5)
                            setCurrentScriptState(player, 0)
                            delayScript(player, 5)
                        } else {
                            stopExecuting(player)
                        }
                    }
                }
            }.open()
            repositionChild(player, Components.SKILL_MAKE_304, 2, 56, 32)
            repositionChild(player, Components.SKILL_MAKE_304, 3, 207, 32)
            return@on true
        }
    }
}
