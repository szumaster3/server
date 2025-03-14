package core.net.event;

import core.game.world.GameWorld;
import core.net.IoReadEvent;
import core.net.IoSession;
import core.net.amsc.ManagementServerState;
import core.net.amsc.WorldCommunicator;
import core.net.producer.MSEventProducer;
import core.tools.Log;

import java.nio.ByteBuffer;

import static core.api.ContentAPIKt.log;

/**
 * The type Registry read event.
 */
public final class RegistryReadEvent extends IoReadEvent {

    private static final MSEventProducer PRODUCER = new MSEventProducer();

    /**
     * Instantiates a new Registry read event.
     *
     * @param session the session
     * @param buffer  the buffer
     */
    public RegistryReadEvent(IoSession session, ByteBuffer buffer) {
        super(session, buffer);
    }

    @Override
    public void read(IoSession session, ByteBuffer buffer) {
        int opcode = buffer.get() & 0xFF;
        switch (opcode) {
            case 0:
                WorldCommunicator.setState(ManagementServerState.NOT_AVAILABLE);
                log(this.getClass(), Log.ERR, "Failed registering world to AMS - [id=" + GameWorld.getSettings().getWorldId() + ", cause=World id out of bounds]!");
                break;
            case 1:
                session.setProducer(PRODUCER);
                WorldCommunicator.setState(ManagementServerState.AVAILABLE);
                break;
            case 2:
            case 3:
                WorldCommunicator.setState(ManagementServerState.NOT_AVAILABLE);
                break;
            default:

                break;
        }
    }

}