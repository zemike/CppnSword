
public class TestSimple {

	public static void main (String args[])
	{
		CppnNetwork myNet = new CppnNetwork();
		
		CppnNode toIn = new CppnNode(CppnNode.functionTypes.gauss);		
		CppnNode toOut = new CppnNode (CppnNode.functionTypes.none);
		CppnEdge connection = new CppnEdge(toIn, toOut, 2);
		
		myNet.inputs.add(toIn);
		myNet.outputs.add(toOut);
		myNet.edges.add(connection);
		
		double [] in = {1};
		
		double [] out = myNet.calculate(in);
		System.out.println(out[0]);
	}
}
