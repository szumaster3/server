package core.game.activity

import core.ServerConstants
import core.api.*
import core.api.ui.closeDialogue
import core.api.ui.setMinimapState
import core.api.utils.CameraShakeType
import core.api.utils.PlayerCamera
import core.game.component.Component
import core.game.dialogue.FaceAnim
import core.game.event.EventHook
import core.game.event.SelfDeathEvent
import core.game.interaction.MovementPulse
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.system.timer.impl.AntiMacro
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.Region
import core.game.world.map.RegionManager
import core.game.world.map.build.DynamicRegion
import core.game.world.map.path.Pathfinder
import core.tools.Log
import org.rs.consts.Components

abstract class Cutscene(
    val player: Player,
) {
    lateinit var region: Region
    lateinit var base: Location
    var exitLocation: Location = player.location.transform(0, 0, 0)
    var ended = false

    val camera = PlayerCamera(player)
    private val addedNPCs = HashMap<Int, ArrayList<NPC>>()

    abstract fun setup()

    abstract fun runStage(stage: Int)

    fun loadRegion(regionId: Int) {
        clearNPCs()
        logCutscene("Creating new instance of region $regionId for ${player.username}.")
        region = DynamicRegion.create(regionId)
        logCutscene("Dynamic region instantiated for ${player.username}. Global coordinates: ${region.baseLocation}.")
        base = region.baseLocation
    }

    fun fadeToBlack() {
        logCutscene("Fading ${player.username}'s screen to black.")
        player.interfaceManager.closeOverlay()
        player.interfaceManager.openOverlay(Component(Components.FADE_TO_BLACK_120))
    }

    fun fadeFromBlack() {
        logCutscene("Fading ${player.username}'s screen from black to normal.")
        player.interfaceManager.closeOverlay()
        player.interfaceManager.openOverlay(Component(Components.FADE_FROM_BLACK_170))
    }

    fun teleport(
        entity: Entity,
        regionX: Int,
        regionY: Int,
        plane: Int = 0,
    ) {
        val newLoc = base.transform(regionX, regionY, plane)
        logCutscene(
            "Teleporting ${entity.username} to coordinates: LOCAL[$regionX,$regionY,$plane] GLOBAL[${newLoc.x},${newLoc.y},$plane].",
        )
        entity.properties.teleportLocation = newLoc
    }

    fun move(
        entity: Entity,
        regionX: Int,
        regionY: Int,
    ) {
        logCutscene("Moving ${entity.username} to LOCAL[$regionX,$regionY].")
        entity.pulseManager.run(
            object : MovementPulse(entity, base.transform(regionX, regionY, 0), Pathfinder.SMART) {
                override fun pulse(): Boolean {
                    return true
                }
            },
        )
    }

    fun dialogueUpdate(
        npcId: Int,
        expression: FaceAnim,
        message: String,
        onContinue: () -> Unit = { incrementStage() },
        hide: Boolean = false,
    ) {
        logCutscene("Sending NPC dialogue update.")
        sendNPCDialogue(player, npcId, message, expression)
        player.dialogueInterpreter.addAction { _, _ -> onContinue.invoke() }
    }

    fun dialogueLinesUpdate(
        npcId: Int,
        expression: FaceAnim,
        vararg message: String,
        onContinue: () -> Unit = { incrementStage() },
    ) {
        logCutscene("Sending NPC dialogue lines update.")
        sendNPCDialogueLines(player, npcId, expression, true, *message)
        player.dialogueInterpreter.addAction { _, _ -> onContinue.invoke() }
    }

    fun dialogueClose() {
        logCutscene("Sending dialogue close.")
        closeDialogue(player)
    }

    fun dialogueUpdate(
        message: String,
        onContinue: () -> Unit = { incrementStage() },
    ) {
        logCutscene("Sending standard dialogue update.")
        sendDialogue(player, message)
        player.dialogueInterpreter.addAction { _, _ -> onContinue.invoke() }
    }

    fun playerDialogueUpdate(
        expression: FaceAnim,
        message: String,
        onContinue: () -> Unit = { incrementStage() },
    ) {
        logCutscene("Sending player dialogue update")
        sendPlayerDialogue(player, message, expression)
        player.dialogueInterpreter.addAction { _, _ -> onContinue.invoke() }
    }

    fun timedUpdate(
        ticks: Int,
        newStage: Int = -1,
    ) {
        logCutscene("Executing timed updated for $ticks ticks.")
        GameWorld.Pulser.submit(
            object : Pulse(ticks) {
                override fun pulse(): Boolean {
                    if (newStage == -1) {
                        incrementStage()
                    } else {
                        updateStage(newStage)
                    }
                    return true
                }
            },
        )
    }

    fun getNPC(id: Int): NPC? {
        return addedNPCs[id]?.firstOrNull()
    }

    fun getNPCs(id: Int): ArrayList<NPC> {
        return addedNPCs[id] ?: ArrayList()
    }

    fun getObject(
        regionX: Int,
        regionY: Int,
        plane: Int = 0,
    ): Scenery? {
        val obj = RegionManager.getObject(base.transform(regionX, regionY, plane))
        logCutscene("Retrieving object at LOCAL[$regionX,$regionY], GOT: ${obj?.definition?.name ?: "null"}.")
        return obj
    }

    fun addNPC(
        id: Int,
        regionX: Int,
        regionY: Int,
        direction: Direction,
        plane: Int = 0,
    ) {
        val npc = NPC(id)
        npc.isRespawn = false
        npc.isAggressive = false
        npc.isWalks = false
        npc.location = base.transform(regionX, regionY, plane)
        npc.init()
        npc.faceLocation(npc.location.transform(direction))

        val npcs = addedNPCs[id] ?: ArrayList()
        npcs.add(npc)
        addedNPCs[id] = npcs
        logCutscene(
            "Added NPC $id at location LOCAL[$regionX,$regionY,$plane] GLOBAL[${npc.location.x},${npc.location.y},$plane]",
        )
    }

    fun start() {
        start(true)
    }

    fun start(hideMiniMap: Boolean) {
        logCutscene("Starting cutscene for ${player.username}.")
        region = RegionManager.forId(player.location.regionId)
        base = RegionManager.forId(player.location.regionId).baseLocation
        setup()
        if (hideMiniMap) {
            setMinimapState(player, 2)
        }
        runStage(player.getCutsceneStage())
        setAttribute(player, ATTRIBUTE_CUTSCENE, this)
        setAttribute(player, ATTRIBUTE_CUTSCENE_STAGE, 0)
        player.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
        player.properties.isSafeZone = true
        player.properties.safeRespawn = player.location
        player.lock()
        player.hook(Event.SelfDeath, CUTSCENE_DEATH_HOOK)
        player.logoutListeners["cutscene"] = { player ->
            player.location = exitLocation
            player.getCutscene()?.end()
        }
        AntiMacro.pause(player)
    }

    fun end(
        fade: Boolean = true,
        endActions: (() -> Unit)? = null,
    ) {
        ended = true
        if (fade) fadeToBlack()
        GameWorld.Pulser.submit(
            object : Pulse() {
                var tick: Int = 0

                override fun pulse(): Boolean {
                    if (fade) {
                        when (tick++) {
                            8 -> player.properties.teleportLocation = exitLocation
                            9 -> fadeFromBlack()
                            16 -> return true
                            else -> return false
                        }
                    } else {
                        player.properties.teleportLocation = exitLocation
                    }
                    return true
                }

                override fun stop() {
                    super.stop()
                    player ?: return
                    player.removeAttribute(ATTRIBUTE_CUTSCENE)
                    player.removeAttribute(ATTRIBUTE_CUTSCENE_STAGE)
                    player.properties.isSafeZone = false
                    player.properties.safeRespawn = ServerConstants.HOME_LOCATION
                    player.interfaceManager.restoreTabs()
                    player.unlock()
                    clearNPCs()
                    player.unhook(CUTSCENE_DEATH_HOOK)
                    player.logoutListeners.remove("cutscene")
                    AntiMacro.unpause(player)
                    setMinimapState(player, 0)
                    try {
                        endActions?.invoke()
                    } catch (e: Exception) {
                        log(
                            this::class.java,
                            Log.ERR,
                            "There's some bad nasty code in ${this::class.java.simpleName} end actions!",
                        )
                        e.printStackTrace()
                    }
                }
            },
        )
    }

    fun endWithoutFade(endActions: (() -> Unit)? = null) {
        ended = true
        GameWorld.Pulser.submit(
            object : Pulse() {
                var tick: Int = 0

                override fun pulse(): Boolean {
                    when (tick++) {
                        0 -> player.properties.teleportLocation = exitLocation
                        1 -> {
                            return true
                        }
                    }
                    return false
                }

                override fun stop() {
                    super.stop()
                    player
                    player.removeAttribute(ATTRIBUTE_CUTSCENE)
                    player.removeAttribute(ATTRIBUTE_CUTSCENE_STAGE)
                    player.properties.isSafeZone = false
                    player.properties.safeRespawn = ServerConstants.HOME_LOCATION
                    player.interfaceManager.restoreTabs()
                    player.unlock()
                    clearNPCs()
                    player.unhook(CUTSCENE_DEATH_HOOK)
                    player.logoutListeners.remove("cutscene")
                    AntiMacro.unpause(player)
                    setMinimapState(player, 0)
                    try {
                        endActions?.invoke()
                    } catch (e: Exception) {
                        log(
                            this::class.java,
                            Log.ERR,
                            "There's some bad nasty code in ${this::class.java.simpleName} end actions!",
                        )
                        e.printStackTrace()
                    }
                }
            },
        )
    }

    fun end(endActions: (() -> Unit)? = null) {
        ended = true
        fadeToBlack()
        GameWorld.Pulser.submit(
            object : Pulse() {
                var tick: Int = 0

                override fun pulse(): Boolean {
                    when (tick++) {
                        8 -> player.properties.teleportLocation = exitLocation
                        9 -> fadeFromBlack()
                        16 -> {
                            return true
                        }
                    }
                    return false
                }

                override fun stop() {
                    super.stop()
                    player
                    player.removeAttribute(ATTRIBUTE_CUTSCENE)
                    player.removeAttribute(ATTRIBUTE_CUTSCENE_STAGE)
                    player.properties.isSafeZone = false
                    player.properties.safeRespawn = ServerConstants.HOME_LOCATION
                    player.interfaceManager.restoreTabs()
                    player.unlock()
                    clearNPCs()
                    player.unhook(CUTSCENE_DEATH_HOOK)
                    player.logoutListeners.remove("cutscene")
                    AntiMacro.unpause(player)
                    setMinimapState(player, 0)
                    try {
                        endActions?.invoke()
                    } catch (e: Exception) {
                        log(
                            this::class.java,
                            Log.ERR,
                            "There's some bad nasty code in ${this::class.java.simpleName} end actions!",
                        )
                        e.printStackTrace()
                    }
                }
            },
        )
    }

    fun moveCamera(
        regionX: Int,
        regionY: Int,
        height: Int = 300,
        speed: Int = 100,
    ) {
        val globalLoc = base.transform(regionX, regionY, 0)
        camera.panTo(globalLoc.x, globalLoc.y, height, speed)
    }

    fun rotateCamera(
        regionX: Int,
        regionY: Int,
        height: Int = 300,
        speed: Int = 100,
    ) {
        val globalLoc = base.transform(regionX, regionY, 0)
        camera.rotateTo(globalLoc.x, globalLoc.y, height, speed)
    }

    fun rotateCameraBy(
        diffX: Int,
        diffY: Int,
        diffHeight: Int = 300,
        diffSpeed: Int = 100,
    ) {
        camera.rotateBy(diffX, diffY, diffHeight, diffSpeed)
    }

    fun shakeCamera(
        cameraType: CameraShakeType,
        jitter: Int = 0,
        amplitude: Int = 0,
        frequency: Int = 128,
        speed: Int = 2,
    ) {
        camera.shake(cameraType.ordinal, jitter, amplitude, frequency, speed)
    }

    fun resetCamera() {
        camera.reset()
    }

    fun setExit(location: Location) {
        exitLocation = location
    }

    private fun loadCurrentStage() {
        if (ended) return
        runStage(player.getCutsceneStage())
    }

    fun incrementStage() {
        setAttribute(player, ATTRIBUTE_CUTSCENE_STAGE, player.getCutsceneStage() + 1)
        loadCurrentStage()
    }

    fun updateStage(newStage: Int) {
        setAttribute(player, ATTRIBUTE_CUTSCENE_STAGE, newStage)
        loadCurrentStage()
    }

    fun logCutscene(message: String) {
        if (ServerConstants.LOG_CUTSCENE) {
            log(this::class.java, Log.FINE, "$message")
        }
    }

    fun clearNPCs() {
        for (entry in addedNPCs.entries) {
            logCutscene("Clearing ${entry.value.size} NPCs with ID ${entry.key} for ${player.username}.")
            for (npc in entry.value) npc.clear()
        }
        addedNPCs.clear()
    }

    companion object {
        const val ATTRIBUTE_CUTSCENE = "cutscene"
        const val ATTRIBUTE_CUTSCENE_STAGE = "cutscene:stage"

        object CUTSCENE_DEATH_HOOK : EventHook<SelfDeathEvent> {
            override fun process(
                entity: Entity,
                event: SelfDeathEvent,
            ) {
                if (entity !is Player) return
                entity.getCutscene()?.end() ?: entity.unhook(this)
            }
        }
    }
}
