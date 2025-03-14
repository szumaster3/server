package content.region.misthalin.handlers.rc_guild.dialogue

import content.global.skill.runecrafting.pouch.PouchManager
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/*
//===================================================================
// Repairing Pouches
// 9,000 coins for a Large Pouch  (50 RC level required)
// 12,000 coins for a Giant Pouch (75 RC level required)
// Note: The Dark mage in the Abyss can repair all pouches for free.
//===================================================================
// Purchasing Pouches
// 25,000 coins for a Large Pouch
// 50,000 coins for a Giant Pouch
//===================================================================
// Information
// He gives players medium pouches for free; however, he does not replace small pouches.
// Keep in mind that players can still buy pouches if they do not have the required level,
// but they will be unable to use these pouches.
// ==============================================================
// Sources
// Dialogue: https://runescape.wiki/w/Wizard_Korvak?oldid=1990011
// Guild: https://www.tip.it/runescape/pages/view/runecrafting_guild.htm
// Review: https://www.youtube.com/watch?v=FtLRZuhinvo&ab_channel=TheChronicNoob
// RC Map: https://runescape.wiki/images/archive/20100723065527%21Rune_Altar_Map.png?2314f
// Glitches & RC guild tab tp loc: https://www.youtube.com/watch?v=Hi49BcN81hU&ab_channel=runescapewordspread
//===================================================================
// TODO:
//  - [ ] Add that message to the object: "distracting the eyes in the Abyss to help you sneak past".
//  - [ ] Check Pouches IDs (There was fix for them but can't check now - compile consuming too much time).
//  - [ ] Check Repair method.
//===================================================================
*/
@Initializable
class WizardKorvakDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HAPPY, "AAAAAAAAAAH!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        // Constants
        val hasMediumPouch =
            hasAnItem(player, Items.MEDIUM_POUCH_5510).container != null ||
                hasAnItem(player, Items.MEDIUM_POUCH_5511).container != null
        val hasOmniItem = inInventory(player, Items.OMNI_TALISMAN_13649) || inInventory(player, Items.OMNI_TIARA_13655)
        val hasOmniTalisman = inInventory(player, Items.OMNI_TALISMAN_13649)
        val hasBlankTiara = inInventory(player, Items.TIARA_5525)
        val hasStaff = inInventory(player, Items.RUNECRAFTING_STAFF_13629)
        // End of constants.

        when (stage) {
            0 -> npc("Don't sneak up on me like that.").also { stage++ }
            1 -> player("Uh, sorry.").also { stage++ }
            2 ->
                if (!hasMediumPouch) {
                    player("I've lost my medium-sized pouch.", "Could you replace it?").also { stage = 35 }
                } else {
                    showTopics(
                        Topic("Can you help me with my essence pouches?", 18),
                        IfTopic(
                            "I've got an omni-talisman that I would like to attach to a tiara or staff.",
                            3,
                            hasOmniItem,
                        ),
                        Topic("Why are you so jumpy?", 7),
                        Topic("Never mind.", END_DIALOGUE),
                    )
                }

            3 -> {
                /*
                 * There's no earlier check for these items, so both options always shows.
                 */
                setTitle(player, 2)
                sendDialogueOptions(player!!, "Which would you like to make?", "Omni-tiara.", "Omni-staff.")
                stage++
            }

            4 ->
                when (buttonId) {
                    1, 2 ->
                        player(
                            "I'd like to attach my omni-talisman to a " +
                                if (buttonId == 1) "tiara." else "staff." + " Do you",
                            "know how to do that?",
                        ).also { stage++ }
                }

            5 -> {
                if (!hasOmniTalisman) {
                    player("Oi, I'm broke!")
                    stage = 36
                    return true
                }
                when {
                    hasBlankTiara -> {
                        npc("Perhaps...transform...carry the three and divide by the", "alteration factor - chickens.")
                        stage = 100
                    }

                    hasStaff -> {
                        npc("A little twist here, a little adhesive spell here. Kapow!")
                        stage = 200
                    }

                    else -> sendMessage(player, "You do not have the required items.")
                }
            }

            100 -> {
                /*
                 * Talisman + tiara.
                 */
                if (!anyInInventory(player, Items.OMNI_TALISMAN_13649, Items.TIARA_5525)) {
                    player("Oi, I'm broke!")
                    stage = 36
                    return true
                }
                if (removeItem(player, Item(Items.OMNI_TALISMAN_13649, 1)) &&
                    removeItem(
                        player,
                        Item(Items.TIARA_5525, 1),
                    )
                ) {
                    npc("There! A pretty tiara for a pretty lady.") // No gender check.
                    addItemOrDrop(player, Items.OMNI_TIARA_13655, 1)
                    stage = END_DIALOGUE
                }
            }

            200 -> {
                /*
                 * Talisman + staff.
                 */
                if (!anyInInventory(player, Items.OMNI_TALISMAN_13649, Items.RUNECRAFTING_STAFF_13629)) {
                    player("Oi, I'm broke!")
                    stage = 36
                    return true
                }
                if (removeItem(player, Item(Items.OMNI_TALISMAN_13649, 1)) &&
                    removeItem(
                        player,
                        Item(Items.RUNECRAFTING_STAFF_13629, 1),
                    )
                ) {
                    end()
                    npc("A staff for all your Runecrafting needs.")
                    addItemOrDrop(player, Items.OMNI_TALISMAN_STAFF_13642, 1)
                }
            }

            7 -> npc("I am not jumpy! I am insane. There is a difference.").also { stage++ }
            8 ->
                player(
                    "If you are insane, wouldn't you think you were sane",
                    "and so be insensible to the insanity that is you?",
                ).also { stage++ }

            9 -> npc("Yebno.").also { stage++ }
            10 -> player("Beg your pardon?").also { stage++ }
            11 ->
                npc(
                    "Yebno. It's a fast way of saying Yes Maybe No. Since",
                    "I didn't know the answer to your question I gave you",
                    "all of the answers.",
                ).also {
                    stage++
                }

            12 -> player("How did you end up in your rather peculiar mindset?").also { stage++ }
            13 ->
                npc(
                    FaceAnim.SAD,
                    "They sent me to the place. They knew the dark",
                    "wizards were there and someone had betrayed them.",
                    "Us. So they send me to spy. To the place.",
                    "They sent me and would not let me come back.",
                ).also {
                    stage++
                }

            14 -> player("who sent you? What betrayal?").also { stage++ }
            15 ->
                npc(
                    "He sits there with the spinning light, always thinking.",
                    "One of us led them to the place where the pickaxe",
                    "hammers and so the betrayal happened.",
                ).also {
                    stage++
                }

            16 -> npc(FaceAnim.SAD, "Please don't make me talk about it.").also { stage++ }
            17 -> player("Okay, we don't have to talk about it.").also { stage = END_DIALOGUE }
            18 -> {
                if (!anyInInventory(
                        player,
                        Items.SMALL_POUCH_5509,
                        Items.MEDIUM_POUCH_5510,
                        Items.MEDIUM_POUCH_5511,
                        Items.LARGE_POUCH_5512,
                        Items.LARGE_POUCH_5513,
                        Items.GIANT_POUCH_5514,
                        Items.GIANT_POUCH_5515,
                    )
                ) {
                    npc(
                        "You don't have any pouches that need repair...I",
                        "could sell you a pouch, but only if you don't tell!",
                    ).also {
                        stage = 31
                    }
                } else {
                    npc(
                        "I can restore them for a price - for a",
                        "price, indeed. Muahahahahaahaha!",
                    ).also { stage++ }
                }
            }

            19 -> player("Uh, what kind of a price?").also { stage++ }
            20 ->
                npc(
                    "Whatever the voices tell me to ask for.",
                    "Currently, they require 9.000 gp for a large pouch",
                    "and 12,000 gp for a giant pouch.",
                ).also {
                    stage++
                }

            21 ->
                npc(
                    "Shhhh, don't tell any one else: I have a connection",
                    "on the inside and I can sell you pouches too.",
                    "For a mere 25,000 gp, you can have a large pouch.",
                ).also {
                    stage++
                }

            22 -> npc("A reasonable 50,000 gp will get you a giant pouch.").also { stage++ }
            23 ->
                options(
                    "I'd like to have a pouch repaired.",
                    "I'd like to buy a pouch.",
                    "Never mind.",
                ).also { stage++ }

            24 ->
                when (buttonId) {
                    1 -> player("I'd like to have a pouch repaired.").also { stage++ }
                    2 -> player("I'd like to buy a pouch.").also { stage = 30 }
                    3 -> player("Never mind.").also { stage = END_DIALOGUE }
                }

            25 -> npc("Very well. Let's have a look at it.").also { stage++ }
            26 -> {
                setTitle(player, 3)
                sendDialogueOptions(
                    player,
                    "Which pouch would you like to repair?",
                    "Repair large pouch for 9,000 gp.",
                    "Repair giant pouch for 12,000 gp.",
                    "Never mind.",
                )
                stage++
            }

            27 ->
                when (buttonId) {
                    1 -> player("Repair large pouch for 9,000 gp.").also { stage++ }
                    2 -> player("Repair giant pouch for 12,000 gp.").also { stage = 29 }
                    3 -> player("Never mind.").also { stage = END_DIALOGUE }
                }

            28 -> {
                if (amountInInventory(player, Items.COINS_995) < 9000) {
                    player("Oi, I'm broke!")
                    stage = 36
                    return true
                }
                if (!inInventory(player, Items.LARGE_POUCH_5513)) {
                    npc("The voices are angry at you! You have nothing to", "repair. Leave us be.")
                    stage = END_DIALOGUE
                    return true
                }
                end()
                repair()
                removeItem(player, Item(Items.COINS_995, 9000))
                npc(
                    "Magic makes me happy, magic makes me glad, magic",
                    "makes the voices quiet, and nothing rhymes with",
                    "purple.",
                )
            }

            29 -> {
                if (amountInInventory(player, Items.COINS_995) < 12000) {
                    player("Oi, I'm broke!")
                    stage = 36
                    return true
                }
                if (!inInventory(player, Items.GIANT_POUCH_5515)) {
                    npc("The voices are angry at you! You have nothing to", "repair. Leave us be.")
                    stage = END_DIALOGUE
                    return true
                }
                end()
                repair()
                removeItem(player, Item(Items.COINS_995, 12000))
                npc(
                    "Ahhh, the simple act of a transformation spell.",
                    "So, soothing. It makes the voices quiet.",
                    "Your pouch is repaired.",
                )
            }

            30 -> npc("Ah, coins to fund my rock collection.").also { stage++ }
            31 -> {
                setTitle(player, 3)
                sendDialogueOptions(
                    player,
                    "Which pouch would you like to buy?",
                    "Buy a large pouch for 25,000 gp.",
                    "Buy a giant pouch for 50,000 gp.",
                    "Never mind.",
                )
                stage++
            }

            32 ->
                when (buttonId) {
                    1 -> player("Buy a large pouch for 25,000 gp.").also { stage++ }
                    2 -> player("Buy a giant pouch for 50,000 gp.").also { stage = 34 }
                    3 -> player("Never mind.").also { stage = END_DIALOGUE }
                }

            33 -> {
                /*
                 * Handles buy the Large pouch.
                 */
                if (!removeItem(player, Item(Items.COINS_995, 25000))) {
                    player("Oi, I'm broke!")
                    stage = 36
                    return true
                } else {
                    end()
                    addItemOrDrop(player, Items.LARGE_POUCH_5512)
                }
            }

            34 -> {
                /*
                 * Handles buy the Giant pouch.
                 */
                if (!removeItem(player, Item(Items.COINS_995, 50000))) {
                    player("Oi, I'm broke!")
                    stage = 36
                } else {
                    end()
                    addItemOrDrop(player, Items.GIANT_POUCH_5514)
                }
            }

            35 -> {
                end()
                npc(
                    "Lost it, did you? Or did they take it back?",
                    "CLAIM IT BACK! Quick, better have another before",
                    "the spoons come.",
                )
                addItemOrDrop(player, Items.MEDIUM_POUCH_5511, 1)
            }

            36 -> {
                end()
                npc("So?")
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WIZARD_KORVAK_8029)
    }

    /**
     * Repairs the player pouches.
     * @return Boolean indicating the success of the repair.
     */
    fun repair(): Boolean {
        player.pouchManager.pouches.forEach { (id: Int, pouch: PouchManager.Pouches) ->
            pouch.currentCap = pouch.capacity
            val newCharges: Int
            newCharges =
                when (id) {
                    Items.MEDIUM_POUCH_5510 -> 264
                    Items.LARGE_POUCH_5512 -> 186
                    Items.GIANT_POUCH_5514 -> 140
                    else -> 3
                }
            pouch.charges = newCharges
            var essence: Item? = null
            if (!pouch.container.isEmpty) {
                val ess = pouch.container[0].id
                val amount = pouch.container.getAmount(ess)
                essence = Item(ess, amount)
            }
            pouch.remakeContainer()
            if (essence != null) {
                pouch.container.add(essence)
            }
            if (id != 5509) {
                if (player.inventory.contains(id + 1, 1)) {
                    player.inventory.remove(Item(id + 1, 1))
                    player.inventory.add(Item(id, 1))
                }
                if (player.bank.contains(id + 1, 1)) {
                    player.bank.remove(Item(id + 1, 1))
                    player.bank.add(Item(id, 1))
                }
            }
        }
        return true
    }
}
