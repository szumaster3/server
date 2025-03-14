package content.global.activity.jobs

import content.global.activity.jobs.dialogue.CheckJobDialogue
import content.global.activity.jobs.dialogue.TutorDialogue
import content.global.activity.jobs.impl.BoneBuryingJobs
import content.global.activity.jobs.impl.CombatJobs
import content.global.activity.jobs.impl.Employers
import content.global.activity.jobs.impl.ProductionJobs
import core.api.openDialogue
import core.api.sendDialogue
import core.api.sendItemDialogue
import core.api.sendNPCDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.StringUtils
import org.rs.consts.Items

class JobListener : InteractionListener {
    override fun defineListeners() {
        on(Items.TASK_LIST_13464, ITEM, "read") { player, _ ->
            val playerJobManager: JobManager = JobManager.getInstance(player)
            val job = playerJobManager.job
            val amount = playerJobManager.jobAmount

            if (job == null) {
                sendDialogue(player, "I have not been assigned a job!")
                return@on false
            }

            if (amount == 0) {
                sendDialogue(
                    player,
                    "I have completed my job. I should return to ${NPC(job.employer.npcId).name} for my reward.",
                )
                return@on true
            }

            when (job.type) {
                JobType.PRODUCTION -> {
                    job as ProductionJobs
                    val itemName = Item(job.itemId).name.lowercase()
                    val itemNamePluralized = if (amount > 1) StringUtils.plusS(itemName) else itemName
                    sendItemDialogue(player, job.itemId, "My job is to gather $amount more $itemNamePluralized.")
                }

                JobType.BONE_BURYING -> {
                    job as BoneBuryingJobs
                    val boneName = Item(job.boneIds.first()).name.lowercase()
                    val boneNamePluralized = if (amount > 1) StringUtils.plusS(boneName) else boneName
                    sendItemDialogue(
                        player,
                        job.boneIds.first(),
                        "My job is to bury $amount more $boneNamePluralized in the ${job.buryArea.title}.",
                    )
                }

                JobType.COMBAT -> {
                    job as CombatJobs
                    val monsterName = NPC(job.monsterIds.first()).name.lowercase()
                    val monsterNamePluralized = if (amount > 1) StringUtils.plusS(monsterName) else monsterName
                    sendNPCDialogue(
                        player,
                        job.monsterIds.first(),
                        "My job is to kill $amount more $monsterNamePluralized.",
                    )
                }
            }
            return@on true
        }

        on(
            Employers.values().map { employer -> employer.npcId }.toIntArray(),
            IntType.NPC,
            "work-for",
        ) { player, node ->
            val playerJobManager = JobManager.getInstance(player)
            val job = playerJobManager.job

            if (job != null && job.employer.npcId == node.id) {
                if (playerJobManager.jobAmount == 0) {
                    playerJobManager.rewardPlayer()
                    sendNPCDialogue(
                        player,
                        job.employer.npcId,
                        "There you go, thanks for your help. You're quite a skilled worker!",
                    )
                } else {
                    openDialogue(player, CheckJobDialogue(), node as NPC)
                }

                return@on true
            }

            openDialogue(player, TutorDialogue(), node as NPC)
            return@on true
        }
    }
}
