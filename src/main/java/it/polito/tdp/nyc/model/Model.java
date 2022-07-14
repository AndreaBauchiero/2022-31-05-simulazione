package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	private NYCDao dao;
	private Graph<String,DefaultWeightedEdge> grafo;
	
	public Model() {
		this.dao = new NYCDao();
	}
	
	public void creaGrafo(String p) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(p));
		for(City c : this.dao.getVerticiPeso(p)) {
			Graphs.addEdgeWithVertices(this.grafo, c.city1, c.city2, c.getPeso());
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<String> getProvider(){
		return this.dao.getAllProviders();
	}
	public Set<String> getQuartieri(){
		return this.grafo.vertexSet();
	}
	
	public List<CittaDistanza> getQuartieriAdiacenti(String q) {
		List<CittaDistanza> result = new ArrayList<>();
		for(String vicino : Graphs.neighborListOf(this.grafo, q)) {
			CittaDistanza temp = new CittaDistanza(vicino,this.grafo.getEdgeWeight(this.grafo.getEdge(vicino, q)));
			result.add(temp);
		}
		
		Collections.sort(result, new Comparator<CittaDistanza>() {
			public int compare (CittaDistanza c1, CittaDistanza c2) {
				return c1.getDistanza().compareTo(c2.getDistanza());
			}
		});
		return result;
		
	}
}
