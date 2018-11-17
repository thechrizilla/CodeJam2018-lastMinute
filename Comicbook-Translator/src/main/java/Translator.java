package main.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONArray;


public class Translator {


public static void main(String[] args) throws Exception 
 {
	
	String one = null;
	String two = null;
	String three = null;
	String answer = translation(one, two, three);
	System.out.print(answer);
  
 }
 
 public static String translation (String comicLanguage, String languageWanted, String sentence) throws Exception {
	 sentence = "hello my name is Chris and I am in McGill university"; //call Georges function
	 comicLanguage = "en"; //call Georges function
	 languageWanted = "ja"; //call Georges function
	 Translator http = new Translator();
	 String translated = http.callUrlAndParseResult(comicLanguage, languageWanted, sentence);
	 //String translation = http.callUrlAndParseResult("eng", "fr", "hi my name is Chris");
	 

	 return translated;
 }
 
 private String callUrlAndParseResult(String langFrom, String langTo,
                                             String word) throws Exception 
 {

  String url = "https://translate.googleapis.com/translate_a/single?"+
    "client=gtx&"+
    "sl=" + langFrom + 
    "&tl=" + langTo + 
    "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");    
  
  URL obj = new URL(url);
  HttpURLConnection con = (HttpURLConnection) obj.openConnection(); 
  con.setRequestProperty("User-Agent", "Mozilla/5.0");
 
  BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
  String inputLine;
  StringBuffer response = new StringBuffer();
 
  while ((inputLine = in.readLine()) != null) {
   response.append(inputLine);
  }
  in.close();
 
  return parseResult(response.toString());
 }
 
 private String parseResult(String inputJson) throws Exception
 {
  /*
   * inputJson for word 'hello' translated to language Hindi from English-
   * [[["नमस्ते","hello",,,1]],,"en"]
   * We have to get 'नमस्ते ' from this json.
   */
  
  JSONArray jsonArray = new JSONArray(inputJson);
  JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
  JSONArray jsonArray3 = (JSONArray) jsonArray2.get(0);
  
  return jsonArray3.get(0).toString();
 }
}