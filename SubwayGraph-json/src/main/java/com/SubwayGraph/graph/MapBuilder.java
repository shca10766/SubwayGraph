package com.SubwayGraph.graph;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedGraph;

import com.SubwayGraph.jackson.Line;
import com.SubwayGraph.jackson.Routes;
import com.SubwayGraph.jackson.Station;
import com.SubwayGraph.jackson.Subway;

public class MapBuilder {
	private Subway subway;
	private Graph<Station, Edge> subwayGraph;

	
	public Subway getSubway() {
		return subway;
	}

	public void setSubway(Subway subway) {
		this.subway = subway;
	}

	public Graph<Station, Edge> getSubwayGraph() {
		return subwayGraph;
	}

	public MapBuilder() {
		super();
		this.subway = Main.init();
		this.subwayGraph = new DefaultUndirectedGraph<Station, Edge>(Edge.class);
	}

	// Construct the graph
	public void buildGraph() {
			//Adding vertices
			addVertices();
			//Adding edges
			addEdges();	

	}
	
	
	//Add vertices
	public void addVertices() {
		for(Station station: subway.getStations()) {
			subwayGraph.addVertex(station);
		}
	}
	
	//Add edges
	public void addEdges() {
		List<Station>stations = new ArrayList<>(subway.getStations());
		List<Routes>routes = new ArrayList<>(subway.getRoutes());
		
		for(int i = 0; i<stations.size();i++) {
			int[][] arrets = stations.get(i).getRoutes();
			for(int j = 0; j<arrets.length;j++) {
				if(arrets[j][1]!=0) {
					String idEdge = routes.get(arrets[j][0]).getArrets()[arrets[j][1]-1];
					addEdge(stations.get(i).getNum(),idEdge);
				}
			}
		}
	}
	
	

	private void addEdge(String num, String idEdge) {
		// TODO Auto-generated method stub
		
	}

	////////////////////////////////
	// Stations in a route
	public List<Station> getStationsOfRoute(String direction) {
		List<Station> res = new ArrayList<Station>();

		for (Routes route : subway.getRoutes()) {
			if (route.getDirection() != null) {

				if (route.getDirection().equals(direction)) {
					for (String stationId : route.getArrets()) {

						Station s = getStationWithId(stationId);
						res.add(s);
						//System.out.println("Station Name: " + s.getNom() + ", stationId: " + s.getNum());
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
			if (line.getNum().equals(num)) {

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

	
	
	public List<Line> getLinesAsociatedWithStation(String stationId) {
		List<Line> res = new ArrayList<>();

		for (Line l : subway.getLines()) {
			if (getStationsOfLine(l.getNum()).contains(getStationWithId(stationId))) {
				res.add(l);
			}
		}
		return res;
	}

	public List<Routes> getRoutesAsociatedWithStation(String stationId) {
		List<Routes> res = new ArrayList<Routes>();

		for (Routes rt : subway.getRoutes()) {
			if (getStationsOfRoute(rt.getDirection()).contains(getStationWithId(stationId))) {
				res.add(rt);

			}
		}
		return res;
	}

	// TODO
	// Neighbors stations
	public List<Station> getNeihborsStations(String name) {
		List<Station> res = new ArrayList<Station>();

		return null;
	}

	//////////////////////////////////////////////////////////////
	// AUXILIAR

	// Order the stations by their position in the route
	public List<Station> orderStationsByPositionRoute(String routeDirection) {

		List<Station> stations = new ArrayList<>(getStationsOfRoute(routeDirection));

		for (Station station : stations) {
			Integer pos = positionStationRoute(station.getNum(), routeDirection);
			System.out.println("pos: "+pos);
		}
		return stations;

	}
	
	

	// Get position of a station in a route
	public Integer positionStationRoute(String stationid, String routeDirection) {
		
		
		
		/*List<Station>stRoute = new ArrayList<>(getStationsOfRoute(routeDirection));
		Integer index = stRoute.indexOf(getStationWithId(stationid));*/
		
		//return index;
		return 0;
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

	// Get a list of all the routes directions
	public List<String> getRoutesDirections() {
		List<String> res = new ArrayList<String>();

		for (Routes route : subway.getRoutes()) {
			res.add(route.getDirection());
		}
		return res;
	}

}
