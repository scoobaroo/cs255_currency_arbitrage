/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currency_arbitrage;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
        for (int k=0; k<edges.size(); k++)
        {
            Vertex u = edges.get(k).src;
            Vertex v = edges.get(k).dest;
            Edge e = edges.get(k);
            if (dist.get(u)+e.weight<dist.get(v)){
              System.out.println("\n======================================");
              System.out.println("\nGraph contains negative weight cycle");
              System.out.println("Cycle contains:" + u.name+ " connected to " + v.name + " by weight "+e.weight);
              path(u,v);
              Set cycle = new HashSet<>();
              while(cycle.add(v)){
                  v=v.predecessor;
              }
              printCycle(cycle);
            }
        }
        printDistanceHashMap(dist, vertices);
    }
    
    void path(Vertex u, Vertex v){
        System.out.println("\n INSIDE PATH FUNCTION");
        Vertex pred = v.predecessor;
        Set cycle1 = new HashSet<>();
        ArrayList<Vertex> cycleArrayList = new ArrayList<>();
        System.out.println("Checking v's name "+v.name);
        System.out.println("Checking u's name "+ u.name);
        cycle1.add(v);
        cycleArrayList.add(v);
        while(cycle1.add(pred)){
            cycleArrayList.add(pred);
            pred = pred.predecessor;          
        }
        Double begin = 1.0;
        Double cycleWeight = 0.0;
        for(int k=0; k<cycleArrayList.size(); k++){
            Vertex v1 = cycleArrayList.get(k);
            System.out.println("YOOOHOOOOO "+v1.name);
            if(k<cycleArrayList.size()-1){
                Vertex v2 = cycleArrayList.get(k+1);
                Edge edge = findEdge(v1,v2);
                cycleWeight += edge.weight;
                begin *= Math.exp(edge.weight);
            }
        }
        Edge lastEdge = findEdge(cycleArrayList.get(cycleArrayList.size()-1),cycleArrayList.get(0));
        cycleWeight += lastEdge.weight;
        begin *= Math.exp(lastEdge.weight);
        System.out.println("HERE ARE THE RESULTS!!!"+ Math.exp(cycleWeight) + "  " + begin);
        System.out.println("\nThere is a negative cycle Path from " + u.name +" to "+ v.name);
        System.out.println("\nStarting with 1 " +v.name+ " we can end up with " + begin +" "+v.name +" by utilizing the negative cycle");
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
        Iterator cycleIterator = c.iterator();
        while(cycleIterator.hasNext()){
            Vertex v = (Vertex) cycleIterator.next();
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
