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
import java.util.Random;
import java.util.Set;

public class AIMLFileParser {

    private final SubtitleParser subtitleParser = new SubtitleParser();
    private File movieNames = new File("aiml.txt");
    protected final static Logger logger = LoggerFactory.getLogger(AIMLFileParser.class);
    private File aimlFile;
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder dBuilder;
    private Document document;
    private HashMap<String, ArrayList<String>> patternAndTemplates = new HashMap<String, ArrayList<String>>();
    private HashMap<String, HashMap<String, ArrayList<String>>> first = new HashMap<String, HashMap<String, ArrayList<String>>>();
    private HashMap<String, HashMap<String,ArrayList<String>>> significant =
            new HashMap<String, HashMap<String, ArrayList<String>>>();
    private HashMap<String, Integer> wordCounts = new HashMap<String, Integer>();
    private int numOfWords = 0;
    private Boolean si = true;


    public AIMLFileParser(Boolean sig)throws Exception{
        si = sig;
        readFiles();
    }

    public void setSignificance(boolean boo){
        si= boo;
    }

    public void readFiles(){
        try{
            if(!movieNames.exists()){
                subtitleParser.startParsing();
            }
            BufferedReader movieNamesReader = new BufferedReader(new FileReader(movieNames));
            //System.out.println("creating reader");



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
        if(si){
            createIndexBySignificant();
        }else {
            createIndexByFirst();
        }

    }

    public void parseDocument(){
        document.getDocumentElement().normalize();

        //System.out.println("Root element :" + document.getDocumentElement().getNodeName());

        NodeList nList = document.getElementsByTagName("category");

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);


            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                if(patternAndTemplates.containsKey(eElement.getElementsByTagName("pattern").item(0).getTextContent())){
                    patternAndTemplates.get(eElement.getElementsByTagName("pattern").item(0).getTextContent())
                            .add(eElement.getElementsByTagName("template").item(0).getTextContent());
                }else{
                    ArrayList<String> array = new ArrayList<String>();
                    array.add(eElement.getElementsByTagName("template").item(0).getTextContent());
                    patternAndTemplates.put(eElement.getElementsByTagName("pattern").item(0).getTextContent(),
                            array);
                }
            }
        }

    }

    public void Match(String userInput){
        PatternMatcher matcher = new PatternMatcher();
        if(si){
            if(significant.containsKey(getMostSignificant(userInput))){
                if(getMostSignificant(userInput)==null){
                    System.out.println("I don't understand");
                    return;
                }
                //System.out.println(getMostSignificant(userInput));
                String bestMatch = matcher.ClosestWords(userInput, significant.get(getMostSignificant(userInput)).keySet());

                System.out.println("best match: " +bestMatch);
                if(bestMatch.equals("")||bestMatch == null){
                    //System.out.println("I don't understand");
                }else{
                    Random r = new Random();
                    //System.out.println(significant.get(getMostSignificant(userInput)).get(bestMatch).size());
                    System.out.println(significant.get(getMostSignificant(userInput)).get(bestMatch).get(
                            r.nextInt(significant.get(getMostSignificant(userInput)).get(bestMatch).size())));
                }
                }else{
                System.out.println("I don't understand");
            }
        }
        else{
            if(first.containsKey(getFirstWord(userInput))){
                String bestMatch = matcher.ClosestWords(userInput, first.get(getFirstWord(userInput)).keySet());

                System.out.println("best match: " +bestMatch);
                if(bestMatch.equals("")||bestMatch == null){
                    System.out.println("I don't understand");
                }else{
                    Random r = new Random();
                    System.out.println(first.get(getFirstWord(userInput)).get(bestMatch)
                            .get(r.nextInt(first.get(getFirstWord(userInput)).get(bestMatch).size())));
                }
            }
            else{
                System.out.println("I don't understand");
            }
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
                    numOfWords++;
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
                    max = numOfWords/wordCounts.get(words[i]);
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
        //System.out.println(firstword);
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
                    HashMap<String, ArrayList<String>> temp = new HashMap<String, ArrayList<String>>();
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
            //System.out.println(firstWord);
            if(first.containsKey(firstWord)){
                first.get(firstWord).put(sentence, patternAndTemplates.get(sentence));
            }
            else{
                HashMap<String, ArrayList<String>> temp = new HashMap<String, ArrayList<String>>();
                temp.put(sentence, patternAndTemplates.get(sentence));
                first.put(firstWord, temp);
            }
        }
    }


    public static void main(String args[]){
        try{
        AIMLFileParser parser = new AIMLFileParser(true);
        parser.parseDocument();
        parser.Match("hhhhh");
        }catch (Exception e){
            logger.info(e.getMessage());
        }

    }
}
