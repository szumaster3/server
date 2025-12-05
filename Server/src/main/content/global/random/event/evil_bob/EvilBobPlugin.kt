package content.global.random.event.evil_bob

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.player.link.TeleportManager
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Sounds

/**
 * Handles interaction for Evil bob random event.
 * @author szu, Zerken
 */
class EvilBobPlugin :
    InteractionListener,
    MapArea {
    override fun defineListeners() {
        on(EvilBobUtils.EVIL_BOB_NPC_ID, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, EvilBobDialogue(), node.asNpc())
            return@on true
        }

        on(EvilBobUtils.SERVANT_NPC_ID, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, ServantDialogue(), node.asNpc())
            return@on true
        }

        on(EvilBobUtils.FISHING_SPOT, IntType.SCENERY, "net") { player, _ ->
            fun zoneFish(): Int {
                val zone = getAttribute(player, GameAttributes.RE_BOB_ZONE, EvilBobUtils.NORTH_FISHING_ZONE.toString())

                val fishingZone = when (zone) {
                    EvilBobUtils.NORTH_FISHING_ZONE.toString() -> EvilBobUtils.NORTH_FISHING_ZONE
                    EvilBobUtils.SOUTH_FISHING_ZONE.toString() -> EvilBobUtils.SOUTH_FISHING_ZONE
                    EvilBobUtils.EAST_FISHING_ZONE.toString()  -> EvilBobUtils.EAST_FISHING_ZONE
                    EvilBobUtils.WEST_FISHING_ZONE.toString()  -> EvilBobUtils.WEST_FISHING_ZONE
                    else -> EvilBobUtils.NORTH_FISHING_ZONE // fallback.
                }

                return if (inBorders(player, fishingZone)) Items.FISHLIKE_THING_6202
                else Items.FISHLIKE_THING_6206
            }

            when {
                getAttribute(player, GameAttributes.RE_BOB_DIAL_INDEX, false) || getAttribute(player, GameAttributes.RE_BOB_COMPLETE, false) -> {
                    sendDialogue(player, "You don't know if this is a good place to go fishing. Perhaps you should ask someone, like one of the human servants.")
                }

                !inInventory(player, Items.SMALL_FISHING_NET_303) -> {
                    sendNPCDialogue(player, NPCs.SERVANT_2481, "You'll need a fishing net. There are plenty scattered around the beach.", FaceAnim.SAD)
                }

                freeSlots(player) == 0 -> {
                    sendDialogue(player, "You don't have enough space in your inventory.")
                }

                getAttribute(player, GameAttributes.RE_BOB_SCORE, false) -> {
                    sendNPCDialogue(player, NPCs.SERVANT_2481, "You've already got a fish. Come over here to uncook it, then serve it to Evil Bob.", FaceAnim.SAD)
                }

                else -> {
                    lock(player, 6)
                    animate(player, EvilBobUtils.FISHING_ANIMATION)
                    sendMessage(player, "You cast out your net...")
                    runTask(player, 6) {
                        val fish = zoneFish()
                        sendItemDialogue(player, fish, "You catch a... what is this?? Is this a fish?? And it's cooked already??")
                        setAttribute(player, GameAttributes.RE_BOB_SCORE, true)
                        addItem(player, fish)
                        resetAnimator(player)
                    }
                }
            }

            return@on true
        }

        onUseWith(IntType.SCENERY, EvilBobUtils.RAW_FISH_CORRECT_IDS, EvilBobUtils.COOKING_POT) { player, _, _ ->
            lock(player, 2)
            animate(player, EvilBobUtils.COOK_ANIMATION)
            playAudio(player, Sounds.UNCOOKING_2322)
            if (removeItem(player, Items.FISHLIKE_THING_6202)) addItem(player, Items.RAW_FISHLIKE_THING_6200)
            if (removeItem(player, Items.FISHLIKE_THING_6206)) addItem(player, Items.RAW_FISHLIKE_THING_6204)
            return@onUseWith true
        }

        onUseWith(IntType.NPC, EvilBobUtils.RAW_FISH_CORRECT_IDS, EvilBobUtils.EVIL_BOB_NPC_ID) { player, _, _ ->
            openDialogue(player, EvilBobDialogue(), NPCs.EVIL_BOB_2479)
            return@onUseWith true
        }

        onUseWith(IntType.NPC, EvilBobUtils.RAW_FISh_INCORRECT_IDS, EvilBobUtils.EVIL_BOB_NPC_ID) { player, _, _ ->
            openDialogue(player, EvilBobDialogue(), NPCs.EVIL_BOB_2479)
            return@onUseWith true
        }

        on(EvilBobUtils.RAW_FISH_CORRECT_IDS, IntType.ITEM, "Eat") { player, _ ->
            sendMessage(player, "It looks vile and smells even worse. You're not eating that!")
            return@on true
        }

        on(EvilBobUtils.EXIT_PORTAL, IntType.SCENERY, "enter") { player, portal ->
            if (getAttribute(player, GameAttributes.RE_BOB_COMPLETE, false)) {
                lock(player, 8)
                queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            forceMove(player, player.location, portal.centerLocation, 0, 60, null, 819)
                            return@queueScript delayScript(player, 3)
                        }

                        1 -> {
                            player.faceLocation(Location.create(3421, 4777, 0))
                            sendChat(player, "Be seeing you!")
                            player.animate(Animation(Animations.HUMAN_BLOW_RASPBERRY_2110))
                            return@queueScript delayScript(player, 2)
                        }

                        2 -> {
                            sendMessage(player, "Welcome back to ${GameWorld.settings!!.name}.")
                            teleport(player, getAttribute(player, RandomEvent.save(), Location.create(3222, 3219, 0)), TeleportManager.TeleportType.RANDOM_EVENT_OLD)
                            EvilBobUtils.cleanup(player)
                            EvilBobUtils.reward(player)
                            return@queueScript delayScript(player, 2)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            } else {
                sendNPCDialogue(player, NPCs.EVIL_BOB_2479, "You're going nowhere, human!", FaceAnim.CHILD_NEUTRAL)
            }
            return@on true
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(3400, 4762, 3443, 4793))

    override fun getRestrictions(): Array<ZoneRestriction> =
        arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS)

    override fun areaEnter(entity: Entity) {
        entity.locks.lockTeleport(100000)
    }
}
