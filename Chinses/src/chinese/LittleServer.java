package chinese;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LittleServer implements Runnable {

	// public int maxPlayers = 6;
	private Socket[] players;
	private int maxPlayers;
	private ServerSocket serverSocket;

	public void makeAble(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	@Override
	public void run() {
		try {
			players = new Socket[maxPlayers];

			serverSocket = new ServerSocket(8000);
			for (int i = 0; i < maxPlayers; i++) {
				players[i] = serverSocket.accept();
			}

			SecretServer task = new SecretServer(players);

			new Thread(task).start();
		} catch (IOException e) {

		}
	}

	public void close() throws IOException {
		serverSocket.close();
	}

}

class SecretServer implements Runnable {
	// TODO google why you would make finals in java

	private DataOutputStream[] toPlayers;
	private DataInputStream[] fromPlayers;
	private final int maxPlayers;

	public SecretServer(Socket[] players) throws IOException {
		this.maxPlayers = players.length;
		toPlayers = new DataOutputStream[maxPlayers];
		for (int i = 0; i < toPlayers.length; i++)
			toPlayers[i] = new DataOutputStream(players[i].getOutputStream());
		fromPlayers = new DataInputStream[maxPlayers];
		for (int i = 0; i < toPlayers.length; i++)
			fromPlayers[i] = new DataInputStream(players[i].getInputStream());

	}

	@Override
	public void run() {
		for (int i = 0; i < maxPlayers; i++)
			try {
				toPlayers[i].writeInt(maxPlayers);
				// players[i].setKeepAlive(true);// TODO WHY
			} catch (IOException e) {
			}

		// /\ this works
		while (true) {
			sending();
			for (int i = 0; i < maxPlayers; i++)
				try {
					toPlayers[i].flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*private void sendings() {
		for (int i = 0; i < maxPlayers; i++) {
			boolean wantTo;
			try {
				wantTo = fromPlayers[i].readBoolean();
				int aLot = 1;
				if (wantTo){
					aLot = fromPlayers[i].readInt();
					System.out.println(""+aLot);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}*/

	private void sending() {
		ServerSendableData data = null;
		try {
			for (int i = 0; i < maxPlayers; i++) {
				boolean wantTo = fromPlayers[i].readBoolean();

				if (wantTo) {
					data = ServerDataHandeler.get(fromPlayers[i]);

					// resiving player
					for (int ii = 0; ii < maxPlayers; ii++) {
						if (ii == i)
							continue;
						toPlayers[ii].writeBoolean(true);
						ServerDataHandeler.write(data, toPlayers[ii]);
					}

					data = null;
				} else {
					toPlayers[i].writeBoolean(false);
				}
			}
		} catch (IOException e) {
		}
	}
}