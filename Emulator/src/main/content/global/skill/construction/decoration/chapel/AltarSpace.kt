package content.global.skill.construction.decoration.chapel

import content.global.skill.prayer.Bones
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class AltarSpace : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.SCENERY, BONES, *ALTAR) { player, used, with ->
            var left: core.game.node.scenery.Scenery? = null
            var right: core.game.node.scenery.Scenery? = null
            if (used.asScenery().rotation % 2 == 0) {
                left = RegionManager.getObject(with.location.z, used.location.x + 3, with.location.y)
                right = RegionManager.getObject(with.location.z, used.location.x - 2, with.location.y)
            } else {
                left = RegionManager.getObject(with.location.z, with.location.x, with.location.y + 3)
                right = RegionManager.getObject(with.location.z, with.location.x, with.location.y - 2)
            }
            val b = Bones.forId(used.id)
            if (b != null) {
                worship(player, with.asScenery(), left, right, b)
            }
            return@onUseWith true
        }

        onUseWith(
            IntType.SCENERY,
            intArrayOf(Items.SPIRIT_SHIELD_13734, Items.HOLY_ELIXIR_13754),
            *ALTAR,
        ) { player, used, with ->
            if (player.ironmanManager.isIronman && !player.houseManager.isInHouse(player)) {
                sendMessage(player, "You cannot do this on someone else's altar.")
                return@onUseWith false
            }
            if (getStatLevel(player, Skills.PRAYER) < 85) {
                sendMessage(player, "You need 85 prayer to do this.")
                return@onUseWith false
            }

            animate(player, Animations.HUMAN_COOKING_RANGE_896)
            playAudio(player, Sounds.POH_OFFER_BONES_958)
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, Items.BLESSED_SPIRIT_SHIELD_13736)
            }
            return@onUseWith true
        }
    }

    private fun worship(
        player: Player,
        altar: Scenery,
        left: Scenery?,
        right: Scenery?,
        bones: Bones,
    ) {
        if (player.ironmanManager.isIronman && !player.houseManager.isInHouse(player)) {
            sendMessage(player, "You cannot do this on someone else's altar.")
            return
        }
        val start = player.location
        val gfxLoc = player.location.transform(player.direction, 1)

        submitIndividualPulse(
            player,
            object : Pulse(1) {
                var counter = 0

                override fun pulse(): Boolean {
                    counter++
                    if (counter == 1 || counter % 5 == 0) {
                        if (player.inventory.remove(Item(bones.itemId))) {
                            player.animate(ANIM)
                            playAudio(player, Sounds.POH_OFFER_BONES_958)
                            player.packetDispatch.sendPositionedGraphics(GFX, gfxLoc)
                            player.sendMessage(getMessage(isLit(left), isLit(right)))
                            player.skills.addExperience(
                                Skills.PRAYER,
                                bones.experience * getMod(altar, isLit(left), isLit(right)),
                            )
                        }
                    }
                    return !(player.location == start || !player.inventory.containsItem(Item(bones.itemId)))
                }
            },
        )
    }

    private fun isLit(obj: Scenery?): Boolean {
        return obj != null &&
            obj.id != org.rs.consts.Scenery.LAMP_SPACE_15271 &&
            SceneryDefinition.forId(obj.id).options != null &&
            !SceneryDefinition
                .forId(
                    obj.id,
                ).hasAction("light")
    }

    private fun getBase(altar: Scenery?): Double {
        var base = 150.0
        if (altar == null) {
            return base
        }
        base =
            when (altar.id) {
                org.rs.consts.Scenery.ALTAR_13182 -> 110.0
                org.rs.consts.Scenery.ALTAR_13185 -> 125.0
                org.rs.consts.Scenery.ALTAR_13188 -> 150.0
                org.rs.consts.Scenery.ALTAR_13191 -> 175.0
                org.rs.consts.Scenery.ALTAR_13194 -> 200.0
                org.rs.consts.Scenery.ALTAR_13197 -> 250.0
                else -> base
            }
        return base
    }

    private fun getMod(
        altar: Scenery,
        isLeft: Boolean,
        isRight: Boolean,
    ): Double {
        var total = getBase(altar)
        if (isLeft) {
            total += 50.0
        }
        if (isRight) {
            total += 50.0
        }
        return total / 100
    }

    private fun getMessage(
        isLeft: Boolean,
        isRight: Boolean,
    ): String {
        return when {
            isLeft && isRight -> "The gods are very pleased with your offering."
            isLeft || isRight -> "The gods are pleased with your offering."
            else -> "The gods accept your offering."
        }
    }

    companion object {
        private val GFX = Graphics(org.rs.consts.Graphics.BONE_ON_ALTAR_624)

        private val ANIM = Animation(Animations.HUMAN_COOKING_RANGE_896)

        private val BONES = Bones.array

        private val ALTAR =
            intArrayOf(
                org.rs.consts.Scenery.ALTAR_13185,
                org.rs.consts.Scenery.ALTAR_13188,
                org.rs.consts.Scenery.ALTAR_13191,
                org.rs.consts.Scenery.ALTAR_13194,
                org.rs.consts.Scenery.ALTAR_13197,
            )
    }
}
