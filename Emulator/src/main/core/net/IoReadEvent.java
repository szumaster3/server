package core.net;

import java.nio.ByteBuffer;

public abstract class IoReadEvent implements Runnable {

	private final IoSession session;

	private ByteBuffer buffer;

	protected boolean usedQueuedBuffer;

	public IoReadEvent(IoSession session, ByteBuffer buffer) {
		this.session = session;
		this.buffer = buffer;
	}

	@Override
	public void run() {
		try {
			if (session.getReadingQueue() != null) {
				buffer = session.getReadingQueue().put(buffer);
				buffer.flip();
				session.setReadingQueue(null);
				usedQueuedBuffer = true;
			}
			read(session, buffer);
		} catch (Throwable t) {
			t.printStackTrace();
			session.disconnect();
		}
	}

	public void queueBuffer(int... data) {
		ByteBuffer queue = ByteBuffer.allocate(data.length + buffer.remaining() + 100_000);
		for (int value : data) {
			queue.put((byte) value);
		}
		queue.put(buffer);
		session.setReadingQueue(queue);
	}

	public abstract void read(IoSession session, ByteBuffer buffer);

}
