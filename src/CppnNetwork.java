import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class CppnNetwork {
	ArrayList<CppnNode> inputs = new ArrayList<CppnNode>();
	ArrayList<CppnNode> outputs = new ArrayList<CppnNode>();

	LinkedList<CppnNode> innerNodes = new LinkedList<CppnNode>();

	LinkedList<CppnEdge> edges = new LinkedList<CppnEdge>();

	public int generation = 1;

	private final boolean optionRemove = false;
	
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

	public void mutate()
	{
		generation++;

		//Add a node
		CppnNode newNode = new CppnNode();
		innerNodes.add(newNode);

		//Add edges //TODO: create a new edge if this one is fake
		//for (int i = 0; i < 1; i++)
		while (Math.random() < 0.2)
		{
			CppnNode from = getRandomNodesFromLists(inputs, innerNodes); //The non-sinks
			CppnNode to = getRandomNodesFromLists(innerNodes, outputs); //Non-sources
			CppnEdge toAdd = new CppnEdge(from, to, 0.5 + Math.random());
			edges.add(toAdd);

			//Check for loop on the new edge - assuming that there were no loops before, the loop can come up only starting a new edge
			if (hasCycles(toAdd))
			{
				edges.remove(toAdd);
				toAdd.detachFromParents();
			}
		}

		if (optionRemove) 
			while (Math.random() < 0.01)
			{
				CppnEdge toRemove = edges.remove((int)(Math.random() * edges.size()));
				toRemove.detachFromParents();
			}

		//Change weights (according to Gaussian?)
		for (CppnEdge e : edges)
		{
			if (Math.random() < 0.5)
				e.edgeWeight *= 1 + Util.gauss(Math.random()); //TODO: Test out values.
		}

		//Remove the new node if it is not used
		if (newNode.input.size() + newNode.output.size() == 0)
			innerNodes.remove(newNode);
	}

	private CppnNode getRandomNodesFromLists(List<CppnNode> a, List<CppnNode> b)
	{
		int totalSize = a.size() + b.size();
		int target = (int) (Math.random() * totalSize);

		if (target < a.size())
			return a.get(target);
		else
			return b.get(target - a.size());
	}

	private boolean hasCycles(CppnEdge newEdge)
	{
		CppnNode lookingFor = newEdge.from;
		LinkedList<CppnNode> fronter = new LinkedList<CppnNode>();
		fronter.add(newEdge.to);

		while (!fronter.isEmpty())
		{
			CppnNode currentNode = fronter.removeFirst();
			for (CppnEdge currentEdge : currentNode.output)
				if (currentEdge.to == lookingFor)
					return true;
				else
					fronter.add(currentEdge.to);
		}

		return false;
	}

	public CppnNetwork duplicate(){
		CppnNetwork toReturn = new CppnNetwork();

		toReturn.generation = generation;

		HashMap<CppnNode, CppnNode> froms = new HashMap<CppnNode, CppnNode>();
		HashMap<CppnNode, CppnNode> tos = new HashMap<CppnNode, CppnNode>();

		for (CppnNode currentNode : inputs)
		{
			CppnNode newNode = currentNode.duplicate();
			froms.put(currentNode, newNode);
			toReturn.inputs.add(newNode);
		}

		for (CppnNode currentNode : innerNodes)
		{
			CppnNode newNode = currentNode.duplicate();
			froms.put(currentNode, newNode);
			tos.put(currentNode, newNode);
			toReturn.innerNodes.add(newNode);
		}

		for (CppnNode currentNode : outputs)
		{
			CppnNode newNode = currentNode.duplicate();
			tos.put(currentNode, newNode);
			toReturn.outputs.add(newNode);
		}

		for (CppnEdge e : edges)
		{
			//Debug
			//			System.out.println("Creating an edge like " + e + "(" + e.from + "->" + e.to + "), "
			//					+ "but from " + froms.get(e.from) + " and to " + tos.get(e.to) + ".");
			//			System.out.println("-Map, is the from there? -" + froms.containsKey(froms.get(e.from)));

			toReturn.edges.add(new CppnEdge(froms.get(e.from), tos.get(e.to), e.edgeWeight));
		}

		return toReturn;
	}
}
