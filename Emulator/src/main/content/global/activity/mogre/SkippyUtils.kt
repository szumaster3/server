package content.global.activity.mogre

import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Animations
import org.rs.consts.Vars

object SkippyUtils {
    const val SKIPPY_VARBIT = Vars.VARBIT_MINI_QUEST_MOGRE_AND_SKIPPY_1344
    const val ANIMATION_THROW = Animations.THROW_385
    const val ANIMATION_THROW_BUCKET = Animations.USE_BUCKET_ALT_4255
    val TUTORIAL_ISLAND: ZoneBorders = ZoneBorders.forRegion(12336)
    val PORT_SARIM: ZoneBorders = ZoneBorders.forRegion(11825)
}
