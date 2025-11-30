package content.global.travel.eagle

import core.api.*
import core.game.activity.Cutscene
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.*

class EagleTransportPlugin : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.NPC, Items.ROPE_954, *TRANSPORT_EAGLES) { player, used, with ->
            val npc = with.asNpc()

            if (!removeItem(player, used.asItem())) {
                sendMessage(player, "You need a rope.")
                return@onUseWith true
            }
            lock(player, 10)
            player.animate(Animation(Animations.WRANGLING_EAGLE_WITH_ROPE_5210))
            playGlobalAudio(player.location, 2622)
            eagleTransport(player, npc, npc.location.regionId)
            return@onUseWith true
        }
    }

    companion object {
        val DESERT_EAGLE  = NPCs.EAGLE_5121
        val JUNGLE_EAGLE  = NPCs.EAGLE_5122
        val POLAR_EAGLE   = NPCs.EAGLE_5123
        val KARAMJA_EAGLE = NPCs.KARAMJAN_JUNGLE_EAGLE_6384

        val TRANSPORT_EAGLES =
            intArrayOf(
                NPCs.KARAMJAN_JUNGLE_EAGLE_6385,
                NPCs.DESERT_EAGLE_5130,
                NPCs.JUNGLE_EAGLE_5131,
                NPCs.POLAR_EAGLE_5132
            )

        private fun eagleTransport(player: Player, npc: NPC, region: Int) {
            queueScript(player, 1, QueueStrength.SOFT) { stage ->
                when (stage) {
                    0 -> {
                        openInterface(player, Components.FADE_TO_BLACK_120)
                        return@queueScript delayScript(player, 6)
                    }
                    1 -> {
                        openInterface(player, Components.FADE_FROM_BLACK_170)
                        teleport(player, eagleDestination(npc.id, region))
                        return@queueScript delayScript(player, 6)
                    }
                    2 -> return@queueScript stopExecuting(player)
                    else -> return@queueScript stopExecuting(player)
                }
            }
        }

        private fun eagleDestination(npcId: Int, region: Int): Location {
            if (region != 8013) return Location.create(2024, 4961, 3)

            return when (npcId) {
                5131 -> Location.create(2520, 9322, 0)
                5132 -> Location.create(2727, 10217, 0)
                6385 -> Location.create(2890, 2981, 3)
                else -> Location.create(3423, 9570, 0)
            }
        }
    }
}

private class DesertEagleCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location.create(2024, 4961, 3))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        addNPC(NPCs.DESERT_EAGLE_5130, 12, 20, Direction.SOUTH)
        loadRegion(Regions.UZER_DESERT_EYRIE_ENTRANCE_13617)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }
            1 -> {
                fadeFromBlack()
                teleport(player, 12, 21)
                moveCamera(13, 31, 500)
                timedUpdate(1)
            }
            2 -> {
                //move(player, 14, 15)
                player.animate(Animation(Animations.FLYING_BEHIND_EAGLE_CUTSCENE_5211))
                //move(getNPC(NPCs.DESERT_EAGLE_5130)!!, 14, 15)
                rotateCamera(14, 15, 500, 1)
                moveCamera(14, 15, 500, 1)
                timedUpdate(6)
            }
            3 -> {
                //move(getNPC(NPCs.DESERT_EAGLE_5130)!!, 14, 15)
                rotateCamera(12, 18, 500, 1)
                player.animate(Animation(Animations.FLYING_BEHIND_EAGLE_CUTSCENE_5211))
                timedUpdate(3)
            }
            4 -> end {}
        }
    }
}

private class JungleEagleCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location.create(2024, 4961, 3))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        addNPC(NPCs.JUNGLE_EAGLE_5131, 14, 42, Direction.EAST)
        loadRegion(Regions.FELDIP_HILLS_EYRIE_ENTRANCE_10029)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }
            1 -> {
                fadeFromBlack()
                teleport(player, 18, 44)
                moveCamera(12, 44, 500)
                player.animate(Animation(Animations.FLYING_BEHIND_EAGLE_CUTSCENE_5211))
                timedUpdate(1)
            }
            2 -> {
                //move(player, 13, 43)
                player.animate(Animation(Animations.FLYING_BEHIND_EAGLE_CUTSCENE_5211))
                //move(getNPC(NPCs.JUNGLE_EAGLE_5131)!!, 14, 43)
                rotateCamera(14, 15, 500, 1)
                moveCamera(14, 15, 500, 1)
                timedUpdate(6)
            }
            3 -> {
                //move(getNPC(NPCs.JUNGLE_EAGLE_5131)!!, 14, 15)
                rotateCamera(17, 44, 500, 1)
                player.animate(Animation(Animations.FLYING_BEHIND_EAGLE_CUTSCENE_5211))
                timedUpdate(3)
            }
            4 -> end {}
        }
    }
}

private class EaglePeakCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location.create(2024, 4961, 3))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        addNPC(5122, 28, 33, Direction.EAST)
        loadRegion(Regions.EAGLES_PEAK_EYRIE_ENTRANCE_9270)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }
            1 -> {
                fadeFromBlack()
                teleport(player, 27, 33)
                moveCamera(23, 33, 500)
                player.animate(Animation(Animations.FLYING_BEHIND_EAGLE_CUTSCENE_5211))
                timedUpdate(1)
            }
            2 -> {
                player.animate(Animation(Animations.FLYING_BEHIND_EAGLE_CUTSCENE_5211))
                rotateCamera(14, 15, 500, 1)
                moveCamera(14, 15, 500, 1)
                timedUpdate(6)
            }
            3 -> {
                rotateCamera(17, 44, 500, 1)
                player.animate(Animation(Animations.FLYING_BEHIND_EAGLE_CUTSCENE_5211))
                timedUpdate(3)
            }
            4 -> end {}
        }
    }
}

private class PolarEagleCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location.create(2024, 4961, 3))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        addNPC(5122, 50, 58, Direction.WEST)
        loadRegion(Regions.RELLEKKA_EYRIE_ENTRANCE_10811)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }
            1 -> {
                fadeFromBlack()
                teleport(player, 53, 58)
                moveCamera(51, 58, 500)
                player.animate(Animation(Animations.FLYING_BEHIND_EAGLE_CUTSCENE_5211))
                timedUpdate(1)
            }
            2 -> {
                player.animate(Animation(Animations.FLYING_BEHIND_EAGLE_CUTSCENE_5211))
                rotateCamera(54, 58, 500, 1)
                moveCamera(54, 58, 500, 1)
                timedUpdate(6)
            }
            3 -> {
                rotateCamera(57, 58, 500, 1)
                player.animate(Animation(Animations.FLYING_BEHIND_EAGLE_CUTSCENE_5211))
                timedUpdate(3)
            }
            4 -> end {}
        }
    }
}
