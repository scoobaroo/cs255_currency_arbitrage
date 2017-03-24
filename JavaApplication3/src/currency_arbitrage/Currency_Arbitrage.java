/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currency_arbitrage;

import static com.sun.jmx.defaults.ServiceName.DOMAIN;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

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
	try {
	    key = new SecretKeySpec( _secret.getBytes( "UTF-8"), "HmacSHA512" ); 
	} catch( UnsupportedEncodingException uee) {
	    System.err.println( "Unsupported encoding exception: " + uee.toString());
	    return null;
	} 

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
        
    }
    
}

