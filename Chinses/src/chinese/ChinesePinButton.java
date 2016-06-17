package chinese;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class ChinesePinButton extends JButton {
	public ChinesePinButton(String label) {
		super(label);
		// This call causes the JButton not to paint
		// the background.
		// This allows us to paint a round background.
		setContentAreaFilled(false);
	}

	public void setSize(int size){
		super.setSize(size, size);
	}
	
	// Paint the round background and label.
	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

		// This call will paint the label and the
		// focus rectangle.
//		super.paintComponent(g);
	}

	// Paint the border of the button using a simple stroke.
	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
		g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
	}

	// Hit detection.
	Shape shape;

	public boolean contains(int x, int y) {
		// If the button has changed size,
		// make a new shape object.
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		return shape.contains(x, y);
	}

	public void setBounds(int i, int j, int k) {
		super.setBounds(i, j, k, k);
		
	}

}