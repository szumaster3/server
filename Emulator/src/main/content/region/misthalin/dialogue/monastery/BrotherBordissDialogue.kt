package content.region.misthalin.dialogue.monastery

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class BrotherBordissDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        // val hasQuest = hasRequirement(player!!, Quests.PERILS_OF_ICE_MOUNTAIN, false)
        // if(!hasQuest) {
        //     npc(FaceAnim.OLD_NORMAL, " ")
        //     stage = 24
        //     return true
        // }
        npc(FaceAnim.OLD_NORMAL, "Hello again!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean {
        when (stage) {
            0 -> if (getSigil(player) != null && inInventory(player, Items.BLESSED_SPIRIT_SHIELD_13736, 1)) {
                npc(FaceAnim.OLD_HAPPY, "Say, what are you carrying there?").also { stage++ }
            } else {
                showTopics(
                    Topic("I still don't understand why Ice Mountain was heating up.", 15),
                    Topic("How is the windmill doing?", 22),
                )
            }
            1 -> {
                showTopics(
                    IfTopic("A blessed spirit shield and a sigil.", 2, getSigil(player) != null && inInventory(player, Items.BLESSED_SPIRIT_SHIELD_13736, 1)),
                    Topic("I still don't understand why Ice Mountain was heating up.", 15),
                    Topic("How is the windmill doing?", 22),
                )
            }
            2 -> npc(FaceAnim.OLD_DEFAULT, "This is very impressive. You know, back before I", "became a monk, I used to do a fair bit of smithing. If", "you want, I could attach a sigil to that shield of yours.").also { stage++ }
            3 -> options("Yes, please.", "No, thanks.").also { stage++ }
            4 -> when (buttonId) {
                1 -> player("Yes, please").also { stage++ }
                2 -> player("No, thanks.").also { stage = 14 }
            }
            5 -> npc(FaceAnim.OLD_DEFAULT, "I'm afraid I must ask you for a small payment, though.", "You have done so much for me already, so I don't", "really feel comfortable doing so, but I am in need of", "money for upkeep of the power station. As you may").also { stage++ }
            6 -> npc(FaceAnim.OLD_DEFAULT, "already be aware, building materials in Keldagrim aren't", "exactly cheap.").also { stage++ }
            7 -> player(FaceAnim.HALF_ASKING, "So, how much are we talking here?").also { stage++ }
            8 -> npc(FaceAnim.OLD_DEFAULT, "I think 1,500,000 coins should be enough.").also { stage++ }
            9 -> options("Yes, that shouldn't be a problem.", "Oh no, that is way too expensive.").also { stage++ }
            10 -> when (buttonId) {
                1 -> player("Yes, that shouldn't be a problem.").also { stage++ }
                2 -> player("Oh no, that is way too expensive.").also { stage = 14 }
            }
            11 -> npc(FaceAnim.OLD_NORMAL, "So, you want me to attach the divine sigil to your", "blessed spirit shield for price of 1,500,000 coins?").also { stage++ }
            12 -> options("Yes, I do.", "No, I've changed my mind.").also { stage++ }
            13 -> when (buttonId) {
                1 -> {
                    end()

                    val sigil = getSigil(player)
                    if (sigil == null) {
                        end()
                        return true
                    }

                    val itemName = getItemName(sigil.id).lowercase()
                    val shieldItem = getShield(sigil)
                    val productID = getItemName(shieldItem!!.id)

                    val coins = Item(Items.COINS_995, 1500000)
                    val blessedShield = Item(Items.BLESSED_SPIRIT_SHIELD_13736, 1)

                    if (!player.inventory.contains(sigil.id, 1) || !player.inventory.containsItem(coins) || !player.inventory.contains(blessedShield.id, 1)) {
                        player.sendMessage("You don't have the required items to combine.")
                        return true
                    }

                    player.inventory.remove(sigil)
                    player.inventory.remove(coins)
                    player.inventory.remove(blessedShield)

                    sendItemDialogue(player, "Bordiss skillfully attaches the $itemName to the blessed", "spirit shield and creates a $productID.")
                    player.inventory.add(shieldItem)

                    return true
                }
                2 -> player("Oh no, that is way too expensive.").also { stage = 14 }
            }
            14 -> npcl(FaceAnim.OLD_NORMAL, "That's a shame, then.").also { stage = END_DIALOGUE }
            15 -> npcl(FaceAnim.OLD_DEFAULT, "No, I suppose I never did get a chance to explain it. It's something I learned from the druids of Guthix. It's about the balance of the atmosphere.").also { stage++ }
            16 -> npcl(FaceAnim.OLD_DEFAULT, "Sunlight causes the world to heat up but, normally, excess heat radiates upwards into the sky and away from ${GameWorld.settings!!.name}.").also { stage++ }
            17 -> npcl(FaceAnim.OLD_NORMAL, "So, the land remains at a constant temperature.").also { stage = 14 }
            18 -> npcl(FaceAnim.OLD_DEFAULT, "Different gases trap heat more or less well. The smoke from the dragon power station trapped heat very well, more so than normal air.").also { stage++ }
            19 -> npcl(FaceAnim.OLD_DEFAULT, "You've had a taste of what would happen if the gases increased. Plants would die - except it wouldn't just be Monastery roses, it would be crops people needed to eat.").also { stage++ }
            20 -> npcl(FaceAnim.OLD_DEFAULT, "Creatures would lose their habitat and go extinct - not just vulnerable icefiends, but all sorts of creatures.").also { stage++ }
            21 -> npcl(FaceAnim.OLD_DEFAULT, "Heat would make water expand and ice melt, so sea levels would rise, and that would mean coastal cities flooding, as well as drowning underground cities like Keldagrim.").also { stage++ }
            22 -> npcl(FaceAnim.OLD_DEFAULT, "If it were to go unchecked, it would mean the end of dwarven civilisation. Human civilisation too. Thank Saradomin you stopped it in time.").also { stage = END_DIALOGUE }
            23 -> npcl(FaceAnim.OLD_DEFAULT, "Quite well, actually. It looks like my estimates were all correct and the wind here is quite enough to power Nurmof's machine.").also { stage = END_DIALOGUE }

            /*
             * Perils of Ice Mountain dialogues.
             */

            24 -> sendNPCDialogue(player, NPCs.BROTHER_ALTHRIC_2588, "I'm afraid Brother Bordiss has taken a vow of silence.").also { stage++ }
            25 -> npc(FaceAnim.OLD_DEFAULT, " ").also { stage++ }
            26 -> {
                showTopics(
                    IfTopic("I have a latter for you.", 27, inInventory(player, Items.LETTER_13229)),
                    Topic("So you've taken a vow of silence, then?", 40),
                    Topic("(Say nothing)", 45)
                )
            }
            27 -> sendItemDialogue(player!!, Items.LETTER_13229, "Brother Bordiss reads the letter with growing anger.").also { stage++ }
            28 -> npcl(FaceAnim.OLD_ANGRY1, "Zamorak's fiery hooves!").also { stage++ }
            29 -> sendNPCDialogue(player, NPCs.BROTHER_ALTHRIC_2588, "Brother Bordiss! Your vow of silence!").also { stage++ }
            30 -> npcl(FaceAnim.OLD_ANGRY1, "It's Drorkar! First he builds his great Zamorakian smoke-machine, and now he's sent me this letter taunting me about it! That filthy little-").also { stage++ }
            31 -> sendNPCDialogue(player, NPCs.BROTHER_ALTHRIC_2588, "Bordiss! Language!").also { stage++ }
            32 -> npcl(FaceAnim.OLD_ANGRY1, "Look what he's written! 'My coal-dragon power station is now running at full capacity. You said this could never be done, but I have proved to be the better dwarf.'").also { stage++ }
            33 -> npcl(FaceAnim.OLD_ANGRY1, "Lies! I didn't say it couldn't be done, I said it shouldn't be done! This power station will be a disaster for dwarves and everyone else.").also { stage++ }
            34 -> sendNPCDialogue(player, NPCs.BROTHER_ALTHRIC_2588, "Brother, you are letting Zamorak's anger overtake you. You have been such a pious servant of Saradomin; do not stray from the path now.").also { stage++ }
            35 -> npcl(FaceAnim.OLD_ANGRY1, "I...I am sorry, Brother Althric. I will renew my vow of silence.").also { stage++ }
            36 -> sendNPCDialogue(player, NPCs.BROTHER_ALTHRIC_2588, "I am sorry for my brother's outburst. The path of the Saradominist monk is a demanding one and Brother Bordiss is one of our newest acolytes, so I am sure you will forgive him.").also { stage++ }
            37 -> sendNPCDialogue(player, NPCs.BROTHER_ALTHRIC_2588, "Once my brother has achieved true peace and serenity he will never make similar- Great Saradomin!").also { stage++ }
            38 -> playerl(FaceAnim.HALF_ASKING, "What's the matter?").also { stage++ }
            39 -> sendNPCDialogue(player, NPCs.BROTHER_ALTHRIC_2588, "My roses! Look! They're dying!").also { stage = END_DIALOGUE }

            40 -> npc(FaceAnim.OLD_DEFAULT, " ").also { stage++ }
            41 -> playerl(FaceAnim.HALF_ASKING, "How's that working out for you?").also { stage++ }
            42 -> npc(FaceAnim.OLD_DEFAULT, " ").also { stage++ }
            43 -> playerl(FaceAnim.HALF_ASKING, "Well, I suppose I'll leave you to it, then.").also { stage++ }
            44 -> npc(FaceAnim.OLD_DEFAULT, " ").also { stage = END_DIALOGUE }

            45 -> npc(FaceAnim.OLD_DEFAULT, " ").also { stage++ }
            46 -> player(FaceAnim.NEUTRAL, " ").also { stage++ }
            47 -> npc(FaceAnim.OLD_DEFAULT, " ").also { stage++ }
            48 -> sendNPCDialogue(player, NPCs.BROTHER_ALTHRIC_2588, "Well, I suppose I'll leave you to it, then.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(
        NPCs.BROTHER_BORDISS_7724,
        /*NPCs.BROTHER_BORDISS_*/ 7730
    )

    /**
     * Returns the first found sigil.
     */
    private fun getSigil(player: Player): Item? {
        for (sigil in arrayOf(
            Items.ARCANE_SIGIL_13746,
            Items.DIVINE_SIGIL_13748,
            Items.SPECTRAL_SIGIL_13752,
            Items.ELYSIAN_SIGIL_13750,
        )) {
            if (inInventory(player, sigil, 1)) return Item(sigil)
        }
        return null
    }

    /**
     * Returns the spirit shield item based on the provided sigil.
     */
    private fun getShield(sigil: Item): Item? = when (sigil.id) {
        Items.ARCANE_SIGIL_13746 -> Item(Items.ARCANE_SPIRIT_SHIELD_13738)
        Items.ELYSIAN_SIGIL_13750 -> Item(Items.ELYSIAN_SPIRIT_SHIELD_13742)
        Items.DIVINE_SIGIL_13748 -> Item(Items.DIVINE_SPIRIT_SHIELD_13740)
        Items.SPECTRAL_SIGIL_13752 -> Item(Items.SPECTRAL_SPIRIT_SHIELD_13744)
        else -> null
    }
}
