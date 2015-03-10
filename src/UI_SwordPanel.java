import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;


public class UI_SwordPanel extends JPanel {

	public int index;
	
	private CppnNetwork network = null;
	private int [] widths = null;
	private static final long serialVersionUID = 4219984920544280853L;
	
	
	public UI_SwordPanel(CppnNetwork network, int x, int y, int index)
	{
		this.network = network;
		setPreferredSize(new Dimension(x,y));
		this.index = index;
		this.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e){
				UI_Main.panelSelected((UI_SwordPanel) e.getComponent());
				//System.out.println("Mouse pressed on " + e.getComponent());
				//e.consume();
			}
			
		});
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		inquireNetwork();
		
		Graphics2D g2d = (Graphics2D) g;
		
		g.setColor(Color.black);
		
		for (int y = 0; y<this.getHeight() - 1; y++)
		{
			g2d.drawLine(this.getWidth()/2 + widths[y], y, this.getWidth()/2 + widths[y+1], y+1);
			g2d.drawLine(this.getWidth()/2 - widths[y], y, this.getWidth()/2 - widths[y+1], y+1);
		}
	}
	
	public void inquireNetwork()
	{
		double maxWidth = 0;
		double [] doubleWidths = new double[this.getHeight()];
		
		double [] networkInput = {0,1};
		
		//Getting the widths from the network
		for (double p = 0; p < 1; p = Math.min(p+1.0/this.getHeight(), 1))
		{
			networkInput[0] = p;
			double newWidth = network.calculate(networkInput)[0];
			doubleWidths[(int)(this.getHeight() * p)] = newWidth;
			if (newWidth > maxWidth)
				maxWidth = newWidth;
		}
		
		//Normalizing if the input is too big...
		if (maxWidth > this.getWidth()/2)
			for (int i = 0; i < doubleWidths.length; i++)
				doubleWidths[i] *= this.getWidth()/2 / maxWidth;
		
		//..and if it is too small.
		else if (maxWidth < this.getWidth()/5)
			for (int i = 0; i < doubleWidths.length; i++)
				doubleWidths[i] *= this.getWidth()/5 / maxWidth;
		
		widths = new int[doubleWidths.length];
		for (int i = 0; i < widths.length; i++)
			widths[i] = (int) doubleWidths[i];
	}
}
