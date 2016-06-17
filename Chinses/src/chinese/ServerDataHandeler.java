package chinese;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerDataHandeler {

	public static void write(ServerSendableData data, DataOutputStream to) throws IOException{
		int size = data.moves.size();
		to.writeInt(size);
		for (int i = 0; i < size; i++){
			to.writeInt(data.moves.get(i));
		}
		to.writeInt(data.counter);
	}
	
	public static ServerSendableData get(DataInputStream from) throws IOException{
		ServerSendableData data = null;
		int size = from.readInt();
		
		SendingData moves = new SendingData();
		for (int i = 0; i < size; i++)
			moves.add(from.readInt());
		int counter = from.readInt();
		
		data = new ServerSendableData(moves, counter);
		
		return data;
	}
}
