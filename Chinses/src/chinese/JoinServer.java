package chinese;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class JoinServer implements Runnable {

	private String host;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	private Socket socket;
	private MyChineseCheckersPanel pan;
	private MainFrame dady;

	public JoinServer(MyChineseCheckersPanel pan, MainFrame dady, String host) {
		this.pan = pan;
		this.dady = dady;

		connectToServer();
		this.host = host;
	}

	private void connectToServer() {
		try {
			// Create a socket to connect to the server

			socket = new Socket(host, 8000);

			fromServer = new DataInputStream(socket.getInputStream());

			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (Exception ex) {
		}

		// Control the game on a separate thread
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() // I HAVE JOINED
	{
		try {
			pan.doPlayerAmount(fromServer.readInt());
			dady.redoPicking();
			// socket.setKeepAlive(true);
		} catch (IOException e) {
		}

		// /\ this works

		while (true) {
			sending();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/*private void sendings() {
		try {
			toServer.writeBoolean(pan.wantToSend);

			if (pan.wantToSend) {
				pan.wantToSend = false;
				toServer.writeInt(69);
				System.out.println("I hate testing");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	private void sending() {
		ServerSendableData data = null;
		try {
			toServer.writeBoolean(pan.wantToSend);

			if (pan.wantToSend) {
				pan.wantToSend = false;
				data = pan.giveMeMyData();
				ServerDataHandeler.write(data, toServer);
			} else {
				boolean gotNewData = fromServer.readBoolean();
				if (gotNewData) {
					data = ServerDataHandeler.get(fromServer);
					pan.revreshMyData(data);
				}
			}

		} catch (IOException e) {
		}
	}

	public void closeServer() throws IOException {
		socket.close();// yeah never gonna use this
	}
}
