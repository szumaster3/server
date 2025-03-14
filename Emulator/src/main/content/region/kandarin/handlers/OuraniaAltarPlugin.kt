package content.region.kandarin.handlers

import core.api.*
import core.api.interaction.*
import core.game.component.Component
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

class OuraniaAltarPlugin :
    InteractionListener,
    InterfaceListener {
    override fun defineListeners() {
        on(NPCs.ENIOLA_6362, IntType.NPC, "bank") { player, _ ->
            restrictForIronman(player, IronmanMode.ULTIMATE) {
                setAttribute(player, "zmi:bankaction", "open")
                openInterface(player, Components.BANK_CHARGE_ZMI_619)
            }
            return@on true
        }

        on(NPCs.ENIOLA_6362, IntType.NPC, "collect") { player, _ ->
            restrictForIronman(player, IronmanMode.ULTIMATE) {
                setAttribute(player, "zmi:bankaction", "collect")
                openInterface(player, Components.BANK_CHARGE_ZMI_619)
            }
            return@on true
        }
    }

    private fun handleButtonClick(
        player: Player,
        component: Component,
        opcode: Int,
        buttonID: Int,
        slot: Int,
        itemID: Int,
    ): Boolean {
        if (buttonID !in RUNE_BUTTONS) return true

        val runeItemId = BUTTON_TO_RUNE[buttonID]

        runeItemId?.let {
            if (amountInInventory(player, it) < 20) {
                sendNPCDialogue(
                    player,
                    NPCs.ENIOLA_6362,
                    "I'm afraid you don't have the necessary runes with you at this time, so " +
                        "I can't allow you to access your account. Please bring 20 runes of one type " +
                        "and you'll be able to open your bank.",
                    FaceAnim.NEUTRAL,
                )
                return true
            }
            if (removeItem(player, Item(it, 20))) {
                when (getAttribute(player, "zmi:bankaction", "")) {
                    "open" -> openBankAccount(player)
                    "collect" -> openGrandExchangeCollectionBox(player)
                }
            }
        }

        return true
    }

    override fun defineInterfaceListeners() {
        on(Components.BANK_CHARGE_ZMI_619, ::handleButtonClick)
    }

    companion object {
        private const val BUTTON_AIR_RUNE = 28
        private const val BUTTON_MIND_RUNE = 29
        private const val BUTTON_WATER_RUNE = 30
        private const val BUTTON_EARTH_RUNE = 31
        private const val BUTTON_FIRE_RUNE = 32
        private const val BUTTON_BODY_RUNE = 33
        private const val BUTTON_COSMIC_RUNE = 34
        private const val BUTTON_CHAOS_RUNE = 35
        private const val BUTTON_ASTRAL_RUNE = 36
        private const val BUTTON_LAW_RUNE = 37
        private const val BUTTON_DEATH_RUNE = 38
        private const val BUTTON_BLOOD_RUNE = 39
        private const val BUTTON_NATURE_RUNE = 40
        private const val BUTTON_SOUL_RUNE = 41

        private val RUNE_BUTTONS =
            intArrayOf(
                BUTTON_AIR_RUNE,
                BUTTON_MIND_RUNE,
                BUTTON_WATER_RUNE,
                BUTTON_EARTH_RUNE,
                BUTTON_FIRE_RUNE,
                BUTTON_BODY_RUNE,
                BUTTON_COSMIC_RUNE,
                BUTTON_CHAOS_RUNE,
                BUTTON_ASTRAL_RUNE,
                BUTTON_LAW_RUNE,
                BUTTON_DEATH_RUNE,
                BUTTON_BLOOD_RUNE,
                BUTTON_NATURE_RUNE,
                BUTTON_SOUL_RUNE,
            )

        private val BUTTON_TO_RUNE =
            hashMapOf(
                BUTTON_AIR_RUNE to Items.AIR_RUNE_556,
                BUTTON_MIND_RUNE to Items.MIND_RUNE_558,
                BUTTON_WATER_RUNE to Items.WATER_RUNE_555,
                BUTTON_EARTH_RUNE to Items.EARTH_RUNE_557,
                BUTTON_FIRE_RUNE to Items.FIRE_RUNE_554,
                BUTTON_BODY_RUNE to Items.BODY_RUNE_559,
                BUTTON_COSMIC_RUNE to Items.COSMIC_RUNE_564,
                BUTTON_CHAOS_RUNE to Items.CHAOS_RUNE_562,
                BUTTON_ASTRAL_RUNE to Items.ASTRAL_RUNE_9075,
                BUTTON_LAW_RUNE to Items.LAW_RUNE_563,
                BUTTON_DEATH_RUNE to Items.DEATH_RUNE_560,
                BUTTON_BLOOD_RUNE to Items.BLOOD_RUNE_565,
                BUTTON_NATURE_RUNE to Items.NATURE_RUNE_561,
                BUTTON_SOUL_RUNE to Items.SOUL_RUNE_566,
            )
    }

    @Initializable
    class EniolaDialogue(
        player: Player? = null,
    ) : Dialogue(player) {
        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            when (stage) {
                START_DIALOGUE ->
                    if (hasIronmanRestriction(player, IronmanMode.ULTIMATE)) {
                        npcl(
                            FaceAnim.NEUTRAL,
                            "My apologies, dear ${if (player.isMale) "sir" else "madam"}, " +
                                "our services are not available for Ultimate ${if (player.isMale) "Ironman" else "Ironwoman"}.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    } else {
                        npcl(FaceAnim.NEUTRAL, "Good day, how may I help you?").also {
                            if (hasAwaitingGrandExchangeCollections(player)) {
                                stage++
                            } else {
                                stage += 2
                            }
                        }
                    }

                1 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Before we go any further, I should inform you that you " +
                            "have items ready for collection from the Grand Exchange.",
                    ).also { stage++ }
                2 -> playerl(FaceAnim.ASKING, "Who are you?").also { stage++ }
                3 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "How frightfully rude of me, my dear ${if (player.isMale) "sir" else "lady"}. " +
                            "My name is Eniola and I work for that excellent enterprise, the Bank of Gielinor.",
                    ).also { stage++ }
                4 ->
                    showTopics(
                        Topic(FaceAnim.HALF_THINKING, "If you work for the bank, what are you doing here?", 10),
                        Topic(FaceAnim.NEUTRAL, "I'd like to access my bank account, please.", 30),
                        Topic(FaceAnim.NEUTRAL, "I'd like to check my PIN settings.", 31),
                        Topic(FaceAnim.NEUTRAL, "I'd like to see my collection box.", 32),
                        Topic(FaceAnim.NEUTRAL, "Never mind.", END_DIALOGUE),
                    )

                10 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "My presence here is the start of a new enterprise of travelling banks. " +
                            "I, and others like me, will provide you with the convenience of having " +
                            "bank facilities where they will be of optimum use to you.",
                    ).also { stage++ }
                11 -> playerl(FaceAnim.ASKING, "So... What are you doing here?").also { stage++ }
                12 ->
                    npcl(
                        FaceAnim.HALF_GUILTY,
                        "The Z.M.I. - that is - the Zamorakian Magical Institute, required my services " +
                            "upon discovery of this altar.",
                    ).also { stage++ }
                13 ->
                    npcl(
                        FaceAnim.HALF_GUILTY,
                        "We at the Bank of Gielinor are a neutral party and are willing to offer our " +
                            "services regardless of affiliation. So that is why I am here.",
                    ).also { stage++ }
                14 -> playerl(FaceAnim.ASKING, "Can I access my bank account by speaking to you?").also { stage++ }
                15 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Of course, dear ${if (player.isMale) "sir" else "lady"}. However, I must inform you " +
                            "that because the Z.M.I. are paying for my services, they require anyone not part of " +
                            "the Institute to pay an access fee to open their bank account.",
                    ).also { stage++ }
                16 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "But, as our goal as travelling bankers is to make our customers' lives more convenient, " +
                            "we have accomodated to your needs.",
                    ).also { stage++ }
                17 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "We know you will be busy creating runes and do not wish " + "to carry money with you.",
                    ).also { stage++ }
                18 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "The charge to open your account is the small amount of twenty of one type of rune. " +
                            "The type of rune is up to you.",
                    ).also { stage++ }
                19 ->
                    npcl(
                        FaceAnim.HALF_ASKING,
                        "Would you like to pay the price of twenty runes to open " + "your bank account?",
                    ).also { stage++ }
                20 ->
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "Yes, please.", 30),
                        Topic(FaceAnim.SUSPICIOUS, "Let me open my account and then I'll give you the runes.", 21),
                        Topic(FaceAnim.ANNOYED, "No way! I'm not paying to withdraw my own stuff.", 22),
                    )
                21 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "It's not that I don't trust you, old ${if (player.isMale) "chap" else "girl"}, " +
                            "but as the old adage goes: 'Payment comes before friends'.",
                    ).also { stage = END_DIALOGUE }
                22 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "I'm sorry to hear that, dear ${if (player.isMale) "sir" else "madam"}. ",
                    ).also { stage++ }
                23 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Should you reconsider, because I believe this service offers excellent " +
                            "value for the price, do not hesitate to contact me.",
                    ).also { stage = END_DIALOGUE }
                30 -> {
                    setAttribute(player, "zmi:bankaction", "open")
                    openInterface(player, Components.BANK_CHARGE_ZMI_619)
                    end()
                }
                31 -> {
                    openBankPinSettings(player)
                    end()
                }
                32 -> {
                    setAttribute(player, "zmi:bankaction", "collect")
                    openInterface(player, Components.BANK_CHARGE_ZMI_619)
                    end()
                }
            }

            return true
        }

        override fun getIds(): IntArray = intArrayOf(NPCs.ENIOLA_6362)
    }
}
