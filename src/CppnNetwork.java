import java.util.ArrayList;
import java.util.LinkedList;


public class CppnNetwork {
	ArrayList<CppnNode> inputs = new ArrayList<CppnNode>();
	ArrayList<CppnNode> outputs = new ArrayList<CppnNode>();
	
	LinkedList<CppnNode> innerNodes = new LinkedList<CppnNode>();
	
	LinkedList<CppnEdge> edges = new LinkedList<CppnEdge>();
	
	public double[] calculate(double[] inValues)
	{
		//Setup Network
		resetNodes();
		
		for (int i = 0; i < inputs.size(); i++)
			inputs.get(i).inValue = inValues[i];
		
		
		//Do Calculations
		inputs.stream().forEach(a -> a.propagateInValue());
		
		
		//Collect Results
		double [] outValues = new double[outputs.size()];
		
		for (int i = 0; i < outputs.size(); i++)
			outValues[i] = outputs.get(i).outValue;
		
		return outValues;
	}
	
	private void resetNodes()
	{
		inputs.parallelStream().forEach(a -> a.reset());
		outputs.parallelStream().forEach(a -> a.reset());
		innerNodes.parallelStream().forEach(a -> a.reset());
	}
}
