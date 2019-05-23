package com.SubwayGraph.jackson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedGraph;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MapBuilder {
	private Subway subway;
	private Graph<Station, Edge> subwayGraph;
	private ObjectMapper obj = new ObjectMapper();

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
			
		
	

		
		//Add edges: vertices in a route, sorted.
		
		
		
		
		
		
		
		// for (Station s: subwayGraph.vertexSet()) {
		// System.out.println("Name: "+s.getNom()+", Number: "+s.getNum()+", Lines:
		// "+s.getLignes());
		// System.out.println(subwayGraph.vertexSet().size());
		// }

	}
	
	
	//Add vertices
	public void addVertices() {
		for(Station station: subway.getStations()) {
			subwayGraph.addVertex(station);
		}
	}
	
	//Add edge
	
	

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
