
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tess on 3/1/14.
 */
public class SubtitleParser {

    private final String subtitleFolder = "Subtitles/";
    private final String movieNames = "Subtitles/Subtitles.txt";
    private BufferedReader movieNamesReader;
    private String line;
    private HashMap<String, Integer> wordMap;
    private String previousLine;
    private String unCompleteLine;
    private final AIMLFileCreator aimlFileCreator;
    private ArrayList<ArrayList<String>> dialogue;


    public SubtitleParser(){
        try{
            movieNamesReader = new BufferedReader(new FileReader(movieNames));
        }catch(FileNotFoundException e){
            System.err.println("File not found");
        }
        this.wordMap = new HashMap<String, Integer>();
        this.aimlFileCreator = new AIMLFileCreator();
        this.dialogue = new ArrayList<ArrayList<String>>();
    }

    public void readMovieNames()throws IOException{
        while((line = movieNamesReader.readLine()) != null){
            line = line.trim();
            unCompleteLine = null;
            previousLine = null;
            aimlFileCreator.setAimlFileName(line.replace(".srt", ".aiml"));
            aimlFileCreator.writeAIMLFile(parseSubtitleFile(line));
        }
    }

    private ArrayList<String> parseSubtitleFile(String filename){
        ArrayList<String> list = new ArrayList<String>();
        int i = 1;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(subtitleFolder+filename));
            BufferedWriter writer = new BufferedWriter(new FileWriter(subtitleFolder+2+filename));
            while ((line = reader.readLine()) != null){
                line = line.trim();
                if(line.isEmpty()){
                    continue;
                }
                if(line.matches("[0-9]+")){
                    continue;
                }
                if(line.equals(i+"")||line.trim().equals((i-1)+"")){
                    i++;
                    continue;
                }
                if(line.equals(1+"")){
                    continue;
                }
                if(line.startsWith("0")||line.startsWith("(")){
                    continue;
                }
                if(line.contains("[") || line.contains("]")){
                    continue;
                }
                if(!line.endsWith(".") && !line.endsWith("?") && !line.endsWith("!")){
                    if(unCompleteLine == null){
                        unCompleteLine = line.replace("\n", " ");
                    }else{
                        unCompleteLine = unCompleteLine + line;
                    }
                    continue;
                }
                else{
                    if(unCompleteLine != null){
                        line = unCompleteLine + " " +line;
                        System.out.println(line);
                        unCompleteLine = null;
                    }
                    line = line.replace('-', ' ').replace('"', ' ')
                            .replace("<i>", " ").replace("</i>", " ")
                            .replace("</font>", " ").replace("<font>", " ")
                            .replace("<", " ").replace(">", " ").trim();
                    list.add(line);

                }
            }
        }catch (IOException e){
            System.err.println("Could not read file: "+ filename);
        }
        return list;
    }

    private void saveLine(String line){
        String[] words = line.split(" ");
        int i = 0;
        for(String word : words){
            if(wordMap.containsKey(word)){
                i = wordMap.get(word);
                wordMap.remove(word);
                i++;
                wordMap.put(word, i);
            }else{
                wordMap.put(word, 1);
            }
        }
    }

    private void writeWords(){
        Object[] words = wordMap.keySet().toArray();
        for(Object word: words){
            String w = (String) word;
            int i = wordMap.get(w);
            System.out.println(w + "  "+ i);
        }
    }

    public static void main(String args[]){
        SubtitleParser parser = new SubtitleParser();
        try{
            parser.readMovieNames();
        }catch (IOException e){
            System.err.println("Could not read file");
        }
        //parser.writeWords();
    }
}
