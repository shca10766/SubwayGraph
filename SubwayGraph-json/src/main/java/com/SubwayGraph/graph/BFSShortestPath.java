package com.SubwayGraph.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.antlr.v4.runtime.tree.Tree;
import org.apache.commons.lang3.mutable.Mutable;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

import com.SubwayGraph.jackson.Station;
import com.SubwayGraph.jackson.Subway;
import com.fasterxml.jackson.core.TreeNode;


public class BFSShortestPath {

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

	public BFSShortestPath(MapBuilder mapBuilder, String id_stationStart, String id_stationEnd, Subway s) {
		super();
		this.subway = s;
		this.idStart = id_stationStart;
		this.idEnd = id_stationEnd;
		this.mapBuilder = mapBuilder;
		this.edges = new ArrayList<>(mapBuilder.getSubwayGraph().edgeSet());
		this.vertices = new ArrayList<>(mapBuilder.getSubwayGraph().vertexSet());
		
		BFSSP();
	}
	
	public void BFSSP() {
		int n = this.vertices.size();
		
		this.marked = new boolean[n];
		this.previous = new int[n];
		this.distance = new double[n];
		
		for (int i = 0; i < this.previous.length; i++) {
			this.previous[i] = -1;
			this.marked[i] = false;
		}
		
		this.out = bfs();
	}
	
	public List<String> bfs() {
		this.out = new LinkedList<>();
		bfs(this.vertices, this.out);
		return out;
	}
	
	private void bfs(List<String> vertices, List<String> out) {
		
		String v = this.idStart;
		this.distance[vertices.indexOf(v)] = 0;
		Queue<String> q = new LinkedList<>();
		q.add(this.idStart);
		
		do {
			//ToDo
			v = q.poll();
			this.marked[vertices.indexOf(v)] = true;
            out.add(v);
            
            Iterator<String> i = getNeighbors(v).listIterator(); 
            while (i.hasNext()) 
            { 
                String u = i.next();
                if (!this.marked[vertices.indexOf(u)]) {
                	
                	this.distance[vertices.indexOf(u)] = this.distance[vertices.indexOf(v)] + 1;
                	this.previous[vertices.indexOf(u)] = vertices.indexOf(v);
                	this.marked[vertices.indexOf(u)] = true;
                	
                	q.add(u);
                }
            } 
			
		} while(q.size() > 0);
	}
	
	public boolean hasPathTo(String v) {
		if (this.out.indexOf(v) != -1) {
			return true;
		}
		return false;
	}

	// Get Neighbors of a vertex
	public List<String> getNeighbors(String stationId) {
		List<String> res = new ArrayList<>();
		res.add(stationId);
		
		for (int j = 1; j < edges.size(); j++) {
			String source = mapBuilder.getSubwayGraph().getEdgeSource(edges.get(j));
			String target = mapBuilder.getSubwayGraph().getEdgeTarget(edges.get(j));
			
			if(source.equals(stationId)) {
				res.add(target);
			}
		}
		return res;	
	}
	
	public ArrayList<Itinerary> getItinerary() {
		List<String> l = this.vertices;
		List<String> bePath = new ArrayList<String>();
		ArrayList<Itinerary> itineraries = new ArrayList<Itinerary>();
		if (hasPathTo(this.idEnd)) {
			bePath.add(this.idEnd);
			int i = l.indexOf(this.idEnd);
			
			while (this.previous[i] != -1) {
				bePath.add(l.get(this.previous[i]));
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
}
