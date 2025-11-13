package content.global.activity.rogues_den.plugin

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.ChanceItem
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.*
import kotlin.math.ceil

class RoguesDenPlugin : InteractionListener {

    override fun defineListeners() {
        on(OBJECTS, IntType.SCENERY, "crack", "open", "disarm", "search") { player, node ->
            when (getUsedOption(player)) {
                "open" -> {
                    sendNPCDialogueLines(
                        player,
                        NPCs.BRIAN_ORICHARD_2266,
                        FaceAnim.THINKING,
                        false,
                        "And where do you think you're going? A little too eager I think.",
                        "Come and talk to me before you go wandering around in there."
                    )
                }
                "search", "disarm" -> {
                    animate(player, DISARM_TRAP)
                    sendMessage(player, "You temporarily disarm the trap!")
                }
                "crack" -> crackSafe(player, node)
            }
            return@on true
        }
    }

    private fun crackSafe(player: Player, node: Node) {
        if (getStatLevel(player, Skills.THIEVING) < 50) {
            sendMessage(player, "You need to be level 50 Thieving to crack this safe.")
            return
        }

        if (freeSlots(player) == 0) {
            sendMessage(player, "Not enough inventory space.")
            return
        }

        val success = success(player, Skills.THIEVING)
        val trapped = RandomFunction.random(3) == 1

        lock(player, 4)
        sendMessage(player, "You start cracking the safe.")
        playAudio(player, SFX_CRACK)
        animate(player, if (success) SAFE_CRACK_SUCCESS else SAFE_CRACK_FAIL)

        queueScript(player, 3, QueueStrength.SOFT) {
            when {
                success -> {
                    handleSuccess(player, node)
                    playAudio(player, SFX_OPEN)
                }
                trapped -> {
                    playAudio(player, SFX_TRAP)
                    sendMessage(player, "You slip and trigger a trap!")
                    impact(player, RandomFunction.random(2, 6), ImpactHandler.HitsplatType.NORMAL)
                    drainStatLevel(player, Skills.THIEVING, 0.05, 0.05)
                    player.animate(Animation.RESET, 1)
                }
            }
            return@queueScript stopExecuting(player)
        }
    }

    private fun handleSuccess(player: Player, node: Node) {
        replaceScenery(node.asScenery(), 7238, 1)
        sendMessage(player, "You get some loot.")
        rewardXP(player, Skills.THIEVING, 70.0)
        addItem(player)
    }

    private fun addItem(player: Player) {
        val l = if (RandomFunction.random(2) == 1) GEMS_REWARD else COINS_REWARD
        val chances: MutableList<ChanceItem?> = ArrayList(20)
        for (c in l) {
            chances.add(c)
        }
        chances.shuffle()
        val rand = RandomFunction.random(100)
        var item: Item? = null
        var tries = 0
        while (item == null) {
            val i = chances[0]
            if (rand <= i!!.chanceRate) {
                item = i
                break
            }
            if (tries > chances.size) {
                if (i.id == Items.UNCUT_DIAMOND_1617) {
                    item = COINS_REWARD[0]
                    break
                }
                item = i
                break
            }
            tries++
        }
        player.inventory.add(item)
    }

    private fun success(player: Player, skill: Int): Boolean {
        val level = player.getSkills().getLevel(skill).toDouble()
        val req = 50.0
        val mod = if (inInventory(player, Items.STETHOSCOPE_5560)) 8 else 17
        val successChance = ceil((level * 50 - req * mod) / req / 3 * 4)
        val roll = RandomFunction.random(99)
        if (successChance >= roll) {
            return true
        }
        return false
    }


    companion object {
        /**
         * The scenery that can be interacted.
         */
        private val OBJECTS = intArrayOf(
            Scenery.WALL_SAFE_7236,
            Scenery.FLOOR_7227,
            Scenery.DOORWAY_7256
        )

        /**
         * The animation ids for wall safe.
         */
        private const val SAFE_CRACK_FAIL    = Animations.SAFE_CRACK_2247
        private const val SAFE_CRACK_SUCCESS = Animations.SAFE_CRACK_2248
        private const val DISARM_TRAP        = Animations.DISARM_TRAP_2244

        /**
         * The sound effect ids for wall safe interaction.
         */
        private const val SFX_CRACK = Sounds.SAFE_CRACK_1243
        private const val SFX_OPEN  = Sounds.ROGUE_SAFE_OPEN_1238
        private const val SFX_TRAP  = Sounds.FLOOR_SPIKES_1383

        /**
         * The coin rewards when cracking a safe.
         */
        private val COINS_REWARD = arrayOf(
            ChanceItem(Items.COINS_995, 10, 10, 90.0),
            ChanceItem(Items.COINS_995, 20, 20, 80.0),
            ChanceItem(Items.COINS_995, 30, 30, 70.0),
            ChanceItem(Items.COINS_995, 40, 40, 60.0)
        )

        /**
         * The gem rewards from the wall safe.
         */
        private val GEMS_REWARD = arrayOf(
            ChanceItem(Items.UNCUT_SAPPHIRE_1623,1, 1, 80.0),
            ChanceItem(Items.UNCUT_EMERALD_1621, 1, 1, 60.0),
            ChanceItem(Items.UNCUT_RUBY_1619,    1, 1, 8.0),
            ChanceItem(Items.UNCUT_DIAMOND_1617, 1, 1, 7.0)
        )
    }
}
