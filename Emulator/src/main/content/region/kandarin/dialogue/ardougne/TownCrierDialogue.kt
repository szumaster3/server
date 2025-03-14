package content.region.kandarin.dialogue.ardougne

import content.global.handlers.item.book.GeneralRuleBook.Companion.openBook
import core.api.lock
import core.api.runTask
import core.api.stopWalk
import core.api.visualize
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TownCrierDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            "Oh, hello citizen. Are you here to find out about Player",
            "Moderators? Or perhaps would you like to know about the",
            "laws of the land?",
        ).also { stage = 1 }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                options(
                    "Tell me about Player Moderators.",
                    "Tell me about the Rules of " + settings!!.name + ".",
                    "Can you give me a handy tip please?",
                    "Bye!",
                ).also { stage++ }

            2 ->
                when (buttonId) {
                    1 -> player("Tell me about Player Moderators.").also { stage = 50 }
                    2 -> player("Tell me about the Rules of " + settings!!.name + ".").also { stage = 70 }
                    3 -> player("Can you give me a handy tip please?").also { stage = 100 }
                    4 -> player("Bye!").also { stage++ }
                }

            3 -> npc("Nice meeting you.").also { stage = END_DIALOGUE }

            50 -> npc("Of course. What would you like to know?").also { stage++ }
            51 ->
                options(
                    "What is a Player Moderator?",
                    "What can Player Moderators do?",
                    "How do I become a Player Moderator?",
                    "What can Player Moderators not do?",
                ).also { stage++ }

            52 ->
                when (buttonId) {
                    1 -> player("What is a Player Moderator?").also { stage = 150 }
                    2 -> npc("What can Player Moderators do?").also { stage = 160 }
                    3 -> player("How do I become a Player Moderator?").also { stage = 170 }
                    4 -> player("What can Player Moderators not do?").also { stage = 180 }
                }

            70 -> npc("At once. Take a look at my book here.").also { stage++ }
            71 -> {
                end()
                stopWalk(npc!!)
                lock(player, 5)
                lock(npc, 5)
                visualize(npc, 6866, 1178)
                runTask(player, 3) {
                    openBook(player)
                }
            }
            100 -> {
                when ((0..4).random()) {
                    1 ->
                        npc(
                            "If the chat window is moving too quickly to report a",
                            "player accurately, run to a quiet spot and review the chat",
                            "at your leisure!",
                        ).also {
                            stage =
                                1
                        }
                    2 ->
                        npc(
                            "If you're lost and have no idea where to go, use the Home",
                            "Teleporter spell for free!",
                        ).also {
                            stage =
                                1
                        }
                    3 ->
                        npc("Make your recovery questions and answers hard to guess", "but easy to remember.").also {
                            stage =
                                1
                        }
                    4 ->
                        npc(
                            "Beware of players trying to lue you into the wilderness.",
                            "Your items cannot be returned if you lose them!",
                        ).also {
                            stage =
                                1
                        }
                    else ->
                        npc("" + settings!!.name + " will never email you asking for your log-in details.").also {
                            stage =
                                1
                        }
                }
            }
            150 ->
                npc(
                    "Player Moderators are normal players of the game, just",
                    "like you. However, since they have shown themselves to be",
                    "trustworthy and active reporters, they have been invited",
                    "by Jagex to monitor the game and take appropriate",
                ).also {
                    stage++
                }
            151 ->
                npc(
                    "reward when they see rule breaking. You can spot a Player",
                    "Moderator in game by looking at the chat screen - when a",
                    "Player Moderator speaks, a silver crown appears to the",
                    "left of their name. Remember, if there's no silver crown",
                ).also {
                    stage++
                }
            152 ->
                npc(
                    "there, they are not a Player Moderator! You can check",
                    "out the website if you'd like more information.",
                ).also {
                    stage++
                }
            153 -> player("Thanks!").also { stage++ }
            154 -> npc("Is there anything else you'd like to know?").also { stage = 51 }
            160 ->
                npc(
                    "Player Moderators, or 'P-mods', have the ability to mute",
                    "rule breakers and " + settings!!.name + " view their reports as a priority so",
                    "that reward is taken as quickly as possible. P-Mods also",
                    "have access to the Player Moderator Centre. Within the",
                ).also { stage++ }
            161 ->
                npc(
                    "Centre are tools to help them Moderate " + settings!!.name + ".",
                    "These tools include dedicated forums, the Player",
                    "Moderator Guidelines and the Player Moderator Code of",
                    "Conduct.",
                ).also {
                    stage =
                        153
                }
            170 ->
                npc(
                    settings!!.name + " picks players who spend their time and effort to",
                    "help better the " + settings!!.name + " community. To increase your",
                    "chances of becoming a Player Moderator:",
                ).also { stage++ }
            171 ->
                npc(
                    "Keep your account secure! This is very important, as a",
                    "player with poor security will never be a P-Mod. Read our",
                    "Security Tips for more information.",
                ).also {
                    stage++
                }
            173 ->
                npc(
                    "Play by the rules! The rules of " + settings!!.name + " are enforced",
                    "for a reason, to make the game a fair and enjoyable",
                    "environment for all.",
                ).also {
                    stage++
                }
            174 ->
                npc(
                    "Report accurately! When " + settings!!.name + " consider an account for",
                    "review they look for quality, not quantity. Ensure your",
                    "reports are of a high quality by following the report",
                    "guidelines.",
                ).also {
                    stage++
                }
            175 ->
                npc(
                    "Be nice to each other! Treat others as you would",
                    "want to be treated yourself. Respect your fellow player.",
                    "More information can be found on the website.",
                ).also {
                    stage =
                        153
                }
            180 ->
                npc(
                    "P-Mods cannot ban your account - they can only report",
                    "offences. " + settings!!.name + " then take reward based on the evidence",
                    "received. If you lose your password or get scammed by",
                    "another player, P_Mods cannot help you get your account",
                ).also { stage++ }
            181 ->
                npc(
                    "back. All they can do is recommend you to go to Player",
                    "Support. They cannot retrieve any items you may have",
                    "lost and they certainly do not receive any free items",
                    "from " + settings!!.name + " for moderating the game. They are players",
                ).also { stage++ }
            182 ->
                npc(
                    "who give their all to help the community, out of the",
                    "goodness of their hearts! P-mods do not work for " + settings!!.name + "",
                    "and so cannot make you a Moderator, or recommend",
                    "other accounts to become Moderators. If you wish yo",
                ).also { stage++ }
            183 -> npc("become a Moderator, feel free to ask me!").also { stage = 153 }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return TownCrierDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.TOWN_CRIER_6135,
            NPCs.TOWN_CRIER_6136,
            NPCs.TOWN_CRIER_6137,
            NPCs.TOWN_CRIER_6138,
            NPCs.TOWN_CRIER_6139,
        )
    }
}
