package core.net.packet

import core.api.log
import core.game.bots.AIPlayer
import core.net.packet.out.*
import core.tools.Log
import java.io.PrintWriter
import java.io.StringWriter

/**
 * The packet repository.
 *
 * @author Emperor
 */
object PacketRepository {

    /**
     * The outgoing packets mapping.
     */
    val OUTGOING_PACKETS: Map<Class<*>, OutgoingPacket<out Context>> = buildMap {
        put(LoginPacket::class.java, LoginPacket())
        put(UpdateSceneGraph::class.java, UpdateSceneGraph())
        put(WindowsPane::class.java, WindowsPane())
        put(Interface::class.java, Interface())
        put(SkillLevel::class.java, SkillLevel())
        put(Config::class.java, Config())
        put(AccessMask::class.java, AccessMask())
        put(GameMessage::class.java, GameMessage())
        put(RunScriptPacket::class.java, RunScriptPacket())
        put(RunEnergy::class.java, RunEnergy())
        put(ContainerPacket::class.java, ContainerPacket())
        put(StringPacket::class.java, StringPacket())
        put(Logout::class.java, Logout())
        put(CloseInterface::class.java, CloseInterface())
        put(AnimateInterface::class.java, AnimateInterface())
        put(DisplayModel::class.java, DisplayModel())
        put(InterfaceConfig::class.java, InterfaceConfig())
        put(PingPacket::class.java, PingPacket())
        put(UpdateAreaPosition::class.java, UpdateAreaPosition())
        put(ConstructScenery::class.java, ConstructScenery())
        put(ClearScenery::class.java, ClearScenery())
        put(HintIcon::class.java, HintIcon())
        put(ClearMinimapFlag::class.java, ClearMinimapFlag())
        put(InteractionOption::class.java, InteractionOption())
        put(SetWalkOption::class.java, SetWalkOption())
        put(MinimapState::class.java, MinimapState())
        put(ConstructGroundItem::class.java, ConstructGroundItem())
        put(ClearGroundItem::class.java, ClearGroundItem())
        put(RepositionChild::class.java, RepositionChild())
        put(PositionedGraphic::class.java, PositionedGraphic())
        put(SystemUpdatePacket::class.java, SystemUpdatePacket())
        put(CameraViewPacket::class.java, CameraViewPacket())
        put(MusicPacket::class.java, MusicPacket())
        put(AudioPacket::class.java, AudioPacket())
        put(GrandExchangePacket::class.java, GrandExchangePacket())
        put(BuildDynamicScene::class.java, BuildDynamicScene())
        put(AnimateObjectPacket::class.java, AnimateObjectPacket())
        put(ClearRegionChunk::class.java, ClearRegionChunk())
        put(ContactPackets::class.java, ContactPackets())
        put(CommunicationMessage::class.java, CommunicationMessage())
        put(UpdateClanChat::class.java, UpdateClanChat())
        put(UpdateGroundItemAmount::class.java, UpdateGroundItemAmount())
        // put(WeightUpdate::class.java, WeightUpdate())
        put(UpdateRandomFile::class.java, UpdateRandomFile())
        put(InstancedLocationUpdate::class.java, InstancedLocationUpdate())
        put(CSConfigPacket::class.java, CSConfigPacket())
        put(Varbit::class.java, Varbit())
        put(ResetInterface::class.java, ResetInterface())
        put(VarcUpdate::class.java, VarcUpdate())
        put(InterfaceSetAngle::class.java, InterfaceSetAngle())
    }

    /**
     * Sends a new packet.
     *
     * @param clazz The class of the outgoing packet to send.
     * @param context The context.
     */
    @JvmStatic
    fun send(clazz: Class<out OutgoingPacket<*>>, context: Context) {
        val player = context.player
        if (player is AIPlayer || player.session == null) return

        val packet = OUTGOING_PACKETS[clazz]
        if (packet == null) {
            log(javaClass, Log.ERR, "Invalid outgoing packet [handler=$clazz, context=$context].")
            return
        }

        if (!player.isArtificial) {
            try {
                @Suppress("UNCHECKED_CAST")
                PacketWriteQueue.handle(packet as OutgoingPacket<Context>, context)
            } catch (e: Exception) {
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                e.printStackTrace(pw)
                log(javaClass, Log.ERR, "Error writing packet out: $sw")
            }
        }
    }
}
