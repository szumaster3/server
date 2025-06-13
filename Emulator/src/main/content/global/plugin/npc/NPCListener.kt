package content.global.plugin.npc

import content.global.activity.ttrail.scrolls.AnagramClueScroll
import content.global.activity.ttrail.scrolls.ChallengeClueScroll
import content.global.ame.RandomEvents
import content.minigame.gnomecook.dialogue.GnomeCookingDialogue
import content.minigame.gnomecook.handlers.GC_BASE_ATTRIBUTE
import content.minigame.gnomecook.handlers.GC_JOB_COMPLETE
import content.minigame.gnomecook.handlers.GC_JOB_ORDINAL
import content.minigame.gnomecook.handlers.GnomeCookingTask
import content.region.kandarin.miniquest.barcrawl.BarcrawlManager
import content.region.kandarin.miniquest.barcrawl.BarcrawlType
import core.api.*
import core.api.interaction.openDepositBox
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.npc.IdleAbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.game.system.timer.impl.AntiMacro
import core.game.worldevents.events.HolidayRandomEventNPC
import core.game.worldevents.events.HolidayRandomEvents
import core.game.worldevents.events.HolidayRandoms
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class NPCListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles hair-cut interaction.
         */

        on(NPCs.HAIRDRESSER_598, IntType.NPC, "hair-cut") { player, node ->
            player.dialogueInterpreter.open(NPCs.HAIRDRESSER_598, (node as NPC?), true)
            return@on true
        }

        /*
         * Handles interaction with dummies.
         */

        on(dummySceneryIds, IntType.SCENERY, "attack", "hit") { player, _ ->
            lock(player, 3)
            animate(player, player.properties.attackAnimation)
            if (player.properties.currentCombatLevel < 8) {
                var experience = 5.0
                when (player.properties.attackStyle.style) {
                    WeaponInterface.STYLE_ACCURATE -> rewardXP(player, Skills.ATTACK, experience)
                    WeaponInterface.STYLE_AGGRESSIVE -> rewardXP(player, Skills.STRENGTH, experience)
                    WeaponInterface.STYLE_DEFENSIVE -> rewardXP(player, Skills.DEFENCE, experience)
                    WeaponInterface.STYLE_CONTROLLED -> {
                        experience /= 3.0
                        rewardXP(player, Skills.ATTACK, experience)
                        rewardXP(player, Skills.STRENGTH, experience)
                        rewardXP(player, Skills.DEFENCE, experience)
                    }
                }
            } else {
                sendMessage(player, "You swing at the dummy.")
                sendMessage(player, "There is nothing more you can learn from hitting a dummy.")
            }
            return@on true
        }

        /*
         * Handles banking interaction with Peer the seer NPC.
         */

        on(PEER_THE_SEER, IntType.NPC, "deposit") { player, _ ->
            if (!anyInEquipment(
                    player,
                    Items.FREMENNIK_SEA_BOOTS_1_14571,
                    Items.FREMENNIK_SEA_BOOTS_2_14572,
                    Items.FREMENNIK_SEA_BOOTS_3_14573,
                )
            ) {
                sendNPCDialogue(
                    player,
                    NPCs.PEER_THE_SEER_1288,
                    "Do not pester me, outerlander! I will only deposit items into the banks of those who have earned Fremennik sea boots!",
                    FaceAnim.ANNOYED,
                )
                return@on true
            }

            openDepositBox(player)
            sendString(player, "Peer the Seer's Deposits", Components.BANK_DEPOSIT_BOX_11, 12)
            return@on true
        }

        /*
         * Handles BarCrawl activity interaction with NPCs.
         */

        on(barCrawlNPCs, IntType.NPC, "talk-to", "talk") { player, node ->
            val type = BarcrawlType.forId(node.id)
            val instance = BarcrawlManager.getInstance(player)
            if (instance.isFinished || !instance.isStarted() || instance.isCompleted(type!!.ordinal)) {
                player.dialogueInterpreter.open(node.id, node)
            } else {
                player.dialogueInterpreter.open("barcrawl dialogue", node.id, type)
            }
            return@on true
        }

        /*
         * Handles disturb.
         */

        on(IntType.NPC, "disturb") { player, node ->
            if (node is IdleAbstractNPC) {
                if (node.canDisturb(player)) {
                    node.disturb(player)
                }
            }
            return@on true
        }

        /*
         * Basic interactions with NPCs.
         */

        on(IntType.NPC, "talk-to", "talk", "talk to") { player, node ->
            val npc = node.asNpc()

            if (AnagramClueScroll.handleClue(player, npc)) return@on true
            val clueScroll = ChallengeClueScroll.getClueForNpc(player, npc)
            if (clueScroll != null) {
                ChallengeClueScroll.handleNPC(player, npc, clueScroll)
                return@on true
            }

            if (RandomEvents.randomIDs.contains(node.id)) {
                if (AntiMacro.getEventNpc(player) == null ||
                    AntiMacro.getEventNpc(player) != node.asNpc() ||
                    AntiMacro
                        .getEventNpc(
                            player,
                        )?.finalized == true
                ) {
                    sendMessage(player, "They aren't interested in talking to you.")
                } else {
                    AntiMacro.getEventNpc(player)?.talkTo(node.asNpc())
                }
                return@on true
            }

            if (HolidayRandomEvents.holidayRandomIDs.contains(node.id) && node is HolidayRandomEventNPC) {
                if (HolidayRandoms.getEventNpc(player) == null ||
                    HolidayRandoms.getEventNpc(player) != node.asNpc() ||
                    HolidayRandoms
                        .getEventNpc(
                            player,
                        )?.finalized == true
                ) {
                    sendMessage(player, "They aren't interested in talking to you.")
                } else {
                    HolidayRandoms.getEventNpc(player)?.talkTo(node.asNpc())
                }
                return@on true
            }

            if (getAttribute(npc, "holiday_random_extra_npc", false) && HolidayRandoms.getEventNpc(player) != null) {
                HolidayRandoms.getEventNpc(player)?.talkTo(node.asNpc())
                return@on true
            }

            if (!npc.getAttribute("facing_booth", false)) {
                npc.faceLocation(player.location)
            }

            if (player.properties.combatPulse.getVictim() == npc) {
                sendMessage(player, "I don't think they have any interest in talking to me right now!")
                return@on true
            }

            if (npc.inCombat()) {
                sendMessage(player, "They look a bit busy at the moment.")
                return@on true
            }

            if (player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL", -1) != -1) {
                val job = GnomeCookingTask.values()[player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL", -1)]
                if (node.id == job.npc_id && !player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_COMPLETE", false)) {
                    player.dialogueInterpreter.open(GnomeCookingDialogue(job))
                    return@on true
                }
            }
            return@on player.dialogueInterpreter.open(npc.id, npc)
        }
    }

    companion object {
        const val PEER_THE_SEER = NPCs.PEER_THE_SEER_1288
        val barCrawlNPCs =
            intArrayOf(
                NPCs.BARTENDER_733,
                NPCs.BLURBERRY_848,
                NPCs.BARTENDER_735,
                NPCs.BARTENDER_739,
                NPCs.BARTENDER_737,
                NPCs.BARTENDER_738,
                NPCs.BARTENDER_731,
                NPCs.ZAMBO_568,
                NPCs.KAYLEE_3217,
                NPCs.EMILY_736,
                NPCs.BARTENDER_734,
            )
        val dummySceneryIds =
            intArrayOf(
                Scenery.DUMMY_2038,
                Scenery.DUMMY_15624,
                Scenery.DUMMY_15625,
                Scenery.DUMMY_15626,
                Scenery.DUMMY_15627,
                Scenery.DUMMY_15628,
                Scenery.DUMMY_15629,
                Scenery.DUMMY_15630,
                Scenery.DUMMY_18238,
                Scenery.DUMMY_25648,
                Scenery.DUMMY_PAWYA_CORPSE_28912,
                Scenery.DUMMY_823,
                Scenery.DUMMY_23921,
            )
    }
}
