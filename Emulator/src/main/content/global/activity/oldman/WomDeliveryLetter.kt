package content.global.activity.oldman

import content.global.handlers.iface.ScrollInterface
import content.global.handlers.iface.ScrollLine
import core.api.setAttribute
import core.game.node.entity.player.Player
import org.rs.consts.Components
import org.rs.consts.NPCs

object WomDeliveryLetter {
    val THE_ORACLE_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To the Oracle of Ice Mountain, greetings:", 2),
            ScrollLine("My sources tell me that many adventurers are becoming", 4),
            ScrollLine("confused about one of your riddles.", 5),
            ScrollLine("When asked to find a BOWL that has not seen heat, a large", 7),
            ScrollLine("number of adventurers assume that a POT will suffice. Of", 8),
            ScrollLine("course, this has no effect, so they send complaints about you to", 9),
            ScrollLine("the Council.", 10),
            ScrollLine("Strength through Wisdom!", 12),
            ScrollLine("*D", 14),
        )

    private val RELDO_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To Reldo, librarian to the King of Varrock, greetings:", 2),
            ScrollLine("I have spent many hours pondering the question you asked me", 4),
            ScrollLine("in your recent letter. From the results seen so far, this new", 5),
            ScrollLine("method of refining metals from their ores possesses the", 6),
            ScrollLine("potential to revolutionise smithing, although I fear that the vast", 7),
            ScrollLine("majority of smiths will never fully adapt to the new technique.", 8),
            ScrollLine("Please keep an eye out for any documents that might be of", 10),
            ScrollLine("interest to me, as always.", 11),
            ScrollLine("*D", 13),
        )

    private val ABBOT_LANGLEY_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To Langley, Abbot of the Monastery of Saradomin, greetings:", 2),
            ScrollLine("Long has it been since out last meeting, my friend, too long.", 4),
            ScrollLine("Truly we are living in tumultuous times, and the foul works of", 5),
            ScrollLine("Zamorak can be seen across the lands. Indeed, I hear a whisper", 6),
            ScrollLine("from the south that the power of the Terrible One has been", 7),
            ScrollLine("rediscovered! But be of good cheer, my friend, for we are all in", 8),
            ScrollLine("the hands of the Lord Saradomin.", 9),
            ScrollLine("Until our next meeting, then,", 11),
            ScrollLine("*D", 13),
        )

    private val THURGO_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To Thurgo, master blacksmith, greetings:", 2),
            ScrollLine("Following your request, I have spend some time re-reading the", 4),
            ScrollLine("relevant scrolls in the Library of Varrock. It appears that when", 5),
            ScrollLine("your forefathers encountered that adventurer, he was on", 6),
            ScrollLine("a quest to find a mysterious shield.", 7),
            ScrollLine("Many thanks for the recipe you sent me; I shall certainly try this", 9),
            ScrollLine("'redberry pie' of which you speak so highly.", 10),
            ScrollLine("Regards,", 12),
            ScrollLine("*D", 14),
        )

    private val HIGH_PRIEST_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To the High Priest of Entrana: greetings.", 2),
            ScrollLine("To respond to your recent questions about the effects of", 4),
            ScrollLine("summoning the power of Saradomin, I have spent some time", 5),
            ScrollLine("searching through the scrolls of Kolodion the battle mage. He", 6),
            ScrollLine("records that a bolt of lightning falls from above, accompanied", 7),
            ScrollLine("by a resounding crash, and the victim loses up to 20 life", 8),
            ScrollLine("points; however, he believed that this could be increased by", 9),
            ScrollLine("50% should one be wearing the Cape of Saradomin and be", 10),
            ScrollLine("charged when casting the spell.", 11),
            ScrollLine("Fare thee well, my young friend,", 13),
            ScrollLine("*D", 15),
        )

    private val FR_LAWRENCE_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To Lawrence, resident priest of Varrock, greetings:", 2),
            ScrollLine("Despite our recent conversation on this matter, I hear that you", 4),
            ScrollLine("are still often found in a less than sober condition. I am forced", 5),
            ScrollLine("to repeat the warning I gave you at the time: if you continue to", 6),
            ScrollLine("indulge yourself in this manner, the Council will have no choice", 7),
            ScrollLine("but to transfer you to Entrana where you can be supervised", 8),
            ScrollLine("more carefully.", 9),
            ScrollLine("I trust you will heed this message.", 11),
            ScrollLine("*D", 13),
        )

    private val FR_AERECK_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To Aereck, resident priest of Lumbridge, greetings:", 2),
            ScrollLine("I am pleased to inform you that, following a careful search, the", 4),
            ScrollLine("staff of the seminary have located your pyjamas.", 5),
            ScrollLine("Before returning them to you, however, they would be very", 7),
            ScrollLine("interested to know exactly how these garments came to be in", 8),
            ScrollLine("the graveyard.", 9),
            ScrollLine("Please pass on my regards to Urhney.", 11),
            ScrollLine("*D", 13),
        )

    val LETTER_DELIVERY_ATTRIBUTE = "/save:${WomTask.LETTER_DELIVERY}"

    fun getLetterContent(
        player: Player,
        npc: Int,
    ) {
        val test =
            intArrayOf(
                NPCs.ORACLE_746,
                NPCs.RELDO_2660,
                NPCs.ABBOT_LANGLEY_801,
                NPCs.THURGO_604,
                NPCs.HIGH_PRIEST_216,
                NPCs.FATHER_LAWRENCE_640,
                NPCs.FATHER_AERECK_456,
            )
        when (test.random()) {
            NPCs.ORACLE_746 ->
                ScrollInterface.scrollSetup(
                    player,
                    Components.MESSAGESCROLL_220,
                    THE_ORACLE_LETTER_CONTENT,
                )
            NPCs.RELDO_2660 -> ScrollInterface.scrollSetup(player, Components.MESSAGESCROLL_220, RELDO_LETTER_CONTENT)
            NPCs.ABBOT_LANGLEY_801 ->
                ScrollInterface.scrollSetup(
                    player,
                    Components.MESSAGESCROLL_220,
                    ABBOT_LANGLEY_LETTER_CONTENT,
                )

            NPCs.THURGO_604 -> ScrollInterface.scrollSetup(player, Components.MESSAGESCROLL_220, THURGO_LETTER_CONTENT)
            NPCs.HIGH_PRIEST_216 ->
                ScrollInterface.scrollSetup(
                    player,
                    Components.MESSAGESCROLL_220,
                    HIGH_PRIEST_LETTER_CONTENT,
                )

            NPCs.FATHER_LAWRENCE_640 ->
                ScrollInterface.scrollSetup(
                    player,
                    Components.MESSAGESCROLL_220,
                    FR_LAWRENCE_LETTER_CONTENT,
                )

            NPCs.FATHER_AERECK_456 ->
                ScrollInterface.scrollSetup(
                    player,
                    Components.MESSAGESCROLL_220,
                    FR_AERECK_LETTER_CONTENT,
                )
        }
        setAttribute(player, LETTER_DELIVERY_ATTRIBUTE, npc)
    }
}
