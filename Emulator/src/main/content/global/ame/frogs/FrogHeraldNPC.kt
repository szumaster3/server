package content.global.ame.frogs

import content.global.ame.RandomEventNPC
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.tools.RandomFunction
import org.rs.consts.NPCs

class FrogHeraldNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.FROG_2471) {
    val phrases =
        arrayOf(
            "@name, the Frog @gender needs your help.",
            "Greetings from the Frog @gender, @name!",
            "Talk to the Frog @gender, @name!",
            "The Frog @gender needs your help, @name!",
            "Please respond to the Frog @gender, @name!",
        )

    override fun init() {
        super.init()
        sendChat(
            phrases
                .random()
                .replace(
                    "@name",
                    player.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                ).replace("@gender", if (player.isMale) "Princess" else "Prince"),
        )
    }

    override fun tick() {
        if (RandomFunction.random(1, 15) == 5) {
            sendChat(
                phrases
                    .random()
                    .replace(
                        "@name",
                        player.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                    ).replace("@gender", if (player.isMale) "Princess" else "Prince"),
            )
        }
        super.tick()
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(FrogHeraldDialogue(false), this.asNpc())
    }
}
