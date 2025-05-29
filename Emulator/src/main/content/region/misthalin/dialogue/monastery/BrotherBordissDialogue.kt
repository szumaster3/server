package content.region.misthalin.dialogue.monastery

import core.api.getItemName
import core.api.inInventory
import core.api.sendItemDialogue
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

@Initializable
class BrotherBordissDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getSigil(player) != null && inInventory(player, Items.BLESSED_SPIRIT_SHIELD_13736, 1)) {
            npc(FaceAnim.OLD_NORMAL, "Hello again!")
            return true
        }
        player(FaceAnim.OLD_NORMAL, "Hello?")
        stage = 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_HAPPY, "Say, what are you carrying there?").also { stage++ }
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
                    val productID = getItemName(getShield(sigil)!!.id)
                    if (player.inventory.remove(sigil, Item(Items.COINS_995, 1500000), Item(Items.BLESSED_SPIRIT_SHIELD_13736))) {
                        sendItemDialogue(player, "Bordis skillfully attaches the $itemName to the blessed", "spirit shield and creates a $productID.")
                        player.inventory.add(getShield(sigil))
                        return true
                    }
                }
                2 -> player("Oh no, that is way too expensive.").also { stage = 14 }
            }
            14 -> npcl(FaceAnim.OLD_NORMAL, "That's a shame, then.").also { stage = END_DIALOGUE }
            15 -> npcl(FaceAnim.OLD_DEFAULT, "No, I suppose I never did get a chance to explain it. It's something I learned from the druids of Guthix. It's about the balance of the atmosphere.").also { stage++ }
            16 -> npcl(FaceAnim.OLD_DEFAULT, "Sunlight causes the world to heat up but, normally, excess heat radiates upwards into the sky and away from RuneScape. So, the land remains at a constant temperature.").also { stage++ }
            17 -> npcl(FaceAnim.OLD_DEFAULT, "Different gases trap heat more or less well. The smoke from the dragon power station trapped heat very well, more so than normal air.").also { stage++ }
            18 -> npcl(FaceAnim.OLD_DEFAULT, "You've had a taste of what would happen if the gases increased. Plants would die - except it wouldn't just be Monastery roses, it would be crops people needed to eat.").also { stage++ }
            19 -> npcl(FaceAnim.OLD_DEFAULT, "Creatures would lose their habitat and go extinct - not just vulnerable icefiends, but all sorts of creatures.").also { stage++ }
            20 -> npcl(FaceAnim.OLD_DEFAULT, "Heat would make water expand and ice melt, so sea levels would rise, and that would mean coastal cities flooding, as well as drowning underground cities like Keldagrim.").also { stage++ }
            21 -> npcl(FaceAnim.OLD_DEFAULT, "If it were to go unchecked, it would mean the end of dwarven civilisation. Human civilisation too. Thank Saradomin you stopped it in time.").also { stage = END_DIALOGUE }
            22 -> npcl(FaceAnim.OLD_DEFAULT, "Quite well, actually. It looks like my estimates were all correct and the wind here is quite enough to power Nurmof's machine.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BROTHER_BORDISS_7724)

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
