package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class ThePiracyBook : InteractionListener {
    /*
     * Obtainable during the Cabin fever quest.
     */

    companion object {
        private const val TITLE = "The Little Book o' Piracy"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("By Cap'n Hook-Hand", 55),
                        BookLine("Morrisane (an AMCE product)", 56),
                        BookLine("<col=8A0808>Chapter 1;</col> So you've decided", 59),
                        BookLine("to become a pirate.", 60),
                        BookLine("Piracy; a life of adventure", 62),
                        BookLine("and romance. The stuff of", 63),
                        BookLine("legends and tales told all", 64),
                        BookLine("over the world. And you,", 65),
                    ),
                    Page(
                        BookLine("humble reader, have decided", 66),
                        BookLine("to join these brave men and", 67),
                        BookLine("women in the exciting field", 68),
                        BookLine("of unlicensed privateering", 69),
                        BookLine("and wholesale plunder", 70),
                        BookLine("redistribution.", 71),
                        BookLine("I congratulate you on making", 72),
                        BookLine("the correct lifestyle choice.", 73),
                        BookLine("While many consider pirates", 75),
                        BookLine("to be unwashed, ignorant,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("thieving brigands, the truth", 55),
                        BookLine("is that they are a valued and", 56),
                        BookLine("important part of the economy.", 57),
                        BookLine("Without them, the architects", 58),
                        BookLine("that design sea-defences", 59),
                        BookLine("against their attacks would be", 60),
                        BookLine("unemployed. The guards in sea", 61),
                        BookLine("ports would be laid off, and", 62),
                        BookLine("the honest merchants that buy", 63),
                        BookLine("their second-hand goods would", 64),
                        BookLine("lose a small portion of their", 65),
                    ),
                    Page(
                        BookLine("income. So you see, by living", 66),
                        BookLine("a life filled with adventure,", 67),
                        BookLine("thievery, romance, plundering,", 68),
                        BookLine("danger and stealing, they are", 69),
                        BookLine("adding to the happiness of the", 70),
                        BookLine("community as a whole. Surely", 71),
                        BookLine("these brave individuals have", 72),
                        BookLine("nothing to be ashamed of for", 73),
                        BookLine("selling their hard-earned", 74),
                        BookLine("loot to respectable merchants", 75),
                        BookLine("in exchange for a reasonable", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("sum of gold. I would go as", 55),
                        BookLine("far as to say it is their", 56),
                        BookLine("duty to redistribute such", 57),
                        BookLine("wealth that they come across", 58),
                        BookLine("in this way, so as to uphold", 59),
                        BookLine("the noble, ancient and", 60),
                        BookLine("profitable honour of the", 61),
                        BookLine("pirates.", 62),
                        BookLine("<col=8A0808>Chapter 2;</col> Looking like a pirate", 64),
                        BookLine("First, to be recognised", 65),
                    ),
                    Page(
                        BookLine("as a pirate, you have", 66),
                        BookLine("to look like a pirate.", 68),
                        BookLine("If you look like an accountant,", 69),
                        BookLine("then people will be confused", 70),
                        BookLine("when you begin talking about", 71),
                        BookLine("'briny deep' this and", 72),
                        BookLine("'plundering' that. It also saves", 73),
                        BookLine("time explaining at parties and", 74),
                        BookLine("other social gatherings exactly", 75),
                        BookLine("what you do for a living,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("allowing precious extra hours", 55),
                        BookLine("of plundering and looting.", 56),
                        BookLine("Pirates must look nautical.", 58),
                        BookLine("This look can be achieved", 59),
                        BookLine("by wearing trousers made", 60),
                        BookLine("from old sails, usually", 61),
                        BookLine("striped. You can also wear", 62),
                        BookLine("an old, tattered naval", 63),
                        BookLine("uniform, as this makes you", 64),
                        BookLine("look like you have been", 65),
                    ),
                    Page(
                        BookLine("trained by the navy before", 66),
                        BookLine("you left for your own,", 67),
                        BookLine("dark and grim reasons.", 68),
                        BookLine("I suggest you come up with", 70),
                        BookLine("at least two good reasons", 71),
                        BookLine("why you left, as customs", 72),
                        BookLine("and excise tend to alert", 73),
                        BookLine("naval officials to 'deserters'.", 74),
                        BookLine("Piracy is a dangerous", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("profession,", 55),
                        BookLine("and can be hazardous to the", 57),
                        BookLine("pirates' extremities.", 58),
                        BookLine("Preventing such injuries", 59),
                        BookLine("with either a hand- covering", 60),
                        BookLine("hook or a patch to protect", 61),
                        BookLine("your eye is always a", 62),
                        BookLine("good plan. This also leaves", 63),
                        BookLine("you prepared for the eventual", 64),
                        BookLine("loss of the extremity,", 65),
                    ),
                    Page(
                        BookLine("as you are already carrying", 66),
                        BookLine("a replacement around with", 67),
                        BookLine("you. Piratical accessories", 68),
                        BookLine("can also include (but are not", 69),
                        BookLine("limited to) cutlasses", 70),
                        BookLine("and scimitars, pirate hats,", 71),
                        BookLine("face masks, bandanas and", 72),
                        BookLine("flashy jewellery.", 73),
                        BookLine("", 74),
                        BookLine("<col=8A0808>Chapter 3;</col> <col=08088A>Pirate</col> ->", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Gielinorian Phrasebook.", 55),
                        BookLine("<col=08088A>Arr -></col> Good Morning/Hello/", 56),
                        BookLine("I agree.", 57),
                        BookLine("<col=08088A>Scurvy -></col>", 58),
                        BookLine("1. Disease contracted", 59),
                        BookLine("when sailing, caused by lack", 60),
                        BookLine("of fresh fruit and vegetables.", 61),
                        BookLine("Eg, 'Cap'n, the gunners be a", 62),
                        BookLine("bunch of scurvy dogs.'", 63),
                        BookLine("2. Inferior/untrustworthy.", 65),
                    ),
                    Page(
                        BookLine("Eg, 'Cap'n, the gunners", 67),
                        BookLine("be a bunch of scurvy dogs.'", 68),
                        BookLine("<col=08088A>Cap'n -></col> Captain.", 70),
                        BookLine("<col=08088A>Plunder -></col> Anything of value", 72),
                        BookLine("that can be traded with any", 73),
                        BookLine("honest Al Kharidian merchants", 74),
                        BookLine("for a reasonable sum of", 75),
                        BookLine("money.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=08088A>Landlubber -></col> Non-Pirate.", 55),
                        BookLine("<col=08088A>Garr! -></col> I am going to attack", 57),
                        BookLine("you/Take this!/It is time for", 58),
                        BookLine("fisticuffs and no mistake.", 59),
                        BookLine("<col=08088A>I say we keelhaul 'em! ->", 61),
                        BookLine("I propose we do unpleasant", 62),
                        BookLine("things to them until they", 63),
                        BookLine("are dead, very sorry or both.", 64),
                    ),
                    Page(
                        BookLine("<col=08088A>Aye aye! -></col> I am in emphatic", 66),
                        BookLine("or otherwise exuberant", 67),
                        BookLine("agreement with your", 68),
                        BookLine("statement.", 69),
                        BookLine("<col=08088A>Say that again and I'll kill ye!", 70),
                        BookLine("<col=08088A>-></col> I deny the truth of", 71),
                        BookLine("your statement.", 73),
                        BookLine("<col=08088A>Ye/Yer/Ye'll/Ye've -></col>>", 74),
                        BookLine("<col=08088A>Want a sip of 'rum'? ->", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("I would like to poison you.", 55),
                        BookLine("<col=08088A>Grog -></col> Alcohol, with the", 57),
                        BookLine("exception of 'rum'.", 58),
                        BookLine("<col=08088A>'Rum' -></col> 1. Anything poisonous", 60),
                        BookLine("/Acidic or caustic substance.", 61),
                        BookLine("2. The concentrated, liquid", 63),
                        BookLine("form of all that is foul.", 64),
                    ),
                    Page(
                        BookLine("<col=08088A>Go see Ali -></col> Travel to", 66),
                        BookLine("Al Kharid to sell loot and", 67),
                        BookLine("purchase top-quality products.", 68),
                        BookLine("<col=08088A>Broadside -></col> 1. To fire all", 70),
                        BookLine("cannons on one side of the", 71),
                        BookLine("ship at the same time.", 72),
                        BookLine("2. A pirate of great size.", 73),
                        BookLine("I'm not a pirate,", 74),
                        BookLine("<col=08088A>I'm a privateer</col> ->", 75),
                        BookLine("I am a pirate,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("and I'm getting paid for it.", 55),
                        BookLine("<col=08088A>Parle -></col> I have become tired", 58),
                        BookLine("of life, kill me at your leisure.", 59),
                        BookLine("<col=08088A>Widdershins -></col> A debilitating", 61),
                        BookLine("disease caused by eating mouldy", 62),
                        BookLine("biscuits for a month. Symptoms", 63),
                        BookLine("include weakness in the limbs,", 64),
                        BookLine("spinning vision, spontaneous", 65),
                    ),
                    Page(
                        BookLine("combustion and implosion of", 66),
                        BookLine("the earlobes.", 67),
                        BookLine("<col=08088A>Forbye -></col> A common greeting", 69),
                        BookLine("said (be)for(e) (good)bye.", 70),
                        BookLine("'Forbye, Cap'n! Lovely day!'", 71),
                        BookLine("<col=08088A>Hornswaggle -></col> To swaggle", 73),
                        BookLine("(or cover with soft cloth)", 74),
                        BookLine("the ship's horn (or compass)", 75),
                        BookLine("to keep it safe and dry", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("during storms.", 55),
                        BookLine("<col=08088A>Drivelswigger -></col> Small metal", 57),
                        BookLine("implement used for removing", 58),
                        BookLine("weevils from hard tack rations.", 59),
                        BookLine("<col=08088A>Abaft -></col> Direct polar", 61),
                        BookLine("opposite of avast.", 62),
                        BookLine("<col=08088A>Binnacled -></col> Condition relating", 64),
                        BookLine("to the consumption of excessive", 65),
                    ),
                    Page(
                        BookLine("quantities of cheap alcohol,", 66),
                        BookLine("or small quantities of 'rum'.", 67),
                        BookLine("'Sound the alarm Mr Bosun,", 68),
                        BookLine("pirates off the starboard bow!'", 69),
                        BookLine("'Not so loud Cap'n, I be feelin'", 70),
                        BookLine("a little binnacled.'", 71),
                        BookLine("<col=08088A>Duffle -></col> Somewhat like a", 73),
                        BookLine("seafaring goblin, only not.", 74),
                        BookLine("<col=08088A>Athwartships -></col> A period spent", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("apprenticed to a pirate,", 55),
                        BookLine("learning piracy, getting drunk,", 56),
                        BookLine("and culminating in the loss", 57),
                        BookLine("of a leg, eye or hand.", 58),
                        BookLine("<col=08088A>Davy's Grip -></col> Popular", 60),
                        BookLine("drinking game involving", 61),
                        BookLine("mangoes, a hammer, and", 62),
                        BookLine("all the corkscrews that", 63),
                        BookLine("players can get their", 64),
                        BookLine("hands on.", 65),
                    ),
                ),
            )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun display(
        player: Player,
        pageNum: Int,
        buttonID: Int,
    ): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.BOOK_O_PIRACY_7144, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
