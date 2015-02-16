
public class CppnEdge {
	CppnNode from;
	CppnNode to;
	double edgeWeight;
	
	public CppnEdge(CppnNode from, CppnNode to, double edgeWeight) {
		this.from = from;
		this.to = to;
		this.edgeWeight = edgeWeight;
		
		this.from.output.add(this);
		this.to.input.add(this);
	}
	
	public void propagateValue(){
		to.propagateValue(from.outValue * edgeWeight);
	}
	
	public void propagateValue(double value){
		to.propagateValue(value * edgeWeight);
	}
}
