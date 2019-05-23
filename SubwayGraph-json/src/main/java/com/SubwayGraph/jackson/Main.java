package com.SubwayGraph.jackson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Main  <V extends Comparable<V>> {
	

	public static void main(String[] args) {
		Subway subway = init();
		
		System.out.println(subway.getLines().get(7).getListChemin());
		
		
	}
	
	public static Subway init() {
		ArrayList<Station> stations = new ArrayList<Station>();
        ArrayList<Routes> routes = new ArrayList<Routes>();	
        ArrayList<Line> lines = new ArrayList<Line>();
        ArrayList<ArrayList<String>> correspondences = new ArrayList<ArrayList<String>>();
        
		try {
	        ObjectMapper mapper = new ObjectMapper();
	        // use the ObjectMapper to read the json string and create a tree
	        JsonNode node = mapper.readTree(new File("subway.json"));
	        Iterator<String> fieldNames = node.fieldNames();
	        while (fieldNames.hasNext()) {
	        	     	
	            JsonNode stationNode = node.get("stations");
	            Iterator<JsonNode> stationElements = stationNode.iterator();
	            while (stationElements.hasNext()) {
	                JsonNode element = stationElements.next();
	                JsonParser parser = mapper.treeAsTokens(element);
	                Station station = mapper.readValue(parser, Station.class);
	                stations.add(station);
	            }
	             
	            JsonNode routeNode = node.get("routes");
	            Iterator<JsonNode> routeElements = routeNode.iterator();
	            while (routeElements.hasNext()) {
	                JsonNode element = routeElements.next();
	                JsonParser parser = mapper.treeAsTokens(element);
	                Routes route = mapper.readValue(parser, Routes.class);
	                routes.add(route);
	            }
	            	 
	            JsonNode lineNode = node.get("lignes");
	            Iterator<JsonNode> lineElements = lineNode.iterator();
	            while (lineElements.hasNext()) {
	                JsonNode element = lineElements.next();	                
	                JsonParser parser = mapper.treeAsTokens(element);
	                Line line = mapper.readValue(parser, Line.class);
	                lines.add(line);
	                line.convertArret();
	            }
	            
	            JsonNode correspNode = node.get("corresp");
	            Iterator<JsonNode> corresp = correspNode.iterator();
	            while (corresp.hasNext()) {
	            	ArrayList<String> correspondence = new ArrayList<String>();
	            	Iterator<JsonNode> stationsElements = corresp.next().iterator();
	            	while(stationsElements.hasNext()) {
	            		correspondence.add(stationsElements.next().textValue());
	            	}
	            	correspondences.add(correspondence);
	            }
	            
	            fieldNames.next();
	        }
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
		
		Subway subway = new Subway(correspondences, stations, routes, lines);
		return subway;
	}

}
