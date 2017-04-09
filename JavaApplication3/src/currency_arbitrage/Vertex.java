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
public class Vertex {
    public Currency currency;
    public String name;
    public Vertex predecessor;
    ArrayList<Vertex> predecessors = new ArrayList<Vertex>();
    public Vertex(){
 
    }
    public Vertex(Currency currency, String name) {
        super();
        this.currency = currency;
        this.name=name;
        this.predecessor = null;
    }
    
}