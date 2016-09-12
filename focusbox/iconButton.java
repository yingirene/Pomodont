package focusbox;

import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class iconButton extends JButton {
	private static final long serialVersionUID = 1L;

	public iconButton(ImageIcon ic){
		this(ic, " ");
	}
	
	public iconButton(ImageIcon ic, String s){
		super(s);
		setIcon(ic);
		setMargin(new Insets(0,0,0,0));
		setBorder(null);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setHorizontalTextPosition(SwingConstants.CENTER);
	}
}
