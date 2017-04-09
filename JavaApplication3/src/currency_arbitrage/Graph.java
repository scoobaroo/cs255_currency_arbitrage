/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currency_arbitrage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author suejanehan
 */
public class Graph {
    public ArrayList<Vertex> vertices;
    public ArrayList<Edge> edges;
    public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges) {
        super();
        this.vertices = vertices;
        this.edges = edges;
    }

    // The main function that finds shortest distances from src
    // to all other vertices using Bellman-Ford algorithm.  The
    // function also detects negative weight cycle
    void BellmanFord(Graph graph, Vertex source)
    {
        
        Vertex src = source;
        int i,j;
        vertices = graph.vertices;
        edges = graph.edges;
        HashMap<Vertex, Double> dist = new HashMap(vertices.size());
        ArrayList<Vertex> preds = new ArrayList<Vertex>();
        
 
        // Step 1: Initialize distances from src to all other
        // vertices as INFINITE
        for(i=0;i<vertices.size();++i){
            dist.put(vertices.get(i),999.0);
        }
       
        dist.put(src,0.0);
        // Step 2: Relax all edges |V| - 1 times. A simple
        // shortest path from src to any other vertex can
        // have at-most |V| - 1 edges
        for (i = 0; i < vertices.size() - 1; ++i) {
            for (j = 0; j < edges.size(); ++j) { //here i am calculating the shortest path
                Vertex u = graph.edges.get(j).src;
                Vertex v = graph.edges.get(j).dest;
                Edge e = findEdge(u,v);
                if (dist.get(u) + e.weight < dist.get(v)) {
                    dist.put(v, dist.get(u) + e.weight);
                    v.predecessor = u;
                    preds.add(u);
                }
             } 
         }
        // Step 3: check for negative-weight cycles.  The above
        // step guarantees shortest distances if graph doesn't
        // contain negative weight cycle. If we get a shorter
        //  path, then there is a cycle.
        
        for (int k=0; k<edges.size(); ++k)
        {
            Vertex u = graph.edges.get(k).src;
            Vertex v = graph.edges.get(k).dest;
            Edge e = findEdge(u,v);
            if (dist.get(u)+e.weight<dist.get(v)){
              System.out.println("Graph contains negative weight cycle");
              System.out.println("Cycle contains:" + v.name+ " + " + u.name + " connected by weight "+e.weight);
              for (Vertex vertex : vertices){
                  while(vertex.predecessor!=null){
                    Vertex pred = vertex.predecessor;
                    System.out.println(pred.name + "-->" + vertex.name);
                    vertex = pred;
                  }
              }
            }
        }
        
        printDistanceHashMap(dist, vertices);
        
    }
    
    Vertex findSource(String name, ArrayList<Vertex> vertices){
        for( int i = 0; i<vertices.size();i++){
            Vertex v= vertices.get(i);
            if (v.name.equals(name))
                return v;
        }
        return null;
    }
    
    Edge findEdge(Vertex src, Vertex dest){
        for ( int i = 0; i < edges.size() ; ++i){
            Edge e = edges.get(i);
            if(e.src == src && e.dest== dest){
                return e;
            }
        }
        return null;
    }

    ArrayList<Vertex> findVertices(Graph subset){
        ArrayList<Vertex> set = new ArrayList<Vertex>();
        ArrayList<Edge> edges = new ArrayList<Edge>();
        edges = subset.edges;
        for (int q= 0; q< edges.size();q++){
            Edge edge = edges.get(q);
            set.add(edge.src);
            set.add(edge.dest);
        }
        return set;
    }
    
    // A utility function used to print the solution
    void printDistanceHashMap(HashMap<Vertex, Double> distance, ArrayList<Vertex> V)
    {
        System.out.println("Vertex Distance from Source");
        for (int i=0; i<V.size(); ++i)
            System.out.println(i+"\t\t"+distance.get(V.get(i)));
    }
}
