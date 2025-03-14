package content.global.skill.thieving

import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

class PickpocketListener : InteractionListener {
    companion object {
        val PICKPOCKET_ANIM = Animation(Animations.HUMAN_PICKPOCKETING_881, Animator.Priority.HIGH)
        val NPC_ANIM = Animation(Animations.PUNCH_422)

        fun pickpocketRoll(
            player: Player,
            low: Double,
            high: Double,
            table: WeightBasedTable,
        ): ArrayList<Item>? {
            var successMod = 0.0
            if (inInventory(player, Items.GLOVES_OF_SILENCE_10075, 1)) {
                successMod += 3
            }
            val chance = RandomFunction.randomDouble(1.0, 100.0)
            val failThreshold =
                RandomFunction.getSkillSuccessChance(low, high, getStatLevel(player, Skills.THIEVING)) + successMod
            if (chance > failThreshold) {
                return null
            } else {
                return table.roll()
            }
        }
    }

    override fun defineListeners() {
        on(IntType.NPC, "pickpocket", "pick-pocket") { player, node ->
            val pocketData = Pickpocket.forID(node.id) ?: return@on false
            val npc = node.asNpc()
            val npcName = npc.name.lowercase()
            val cabinetKey = hasAnItem(player, Items.DISPLAY_CABINET_KEY_4617).container != null

            if (player.inCombat()) {
                sendMessage(player, "You can't do this while in combat.")
                return@on true
            }

            if (pocketData == null) {
                sendMessage(player, "You cannot pickpocket that NPC.")
                return@on true
            }

            if (getStatLevel(player, Skills.THIEVING) < pocketData.requiredLevel) {
                sendMessage(player, "You need a Thieving level of ${pocketData.requiredLevel} to do that.")
                return@on true
            }

            if (DeathTask.isDead(npc)) {
                sendMessage(player, "Too late, $npcName is already dead.")
                return@on true
            }

            if (npc.id == NPCs.CURATOR_HAIG_HALEN_646 && cabinetKey) {
                sendMessage(player, "You have no reason to do that.")
                return@on true
            }

            if (!pocketData.table.canRoll(player)) {
                sendMessage(player, "You don't have enough inventory space to do that.")
                return@on true
            }

            animate(player, PICKPOCKET_ANIM)
            sendMessage(player, "You attempt to pick the $npcName pocket.")
            val lootTable = pickpocketRoll(player, pocketData.low, pocketData.high, pocketData.table)

            if (lootTable == null) {
                npc.face(player)
                npc.animator.animate(NPC_ANIM)
                npc.sendChat(pocketData.message)
                sendMessage(player, "You fail to pick the $npcName pocket.")

                playHurtAudio(player, 20)
                stun(player, pocketData.stunTime)
                impact(
                    player,
                    RandomFunction.random(pocketData.stunDamageMin, pocketData.stunDamageMax),
                    ImpactHandler.HitsplatType.NORMAL,
                )
                sendMessage(player, "You feel slightly concussed from the blow.")
                npc.face(null)
            } else {
                lock(player, 2)
                playAudio(player, Sounds.PICK_2581)
                lootTable.forEach {
                    player.inventory.add(it)
                }

                if (getStatLevel(player, Skills.THIEVING) >= 40) {
                    when {
                        inBorders(player, ZoneBorders(3201, 3456, 3227, 3468)) && npc.id == NPCs.GUARD_5920
                        -> {
                            finishDiaryTask(player, DiaryType.VARROCK, 1, 12)
                        }

                        inBorders(player, ZoneBorders(2934, 3399, 3399, 3307)) &&
                            npc.id in intArrayOf(NPCs.GUARD_9, NPCs.GUARD_3230, NPCs.GUARD_3228, NPCs.GUARD_3229)
                        -> {
                            finishDiaryTask(player, DiaryType.FALADOR, 1, 6)
                        }
                    }
                }

                sendMessage(
                    player,
                    if (npc.id ==
                        NPCs.CURATOR_HAIG_HALEN_646
                    ) {
                        "You steal a tiny key."
                    } else {
                        "You pick the $npcName's pocket."
                    },
                )
                rewardXP(player, Skills.THIEVING, pocketData.experience)
            }
            return@on true
        }
    }
}
