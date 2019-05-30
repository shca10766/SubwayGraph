package com.SubwayGraph.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.PriorityQueue;
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
import com.fasterxml.jackson.core.TreeNode;


public class BFSShortestPath {

	private MapBuilder mapBuilder;
	private List<DefaultEdge> edges;
	private List<String> vertices;

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

	// Get Neighbors of a vertex
	public List<String> getNeighbors(String stationId) {
		List<String> res = new ArrayList<>();

		for (DefaultEdge e : getEdges()) {
			String source = mapBuilder.getSubwayGraph().getEdgeSource(e);
			String target = mapBuilder.getSubwayGraph().getEdgeTarget(e);

			if (source.equals(stationId)) {
				if (!res.contains(target))
					res.add(target);
			} else if (target.equals(stationId)) {
				if (!res.contains(source))
					res.add(source);
			}
		}
		return res;
	}

	public List<String> neigborsOfNeighbors(String source) {
		List<String> res = new ArrayList<>();

		for (String neigh : getNeighbors(source)) {
			for (String subneigh : getNeighbors(neigh)) {
				if (!res.contains(subneigh))
					res.add(subneigh);
			}
		}

		System.out.println("neighbors of neighbors: " + res);

		return res;
	}

	public List<String> bfs(String sourceStation, String targetStation) {

		PriorityQueue<String> queue = new PriorityQueue<>();
		queue.add(sourceStation);

		List<String> visited = new ArrayList<>();
		visited.add(sourceStation);

		List<String> bfs = new ArrayList<>();
		bfs.add(sourceStation);

		while (!queue.isEmpty()) {
			String element = queue.poll();
			
			ComparatorStations cs = new ComparatorStations();
			cs.setMapBuilder(mapBuilder);
			cs.setParent(targetStation);

			TreeSet treeSet = new TreeSet(cs);
			List<String> neighbors = getNeighbors(element);
			neighbors.removeAll(visited);
			treeSet.addAll(neighbors);
			//neighbors.sort(cs);
			List<String>aux = new ArrayList<>(treeSet);
			
			for(String neighbor:aux) {
				String name = mapBuilder.getStationWithId(neighbor).getNom();
				System.out.println("neighbor: "+name);
			}
			
			System.out.println();
			
			String nh = "";
			for (String neighbor : neighbors) {
				queue.add(neighbor);
				visited.add(neighbor);
				bfs.add(neighbor);

				if (neighbor.equals(targetStation)) {
					nh = neighbor;
					break;
				}
			}

			if (nh.equals(targetStation)) {

				break;
			}
		}

		return bfs;

	}

	public void printBFS(List<String> stationsBfs) {

		System.err.println("Still needs to be improved: THIS IS THE BFS until the target is reached, not the route yet: ");
		for (String s : stationsBfs) {
			Station station = mapBuilder.getStationWithId(s);
			System.out.println("Station Name: " + station.getNom() + ", id: " + station.getNum());

		}

	}

	/*
	 * public int preferenceSameLine(String stationId1, String stationId2, String
	 * parent) {
	 * 
	 * if(mapBuilder.stationsSameLine(parent, stationId1) &&
	 * !mapBuilder.stationsSameLine(parent, stationId2) ) { return 1; }else if
	 * (!mapBuilder.stationsSameLine(parent, stationId1) &&
	 * mapBuilder.stationsSameLine(parent, stationId2) ){ return -1; }
	 * 
	 * return 0;
	 * 
	 * }
	 */

}

class ComparatorStations implements Comparator<String> {

