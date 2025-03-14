package content.global.ame.eviltwin

import content.data.GameAttributes
import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Components
import org.rs.consts.Sounds

class EvilTwinInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.CRANE_CONTROL_240) { player, _, _, buttonID, _, _ ->
            if (EvilTwinUtils.success) return@on false
            when (buttonID) {
                28 -> {
                    EvilTwinUtils.success = false
                    for (npc in EvilTwinUtils.region.planes[0].npcs) {
                        if (npc.location == EvilTwinUtils.currentCrane!!.location) {
                            val evilTwin =
                                EvilTwinUtils.isEvilTwin(npc, player.getAttribute(GameAttributes.RE_TWIN_START, 0))
                            if (evilTwin) {
                                EvilTwinUtils.success = true
                                sendMessage(player, "You caught the Evil twin!")
                            } else {
                                sendMessage(player, "You caught an innocent civilian!")
                            }
                            visualize(
                                npc,
                                Animation.create(4001),
                                Graphics(666),
                            )
                            npc.lock(10)
                            player.locks.lockComponent(15)
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
                                                    Scenery(66, EvilTwinUtils.currentCrane?.location, 22, 0),
                                                )
                                                npc.transform(npc.id + 20)
                                                npc.lock(20)
                                                npc.walkingQueue.reset()
                                                npc.walkingQueue.addPath(
                                                    EvilTwinUtils.region.baseLocation.x + 10,
                                                    EvilTwinUtils.region.baseLocation.y + 4,
                                                )
                                                delay = npc.walkingQueue.queue.size + 1
                                                EvilTwinUtils.craneNPC = npc
                                            }

                                            1 -> {
                                                val animDuration = animationDuration(Animation(4003))
                                                EvilTwinUtils.craneNPC = null
                                                playAudio(player, Sounds.TWIN_LOWER_CRANE_2272)
                                                animate(npc, Animation.create(4003), true)
                                                delay = animDuration
                                            }

                                            2 -> {
                                                playAudio(player, Sounds.TWIN_MOVE_CRANE_2273)
                                                npc.reTransform()
                                                npc.faceLocation(player.location)
                                                if (evilTwin) {
                                                    EvilTwinUtils.removeSuspects(player)
                                                    npc.animate(Animation.create(859))
                                                    playAudio(player, Sounds.TWIN_CRANE_DROP_2271)
                                                } else {
                                                    npc.sendChat("You're putting me in prison?!")
                                                }
                                                EvilTwinUtils.currentCrane =
                                                    EvilTwinUtils.currentCrane!!.transform(
                                                        EvilTwinUtils.currentCrane!!.id,
                                                        EvilTwinUtils.currentCrane!!.rotation,
                                                        EvilTwinUtils.region.baseLocation.transform(14, 12, 0),
                                                    )
                                                SceneryBuilder.add(
                                                    Scenery(14977, EvilTwinUtils.currentCrane?.location, 22, 0),
                                                )
                                                SceneryBuilder.add(EvilTwinUtils.currentCrane)
                                            }

                                            3 -> {
                                                EvilTwinUtils.updateCraneCam(player, 14, 12)
                                                if (!evilTwin) {
                                                    player.interfaceManager.closeSingleTab()
                                                    openDialogue(player, MollyDialogue(0), EvilTwinUtils.mollyNPC!!)
                                                } else {
                                                    EvilTwinUtils.attempts(player)
                                                }
                                                return true
                                            }
                                        }
                                        return false
                                    }
                                },
                            )
                            player.packetDispatch.sendSceneryAnimation(
                                EvilTwinUtils.currentCrane,
                                Animation.create(4000),
                            )
                            return@on true
                        }
                    }
                    EvilTwinUtils.attempts(player)
                    player.packetDispatch.sendSceneryAnimation(EvilTwinUtils.currentCrane, Animation.create(4000))
                    return@on true
                }

                29 -> EvilTwinUtils.moveCrane(player, Direction.SOUTH)
                30 -> EvilTwinUtils.moveCrane(player, Direction.NORTH)
                31 -> EvilTwinUtils.moveCrane(player, Direction.EAST)
                32 -> EvilTwinUtils.moveCrane(player, Direction.WEST)
                33 -> {
                    player.interfaceManager.singleTab.close(player)
                    player.interfaceManager.restoreTabs()
                }
            }
            return@on true
        }
    }
}
