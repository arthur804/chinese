package chinese;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MyChineseCheckersPanel extends JPanel {

	private int screenX, screenY;
	private ChinesePinButton[] btnArray;
	protected int playerAmount = 0;
	private Color[] colorsToPlayers = new Color[] { Color.red, Color.white, Color.green, Color.blue, Color.magenta,
			Color.yellow };
	public int holdingColor = -1;
	public SendingData data = new SendingData();
	public int[] possibleHolds;
	private boolean threePlayers = false;
	public int turnCounter = 0;
	public int maxPlayers = 6;
	public boolean wantToSend;

	public MyChineseCheckersPanel(int screenX, int screenY) {
		this.screenX = screenX - 10;
		this.screenY = screenY - 10;
		setLayout(null);
		setBackground(Color.orange);
		fillMe();
	}

	private void fillMe() {
		// 13
		btnArray = new ChinesePinButton[121];
		int counter = 1;
		int arrayCounter = 0;
		boolean rowNotDone = true;
		boolean thing = false;

		for (int i = 0; i < 17; i++) {
			for (int ii = 0; ii < counter; ii++) {
				ChinesePinButton btnNewButton = new ChinesePinButton("");
				btnNewButton.setBackground(Color.black);
				final int ARRAYCOUNTER = arrayCounter;
				btnNewButton.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent e) {
						boolean rightMouse = (e.getButton() == MouseEvent.BUTTON1);
						movePice(ARRAYCOUNTER, rightMouse);
					}
				});
				btnArray[arrayCounter] = btnNewButton;
				arrayCounter++;
			}
			Awnser awns = counterCalc(rowNotDone, counter, thing, i);
			counter = awns.counter;
			thing = awns.thing;
			rowNotDone = awns.rowNotDone;
		}
	}

	private void movePice(int counter, boolean rightMouse) {
		Color myCol = btnArray[counter].getBackground();
		if (!rightMouse && !data.isEmpty()) {
			data.add(counter);
			if (myCol.equals(Color.black)) {
				btnArray[counter].setForeground(new Color (255, 91,91));
			}
		} else {
			if (possibleHolds == null)
				return;// TODO Waring Message you hava no team
			if (holdingColor == -1) {
				if (!data.isEmpty()) {
					for (int i = 0; i < data.size() - 1; i++) {
						btnArray[data.get(i)].setForeground(Color.black);
						btnArray[data.get(i)].setBackground(Color.BLACK);
					}

				}
				for (int i = 0; i < colorsToPlayers.length; i++)
					if (myCol.equals(colorsToPlayers[i])) {
						for (int ii = 0; ii < possibleHolds.length; ii++)
							if (i == possibleHolds[ii] && i == turnCounter)
								holdingColor = i;
						break;
					}

				if (holdingColor == -1)
					return;
				else
					data.clear();
				btnArray[counter].setBackground(Color.black);
				btnArray[counter].setForeground(Color.blue);
				data.add(counter);
			} else if (myCol.equals(colorsToPlayers[holdingColor])) {
				if (!data.isEmpty()) {
					for (int i = 0; i < data.size(); i++) {
						btnArray[data.get(i)].setForeground(Color.black);
					}
					btnArray[data.get(0)].setBackground(colorsToPlayers[holdingColor]);
					data.retryFirst(counter);
				}
				btnArray[counter].setBackground(Color.black);
				btnArray[counter].setForeground(Color.blue);
			} else if (myCol.equals(Color.black)) {
				if (holdingColor == -1)
					return;// TODO something funn
				if (counter == data.get(0))
					return;
				data.add(counter);
				for (int i = 0; i < data.size(); i++) {
					btnArray[data.get(i)].setForeground(Color.black);
				}
				btnArray[counter].setBackground(colorsToPlayers[holdingColor]);
				holdingColor = -1;
				// ***
				turnCounter++;
				if (turnCounter == maxPlayers)
					turnCounter = 0;
				wantToSend = true;
				// ***

			}
		}
	}

	private Awnser counterCalc(boolean rowNotDone, int counter, boolean thing, int i) {
		if (rowNotDone && counter == 4 && !thing) {
			counter += 9;
		} else if (rowNotDone && counter > 5 && !thing) {
			if (counter == 9) {
				rowNotDone = false;
				counter++;
			} else
				counter--;
		} else if (i != 12 && !thing)
			counter++;
		else {
			if (i == 12 && !thing) {
				counter -= 8;
				thing = true;
			}
			counter--;
		}
		return new Awnser(thing, rowNotDone, counter);
	}

	private void refillMe() {
		int smallestOne;
		if (screenX < screenY)
			smallestOne = screenX;
		else
			smallestOne = screenY;
		int counter = 1;
		int arrayCounter = 0;

		int calco = Math.floorDiv(smallestOne, 20);
		int size = calco + 10;
		int x = screenX / 2;
		int y = screenY / 17;
		boolean rowNotDone = true;
		boolean thing = false;

		for (int i = 0; i < 17; i++) {
			for (int ii = 0; ii < counter; ii++) {
				ChinesePinButton btnNewButton = btnArray[arrayCounter];
				// if even
				int formula = ii * size - size * (counter / 2);
				if (i % 2 == 0)
					btnNewButton.setBounds(x - formula, 5 + y * i, size - 10);
				else
					btnNewButton.setBounds(x - (size / 2) - formula, 5 + y * i, size - 10);
				arrayCounter++;
				add(btnNewButton);
			}
			Awnser awns = counterCalc(rowNotDone, counter, thing, i);
			counter = awns.counter;
			thing = awns.thing;
			rowNotDone = awns.rowNotDone;
		}
	}

	public void repaint() {
		this.screenX = getWidth() - 10;
		this.screenY = getHeight() - 10;
		if (btnArray != null)
			refillMe();
		super.repaint();
	}

	public void pickPlayer(int i) {
		if (threePlayers) {
			i = i * 2;
			possibleHolds = new int[] { i, i + 1 };
		} else
			possibleHolds = new int[] { i };
	}

	public void undoLastMove() {
		if (data.isEmpty())
			return;
		Color myCol = btnArray[data.get(data.size() - 1)].getBackground();
		for (int i = 0; i < data.size(); i++) {
			btnArray[data.get(i)].setForeground(Color.black);
		}
		btnArray[data.get(data.size() - 1)].setBackground(Color.black);
		btnArray[data.get(0)].setBackground(myCol);
		turnCounter--;
		if (turnCounter == -1)
			turnCounter = maxPlayers - 1;
		data.clear();
		// TODO SEND TO SERVER
	}

	public void displayPrevMove(SendingData newData) {
		if (data.size() == newData.size() && data.get(data.size() - 1) == newData.get(data.size() - 1))
			return;
		data = newData;
		Color myCol = btnArray[data.get(0)].getBackground();
		for (int i = 0; i < data.size(); i++) {
			btnArray[data.get(i)].setForeground(new Color (255, 91,91));
		}
		btnArray[data.get(0)].setForeground(Color.blue);
		btnArray[data.get(0)].setBackground(Color.black);
		btnArray[data.get(data.size() - 1)].setBackground(myCol);
		btnArray[data.get(data.size() - 1)].setForeground(Color.black);
	}

	public ServerSendableData giveMeMyData() {
		ServerSendableData bobCat = new ServerSendableData(data, turnCounter);
		return bobCat;
	}

	public void revreshMyData(ServerSendableData sent) {
		displayPrevMove(sent.moves);
		turnCounter = sent.counter;
	}

	// MUST HAVE BUT NOT INTRESTING
	public void doPlayerAmount(int amount) {
		playerAmount = amount;
		threePlayers = false;
		colorsToPlayers = new Color[] { Color.red, Color.white, Color.green, Color.blue, Color.magenta, Color.yellow };
		switch (amount) {
		default:
			twoPlayers();
			break;
		case 3:
			threeOrSixPlayers(true);
			break;
		case 4:
			fourPlayers();
			break;
		case 6:
			threeOrSixPlayers(false);
			break;
		}
		repaint();
	}

	private void twoPlayers() {
		clearBoard();

		maxPlayers = 2;
		doPlayerOne();
		doPlayerFour();
		colorsToPlayers = new Color[] { Color.red, Color.blue };
	}

	private void threeOrSixPlayers(boolean three) {
		clearBoard();

		doPlayerOne();
		doPlayerTwo();
		doPlayerThree();
		doPlayerFour();
		doPlayerFive();
		doPlayerSix();
		if (three)
			threePlayers = true;
		maxPlayers = 6;
	}

	private void fourPlayers() {
		clearBoard();

		doPlayerOne();
		doPlayerTwo();

		maxPlayers = 4;
		doPlayerFour();
		doPlayerFive();
		colorsToPlayers = new Color[] { Color.red, Color.white, Color.blue, Color.magenta };
	}

	public void clearBoard() {
		for (int i = 0; i < 121; i++) {
			btnArray[i].setBackground(Color.black);
			btnArray[i].setForeground(Color.black);
		}
		possibleHolds = null;
	}

	private void doPlayerOne() {
		Color col = colorsToPlayers[0];
		for (int i = 0; i < 10; i++)
			btnArray[i].setBackground(col);
	}

	private void doPlayerTwo() {
		Color col = colorsToPlayers[1];
		for (int i = 10; i <= 13; i++)
			btnArray[i].setBackground(col);
		for (int i = 23; i <= 25; i++)
			btnArray[i].setBackground(col);
		for (int i = 35; i <= 36; i++)
			btnArray[i].setBackground(col);
		btnArray[46].setBackground(col);
	}

	private void doPlayerThree() {
		Color col = colorsToPlayers[2];
		for (int i = 98; i <= 101; i++)
			btnArray[i].setBackground(col);
		for (int i = 86; i <= 88; i++)
			btnArray[i].setBackground(col);
		for (int i = 75; i <= 76; i++)
			btnArray[i].setBackground(col);
		btnArray[65].setBackground(col);
	}

	private void doPlayerFour() {
		Color col = colorsToPlayers[3];
		for (int i = 111; i < 121; i++)
			btnArray[i].setBackground(col);
	}

	private void doPlayerFive() {
		Color col = colorsToPlayers[4];
		for (int i = 107; i <= 110; i++)
			btnArray[i].setBackground(col);
		for (int i = 95; i <= 97; i++)
			btnArray[i].setBackground(col);
		for (int i = 84; i <= 85; i++)
			btnArray[i].setBackground(col);
		btnArray[74].setBackground(col);
	}

	private void doPlayerSix() {
		Color col = colorsToPlayers[5];
		for (int i = 19; i <= 22; i++)
			btnArray[i].setBackground(col);
		for (int i = 32; i <= 34; i++)
			btnArray[i].setBackground(col);
		for (int i = 44; i <= 45; i++)
			btnArray[i].setBackground(col);
		btnArray[55].setBackground(col);
	}

	public void doPlayerAmountWithoutServer(int x) {
		doPlayerAmount(x);
		// TODO Auto-generated method stub
		possibleHolds = new int[x];
		for (int i  = 0; i < x; i ++)
			possibleHolds[i] = i;
	}
}

class Awnser {
	boolean thing, rowNotDone;
	int counter;

	Awnser(boolean thing, boolean rowNotDone, int counter) {
		this.counter = counter;
		this.rowNotDone = rowNotDone;
		this.thing = thing;
	}
}
