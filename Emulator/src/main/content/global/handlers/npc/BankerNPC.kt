package content.global.handlers.npc

import content.global.handlers.scenery.BankBoothListener
import core.api.getScenery
import core.api.hasSealOfPassage
import core.api.interaction.openBankAccount
import core.api.interaction.openGrandExchangeCollectionBox
import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class BankerNPC :
    AbstractNPC,
    InteractionListener {
    companion object {
        private const val LUNAR_ISLE_BANK_REGION = 8253
        val NPC_IDS =
            intArrayOf(
                NPCs.SIRSAL_BANKER_4519,
                NPCs.BANK_TUTOR_4907,
                NPCs.JADE_4296,
                NPCs.OGRESS_BANKER_7049,
                NPCs.OGRESS_BANKER_7050,
                NPCs.ARNOLD_LYDSPOR_3824,
                NPCs.BANKER_6538,
                NPCs.BANKER_44,
                NPCs.BANKER_45,
                NPCs.BANKER_494,
                NPCs.BANKER_495,
                NPCs.BANKER_496,
                NPCs.BANKER_497,
                NPCs.BANKER_498,
                NPCs.BANKER_499,
                NPCs.BANKER_1036,
                NPCs.BANKER_1360,
                NPCs.BANKER_2163,
                NPCs.BANKER_2164,
                NPCs.BANKER_2354,
                NPCs.BANKER_2355,
                NPCs.BANKER_2568,
                NPCs.BANKER_2569,
                NPCs.BANKER_2570,
                NPCs.BANKER_3198,
                NPCs.BANKER_3199,
                NPCs.BANKER_5258,
                NPCs.BANKER_5259,
                NPCs.BANKER_5260,
                NPCs.BANKER_5261,
                NPCs.BANKER_5776,
                NPCs.BANKER_5777,
                NPCs.BANKER_5912,
                NPCs.BANKER_5913,
                NPCs.BANKER_6200,
                NPCs.BANKER_6532,
                NPCs.BANKER_6533,
                NPCs.BANKER_6534,
                NPCs.BANKER_6535,
                NPCs.BANKER_7445,
                NPCs.BANKER_7446,
                NPCs.BANKER_7605,
                NPCs.GUNDAI_902,
                NPCs.GHOST_BANKER_1702,
                NPCs.GNOME_BANKER_166,
                NPCs.NARDAH_BANKER_3046,
                NPCs.MAGNUS_GRAM_5488,
                NPCs.TZHAAR_KET_ZUH_2619,
                NPCs.CORNELIUS_3568,
            )

        fun checkLunarIsleRestriction(
            player: Player,
            node: Node,
        ): Boolean {
            if (node.location.regionId != LUNAR_ISLE_BANK_REGION) return false
            if (!hasSealOfPassage(player)) {
                return true
            }
            return false
        }

        fun attemptBank(
            player: Player,
            node: Node,
        ): Boolean {
            val npc = node as NPC

            if (checkLunarIsleRestriction(player, node)) {
                openDialogue(player, npc.id, npc)
                return true
            }

            npc.faceLocation(null)
            openBankAccount(player)

            return true
        }

        fun attemptCollect(
            player: Player,
            node: Node,
        ): Boolean {
            val npc = node as NPC

            if (checkLunarIsleRestriction(player, node)) {
                openDialogue(player, npc.id, npc)
                return true
            }

            npc.faceLocation(null)
            openGrandExchangeCollectionBox(player)

            return true
        }
    }

    constructor() : super(0, null)
    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any?,
    ): AbstractNPC {
        return BankerNPC(id, location)
    }

    private fun findAdjacentBankBoothLocation(): Pair<Direction, Location>? {
        for (side in arrayOf(Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST)) {
            val boothLocation = location.transform(side)
            val sceneryObject = getScenery(boothLocation)

            if (sceneryObject != null && sceneryObject.id in BankBoothListener.BANK_BOOTHS) {
                return Pair(side, boothLocation.transform(side, 1))
            }
        }

        return null
    }

    private fun provideDestinationOverride(
        entity: Entity,
        node: Node,
    ): Location {
        val npc = node as NPC

        return when (npc.id) {
            NPCs.BANKER_2354, NPCs.BANKER_2355 -> npc.location.transform(npc.direction, 2)
            NPCs.OGRESS_BANKER_7049, NPCs.OGRESS_BANKER_7050 -> npc.location.transform(3, 1, 0)
            NPCs.BANKER_6532, NPCs.BANKER_6533, NPCs.BANKER_6534, NPCs.BANKER_6535 ->
                npc.location.transform(
                    npc.direction,
                    1,
                )

            NPCs.MAGNUS_GRAM_5488 -> npc.location.transform(Direction.NORTH, 2)
            else -> {
                if (npc is BankerNPC) {
                    npc.findAdjacentBankBoothLocation()?.let {
                        return it.second
                    }
                }
                return npc.location
            }
        }
    }

    override fun defineListeners() {
        on(NPC_IDS, IntType.NPC, "bank", handler = Companion::attemptBank)
        on(NPC_IDS, IntType.NPC, "collect", handler = Companion::attemptCollect)
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, NPC_IDS, "bank", "collect", "talk-to", handler = ::provideDestinationOverride)
    }

    override fun init() {
        super.init()
        findAdjacentBankBoothLocation()?.let {
            val (boothDirection, _) = it
            direction = boothDirection
            isWalks = false
            setAttribute("facing_booth", true)
        }
    }

    override fun getIds(): IntArray = NPC_IDS
}
