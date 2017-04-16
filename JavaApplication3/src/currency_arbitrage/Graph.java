/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currency_arbitrage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author suejanehan
 */
public class Graph {
    public ArrayList<Vertex> vertices;
    public ArrayList<Edge> edges;
    double inf = Double.POSITIVE_INFINITY;
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
 
        // Step 1: Initialize distances from src to all other
        // vertices as INFINITE
        for(i=0;i<vertices.size();i++){
            dist.put(vertices.get(i),inf);
        }
       
        dist.put(src,0.0);
        // Step 2: Relax all edges |V| - 1 times. A simple
        // shortest path from src to any other vertex can
        // have at-most |V| - 1 edges
        for (i = 0; i < vertices.size() - 1; i++) {
            for (j = 0; j < edges.size(); j++) { //here i am calculating the shortest path
                Vertex u = edges.get(j).src;
                Vertex v = edges.get(j).dest;
                Edge e = edges.get(j);
                if (dist.get(u) + e.weight < dist.get(v)) {
                    dist.put(v, dist.get(u) + e.weight);
                    v.predecessor = u;
                }
             } 
         }
        // Step 3: check for negative-weight cycles.  The above
        // step guarantees shortest distances if graph doesn't
        // contain negative weight cycle. If we get a shorter
        //  path, then there is a cycle.
        Set outsideCycle = new HashSet<Vertex>();
        for (int k=0; k<edges.size(); k++)
        {
            Vertex u = edges.get(k).src;
            Vertex v = edges.get(k).dest;
            Edge e = edges.get(k);
            if (dist.get(u)+e.weight<dist.get(v)){
              System.out.println("\n======================================");
              System.out.println("\nGraph contains negative weight cycle");
              System.out.println("Cycle contains:" + u.name+ " connected to " + v.name + " by weight "+e.weight);
              System.out.println("But there is a negative cycle so the actual weight is actually even more negative");
              System.out.println("Distance in hashtable for Vertex v:"+dist.get(v));
              System.out.println("Distance in hashtable for Vertex u:"+dist.get(u));
              path(u,v);
              Set cycle = new HashSet<>();
              while(cycle.add(v)){
                  v=v.predecessor;
              }
              printCycle(cycle);
              
              outsideCycle = cycle;
            }
        }
        
        printDistanceHashMap(dist, vertices);
        
    }
    
    void path(Vertex u, Vertex v){
        System.out.println("\n we are inside path function now");
        Vertex curr = v;
        Vertex pred = v.predecessor;
        Set cycle1 = new HashSet<>();
        cycle1.add(v);
        while(cycle1.add(pred)){
            cycle1.add(pred);
            pred = pred.predecessor;          
        }
        System.out.println("\nPath from " + v.name +" to "+ u.name);
        Iterator cycleIterator = cycle1.iterator();
        Double cycleWeight = 0.0;
        while(cycleIterator.hasNext()){
            Vertex vert = (Vertex) cycleIterator.next();
            Vertex vert2 = vert.predecessor;
            Edge edge = findEdge(vert2,vert);
            cycleWeight = cycleWeight + edge.weight;
            System.out.print( vert.name + "-->by weight " + edge.weight + "-->"+vert2.name + " ");
        }
        Edge finalEdge = findEdge(v,u);
        cycleWeight +=finalEdge.weight;
        System.out.println("\nTotal weight for cycle is"+ cycleWeight);
        System.out.println("Starting with 1 " +v.name+ " we can end up with " + 1/Math.exp(cycleWeight) +" "+v.name +" by following the negative cycle");
//        System.out.print(v.name+"<---");
//        System.out.print(v.predecessor.name+"<---");
//        System.out.print(v.predecessor.predecessor.name+"<---");
//        System.out.print(v.predecessor.predecessor.predecessor.name+"<---");
//        System.out.print(v.predecessor.predecessor.predecessor.predecessor.name+"<---");
//        System.out.print(curr.name+"---->");
//        Double pathWeight = 0.0;
//        while(true) {
//            curr=pred;
//            pred = curr.predecessor;
//            Edge e = findEdge(curr,pred);
//            System.out.println(e.weight);
//            pathWeight = pathWeight + e.weight;
//            System.out.println("path from" +curr.name +" to " +"pred.name " +"connected by weight "+e.weight);
//            System.out.print(pred.name+"--->");
//            if(pred.name.equals(u.name)){
//                System.out.print(u.name);
//                break;
//            }
//        }
    }
    
    void printCycle(Set<Vertex> c){
        System.out.println("we are printing the contents of the Set<Vertex> cycle");
        Iterator cycleIterator2 = c.iterator();
        while(cycleIterator2.hasNext()){
            Vertex v = (Vertex) cycleIterator2.next();
            System.out.print(v.name + "-->");
        }
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
        for ( int i = 0; i < edges.size(); ++i){
            Edge e = edges.get(i);
            if(e.src == src && e.dest== dest){
                return e;
            }
        }
        return null;
    }
    // A utility function used to print the solution
    void printDistanceHashMap(HashMap<Vertex, Double> distance, ArrayList<Vertex> V)
    {   System.out.println("\n****************************");
        System.out.println("Vertex Distance from Source");
        for (int i=0; i<V.size(); ++i)
            System.out.println(V.get(i).name+"\t\t"+distance.get(V.get(i)));
    }
}
