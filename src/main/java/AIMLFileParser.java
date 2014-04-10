/**
 * Created by tess on 4/9/14.
 */

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AIMLFileParser {

    private final SubtitleParser subtitleParser = new SubtitleParser();
    private File movieNames = new File("aiml.txt");
    protected final static Logger logger = LoggerFactory.getLogger(AIMLFileParser.class);
    private File aimlFile;
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder dBuilder;
    private Document document;
    private HashMap<String, String> patternAndTemplates = new HashMap<String, String>();
    private HashMap<String, HashMap<String, String>> first = new HashMap<String, HashMap<String, String>>();
    private HashMap<String, HashMap<String,String>> significant =
            new HashMap<String, HashMap<String, String>>();
    private HashMap<String, Integer> wordCounts = new HashMap<String, Integer>();
    private int numOfWords = 0;


    public AIMLFileParser()throws Exception{
        readFiles();
    }

    public void readFiles(){
        try{
            BufferedReader movieNamesReader = new BufferedReader(new FileReader(movieNames));
            //System.out.println("creating reader");

            if(!movieNames.exists()){
            subtitleParser.startParsing();
            }

            String movie = "hello";
            while((movie = movieNamesReader.readLine()) != null){
                //System.out.println(movie);
                aimlFile = new File("AIML/"+movie);
                if(!aimlFile.exists()){
                    subtitleParser.startParsing();
                }
                dBuilder = dbFactory.newDocumentBuilder();
                document = dBuilder.parse(aimlFile);
                parseDocument();
                //System.out.println(patternAndTemplates.size());

            }
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
        createWordSignificance();
        createIndexBySignificant();

    }

    public void parseDocument(){
        document.getDocumentElement().normalize();

        //System.out.println("Root element :" + document.getDocumentElement().getNodeName());

        NodeList nList = document.getElementsByTagName("category");

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);


            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;

                patternAndTemplates.put(eElement.getElementsByTagName("pattern").item(0).getTextContent(),
                        eElement.getElementsByTagName("template").item(0).getTextContent());
            }
        }

    }

    public void Match(String userInput){
        PatternMatcher matcher = new PatternMatcher();
        //System.out.println("create patternmatcher");
        if(getMostSignificant(userInput)==null){
            System.out.println("I don't understand");
            return;
        }
        String bestMatch = matcher.ClosestWords(userInput, significant.get(getMostSignificant(userInput)).keySet());

        //System.out.println("best match: " +bestMatch);
        if(bestMatch.equals("")||bestMatch == null){
            System.out.println("I don't understand");
        }else{
            System.out.println(significant.get(getMostSignificant(userInput)).get(bestMatch));
        }

    }

    private void createWordSignificance(){
        Set<String> set = patternAndTemplates.keySet();
        for(String s: set){
            //System.out.println(s);
            String[] words = s.split(" ");
            for(int i = 0; i< words.length; i++){
                String word = words[i];
                if(wordCounts.containsKey(word)){
                    int j = wordCounts.get(word);
                    j++;
                    wordCounts.remove(word);
                    wordCounts.put(word, j);
                }else{
                    wordCounts.put(word, 1);
                    numOfWords++;
                }
            }
        }
    }

    public String getMostSignificant(String sentence){
        //System.out.println(sentence);
        String mostSignificant = "";
        int max= 0;
        String[] words = sentence.split(" ");
        for(int i = 0; i<words.length; i++){
            if(wordCounts.containsKey(words[i])){
                //System.out.println(wordCounts.get(words[i]));
                if(numOfWords/wordCounts.get(words[i]) > max && (!words[i].equals(" "))){
                    max = wordCounts.get(words[i])/numOfWords;
                    mostSignificant = words[i];
                }
            }
        }

        //System.out.println("most significant word: "+ mostSignificant);
        return mostSignificant;
    }

    public String getFirstWord(String sentence){
        String firstword = "";
        String[] words = sentence.split(" ");
        firstword = words[0];
        return firstword;
    }

    private void createIndexBySignificant(){

        Set<String> set = patternAndTemplates.keySet();
        for(String sentence: set){
            String[] significantWords = sentence.split(" ");
                for(String word: significantWords){
                if(significant.containsKey(word)){
                    significant.get(word).put(sentence, patternAndTemplates.get(sentence));
                }
                else{
                    HashMap<String, String> temp = new HashMap<String, String>();
                    temp.put(sentence, patternAndTemplates.get(sentence));
                    significant.put(word, temp);
                }
            }
        }
    }

    private void createIndexByFirst(){

        Set<String> set = patternAndTemplates.keySet();
        for(String sentence: set){
            String firstWord = getFirstWord(sentence);
            if(significant.containsKey(firstWord)){
                significant.get(firstWord).put(sentence, patternAndTemplates.get(sentence));
            }
            else{
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(sentence, patternAndTemplates.get(sentence));
                significant.put(firstWord, temp);
            }
        }
    }


    public static void main(String args[]){
        try{
        AIMLFileParser parser = new AIMLFileParser();
        parser.parseDocument();
        parser.Match("hhhhh");
        }catch (Exception e){
            logger.info(e.getMessage());
        }

    }
}
