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

    private HashMap<String, Integer> numOfMatches = new HashMap<String, Integer>();
    private final SubtitleParser subtitleParser;
    private final File aimlFileNames;
    protected final static Logger logger = LoggerFactory.getLogger(AIMLFileParser.class);
    private File aimlFile;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document document;
    private HashMap<String, ArrayList<String>> patternAndTemplates;
    private HashMap<String, HashMap<String, ArrayList<String>>> first;
    private HashMap<String, HashMap<String,ArrayList<String>>> significant;
    private HashMap<String, Integer> wordCounts;
    private int numOfWords = 0;
    private Boolean isSignificant;
    private final Normalizer normalizer;


    public AIMLFileParser(Boolean isSignificant, Normalizer normalizer)throws Exception{
        aimlFileNames = new File("aiml.txt");
        this.normalizer = normalizer;
        this.isSignificant = isSignificant;
        subtitleParser = new SubtitleParser(normalizer);
        dbFactory = DocumentBuilderFactory.newInstance();
        patternAndTemplates = new HashMap<String, ArrayList<String>>();
        first = new HashMap<String, HashMap<String, ArrayList<String>>>();
        significant = new HashMap<String, HashMap<String, ArrayList<String>>>();
        wordCounts = new HashMap<String, Integer>();
    }


    public HashMap<String, ArrayList<String>> readAIMLFiles(){
        try{
            if(!aimlFileNames.exists()){
                subtitleParser.startParsing();
            }
            BufferedReader aimlFileNamesReader = new BufferedReader(new FileReader(aimlFileNames));
            String fileName;
            while((fileName = aimlFileNamesReader.readLine()) != null){
                aimlFile = new File("AIML/"+fileName);
                if(!aimlFile.exists()){
                    subtitleParser.startParsing();
                }
                dBuilder = dbFactory.newDocumentBuilder();
                document = dBuilder.parse(aimlFile);
                parseAIMLFile();

            }
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
        calculateAverageMatches(numOfMatches);
        return patternAndTemplates;
    }

    public void parseAIMLFile(){
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

        private void calculateAverageMatches(HashMap<String, Integer> numOfMatches){
        Set<String> wordSet = patternAndTemplates.keySet();
            float total = 0;
            for(String key: wordSet){
                total = total + patternAndTemplates.get(key).size();
            }
            System.out.println("Total amount of matches is: " + (total));
            System.out.println("Amount of patterns is: " + (wordSet.size()));
            System.out.println("Average amount of matches is: " + (total/wordSet.size()));
        }


    public static void main(String args[]){
        try{
        AIMLFileParser parser = new AIMLFileParser(true, new Normalizer());
        parser.parseAIMLFile();
        }catch (Exception e){
            logger.info(e.getMessage());
        }

    }
}
