
import java.io.Serializable;

public class RPCMessage implements Serializable {
	public static final short REQUEST = 0;
	public static final short REPLY = 1;

	public enum MessageType {
		REQUEST, REPLY
	};

	private MessageType messageType;
	private long transactionId; /* transaction id */
	private long rpcId; /* Globally unique identifier */
	private long requestId; /* Client request message counter */
	private short procedureId; /* e.g.(1,2,3,4) */
	private String csv_data; /* data as comma separated values */
	private short status;

	// Constructor for message object
	public RPCMessage(MessageType msgType, long transID, long rpc_ID, long requestID, short procID, short sta,
			String csv_dt) {
		super();
		messageType = msgType;
		transactionId = transID;
		rpcId = rpc_ID;
		requestId = requestID;
		procedureId = procID;
		status = sta;
		csv_data = csv_dt;
	}

	public RPCMessage() {
		this(MessageType.REPLY, 0L, 0L, 0L, (short) 0, (short) 0, null);
	}

	// Size of object, this is fixed
	public int getSizeInBytes() {
		return 2 + 8 + 8 + 8 + 2 + 2 + csv_data.length() * 2;
	}

	// enumerator for message, can either be request or reply
	public static MessageType fromShort(short value) {
		switch (value) {
		case 0:
			return MessageType.REQUEST;
		case 1:
			return MessageType.REPLY;
		default:
			return null;
		}
	}

	// Getters and setters
	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public long getRpcId() {
		return rpcId;
	}

	public void setRpcId(long rpcId) {
		this.rpcId = rpcId;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public short getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(short procedureId) {
		this.procedureId = procedureId;
	}

	public String getCsv_data() {
		return csv_data;
	}

	public void setCsv_data(String csv_data) {
		this.csv_data = csv_data;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "RPCMessage [messageType=" + messageType + ", transactionId=" + transactionId + ", rpcId=" + rpcId
				+ ", requestId=" + requestId + ", procedureId=" + procedureId + ", csv_data=" + csv_data + ", status="
				+ status + "]";
	}

}