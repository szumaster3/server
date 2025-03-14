package content.region.kandarin.miniquest.barcrawl

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket
import core.tools.StringUtils
import org.rs.consts.Items
import org.rs.consts.NPCs

enum class BarcrawlType {
    BLUE_MOON(
        NPCs.BARTENDER_733,
        50,
        "Uncle Humphrey's Gutrot",
        arrayOf("Oh no not another of you guys. These barbarian", "barcrawls cause too much damage to my bar."),
        arrayOf("You're going to have to pay me 50 gold for the Uncle", "Humphrey's Gutrot."),
    ) {
        override fun effect(player: Player) {
            sendChat(player, "Blearrgh!")
            impact(player, 1, ImpactHandler.HitsplatType.NORMAL)
            addBonus(player, 1, Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH, Skills.SMITHING)
        }

        override fun message(
            player: Player,
            start: Boolean,
        ) {
            if (!start) {
                sendMessages(player, "Your insides feel terrible.", "The bartender signs your card.")
            } else {
                sendMessages(player, "You buy some $barName.", "You drink the $barName.")
            }
        }
    },

    BLUEBERRY_BAR(
        NPCs.BLURBERRY_848,
        10,
        "Fire Toad Blast",
        arrayOf(
            "Ah, you've come to the best stop on your list! I'll give",
            "you my famous Fire Toad last! It'll cost you 10",
            "coins.",
        ),
    ) {
        override fun effect(player: Player) {
            impact(player, 1, ImpactHandler.HitsplatType.NORMAL)
        }

        override fun message(
            player: Player,
            start: Boolean,
        ) {
            if (!start) {
                sendMessages(player, "Blueberry signs your card.")
            } else {
                super.message(player, start)
                sendMessages(player, "Your mouth and throat burns as you gulp it down.")
            }
        }
    },

    DEADMAN_CHEST(
        NPCs.BARTENDER_735,
        15,
        "Supergrog",
        arrayOf("Haha time to be breaking out the old Supergrog. That'll", "be 15 coins please."),
    ) {
        override fun effect(player: Player) {
            addBonus(player, 1, Skills.ATTACK, Skills.DEFENCE, Skills.HERBLORE, Skills.CONSTRUCTION, Skills.PRAYER)
        }

        override fun message(
            player: Player,
            start: Boolean,
        ) {
            if (!start) {
                sendMessages(
                    player,
                    "You stagger backwards.",
                    "You think you see 2 bartenders signing 2 barcrawl cards.",
                )
            } else {
                sendMessages(
                    player,
                    "The bartender serves you a glass of strange thick dark liquid.",
                    "You wince and drink it.",
                )
            }
        }
    },

    DRAGON_INN(
        NPCs.BARTENDER_739,
        12,
        "Fire Brandy",
        arrayOf("I suppose you'll be wanting some Fire Brandy. That'll", "cost you 12 coins."),
    ) {
        override fun effect(player: Player) {
            addBonus(player, 1, Skills.ATTACK, Skills.DEFENCE)
        }

        override fun message(
            player: Player,
            start: Boolean,
        ) {
            if (!start) {
                sendMessages(
                    player,
                    "Your vision blurs and you stagger slightly.",
                    "You can just about make out the bartender signing your barcrawl card.",
                )
            } else {
                sendMessages(
                    player,
                    "The bartender hands you a small glass and sets light to the contents.",
                    "You blow out the flame and drink it.",
                )
            }
        }
    },

    FLYING_HORSE_INN(
        NPCs.BARTENDER_737,
        8,
        "Heart Stopper",
        arrayOf("Fancy a bit of Heart Stopper then do you? It'll only be", "8 coins."),
    ) {
        override fun effect(player: Player) {
            impact(player, (getStatLevel(player, Skills.HITPOINTS) * 0.15).toInt(), ImpactHandler.HitsplatType.NORMAL)
        }

        override fun message(
            player: Player,
            start: Boolean,
        ) {
            if (!start) {
                sendMessages(
                    player,
                    "You clutch your chest.",
                    "Through your tears you see the bartender...",
                    "signing your barcrawl card.",
                )
            } else {
                sendMessages(player, "The bartender hands you a shot of Heart Stopper.", "You grimace and drink it.")
            }
        }
    },

    FORESTERS_ARMS(
        NPCs.BARTENDER_738,
        18,
        "Liverbane Ale",
        arrayOf(
            "Oh you're a barbarian then. Now which of these barrels",
            "contained the Liverbane Ale? That'll be 18 coins please.",
        ),
    ) {
        override fun effect(player: Player) {
            addBonus(player, Skills.ATTACK, Skills.DEFENCE, Skills.FLETCHING, Skills.FIREMAKING, Skills.WOODCUTTING)
        }

        override fun message(
            player: Player,
            start: Boolean,
        ) {
            if (!start) {
                sendMessages(
                    player,
                    "The room seems to be swaying.",
                    "The bartender scrawls his signature on your card.",
                )
            } else {
                sendMessages(player, "The bartender gives you a glass of Liverbane Ale.", "You gulp it down.")
            }
        }
    },

