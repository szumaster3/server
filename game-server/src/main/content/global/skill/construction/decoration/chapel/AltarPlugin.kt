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
import core.game.world.map.Direction
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class AltarPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles use bones on altar.
         */

        onUseWith(IntType.SCENERY, BONES, *ALTAR) { player, used, with ->
            val altar = with.asScenery()
            val rotation = altar.rotation

            var left: Scenery? = null
            var right: Scenery? = null

            if (rotation % 2 == 0) {
                left = RegionManager.getObject(with.location.z, with.location.x + 3, with.location.y)
                right = RegionManager.getObject(with.location.z, with.location.x - 2, with.location.y)
            } else {
                left = RegionManager.getObject(with.location.z, with.location.x, with.location.y + 3)
                right = RegionManager.getObject(with.location.z, with.location.x, with.location.y - 2)
            }

            Bones.forId(used.id)?.let { bones ->
                worship(player, altar, left, right, bones)
            }

            return@onUseWith true
        }

        /*
         * Handles blessing the shield on altar.
         */

        onUseWith(IntType.SCENERY, intArrayOf(Items.SPIRIT_SHIELD_13734, Items.HOLY_ELIXIR_13754), *ALTAR) { player, used, with ->
            if (player.ironmanManager.isIronman && !player.houseManager.isInHouse(player)) {
                sendMessage(player, "You cannot do this on someone else's altar.")
                return@onUseWith false
            }

            if (getStatLevel(player, Skills.PRAYER) < 85) {
                sendMessage(player, "You need 85 Prayer to do this.")
                return@onUseWith false
            }

            animate(player, Animations.HUMAN_PRAY_645)
            playAudio(player, Sounds.POH_OFFER_BONES_958)

            val first = used.asItem()
            val second = with.asItem()

            val hasShield = (first.id == Items.SPIRIT_SHIELD_13734 || second.id == Items.SPIRIT_SHIELD_13734)
            val hasElixir = (first.id == Items.HOLY_ELIXIR_13754 || second.id == Items.HOLY_ELIXIR_13754)

            if (hasShield && hasElixir) {
                removeItem(player, Item(Items.SPIRIT_SHIELD_13734))
                removeItem(player, Item(Items.HOLY_ELIXIR_13754))
                addItem(player, Items.BLESSED_SPIRIT_SHIELD_13736)
                sendMessage(player, "You bless the spirit shield using the holy elixir and the power of Saradomin.")
            } else {
                sendMessage(player, "You need both a Spirit Shield and Holy Elixir.")
            }

            return@onUseWith true
        }
    }

    private fun worship(player: Player, altar: Scenery, left: Scenery?, right: Scenery?, bones: Bones, ) {
        if (player.ironmanManager.isIronman && !player.houseManager.isInHouse(player)) {
            sendMessage(player, "You cannot do this on someone else's altar.")
            return
        }

        val start = player.location

        val dx = altar.location.x - player.location.x
        val dy = altar.location.y - player.location.y
        val direction = Direction.getDirection(dx.coerceIn(-1, 1), dy.coerceIn(-1, 1))
        val gfxLoc = player.location.transform(direction, 1)

        submitIndividualPulse(player, object : Pulse(1) {
            var counter = 0

            override fun pulse(): Boolean {
                counter++
                if (counter == 1 || counter % 5 == 0) {
                    if (removeItem(player, bones.itemId)) {
                        animate(player, ANIM)
                        playAudio(player, Sounds.POH_OFFER_BONES_958)
                        sendGraphics(GFX, gfxLoc)
                        sendMessage(player, getMessage(isLit(left), isLit(right)))
                        rewardXP(player, Skills.PRAYER, bones.experience * getMod(altar, isLit(left), isLit(right)))
                    }
                }
                return !(player.location == start || !inInventory(player, bones.itemId))
            }
        })
    }

    private fun isLit(obj: Scenery?): Boolean =
        obj != null && obj.id != org.rs.consts.Scenery.LAMP_SPACE_15271 && !SceneryDefinition.forId(obj.id).hasAction("light")

    private fun getBase(altar: Scenery?): Double = when (altar?.id) {
        org.rs.consts.Scenery.ALTAR_13182 -> 110.0
        org.rs.consts.Scenery.ALTAR_13185 -> 125.0
        org.rs.consts.Scenery.ALTAR_13188 -> 150.0
        org.rs.consts.Scenery.ALTAR_13191 -> 175.0
        org.rs.consts.Scenery.ALTAR_13194 -> 200.0
        org.rs.consts.Scenery.ALTAR_13197 -> 250.0
        else -> 150.0
    }

    private fun getMod(altar: Scenery, isLeft: Boolean, isRight: Boolean, ): Double {
        var total = getBase(altar)
        if (isLeft) {
            total += 50.0
        }
        if (isRight) {
            total += 50.0
        }
        return total / 100
    }

    private fun getMessage(isLeft: Boolean, isRight: Boolean, ): String =
        when {
            isLeft && isRight -> "The gods are very pleased with your offering."
            isLeft || isRight -> "The gods are pleased with your offering."
            else -> "The gods accept your offering."
        }

    companion object {
        private val GFX = Graphics(org.rs.consts.Graphics.BONE_ON_ALTAR_624)
        private val ANIM = Animation(Animations.HUMAN_COOKING_RANGE_896)
        private val BONES = Bones.array
        private val ALTAR = intArrayOf(org.rs.consts.Scenery.ALTAR_13185, org.rs.consts.Scenery.ALTAR_13188, org.rs.consts.Scenery.ALTAR_13191, org.rs.consts.Scenery.ALTAR_13194, org.rs.consts.Scenery.ALTAR_13197)
    }
}
