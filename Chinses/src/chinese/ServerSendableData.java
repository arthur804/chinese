package chinese;

public class ServerSendableData {

	public SendingData moves;
	public int counter;
	
	public ServerSendableData(SendingData moves, int counter){
		this.moves = moves;
		this.counter = counter;
	}
}
