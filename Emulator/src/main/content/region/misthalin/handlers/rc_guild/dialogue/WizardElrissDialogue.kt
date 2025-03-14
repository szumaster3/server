package content.region.misthalin.handlers.rc_guild.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.*

/* TODO:
 * [ ] CHECK IF tiara is also accepted or if I made it up to myself at the time.:
 *     - Show every regular talisman or tiara available, excluding the elemental talisman for omni-talisman.
 * [x] - Restored npc descriptions.
 * [ ] - Check if task is possible to complete.
 * [ ] - ShowTopics - Exception risk - Undo or try to add method.
 * [ ] - Added onUseWith interaction [Talisman on NPC].
 */

/**
 * An established member of the Runecrafting Guild, a section of the
 * revolutionary Wizards' Tower in southern Misthalin. Elriss, apart
 * from managing the guild, is also its shop owner, selling goods in
 * exchange for Runecrafting guild tokens.
 */
@Initializable
class WizardElrissDialogue(
    player: Player? = null,
) : Dialogue(player) {
    /**
     * Experience reward for each shown talisman.
     */
    val xpPerTalisman =
        mapOf(
            Items.AIR_TALISMAN_1438 to 9.0,
            Items.MIND_TALISMAN_1448 to 9.0,
            Items.WATER_TALISMAN_1444 to 9.0,
            Items.EARTH_TALISMAN_1440 to 12.0,
            Items.FIRE_TALISMAN_1442 to 27.0,
            Items.BODY_TALISMAN_1446 to 50.0,
            Items.COSMIC_TALISMAN_1454 to 109.0,
            Items.CHAOS_TALISMAN_1452 to 171.0,
            Items.NATURE_TALISMAN_1462 to 531.0,
            Items.LAW_TALISMAN_1458 to 1428.0,
            Items.DEATH_TALISMAN_1456 to 4242.0,
            Items.BLOOD_TALISMAN_1450 to 6958.0,
        )

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val talismans = xpPerTalisman.keys

        /**
         * Guard for talisman task.
         */
        val startTask = getAttribute(player, GameAttributes.RC_GUILD_TALISMAN_TASK_START, false)
        if (startTask && talismans.any { inInventory(player, it) }) {
            playerl(FaceAnim.HAPPY, "I have talisman to show you")
            stage = 2
            return true
        }
        if (talismans.any { hasAnItem(player, it).container != null }) {
            npc("Show me all the talismans one by one.")
            stage = 2
        } else {
            npc("You don't have any required talismans. Come back when you find them.")
            stage = END_DIALOGUE
        }
        npcl(FaceAnim.HAPPY, "Welcome to the Runecrafting Guild.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        /**
         * Amount of tokens.
         */
        var buyAmount = 0

        /**
         * Search for omni talisman.
         */
        val hasOmniTalisman = hasAnItem(player, Items.OMNI_TALISMAN_13649).container != null

        /**
         * Complete talisman task.
         */
        val completeTask = getAttribute(player, GameAttributes.RC_GUILD_TALISMAN_TASK_COMPLETE, false)

        /**
         * Tracks whether the player has given at least one talisman during this interaction.
         */
        var hasGivenAny = false

        when (stage) {
            0 -> {
                showTopics(
                    IfTopic(
                        text = "I've lost my omni-talisman.",
                        toStage = 4,
                        showCondition = !hasOmniTalisman && completeTask,
                    ),
                    Topic("What is this place?", 5),
                    Topic("What can I do here?", 19),
                    IfTopic("Can I buy some tokens?", 200, !inInventory(player, Items.RUNECRAFTING_GUILD_TOKEN_13650)),
                    Topic(
                        "I have some tokens I'd like to cash in.",
                        1,
                        !inInventory(player, Items.RUNECRAFTING_GUILD_TOKEN_13650),
                    ),
                    Topic("Never mind.", END_DIALOGUE),
                )
            }
            1 -> {
                end()
                openInterface(player, Components.RCGUILD_REWARDS_779)
            }
            2 -> {
                val talismans = xpPerTalisman.keys
                if (completeTask) {
                    npc(
                        "You've already shown me that you have all the",
                        "talismans, which is why I've already given you an omni",
                        "talisman!",
                    )
                    stage = 60
                    return true
                }
                var allTalismansShown = true
                for (talisman in talismans) {
                    val shownTalisman = getAttribute(player, GameAttributes.RC_GUILD_TALISMAN + "_$talisman", false)
                    if (hasAnItem(player, talisman).container == player.inventory && !shownTalisman) {
                        setAttribute(player, GameAttributes.RC_GUILD_TALISMAN + "_$talisman", true)
                        val xpReward = xpPerTalisman[talisman] ?: 0.0
                        rewardXP(player, Skills.RUNECRAFTING, xpReward)
                        sendItemDialogue(player, talisman, "You show Elriss the ${getItemName(talisman)}.")
                        hasGivenAny = true
                    }
                    if (!shownTalisman) {
                        allTalismansShown = false
                    }
                }
                if (allTalismansShown) {
                    npc("Excellent! You've shown me enough talismans. I can", "give you an omni talisman now.")
                    stage = 3
                } else if (!hasGivenAny) {
                    npc("You don't have any new talismans for me. Come back when you find them.")
                    stage = END_DIALOGUE
                }
            }
            3 -> {
                sendItemDialogue(player!!, Items.OMNI_TALISMAN_13649, "Wizard Elriss gives you an omni-talisman.")
                addItemOrDrop(player!!, Items.OMNI_TALISMAN_13649)
                setAttribute(player!!, GameAttributes.RC_GUILD_TALISMAN_TASK_COMPLETE, true)
                end()
            }
            4 -> {
                end()
                npc("Oh, well. Here's another one. Do try to be careful with", "it this time.")
                addItemOrDrop(player, Items.OMNI_TALISMAN_13649)
            }

            5 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "This is the Runecrafting Guild, as I said. After the",
                    "secret of Runecrafting was re-discovered, I set up the",
                    "guild as a place for the most advanced runecrafters to",
                    "work together.",
                ).also {
                    stage++
                }

            6 ->
                options(
                    "Work together towards what?",
                    "Where are we exactly?",
                    "What can I do here?",
                    "Can I buy some tokens?",
                    "Never mind.",
                ).also {
                    stage++
                }

            7 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "Work together towards what?").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_ASKING, "Where are we exactly?").also { stage = 38 }
                    3 -> playerl(FaceAnim.HALF_ASKING, "What can I do here?").also { stage = 19 }
                    4 -> playerl(FaceAnim.HALF_ASKING, "Can I buy some tokens?").also { stage = 200 }
                    5 -> player(FaceAnim.NEUTRAL, "Never mind.").also { stage = END_DIALOGUE }
                }

            8 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Towards a greater understanding of Runecrafting, of",
                    "course. The basics of Runecrafting may have been re-",
                    "discovered, but many of the secrets of the first",
                    "Wizards' Tower remain unknown.",
                ).also {
                    stage++
                }

            9 ->
                options(
                    "What secrets?",
                    "Where are we exactly?",
                    "What can I do here?",
                    "Never mind.",
                ).also { stage++ }

            10 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "What secrets?").also { stage++ }
                    2 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "Acantha and Vief are hardly working together!",
                        ).also { stage++ }

                    3 -> playerl(FaceAnim.HALF_ASKING, "Where are we exactly?").also { stage = 38 }
                    4 -> playerl(FaceAnim.HALF_ASKING, "What can I do here?").also { stage = 19 }
                    5 -> player(FaceAnim.NEUTRAL, "Never mind.").also { stage = END_DIALOGUE }
                }

            11 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Oh, nothing to interest an adventurer such as yourself",
                    "I'm sure.",
                ).also { stage++ }

            12 -> options("I am interested.", "Yeah, I'm not really interested.").also { stage++ }
            13 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "I am interested.").also { stage++ }
                    2 ->
                        playerl(FaceAnim.HALF_ASKING, "Yeah, I'm not really interested.").also {
                            stage = END_DIALOGUE
                        }
                }

            14 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "We all have our projects, adventurer. Acantha and Vief",
                    "are happy to involve junior runecrafters in their feud,",
                    "but others prefer to keep their research private until it",
                    "is revealed.",
                ).also {
                    stage++
                }

            15 ->
                npc(
                    FaceAnim.SAD,
                    "An idea may be subject to cruel ridicule if it is aired",
                    "prematurely, as I have learned to my cost. You must",
                    "forgive me if I am not so forthcoming again.",
                ).also {
                    stage++
                }

            16 -> options("Never mind, then.", "Go on, tell me.").also { stage++ }
            17 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "Never mind, then.").also { stage = END_DIALOGUE }
                    2 -> playerl(FaceAnim.HALF_ASKING, "Go on, tell me.").also { stage++ }
                }

            18 -> npc(FaceAnim.FRIENDLY, "Leave me be!").also { stage = END_DIALOGUE }
            19 ->
                npc(
                    "Wizard Acantha and Wizard Vief are running The",
                    "Great Orb Project. It requires large numbers of",
                    "runecrafters, so you should speak with them if you",
                    "want something to do.",
                ).also {
                    stage++
                }

            20 ->
                options(
                    "Tell me about Acantha and Vief's project.",
                    "Tell me about the omni-talisman.",
                    "Tell me about Wizard Korvak's pouch repairs.",
                    "Never mind.",
                ).also {
                    stage++
                }

            21 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "Tell me about Acantha and Vief's project.").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_ASKING, "Tell me about the omni-talisman.").also { stage = 54 }
                    3 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "Tell me about Wizard Korvak's pouch repairs.",
                        ).also { stage = 58 }

                    4 -> playerl(FaceAnim.HALF_ASKING, "Never mind.").also { stage = END_DIALOGUE }
                }

            22 ->
                npc(
                    "The Orb Proj...I beg your pardon, The Great Orb",
                    "Project? It's truly fascinating. Wizards Acantha and",
                    "Vief have found that energy leaks out of some of the",
                    "Runecrafting altars. They are recruiting teams of",
                ).also {
                    stage++
                }

            23 -> npc("experienced runecrafters such as yourself, to force the", "energy back in.").also { stage++ }
            24 ->
                npc(
                    "Join one of the teams by speaking to Wizard Acantha",
                    "or Wizard Vief. When the wizards have enough helpers,",
                    "I will open a portal to the Air Altar.",
                ).also {
                    stage++
                }

            25 ->
                npc(
                    "The energy appears in the form of floating orbs. These",
                    "can be moved by means of wands that attract or repel",
                    "them. Acantha or Vief will give you one of each wand.",
                ).also {
                    stage++
                }

            26 ->
                npc(
                    "Your goal is to move the correct colour orb to the altar",
                    "stone, while keeping the other orbs away. Wizard",
                    "Acantha favours green orbs, while Wizard Vief",
                    "favours yellow ones.",
                ).also {
                    stage++
                }

            27 ->
                npc(
                    "You will also have a third magic wand, which allows you to",
                    "create magical barriers to block the opposing team's",
                    "orbs.",
                ).also {
                    stage++
                }

            28 ->
                npc(
                    "After two minutes the team that absorbed the most orbs",
                    "wins that altar. I then open the portal to the next altar",
                    "in the sequence. After you have visited all eight altars,",
                    "you will be returned here.",
                ).also {
                    stage++
                }

            29 ->
                options(
                    "What's in it for me?",
                    "Could you go over the instruction again?",
                    "Which colour orb is best?",
                    "Thanks.",
                ).also {
                    stage++
                }

            30 ->
                when (buttonId) {
                    1 -> player("What's in it for me?").also { stage = 46 }
                    2 -> player("Could you go over the instruction again?").also { stage = 24 }
                    3 -> player("Which colour orb is best?").also { stage++ }
                    4 -> player("Thanks.").also { stage = END_DIALOGUE }
                }

            31 ->
                npc(
                    "Wizard Acantha believes that the green orbs are best.",
                    "Wizard Vief believe that the yellow ones are. You",
                    "should help out the wizard whose team you join.",
                ).also {
                    stage++
                }

            32 -> player("But what do you think?").also { stage++ }
            33 -> npc("does it metter?").also { stage++ }
            34 -> options("Of course it matters!", "No, I suppose not.", "Never mind.").also { stage++ }
            35 ->
                when (buttonId) {
                    1 -> player("Of course it matters!").also { stage = 37 }
                    2 -> player("No, I suppose not.").also { stage++ }
                    3 -> player("Never mind.").also { stage = END_DIALOGUE }
                }

            36 ->
                npc(
                    "No. The important thing is that the orbs get pushed",
                    "back into the altars, whatever colour they are.",
                ).also {
                    stage = 1
                }

            37 ->
                npc(
                    "Of course it does, of course it does. Be careful",
                    "which team you join, then.",
                    "I'll accept your reward tokens, either way.",
                ).also {
                    stage = 1
                }

            38 ->
                npc(
                    "You will notice that, whenever you use a Runecrafting",
                    "altar, you enter another plane: a self-contained island, or",
                    "cave, or some other place, which contains the true altar.",
                ).also {
                    stage++
                }

            39 ->
                npc(
                    "These temples are not exactly in " + GameWorld.settings!!.name + ". They are pocket",
                    "dimensions unto themselves: areas of folded space created",
                    "by the energy of the rune altar.",
                ).also {
                    stage++
                }

            40 ->
                options(
                    "So we're in something similar?",
                    "What does that have to do with the guild?",
                    "Not the Astral Altar.",
                ).also {
                    stage++
                }

            41 ->
                when (buttonId) {
                    1 -> player("So we're in something similar?").also { stage = 42 }
                    2 -> player("What does that have to do with the guild?").also { stage = 45 }
                    3 -> player("Not the Astral Altar. That has no pocket dimension.").also { stage = 43 }
                }

            42 ->
                npc(
                    "Quite right. This is a shadow of Wizard's",
                    "Tower, What better place to study the",
                    "mysteries of Runecrafting?",
                ).also {
                    stage = 40
                }

            43 ->
                npc(
                    "Quite right. I have heard of the Astral Altar,",
                    "although I have not been there myself. The lunar",
                    "wizards have found a way to keep the altar open.",
                ).also {
                    stage++
                }

            44 ->
                npc(
                    "Their magic has flattened out the space around the",
                    "altar so the pocket dimension becomes part of normal space.",
                ).also {
                    stage = 1
                }

            45 ->
                npc(
                    "Don't you see? The Runecrafting Guild exists in",
                    "a similar pocket dimension, created by our own magic.",
                    "What better place to study the mysteries of Runecrafting?",
                ).also {
                    stage = 1
                }

            46 ->
                npc(
                    "A fair question. We have agreed on a token scheme",
                    "that allows you to choose from several rewards. When",
                    "you return from the last altar, your senior wizard will",
                    "give you a number of tokens.",
                ).also {
                    stage++
                }

            47 ->
                npc(
                    "You will get 50 tokens per altar that your team captured,",
                    "provided that you contributed to the capture in some way.",
                    "You will get an extra 100 tokens if your team captured",
                    "more altars overall, or 50 extra if it is a draw.",
                ).also {
                    stage++
                }

            48 ->
                npc(
                    "You can exchange the tokens for rewards by speaking to me.",
                    "You may also find rune essence appearing in your inventory",
                    "at the end of each round. This is a side-product of the",
                    "absorption process and you are free to use it as you wish.",
                ).also {
                    stage = 1
                }

            49 ->
                options(
                    "What rewards are there?",
                    "Could you go over the instruction again?",
                    "Which colour orb is best?",
                    "Thanks.",
                ).also {
                    stage++
                }

            50 ->
                when (buttonId) {
                    1 -> player("What rewards are there?").also { stage = 51 }
                    2 -> player("Could you go over the instruction again?").also { stage = 24 }
                    3 -> player("Which colour orb is best?").also { stage = 31 }
                    4 -> player("Thanks.").also { stage = END_DIALOGUE }
                }

            51 ->
                npc(
                    "The rewards include runemaster robes, designed to",
                    "protect you while Runecrafting. These robes also",
                    "let you move orbs a little further - if you wear",
                    "robes of the same colour as the orb.",
                ).also {
                    stage++
                }

            52 ->
                npc(
                    "Another reward is the Runecrafting staff. This",
                    "can be combined with a talisman, in the same way",
                    "that a tiara can.",
                ).also {
                    stage++
                }

            53 ->
                npc(
                    "I also offer teleport tablets to the various altars.",
                    "You may also trade your tokens in for talismans and",
                    "certificates you can exchange at a bank for",
                    "rune essence.",
                ).also {
                    stage = 1
                }

            54 ->
                npc(
                    "Ever since the Duke of Lumbridge sent the first air",
                    "talisman to Sedridor, I have studied the talismans in",
                    "great detail. I believe I can create a new form of",
                    "talisman that combines the properties of all of them.",
                ).also {
                    stage++
                }

            55 ->
                npc(
                    "The omni-talisman will allow you to access any of the",
                    "Runecrafting altars. It can be combined with a tiara or",
                    "a staff, just like an ordinary talisman.",
                ).also {
                    stage++
                }

            56 ->
                npc(
                    "If you show me each type of known talisman, I will",
                    "create an omni-talisman for you. For each talisman you",
                    "show me, I will also teach you a bit about Runecrafting.",
                ).also {
                    setAttribute(player, GameAttributes.RC_GUILD_TALISMAN_TASK_START, true)
                    stage = 57
                }

            57 ->
                showTopics(
                    Topic("I have a talisman to show you.", 2),
                    Topic("Never mind.", END_DIALOGUE),
                )

            58 ->
                npc(
                    "Wizard Korvak is the only one of us to have",
                    "visited the Abyss. He learned about rune pouches",
                    "and how to repair them.",
                ).also {
                    stage++
                }

            59 ->
                npc(
                    "None of us quite knows how he does it, and",
                    "I'm not sure he does either, but it seems to work.",
                ).also {
                    stage = 20
                }

            60 -> player("Oh yeah.").also { stage = END_DIALOGUE }

            200 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Sure, there will be no problem with it, the cost of one token is 100 coins, how many will you need?",
                ).also {
                    stage = 201
                }

            201 -> {
                /**
                 * Contractual permission to purchase tokens in exchange for coins.
                 * Similar to FOG.
                 */
                setTitle(player, 4)
                sendDialogueOptions(
                    player,
                    "How many tokens do you need?",
                    "50",
                    "250",
                    "1000",
                    "5000",
                ).also { stage++ }
            }

            202 ->
                when (buttonId) {
                    1 -> buyAmount = 50
                    2 -> buyAmount = 250
                    3 -> buyAmount = 1000
                    4 -> buyAmount = 5000
                    else -> buyAmount = 0
                }.also {
                    if (buyAmount > 0) {
                        if (player?.inventory?.containsItem(Item(995, 100 * buyAmount))!!) {
                            player?.inventory?.add(Item(13650, buyAmount))
                            player?.inventory?.remove(Item(995, 100 * buyAmount))
                        } else {
                            sendMessage(player, "You don't have enough coins for that.")
                        }
                    }
                    end()
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WIZARD_ELRISS_8032)
    }
}
