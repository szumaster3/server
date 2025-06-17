package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader
import core.net.packet.context.RunScriptContext

/**
 * The run script outgoing packet.
 * @author Snickerize
 */
class RunScriptPacket : OutgoingPacket<RunScriptContext> {
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
    override fun send(context: RunScriptContext?) {
        if (context == null) return

        val types = context.string
        val objects = context.objects
        val buffer = IoBuffer(115, PacketHeader.SHORT)
        buffer.putShort(context.player.interfaceManager.getPacketCount(1))

        buffer.putString(types)

        var j = 0
        for (i in types.length - 1 downTo 0) {
            val obj = objects?.getOrNull(j)
            when (types[i]) {
                's' -> {
                    val value = obj as? String ?: ""
                    buffer.putString(value)
                }
                else -> {
                    val value = when (obj) {
                        is Int -> obj
                        is Number -> obj.toInt()
                        else -> 0 // Fallback.
                    }
                    buffer.putInt(value)
                }
            }
            j++
        }

        buffer.putInt(context.id)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        context.player.details.session.write(buffer)
    }
}