    JOLLY_BOAR(
        NPCs.BARTENDER_731,
        10,
        "Olde Suspiciouse",
        arrayOf(
            "Ah, there seems to be a fair few doing that one these",
            "days. My supply of Olde suspiciouse is starting to run",
            "low, it'll cost you 10 coins.",
        ),
    ) {
        override fun effect(player: Player) {
            addBonus(
                player,
                1,
                Skills.ATTACK,
                Skills.DEFENCE,
                Skills.STRENGTH,
                Skills.MINING,
                Skills.CRAFTING,
                Skills.MAGIC,
            )
            impact(player, 1, ImpactHandler.HitsplatType.NORMAL)
            sendPlayerDialogue(player, "Thanksh very mush...", FaceAnim.DRUNK)
        }

        override fun message(
            player: Player,
            start: Boolean,
        ) {
            if (!start) {
                sendMessages(player, "Your head is spinning.", "The bartender signs your card.")
            } else {
                sendMessages(player, "You buy a pint of Olde Suspiciouse.", "You gulp it down.")
            }
        }
    },

    KARAMJA_SPIRITS(
        NPCs.ZAMBO_568,
        7,
        "Ape Bite Liqueur",
        arrayOf(
            "Ah, you'll be wanting some Ape Bite Liqueur then. It's",
            "got a lovely bannana taste, and it'll only cost you 7",
            "coins.",
        ),
    ) {
        override fun effect(player: Player) {
            addBonus(player, 1, Skills.ATTACK, Skills.DEFENCE)
        }

        override fun message(
            player: Player,
            start: Boolean,
        ) {
            if (!start) {
                sendMessages(player, "Zamo signs your card.")
                sendDialogue(player, "Mmmmm, dat was luverly...")
            } else {
                sendMessages(player, "You buy some Ape Bite liqueur.", "You swirl it around and swallow it.")
            }
        }
    },

    RISING_SUNN_INN(
        intArrayOf(NPCs.KAYLEE_3217, NPCs.EMILY_736),
        70,
        "Hand of Death Cocktail",
        arrayOf("Heehee, this'll be fun!"),
        arrayOf(
            "You'll be after our Hand of Death cocktail, then. Lots",
            "of expensive parts to the cocktail, though, so it will cost",
            "you 70 coins.",
        ),
    ) {
        override fun effect(player: Player) {
            addBonus(player, 1, Skills.ATTACK, Skills.DEFENCE, Skills.RANGE, Skills.FIREMAKING)
            player.impactHandler.manualHit(player, 1, ImpactHandler.HitsplatType.NORMAL)
        }

        override fun message(
            player: Player,
            start: Boolean,
        ) {
            if (!start) {
                sendMessage(player, "The barmaid giggles. The barmaid signs your card.")
            } else {
                PacketRepository.send(
                    CameraViewPacket::class.java,
                    CameraContext(player, CameraContext.CameraType.SHAKE, 4, 4, 1, 4, 4),
                )
                Pulser.submit(
                    object : Pulse(3, player) {
                        override fun pulse(): Boolean {
                            PacketRepository.send(
                                CameraViewPacket::class.java,
                                CameraContext(player, CameraContext.CameraType.RESET, 4, 4, 1, 4, 4),
                            )
                            return true
                        }
                    },
                )
                player.packetDispatch.sendMessages(
                    "You buy a Hand of Death cocktail.",
                    "You drink the cocktail.",
                    "You stumble around the room.",
                )
            }
        }
    },

    RUSTY_ANCHOR_INN(
        NPCs.BARTENDER_734,
        8,
        "Black Skull Ale",
        arrayOf("Okay, one Black Skull Ale coming up. Eight coins, please."),
    ) {
        override fun effect(player: Player) {
            sendChat(player, "Hiccup!")
        }

        override fun message(
            player: Player,
            start: Boolean,
        ) {
            if (!start) {
                super.message(player, start)
            } else {
                sendMessages(
                    player,
                    "You buy a Black Skull Ale...",
                    "You drink your Black Skull Ale...",
                    "Your vision blurs.",
                )
            }
        }
    }, ;

    val npc: IntArray

    var barName: String? = null

    val coins: Item

    val dialogue: Array<Array<String>>

    constructor(npc: Int, coins: Item, name: String, dialogue: Array<Array<String>>) {
        this.npc = intArrayOf(npc)
        this.barName = name
        this.coins = coins
        this.dialogue = dialogue
    }

    constructor(npc: Int, coins: Int, name: String, first: Array<String>, second: Array<String>) {
        this.npc = intArrayOf(npc)
        this.barName = name
        this.coins = Item(Items.COINS_995, coins)
        this.dialogue = arrayOf(first, second)
    }

    constructor(npc: IntArray, coins: Int, name: String, first: Array<String>, second: Array<String>) {
        this.npc = npc
        this.barName = name
        this.coins = Item(Items.COINS_995, coins)
        this.dialogue = arrayOf(first, second)
    }

    constructor(npc: Int, coins: Int, name: String, first: Array<String>) {
        this.npc = intArrayOf(npc)
        this.barName = name
        this.coins = Item(Items.COINS_995, coins)
        this.dialogue = arrayOf(first)
    }

    open fun effect(player: Player) {
    }

    open fun message(
        player: Player,
        start: Boolean,
    ) {
        if (!start) {
            sendMessage(player, "The bartender signs your card.")
        } else {
            sendMessage(player, "You buy a " + (if (StringUtils.isPlusN(barName)) "an" else "a") + " " + barName + ".")
        }
    }

    fun addBonus(
        player: Player,
        amount: Int,
        vararg skills: Int,
    ) {
        for (i in skills) {
            player.getSkills().updateLevel(i, -amount, 0)
        }
    }

    companion object {
        fun forId(id: Int): BarcrawlType? {
            for (type in values()) {
                for (npc in type.npc) {
                    if (npc == id) {
                        return type
                    }
                }
            }
            return null
        }
    }
}
