package com.SubwayGraph.jackson;

public class Itinerary {
	private String type;
	private String line;
	private String station1;
	private String station2;
	private String direction;
	
	public Itinerary(String typeI, String lineI, String station1_I, String station2_I, String directionI) {
		type = typeI;
		line = lineI;
		station1 = station1_I;
		station2 = station2_I;
		direction = directionI;
	}

	public String getType() { return type; }
	public void setType(String type) { this.type = type; }

	public String getLine() { return line; }
	public void setLine(String line) { this.line = line; }

	public String getStation1() { return station1; }
	public void setStation1(String station1) { this.station1 = station1; }

	public String getStation2() { return station2; }
	public void setStation2(String station2) { this.station2 = station2; }

	public String getDirection() { return direction; }
	public void setDirection(String direction) { this.direction = direction; }
}
