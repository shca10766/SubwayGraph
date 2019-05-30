package com.SubwayGraph.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import com.SubwayGraph.jackson.Line;
import com.SubwayGraph.jackson.Routes;
import com.SubwayGraph.jackson.Station;
import com.SubwayGraph.jackson.Subway;

public class MapBuilder {
	private Subway subway;
	private Graph<String, DefaultEdge> subwayGraph;

	public Subway getSubway() {
		return subway;
	}

	public void setSubway(Subway subway) {
		this.subway = subway;
	}

	public Graph<String, DefaultEdge> getSubwayGraph() {
		return subwayGraph;
	}

	public void setSubwayGraph(Graph<String, DefaultEdge> subwayGraph) {
		this.subwayGraph = subwayGraph;
	}

	public MapBuilder(Graph<String, DefaultEdge> subwayGraph) {
		super();
		this.subwayGraph = subwayGraph;
	}

	public MapBuilder(Subway subway) {
		super();
		this.subway = subway;
		this.subwayGraph = new DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		addVertices();
		addEdges();
	}

	// Add vertices
	public void addVertices() {
		for (Station station : subway.getStations()) {
			subwayGraph.addVertex(station.getNum());
		}
	}

	// Add edges
	public void addEdges() {
		List<Station> stations = new ArrayList<>(subway.getStations());
		List<Routes> routes = new ArrayList<>(subway.getRoutes());

		for (int i = 0; i < stations.size(); i++) {
			int[][] arrets = stations.get(i).getRoutes();
			for (int j = 0; j < arrets.length; j++) {
				if (arrets[j][1] != 0) {
					String idEdge = routes.get(arrets[j][0]).getArrets()[arrets[j][1] - 1];
					subwayGraph.addEdge(stations.get(i).getNum(), idEdge);
				}
			}
		}
	}

	////////////////////////////////

	// Get a station by its id
	public Station getStationWithId(String id) {
		Station res = null;

		for (Station st : subway.getStations()) {
			if (st.getNum().equals(id)) {
				res = st;
				break;
			}
		}

		return res;
	}

	public List<String> getLinesAsociatedWithStation(String stationId) {
		List<String> res = new ArrayList<>();

		for (Line l : subway.getLines()) {
			if (!l.getNum().equals("0")) {
				if (getStationsOfLine(l.getNum()).contains(getStationWithId(stationId)) && !res.contains(l.getNum())) {
					res.add(l.getNum());
				}
			}
		}
		return res;
	}

	// Stations in the same line?
	public boolean stationsSameLine(String stationId1, String stationId2) {
		boolean res;

		List<String> lsStations1 = new ArrayList<>(getLinesAsociatedWithStation(stationId1));
		List<String> lsStations2 = new ArrayList<>(getLinesAsociatedWithStation(stationId2));
		// System.out.println("sts st1: "+lsStations1);
		// System.out.println("sts st2: "+lsStations2);

		lsStations1.retainAll(lsStations2);

		if (lsStations1.isEmpty()) {
			res = false;
		} else {
			res = true;
		}

		return res;
	}

	public List<Station> getStationsOfRoute(Routes route) {
		List<Station> res = new ArrayList<Station>();

		for (String stationId : route.getArrets()) {

			Station s = getStationWithId(stationId);
			res.add(s);
		}

		return res;
	}
	
	public List<String> getStationsOfRouteIds(Routes route) {
		List<String> res = new ArrayList<>();

		for (String stationId : route.getArrets()) {
			res.add(stationId);
		}

		return res;
	}
	
	public Integer positionStationRoute(String stationid, Routes route) {
		List<Station> stRoute = new ArrayList<>(getStationsOfRoute(route));
		Integer index = stRoute.indexOf(getStationWithId(stationid));
		return index;
	}

	
	
	public List<Routes> getRoutesAsociatedWithStation(String stationId) {
		List<Routes> res = new ArrayList<Routes>();
		List<String>s = new ArrayList<>();
		
		for (Routes rt : subway.getRoutes()) {
		
			if(!s.contains(rt.getDirection())) {
			if (getStationsOfRoute(rt.getDirection()).contains(getStationWithId(stationId))) {
				res.add(rt);
				s.add(rt.getDirection());
				//System.out.println("Direction: " + rt.getDirection());
				}
			}
		}
		return res;
	}
	
