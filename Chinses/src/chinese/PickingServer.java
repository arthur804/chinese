package chinese;

import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class PickingServer extends JFrame{

	private static final long serialVersionUID = 2998507721011856504L;

	private JTextField ipadressGetter;

	private int players;
	private MainFrame dady;
	private boolean startServer;
	
	public PickingServer(int x, int y, int players, MainFrame dady, boolean startServer){
		setVisible(true);
		setResizable(false);
		setLocation(x, y);
		setSize(300, 300);
		ipadressGetter = new JTextField("                                             ");
		ipadressGetter.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e){
				ipadressGetter.setText("");
			}
		});
		
		JButton done = new JButton("Ok");
		done.addActionListener(ActionListener -> {
			startServer();
		});
		
		JButton doneLocal = new JButton("Local Host");
		doneLocal.addActionListener(ActionListener -> {
			startLocal();
		});
		
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		getContentPane().add(ipadressGetter);
		getContentPane().add(done);
		getContentPane().add(doneLocal);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.players = players;
		this.dady = dady;
		this.startServer = startServer;
		setAlwaysOnTop(true);
	}
	
	public void startServer(){
		String string = ipadressGetter.getText();
		if (startServer)
		dady.pickServerToMake(players, string);
		else {
			dady.pickServerToJoin(string);
			dady.redoPicking();
		}
		dispose();
	}
	
	public void startLocal(){
		if (startServer)
			dady.pickServerToMake(players, "localhost");
			else {
				dady.pickServerToJoin("localhost");
				dady.redoPicking();
			}
		dispose();
		
	}
}
