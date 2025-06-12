package content.region.kandarin.ardougne.east.quest.biohazard.plugin

import core.game.world.map.Location

object BiohazardUtils {
    const val FIRST_VIAL_CORRECT = "/save:biohazard-vial:1"
    const val SECOND_VIAL_CORRECT = "/save:biohazard-vial:2"
    const val THIRD_VIAL_CORRECT = "/save:biohazard-vial:3"

    const val FIRST_VIAL_WRONG = "/save:biohazard-vial:4"
    const val SECOND_VIAL_WRONG = "/save:biohazard-vial:5"
    const val THIRD_VIAL_WRONG = "/save:biohazard-vial:6"

    const val ELENA_REPLACE = "/save:biohazard-items"
    const val FEED_ON_FENCE = "/save:biohazard:fed-fence"

    val FENCE_CORNER_LOCATION = Location(2563, 3301, 0)
    val WATCHTOWER_CORNER_LOCATION = Location(2561, 3303, 0)
}
