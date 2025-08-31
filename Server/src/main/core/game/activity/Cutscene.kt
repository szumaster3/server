package core.game.activity

import core.ServerConstants
import core.api.*
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
import shared.consts.Components

/**
 * A utility class for making cutscenes.
 *
 * @author Ceikry
 */
abstract class Cutscene(val player: Player) {

    /**
     * The region in which the cutscene is taking place.
     */
    lateinit var region: Region

    /**
     * The base location for the cutscene, typically used for positioning NPCs and other elements.
     */
    lateinit var base: Location

    /**
     * The location to which the player will be teleported when the cutscene ends.
     */
    var exitLocation: Location = player.location.transform(0, 0, 0)

    /**
     * A flag indicating whether the cutscene has ended.
     */
    var ended = false

    /**
     * The camera used to control the player's view during the cutscene.
     */
    val camera = PlayerCamera(player)

    /**
     * A map that holds NPCs that have been added to the cutscene, identified by their NPC ID.
     */
    private val addedNPCs = HashMap<Int, ArrayList<NPC>>()

    /**
     * Sets up the cutscene. This method should be implemented by subclasses to define the specifics of the cutscene.
     */
    abstract fun setup()

    /**
     * Runs the specified stage of the cutscene.
     *
     * @param stage The stage to run.
     */
    abstract fun runStage(stage: Int)

    /**
     * Loads a new region for the cutscene based on the provided region ID.
     * Clears any previously added NPCs and logs the region creation.
     *
     * @param regionId The ID of the new region.
     */
    fun loadRegion(regionId: Int) {
        clearNPCs()
        logCutscene("Creating new instance of region $regionId for ${player.username}.")
        region = DynamicRegion.create(regionId)
        logCutscene("Dynamic region instantiated for ${player.username}. Global coordinates: ${region.baseLocation}.")
        base = region.baseLocation
    }

    /**
     * Fades the player's screen to black, typically used to signify the start of a cutscene.
     */
    fun fadeToBlack() {
        logCutscene("Fading ${player.username}'s screen to black.")
        player.interfaceManager.closeOverlay()
        player.interfaceManager.openOverlay(Component(Components.FADE_TO_BLACK_120))
    }

    /**
     * Fades the player's screen from black to normal.
     */
    fun fadeFromBlack() {
        logCutscene("Fading ${player.username}'s screen from black to normal.")
        player.interfaceManager.closeOverlay()
        player.interfaceManager.openOverlay(Component(Components.FADE_FROM_BLACK_170))
    }

    /**
     * Teleports an entity (player, NPC, etc.) to a specified location.
     *
     * @param entity The entity to teleport.
     * @param regionX The X-coordinate in the region.
     * @param regionY The Y-coordinate in the region.
     * @param plane The plane (height level) of the location. Defaults to 0.
     */
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

    /**
     * Moves an entity (player, NPC, etc.) to a specified location in the region.
     *
     * @param entity The entity to move.
     * @param regionX The X-coordinate in the region.
     * @param regionY The Y-coordinate in the region.
     */
    fun move(
        entity: Entity,
        regionX: Int,
        regionY: Int,
    ) {
        logCutscene("Moving ${entity.username} to LOCAL[$regionX,$regionY].")
        entity.pulseManager.run(
            object : MovementPulse(entity, base.transform(regionX, regionY, 0), Pathfinder.SMART) {
                override fun pulse(): Boolean = true
            },
        )
    }

    /**
     * Updates the dialogue of an NPC during the cutscene.
     *
     * @param npcId The ID of the NPC.
     * @param expression The facial expression of the NPC.
     * @param message The message to display.
     * @param onContinue The action to perform when the dialogue is continued.
     * @param hide Whether to hide the dialogue after it's shown.
     */
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

    /**
     * Updates the dialogue with multiple lines for an NPC during the cutscene.
     *
     * @param npcId The ID of the NPC.
     * @param expression The facial expression of the NPC.
     * @param message The messages to display.
     * @param onContinue The action to perform when the dialogue is continued.
     */
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

    /**
     * Closes the current dialogue.
     */
    fun dialogueClose() {
        logCutscene("Sending dialogue close.")
        closeDialogue(player)
    }

