/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currency_arbitrage;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


/**
 *
 * @author suejanehan
 */

public class Currency_Arbitrage {
	private static String [] pairings = {
			"btc_usd",
			"ltc_btc",
			"btc_rur",
			"btc_eur",
			"ltc_usd",
			"ltc_rur",
			"ltc_eur",
			"nmc_btc",
			"nmc_usd",
			"nvc_btc",
			"nvc_usd",
			"usd_rur",
			"eur_usd",
			"eur_rur",
			"ppc_btc",
			"ppc_usd",
			"dsh_btc",
			"dsh_usd",
			"dsh_rur",
			"dsh_eur",
			"dsh_ltc",
			"dsh_eth",
			"eth_btc",
			"eth_usd",
			"eth_rur",
			"eth_eur",
			"eth_ltc"
			};
	private static HashMap<String,Double> exchangeRate;
	private static HashMap<String,Double> negExchangeRate;

	
	public static void getExchangeRates() {
		exchangeRate = new HashMap<String, Double>();
		negExchangeRate = new HashMap<String, Double>();
		try {
			for(String pair : pairings) {
				//System.out.println("Current url:" + "https://btc-e.com/api/3/ticker/" + pair);
				HttpResponse<JsonNode> jsonResponse = Unirest.get("https://btc-e.com/api/3/ticker/" + pair).asJson();
				Double rate = (jsonResponse.getBody().getObject().getJSONObject(pair).getDouble("avg"));
				exchangeRate.put(pair, rate);
				negExchangeRate.put(pair, -Math.log(rate));
				//System.out.println(pair + ":" + -Math.log(rate));
			}
			
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
    public static void main(String[] args) {
    	
    	//get new exchange rates
    	getExchangeRates();

        //creating set of vertices with currencies as their value
        Vertex BTC =new Vertex(Currency.BTC, "BTC");
        Vertex USD =new Vertex(Currency.USD, "USD");
        Vertex LTC =new Vertex(Currency.LTC, "LTC");
        Vertex RUR =new Vertex(Currency.RUR, "RUR");
        Vertex EUR =new Vertex(Currency.EUR, "EUR");
        Vertex NMC =new Vertex(Currency.NMC, "NMC");
        Vertex NVC =new Vertex(Currency.NVC, "NVC");
        Vertex PPC =new Vertex(Currency.PPC, "PPC");
        Vertex DSH =new Vertex(Currency.DSH, "DSH");
        Vertex ETH =new Vertex(Currency.ETH, "ETH");

        //creating ArrayList of vertexes to use in bellman ford
        ArrayList<Vertex> currencies = new ArrayList<Vertex>();
        currencies.add(BTC); //0
        currencies.add(USD); //1
        currencies.add(LTC); //2
        currencies.add(RUR); //3 
        currencies.add(EUR); //4
        currencies.add(NMC); //5
        currencies.add(NVC); //6
        currencies.add(PPC); //7
        currencies.add(DSH); //8
        currencies.add(ETH); //9
        //creating edges to use in bellman ford
       
        Edge btcusd =new Edge(BTC,USD,negExchangeRate.get("btc_usd"));
        Edge ltcbtc =new Edge(LTC,BTC,negExchangeRate.get("ltc_btc"));
        Edge btcrur =new Edge(BTC,RUR,negExchangeRate.get("btc_rur"));
        Edge btceur =new Edge(BTC,EUR,negExchangeRate.get("btc_eur"));
        Edge ltcusd =new Edge(LTC,USD,negExchangeRate.get("ltc_usd"));
        Edge ltcrur =new Edge(LTC,RUR,negExchangeRate.get("ltc_rur"));
        Edge ltceur =new Edge(LTC,EUR,negExchangeRate.get("ltc_eur"));
        Edge nmcbtc =new Edge(NMC,BTC,negExchangeRate.get("nmc_btc"));
        Edge nmcusd =new Edge(NMC,USD,negExchangeRate.get("nmc_usd"));
        Edge nvcbtc =new Edge(NVC,BTC,negExchangeRate.get("nvc_btc"));
        Edge nvcusd =new Edge(NVC,USD,negExchangeRate.get("nvc_usd"));
        Edge usdrur =new Edge(USD,RUR,negExchangeRate.get("usd_rur"));
        Edge eurusd =new Edge(EUR,USD,negExchangeRate.get("eur_usd"));
        Edge eurrur =new Edge(EUR,RUR,negExchangeRate.get("eur_rur"));
        Edge ppcbtc =new Edge(PPC,BTC,negExchangeRate.get("ppc_btc"));
        Edge dshbtc =new Edge(DSH,BTC,negExchangeRate.get("dsh_btc"));
        Edge dshusd =new Edge(DSH,USD,negExchangeRate.get("dsh_usd"));
        Edge dshrur =new Edge(DSH,RUR,negExchangeRate.get("dsh_rur"));
        Edge dsheur =new Edge(DSH,EUR,negExchangeRate.get("dsh_eur"));
        Edge dshltc =new Edge(DSH,LTC,negExchangeRate.get("dsh_ltc"));
        Edge dsheth =new Edge(DSH,ETH,negExchangeRate.get("dsh_eth"));
        Edge ethbtc =new Edge(ETH,BTC,negExchangeRate.get("eth_btc"));
        Edge ethusd =new Edge(ETH,USD,negExchangeRate.get("eth_usd"));
        Edge ethrur =new Edge(ETH,RUR,negExchangeRate.get("eth_rur"));
        Edge etheur =new Edge(ETH,EUR,negExchangeRate.get("eth_eur"));
        Edge ethltc =new Edge(ETH,LTC,negExchangeRate.get("eth_ltc"));
        Edge ppcusd =new Edge(PPC,USD,negExchangeRate.get("ppc_usd"));
        //creating list of these edges
        ArrayList<Edge> edges = new ArrayList<Edge>();
        edges.add(btcusd);
        edges.add(ltcbtc);
        edges.add(btcrur);
        edges.add(btceur);
        edges.add(ltcusd);
        edges.add(ltcrur);
        edges.add(ltceur);
        edges.add(nmcbtc);
        edges.add(nmcusd);
        edges.add(nvcbtc);
        edges.add(nvcusd);
        edges.add(usdrur);
        edges.add(eurusd);
        edges.add(eurrur);
        edges.add(ppcbtc);
        edges.add(dshbtc);
        edges.add(dshusd);
        edges.add(dshrur);
        edges.add(dsheur);
        edges.add(dshltc);
        edges.add(dsheth);
        edges.add(ethbtc);
        edges.add(ethusd);
        edges.add(ethrur);
        edges.add(etheur);
        edges.add(ethltc);
        edges.add(ppcusd);
              
        ArrayList<Edge> allEdges = new ArrayList<Edge>();
        //creating new edges for reversing directions of edges with new weights, sources, and destinations
        for (int i = 0; i< edges.size() ; i++){
            allEdges.add(edges.get(i));
            Edge e = new Edge(edges.get(i).dest,edges.get(i).src, -edges.get(i).weight);
            allEdges.add(e);
            //System.out.println(edges.get(i).weight);
        }

        Graph graph = new Graph(currencies,allEdges);
        
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter starting currency: ");
        String n = reader.next();
        Vertex src = graph.findSource(n,graph.vertices);
        graph.BellmanFord(graph, src);


    }
    
}