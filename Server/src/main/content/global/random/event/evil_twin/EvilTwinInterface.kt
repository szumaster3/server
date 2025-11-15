package content.global.random.event.evil_twin

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import shared.consts.Components
import shared.consts.Sounds

/**
 * Handles interfaces for Evil Twin random event.
 * @author Emperor
 */
class EvilTwinInterface : InterfaceListener {

    override fun defineInterfaceListeners() {

        /*
         * Listener for when the crane control interface is opened
         */

        onOpen(Components.CRANE_CONTROL_240) { player, _ ->
            player.lock()
            return@onOpen true
        }

        /*
         * Listener for when the crane control interface is closed
         */

        onClose(Components.CRANE_CONTROL_240) { player, _ ->
            player.unlock()
            return@onClose true
        }

        /*
         * Listener for button interactions within the crane control interface
         */

        on(Components.CRANE_CONTROL_240) { player, _, _, buttonID, _, _ ->
            if (EvilTwinUtils.success) return@on false
            when (buttonID) {
                28 -> {
                    EvilTwinUtils.success = false
                    for (npc in EvilTwinUtils.region.planes[0].npcs) {
                        if (npc.location == EvilTwinUtils.currentCrane!!.location) {
                            val evilTwin =
                                EvilTwinUtils.isEvilTwin(npc, player.getAttribute(EvilTwinUtils.RANDOM_EVENT, 0))
                            if (evilTwin) {
                                EvilTwinUtils.success = true
                                sendMessage(player, "You caught the Evil twin!")
                            } else {
                                sendMessage(player, "You caught an innocent civilian!")
                            }

                            npc.lock(10)
                            player.locks.lockComponent(15)
                            visualize(npc, 4001, 666)
                            EvilTwinUtils.updateCraneCam(player, 10, 4)
                            GameWorld.Pulser.submit(
                                object : Pulse(5, player) {
                                    var counter: Int = 0

                                    override fun pulse(): Boolean {
                                        when (counter++) {
                                            0 -> {
                                                animate(player, 4004)
                                                SceneryBuilder.remove(EvilTwinUtils.currentCrane)
                                                SceneryBuilder.add(
                                                    Scenery(66, EvilTwinUtils.currentCrane?.location, 22, 0)
                                                )
                                                npc.transform(npc.id + 20)
                                                npc.lock(20)
                                                npc.walkingQueue.reset()
                                                npc.walkingQueue.addPath(
                                                    EvilTwinUtils.region.baseLocation.x + 10,
                                                    EvilTwinUtils.region.baseLocation.y + 4
                                                )
                                                delay = npc.walkingQueue.queue.size + 1
                                                EvilTwinUtils.craneNPC = npc
                                            }
                                            1 -> {
                                                EvilTwinUtils.craneNPC = null
                                                playAudio(player, Sounds.TWIN_LOWER_CRANE_2272)
                                                animate(npc, Animation.create(4003), true)
                                                delay = 3
                                            }
                                            2 -> {
                                                npc.reTransform()
                                                npc.faceLocation(player.location)
                                                playAudio(player, Sounds.TWIN_CRANE_DROP_2271)
                                                if (evilTwin) {
                                                    playJingle(player, 101)
                                                    EvilTwinUtils.removeSuspects(player)
                                                    npc.animate(Animation.create(859))
                                                    runTask(player, 16) { npc.clear() }
                                                } else {
                                                    npc.sendChat("You're putting me in prison?!")
                                                }
                                                EvilTwinUtils.currentCrane =
                                                    EvilTwinUtils.currentCrane!!.transform(
                                                        EvilTwinUtils.currentCrane!!.id,
                                                        EvilTwinUtils.currentCrane!!.rotation,
                                                        EvilTwinUtils.region.baseLocation.transform(14, 12, 0)
                                                    )
                                                SceneryBuilder.add(
                                                    Scenery(14977, EvilTwinUtils.currentCrane?.location, 22, 0)
                                                )
                                                SceneryBuilder.add(EvilTwinUtils.currentCrane)
                                            }
                                            3 -> {
                                                EvilTwinUtils.updateCraneCam(player, 14, 12)
                                                if (evilTwin) {
                                                    player.interfaceManager.closeSingleTab()
                                                    openDialogue(player, MollyDialogue(0), EvilTwinUtils.mollyNPC!!)
                                                } else {
                                                    EvilTwinUtils.decreaseTries(player)
                                                }
                                                return true
                                            }
                                        }
                                        return false
                                    }
                                }
                            )

                            player.packetDispatch.sendSceneryAnimation(
                                EvilTwinUtils.currentCrane,
                                Animation.create(4000)
                            )
                            return@on true
                        }
                    }

                    EvilTwinUtils.decreaseTries(player)
                    player.packetDispatch.sendSceneryAnimation(EvilTwinUtils.currentCrane, Animation.create(4000))
                    return@on true
                }

                29 -> EvilTwinUtils.moveCrane(player, Direction.SOUTH)
                30 -> EvilTwinUtils.moveCrane(player, Direction.NORTH)
                31 -> EvilTwinUtils.moveCrane(player, Direction.EAST)
                32 -> EvilTwinUtils.moveCrane(player, Direction.WEST)
                33 -> closeInterface(player)
            }

            playAudio(player, Sounds.TWIN_MOVE_CRANE_2273)
            return@on true
        }
    }

}