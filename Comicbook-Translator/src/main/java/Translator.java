package main.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.json.JSONArray;


public class Translator {


public static void main(String[] args) throws Exception 
 {
	//Testing values
	String comicLanguage = "en", desiredLanguage = "ja", comicSentence = "hello my name is Chris, what is your name?";	
	String comicTranslation = translateComic (comicLanguage, desiredLanguage, comicSentence);
	System.out.println(comicTranslation);
 }
 
 public static String translateComic (String comicLanguage, String languageWanted, String sentence) throws Exception 
 {
	 Translator http = new Translator();
	 String translatedText = http.callUrlAndParseResult(comicLanguage, languageWanted, sentence);
	 return translatedText;
 }
 
 private String callUrlAndParseResult(String langFrom, String langTo, String word) throws Exception 
 {
  String googleTranslate = 
	"https://translate.googleapis.com/translate_a/single?"+
    "client=gtx&"+
    "sl=" + langFrom + 
    "&tl=" + langTo + 
    "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");    
  
  URL obj = new URL(googleTranslate);
  HttpURLConnection con = (HttpURLConnection) obj.openConnection(); 
  con.setRequestProperty("User-Agent", "Chrome/5.0");
 
  BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
  String inputLine;
  StringBuffer response = new StringBuffer();
 
  while ((inputLine = in.readLine()) != null)
	  
  {
   response.append(inputLine);
  }
  
  in.close();
 
  return parseResult(response.toString());
 }
 
 private String parseResult(String inputJson) throws Exception
 {
  JSONArray jsonArray = new JSONArray(inputJson);
  JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
  JSONArray jsonArray3 = (JSONArray) jsonArray2.get(0);
  return jsonArray3.get(0).toString();
 }
}

