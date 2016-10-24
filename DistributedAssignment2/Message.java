
import java.io.Serializable;
import java.nio.ByteBuffer;

public class Message implements Serializable {
	private static final long serialVersionUID = 227L;
	private byte data[];
	private int length;

	public Message(byte[] data, int length) {
		super();
		this.data = data;
		this.length = length;
	}

	public Message() {
		this(null, 0);
	}

	public byte[] getData() {
		return data;
	}

	// Sets data in bytes whem marshalling
	public void setData(byte[] data) {
		this.data = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			this.data[i] = data[i];
		}
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public RPCMessage unMarshal() {
		// System.out.println("Unmarshalling...");
		RPCMessage rm = new RPCMessage();
		ByteBuffer bb2 = ByteBuffer.wrap(getData());
		// unmarshals data, into appropriate object structure,
		// Increases bye index as we know the fixed max-length of each data type
		int index = 0;

		short ordinal = bb2.getShort(index);
		rm.setMessageType(RPCMessage.fromShort(ordinal));
		index += 2;

		rm.setTransactionId(bb2.getLong(index));
		index += 8;

		rm.setRpcId(bb2.getLong(index));
		index += 8;

		rm.setRequestId(bb2.getLong(index));
		index += 8;

		rm.setProcedureId(bb2.getShort(index));
		index += 2;

		rm.setStatus(bb2.getShort(index));
		index += 2;

		StringBuffer sb = new StringBuffer();
		for (; index < bb2.array().length; index += 2) {
			sb.append(bb2.getChar(index));
		}
		rm.setCsv_data(sb.toString());

		return rm;
	}

	public void marshal(RPCMessage rm) {
		// System.out.println("Marshalling...");
		int bufferSize = rm.getSizeInBytes();
		ByteBuffer bb = ByteBuffer.allocate(bufferSize);
		//Marshalls the data
		short msgTypeNum = (short) rm.getMessageType().ordinal();
		int index = 0;
		bb.putShort(0, msgTypeNum);
		index += 2;

		bb.putLong(index, rm.getTransactionId());
		index += 8;

		bb.putLong(index, rm.getRpcId());
		index += 8;

		bb.putLong(index, rm.getRequestId());
		index += 8;

		bb.putShort(index, rm.getProcedureId());
		index += 2;

		bb.putShort(index, rm.getStatus());
		index += 2;

		String csv_data = rm.getCsv_data();
		for (int i = 0; i < csv_data.length(); i++, index += 2) {
			bb.putChar(index, csv_data.charAt(i));
		}
		//Sets the data
		setData(bb.array());
		setLength(bb.array().length);

	}

}
