package content.region.morytania.port_phasmatys.plugin

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
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery
import shared.consts.Sounds

class BoneGrinderPlugin : InteractionListener {

    companion object {
        private const val LOADED_BONE_KEY = "/save:bonegrinder-bones"
        private const val BONE_HOPPER_KEY = "/save:bonegrinder-hopper"
        private const val BONE_BIN_KEY = "/save:bonegrinder-bin"

        private val WIND_ANIM = Animation(Animations.TURN_WHEEL_1648)
        private val FILL_ANIM = Animation(Animations.FILL_HOPPER_1649)
        private val SCOOP_ANIM = Animation(Animations.FILL_POT_1650)

        private val boneIDs = Bones.values().map { it.itemId }.toIntArray()
    }

    override fun defineListeners() {
        on(Scenery.LOADER_11162, IntType.SCENERY, "fill") { player, _ -> handleFill(player) }
        on(Scenery.BONE_GRINDER_11163, IntType.SCENERY, "wind") { player, _ -> handleWind(player) }
        on(Scenery.BONE_GRINDER_11163, IntType.SCENERY, "status") { player, _ -> handleStatus(player) }
        on(Scenery.BIN_11164, IntType.SCENERY, "empty") { player, _ -> handleEmpty(player) }
        onUseWith(IntType.SCENERY, Scenery.LOADER_11162, *boneIDs) { player, _, _ -> handleFill(player)
            return@onUseWith true
        }
    }

    fun handleFill(player: Player): Boolean {
        val bone = getBone(player)

        if (bone?.bonemealId == null) {
            handleInvalidBone(player)
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

        val fillPulse = createFillPulse(player, bone)

        if (inInventory(player, bone.itemId)) {
            runAutoCycle(player, fillPulse)
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

        player.pulseManager.run(createWindPulse(player), PulseType.CUSTOM_1)
        return true
    }

    fun handleEmpty(player: Player): Boolean {
        val bonesLoaded = getAttribute(player, BONE_HOPPER_KEY, false)
        val boneType = getAttribute(player, LOADED_BONE_KEY, -1)
        val hasMeal = getAttribute(player, BONE_BIN_KEY, false) && boneType != -1

        if (!hasMeal) return handleEmptyFail(player, bonesLoaded, boneType)

        if (!inInventory(player, Items.EMPTY_POT_1931, 1)) {
            sendMessage(player, "You don't have any pots to take the bonemeal with.")
            return true
        }

        val bone = Bones.values()[boneType]

        removeAttributes(player, BONE_HOPPER_KEY, BONE_BIN_KEY, LOADED_BONE_KEY)
        lock(player, SCOOP_ANIM.duration + 1)

        player.pulseManager.run(createEmptyPulse(player, bone), PulseType.CUSTOM_1)
        return true
    }

    private fun handleStatus(player: Player): Boolean {
        val bones = getAttribute(player, BONE_HOPPER_KEY, false)
        val meal = getAttribute(player, BONE_BIN_KEY, false)

        when {
            bones -> sendMessage(player, "There are already some bones in the grinder's hopper.")
            meal  -> sendMessage(player, "There is bonemeal waiting in the bin to be collected.")
            else -> sendMessage(player, "There is nothing loaded into the machine.")
        }
        return true
    }

    private fun createFillPulse(player: Player, bone: Bones) = object : Pulse() {
        private var ticks = 0

        override fun pulse(): Boolean {
            return when (ticks++) {
                0 -> {
                    lock(player, FILL_ANIM.duration)
                    animate(player, FILL_ANIM)
                    playAudio(player, Sounds.FILL_GRINDER_1133)
                    false
                }

                FILL_ANIM.duration -> {
                    sendMessage(player, "You fill the hopper with bones.")
                    removeItem(player, Item(bone.itemId), Container.INVENTORY)
                    setAttribute(player, LOADED_BONE_KEY, bone.ordinal)
                    setAttribute(player, BONE_HOPPER_KEY, true)
                    true
                }

                else -> false
            }
        }
    }

    private fun createWindPulse(player: Player) = object : Pulse() {
        private var ticks = 0

        override fun pulse(): Boolean {
            return when (ticks++) {
                0 -> {
                    face(player, Location(3659, 3526, 1))
                    lock(player, WIND_ANIM.duration)
                    animate(player, WIND_ANIM)
                    sendMessage(player, "You wind the grinder handle.")
                    playAudio(player, Sounds.GRINDER_GRINDING_1131)
                    false
                }

                WIND_ANIM.duration -> {
                    sendMessage(player, "Some crushed bones pour into the bin.")
                    setAttribute(player, BONE_HOPPER_KEY, false)
                    setAttribute(player, BONE_BIN_KEY, true)
                    true
                }

                else -> false
            }
        }
    }

    private fun createEmptyPulse(player: Player, bone: Bones) = object : Pulse() {
        private var ticks = 0

        override fun pulse(): Boolean {
            return when (ticks++) {
                0 -> {
                    face(player, Location(3658, 3525, 1))
                    animate(player, SCOOP_ANIM)
                    playAudio(player, Sounds.GRINDER_EMPTY_1136)
                    false
                }

                SCOOP_ANIM.duration -> {
                    if (removeItem(player, Item(Items.EMPTY_POT_1931), Container.INVENTORY)) {
                        addItem(player, bone.bonemealId!!)
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun runAutoCycle(player: Player, fillPulse: Pulse) {
        player.pulseManager.run(object : Pulse() {
            private var step = 0

            override fun pulse(): Boolean {
                when (step++) {
                    0 -> {
                        Pulser.submit(fillPulse)
                        delay = FILL_ANIM.duration + 1
                    }

                    1 -> {
                        move(player, 3659, 3524)
                        delay = 2
                    }

                    2 -> {
                        handleWind(player)
                        delay = WIND_ANIM.duration + 1
                    }

                    3 -> {
                        move(player, 3658, 3524)
                        delay = 2
                    }

                    4 -> {
                        handleEmpty(player)
                        delay = SCOOP_ANIM.duration + 1
                    }

                    5 -> {
                        move(player, 3660, 3524)
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
        })
    }

    private fun move(player: Player, x: Int, y: Int) {
        stopWalk(player)
        forceWalk(player, Location(x, y), "smart")
    }

    private fun handleInvalidBone(player: Player) {
        if (inInventory(player, Items.MARINATED_J_BONES_3130) ||
            inInventory(player, Items.MARINATED_J_BONES_3133)
        ) {
            sendDialogue(player,
                "These bones could break the bone grinder. Perhaps I should find some different bones."
            )
        } else {
            sendMessage(player, "You have no bones to grind.")
        }
    }

    private fun handleEmptyFail(player: Player, inHopper: Boolean, boneType: Int): Boolean {
        when {
            inHopper -> sendMessage(player, "You need to wind the wheel to grind the bones.")
            boneType == -1 -> sendMessage(player, "You need to load some bones in the hopper first.")
            else -> sendMessage(player, "You have no bonemeal to collect.")
        }
        return true
    }

    private fun getBone(player: Player): Bones? =
        Bones.values().firstOrNull { inInventory(player, it.itemId) }
}
