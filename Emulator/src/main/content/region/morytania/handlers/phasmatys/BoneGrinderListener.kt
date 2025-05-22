package content.region.morytania.handlers.phasmatys

import content.global.skill.prayer.Bones
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery
import org.rs.consts.Sounds

class BoneGrinderListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.LOADER_11162, IntType.SCENERY, "fill") { player, _ ->
            handleFill(player)
        }

        on(Scenery.BONE_GRINDER_11163, IntType.SCENERY, "wind") { player, _ ->
            handleWind(player)
        }

        on(Scenery.BONE_GRINDER_11163, IntType.SCENERY, "status") { player, _ ->
            handleStatus(player)
        }

        on(Scenery.BIN_11164, IntType.SCENERY, "empty") { player, _ ->
            handleEmpty(player)
        }

        onUseWith(IntType.SCENERY, Scenery.LOADER_11162, *boneIDs) { player, _, _ ->
            handleFill(player)
            return@onUseWith true
        }
    }

    companion object {
        private const val LOADED_BONE_KEY = "/save:bonegrinder-bones"
        private const val BONE_HOPPER_KEY = "/save:bonegrinder-hopper"
        private const val BONE_BIN_KEY = "/save:bonegrinder-bin"

        private val WIND_ANIM = Animation(Animations.TURN_WHEEL_1648)
        private val FILL_ANIM = Animation(Animations.FILL_HOPPER_1649)
        private val SCOOP_ANIM = Animation(Animations.FILL_POT_1650)

        private val boneIDs = Bones.values().map { it.itemId }.toIntArray()
    }

    fun handleFill(player: Player): Boolean {
        val bone = getBone(player)
        if ((bone == null) || (bone.bonemealId == null)) {
            if (inInventory(player, Items.MARINATED_J_BONES_3130) ||
                inInventory(player, Items.MARINATED_J_BONES_3133)
            ) {
                sendDialogue(
                    player,
                    "These bones could break the bone grinder. Perhaps I should find some different bones.",
                )
            } else {
                sendMessage(player, "You have no bones to grind.")
            }
            return true
        }
        if (getAttribute(player, BONE_HOPPER_KEY, false)) {
            sendMessage(player, "You already have some bones in the hopper.")
            return true
        }
        if (getAttribute(player, BONE_BIN_KEY, false)) {
            sendMessage(player, "You already have some bonemeal that needs to be collected.")
            return true
        }

        val fillPulse =
            object : Pulse() {
                var stage = 0

                override fun pulse(): Boolean {
                    when (stage++) {
                        0 -> {
                            lock(player, FILL_ANIM.duration)
                            animate(player, FILL_ANIM)
                            playAudio(player, Sounds.FILL_GRINDER_1133)
                        }

                        FILL_ANIM.duration -> {
                            sendMessage(player, "You fill the hopper with bones.")
                            removeItem(player, Item(bone.itemId), Container.INVENTORY)
                            setAttribute(player, LOADED_BONE_KEY, bone.ordinal)
                            setAttribute(player, BONE_HOPPER_KEY, true)
                            return true
                        }
                    }
                    return false
                }
            }

        if (inInventory(player, bone.itemId)) {
            player.pulseManager.run(
                object : Pulse() {
                    var stage = 0

                    override fun pulse(): Boolean {
                        when (stage++) {
                            0 -> Pulser.submit(fillPulse).also { delay = FILL_ANIM.duration + 1 }
                            1 -> {
                                stopWalk(player)
                                forceWalk(player, Location(3659, 3524), "smart")
                                delay = 2
                            }

                            2 -> {
                                handleWind(player)
                                delay = WIND_ANIM.duration + 1
                            }

                            3 -> {
                                stopWalk(player)
                                forceWalk(player, Location(3658, 3524), "smart")
                                delay = 2
                            }

                            4 -> {
                                if (!inInventory(player, Items.EMPTY_POT_1931, 1)) {
                                    return handleEmpty(player)
                                } else {
                                    handleEmpty(player)
                                    delay = SCOOP_ANIM.duration + 1
                                }
                            }

                            5 -> {
                                stopWalk(player)
                                forceWalk(player, Location(3660, 3524), "smart")
                                delay = 4
                            }

                            6 -> {
                                face(player, Location(3660, 3526))
                                handleFill(player)
                                return true
                            }
                        }
                        return false
                    }
                },
            )
        } else {
            player.pulseManager.run(fillPulse, PulseType.CUSTOM_1)
        }
        return true
    }

    fun handleWind(player: Player): Boolean {
        if (!getAttribute(player, BONE_HOPPER_KEY, false)) {
            sendMessage(player, "You have no bones loaded to grind.")
            return true
        }

        if (getAttribute(player, BONE_BIN_KEY, false)) {
            sendMessage(player, "You already have some bonemeal which you need to collect.")
            return true
        }

        player.pulseManager.run(
            object : Pulse() {
                var stage = 0

                override fun pulse(): Boolean {
                    when (stage++) {
                        0 -> {
                            face(player, Location(3659, 3526, 1))
                            lock(player, WIND_ANIM.duration)
                            animate(player, WIND_ANIM)
                            sendMessage(player, "You wind the handle.")
                            playAudio(player, Sounds.GRINDER_GRINDING_1131)
                        }

                        WIND_ANIM.duration -> {
                            sendMessage(player, "The bonemeal falls into the bin.")
                            setAttribute(player, BONE_HOPPER_KEY, false)
                            setAttribute(player, BONE_BIN_KEY, true)
                            return true
                        }
                    }
                    return false
                }
            },
            PulseType.CUSTOM_1,
        )
        return true
    }

    private fun handleStatus(player: Player): Boolean {
        val bonesLoaded = getAttribute(player, BONE_HOPPER_KEY, false)
        val boneMealReady = getAttribute(player, BONE_BIN_KEY, false)

        if (bonesLoaded) sendMessage(player, "There are bones waiting in the hopper.")
        if (boneMealReady) sendMessage(player, "There is bonemeal waiting in the bin to be collected.")

        if (!bonesLoaded && !boneMealReady) {
            sendMessage(player, "There is nothing loaded into the machine.")
        }

        return true
    }

    fun handleEmpty(player: Player): Boolean {
        val inHopper = getAttribute(player, BONE_HOPPER_KEY, false)
        val boneType = getAttribute(player, LOADED_BONE_KEY, -1)
        val hasMeal = getAttribute(player, BONE_BIN_KEY, false) && boneType != -1

        if (!hasMeal) {
            if (inHopper) {
                sendMessage(player, "You need to wind the wheel to grind the bones.")
            } else if (boneType == -1) {
                sendMessage(player, "You need to load some bones in the hopper first.")
            } else {
                sendMessage(player, "You have no bonemeal to collect.")
            }
            return true
        }

        if (!inInventory(player, Items.EMPTY_POT_1931, 1)) {
            sendMessage(player, "You don't have any pots to take the bonemeal with.")
            return true
        }

        val bone = Bones.values()[boneType]

        removeAttributes(player, BONE_HOPPER_KEY, BONE_BIN_KEY, LOADED_BONE_KEY)
        lock(player, SCOOP_ANIM.duration + 1)

        player.pulseManager.run(
            object : Pulse() {
                var stage = 0

                override fun pulse(): Boolean {
                    when (stage++) {
                        0 -> {
                            face(player, Location(3658, 3525, 1))
                            animate(player, SCOOP_ANIM)
                            playAudio(player, Sounds.GRINDER_EMPTY_1136)
                        }

                        SCOOP_ANIM.duration -> {
                            if (removeItem(player, Item(Items.EMPTY_POT_1931), Container.INVENTORY)) {
                                addItem(player, bone.bonemealId!!)
                            }
                            return true
                        }
                    }
                    return false
                }
            },
            PulseType.CUSTOM_1,
        )
        return true
    }

    private fun getBone(player: Player): Bones? {
        for (bone in Bones.values()) {
            if (inInventory(player, bone.itemId)) return bone
        }
        return null
    }
}
