import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;


public class UI_Main {

	public static int nPanels = 8;
	public static UI_SwordPanel[] swordPanels = null;

	private static int xSize = 150, ySize = 600;
	
	private static JFrame myFrame;

	public static void main(String args[])
	{
		myFrame = new JFrame("CppSword");
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setLayout(new FlowLayout());
		myFrame.setPreferredSize(new Dimension(xSize * 8 + 100, ySize * 1 + 100));

		//Initialize the panels
		swordPanels = new UI_SwordPanel[nPanels];

		for (int i = 0; i < nPanels; i++)
		{
			CppnNetwork currentNetwork = makeSwordNetwork();
			UI_SwordPanel currentPanel = new UI_SwordPanel(currentNetwork, 150, 600, i);
			currentPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
			swordPanels[i] = currentPanel;
			myFrame.add(currentPanel);
		}		

		myFrame.pack();
		myFrame.setVisible(true);
	}

	private static CppnNetwork makeSwordNetwork() {
		CppnNetwork sNet = new CppnNetwork();

		//		CppnNode toIn = new CppnNode(CppnNode.functionTypes.none);		
		//		CppnNode toOut = new CppnNode (CppnNode.functionTypes.none);
		//		CppnEdge connection = new CppnEdge(toIn, toOut, 2);
		//		
		//		toReturn.inputs.add(toIn);
		//		toReturn.outputs.add(toOut);
		//		toReturn.edges.add(connection);

		CppnNode p = new CppnNode (CppnNode.functionTypes.none);
		CppnNode bias = new CppnNode (CppnNode.functionTypes.none);
		sNet.inputs.add(p);
		sNet.inputs.add(bias);

		CppnNode internal = new CppnNode();
		sNet.innerNodes.add(internal);

		CppnNode width = new CppnNode (CppnNode.functionTypes.none);
		sNet.outputs.add(width);

		CppnEdge connectionA = new CppnEdge(p, internal, 1);
		CppnEdge connectionB = new CppnEdge(internal, width, 1);
		sNet.edges.add(connectionA);
		sNet.edges.add(connectionB);

		return sNet;
	}

	public static void panelSelected(UI_SwordPanel selection){
		
		myFrame.setTitle("CppSword, gen " + (selection.network.generation + 1));
		
		for (int i = 0; i < swordPanels.length; i++)
		{
			myFrame.remove(swordPanels[i]);
		}
		
		swordPanels = new UI_SwordPanel[nPanels];

		for (int i = 0; i < nPanels; i++)
		{
			if (i == selection.index)
			{
				swordPanels[i] = selection;
				myFrame.add(selection);
				selection.setBorder(BorderFactory.createLineBorder(Color.red));
			}
			else
			{
				//CppnNetwork currentNetwork = makeSwordNetwork();
				CppnNetwork currentNetwork = selection.network.duplicate();
				currentNetwork.mutate();
				UI_SwordPanel currentPanel = new UI_SwordPanel(currentNetwork, xSize, ySize, i);
				currentPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
				swordPanels[i] = currentPanel;
				myFrame.add(currentPanel);
			}

		}		
		
		myFrame.repaint();
		myFrame.revalidate();
	}
}
