package content.minigame.blastfurnace.plugin

import core.api.animateScenery
import core.api.getScenery
import core.api.replaceScenery
import core.game.world.map.Location
import shared.consts.Scenery

class BFSceneryController {
    fun updateBreakable(
        potPipeBroken: Boolean,
        pumpPipeBroken: Boolean,
        beltBroken: Boolean,
        cogBroken: Boolean,
    ) {
        val beltObj = getScenery(beltGearRight)!!
        val gearObj = getScenery(cogRightLoc)!!
        val potPipe = getScenery(potPipeLoc)!!
        val pumpPipe = getScenery(pumpPipeLoc)!!

        if (potPipeBroken && potPipe.id != BROKEN_POT_PIPE) {
            replaceScenery(potPipe, BROKEN_POT_PIPE, -1)
        } else if (!potPipeBroken && potPipe.id == BROKEN_POT_PIPE) {
            replaceScenery(potPipe, DEFAULT_POT_PIPE, -1)
        }

        if (pumpPipeBroken && pumpPipe.id != BROKEN_PUMP_PIPE) {
            replaceScenery(pumpPipe, BROKEN_PUMP_PIPE, -1)
        } else if (!pumpPipeBroken && pumpPipe.id == BROKEN_PUMP_PIPE) {
            replaceScenery(pumpPipe, DEFAULT_PUMP_PIPE, -1)
        }

        if (beltBroken && beltObj.id != BROKEN_BELT) {
            replaceScenery(beltObj, BROKEN_BELT, -1)
        } else if (!beltBroken && beltObj.id == BROKEN_BELT) {
            replaceScenery(beltObj, DEFAULT_BELT, -1)
        }

        if (cogBroken && gearObj.id != BROKEN_COG) {
            replaceScenery(gearObj, BROKEN_COG, -1)
        } else if (!cogBroken && gearObj.id == BROKEN_COG) {
            replaceScenery(gearObj, DEFAULT_COG, -1)
        }
    }

    fun updateAnimations(
        pedaling: Boolean,
        beltBroken: Boolean,
        cogBroken: Boolean,
    ) {
        val belt1 = getScenery(belt1Loc)!!
        val belt2 = getScenery(belt2Loc)!!
        val belt3 = getScenery(belt3Loc)!!
        val beltGearLeft = getScenery(beltGearLeft)!!
        val beltGearRight = getScenery(beltGearRight)!!
        val cogLeft = getScenery(cogLeftLoc)!!
        val cogRight = getScenery(cogRightLoc)!!
        val cogCenter = getScenery(centralGearLoc)!!

        val beltAnim = if (pedaling && !beltBroken && !cogBroken) BELT_ANIM else -1
        val gearAnim = if (pedaling && !beltBroken && !cogBroken) GEAR_ANIM else -1

        animateScenery(belt1, beltAnim)
        animateScenery(belt2, beltAnim)
        animateScenery(belt3, beltAnim)
        animateScenery(beltGearLeft, gearAnim)
        animateScenery(beltGearRight, gearAnim)
        animateScenery(cogLeft, gearAnim)
        animateScenery(cogRight, gearAnim)
        animateScenery(cogCenter, gearAnim)
    }

    fun updateStove(temp: Int) {
        val stoveObj = getScenery(stoveLoc)!!

        if (temp >= 67 && stoveObj.id != STOVE_HOT) {
            replaceScenery(stoveObj, STOVE_HOT, -1)
        } else if (temp in 34..66 && stoveObj.id != STOVE_WARM) {
            replaceScenery(stoveObj, STOVE_WARM, -1)
        } else if (temp in 0..33 && stoveObj.id != STOVE_COLD) {
            replaceScenery(stoveObj, STOVE_COLD, -1)
        }
    }

    fun resetAllScenery() {
        val beltObj = getScenery(beltGearRight)!!
        val gearObj = getScenery(cogRightLoc)!!
        val potPipe = getScenery(potPipeLoc)!!
        val pumpPipe = getScenery(pumpPipeLoc)!!
        val stoveObj = getScenery(stoveLoc)!!

        replaceScenery(gearObj, DEFAULT_COG, -1)
        replaceScenery(beltObj, DEFAULT_BELT, -1)
        replaceScenery(pumpPipe, DEFAULT_PUMP_PIPE, -1)
        replaceScenery(potPipe, DEFAULT_POT_PIPE, -1)
        replaceScenery(stoveObj, STOVE_COLD, -1)
    }

    companion object {
        val belt1Loc = Location(1943, 4967, 0)
        val belt2Loc = Location(1943, 4966, 0)
        val belt3Loc = Location(1943, 4965, 0)
        var potPipeLoc = Location(1943, 4961, 0)
        var pumpPipeLoc = Location(1947, 4961, 0)
        var cogLeftLoc = Location(1945, 4965, 0)
        var cogRightLoc = Location(1945, 4967, 0)
        var beltGearLeft = Location(1944, 4965, 0)
        var beltGearRight = Location(1944, 4967, 0)
        var centralGearLoc = Location(1945, 4966, 0)
        var stoveLoc = Location(1948, 4963, 0)

        const val DEFAULT_BELT = Scenery.DRIVE_BELT_9102
        const val BROKEN_BELT = Scenery.DRIVE_BELT_9103
        const val DEFAULT_COG = Scenery.COGS_9104
        const val BROKEN_COG = Scenery.COGS_9105
        const val DEFAULT_POT_PIPE = Scenery.PIPES_9116
        const val BROKEN_POT_PIPE = Scenery.PIPES_9117
        const val DEFAULT_PUMP_PIPE = Scenery.PIPES_9120
        const val BROKEN_PUMP_PIPE = Scenery.PIPES_9121
        const val STOVE_COLD = Scenery.STOVE_9085
        const val STOVE_WARM = Scenery.STOVE_9086
        const val STOVE_HOT = Scenery.STOVE_9087
        const val BELT_ANIM = 2435
        const val GEAR_ANIM = 2436
    }
}
