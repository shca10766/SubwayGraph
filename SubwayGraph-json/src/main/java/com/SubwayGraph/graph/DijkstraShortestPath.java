package com.SubwayGraph.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.graph.DefaultEdge;

import com.SubwayGraph.jackson.Station;
import com.SubwayGraph.jackson.Line;
import com.SubwayGraph.jackson.Routes;
import com.SubwayGraph.jackson.Subway;

public class DijkstraShortestPath {
	private boolean[] marked;
	private int[] previous;
	private double[] distance;
	
	private MapBuilder mapBuilder;
	private String idStart;
	private String idEnd;
	private List<String> out;
	
	private Subway subway;
	private List<DefaultEdge>edges;
	private List<String>vertices;
	
	public DijkstraShortestPath(MapBuilder mapBuilder, String id_stationStart, String id_stationEnd, Subway s) {
		this.subway = s;
		this.idStart = id_stationStart;
		this.idEnd = id_stationEnd;
		this.mapBuilder = mapBuilder;
		this.edges = new ArrayList<>(mapBuilder.getSubwayGraph().edgeSet());
		this.vertices = new ArrayList<>(mapBuilder.getSubwayGraph().vertexSet());
		
		DijkstraSP(id_stationStart);
	}
	
	public double getDistance(String idStation1, String idStation2) {		
		Station station1 = this.mapBuilder.getStationWithId(idStation1);
		Station station2 = this.mapBuilder.getStationWithId(idStation2);
		
		double x = station2.getLng() - station1.getLng();
		double y = station2.getLat() - station1.getLat();
		
		double res =  Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));		
		return res;
	}
	
	public void DijkstraSP(String id_stationStart) {
		int n = this.vertices.size();
		
		this.marked = new boolean[n];
		this.previous = new int[n];
		this.distance = new double[n];
		
		for (int i = 0; i < this.previous.length; i++) {
			this.previous[i] = -1;
			this.marked[i] = false;
			this.distance[i] = Float.POSITIVE_INFINITY;
		}
		
		this.out = dijkstra();
	}
	
	public List<String> dijkstra() {
		this.out = new LinkedList<>();
		
		String v = this.idStart;
		this.distance[vertices.indexOf(v)] = 0;
		this.marked[vertices.indexOf(v)] = true;
		out.add(v);
		
		List<String> list =  getNeighbors(v);
		
		dijkstra(out, list);
		
		return out;
	}
	
	public List<String> getNeighbors(String stationId){
		List<String> res = new ArrayList<>();
		res.add(stationId);
		
		for (int j = 1; j < edges.size(); j++) {
			String source = mapBuilder.getSubwayGraph().getEdgeSource(edges.get(j));
			String target = mapBuilder.getSubwayGraph().getEdgeTarget(edges.get(j));
			
			if(source.equals(stationId)) {
				res.add(target);
			}
			else if(target.equals(stationId)) {
				res.add(source);
			}
		}
		return res;		
	}
	
	public void dijkstra(List<String> out, List<String> edgeNeighbors) {
		
		for (int i = 1; i < edgeNeighbors.size(); i++) {
			String source = edgeNeighbors.get(0);
			String target = edgeNeighbors.get(i);
			double c = getDistance(source, target) + this.distance[vertices.indexOf(source)];
			if (this.distance[vertices.indexOf(target)] > c) {
				this.distance[vertices.indexOf(target)] = c;
				this.previous[vertices.indexOf(target)] = vertices.indexOf(source);
			}
		}
		
		double min = Float.POSITIVE_INFINITY;
		int indexMin = -1;
		
		for (int j = 0; j < this.distance.length; j++) {
			if (this.marked[j] == false && min > this.distance[j]) {
				min = this.distance[j];
				indexMin = j;
			}
		}
		
		if (indexMin != -1) {
			this.marked[indexMin] = true;
			out.add(vertices.get(indexMin));
			
			List<String> list = getNeighbors(vertices.get(indexMin));
			List<String> neighbors = new ArrayList<String>();
			neighbors.add(vertices.get(indexMin));
			
			for (int k = 1; k < list.size(); k++) {
				String target2 = list.get(k);
				if (this.marked[vertices.indexOf(target2)] == false) {
					neighbors.add(target2);
				}
			}

			dijkstra(out, neighbors);
		}
	}
	
	public ArrayList<Itinerary> getItinerary() {
		List<String> bePath = new ArrayList<String>();
		ArrayList<Itinerary> itineraries = new ArrayList<Itinerary>();
		if (hasPathTo(idEnd)) {
			bePath.add(idEnd);
			int i = vertices.indexOf(idEnd);
			
			while (this.previous[i] != -1) {
				bePath.add(vertices.get(this.previous[i]));
				i = this.previous[i];
			}
			
			String stationStart = mapBuilder.getStationWithId(bePath.get(bePath.size()-1)).getNom(); 
			String lineStart = mapBuilder.getRouteByStations(bePath.get(bePath.size()-1), bePath.get(bePath.size()-2)).getLigne();
			String directionStart = mapBuilder.getRouteByStations(bePath.get(bePath.size()-1), bePath.get(bePath.size()-2)).getDirection();
			String typeStart = mapBuilder.getRouteByStations(bePath.get(bePath.size()-1), bePath.get(bePath.size()-2)).getType();
			
			for (int j = (bePath.size() - 2); j >= 0; j--) {
				String direction = mapBuilder.getRouteByStations(bePath.get(j+1), bePath.get(j)).getDirection();
				String line = mapBuilder.getRouteByStations(bePath.get(j+1), bePath.get(j)).getLigne();
				String type = mapBuilder.getRouteByStations(bePath.get(j+1), bePath.get(j)).getType();
				
				if (!type.equals("corresp")) {
					if (!lineStart.equals(line)) {
						Itinerary itinerary = new Itinerary(typeStart, lineStart, stationStart, mapBuilder.getStationWithId(bePath.get(j+1)).getNom(), directionStart);
						itineraries.add(itinerary);
						
						stationStart = mapBuilder.getStationWithId(bePath.get(j+1)).getNom();
						lineStart = line;
						directionStart = direction;
						typeStart = type;
					}
				}
				if (bePath.get(j).equals(idEnd)) {
					Itinerary itinerary = new Itinerary(typeStart, lineStart, stationStart, mapBuilder.getStationWithId(bePath.get(j)).getNom(), directionStart);
					itineraries.add(itinerary);
					return itineraries;
				}
			}
		}	
		return itineraries;
	}
	
	public boolean hasPathTo(String v) {
		if (this.out.indexOf(v) != -1) {
			return true;
		}
		return false;
	}
}
