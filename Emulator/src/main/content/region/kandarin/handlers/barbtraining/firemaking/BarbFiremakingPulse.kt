package content.region.kandarin.handlers.barbtraining.firemaking

import content.data.items.SkillingTool
import content.global.skill.firemaking.Log
import content.global.skill.firemaking.Log.Companion.forId
import content.region.kandarin.handlers.barbtraining.BarbarianTraining
import core.api.*
import core.game.event.LitFireEvent
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.GameWorld
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs.consts.Items
import kotlin.math.ceil

class BarbFiremakingPulse(
    player: Player,
    node: Item,
    groundItem: GroundItem?,
) : SkillPulse<Item?>(player, node) {
    val tools = SkillingTool.getFiremakingTool(player)
    private val animationId = Animation(tools!!.animation)
    private val graphicsId = Graphics(org.rs.consts.Graphics.BARBARIAN_FIREMAKING_1169)
    private val fire = forId(node.id)
    private var groundItem: GroundItem? = null
    private var ticks = 0

    init {
        if (groundItem == null) {
            this.groundItem = GroundItem(node, player.location, player)
            player.setAttribute("remove-log", true)
        } else {
            this.groundItem = groundItem
            player.removeAttribute("remove-log")
        }
    }

    override fun checkRequirements(): Boolean {
        if (fire == null) {
            return false
        }
        if (player.ironmanManager.isIronman && !groundItem!!.droppedBy(player)) {
            sendMessage(player, "You can't do that as an Ironman.")
            return false
        }
        if (getObject(player.location) != null || player.zoneMonitor.isInZone("bank")) {
            sendMessage(player, "You can't light a fire here.")
            return false
        }
        if (!inInventory(player, tools!!.id, 1)) {
            sendMessage(player, "You do not have the required items to light this.")
            return false
        }
        if (getStatLevel(player, Skills.FIREMAKING) < fire.barbarianLevel) {
            sendMessage(player, "You need a firemaking level of " + fire.barbarianLevel + " to light this log.")
            return false
        }
        if (player.getAttribute("remove-log", false)) {
            player.removeAttribute("remove-log")
            if (inInventory(player, node!!.id, 1)) {
                replaceSlot(player, node!!.slot, Item(node!!.id, (node!!.amount - 1)), node, Container.INVENTORY)
                GroundItemManager.create(groundItem)
            }
        }
        return true
    }

    override fun animate() {
    }

    override fun reward(): Boolean {
        if (lastFire >= GameWorld.ticks) {
            createFire()
            return true
        }
        if (ticks == 0) {
            visualize(player, animationId, graphicsId)
        }
        if (++ticks % 3 != 0) {
            return false
        }
        if (ticks % 12 == 0) {
            animate(player, animationId)
        }
        if (!success()) {
            return false
        }
        createFire()

        return true
    }

    fun createFire() {
        if (!groundItem!!.isActive) {
            return
        }

        val scenery = Scenery(fire!!.fireId, player.location)
        SceneryBuilder.add(scenery, fire.life, getAsh(player, fire, scenery))
        GroundItemManager.destroy(groundItem)
        player.moveStep()
        player.faceLocation(scenery.getFaceLocation(player.location))
        player.getSkills().addExperience(Skills.FIREMAKING, fire.xp)

        val playerRegion = player.viewport.region.id

        setLastFire()
        player.dispatch(LitFireEvent(fire.logId))
        player.graphics(Graphics(-1))

        if (getAttribute(player, BarbarianTraining.FM_BASE, false)) {
            removeAttribute(player, BarbarianTraining.FM_BASE)
            setAttribute(player, BarbarianTraining.FM_FULL, true)
            sendDialogueLines(
                player,
                "You feel you have learned more of barbarian ways. Otto might wish",
                "to talk to you more.",
            )
        }
    }

    override fun message(type: Int) {
        val name = if (node!!.id == Items.JOGRE_BONES_3125) "bones" else "logs"
        when (type) {
            0 -> sendMessage(player, "You attempt to light the $name..")
            1 -> sendMessage(player, "The fire catches and the $name begin to burn.")
        }
    }

    val lastFire: Int

        get() = player.getAttribute("last-firemake", 0)

    fun setLastFire() {
        player.setAttribute("last-firemake", GameWorld.ticks + 2)
    }

    private fun success(): Boolean {
        val level = 1 + player.getSkills().getLevel(Skills.FIREMAKING)
        val req = fire!!.defaultLevel.toDouble()
        val successChance = ceil((level * 50 - req * 15) / req / 3 * 4)
        val roll = RandomFunction.random(99)
        if (successChance >= roll) {
            return true
        }
        return false
    }

    companion object {
        fun getAsh(
            player: Player?,
            fire: Log,
            scenery: Scenery,
        ): GroundItem {
            val ash = GroundItem(Item(Items.ASHES_592), scenery.location, player)
            ash.decayTime = fire.life + 200
            return ash
        }
    }
}
