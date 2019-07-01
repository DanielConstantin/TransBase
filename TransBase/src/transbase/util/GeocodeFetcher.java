package transbase.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

public class GeocodeFetcher {
 //Bing service REST-URL
 public static final String bingLocationUrl="http://dev.virtualearth.net/REST/v1/Locations";
 //Authentication key to invoke Bing URL [user need to change this key]
 public static final String bingAuthenticationKey="ACpIVYe9vH3LKg8l3AxF~2uoB07X11dBdvit26Fx4tw~AsJdww2NqEObxMWc0DQ1oJCInEjoJ_bij0gOYv2-tzvykiRlLuHkFRfqBpnmmvnS";
  
 protected static final String EQUAL = "=";
 protected static final String AND = "&";
 private static final String PARAM_COUNTRY = "countryRegion";
 private static final String PARAM_STATE = "adminDistrict";
 private static final String PARAM_CITY = "locality";
 private static final String PARAM_ZIP = "postalCode";
 private static final String PARAM_ADDRESS = "addressLine";
 protected static final String PARAM_KEY = "key";
 protected static final String PARAM_OUTPUT = "output";
 
 /**
  * @param args
  */
 public static String getLoc(String streetAdreess, String city, String zipCode, String country ) throws ParserConfigurationException, org.json.simple.parser.ParseException {
  // TODO Auto-generated method stub
  GeocodeFetcher geocodeFetcherInstance = new GeocodeFetcher();
//  String streetAdreess = "3301 Stearns Hill Rd";
 // String city = "Waltham";
 String state = "";
 if(zipCode==null) zipCode="";
 // String country = "US";
 //String zipCode = "02451";
  String resposeOutputType = "json";
  /*
   * invoking prepareDynamicURL() to prepare URL dynamically by appenidng 
   * street address, city, state, country and zipCode
   */
  String dynamicURl = geocodeFetcherInstance.prepareDynamicURL(streetAdreess, city, state, zipCode, country, resposeOutputType );
  System.out.println("dynamicURl: " + dynamicURl);
  String result = null;
  if(!StringUtils.isEmpty(dynamicURl)){
   result = geocodeFetcherInstance.getResultHttpAsStream(dynamicURl);
  }
 
    return parseJsonStringstatic(result);
    
    
 }
 
 
//String xml = ""; //Populated XML String....

public static String parseJsonStringstatic(String result)throws ParserConfigurationException, org.json.simple.parser.ParseException{
    JSONObject rootJSON = (JSONObject) new JSONParser().parse(result); //do smth
    JSONArray dataList = (JSONArray) rootJSON.get("resourceSets");
    for(Object projectObj: dataList.toArray()){
        JSONObject project = (JSONObject)projectObj;
        JSONArray issueList = (JSONArray) project.get("resources");
        for(Object issueObj: issueList.toArray()){
            JSONObject issue = (JSONObject) issueObj;
            System.out.println("resources: "+issue);
            JSONArray geocodep = (JSONArray) issue.get("geocodePoints");
            for(Object coordinates: geocodep.toArray()){
                JSONObject coordin = (JSONObject) coordinates;
                System.out.println("Res: "+coordin+"\n");
                JSONArray res = (JSONArray) coordin.get("coordinates");
                 
                System.out.println("Res2: "+res+"\n");
                String resultStr = res.toJSONString();
                String toReturn = resultStr.substring(1, resultStr.length()-2);
                System.out.println(toReturn);
                return toReturn;
           
            }
        }

    }
    return null;
}    
    public static String getTagValue(String xml, String tagName){
    return xml.split("<"+tagName+">")[1].split("</"+tagName+">")[0];
}
  
 /*
  * This prepareDynamicURL() prepare URL dynamically by appending 
  * street address, city, state, country and zipCode as parameter 
  * after the static part of Bing api
  */
 protected String prepareDynamicURL(String streetAdreess, String city, 
   String state, String zipCode, String country, String resposeOutputType ) 
 {
  StringBuffer sb = new StringBuffer();
  sb.append(bingLocationUrl).append("?");
  sb.append(PARAM_COUNTRY).append(EQUAL).append(country).append(AND);
  try {
   sb.append(PARAM_STATE).append(EQUAL).append(encodeString(state)).append(AND);
  } catch (UnsupportedEncodingException e) {
   System.out.println("Unsupported code of state. Ignore the state");
  }
  try {
   sb.append(PARAM_CITY).append(EQUAL).append(encodeString(city)).append(AND);
  } catch (UnsupportedEncodingException e) {
   System.out.println("Unsupported code of city. Ignore the city");
  }
  sb.append(PARAM_ZIP).append(EQUAL).append(zipCode).append(AND);
  try {
   sb.append(PARAM_ADDRESS).append(EQUAL).append(encodeString(streetAdreess)).append(AND);
  } catch (UnsupportedEncodingException e) {
   System.out.println("Unsupported code of street. Ignore the address line");
  }
  //appending bing authentication key
  sb.append(PARAM_KEY).append(EQUAL).append(bingAuthenticationKey).append(AND);
   
  /*
   * appending response type/ output type value. Bing return response in
   * XML/ JSON/ Text format.   
  */
  sb.append(PARAM_OUTPUT).append(EQUAL).append(resposeOutputType);
  return sb.toString();
 }
  
 /*
  * this encodeString() used to encode 
  * street address, city, state, country and zipCode
  */
 protected String encodeString(String str) 
 throws UnsupportedEncodingException
 {
  return URLEncoder.encode(str.replace(".", "").replace(",","").replace(":", ""), "UTF-8");//.replace("+", "%20");
 }
  
 /*
  * this getResultHttpAsStream() used to invoke Bing Rest-api
  * using  HttpURLConnection and converting the result in string format
  */
 protected final String getResultHttpAsStream(String url) 
 {
  System.out.println("url = " + url);
     HttpURLConnection conn = null;
     InputStream in = null;
     BufferedReader rd = null;
     StringBuffer sb = new StringBuffer();
  try {
   URL u = new URL(url);
   conn = (HttpURLConnection)u.openConnection();
    
   if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
    //in = conn.getInputStream();
    // Get the response
    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String line;
    while ((line = rd.readLine()) != null)
    {
     sb.append(line);
    }
    rd.close();
   } 
    
  } catch (Throwable e) {
   in = null;
  } finally{
   conn = null;
  }
  return sb.toString();
 }
}