	private String parent;
	private MapBuilder mapBuilder;
	
	

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}


	public MapBuilder getMapBuilder() {
		return mapBuilder;
	}

	public void setMapBuilder(MapBuilder mapBuilder) {
		this.mapBuilder = mapBuilder;
	}




	@Override
	public int compare(String stationId1, String stationId2) {

		
		Integer distToTargetst1 = mapBuilder.getRouteDistanceIncludingBothStations(stationId1, parent);
		Integer distTotargetst2 = mapBuilder.getRouteDistanceIncludingBothStations(stationId2, parent);

		if(distToTargetst1 !=0 && distTotargetst2==0) {
			return 1;
		}
		
		else if(distToTargetst1==0 && distTotargetst2!=0){
			return -1;
		}
		
		
		else if (distToTargetst1 != 0 && distTotargetst2 != 0) {
			if (distToTargetst1 > distTotargetst2) {
				System.out.println("dist1: " + distToTargetst1);
				System.out.println("dist2: " + distTotargetst2);
				System.out.println(mapBuilder.getStationWithId(stationId1).getNom() + " closer to "
						+ mapBuilder.getStationWithId(parent).getNom() +"than"+mapBuilder.getStationWithId(stationId2).getNom());
				return 1;

			} else if (distToTargetst1 < distTotargetst2) {
				System.out.println(mapBuilder.getStationWithId(stationId2).getNom() + " closer to "
						+ mapBuilder.getStationWithId(parent).getNom()+" than "+mapBuilder.getStationWithId(stationId1).getNom());
				return -1;
			} else {
					System.out.println("Both stations at the same distance: "+mapBuilder.getStationWithId(stationId1).getNom()+" , "+mapBuilder.getStationWithId(stationId2).getNom()
							+"to "+mapBuilder.getStationWithId(parent).getNom());
					
					if(mapBuilder.stationsSameLine(stationId1, parent) && !mapBuilder.stationsSameLine(stationId2, parent)) {
						
						System.out.println("same distance, but same line for station "+mapBuilder.getStationWithId(stationId1).getNom());
						return 1;
					}else if(!mapBuilder.stationsSameLine(stationId1, parent) && mapBuilder.stationsSameLine(stationId2, parent)) {
							System.out.println("same distance, but same line for station "+mapBuilder.getStationWithId(stationId2).getNom());
							return -1;
					}else if(mapBuilder.stationsSameLine(stationId1, parent) && mapBuilder.stationsSameLine(stationId2, parent)) {
						System.out.println("Both in the same line");
					}else {
						System.out.println("None in the line of the target");
					}
					
					
					
					return 0;
				}

			
		}

		// System.out.println("lines " +
		// mapBuilder.getStationWithId(stationId1).getNom() + ": "
		// + mapBuilder.getLinesAsociatedWithStation(stationId1));
		// System.out.println("lines " +
		// mapBuilder.getStationWithId(stationId2).getNom() + ": "
		// + mapBuilder.getLinesAsociatedWithStation(stationId2));
		// System.out.println("Parent: lines " +
		// mapBuilder.getStationWithId(parent).getNom() + ": "
		// + mapBuilder.getLinesAsociatedWithStation(parent));
		// System.out.println();
		//
		// if (mapBuilder.stationsSameLine(parent, stationId1) &&
		// !mapBuilder.stationsSameLine(parent, stationId2)) {
		// System.out.println("Station " +
		// mapBuilder.getStationWithId(stationId1).getNom() + " and "
		// + mapBuilder.getStationWithId(parent).getNom() + " in the same line");
		// System.out.println();
		// res=1;
		//
		// } else if (!mapBuilder.stationsSameLine(parent, stationId1)
		// && mapBuilder.stationsSameLine(parent, stationId2)) {
		// System.out.println("Station " +
		// mapBuilder.getStationWithId(stationId2).getNom() + " and "
		// + mapBuilder.getStationWithId(parent).getNom() + " in the same line");
		// System.out.println();
		// res=-1;
		//
		// } else if (mapBuilder.stationsSameLine(parent, stationId1) &&
		// mapBuilder.stationsSameLine(parent, stationId2)) {
		// Integer distToTargetst1 =
		// mapBuilder.getRouteDistanceIncludingBothStations(stationId1, parent);
		// Integer distTotargetst2 =
		// mapBuilder.getRouteDistanceIncludingBothStations(stationId2, parent);
		//
		// if (distToTargetst1 > distTotargetst2) {
		// System.out.println(mapBuilder.getStationWithId(stationId1).getNom() + "
		// closer to "
		// + mapBuilder.getStationWithId(parent).getNom());
		// res=1;
		//
		// } else if (distToTargetst1 < distTotargetst2) {
		// System.out.println(mapBuilder.getStationWithId(stationId2).getNom() + "
		// closer to "
		// + mapBuilder.getStationWithId(parent).getNom());
		// res=-1;
		// } else if (distToTargetst1 == distTotargetst2) {
		// {
		// System.out.println("Both stations at the same distance");
		// res = 0;
		// }
		//
		// }
		// }
		//
		// System.out.println();
		// return res;

		return 0;

	}
}
