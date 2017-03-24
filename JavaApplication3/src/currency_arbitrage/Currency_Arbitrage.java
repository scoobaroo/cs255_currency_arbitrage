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
import com.sun.grizzly.utils.*; 
import static java.lang.Math.log;

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
        Double BTCUSD = -Math.log(BTCUSD);
        Double LTCBTC = -Math.log(LTCBTC);
        Double BTCRUR = -Math.log(BTCRUR);
        Double BTCEUR = -Math.log(BTCEUR);
        
        Double LTCUSD = -Math.log(LTCUSD);
        Double LTCRUR = -Math.log(LTCRUR);
        Double LTCEUR = -Math.log(LTCEUR);
        
        Double NMCBTC = -Math.log(NMCBTC);
        Double NMCUSD = -Math.log(NMCUSD);
        
        Double NVCBTC = -Math.log(NVCBTC);
        Double NVCUSD = -Math.log(NVCUSD);
        
        Double USDRUR = -Math.log(USDRUR);
        Double EURUSD = -Math.log(EURUSD);
        Double EURRUR = -Math.log(EURRUR);
        
        Double PPCBTC = -Math.log(PPCBTC);
        
        Double DSHBTC = -Math.log(DSHBTC);
        Double DSHUSD = -Math.log(DSHUSD);
        Double DSHRUR = -Math.log(DSHRUR);
        Double DSHEUR = -Math.log(DSHEUR);
        Double DSHLTC = -Math.log(DSHLTC);
        Double DSHETH = -Math.log(DSHETH);
        
        Double ETHBTC = -Math.log(ETHBTC);
        Double ETHUSD = -Math.log(ETHUSD);
        Double ETHRUR = -Math.log(ETHRUR);
        Double ETHEUR = -Math.log(ETHEUR);
        Double ETHLTC = -Math.log(ETHLTC);
        
        ArrayList currencies;
        currencies = new ArrayList<Object>();
        currencies.add("BTC");
        currencies.add("USD");
        currencies.add("LTC");
        currencies.add("RUR");
        currencies.add("EUR");
        currencies.add("NMC");
        currencies.add("NVC");
        currencies.add("PPC");
        currencies.add("DSH");
        currencies.add("ETH");
                
        Graph graph;
        graph = new Graph(currencies,25);
        
        // add edge BTCUSD
        graph.edge[0].src = (String) currencies.get(0);
        graph.edge[0].dest = (String) currencies.get(1);
        graph.edge[0].weight = BTCUSD;
        // add edge LTCBTC
        graph.edge[1].src = (String) currencies.get(2);
        graph.edge[1].dest = (String) currencies.get(0);
        graph.edge[1].weight = LTCBTC;
        // add edge BTCRUR
        graph.edge[2].src = (String) currencies.get(0);
        graph.edge[2].dest = (String) currencies.get(3);
        graph.edge[2].weight = BTCRUR;
        // add edge BTCEUR
        graph.edge[3].src = (String) currencies.get(0);
        graph.edge[3].dest = (String) currencies.get(4);
        graph.edge[3].weight = BTCEUR;
        // add edge LTCUSD
        graph.edge[4].src = (String) currencies.get(2);
        graph.edge[4].dest = (String) currencies.get(1);
        graph.edge[4].weight = LTCUSD;
        // add edge LTCRUR
        graph.edge[5].src = (String) currencies.get(2);
        graph.edge[5].dest = (String) currencies.get(3);
        graph.edge[5].weight = LTCRUR;
        // add edge LTCEUR
        graph.edge[6].src = (String) currencies.get(2);
        graph.edge[6].dest = (String) currencies.get(4);
        graph.edge[6].weight = LTCEUR;
        // add edge NMCBTC
        graph.edge[7].src = (String) currencies.get(5);
        graph.edge[7].dest = (String) currencies.get(0);
        graph.edge[7].weight = NMCBTC;
        // add edge NMCUSD
        graph.edge[8].src = (String) currencies.get(5);
        graph.edge[8].dest = (String) currencies.get(1);
        graph.edge[8].weight = NMCUSD;
        // add edge NVCBTC
        graph.edge[9].src = (String) currencies.get(6);
        graph.edge[9].dest = (String) currencies.get(0);
        graph.edge[9].weight = NVCBTC;
        // add edge NVCUSD
        graph.edge[10].src = (String) currencies.get(6);
        graph.edge[10].dest = (String) currencies.get(1);
        graph.edge[10].weight = NVCUSD;
        
        ///NOT DONE YET///
        // add edge xxxxxx
        graph.edge[11].src = (String) currencies.get(0);
        graph.edge[11].dest = (String) currencies.get(0);
        graph.edge[11].weight = NMCBTC;
        // add edge xxxxxx
        graph.edge[12].src = (String) currencies.get(0);
        graph.edge[12].dest = (String) currencies.get(0);
        graph.edge[12].weight = NMCBTC;
        // add edge NMCBTC
        graph.edge[13].src = (String) currencies.get(0);
        graph.edge[13].dest = (String) currencies.get(0);
        graph.edge[13].weight = NMCBTC;
        // add edge NMCBTC
        graph.edge[14].src = (String) currencies.get(0);
        graph.edge[14].dest = (String) currencies.get(0);
        graph.edge[14].weight = NMCBTC;
        // add edge NMCBTC
        graph.edge[15].src = (String) currencies.get(0);
        graph.edge[15].dest = (String) currencies.get(0);
        graph.edge[15].weight = NMCBTC;
        // add edge NMCBTC
        graph.edge[16].src = (String) currencies.get(0);
        graph.edge[16].dest = (String) currencies.get(0);
        graph.edge[16].weight = NMCBTC;
                // add edge NMCBTC
        graph.edge[17].src = (String) currencies.get(0);
        graph.edge[17].dest = (String) currencies.get(0);
        graph.edge[17].weight = NMCBTC;
        // add edge NMCBTC
        graph.edge[18].src = (String) currencies.get(0);
        graph.edge[18].dest = (String) currencies.get(0);
        graph.edge[18].weight = NMCBTC;
        // add edge NMCBTC
        graph.edge[19].src = (String) currencies.get(0);
        graph.edge[19].dest = (String) currencies.get(0);
        graph.edge[19].weight = NMCBTC;
        // add edge NMCBTC
        graph.edge[20].src = (String) currencies.get(0);
        graph.edge[20].dest = (String) currencies.get(0);
        graph.edge[20].weight = NMCBTC;
        // add edge NMCBTC
        graph.edge[21].src = (String) currencies.get(0);
        graph.edge[21].dest = (String) currencies.get(0);
        graph.edge[21].weight = NMCBTC;
        // add edge NMCBTC
        graph.edge[22].src = (String) currencies.get(0);
        graph.edge[22].dest = (String) currencies.get(0);
        graph.edge[22].weight = NMCBTC;
        // add edge xxx
        graph.edge[23].src = (String) currencies.get(0);
        graph.edge[23].dest = (String) currencies.get(0);
        graph.edge[23].weight = NMCBTC;
        // add edge NMCBTC
        graph.edge[24].src = (String) currencies.get(0);
        graph.edge[24].dest = (String) currencies.get(0);
        graph.edge[24].weight = NMCBTC;
        // add edge NMCBTC
        graph.edge[25].src = (String) currencies.get(0);
        graph.edge[25].dest = (String) currencies.get(0);
        graph.edge[25].weight = NMCBTC;
 
 
    }
    
}

