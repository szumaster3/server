package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader

/**
 * The run script outgoing packet.
 *
 * @author Snickerize
 */
class RunScriptPacket : OutgoingPacket<OutgoingContext.RunScript> {
    /*
     *  @Override
     *  public void send(RunScriptContext context) {
     *      String string = context.getString();
     *      Object[] objects = context.getObjects();
     *      IoBuffer buffer = new IoBuffer(115, PacketHeader.SHORT);
     *      buffer.putShort(context.getPlayer().getInterfaceManager().getPacketCount(1));
     *      buffer.putString(string);
     *      int j = 0;
     *      for (int i = (string.length() - 1); i >= 0; i--) {
     *          if (string.charAt(i) == 's') {
     *              buffer.putString((String) objects[j]);
     *          } else {
     *              buffer.putInt((Integer) objects[j]);
     *          }
     *          j++;
     *      }
     *      buffer.putInt(context.getId());
     *      context.getPlayer().getDetails().getSession().write(buffer);
     *  }
     */
    override fun send(context: OutgoingContext.RunScript) {
        val types = context.string
        val objects = context.objects
        val buffer = IoBuffer(115, PacketHeader.SHORT)
        buffer.putShort(context.player.interfaceManager.getPacketCount(1))

        buffer.putString(types)
        for ((j, i) in ((types.length - 1) downTo 0).withIndex()) {
            if (types[i] == 's') {
                buffer.putString(objects[j] as String)
            } else {
                buffer.putInt((objects[j] as Int))
            }
        }

        buffer.putInt(context.id)
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        context.player.details.session.write(buffer)
    }
}