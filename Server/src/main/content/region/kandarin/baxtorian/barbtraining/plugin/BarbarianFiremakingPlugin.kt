package content.region.kandarin.baxtorian.barbtraining.plugin

import content.data.items.SkillingTool
import content.global.skill.firemaking.Log
import content.global.skill.firemaking.Log.Companion.forId
import content.region.kandarin.baxtorian.barbtraining.BarbarianTraining
import core.api.*
import core.game.event.LitFireEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
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
import shared.consts.Items
import kotlin.math.ceil

class BarbarianFiremakingPlugin : InteractionListener {

    companion object {
        val tools = intArrayOf(
            Items.OGRE_BOW_2883,
            Items.COMP_OGRE_BOW_4827,
            Items.TRAINING_BOW_9705,
            Items.LONGBOW_839,
            Items.SHORTBOW_841,
            Items.OAK_SHORTBOW_843,
            Items.OAK_LONGBOW_845,
            Items.WILLOW_LONGBOW_847,
            Items.WILLOW_SHORTBOW_849,
            Items.MAPLE_LONGBOW_851,
            Items.MAPLE_SHORTBOW_853,
            Items.YEW_LONGBOW_855,
            Items.YEW_SHORTBOW_857,
            Items.MAGIC_LONGBOW_859,
            Items.MAGIC_SHORTBOW_861,
            Items.SEERCULL_6724,
            Items.DARK_BOW_11235,
            Items.DARK_BOW_13405
        )
        val logs = intArrayOf(
            Items.LOGS_1511,
            Items.OAK_LOGS_1521,
            Items.WILLOW_LOGS_1519,
            Items.MAPLE_LOGS_1517,
            Items.YEW_LOGS_1515,
            Items.MAGIC_LOGS_1513,
            Items.ACHEY_TREE_LOGS_2862,
            Items.PYRE_LOGS_3438,
            Items.OAK_PYRE_LOGS_3440,
            Items.WILLOW_PYRE_LOGS_3442,
            Items.MAPLE_PYRE_LOGS_3444,
            Items.YEW_PYRE_LOGS_3446,
            Items.MAGIC_PYRE_LOGS_3448,
            Items.TEAK_PYRE_LOGS_6211,
            Items.MAHOGANY_PYRE_LOG_6213,
            Items.MAHOGANY_LOGS_6332,
            Items.TEAK_LOGS_6333,
            Items.RED_LOGS_7404,
            Items.GREEN_LOGS_7405,
            Items.BLUE_LOGS_7406,
            Items.PURPLE_LOGS_10329,
            Items.WHITE_LOGS_10328,
            Items.SCRAPEY_TREE_LOGS_8934,
            Items.DREAM_LOG_9067,
            Items.ARCTIC_PYRE_LOGS_10808,
            Items.ARCTIC_PINE_LOGS_10810,
            Items.SPLIT_LOG_10812,
            Items.WINDSWEPT_LOGS_11035,
            Items.EUCALYPTUS_LOGS_12581,
            Items.EUCALYPTUS_PYRE_LOGS_12583,
            Items.JOGRE_BONES_3125
        )

        private fun checkRequirements(item: Item): String? = when {
            item.id == Items.DARK_BOW_11235 || item.id == Items.DARK_BOW_13405 ->
                "The innate darkness of the bow sucks all the heat from your firemaking attempt. You realise that this type of bow is useless for firelighting."

            item.name.contains("CRYSTAL BOW", true) || item.name.contains("CRYSTAL SHIELD", true) ->
                "The bow resists all attempts to light the fire. It seems that the sentient tools of the elves don't approve of you burning down forests."

            item.id in listOf(Items.COMP_OGRE_BOW_4827, Items.OGRE_BOW_2883) ->
                "This bow is vast, clumsy and most of a tree. You realise that this type of bow is useless for firelighting."

            else -> null
        }
    }

    override fun defineListeners() {
        onUseWith(IntType.ITEM, tools, *logs) { player, used, with ->


            val activityDone = player.savedData.activityData.isBarbarianFiremakingBow
            if (!activityDone && getAttribute(player, BarbarianTraining.FM_START, false)) {
                sendDialogue(player, "You must begin the relevant section of Otto Godblessed's barbarian training.")
                return@onUseWith false
            }

            checkRequirements(used.asItem())?.let {
                sendDialogue(player, it)
                return@onUseWith false
            }

            submitIndividualPulse(player, BarbFiremakingPulse(player, with.asItem(), null))
            return@onUseWith true
        }
    }

}

