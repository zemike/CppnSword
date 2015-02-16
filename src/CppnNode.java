import java.util.LinkedList;


public class CppnNode {
	LinkedList<CppnEdge> input = new LinkedList<CppnEdge>();
	LinkedList<CppnEdge> output = new LinkedList<CppnEdge>();
	double outValue = 0;
	double inValue = 0;
	enum functionTypes {sine, gauss, none};
	functionTypes function;
	
	public void propagateValue(double inValue) {
		double functionResult = applyFunction(inValue);
		outValue += functionResult; //if the function has multiple inputs, they are just summed up.
		
		output.stream().forEach(a -> a.propagateValue(functionResult));
	}
	
	public CppnNode(functionTypes function) {
		super();
		this.function = function;
	}

	public void propagateInValue(){
		propagateValue(inValue);
	}
	
	public void reset(){
		outValue = 0;
		inValue = 0;
	}
	
	private double applyFunction(double input){
		double output = input;
		
		switch (function)
		{
		case sine:
			output = Math.sin(input);
			break;
			
		case gauss:
			output = Util.gauss(input);
			break;
			
		case none:
		}
		
		
		return output;
	}
}
