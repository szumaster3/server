package content.global.plugin.npc

import content.global.activity.ttrail.plugin.AnagramScroll
import content.global.activity.ttrail.plugin.ChallengeClueScroll
import content.global.ame.RandomEvents
import content.minigame.gnomecook.dialogue.GCCompletionDialogue
import content.minigame.gnomecook.plugin.GC_BASE_ATTRIBUTE
import content.minigame.gnomecook.plugin.GC_JOB_COMPLETE
import content.minigame.gnomecook.plugin.GC_JOB_ORDINAL
import content.minigame.gnomecook.plugin.GnomeCookingJob
import content.region.kandarin.miniquest.barcrawl.BarcrawlManager
import content.region.kandarin.miniquest.barcrawl.BarcrawlType
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.IdleAbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.timer.impl.AntiMacro
import core.game.worldevents.events.HolidayRandomEventNPC
import core.game.worldevents.events.HolidayRandomEvents
import core.game.worldevents.events.HolidayRandoms
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

class NPCListener : InteractionListener {

    val PEER_THE_SEER = NPCs.PEER_THE_SEER_1288
    val BARCRAWL_NPC = intArrayOf(NPCs.BARTENDER_733, NPCs.BLURBERRY_848, NPCs.BARTENDER_735, NPCs.BARTENDER_739, NPCs.BARTENDER_737, NPCs.BARTENDER_738, NPCs.BARTENDER_731, NPCs.ZAMBO_568, NPCs.KAYLEE_3217, NPCs.EMILY_736, NPCs.BARTENDER_734)

    override fun defineListeners() {

        /*
         * Handles hair-cut interaction.
         */

        on(NPCs.HAIRDRESSER_598, IntType.NPC, "hair-cut") { player, node ->
            player.dialogueInterpreter.open(NPCs.HAIRDRESSER_598, (node as NPC?), true)
            return@on true
        }

        /*
         * Handles banking interaction with Peer the seer NPC.
         */

        on(PEER_THE_SEER, IntType.NPC, "deposit") { player, _ ->
            if (!isDiaryComplete(player, DiaryType.FREMENNIK, 0)) {
                sendNPCDialogue(player, NPCs.PEER_THE_SEER_1288, "Do not pester me, Outlander! I will only deposit items into the banks of those who have earned Fremennik sea boots!", FaceAnim.ANNOYED)
                return@on true
            }

            openDepositBox(player)
            sendString(player, "Peer the Seer's Deposits", Components.BANK_DEPOSIT_BOX_11, 12)
            return@on true
        }

        /*
         * Handles BarCrawl activity interaction with NPCs.
         */

        on(BARCRAWL_NPC, IntType.NPC, "talk-to", "talk") { player, node ->
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

            if (AnagramScroll.handleClue(player, npc)) return@on true
            val clueScroll = ChallengeClueScroll.getClueForNpc(player, npc)
            if (clueScroll != null) {
                val dialogue = ChallengeClueScroll.Companion.ChallengeDialogue(player, npc, clueScroll)
                player.dialogueInterpreter.open(dialogue, npc)
                return@on true
            }

            if (RandomEvents.randomIDs.contains(node.id)) {
                if (AntiMacro.getEventNpc(player) == null ||
                    AntiMacro.getEventNpc(player) != node.asNpc() ||
                    AntiMacro
                        .getEventNpc(player)?.finalized == true
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
                        .getEventNpc(player)?.finalized == true
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

            if (player.properties.combatPulse.victim == npc) {
                sendMessage(player, "I don't think they have any interest in talking to me right now!")
                return@on true
            }

            if (npc.inCombat()) {
                sendMessage(player, "They look a bit busy at the moment.")
                return@on true
            }

            if (player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL", -1) != -1) {
                val job = GnomeCookingJob.values()[player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL", -1)]
                if (node.id == job.npc_id && !player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_COMPLETE", false)) {
                    player.dialogueInterpreter.open(GCCompletionDialogue(job))
                    return@on true
                }
            }
            return@on player.dialogueInterpreter.open(npc.id, npc)
        }
    }
}