    /**
     * Updates the player's dialogue with a standard message during the cutscene.
     *
     * @param splitLines Split lines automatically. (Default: `false`)
     * @param messages The message to display.
     * @param onContinue The action to perform when the dialogue is continued.
     */
    fun dialogueUpdate(
        splitLines: Boolean = false,
        vararg messages: String,
        onContinue: () -> Unit = { incrementStage() }
    ) {
        logCutscene("Sending standard dialogue update. splitLines=$splitLines")

        val finalMessage = messages.joinToString(" ")

        if (splitLines) {
            sendDialogue(player, finalMessage)
        } else {
            player.dialogueInterpreter.sendDialogue(*messages)
        }

        player.dialogueInterpreter.addAction { _, _ -> onContinue.invoke() }
    }

    /**
     * Updates the player's dialogue with a message and a facial expression during the cutscene.
     *
     * @param expression The facial expression of the player.
     * @param message The message to display.
     * @param onContinue The action to perform when the dialogue is continued.
     */
    fun playerDialogueUpdate(
        expression: FaceAnim,
        message: String,
        onContinue: () -> Unit = { incrementStage() },
    ) {
        logCutscene("Sending player dialogue update")
        sendPlayerDialogue(player, message, expression)
        player.dialogueInterpreter.addAction { _, _ -> onContinue.invoke() }
    }

    /**
     * Executes a timed update that will trigger after a set number of ticks.
     *
     * @param ticks The number of ticks to wait before triggering the update.
     * @param newStage The new stage to move to after the update, or -1 to increment the stage.
     */
    fun timedUpdate(
        ticks: Int,
        newStage: Int = -1,
    ) {
        logCutscene("Executing timed update for $ticks ticks.")
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

    /**
     * Retrieves an NPC by its ID if it has been added to the cutscene.
     *
     * @param id The ID of the NPC.
     * @return The NPC, or null if it was not added.
     */
    fun getNPC(id: Int): NPC? = addedNPCs[id]?.firstOrNull()

    /**
     * Retrieves all NPCs of a given ID that have been added to the cutscene.
     *
     * @param id The ID of the NPC.
     * @return A list of NPCs, possibly empty.
     */
    fun getNPCs(id: Int): ArrayList<NPC> = addedNPCs[id] ?: ArrayList()

    /**
     * Retrieves an object in the cutscene by its coordinates.
     *
     * @param regionX The X-coordinate in the region.
     * @param regionY The Y-coordinate in the region.
     * @param plane The plane (height level) of the location. Defaults to 0.
     * @return The object at the specified location, or null if not found.
     */
    fun getObject(
        regionX: Int,
        regionY: Int,
        plane: Int = 0,
    ): Scenery? {
        val obj = RegionManager.getObject(base.transform(regionX, regionY, plane))
        logCutscene("Retrieving object at LOCAL[$regionX,$regionY], GOT: ${obj?.definition?.name ?: "null"}.")
        return obj
    }

    /**
     * Adds an NPC to the cutscene at the specified coordinates and direction.
     *
     * @param id The ID of the NPC.
     * @param regionX The X-coordinate in the region.
     * @param regionY The Y-coordinate in the region.
     * @param direction The direction the NPC should face.
     * @param plane The plane (height level) of the location. Defaults to 0.
     */
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

    /**
     * Starts the cutscene and hides the minimap if requested.
     *
     * @param hideMiniMap A flag indicating whether to hide the minimap during the cutscene.
     */
    fun start() {
        start(true)
    }

    /**
     * Starts the cutscene with the option to hide the minimap.
     *
     * @param hideMiniMap A flag indicating whether to hide the minimap during the cutscene.
     */
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
        player.logoutListeners["cutscene"] = { p -> onLogout(p) }
        AntiMacro.pause(player)
    }

    /**
     * Ends the cutscene with a fade effect and optional additional actions.
     *
     * @param fade Whether to include a fade effect when ending the cutscene.
     * @param endActions Additional actions to perform when the cutscene ends.
     */
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

    /**
     * Ends the cutscene without a fade effect and optional additional actions.
     *
     * @param endActions Additional actions to perform when the cutscene ends.
     */
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

    /**
     * Ends the cutscene, optionally with additional actions.
     *
     * @param endActions Additional actions to perform when the cutscene ends.
     */
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

