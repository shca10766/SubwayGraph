package com.SubwayGraph.graph;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.DefaultEdge;

import com.SubwayGraph.jackson.Station;

public class BFSShortestPath {
	
	private MapBuilder mapBuilder;
	private List<DefaultEdge>edges;
	private List<String>vertices;
	
	public BFSShortestPath(MapBuilder mapBuilder) {
		super();
		this.mapBuilder = mapBuilder;
		this.edges = new ArrayList<>(mapBuilder.getSubwayGraph().edgeSet());
		this.vertices = new ArrayList<>(mapBuilder.getSubwayGraph().vertexSet());
	}

	public List<DefaultEdge> getEdges() {
		return new ArrayList<>(edges);
	}

	public List<String> getVertices() {
		return new ArrayList<>(vertices);
	}
	
	//Get Neighbors of a vertex
	public List<String>getNeighbors(String stationId){
		List<String>res = new ArrayList<>();
		
		for(DefaultEdge e: getEdges()) {
			String source = mapBuilder.getSubwayGraph().getEdgeSource(e);
			String target = mapBuilder.getSubwayGraph().getEdgeTarget(e);
			
			if(source.equals(stationId)) {
				res.add(target);
			}else if(target.equals(stationId)) {
				res.add(source);
			}
		}
		return res;		
	}
	
	
	
	
	public List<String> bfsRec(String source, String target){
		return bfs(source, target, new ArrayList<>());
	}
	
	
	
	public List<String> bfs(String source, String target, ArrayList<Object> arrayList) {
		return null;
	}
	
	
	
	public void printBFS (List<String>stationsBfs) {
		
		for(String s: stationsBfs ) {
			Station station = mapBuilder.getStationWithId(s);
			System.out.println("Station Name: "+station.getNom()+", id: "+station.getNum());
			
			
		}
		
		
		
	}
	
	
	
}
