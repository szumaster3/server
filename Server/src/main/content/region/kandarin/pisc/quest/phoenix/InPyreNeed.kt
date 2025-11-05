package content.region.kandarin.pisc.quest.phoenix

import content.data.GameAttributes
import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.*

@Initializable
class InPyreNeed : Quest(Quests.IN_PYRE_NEED, 162, 161, 1, PROGRESS, 0, 1, 30) {

    // https://runescape.wiki/w/Phoenix_eggling
    // https://runescape.wiki/w/Cracked_phoenix_egg
    // https://runescape.wiki/w/Phoenix_eggling?oldid=1105267

    companion object {
        /**
         * Quest progress varbit.
         */
        const val PROGRESS = Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761
        /**
         * Location of the Phoenix chamber entrance inside.
         */
        val PHOENIX_CHAMBER = Location(3566, 5224, 0)

        /**
         * Outside location of phoenix lair.
         */
        val CAVE_EXIT_LOCATION: Location = Location.create(2294, 3626, 0)

        /**
         * The types of twig item ids gathered from trees.
         */
        val TWIG_ID = intArrayOf(
            Items.CINNAMON_TWIGS_14606,
            Items.SASSAFRAS_TWIGS_14607,
            Items.AILANTHUS_TWIGS_14608,
            Items.CEDAR_TWIGS_14609,
            Items.MASTIC_TWIGS_14610
        )

        /**
         * The weaving ribbon item ids crafted from twigs.
         */
        val RIBBON_ID = intArrayOf(
            Items.CINNAMON_WEAVING_RIBBON_14611,
            Items.CEDAR_WEAVING_RIBBON_14614,
            Items.AILANTHUS_WEAVING_RIBBON_14613,
            Items.SASSAFRAS_WEAVING_RIBBON_14612,
            Items.MASTIC_WEAVING_RIBBON_14615
        )

        /**
         * The pet npc ids.
         */
        val PHOENIX_EGGLING_ID = intArrayOf(NPCs.LARGE_EGG_8552, NPCs.PHOENIX_EGGLING_8550)

        /**
         * Random chance for receiving pet.
         */
        val CHANCE_TO_RECEIVE_PET = RandomFunction.random(100)

        /**
         * The object ids to twig item ids map.
         */
        val itemMap = mapOf(
            Scenery.CINNAMON_TREE_41903 to Items.CINNAMON_TWIGS_14606,
            Scenery.SASSAFRAS_TREE_41904 to Items.SASSAFRAS_TWIGS_14607,
            Scenery.AILANTHUS_TREE_41905 to Items.AILANTHUS_TWIGS_14608,
            Scenery.CEDAR_TREE_41906 to Items.CEDAR_TWIGS_14609,
            Scenery.MASTIC_TREE_41907 to Items.MASTIC_TWIGS_14610,
        )

        /**
         * Logout listener string.
         */
        const val LOGOUT_LISTENER = "phoenix-lair"

        /**
         * The reborn warrior npc ids.
         */
        val REBORN_WARRIOR_ID = intArrayOf(NPCs.LESSER_REBORN_WARRIOR_8557, NPCs.LESSER_REBORN_WARRIOR_8558, NPCs.GREATER_REBORN_WARRIOR_8559, NPCs.GREATER_REBORN_WARRIOR_8560, NPCs.LESSER_REBORN_RANGER_8561, NPCs.LESSER_REBORN_RANGER_8562, NPCs.GREATER_REBORN_RANGER_8563, NPCs.GREATER_REBORN_RANGER_8564, NPCs.LESSER_REBORN_MAGE_8565, NPCs.LESSER_REBORN_MAGE_8566, NPCs.GREATER_REBORN_MAGE_8567, NPCs.GREATER_REBORN_MAGE_8568)

        /**
         * The non-hostile reborn warrior npc ids.
         */
        val NON_HOSTILE_IDS = intArrayOf(NPCs.LESSER_REBORN_MAGE_8573, NPCs.LESSER_REBORN_RANGER_8571, NPCs.LESSER_REBORN_WARRIOR_8569)

        /**
         * Instance of the wounded phoenix NPC, placed at a fixed location.
         */
        val WOUNDED_PHOENIX_ID: NPC = NPC.create(NPCs.WOUNDED_PHOENIX_8547, Location.create(3534, 5196, 0), Direction.NORTH)

        /**
         * Array of tree scenery object IDs present in the Phoenix Lair.
         */
        val TREE_SCENERY_ID = arrayOf(41903, 41904, 41905, 41906, 41907)

        /**
         * Set of possible tree locations where scenery can respawn.
         */
        val TREE_LOCATION_MAP = mutableSetOf(
            Location.create(3481, 5241, 0),
            Location.create(3514, 5205, 0),
            Location.create(3503, 5236, 0),
            Location.create(3473, 5209, 0),
            Location.create(3535, 5242, 0)
        )

        /**
         * Locations where NPCs will respawn.
         */
        val NPC_RESPAWNS = arrayOf(
            Location.create(3464, 5242, 0),
            Location.create(3460, 5237, 0),
            Location.create(3484, 5234, 0),
            Location.create(3479, 5225, 0),
            Location.create(3475, 5220, 0),
            Location.create(3460, 5222, 0),
            Location.create(3494, 5241, 0),
            Location.create(3498, 5243, 0),
            Location.create(3504, 5241, 0),
            Location.create(3502, 5233, 0),
            Location.create(3492, 5224, 0),
            Location.create(3496, 5222, 0),
            Location.create(3509, 5223, 0),
            Location.create(3516, 5227, 0),
            Location.create(3515, 5242, 0),
            Location.create(3526, 5240, 0),
            Location.create(3526, 5244, 0),
            Location.create(3531, 5236, 0),
            Location.create(3534, 5233, 0),
            Location.create(3527, 5225, 0),
            Location.create(3535, 5220, 0),
            Location.create(3547, 5222, 0),
            Location.create(3547, 5232, 0),
            Location.create(3545, 5240, 0),
            Location.create(3465, 5210, 0),
            Location.create(3462, 5205, 0),
            Location.create(3466, 5204, 0),
            Location.create(3466, 5211, 0),
            Location.create(3471, 5210, 0),
            Location.create(3480, 5212, 0),
            Location.create(3482, 5205, 0),
            Location.create(3484, 5196, 0),
            Location.create(3475, 5190, 0),
            Location.create(3494, 5211, 0),
            Location.create(3498, 5202, 0),
            Location.create(3497, 5192, 0),
            Location.create(3493, 5188, 0),
            Location.create(3515, 5188, 0),
            Location.create(3513, 5196, 0),
            Location.create(3514, 5208, 0),
            Location.create(3505, 5203, 0),
            Location.create(3504, 5210, 0),
            Location.create(3474, 5242, 0),
            Location.create(3480, 5243, 0),
        )

    }

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        val inPyreNeedProgress = getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761)

        if (stage == 0) {
            line(player, "I can start this quest by talking to the !!Priest of Guthix??,", line++)
            line(player, "near the cave south of the !!Piscatoris Fishing Colony??.", line++)
            line++
            line(player, "This quest has the following requirements:", line++)
            line(player, "Level 53 Fletching", line++, hasLevelStat(player, Skills.FLETCHING, 53))
            line(player, "Level 52 Crafting", line++, hasLevelStat(player, Skills.CRAFTING, 52))
            line(player, "Level 55 Firemaking", line++, hasLevelStat(player, Skills.FIREMAKING, 55))
            line++
        }

        if (stage >= 1) {
            line++
            line(player, "I spoke to the Priest of Guthix, near the cave south of", line++, inPyreNeedProgress >= 2)
            line(player, "the Piscatoris Fishing Colony.", line++, inPyreNeedProgress >= 2)
            line(player, "He wants me to enter a cave that is apparently the lair", line++, inPyreNeedProgress >= 2)
            line(player, "of the legendary phoenix, which was attacked and mortally", line++, inPyreNeedProgress >= 2)
            line(player, "wounded on its way back to its roost. I must complete a ritual, which", line++, inPyreNeedProgress >= 2)
            line(player, "he believes will ensure the phoenix's survival.", line++, inPyreNeedProgress >= 2)
            line++
        }

        if (inPyreNeedProgress > 1) {
            line(player, "I could enter the cave and try to figure it out for myself, but", line++, getAttribute(player, GameAttributes.TALK_WITH_PRIEST, 0) >= 3 || stage == 100 || inPyreNeedProgress > 5)
            line(player, "I should ask the Priest of Guthix for more information.", line++, getAttribute(player, GameAttributes.TALK_WITH_PRIEST, 0) >= 3 || stage == 100 || inPyreNeedProgress > 5)
            line++
        }

        if (inPyreNeedProgress >= 4) {
            line(player, "I have gathered the !!cinnamon twigs??.", line++, inInventory(player, Items.CINNAMON_TWIGS_14606) || stage == 100 || inPyreNeedProgress > 5)
            line(player, "I have fletched the !!cinnamon twigs?? into !!weaving ribbon??.", line++, inInventory(player, Items.CINNAMON_WEAVING_RIBBON_14611) || stage == 100 || inPyreNeedProgress > 5)
            line++
            line(player, "I have gathered the !!sassafras twigs??.", line++, inInventory(player, Items.SASSAFRAS_TWIGS_14607) || stage == 100 || inPyreNeedProgress > 5)
            line(player, "I have fletched the !!sassafras twigs?? into !!weaving ribbon??.", line++, inInventory(player, Items.SASSAFRAS_WEAVING_RIBBON_14612) || stage == 100 || inPyreNeedProgress > 5)
            line++
            line(player, "I have gathered the !!ailanthus twigs??.", line++, inInventory(player, Items.AILANTHUS_TWIGS_14608) || stage == 100 || inPyreNeedProgress > 5)
            line(player, "I have fletched the !!ailanthus twigs into !!weaving ribbon??.", line++, inInventory(player, Items.AILANTHUS_WEAVING_RIBBON_14613) || stage == 100 || inPyreNeedProgress > 5)
            line++
            line(player, "I have gathered the !!cedar twigs??.", line++, inInventory(player, Items.CEDAR_TWIGS_14609) || stage == 100 || inPyreNeedProgress > 5)
            line(player, "I have fletched the !!cedar twigs into !!weaving ribbon??.", line++, inInventory(player, Items.CEDAR_WEAVING_RIBBON_14614) || stage == 100 || inPyreNeedProgress > 5)
            line++
            line(player, "I have gathered the !!mastic twigs??.", line++, inInventory(player, Items.MASTIC_TWIGS_14610) || stage == 100 || inPyreNeedProgress > 5)
            line(player, "I have fletched the !!mastic twigs?? into !!weaving ribbon??.", line++, inInventory(player, Items.MASTIC_WEAVING_RIBBON_14615) || stage == 100 || inPyreNeedProgress > 5)
            line++
        }

        if (inPyreNeedProgress > 5) {
            line(player, "I have gathered all the twigs, and made my way to the phoenix's roost.", line++, allInInventory(player, *TWIG_ID) || stage == 100 || inPyreNeedProgress > 6)
            line(player, "The phoenix is here. It is gravely wounded. I should look around for", line++, allInInventory(player, *TWIG_ID) || stage == 100 || inPyreNeedProgress > 6)
            line(player, "clues as to what to do next.", line++, allInInventory(player, *TWIG_ID) || stage == 100 || inPyreNeedProgress > 6)
            line++
        }

        if (inPyreNeedProgress >= 6) {
            line(player, "I should hurry and fletch all the twigs into weaving ribbon,", line++, inPyreNeedProgress >= 7 || stage == 100)
            line(player, "construct the funeral pyre and light it.", line++, inPyreNeedProgress >= 7 || stage == 100)
            line++
        }

        if (inPyreNeedProgress >= 7) {
            line(player, "I have constructed and lit the funeral pyre. The phoenix", line++, stage == 100)
            line(player, "managed to limp over and throw itself into the fire.", line++, stage == 100)
            line++
            line(player, "It burned to ashes, and in a flash of flame was reborn.", line++, stage == 100)
            line++
            line(player, "I should speak to the phoenix and ensure all is okay before", line++, stage == 100)
            line(player, "returning to the Priest of Guthix.", line++, stage == 100)
            line++
        }

        if (inPyreNeedProgress >= 10) {
            line(player, "The reborn phoenix spoke to me, thanking me for helping it its", line++, stage == 100)
            line(player, "time of need.", line++, stage == 100)
            line(player, "As a reward, the phoenix gave me 5 phoenix quills, and also gave me", line++, stage == 100)
            line(player, "permission to challenge it to combat in its lair, once per day", line++, stage == 100)
            line(player, "once my combat skills are proven.", line++, stage == 100)
            line++
        }

        if (inPyreNeedProgress >= 10) {
            line(player, "I should meet the Priest of Guthix at the entrance to the phoenix's lair", line++, stage == 100)
            line(player, "to claim my reward from him, too.", line++, stage == 100)
            line++
            line(player, "I have spoken to the Priest of Guthix, who", line++, stage == 100)
            line(player, "thanked and rewarded me for my help.", line++, stage == 100)
        }

        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        displayQuestItem(player, Items.PHOENIX_QUILL_14616)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "14,400 Firemaking xp", ln++)
        drawReward(player, "12,500 Fletching xp", ln++)
        drawReward(player, "11,556 Crafting xp", ln++)
        drawReward(player, "Access to the pheonix lair", ln++)
        drawReward(player, "once per day", ln)
        rewardXP(player, Skills.FIREMAKING, 14400.0)
        rewardXP(player, Skills.FLETCHING, 12500.0)
        rewardXP(player, Skills.CRAFTING, 11556.0)
        addItemOrDrop(player, Items.PHOENIX_QUILL_14616, 5)
        setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 30, true)
    }

    override fun newInstance(`object`: Any?): Quest = this
}
/*
 * 1 quest point
 * 14,400 Firemaking experience
 * 12,500 Fletching experience
 * 11,556 Crafting experience
 * 5 phoenix quills worth 126,065 coins.
 * Access to the Phoenix Lair once per day; which is a Distraction and Diversion.
 * The chance to receive a Phoenix eggling pet while doing the Phoenix Lair.
 * Raising the pet requires level 72 Summoning. Players without level 72 Summoning
 * will receive a phoenix egg instead, which they can later hatch at the required level.
 * 2 Treasure Hunter keys (Ironman accounts will not receive these)
 */
