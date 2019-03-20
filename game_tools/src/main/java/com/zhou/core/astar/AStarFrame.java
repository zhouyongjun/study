package com.zhou.core.astar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhou.core.astar.RectCell.CellType;
public class AStarFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(AStarFrame.class);
	private JPanel contentPane ;
	boolean isRuned;
	boolean isNeedOnceRepaint = true;
	AStar aStar;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AStarFrame frame = new AStarFrame();
					AStar aStar = new AStar();
					aStar.frame = frame;
					aStar.targetCell = RectCell.newInstance(CellType.SPACE,760,760,true); 
					aStar.loadInitDataFromFile();
					frame.setVisible(true);
					frame.setAStar(aStar);
				} catch (Exception e) { 
					logger.error("",e);
				}
			}
		});
	}
	public void setAStar(AStar aStar) {
		this.aStar = aStar;
	}
	static class MyRunnable implements Runnable {
		AStarFrame frame;
		MyRunnable(AStarFrame frame) {
			this.frame = frame;
		}
		@Override
		public void run() {
			try {
				frame.tryTo();
			} catch (Exception e) {
				logger.error("",e);
			}
		}
		
	}
	 class MyPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2d = (Graphics2D)g;

			for (RectCell column : aStar.getCells().values()) {
				Color color = Color.black;
				if (column.isBlock()) {
					g2d.setColor(color);
					g2d.fill(column.getRect());
				}
				else if(column.isJump()){
					color = Color.green;
					g2d.setColor(color);
					g2d.fill(column.getRect());
				}else if(column.type == CellType.CLOSED) {
					color = Color.pink;
					g2d.setColor(color);
					g2d.fill(column.getRect());
				}
				else if(column.type == CellType.TARGET) {
					color = Color.red;
					g2d.setColor(color);
					g2d.fill(column.getRect());
				}
				else { 
					g2d.setColor(color);
					g2d.draw(column.getRect());	
				}
			}
		
			g2d.setColor(Color.BLUE);
			g2d.fill(aStar.srcCell.getRect());
			g2d.setColor(Color.red);
			g2d.fill(aStar.targetCell.getRect());
	}
	public void paint(Graphics2D  g2d,Shape s) {
		g2d.drawRect(10, 10, 10, 10);
	}
	}
	/**
	 * Create the frame.
	 */
	public AStarFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 1800, 1024);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
//		this.getContentPane().setBounds(100, 100, 1024, 1024);
		getContentPane().setLayout(null);
		contentPane.setLayout(null);
		JPanel jp=new MyPanel();
		jp.setBorder(new EmptyBorder(5, 5, 5, 5));
		JScrollPane sp=new JScrollPane( jp);
		sp.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				JOptionPane.showMessageDialog(AStarFrame.this, "ÓÒ»÷Ñ¡Ôñ");
			}
		});
		sp.setBounds(10, 10, 1700, 970);
		this.getContentPane().add(sp);
		
		JButton btnNewButton = new JButton("Ñ°Â·");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isRuned)
				{
					for (RectCell cell : AStarFrame.this.aStar.getCells().values())
					{
						cell.resest();
					}
					for (Entry<Integer,Integer> entry : AStarFrame.this.aStar.getValues().entrySet())
					{
						AStarFrame.this.aStar.getValues().put(entry.getKey(), Integer.MAX_VALUE);
					}
					repaint();
				}
				new Thread(new MyRunnable(AStarFrame.this)).start();
				AStarFrame.this.isRuned = true;
			}
		});
		
		btnNewButton.setBounds(1720, 45, 60, 42);
		contentPane.add(btnNewButton);

		jp.setPreferredSize(new Dimension(900 * RectCell.BASIC,900 * RectCell.BASIC));
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void tryTo() {
		AStarResult result = aStar.tryToFindAWay();
		repaint();
		JOptionPane.showMessageDialog(this, result.toString());
	}
	public boolean isNeedOnceRepaint() {
		return isNeedOnceRepaint;
	}
	public void setNeedOnceRepaint(boolean isNeedOnceRepaint) {
		this.isNeedOnceRepaint = isNeedOnceRepaint;
	}
}
