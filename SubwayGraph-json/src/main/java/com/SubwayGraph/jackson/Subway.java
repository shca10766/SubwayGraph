package com.SubwayGraph.jackson;

import java.util.ArrayList;

public class Subway {
	private ArrayList<ArrayList<String>> correspondences;
	private ArrayList<Station> stations;
	private ArrayList<Routes> routes;
	private ArrayList<Line> lines;
	
	public Subway(ArrayList<ArrayList<String>> c, ArrayList<Station> s, ArrayList<Routes> r, ArrayList<Line> l) {
		correspondences = c;
		stations = s;
		routes = r;
		lines = l;
	}

	public ArrayList<ArrayList<String>> getCorrespondences() {
		return correspondences;
	}

	public void setCorrespondences(ArrayList<ArrayList<String>> correspondences) {
		this.correspondences = correspondences;
	}

	public ArrayList<Station> getStations() {
		return stations;
	}

	public void setStations(ArrayList<Station> stations) {
		this.stations = stations;
	}

	public ArrayList<Routes> getRoutes() {
		return routes;
	}

	public void setRoutes(ArrayList<Routes> routes) {
		this.routes = routes;
	}

	public ArrayList<Line> getLines() {
		return lines;
	}

	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}
}
