/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currency_arbitrage;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.*;
import java.io.*;
import java.net.URL;
import java.io.IOException;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import static com.sun.jmx.defaults.ServiceName.DOMAIN;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.json.*;
import com.sun.grizzly.util.*; 
import static java.lang.Math.*;

/**
 *
 * @author suejanehan
 */
public class Currency_Arbitrage {

   private static long _nonce;
   
   // Create a unixtime nonce for the new API.
   _nonce = ( TradeApp.getApp().getCurrentGMTTimeMicros() / 1000000);

    /**
     * Execute a authenticated query on btc-e.
     *
     * @param method The method to execute.
     * @param arguments The arguments to pass to the server.
     *
     * @return The returned data as JSON or null, if the request failed.
     *
     * @see http://pastebin.com/K25Nk2Sv
     */
    private final JSONObject authenticatedHTTPRequest( String method, Map<String, String> arguments) {
	HashMap<String, String> headerLines = new HashMap<String, String>();  // Create a new map for the header lines.
	Mac mac;
	SecretKeySpec key = null;

	if( arguments == null) {  // If the user provided no arguments, just create an empty argument array.
	    arguments = new HashMap<String, String>();
	}
	
	arguments.put( "GET", method);  // Add the method to the post data.
	arguments.put( "nonce",  "" + ++_nonce);  // Add the dummy nonce.

	String postData = "";

	for( Iterator argumentIterator = arguments.entrySet().iterator(); argumentIterator.hasNext(); ) {
	    Map.Entry argument = (Map.Entry)argumentIterator.next();
	    
	    if( postData.length() > 0) {
		postData += "&";
	    }
	    postData += argument.getKey() + "=" + argument.getValue();
	}

       // Create a new secret key
       key = new SecretKeySpec( _secret.getBytes( "UTF-8"), "HmacSHA512" ); 

	// Create a new mac
	try {
	    mac = Mac.getInstance( "HmacSHA512" );
	} catch( NoSuchAlgorithmException nsae) {
	    System.err.println( "No such algorithm exception: " + nsae.toString());
	    return null;
	}

	// Init mac with key.
	try {
	    mac.init( key);
	} catch( InvalidKeyException ike) {
	    System.err.println( "Invalid key exception: " + ike.toString());
	    return null;
	}

	// Add the key to the header lines.
	headerLines.put( "Key", _key);

	// Encode the post data by the secret and encode the result as base64.
	try {
	    headerLines.put( "Sign", Hex.encodeHexString( mac.doFinal( postData.getBytes( "UTF-8"))));
	} catch( UnsupportedEncodingException uee) {
	    System.err.println( "Unsupported encoding exception: " + uee.toString());
	    return null;
	} 
	
	// Now do the actual request
	String requestResult = HttpUtils.httpPost( "https://" + DOMAIN + "/tapi", headerLines, postData);

	if( requestResult != null) {   // The request worked

	    try {
		// Convert the HTTP request return value to JSON to parse further.
		JSONObject jsonResult = JSONObject.fromObject( requestResult);

		// Check, if the request was successful
		int success = jsonResult.getInt( "success");

		if( success == 0) {  // The request failed.
		    String errorMessage = jsonResult.getString( "error");
		    
		    System.err.println( "btc-e.com trade API request failed: " + errorMessage);

		    return null;
		} else {  // Request succeeded!
		    return jsonResult.getJSONObject( "return");
		}

	    } catch( JSONException je) {
		System.err.println( "Cannot parse json request result: " + je.toString());

		return null;  // An error occured...
	    }
	} 

	return null;  // The request failed.
    } 
    public static void main(String[] args) {
        // original exchange rates
        Double BTCUSD1 = 931.809;
        Double LTCBTC1 = 0.00437;
        Double BTCRUR1 = 54985.93752;
        Double BTCEUR1 = 865.248;
        Double LTCUSD1 = 4.069039;
        Double LTCRUR1 = 239.63;
        Double LTCEUR1 = 3.784;
        Double NMCBTC1 = 0.00056;
        Double NMCUSD1 = 0.516;
        Double NVCBTC1 = 0.00236;
        Double NVCUSD1 = 2.204;
        Double USDRUR1 = 58.94;
        Double EURUSD1 = 1.07957;
        Double EURRUR1 = 63.86889;
        Double PPCBTC1 = 0.00056;    
        Double PPCUSD1 = 0.517;
        Double DSHBTC1 = 0.10433;
        Double DSHUSD1 = 97.56782;
        Double DSHRUR1 = 5694.085;
        Double DSHEUR1 = 90.329;
        Double DSHLTC1 = 23.877;
        Double DSHETH1 = 1.96;
        Double ETHBTC1 = 0.05308;
        Double ETHUSD1 = 49.50001;
        Double ETHRUR1 = 2930.48608;
        Double ETHEUR1 = 46.17211;
        Double ETHLTC1 = 12.17379;
        //creating -Log of original exchange rates to use in bellman ford
        Double BTCUSD = -Math.log(BTCUSD1);
        Double LTCBTC = -Math.log(LTCBTC1);
        Double BTCRUR = -Math.log(BTCRUR1);
        Double BTCEUR = -Math.log(BTCEUR1);
        Double LTCUSD = -Math.log(LTCUSD1);
        Double LTCRUR = -Math.log(LTCRUR1);
        Double LTCEUR = -Math.log(LTCEUR1);
        Double NMCBTC = -Math.log(NMCBTC1);
        Double NMCUSD = -Math.log(NMCUSD1);
        Double NVCBTC = -Math.log(NVCBTC1);
        Double NVCUSD = -Math.log(NVCUSD1);
        Double USDRUR = -Math.log(USDRUR1);
        Double EURUSD = -Math.log(EURUSD1);
        Double EURRUR = -Math.log(EURRUR1);        
        Double PPCBTC = -Math.log(PPCBTC1);
        Double PPCUSD = -Math.log(PPCUSD1);
        Double DSHBTC = -Math.log(DSHBTC1);
        Double DSHUSD = -Math.log(DSHUSD1);
        Double DSHRUR = -Math.log(DSHRUR1);
        Double DSHEUR = -Math.log(DSHEUR1);
        Double DSHLTC = -Math.log(DSHLTC1);
        Double DSHETH = -Math.log(DSHETH1);
        Double ETHBTC = -Math.log(ETHBTC1);
        Double ETHUSD = -Math.log(ETHUSD1);
        Double ETHRUR = -Math.log(ETHRUR1);
        Double ETHEUR = -Math.log(ETHEUR1);
        Double ETHLTC = -Math.log(ETHLTC1);
        //checking if log(1/x) = -log(x)
        System.out.println(LTCBTC);
        System.out.println(Math.log(1/LTCBTC1));
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
        ArrayList<Vertex> currencies = new ArrayList<>();
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
        Edge btcusd =new Edge(BTC,USD,BTCUSD);
        Edge ltcbtc =new Edge(LTC,BTC,LTCBTC);
        Edge btcrur =new Edge(BTC,RUR,BTCRUR);
        Edge btceur =new Edge(BTC,EUR,BTCEUR);
        Edge ltcusd =new Edge(LTC,USD,LTCUSD);
        Edge ltcrur =new Edge(LTC,RUR,LTCRUR);
        Edge ltceur =new Edge(LTC,EUR,LTCEUR);
        Edge nmcbtc =new Edge(NMC,BTC,NMCBTC);
        Edge nmcusd =new Edge(NMC,USD,NMCUSD);
        Edge nvcbtc =new Edge(NVC,BTC,NVCBTC);
        Edge nvcusd =new Edge(NVC,USD,NVCUSD);
        Edge usdrur =new Edge(USD,RUR,USDRUR);
        Edge eurusd =new Edge(EUR,USD,EURUSD);
        Edge eurrur =new Edge(EUR,RUR,EURRUR);
        Edge ppcbtc =new Edge(PPC,BTC,PPCBTC);
        Edge dshbtc =new Edge(DSH,BTC,DSHBTC);
        Edge dshusd =new Edge(DSH,USD,DSHUSD);
        Edge dshrur =new Edge(DSH,RUR,DSHRUR);
        Edge dsheur =new Edge(DSH,EUR,DSHEUR);
        Edge dshltc =new Edge(DSH,LTC,DSHLTC);
        Edge dsheth =new Edge(DSH,ETH,DSHETH);
        Edge ethbtc =new Edge(ETH,BTC,ETHBTC);
        Edge ethusd =new Edge(ETH,USD,ETHUSD);
        Edge ethrur =new Edge(ETH,RUR,ETHRUR);
        Edge etheur =new Edge(ETH,EUR,ETHEUR);
        Edge ethltc =new Edge(ETH,LTC,ETHLTC);
        Edge ppcusd =new Edge(PPC,USD,PPCUSD);
        //creating list of these edges
        ArrayList<Edge> edges = new ArrayList<>();
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
                
               
        System.out.println("Before for loop");
        ArrayList<Edge> newEdges = new ArrayList<Edge>();
        //creating new edges for reversing directions of edges with new weights, sources, and destinations
        for (int i = 0; i< edges.size() ; i++){
            System.out.println("Inside for loop 1");
            newEdges.add(edges.get(i));
            Edge e = new Edge(edges.get(i).dest,edges.get(i).src,-edges.get(i).weight);
            newEdges.add(e);
        }
        System.out.println("OUtside for loop");
        Graph graph;
        graph = new Graph(currencies,newEdges);
        
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter starting currency: ");
        String n = reader.next();
        Vertex src = graph.findSource(n,currencies);
        graph.BellmanFord(graph, src);
    }
    
}

