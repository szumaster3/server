package content.global.skill.thieving.pickpocket

import content.global.skill.thieving.pickpocket.loot.FremennikCitizenLootTable
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.*

class PickpocketListener : InteractionListener {
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

            if (!pocketData.loot.canRoll(player)) {
                sendMessage(player, "You don't have enough inventory space to do that.")
                return@on true
            }

            delayClock(player, Clocks.SKILLING, 2)
            animate(player, Animation(Animations.HUMAN_PICKPOCKETING_881))
            sendMessage(player, "You attempt to pick the $npcName's pocket.")

            if (npc.id in FremennikCitizenLootTable.NPC_ID && !isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                npc.sendChat("You stay away from me outerlander!")
                sendMessage(player, "They are too suspicious of you for you to get close enough to steal from them.")
                return@on true
            }

            val lootTable = pickpocketRoll(player, pocketData.low, pocketData.high, pocketData.loot)

            if (lootTable == null) {
                npc.face(player)
                npc.animator.animate(Animation(Animations.PUNCH_422))
                npc.sendChat(pocketData.message)
                sendMessage(player, "You fail to pick the $npcName's pocket.")

                playHurtAudio(player, 20)
                stun(player, pocketData.stunTime)
                impact(player, RandomFunction.random(pocketData.stunDamageMin, pocketData.stunDamageMax))
                sendMessage(player, "You feel slightly concussed from the blow.")
                npc.face(null)
            } else {
                lock(player, 2)
                playAudio(player, Sounds.PICK_2581)
                lootTable.forEach { player.inventory.add(it) }

                if (getStatLevel(player, Skills.THIEVING) >= 40) {
                    when {
                        inBorders(player, ZoneBorders(3201, 3456, 3227, 3468)) && npc.id == NPCs.GUARD_5920 -> {
                            finishDiaryTask(player, DiaryType.VARROCK, 1, 12)
                        }
                        inBorders(player, ZoneBorders(2934, 3399, 3399, 3307)) && npc.id in intArrayOf(NPCs.GUARD_9, NPCs.GUARD_3230, NPCs.GUARD_3228, NPCs.GUARD_3229) -> {
                            finishDiaryTask(player, DiaryType.FALADOR, 1, 6)
                        }
                    }
                }

                sendMessage(player, if (npc.id == NPCs.CURATOR_HAIG_HALEN_646) "You steal a tiny key." else "You pick the $npcName's pocket.")
                rewardXP(player, Skills.THIEVING, pocketData.xp)
            }

            return@on true
        }
    }

    companion object {
        /**
         * Rolls for a pickpocket attempt.
         *
         * @param player The [Player] attempting to pickpocket.
         * @param low The minimum base success rate.
         * @param high The maximum base success rate.
         * @param table The [WeightBasedTable] loot for a pickpocket.
         * @return An [ArrayList] of [Item]s if the pickpocket succeeds, or `null` if the attempt fails.
         */
        @JvmStatic
        fun pickpocketRoll(player: Player, low: Double, high: Double, table: WeightBasedTable): ArrayList<Item>? {
            var successMod = 0.0
            if (inEquipment(player, Items.GLOVES_OF_SILENCE_10075)) {
                successMod += 3
            }
            val chance = RandomFunction.randomDouble(1.0, 100.0)
            val failThreshold = RandomFunction.getSkillSuccessChance(low, high, getStatLevel(player, Skills.THIEVING)) + successMod
            return if (chance > failThreshold) null else table.roll()
        }
    }
}