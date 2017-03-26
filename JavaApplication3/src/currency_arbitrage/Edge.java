/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currency_arbitrage;

/**
 *
 * @author suejanehan
 */

public class Edge {
    public Vertex src;
    public Vertex dest;
    public double weight;

    public Edge(Vertex src, Vertex dest, double weight) {
        super();
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
}