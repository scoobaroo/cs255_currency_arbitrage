/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currency_arbitrage;

import java.util.ArrayList;

/**
 *
 * @author suejanehan
 */
public class Graph
{
    // A class to represent a weighted edge in graph
    class Edge {
        Currency src, dest;
        Double weight;
        Edge() {
            src = dest = Currency.BTC;
            weight = 0.0;
        }
    };
 
    int E;
    ArrayList<Currency> V;
    Edge edge[];
 
    // Creates a graph with V vertices and E edges
    Graph(ArrayList<Currency> v, int e)
    {
        V = v;
        E = e;
        edge = new Edge[e];
        for (int i=0; i<e; ++i)
            edge[i] = new Edge();
    }
 
    // The main function that finds shortest distances from src
    // to all other vertices using Bellman-Ford algorithm.  The
    // function also detects negative weight cycle
    void BellmanFord(Graph graph, int src)
    {
        ArrayList<Currency> V = graph.V;
        E = graph.E;
        double dist[] = new double[V.size()];
 
        // Step 1: Initialize distances from src to all other
        // vertices as INFINITE
        for (int i=0; i<V.size(); ++i)
            dist[i] = Integer.MAX_VALUE;
        dist[src] = 0;
 
        // Step 2: Relax all edges |V| - 1 times. A simple
        // shortest path from src to any other vertex can
        // have at-most |V| - 1 edges
        for (int i=1; i<V.size(); ++i)
        {
            for (int j=0; j<E; ++j)
            {
                Currency u = graph.edge[j].src;
                Currency v = graph.edge[j].dest;
                double weight = graph.edge[j].weight;
                if (dist[u]!=Integer.MAX_VALUE &&
                    dist[u]+weight<dist[v])
                    dist[v]=dist[u]+weight;
            }
        }
 
        // Step 3: check for negative-weight cycles.  The above
        // step guarantees shortest distances if graph doesn't
        // contain negative weight cycle. If we get a shorter
        //  path, then there is a cycle.
        for (int j=0; j<E; ++j)
        {
            Currency u = graph.edge[j].src;
            Currency v = graph.edge[j].dest;
            double weight = graph.edge[j].weight;
            if (dist[u]!=Integer.MAX_VALUE &&
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
 
    // Driver method to test above function
    public static void main(String[] args)
    {
        int V = 5;  // Number of vertices in graph
        int E = 8;  // Number of edges in graph
 
        Graph graph = new Graph(V, E);
        
        // add edge 0-1 (or A-B in above figure)
        graph.BellmanFord(graph, 0);
    }
}
