package com.SubwayGraph.jackson;

import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

public class Routes {
	private String[] arrets;
	private String direction;
	private String type;
	private String ligne;
	private JsonNode intersections;
	
	
	public String[] getArrets() { return arrets; }
	public void setArrets(String[] arrets) { this.arrets = arrets; }
	
	public String getDirection() { return direction; }
	public void setDirection(String direction) { this.direction = direction; }
	
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	
	public String getLigne() { return ligne; }
	public void setLigne(String ligne) { this.ligne = ligne; }
	
	public JsonNode getIntersections() { return intersections; }
	public void setIntersections(JsonNode intersections) { this.intersections = intersections; }
	
	public ArrayList<String> getLineIntersection() {
		ArrayList<String> lines = new ArrayList<String>();
		
		JsonNode all = intersections.get("all");
		Iterator<JsonNode> fields = all.iterator();
        while (fields.hasNext()) {
            lines.add(fields.next().toString());
        }
		
		return lines;
	}
	
	public ArrayList<String> getStationIntersection(String line) {
		ArrayList<String> lines = getLineIntersection();
		ArrayList<String> stations = new ArrayList<String>();
		
		if (lines.contains(line)) {
			JsonNode lineNode = intersections.get(line);
			Iterator<JsonNode> elements = lineNode.iterator();
	        while (elements.hasNext()) {
	            stations.add(elements.next().toString());
	        }
		}
		
		return stations;
	}
}
