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
public class Vertex {
    public Currency currency;
    public String name;
    public Vertex(){
 
    }
    public Vertex(Currency currency, String name) {
        super();
        this.currency = currency;
        this.name=name;
    }
    public Vertex getVertex(String name){
        if(name == this.name){
            return this;
        }
        else return null;
    }
}