    /**
     * Moves the camera to a specified location during the cutscene.
     *
     * @param regionX The X-coordinate in the region to move the camera to.
     * @param regionY The Y-coordinate in the region to move the camera to.
     * @param height The height (Z-axis) at which to set the camera. Defaults to 300.
     * @param speed The speed at which the camera will move. Defaults to 100.
     */
    fun moveCamera(
        regionX: Int,
        regionY: Int,
        height: Int = 300,
        speed: Int = 100,
    ) {
        val globalLoc = base.transform(regionX, regionY, 0)
        camera.panTo(globalLoc.x, globalLoc.y, height, speed)
    }

    /**
     * Rotates the camera to a specified location during the cutscene.
     *
     * @param regionX The X-coordinate in the region to rotate the camera to.
     * @param regionY The Y-coordinate in the region to rotate the camera to.
     * @param height The height (Z-axis) at which to set the camera. Defaults to 300.
     * @param speed The speed at which the camera will rotate. Defaults to 100.
     */
    fun rotateCamera(
        regionX: Int,
        regionY: Int,
        height: Int = 300,
        speed: Int = 100,
    ) {
        val globalLoc = base.transform(regionX, regionY, 0)
        camera.rotateTo(globalLoc.x, globalLoc.y, height, speed)
    }

    /**
     * Rotates the camera by a specified difference during the cutscene.
     *
     * @param diffX The difference in the X-axis to rotate the camera by.
     * @param diffY The difference in the Y-axis to rotate the camera by.
     * @param diffHeight The difference in the height (Z-axis) to rotate the camera by. Defaults to 300.
     * @param diffSpeed The speed at which to rotate the camera. Defaults to 100.
     */
    fun rotateCameraBy(
        diffX: Int,
        diffY: Int,
        diffHeight: Int = 300,
        diffSpeed: Int = 100,
    ) {
        camera.rotateBy(diffX, diffY, diffHeight, diffSpeed)
    }

    /**
     * Shakes the camera with a specified shake type and intensity.
     *
     * @param cameraType The type of camera shake to apply.
     * @param jitter The intensity of the shake's jitter. Defaults to 0.
     * @param amplitude The amplitude of the shake. Defaults to 0.
     * @param frequency The frequency of the shake. Defaults to 128.
     * @param speed The speed at which the shake occurs. Defaults to 2.
     */
    fun shakeCamera(
        cameraType: CameraShakeType,
        jitter: Int = 0,
        amplitude: Int = 0,
        frequency: Int = 128,
        speed: Int = 2,
    ) {
        camera.shake(cameraType.ordinal, jitter, amplitude, frequency, speed)
    }

    /**
     * Resets the camera to its original state.
     */
    fun resetCamera() {
        camera.reset()
    }

    /**
     * Sets the exit location of the cutscene.
     *
     * @param location The location to set as the exit.
     */
    fun setExit(location: Location) {
        exitLocation = location
    }

    /**
     * Sets the exit location for logout/disconnect.
     */
    open fun onLogout(p: Player) {
        p.location = exitLocation
        p.getCutscene()?.end()
    }

    /**
     * Loads the current stage of the cutscene and runs the appropriate actions.
     */
    private fun loadCurrentStage() {
        if (ended) return
        runStage(player.getCutsceneStage())
    }

    /**
     * Increments the current stage of the cutscene and loads the next stage.
     */
    fun incrementStage() {
        setAttribute(player, ATTRIBUTE_CUTSCENE_STAGE, player.getCutsceneStage() + 1)
        loadCurrentStage()
    }

    /**
     * Updates the stage to the specified stage and loads the new stage actions.
     *
     * @param newStage The stage number to update to.
     */
    fun updateStage(newStage: Int) {
        setAttribute(player, ATTRIBUTE_CUTSCENE_STAGE, newStage)
        loadCurrentStage()
    }

    /**
     * Logs a message related to the cutscene, if cutscene logging is enabled.
     *
     * @param message The message to log.
     */
    fun logCutscene(message: String) {
        if (ServerConstants.LOG_CUTSCENE) {
            log(this::class.java, Log.FINE, "$message")
        }
    }

    /**
     * Clears all NPCs associated with the cutscene.
     */
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
