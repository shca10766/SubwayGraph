package com.SubwayGraph.jackson;

import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

public class Station {	
	private String commune;
	private float lat;
	private JsonNode lignes;
	private float lng;
	private String nom;
	private String num;
	private String type;
	private boolean isHub;
	private int[][] routes;
	private boolean isAmbiguous;
	private String cp;
	private boolean show;
	
	
	public String getCommune() { return commune; }
	public void setCommune(String commune) { this.commune = commune; }
	
	public float getLat() { return lat; }
	public void setLat(float lat) { this.lat = lat; }
	
	public JsonNode getLignes() { return lignes; }
	public void setLignes(JsonNode lignes) { this.lignes = lignes; }
	
	public float getLng() { return lng; }
	public void setLng(float lng) { this.lng = lng; }
	
	public String getNom() { return nom; }
	public void setNom(String nom) { this.nom = nom; }
	
	public String getNum() { return num; }
	public void setNum(String num) { this.num = num; }
	
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	
	public boolean getIsHub() { return isHub; }
	public void setIsHub(boolean isHub) { this.isHub = isHub; }
	
	public int[][] getRoutes() { return routes; }
	public void setRoutes(int[][] routes) { this.routes = routes; }
	
	public boolean getIsAmbiguous() { return isAmbiguous; }
	public void setIsAmbiguous(boolean isAmbiguous) { this.isAmbiguous = isAmbiguous; }
	
	public String getCp() { return cp; }
	public void setCp(String cp) { this.cp = cp; }
	
	public boolean getShow() { return show; }
	public void setShow(boolean show) { this.show = show; }
	
	public ArrayList<String> getListLines() {
		ArrayList<String> list = new ArrayList<String>();
		
		Iterator<String> type = getLignes().fieldNames();
        while (type.hasNext()) {
        	JsonNode lineNode = lignes.get(type.next());
			Iterator<JsonNode> elements = lineNode.iterator();
	        while (elements.hasNext()) {
	        	list.add(elements.next().toString());
	        }
        }
		
		return list;
	}
	
	
}
