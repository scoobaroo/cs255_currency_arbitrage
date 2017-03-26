/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currency_arbitrage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    void BellmanFord(Graph graph, int src)
    {
        vertices = graph.vertices;
        edges = graph.edges;
        double dist[] = new double[vertices.size()];
 
        // Step 1: Initialize distances from src to all other
        // vertices as INFINITE
        for (int i=0; i<V.size(); ++i)
            dist[i] = Double.MAX_VALUE;
        dist[src] = 0.0;
 
        // Step 2: Relax all edges |V| - 1 times. A simple
        // shortest path from src to any other vertex can
        // have at-most |V| - 1 edges
        for (int i=1; i<V.size(); ++i)
        {
            for (int j=0; j<E; ++j)
            {
                Vertex u = graph.edges.get(j).src;
                Vertex v = graph.edges.get(j).dest;
                double weight = graph.edges.get(j).weight;
                if (dist[u]!=Double.MAX_VALUE && dist[u]+weight<dist[v]){
                    dist[v]=dist[u]+weight;
                }
            }
        }
 
        // Step 3: check for negative-weight cycles.  The above
        // step guarantees shortest distances if graph doesn't
        // contain negative weight cycle. If we get a shorter
        //  path, then there is a cycle.
        for (int j=0; j<E; ++j)
        {
            Vertex u = graph.edges.get(j).src;
            Vertex v = graph.edges.get(j).dest;
            double weight = graph.edges.get(j).weight;
            if (dist[u]!=Double.MAX_VALUE &&
                dist[u]+weight<dist[v])
              System.out.println("Graph contains negative weight cycle");
        }
        printArr(dist, V);
    }
 
    // A utility function used to print the solution
    void printArr(double dist[], int V)
    {
        System.out.println("Vertex   Distance from Source");
        for (int i=0; i<V; ++i)
            System.out.println(i+"\t\t"+dist[i]);
    }
 
}
