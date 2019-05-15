package com.SubwayGraph.jackson;

public class Line {
	private String[][] arrets;
	private String name;
	private String num;
	private String color;
	private String type;
	private double[][] labels;
	private boolean show;
	
	
	public String[][] getArrets() { return arrets; }
	public void setArrets(String[][] arrets) { this.arrets = arrets; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getNum() { return num; }
	public void setNum(String num) { this.num = num; }
	
	public String getColor() { return color; }
	public void setColor(String color) { this.color = color; }
	
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	
	public double[][] getLabels() { return labels; }
	public void setLabels(double[][] labels) { this.labels = labels; }
	
	public boolean getShow() { return show; }
	public void setShow(boolean show) { this.show = show; }
}
