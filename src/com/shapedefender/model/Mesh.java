package com.shapedefender.model;

import java.util.ArrayList;


public class Mesh {

	protected ArrayList<MeshLayer> layers;
	
	public Mesh(){
		this.layers = new ArrayList<MeshLayer>();
	}
	
	public void addLayer(MeshLayer layer){
		layers.add(layer);
	}
	
	public void addLayer(MeshLayer layer, int index){
		layers.add(index, layer);
	}
	
	public ArrayList<MeshLayer> getLayers(){
		return layers;
	}
	
}
