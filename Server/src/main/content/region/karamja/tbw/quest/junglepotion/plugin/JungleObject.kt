package content.region.karamja.tbw.quest.junglepotion.plugin

import core.api.*
import core.game.interaction.Clocks
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Scenery
import shared.consts.Items

/**
 * Represents a search objects in the Jungle Potion quest.
 */
enum class JungleObject(val objectId: Int, private val herbId: Int, val productId: Int, val stage: Int, val clue: String, private val animationId: Int = Animations.HUMAN_SEARCH_BUSHES_800) {
    JUNGLE_VINE(Scenery.MARSHY_JUNGLE_VINE_2575, Items.GRIMY_SNAKE_WEED_1525, Items.CLEAN_SNAKE_WEED_1526, 10, "It grows near vines in an area to the south west where the ground turns soft and the water kisses your feet.", Animations.SEARCH_FOR_SNAKEWEED_JUNGLE_POTION_2094),
    PALM_TREE(Scenery.PALM_TREE_2577, Items.GRIMY_ARDRIGAL_1527, Items.CLEAN_ARDRIGAL_1528, 20, "You are looking for Ardrigal. It is related to the palm and grows in its brothers shady profusion."),
    SITO_FOIL(Scenery.SCORCHED_EARTH_2579, Items.GRIMY_SITO_FOIL_1529, Items.CLEAN_SITO_FOIL_1530, 30, "You are looking for Sito Foil, and it grows best where the ground has been blackened by the living flame."),
    VOLENCIA_MOSS(Scenery.ROCK_2581, Items.GRIMY_VOLENCIA_MOSS_1531, Items.CLEAN_VOLENCIA_MOSS_1532, 40, "You are looking for Volencia Moss. It clings to rocks for its existence. It is difficult to see, so you must search for it well."),
    ROGUES_PURSE(Scenery.FUNGUS_COVERED_CAVERN_WALL_32106, Items.GRIMY_ROGUES_PURSE_1533, Items.CLEAN_ROGUES_PURSE_1534, 50, "It inhabits the darkness of the underground, and grows in the caverns to the north. A secret entrance to the caverns is set into the northern cliffs, be careful Bwana.", Animations.SEARCH_WALL_JUNGLE_POTION_2097);

    /**
     * Handle the search for the object.
     */
    fun search(player: Player, scenery: core.game.node.scenery.Scenery) {
        val anim = Animation.create(animationId)
        val successChance = if (scenery.id == Scenery.FUNGUS_COVERED_CAVERN_WALL_32106) 4 else 3

        queueScript(player, 1, QueueStrength.WEAK)
        { stage ->
            if (!clockReady(player, Clocks.SKILLING))
                return@queueScript stopExecuting(player)

            if (freeSlots(player) < 1) {
                sendMessage(player,"You don't have enough inventory space.")
                return@queueScript stopExecuting(player)
            }

            when (stage) {
                0 -> {
                    player.animate(anim)
                    delayClock(player, Clocks.SKILLING, 2)
                    delayScript(player, 2)
                }

                else -> {
                    sendMessage(player, "You search the area...")
                    if (RandomFunction.random(successChance) == 1)
                    {
                        if (scenery.isActive)
                        {
                            replaceScenery(scenery.asScenery(), scenery.id + 1, 80)
                        }
                        addItem(player, herbId)
                        sendItemDialogue(player, herbId, "You find a grimy herb.")
                        return@queueScript stopExecuting(player)
                    }

                    setCurrentScriptState(player, 0)
                    delayScript(player, 2)
                }
            }
        }
    }

    companion object {
        private val BY_ID = values().associateBy { it.objectId }
        private val BY_STAGE = values().associateBy { it.stage }

        fun forId(id: Int): JungleObject? = BY_ID[id]
        fun forStage(stage: Int): JungleObject? = BY_STAGE[stage]
    }
}
