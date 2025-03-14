package content.region.misthalin.quest.rag

import content.region.misthalin.quest.rag.handlers.BoneBoiler
import core.api.getAttribute
import core.api.inInventory
import core.api.removeAttributes
import core.api.rewardXP
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.Quests

@Initializable
class RagAndBoneMan : Quest(Quests.RAG_AND_BONE_MAN, 100, 99, 2, 714, 0, 1, 4) {
    companion object {
        const val attributeGoblinBone = "/save:quest:rag_goblinbonesubmit"
        const val attributeBearBone = "/save:quest:rag_bearbonesubmit"
        const val attributeBigFrogBone = "/save:quest:rag_bigfrogbonesubmit"
        const val attributeRamBone = "/save:quest:rag_rambonesubmit"
        const val attributeUnicornBone = "/save:quest:rag_unicornbonesubmit"
        const val attributeMonkeyBone = "/save:quest:rag_monkeybonesubmit"
        const val attributeGiantRatBone = "/save:quest:rag_giantratbonesubmit"
        const val attributeGiantBatBone = "/save:quest:rag_giantbatbonesubmit"

        val requiredBonesMap =
            mapOf(
                Items.GOBLIN_SKULL_7814 to attributeGoblinBone,
                Items.BEAR_RIBS_7817 to attributeBearBone,
                Items.BIG_FROG_LEG_7910 to attributeBigFrogBone,
                Items.RAM_SKULL_7820 to attributeRamBone,
                Items.UNICORN_BONE_7823 to attributeUnicornBone,
                Items.MONKEY_PAW_7856 to attributeMonkeyBone,
                Items.GIANT_RAT_BONE_7826 to attributeGiantRatBone,
                Items.GIANT_BAT_WING_7829 to attributeGiantBatBone,
            )
    }

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        when (stage) {
            0 -> {
                line(player, "I can start this quest by talking to the !!Odd Old Man?? to the", line++)
                line(player, "West of the !!Limestone Mine??", line)
            }

            in 1..3 -> {
                line++
                line(player, "I have spoken to the Odd Old Man and have agreed to help him ", line++, true)
                line(player, "complete his collection of bones. I should check which ones", line++, true)
                line(player, "he needs.", line++, true)
                line++
                line(player, "The !!Odd Old Man?? has instructed me on which bones to collect", line++, false)
                line(player, "and how to prepare them. I must find !!whole, unbroken bones??", line++, false)
                line(player, "and put them into a !!pot of vinegar??. I must then put some", line++, false)
                line(player, "!!logs?? under the !!pot boiler??, !!put the bone in vinegar on it??,", line++, false)
                line(player, "!!and light the logs??. This will clean the bone.", line++, false)
                line++
                line(player, "I need to buy the !!vinegar?? from the !!wine merchant?? in !!Draynor??.", line++, false)
                line++
                line(player, "I need to give the !!Odd Old Man?? the following polished bones:", line++, false)
                boneChecklist(player, line, "Goblin", BoneBoiler.GOBLIN_SKULL, attributeGoblinBone)
                line++
                boneChecklist(player, line, "Bear", BoneBoiler.BEAR_RIBS, attributeBearBone)
                line++
                boneChecklist(player, line, "Big frog", BoneBoiler.BIG_FROG_LEG, attributeBigFrogBone)
                line++
                boneChecklist(player, line, "Ram", BoneBoiler.RAM_SKULL, attributeRamBone)
                line++
                boneChecklist(player, line, "Unicorn", BoneBoiler.UNICORN_BONE, attributeUnicornBone)
                line++
                boneChecklist(player, line, "Monkey", BoneBoiler.MONKEY_PAW, attributeMonkeyBone)
                line++
                boneChecklist(player, line, "Giant rat", BoneBoiler.GIANT_RAT_BONE, attributeGiantRatBone)
                line++
                boneChecklist(player, line, "Giant bat", BoneBoiler.GIANT_BAT_WING, attributeGiantBatBone)
            }

            100 -> {
                line++
                line(player, "I have spoken to the Odd Old Man and have agreed to help him ", line++, true)
                line(player, "complete his collection of bones. I should check which ones", line++, true)
                line(player, "he needs.", line++, true)
                line++
                line(player, "The !!Odd Old Man?? has instructed me on which bones to collect", line++, true)
                line(player, "and how to prepare them. I must find !!whole, unbroken bones??", line++, true)
                line(player, "and put them into a !!pot of vinegar??. I must then put some", line++, true)
                line(player, "!!logs?? under the !!pot boiler??, !!put the bone in vinegar on it??,", line++, true)
                line(player, "!!and light the logs??. This will clean the bone.", line++, true)
                line++
                line(player, "I have given the last of the bones to the Odd Old Man.", line++, true)
                line(player, "I am sure he will reward me.", line++, true)
                line++
                line(player, "The Odd Old Man has given me a reward. I will see if I can", line++, false)
                line(player, "find any more bones from his wish list, and will bring them", line++, false)
                line(player, "them to him if I do.", line++, false)
                line++
                line(player, "%%QUEST COMPLETE!&&", line)
            }
        }
    }

    private fun boneChecklist(
        player: Player,
        line: Int,
        mob: String,
        boneEnum: BoneBoiler,
        questAttribute: String,
    ) {
        line(
            player,
            if (getAttribute(player, questAttribute, false)) {
                "!!$mob.??"
            } else if (inInventory(player, boneEnum.polishedBone)) {
                "!!$mob.?? (I have a prepared one !!with me.??)"
            } else if (inInventory(player, boneEnum.bone) || inInventory(player, boneEnum.boneInVinegar)) {
                "!!$mob.?? (I have an unprepared one !!with me.??)"
            } else {
                "!!$mob.??"
            },
            line,
            getAttribute(player, questAttribute, false),
        )
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed ${Quests.RAG_AND_BONE_MAN}!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.BONE_IN_VINEGAR_7813, 240, 277, 5)

        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "500 Cooking XP and 500", ln++)
        drawReward(player, "Prayer XP", ln)

        rewardXP(player, Skills.COOKING, 500.0)
        rewardXP(player, Skills.PRAYER, 500.0)

        removeAttributes(
            player,
            attributeGoblinBone,
            attributeBearBone,
            attributeBigFrogBone,
            attributeRamBone,
            attributeUnicornBone,
            attributeMonkeyBone,
            attributeGiantRatBone,
            attributeGiantBatBone,
        )
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
