package com.SubwayGraph.jackson;

public class Edge {
	
	private String firstNode;
	private String secondNode;
	
	public Edge(String firstNode, String secondNode) {
		super();
		this.firstNode = firstNode;
		this.secondNode = secondNode;
	}
	
	public String getFirstNode() {
		return firstNode;
	}
	public void setFirstNode(String firstNode) {
		this.firstNode = firstNode;
	}
	public String getSecondNode() {
		return secondNode;
	}
	public void setSecondNode(String secondNode) {
		this.secondNode = secondNode;
	}

	@Override
	public String toString() {
		return "Edge [firstNode=" + firstNode + ", secondNode=" + secondNode + "]";
	}
}
