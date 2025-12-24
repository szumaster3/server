import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import shared.consts.NPCs

/**
 * Handles the Ali Leaflet Dropper NPC.
 */
class AliLeafletDropperNPC : NPCBehavior(NPCs.ALI_THE_LEAFLET_DROPPER_3680) {

    private var tickDelay = RandomFunction.random(5)
    private var nextChatTick = RandomFunction.random(20, 40)

    private val forceChat = listOf(
        "Keep west as you travel south...", "to avoid the killer scorpions!",
        "Visit Ranael's Super Skirt Store...", "for the most stylish protection money can buy!",
        "The world's finest market can be found to the south...", "in Al Kharid!",
        "Visit Louie's Armoured Legs Bazaar...", "Number one for clanky trousers!",
        "Dommik's crafting store...", "The place for all your crafting needs.",
        "Run your enemies through in style...", "with a Scimitar from Zeke's Superior Scimitars!",
        "Ellis' Tannery...", "The prices are better than the smell!",
        "Ali's Discount Wares...", "The finest store in the world!!"
    )

    override fun tick(self: NPC): Boolean {

        tickDelay++

        if (tickDelay >= nextChatTick) {
            tickDelay = 0
            nextChatTick = RandomFunction.random(40, 80)

            if (RandomFunction.random(2) == 1) {
                val index = RandomFunction.random(0, forceChat.size / 2 - 1) * 2
                val firstLine = forceChat[index]
                val secondLine = forceChat[index + 1]

                sendChat(self, firstLine)
                sendChat(self, secondLine, 3)
            }
        }

        return true
    }
}