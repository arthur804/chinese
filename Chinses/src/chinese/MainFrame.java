package chinese;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private LittleServer server;

	public static void main(String[] args) {
		new MainFrame();
	}

	private MyChineseCheckersPanel pan;
	private JMenu picking;

	public MainFrame() {
		this.setVisible(true);
		pan = new MyChineseCheckersPanel(500, 500);
		getContentPane().add(pan);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 500);
		this.setLocation(400, 0);
		this.setAlwaysOnTop(true);
		addBars();
		revalidate();

	}

	private void addBars() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Chinese Checkers");
		menuBar.add(mnNewMenu);

		String[] number = new String[] { "Two", "Three", "Four", "", "Six" };

		for (int i = 0; i < 5; i++) {
			if (i == 3)
				continue;
			JMenuItem players = new JMenuItem("New Game with " + number[i]);
			final int I = i;
			players.addActionListener(ActionListener -> {
				pan.doPlayerAmountWithoutServer(I + 2);
			});
			mnNewMenu.add(players);
		}

		JMenuItem clearPlayers = new JMenuItem("Clear Board");
		clearPlayers.addActionListener(ActionListener -> {
			pan.clearBoard();
			revalidate();
		});
		mnNewMenu.add(clearPlayers);

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(ActionListener -> {
			System.exit(0);
		});
		mnNewMenu.add(exit);

		JMenu server = new JMenu("Server");
		menuBar.add(server);

		JMenuItem join = new JMenuItem("Join server");
		join.addActionListener(ActionListener -> {
			new PickingServer(getX(), getY(), 0, this, true);
		});
		server.add(join);

		for (int i = 0; i < 5; i++) {
			if (i == 3)
				continue;
			JMenuItem servPlayers = new JMenuItem("Start new Server with " + number[i]);
			final int I = i;
			servPlayers.addActionListener(ActionListener -> {
				this.server = new LittleServer();
				new PickingServer(getX(), getY(), I, this, true);
			});
			server.add(servPlayers);
		}

		picking = new JMenu("Picking");
		menuBar.add(picking);

		JMenuItem undo = new JMenuItem("Undo Last Move");
		undo.addActionListener(ActionListener -> {
			pan.undoLastMove();
		});
		menuBar.add(undo);

	}

	public void redoPicking() {
		picking.removeAll();
		for (int i = 1; i <= pan.playerAmount; i++) {
			JMenuItem playerPick = new JMenuItem("I am player -> " + i);
			final int I = i;
			playerPick.addActionListener(ActionListener -> {
				pan.pickPlayer(I - 1);

			});
			picking.add(playerPick);
		}
	}

	public void pickServerToMake(int players, String host) {
		try {
			server = new LittleServer();
			server.makeAble(players + 2);
			new Thread(this.server).start();

			new JoinServer(pan, this, host);
		} catch (Exception e) {
			// TODO make a nice waring
			e.printStackTrace();
			// yo server done goofed
		}

	}

	public void pickServerToJoin(String host) {
		try {
			new JoinServer(pan, this, host);
		} catch (Exception e) {
			// TODO make a nice waring
			e.printStackTrace();
		}
	}
}
