package content.region.karamja.taibwo.quest.junglepotion.plugin

import content.global.skill.herblore.HerbItem
import core.api.addItem
import core.api.replaceScenery
import core.api.sendItemDialogue
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Scenery

/**
 * Enum represents a search objective in the Jungle Potion quest.
 */
enum class JungleObjective(val objectId: Int, val herb: HerbItem, val stage: Int, val clue: Array<String>) {
    JUNGLE_VINE(Scenery.MARSHY_JUNGLE_VINE_2575, HerbItem.SNAKE_WEED, 10, arrayOf("It grows near vines in an area to the south west where", "the ground turns soft and the water kisses your feet.")) {
        override fun search(player: Player, scenery: Node) {
            val anim = Animation.create(Animations.SEARCH_FOR_SNAKEWEED_JUNGLE_POTION_2094)
            player.animate(anim)
            player.pulseManager.run(object : Pulse(anim.duration, player, scenery) {
                override fun pulse(): Boolean {
                    if (RandomFunction.random(3) == 1) {
                        player.sendMessage("You search the vine...")
                        replaceScenery(scenery.asScenery())
                        reward(player)
                        return true
                    }
                    player.animate(anim)
                    return false
                }
            })
        }
    },
    PALM_TREE(Scenery.PALM_TREE_2577, HerbItem.ARDRIGAL, 20, arrayOf("You are looking for Ardrigal. It is related to the palm", "and grows in its brothers shady profusion.")),
    SITO_FOIL(Scenery.SCORCHED_EARTH_2579, HerbItem.SITO_FOIL, 30, arrayOf("You are looking for Sito Foil, and it grows best where", "the ground has been blackened by the living flame.")),
    VOLENCIA_MOSS(Scenery.ROCK_2581, HerbItem.VOLENCIA_MOSS, 40, arrayOf("You are looking for Volencia Moss. It clings to rocks", "for its existence. It is difficult to see, so you must", "search for it well.")),
    ROGUES_PURSE(Scenery.FUNGUS_COVERED_CAVERN_WALL_32106, HerbItem.ROGUES_PUSE, 50, arrayOf("It inhabits the darkness of the underground, and grows", "in the caverns to the north. A secret entrance to the", "caverns is set into the northern cliffs, be careful Bwana.")) {
        override fun search(player: Player, scenery: Node) {
            val anim = Animation.create(Animations.SEARCH_WALL_JUNGLE_POTION_2097)
            player.animate(anim)
            player.pulseManager.run(object : Pulse(anim.duration, player, scenery) {
                override fun pulse(): Boolean {
                    if (RandomFunction.random(4) == 1) {
                        replaceScenery(scenery.asScenery())
                        reward(player)
                        return true
                    }
                    player.animate(anim, 1)
                    return false
                }
            })
        }
    };

    open fun search(player: Player, scenery: Node) {
        reward(player)
        replaceScenery(scenery.asScenery())
    }

    protected fun replaceScenery(scenery: Node) {
        if (scenery.isActive) {
            replaceScenery(scenery.asScenery(), scenery.id + 1, 80)
        }
    }

    protected fun reward(player: Player) {
        addItem(player, herb.herb.id)
        sendItemDialogue(player, herb.herb, "You find a grimy herb.")
    }

    companion object {
        private val BY_ID = values().associateBy { it.objectId }
        private val BY_STAGE = values().associateBy { it.stage }

        fun forId(id: Int): JungleObjective? = BY_ID[id]
        fun forStage(stage: Int): JungleObjective? = BY_STAGE[stage]
    }
}