private class BarbFiremakingPulse(
    player: Player,
    node: Item,
    groundItem: GroundItem?
) : SkillPulse<Item?>(player, node) {

    private val tools = requireNotNull(SkillingTool.getFiremakingTool(player))
    private val animationId = Animation(tools.animation)
    private val graphicsId = Graphics(shared.consts.Graphics.BARBARIAN_FIREMAKING_1169)
    private val fire = forId(node.id)
    private var groundItem: GroundItem? = groundItem
    private var ticks = 0

    init {
        if (this.groundItem == null) {
            this.groundItem = GroundItem(node, player.location, player)
            player.setAttribute("remove-log", true)
        } else {
            player.removeAttribute("remove-log")
        }
    }

    override fun checkRequirements(): Boolean {
        val fire = fire ?: return false

        return when {
            player.ironmanManager.isIronman && !groundItem!!.droppedBy(player) -> {
                sendMessage(player, "You can't do that as an Ironman."); false
            }

            getObject(player.location) != null || player.zoneMonitor.isInZone("bank") -> {
                sendMessage(player, "You can't light a fire here."); false
            }

            !inInventory(player, tools.id, 1) -> {
                sendMessage(player, "You do not have the required items to light this."); false
            }

            getStatLevel(player, Skills.FIREMAKING) < fire.barbarianLevel -> {
                sendMessage(player, "You need a firemaking level of ${fire.barbarianLevel} to light this log."); false
            }

            else -> {
                if (player.getAttribute("remove-log", false)) {
                    player.removeAttribute("remove-log")
                    if (inInventory(player, node!!.id, 1)) {
                        replaceSlot(player, node!!.slot, Item(node!!.id, node!!.amount - 1), node)
                        GroundItemManager.create(groundItem)
                    }
                }
                true
            }
        }
    }

    override fun animate() {
    }

    override fun reward(): Boolean {
        if (lastFire >= GameWorld.ticks) {
            createFire(); return true
        }

        if (ticks == 0) visualize(player, animationId, graphicsId)
        if (++ticks % 12 == 0) animate(player, animationId)
        if (ticks % 3 != 0 || !success()) return false

        createFire()
        return true
    }

    private fun createFire() {
        if (!groundItem!!.isActive) return

        val scenery = Scenery(fire!!.fireId, player.location)
        SceneryBuilder.add(scenery, fire.life, getAsh(player, fire, scenery))
        GroundItemManager.destroy(groundItem)
        player.moveStep()
        player.faceLocation(scenery.getFaceLocation(player.location))
        player.skills.addExperience(Skills.FIREMAKING, fire.xp)

        setLastFire()
        player.dispatch(LitFireEvent(fire.logId))
        player.graphics(Graphics(-1))

        if (getAttribute(player, BarbarianTraining.FM_BASE, false)) {
            removeAttribute(player, BarbarianTraining.FM_BASE)
            player.savedData.activityData.isBarbarianFiremakingBow = true
            sendDialogueLines(player, "You feel you have learned more of barbarian ways. Otto might wish", "to talk to you more.")
        }
    }

    override fun message(type: Int) {
        val name = if (node!!.id == Items.JOGRE_BONES_3125) "bones" else "logs"
        val msg = when (type) {
            0 -> "You attempt to light the $name.."
            1 -> "The fire catches and the $name begin to burn."
            else -> return
        }
        sendMessage(player, msg)
    }

    private val lastFire: Int
        get() = player.getAttribute("last-firemake", 0)

    private fun setLastFire() {
        player.setAttribute("last-firemake", GameWorld.ticks + 2)
    }

    private fun success(): Boolean {
        val level = 1 + player.skills.getLevel(Skills.FIREMAKING)
        val req = fire!!.defaultLevel.toDouble()
        val successChance = ceil((level * 50 - req * 15) / req / 3 * 4)
        return successChance >= RandomFunction.random(99)
    }

    companion object {
        fun getAsh(player: Player?, fire: Log, scenery: Scenery): GroundItem {
            return GroundItem(Item(Items.ASHES_592), scenery.location, player).apply {
                decayTime = fire.life + 200
            }
        }
    }
}
