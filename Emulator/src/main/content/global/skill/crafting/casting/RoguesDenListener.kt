package content.global.skill.crafting.casting

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.ChanceItem
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.*
import kotlin.math.ceil

class RoguesDenListener : InteractionListener {
    private val OBJECTS = intArrayOf(Scenery.WALL_SAFE_7236, Scenery.FLOOR_7227, Scenery.DOORWAY_7256)
    private val ANIMATIONS =
        arrayOf(
            Animation(Animations.SAFE_CRACK_2247),
            Animation(Animations.SAFE_CRACK_2248),
            Animation(1111),
            Animation(Animations.CLEAN_SOMETHING_ON_GROUND_2244),
        )
    private val SOUNDS_EFFECTS = arrayOf(Sounds.SAFE_CRACK_1243, Sounds.ROGUE_SAFE_OPEN_1238, Sounds.FLOOR_SPIKES_1383)
    private val COINS_REWARD = arrayOf(ChanceItem(Items.COINS_995, 20, 20, 90.0), ChanceItem(995, 40, 40, 80.0))
    private val GEMS_REWARD =
        arrayOf(
            ChanceItem(Items.UNCUT_SAPPHIRE_1623, 1, 1, 80.0),
            ChanceItem(Items.UNCUT_EMERALD_1621, 1, 1, 60.0),
            ChanceItem(Items.UNCUT_RUBY_1619, 1, 1, 8.0),
            ChanceItem(Items.UNCUT_DIAMOND_1617, 1, 1, 7.0),
        )

    override fun defineListeners() {
        on(OBJECTS, IntType.SCENERY, "crack", "open", "disarm", "search") { player, node ->
            if (getUsedOption(player) == "open") {
                sendNPCDialogue(
                    player,
                    NPCs.BRIAN_ORICHARD_2266,
                    "And where do you think you're going? A little too eager I think. Come and talk to me before you go wandering around in there.",
                )
                return@on true
            }

            if (getUsedOption(player) == "search") {
                animate(player, ANIMATIONS[3])
                sendMessage(player, "You temporarily disarm the trap!")
                return@on true
            }

            if (getUsedOption(player) == "crack") {
                if (getStatLevel(player, Skills.THIEVING) < 50) {
                    sendMessage(player, "You need to be level 50 thief to crack this safe.")
                    return@on true
                }

                if (freeSlots(player) == 0) {
                    sendMessage(player, "Not enough inventory space.")
                    return@on true
                }

                val success = success(player, Skills.THIEVING)
                val trapped = RandomFunction.random(3) == 1

                lock(player, 4)
                sendMessage(player, "You start cracking the safe.")
                playAudio(player, SOUNDS_EFFECTS[0])
                animate(player, ANIMATIONS[if (success) 1 else 0])
                queueScript(player, 3, QueueStrength.SOFT) {
                    if (success) {
                        handleSuccess(player, node.asScenery())
                        playAudio(player, SOUNDS_EFFECTS[1])
                    } else if (trapped) {
                        playAudio(player, SOUNDS_EFFECTS[2])
                        sendMessage(player, "You slip and trigger a trap!")
                        impact(player, RandomFunction.random(2, 6), ImpactHandler.HitsplatType.NORMAL)
                        player.skills.drainLevel(Skills.THIEVING, 0.05, 0.05)
                        player.animate(Animation.RESET, 1)
                    }
                    return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }
    }

    private fun handleSuccess(
        player: Player,
        scenery: core.game.node.scenery.Scenery,
    ) {
        replaceScenery(scenery, 7238, 1)
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
                if (i.id == 1617) {
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

    fun success(
        player: Player,
        skill: Int,
    ): Boolean {
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
}
