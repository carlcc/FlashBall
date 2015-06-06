import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class GamePanel extends JPanel {

	private final double MAX_PLAYER_SPEED = 5d;
	private final double PLAYER_ACCELERATION = 0.5;
	private final double MAX_SPEED = 3d;
	private final int PLAYER_SIZE = 9;
	private final int ENERMY_SIZE = 5;
	
	// 默认的KeyListener，有加速度
	private KeyListener defaultKeyListener = new KeyListener() {
		
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				player.setAy(-PLAYER_ACCELERATION);
				return;
			case KeyEvent.VK_DOWN:
				player.setAy(PLAYER_ACCELERATION);
				return;
			case KeyEvent.VK_LEFT:
				player.setAx(-PLAYER_ACCELERATION);
				return;
			case KeyEvent.VK_RIGHT:
				player.setAx(PLAYER_ACCELERATION);
			default:
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_DOWN:
				player.setAy(0);
				return;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_RIGHT:
				player.setAx(0);
			default:
				break;
			}
		}
		
		@Override
		public void keyTyped(KeyEvent e) {
		}
	};

	// 使用恒定速度的技能后使用的KeyListener， 没有加速度
	private KeyListener stableKeyListener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
				case KeyEvent.VK_DOWN:
					player.setVy(0);
					return;
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
					player.setVx(0);
				default:
					break;
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					player.setVy(-player.getMax_vy());
					return;
				case KeyEvent.VK_DOWN:
					player.setVy(player.getMax_vy());
					return;
				case KeyEvent.VK_LEFT:
					player.setVx(-player.getMax_vx());
					return;
				case KeyEvent.VK_RIGHT:
					player.setVx(player.getMax_vx());
				default:
					break;
				}
			}
		};


	private GameForm thisForm = null;
	private int score;
	private GameObject player;
	private ArrayList<GameObject> enermies;
	private Thread gameThread;
	private Random rand = new Random();
	private KeyListener usedKeyListener = defaultKeyListener;
	
	
	public GamePanel(int width, int height, GameForm parentFrame) {
		this.thisForm = parentFrame;
		
		this.init(width, height);
		
	}
	

	private void init(int width, int height) {
		this.player = new GameObject(width/2, height/2, PLAYER_SIZE, width, height);
		this.player.setMax_vx(MAX_PLAYER_SPEED);
		this.player.setMax_vy(MAX_PLAYER_SPEED);
		this.enermies = new ArrayList<GameObject>(16);
		
		for (int i = 0; i < 4; ++i) {
			this.enermies.add(new GameObject(0, rand.nextInt(height), ENERMY_SIZE, width, height));
			this.enermies.add(new GameObject(width, rand.nextInt(height), ENERMY_SIZE, width, height));
			this.enermies.add(new GameObject(rand.nextInt(width), 0, ENERMY_SIZE, width, height));
			this.enermies.add(new GameObject(rand.nextInt(width), height, ENERMY_SIZE, width, height));
		}
		for (int i = 0; i < 16; ++i) {
			GameObject o = this.enermies.get(i);
			o.setTarget(this.player);
			o.setMax_vx(MAX_SPEED);
			o.setMax_vy(MAX_SPEED);
			o.randomSpeed();
		}
		this.score = 0;
		
		this.thisForm.getAbilityIcons()[0].setOnCdComplete(new Runnable() {
			
			@Override
			public void run() {
				usedKeyListener = defaultKeyListener;
			}
		});
	}
	
	public void startGame() {
		stopGame();
		init(this.getWidth(), this.getHeight());
		this.gameThread = new Thread(new Runnable() {
						
			@Override
			public void run() {
				boolean collision = false;
				int width = thisForm.getWidth();
				int height = thisForm.getHeight();
				Random random = new Random();
				
				while (true) {
					for (int i = 0; i < enermies.size(); ++i) {
						enermies.get(i).stepIgnoreAcceleration();
						if (player.collision(enermies.get(i)))
							collision = true;
					}
					if (usedKeyListener == defaultKeyListener)
						player.step();
					else if (usedKeyListener == stableKeyListener)
						player.stepIgnoreAcceleration();
					
					repaint();

					score++;
					if (score % 20 == 0) {
						GameObject o = null;
						switch (rand.nextInt(4)) {
						case 1:
							enermies.add(o = new GameObject(0, random.nextInt(height), ENERMY_SIZE, width, height));
							break;
						case 2:
							enermies.add(o = new GameObject(width, rand.nextInt(height), ENERMY_SIZE, width, height));
							break;
						case 0:
							enermies.add(o = new GameObject(rand.nextInt(width), 0, ENERMY_SIZE, width, height));
							break;
						case 3:
							enermies.add(o = new GameObject(rand.nextInt(width), height, ENERMY_SIZE, width, height));
							break;
						default:
							break;
						}
						o.setTarget(player);
						o.setMax_vx(MAX_SPEED);
						o.setMax_vy(MAX_SPEED);
						o.randomSpeed();
					}
					thisForm.setTitle("point count: " + enermies.size() + "   score: " + score);
					if (collision) {
						JOptionPane.showMessageDialog(null, "Game Over");
						return;
					}
					
					
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		this.gameThread.start();
	}
	
	public void stopGame() {
		if (this.gameThread == null) {
			return;
		}
		
		this.gameThread.stop();
	//	this.gameThread.interrupt();
	}
	
	@Override
	public void paint(Graphics g) {
		Image image = this.createImage(this.getWidth(), this.getHeight());
		
		Graphics gg = image.getGraphics();
		gg.setColor(Color.gray);
		gg.fillRect(0, 0, this.getWidth(), this.getHeight());
		super.paint(gg);
		
		for (int i = 0; i < this.enermies.size(); ++i) {
			GameObject o = this.enermies.get(i);
			gg.fillOval((int)(o.getX()-o.getRadius()), (int)(o.getY()-o.getRadius()),
					o.getRadius()*2, o.getRadius()*2);
			
		}
		GameObject flashPreview = new GameObject(this.player);
		flashPreview.flash();
		gg.setColor(Color.LIGHT_GRAY);
		gg.fillOval((int)(flashPreview.getX()-flashPreview.getRadius()), (int)(flashPreview.getY()-flashPreview.getRadius()),
				flashPreview.getRadius()*2, flashPreview.getRadius()*2);
		
		GameObject o = this.player;
		if (o.isInvincible())
			gg.setColor(Color.yellow);
		else
			gg.setColor(Color.blue);
		gg.fillOval((int)(o.getX()-o.getRadius()), (int)(o.getY()-o.getRadius()),
				o.getRadius()*2, o.getRadius()*2);
		
		g.drawImage(image, 0, 0, null);
	}
	

	
	public void keyPressed(KeyEvent e) {
		this.usedKeyListener.keyPressed(e);
	}
	
	public void keyReleased(KeyEvent e) {
		this.usedKeyListener.keyReleased(e);
	}
	
	public void keyTyped(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 's':
			startGame();
			break;

		case 'q':
			if (!this.thisForm.getAbilityIcons()[0].isColding()) {
				this.usedKeyListener = this.stableKeyListener;
				this.player.setAx(0);
				this.player.setAy(0);
				this.player.setVx(0);
				this.player.setVy(0);
				this.thisForm.getAbilityIcons()[0].cd();
			}
			break;
			
		case ' ':
		case 'w':
			if (!this.thisForm.getAbilityIcons()[1].isColding()) {
				this.player.flash();
				this.thisForm.getAbilityIcons()[1].cd();
			}
			break;
			
		case 'e':
			if (!this.thisForm.getAbilityIcons()[2].isColding()) {
				this.player.invincible(3000);
				this.thisForm.getAbilityIcons()[2].cd();
			}
			break;
		default:
			break;
		}
	}
}

