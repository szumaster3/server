package core.game.interaction

import core.api.*
import core.api.event.isStunned
import core.api.movement.finishedMoving
import core.game.bots.AIPlayer
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.scenery.Scenery
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.tools.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Integer.max

/**
 * Manages the execution and queueing of scripts for an entity, processing interactions and movements.
 *
 * @property entity The [Player] or [NPC] that this processor is managing.
 */
class ScriptProcessor(
    val entity: Entity,
) {
    private var apScript: Script<*>? = null
    private var opScript: Script<*>? = null
    private var interactTarget: Node? = null
    private var currentScript: Script<*>? = null
    private val queue = ArrayList<Script<*>>()

    var delay = 0
    var interacted = false
    var apRangeCalled = false
    var apRange = 10
    var persistent = false
    var targetDestination: Location? = null

    /**
     * Pre-movement function that processes the interaction scripts and queues for the entity.
     * Checks for stun status, delay, and interaction with the target.
     */
    fun preMovement() {
        var allSkipped = false
        while (!allSkipped) {
            allSkipped = processQueue()
        }

        if (isStunned(entity)) return
        if (entity.delayed()) return

        var canProcess = !entity.delayed()
        if (entity is Player && entity !is AIPlayer) {
            canProcess = canProcess && !entity.hasModalOpen()
        }

        if (entity !is Player) return
        if (!entity.delayed() && canProcess && interactTarget != null) {
            if (opScript != null && inOperableDistance()) {
                face(entity, interactTarget?.getFaceLocation(entity.location) ?: return reset())
                processInteractScript(opScript ?: return reset())
            } else if (apScript != null && inApproachDistance(apScript ?: return reset())) {
                face(entity, interactTarget?.getFaceLocation(entity.location) ?: return reset())
                processInteractScript(apScript ?: return reset())
            } else if (apScript == null && opScript == null && inOperableDistance()) {
                sendMessage(entity, "Nothing interesting happens.")
            }
        }
    }

    /**
     * Post-movement function that processes interaction scripts and checks if the entity moved.
     * Resets interaction states and checks operable distances for interaction.
     *
     * @param didMove Flag indicating if the entity moved.
     */
    fun postMovement(didMove: Boolean) {
        if (didMove) {
            entity.clocks[Clocks.MOVEMENT] = GameWorld.ticks + if (entity.walkingQueue.isRunning) 0 else 1
        }
        var canProcess = !entity.delayed()
        if (entity is Player && entity !is AIPlayer) {
            canProcess = canProcess && !entity.interfaceManager.isOpened && !entity.interfaceManager.hasChatbox()
        }

        if (entity !is Player) return
        if (!entity.delayed() && canProcess && interactTarget != null && !interacted) {
            if (opScript != null && inOperableDistance()) {
                face(entity, interactTarget?.centerLocation ?: return reset())
                processInteractScript(opScript ?: return reset())
            } else if (apScript != null && inApproachDistance(apScript ?: return reset())) {
                face(entity, interactTarget?.centerLocation ?: return reset())
                processInteractScript(apScript ?: return reset())
            } else if (apScript == null && opScript == null && inOperableDistance()) {
                sendMessage(entity, "Nothing interesting happens.")
            }
        }
        if (canProcess && (apScript != null || opScript != null)) {
            if (!interacted && !didMove && finishedMoving(entity)) {
                sendMessage(entity, "I can't reach that!")
                reset()
            }
        }
        if (interacted && !apRangeCalled && !persistent) reset()
        if (interactTarget != null && interactTarget?.isActive != true) reset()
    }

    /**
     * Processes any queued scripts that are ready to be executed.
     * Executes scripts in order of priority based on their type and timing.
     *
     * @return Returns `true` if all scripts were skipped, `false` otherwise.
     */
    fun processQueue(): Boolean {
        var strongInQueue = false
        var softInQueue = false
        var anyExecuted = false
        strongInQueue = hasTypeInQueue(QueueStrength.STRONG)
        softInQueue = hasTypeInQueue(QueueStrength.SOFT)

        if (strongInQueue) {
            if (entity is Player) {
                closeAllInterfaces(entity)
            }
        }

        if (strongInQueue) {
            removeWeakScripts()
        }

        val toRemove = ArrayList<Script<*>>()

        for (i in 0 until queue.size) {
            when (val script = queue[i]) {
                is QueuedScript -> {
                    if (entity.delayed() && script.strength != QueueStrength.SOFT) {
                        continue
                    }
                    if (script.nextExecution > GameWorld.ticks) {
                        continue
                    }
                    if ((script.strength == QueueStrength.STRONG) && entity is Player) {
                        closeAllInterfaces(entity)
                    }
                    script.nextExecution = GameWorld.ticks + 1
                    val finished = executeScript(script)
                    script.state++
                    if (finished && !script.persist) {
                        toRemove.add(script)
                    } else if (finished) {
                        script.state = 0
                    }
                    anyExecuted = true
                }
                is QueuedUseWith -> {
                    if (entity.delayed() && script.strength != QueueStrength.SOFT) {
                        continue
                    }
                    if (entity !is Player) {
                        toRemove.add(script)
                        log(this::class.java, Log.ERR, "Tried to queue an item UseWith interaction for a non-player!")
                        continue
                    }
                    if (script.nextExecution > GameWorld.ticks) {
                        continue
                    }
                    if ((script.strength == QueueStrength.STRONG)) {
                        closeAllInterfaces(entity)
                    }
                    script.nextExecution = GameWorld.ticks + 1
                    val finished = executeScript(script)
                    script.state++
                    if (finished && !script.persist) {
                        toRemove.add(script)
                    } else if (finished) {
                        script.state = 0
                    }
                    anyExecuted = true
                }
            }
        }

        queue.removeAll(toRemove.toSet())
        return !anyExecuted
    }

    /**
     * Determines if a script is persistent (i.e., should continue even if the entity moves or other conditions occur).
     *
     * @param script The script to check for persistence.
     * @return Returns `true` if the script is persistent, `false` otherwise.
     */
    fun isPersist(script: Script<*>): Boolean {
        return script.persist
    }

    /**
     * Processes interaction scripts when the entity is within range and ready to interact.
     *
     * @param script The interaction script to execute.
     */
    fun processInteractScript(script: Script<*>) {
        if (interactTarget == null || !interactTarget!!.isActive) {
            log(this::class.java, Log.FINE, "Interact target $interactTarget no longer active, cancelling interaction.")
            reset()
        }
        if (script.nextExecution < GameWorld.ticks) {
            val finished = executeScript(script)
            script.state++
            if (finished && isPersist(script)) {
                script.state = 0
            }
            interacted = true
        }
    }

    /**
     * Executes a given script.
     *
     * @param script The script to execute.
     * @return Returns `true` if the script finished execution, `false` otherwise.
     */
    fun executeScript(script: Script<*>): Boolean {
        currentScript = script
        try {
            when (script) {
                is Interaction -> return script.execution.invoke(
                    entity as? Player ?: return true,
                    interactTarget ?: return true,
                    script.state,
                )

                is UseWithInteraction -> return script.execution.invoke(
                    entity as? Player ?: return true,
                    script.used,
                    script.with,
                    script.state,
                )

                is QueuedScript -> return script.execution.invoke(script.state)
                is QueuedUseWith -> return script.execution.invoke(
                    entity as? Player ?: return true,
                    script.used,
                    script.with,
                    script.state,
                )
            }
        } catch (e: Exception) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            e.printStackTrace(pw)
            log(
                this::class.java,
                Log.ERR,
                "Error processing ${script::class.java.simpleName} - stopping the script. Exception follows: $sw",
            )
            reset()
        }
        currentScript = null
        return true
    }

    /**
     * Removes weak scripts from the queue.
     */
    fun removeWeakScripts() {
        queue.removeAll(
            queue
                .filter {
                    it is QueuedScript &&
                        it.strength == QueueStrength.WEAK ||
                        it is QueuedUseWith &&
                        it.strength == QueueStrength.WEAK
                }.toSet(),
        )
    }

    /**
     * Removes normal priority scripts from the queue.
     */
    fun removeNormalScripts() {
        queue.removeAll(
            queue
                .filter {
                    it is QueuedScript &&
                        it.strength == QueueStrength.NORMAL ||
                        it is QueuedUseWith &&
                        it.strength == QueueStrength.NORMAL
                }.toSet(),
        )
    }

    /**
     * Checks if the entity is within the approach distance to the target for an interaction.
     *
     * @param script The script to check for distance.
     * @return Returns `true` if the entity is within approach distance to the target, `false` otherwise.
     */
    fun inApproachDistance(script: Script<*>): Boolean {
        val distance =
            when (script) {
                is Interaction -> script.distance
                is UseWithInteraction -> script.distance
                else -> 10
            }
        targetDestination?.let {
            return it.location.getDistance(entity.location) <= distance && hasLineOfSight(entity, it)
        }
        return false
    }

    /**
     * Checks if the entity is within operable distance to the target for interaction.
     *
     * @return Returns `true` if the entity is within operable distance, `false` otherwise.
     */
    fun inOperableDistance(): Boolean {
        targetDestination?.let {
            return it.cardinalTiles.any { loc -> loc == entity.location } && hasLineOfSight(entity, it)
        }
        return false
    }

    /**
     * Resets the state of the script processor, clearing active scripts and interactions.
     */
    fun reset() {
        apScript = null
        opScript = null
        currentScript = null
        apRangeCalled = false
        interacted = false
        apRange = 10
        interactTarget = null
        persistent = false
        targetDestination = null
        resetAnimator(entity as? Player ?: return)
    }

    /**
     * Sets a new interaction script with the target, adjusting ranges and persistent states as needed.
     *
     * @param target The target node the entity is interacting with.
     * @param script The script to associate with the interaction.
     */
    fun setInteractionScript(
        target: Node,
        script: Script<*>?,
    ) {
        if (apScript != null && script != null && script.execution == apScript!!.execution) return
        if (opScript != null && script != null && script.execution == opScript!!.execution) return
        reset()
        interactTarget = target
        if (script != null) {
            apRange =
                when (script) {
                    is Interaction -> script.distance
                    is UseWithInteraction -> script.distance
                    else -> 10
                }
            persistent = script.persist
            if (apRange == -1) {
                opScript = script
            } else {
                apScript = script
            }
            targetDestination =
                when (interactTarget) {
                    is NPC -> DestinationFlag.ENTITY.getDestination(entity, interactTarget)
                    is Scenery -> {
                        val basicPath = Pathfinder.find(entity, interactTarget)
                        val path = basicPath.points.lastOrNull()
                        if (basicPath.isMoveNear) {
                            target.location
                            return
                        }
                        if (path == null) {
                            clearScripts(entity)
                            return
                        }
                        Location.create(path.x, path.y, entity.location.z)
                    }

                    is GroundItem -> DestinationFlag.ITEM.getDestination(entity, interactTarget)
                    else -> target.location
                }
        }
    }

    /**
     * Adds a script to the queue for future execution with a specified strength.
     *
     * @param script The script to add to the queue.
     * @param strength The priority strength of the script.
     */
    fun addToQueue(
        script: Script<*>,
        strength: QueueStrength,
    ) {
        if (script !is QueuedScript && script !is QueuedUseWith) {
            log(
                this::class.java,
                Log.ERR,
                "Tried to queue ${script::class.java.simpleName} as a queueable script but it's not!",
            )
            return
        }
        if (strength == QueueStrength.STRONG && entity is Player) {
            entity.interfaceManager.close()
            entity.interfaceManager.closeChatbox()
            entity.dialogueInterpreter.close()
        }
        script.nextExecution = max(GameWorld.ticks + 1, script.nextExecution)
        queue.add(script)
    }

    /**
     * Gets the active script being processed, either the current interaction or a queued script.
     *
     * @return The currently active script or null if no script is active.
     */
    fun getActiveScript(): Script<*>? {
        return currentScript ?: getActiveInteraction()
    }

    private fun getActiveInteraction(): Script<*>? {
        return opScript ?: apScript
    }

    /**
     * Checks if a specific type of script is present in the queue.
     *
     * @param type The strength of the queue to check.
     * @return Returns `true` if a script of the specified type is in the queue, `false` otherwise.
     */
    fun hasTypeInQueue(type: QueueStrength): Boolean {
        for (script in queue) {
            if (script is QueuedScript && script.strength == type) {
                return true
            } else if (script is QueuedUseWith && script.strength == type) {
                return true
            }
        }
        return false
    }
}
