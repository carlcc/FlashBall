
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JPanel;


public class AbilityIcon extends JButton {
	private double cd;
	private double cdLeft;
	private String text;
	private boolean isColding;
	
	private Runnable onCdComplete;
	
	public AbilityIcon(String text, int width, int height, double cd) {
		super();
		this.setSize(width, height);
		this.text = text;
		this.setText("fds");
		this.cd = cd;
		this.setEnabled(false);
		this.isColding = false;
	}
	
	public void setOnCdComplete(Runnable onCdComplete) {
		this.onCdComplete = onCdComplete;
	}
	
	public boolean isColding() {
		return this.isColding;
	}
	
	public void cd() {
		this.isColding = true;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				cdLeft = cd;
				while (cdLeft > 0) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					cdLeft -= 0.05;
					repaint();
				}
				isColding = false;
				if (onCdComplete != null)
					onCdComplete.run();
			}
		}).start();
	}

	
	@Override
	public void paint(Graphics g) {
		Image image = this.createImage(this.getWidth(), this.getHeight());
		
		Graphics gg = image.getGraphics();
		gg.setColor(Color.white);
		gg.fillRect(0, 0, this.getWidth(), this.getHeight());
	//	super.paint(gg);
		
		gg.setColor(Color.GREEN);
		gg.fillRect(0, (int)(this.getHeight()*cdLeft/cd), this.getWidth(), this.getHeight());
		
		gg.setColor(Color.black);
		gg.drawString(this.text, this.getWidth()*2/7, this.getHeight()*3/7);

		g.drawImage(image, 0, 0, null);
	}
}
