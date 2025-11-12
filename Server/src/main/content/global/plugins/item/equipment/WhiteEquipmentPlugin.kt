package content.global.plugins.item.equipment

import core.api.getAttribute
import core.api.hasRequirement
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.shops.Shops
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

enum class WhiteKnightsRanking(val requiredKillCount: Int) {
    UNRANKED(0),
    NOVICE(  100),
    PEON(    300),
    PAGE(    500),
    NOBLE(   800),
    ADEPT(   1300),
    MASTER(  1500)
}

@Initializable
class BlackKnightNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = BlackKnightNPC(id, location)

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            val killsToAdd = if (id > 8000) 12 else 1
            WhiteKnightRankManager.addToKillCount(killer, killsToAdd)
        }
    }

    override fun getIds(): IntArray = ID

    companion object {
        private val ID = intArrayOf(
            NPCs.BLACK_KNIGHT_178,
            NPCs.BLACK_KNIGHT_179,
            NPCs.BLACK_KNIGHT_2698,
            NPCs.BLACK_KNIGHT_2777,
            NPCs.BLACK_KNIGHT_6189,
            NPCs.ELITE_BLACK_KNIGHT_8324,
            NPCs.ELITE_BLACK_KNIGHT_8325,
            NPCs.ELITE_BLACK_KNIGHT_8326,
            NPCs.ELITE_BLACK_KNIGHT_8327,
            NPCs.ELITE_BLACK_KNIGHT_8330,
        )
    }
}

/**
 * Represents the Sir Vyvin dialogue.
 */
@Initializable
class SirVyvinDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hello.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val rank = WhiteKnightRankManager.getRank(player)

        when (stage) {
            0 -> npc("Greetings traveller.").also { stage++ }

            1 -> options(
                "Do you have anything to trade?",
                "Why are there so many knights in this city?",
                "Can I just distract you for a minute?"
            ).also { stage++ }

            2 -> when (buttonId) {
                1 -> playerl(FaceAnim.HALF_ASKING, "Do you have anything to trade?").also { stage = 10 }
                2 -> playerl(FaceAnim.HALF_ASKING, "Why are there so many knights in this city?").also { stage = 30 }
                3 -> playerl(FaceAnim.FRIENDLY, "Can I just talk to you very slowly for a few minutes,").also { stage = 40 }
            }

            // Trade.
            10 -> {
                when {
                    !hasRequirement(player, Quests.WANTED) -> npc("No, I'm sorry.").also { stage = END_DIALOGUE }
                    rank == WhiteKnightsRanking.UNRANKED -> {
                        npcl(FaceAnim.NEUTRAL,"Well, maybe I do, but you need to prove yourself worthy as a White Knight before I will sell you anything.")
                        stage++
                    }
                    else -> {
                        npc(FaceAnim.HAPPY,"Well, of course!", "I AM the White Knights armourer after all!")
                        stage = 15
                    }
                }
            }
            11 -> playerl(FaceAnim.ASKING, "And how can I do that?").also { stage++ }
            12 -> npcl(FaceAnim.HALF_GUILTY,"Kill Black Knights and make sure you don't kill any White Knights!").also { stage++ }
            13 -> playerl(FaceAnim.HALF_GUILTY,"Well, okay then.").also { stage = END_DIALOGUE }

            15 -> {
                npcl(FaceAnim.HAPPY, getRankDialogue(rank))
                stage++
            }
            16 -> {
                end()
                Shops.openId(player, getShopForRank(rank))
            }

            // Why so many knights?
            30 -> npcl(FaceAnim.FRIENDLY, "We are the White Knights of Falador. We are the most powerful order of knights in the land.").also { stage++ }
            31 -> npcl(FaceAnim.FRIENDLY,"We are helping King Vallance rule the kingdom as he is getting old and tired.")

            // Distraction.
            40 -> playerl(FaceAnim.HALF_ASKING, "while I distract you, so that my friend over there can do something while you're busy being distracted by me?").also { stage++ }
            41 -> npcl(FaceAnim.ASKING,"... ...what?").also { stage++ }
            42 -> npcl(FaceAnim.HALF_THINKING,"I'm... not sure what you're asking me... you want to join the White Knights?").also { stage++ }
            43 -> playerl(FaceAnim.FRIENDLY, "Nope. I'm just trying to distract you.").also { stage++ }
            44 -> npc(FaceAnim.THINKING, "... ...you are very odd.").also { stage++ }
            45 -> player(FaceAnim.HALF_ASKING,"So can I distract you some more?").also { stage++ }
            46 -> npcl(FaceAnim.NEUTRAL,"... ...I don't think I want to talk to you anymore.").also { stage++ }
            47 -> player(FaceAnim.HALF_GUILTY, "Ok. My work here is done. 'Bye!").also { stage = END_DIALOGUE }
        }
        return true
    }

    /**
     * Returns player rank.
     */
    private fun getRankDialogue(rank: WhiteKnightsRanking): String = when (rank) {
        WhiteKnightsRanking.NOVICE -> "You are currently at Novice level within the White Knights, so your access to equipment is limited."
        WhiteKnightsRanking.PEON -> "You are at Peon level within the White Knights, so not much to choose from I'm afraid."
        WhiteKnightsRanking.PAGE -> "You've reached Page level, that shows a bit of dedication you know! Your available equipment will reflect that!"
        WhiteKnightsRanking.NOBLE -> "And may I say what a pleasure it is to serve any Knight who has achieved Noble level!"
        WhiteKnightsRanking.ADEPT -> "And it is so very rare that I see any White Knight who has reached Adept level!"
        WhiteKnightsRanking.MASTER -> "And for a dedicated White Knight to have reached Master level like yourself, you get only the very best of all equipment available!"
        else -> "Hmm, I'm not sure what rank you are. Strange!"
    }

    /**
     * Returns the shop id for rank.
     */
    private fun getShopForRank(rank: WhiteKnightsRanking): Int = when (rank) {
        WhiteKnightsRanking.NOVICE -> 269
        WhiteKnightsRanking.PEON -> 270
        WhiteKnightsRanking.PAGE -> 271
        WhiteKnightsRanking.NOBLE -> 272
        WhiteKnightsRanking.ADEPT -> 273
        WhiteKnightsRanking.MASTER -> 274
        else -> 269
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_VYVIN_605)
}

object WhiteKnightRankManager {

    private const val KILL_COUNT = "/save:white_knight_kills"

    private fun getKillCount(player: Player): Int {
        return getAttribute(player, KILL_COUNT, 0)
    }

    private fun setKillCount(player: Player, kills: Int) {
        setAttribute(player, KILL_COUNT, kills)
    }

    fun addToKillCount(player: Player, kills: Int) {
        val totalKills = getKillCount(player) + kills
        setKillCount(player, totalKills)
    }

    fun addKill(player: Player) {
        val kills = getKillCount(player) + 1
        setKillCount(player, kills)
    }

    fun getRank(player: Player): WhiteKnightsRanking {
        val kills = getKillCount(player)
        return WhiteKnightsRanking.values().reversed().find { kills >= it.requiredKillCount } ?: WhiteKnightsRanking.UNRANKED
    }

}