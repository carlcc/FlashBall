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
		JMenu menu = new JMenu("��Ϸ");
		JMenuItem item = new JMenuItem("��ʼ");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
		});
		menu.add(item);
		item = new JMenuItem("����");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "����˵����\n�������Ҽ�ͷ������������\nq------cd 5�룬5�������������Ժ㶨�ٶ��ƶ���\n" + 
						"w------���֣�cd 5�룬˲���ƶ���ǰ��һ�����룬�����뵱ǰ�ٶ��йأ����ֵ���ɫ�򴦣�\ne------�޵У�" +
						"cd 5�룬3�����޵�\ns------��ʼ��Ϸ\n�÷���ʾ�ڴ��������";
				
				JOptionPane.showMessageDialog(thisForm, msg);
			}
		});
		menu.add(item);
		item = new JMenuItem("����");
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
			new AbilityIcon("����", 100, 100, 5),
			new AbilityIcon("����", 100, 100, 5),
			new AbilityIcon("�޵�", 100, 100, 8),
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