	public Integer getRouteDistanceIncludingBothStations(String stationId1, String stationId2) {
		
		Routes r =null;
		Integer distance = Integer.MAX_VALUE;
		Integer res = 0;
		
		List<Routes>routesSt1 = new ArrayList<>(getRoutesAsociatedWithStation(stationId1));
//		System.out.println("Init: "+getStationWithId(stationId1).getNom());
//		System.out.println("Target: "+getStationWithId(stationId2).getNom());
//		System.out.println();
		for(Routes rt: routesSt1) {
			
			if(getStationsOfRouteIds(rt).contains(stationId2)) {
				
				int posRtSt1 = positionStationRoute(stationId1, rt);
				int posRtSt2 = positionStationRoute(stationId2, rt);
				Integer dist = posRtSt1-posRtSt2;
				
				if(dist<distance) {
					distance = dist;
					r= rt;
				}
				
			}
		}
		
		//System.out.println("Direction: "+r.getDirection());
		//System.out.println("Distance to target: "+distance);
		
		if(!distance.equals(Integer.MAX_VALUE)) {
			res =distance;
		}
	
		return res;
	}

	///////////////////////////////////////////////////////////////

	// Stations in a route
	public List<Station> getStationsOfRoute(String direction) {
		List<Station> res = new ArrayList<Station>();

		for (Routes route : subway.getRoutes()) {
			if (route.getDirection() != null) {

				if (route.getDirection().equals(direction)) {
					for (String stationId : route.getArrets()) {

						Station s = getStationWithId(stationId);
						res.add(s);
						// System.out.println("Station Name: " + s.getNom() + ", stationId: " +
						// s.getNum());
					}

					break;
				}
			}

		}
		return res;
	}

	// Get stations in a line
	public List<Station> getStationsOfLine(String num) {
		List<Station> res = new ArrayList<Station>();

		for (Line line : subway.getLines()) {

			if (line.getNum().equals(num) && !line.getNum().equals("0")) {

				for (int i = 0; i < line.getArrets().length; i++) {
					for (int j = 0; j < line.getArrets()[i].length; j++) {

						String stationId = line.getArrets()[i][j];
						Station st = getStationWithId(stationId);
						// System.out.println(st.getNum() + ", " + st.getNom());

						if (st != null) {
							res.add(st);
						}
					}

				}

				break;
			}
		}
		return res;
	}



	//////////////////////////////////////////////////////////////
	// AUXILIAR

	// Order the stations by their position in the route
	public List<Station> orderStationsByPositionRoute(String routeDirection) {

		List<Station> stations = new ArrayList<>(getStationsOfRoute(routeDirection));

		for (Station station : stations) {
			Integer pos = positionStationRoute(station.getNum(), routeDirection);
			System.out.println("pos: " + pos);
		}
		return stations;

	}

	// Get position of a station in a route
	public Integer positionStationRoute(String stationid, String routeDirection) {
		List<Station> stRoute = new ArrayList<>(getStationsOfRoute(routeDirection));
		Integer index = stRoute.indexOf(getStationWithId(stationid));
		return index;
	}

	public Routes getRouteByDirection(String routeDirection) {
		Routes res = null;

		for (Routes route : subway.getRoutes()) {
			if (route.getDirection().equals(routeDirection)) {
				res = route;
				break;
			}

		}
		return res;
	}

	// Get routes in a line
	public List<Routes> getRoutesOfLine(String lineId) {
		List<Routes> res = new ArrayList<>();

		for (Routes route : subway.getRoutes()) {
			if (route.getLigne().equals(lineId)) {
				res.add(route);
			}
		}
		return res;
	}

	// Get a list of all the routes directions
	public List<String> getRoutesDirections() {
		List<String> res = new ArrayList<String>();

		for (Routes route : subway.getRoutes()) {
			res.add(route.getDirection());
		}
		return res;
	}

	public Routes getRouteByStations(String source, String target) {
		ArrayList<Routes> routes = subway.getRoutes();
		for (int i = 1; i < routes.size(); i++) {
			String[] arrets = routes.get(i).getArrets();
			for (int j = 0; j < arrets.length; j++) {
				if (arrets[j].equals(source)) {
					for (int k = j + 1; k < arrets.length; k++) {
						if (arrets[k].equals(target)) {
							return routes.get(i);
						}
					}
				}
			}
		}
		return null;
	}

}
