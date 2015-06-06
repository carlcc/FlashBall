import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.print.attribute.standard.Media;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class GameForm extends JFrame {

	private GamePanel gp;
	private JPanel abilityPanel;
	private AbilityIcon[] abilityIcons;
	
	private GameForm thisForm;
	
	public GameForm() {
		this(1000, 500);
	}
	
	public GameForm(int width, int height) {
		super();
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("游戏");
		JMenuItem item = new JMenuItem("开始");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
		});
		menu.add(item);
		item = new JMenuItem("帮助");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "按键说明：\n上下左右箭头控制上下左右\nq------cd 5秒，5秒内上下左右以恒定速度移动。\n" + 
						"w------闪现，cd 5秒，瞬间移动到前方一定距离，距离与当前速度有关（闪现到灰色球处）\ne------无敌，" +
						"cd 5秒，3秒内无敌\ns------开始游戏\n得分显示在窗体标题上";
				
				JOptionPane.showMessageDialog(thisForm, msg);
			}
		});
		menu.add(item);
		item = new JMenuItem("关于");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "Just for fun";
				
				JOptionPane.showMessageDialog(thisForm, msg);
			}
		});
		menu.add(item);
		menuBar.add(menu);
		this.add(menuBar, BorderLayout.NORTH);

		this.abilityPanel = new JPanel();
		this.abilityPanel.setSize(width, 400);
		this.abilityPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.abilityIcons = new AbilityIcon[] {
			new AbilityIcon("疾走", 100, 100, 5),
			new AbilityIcon("闪现", 100, 100, 5),
			new AbilityIcon("无敌", 100, 100, 8),
		};
		gp = new GamePanel(width, height, this);
		for (int i = 0; i < this.abilityIcons.length; ++i)
			this.abilityPanel.add(this.abilityIcons[i]);
		
		
		this.add(this.abilityPanel, BorderLayout.SOUTH);
		this.add(gp, BorderLayout.CENTER);
		
		
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				gp.keyTyped(e);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				gp.keyReleased(e);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				gp.keyPressed(e);
			}
		});
	}
	
	public void startGame() {
		gp.startGame();
	}
	
	public AbilityIcon[] getAbilityIcons() {
		return this.abilityIcons;
	}
}