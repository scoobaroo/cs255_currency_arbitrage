/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currency_arbitrage;
import java.util.*;
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
           
        System.out.println(LTCBTC);
        ArrayList<Currency> currencies = new ArrayList<>();
        currencies.add(Currency.BTC); //0
        currencies.add(Currency.USD); //1
        currencies.add(Currency.LTC); //2
        currencies.add(Currency.RUR); //3 
        currencies.add(Currency.EUR); //4
        currencies.add(Currency.NMC); //5
        currencies.add(Currency.NVC); //6
        currencies.add(Currency.PPC); //7
        currencies.add(Currency.DSH); //8
        currencies.add(Currency.ETH); //9
               
        Graph graph;
        graph = new Graph(currencies,25);
        
        // add edge BTCUSD
        graph.edge[0].src = (Currency) currencies.get(0);
        graph.edge[0].dest = (Currency) currencies.get(1);
        graph.edge[0].weight = BTCUSD;
        // add edge LTCBTC
        graph.edge[1].src = (Currency) currencies.get(2);
        graph.edge[1].dest = (Currency) currencies.get(0);
        graph.edge[1].weight = LTCBTC;
        // add edge BTCRUR
        graph.edge[2].src = (Currency) currencies.get(0);
        graph.edge[2].dest = (Currency) currencies.get(3);
        graph.edge[2].weight = BTCRUR;
        // add edge BTCEUR
        graph.edge[3].src = (Currency) currencies.get(0);
        graph.edge[3].dest = (Currency)  currencies.get(4);
        graph.edge[3].weight = BTCEUR;
        // add edge LTCUSD
        graph.edge[4].src =(Currency) currencies.get(2);
        graph.edge[4].dest = (Currency)  currencies.get(1);
        graph.edge[4].weight = LTCUSD;
        // add edge LTCRUR
        graph.edge[5].src = (Currency)  currencies.get(2);
        graph.edge[5].dest = (Currency)  currencies.get(3);
        graph.edge[5].weight = LTCRUR;
        // add edge LTCEUR
        graph.edge[6].src = (Currency) currencies.get(2);
        graph.edge[6].dest = (Currency)  currencies.get(4);
        graph.edge[6].weight = LTCEUR;
        // add edge NMCBTC
        graph.edge[7].src = (Currency) currencies.get(5);
        graph.edge[7].dest = (Currency)  currencies.get(0);
        graph.edge[7].weight = NMCBTC;
        // add edge NMCUSD
        graph.edge[8].src = (Currency)  currencies.get(5);
        graph.edge[8].dest = (Currency)  currencies.get(1);
        graph.edge[8].weight = NMCUSD;
        // add edge NVCBTC
        graph.edge[9].src = (Currency)  currencies.get(6);
        graph.edge[9].dest = (Currency) currencies.get(0);
        graph.edge[9].weight = NVCBTC;
        // add edge NVCUSD
        graph.edge[10].src = (Currency)  currencies.get(6);
        graph.edge[10].dest = (Currency)  currencies.get(1);
        graph.edge[10].weight = NVCUSD;
        // add edge USDRUR
        graph.edge[11].src = (Currency)  currencies.get(1);
        graph.edge[11].dest = (Currency)  currencies.get(3);
        graph.edge[11].weight = USDRUR;
        // add edge EURUSD
        graph.edge[12].src = (Currency)  currencies.get(4);
        graph.edge[12].dest = (Currency)  currencies.get(1);
        graph.edge[12].weight = EURUSD;
        // add edge EURRUR
        graph.edge[13].src =(Currency) currencies.get(4);
        graph.edge[13].dest = (Currency)  currencies.get(3);
        graph.edge[13].weight = EURRUR;
        // add edge PPCBTC
        graph.edge[14].src =(Currency)  currencies.get(7);
        graph.edge[14].dest =(Currency)  currencies.get(0);
        graph.edge[14].weight = PPCBTC;
        // add edge DSHBTC
        graph.edge[15].src = (Currency)  currencies.get(8);
        graph.edge[15].dest = (Currency)  currencies.get(0);
        graph.edge[15].weight = DSHBTC;
        // add edge DSHUSD
        graph.edge[16].src = (Currency)  currencies.get(8);
        graph.edge[16].dest = (Currency)  currencies.get(1);
        graph.edge[16].weight = DSHUSD;
        // add edge DSHRUR
        graph.edge[17].src = (Currency)  currencies.get(8);
        graph.edge[17].dest = (Currency)  currencies.get(3);
        graph.edge[17].weight = DSHRUR;
        // add edge DSHEUR
        graph.edge[18].src = (Currency)  currencies.get(8);
        graph.edge[18].dest = (Currency)  currencies.get(4);
        graph.edge[18].weight = DSHEUR;
        // add edge DSHLTC
        graph.edge[19].src = (Currency) currencies.get(8);
        graph.edge[19].dest = (Currency)  currencies.get(2);
        graph.edge[19].weight = DSHLTC;
        // add edge DSHETH
        graph.edge[20].src = (Currency)  currencies.get(8);
        graph.edge[20].dest = (Currency)  currencies.get(9);
        graph.edge[20].weight = DSHETH;
        // add edge ETHBTC
        graph.edge[21].src = (Currency)  currencies.get(9);
        graph.edge[21].dest = (Currency)  currencies.get(0);
        graph.edge[21].weight = ETHBTC;
        // add edge ETHUSD
        graph.edge[22].src = (Currency)  currencies.get(9);
        graph.edge[22].dest = (Currency) currencies.get(1);
        graph.edge[22].weight = ETHUSD;
        // add edge ETHRUR
        graph.edge[23].src =(Currency) currencies.get(9);
        graph.edge[23].dest = (Currency)  currencies.get(3);
        graph.edge[23].weight = ETHRUR;
        // add edge ETHEUR
        graph.edge[24].src = (Currency)  currencies.get(9);
        graph.edge[24].dest = (Currency)  currencies.get(4);
        graph.edge[24].weight = ETHEUR;
        // add edge ETHLTC
        graph.edge[25].src = (Currency)  currencies.get(9);
        graph.edge[25].dest = (Currency)  currencies.get(2);
        graph.edge[25].weight = ETHLTC;
        // add edge PPCUSD
        graph.edge[26].src = (Currency)  currencies.get(7);
        graph.edge[26].dest = (Currency)  currencies.get(1);
        graph.edge[26].weight = PPCUSD;
 
        graph.BellmanFord(graph, 0);
    }
    
}

