
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
    private ArrayList<String> aimlFiles = new ArrayList<String>();


    public SubtitleParser(){
        //System.out.println("Creating movieNamesReader");
        try{
            movieNamesReader = new BufferedReader(new FileReader(movieNames));
        }catch(FileNotFoundException e){
            System.err.println("File not found");
        }
        this.wordMap = new HashMap<String, Integer>();
        this.aimlFileCreator = new AIMLFileCreator();
        this.dialogue = new ArrayList<ArrayList<String>>();
    }

    public void startParsing()throws IOException{
       // System.out.println("startParsing");
            while((line = movieNamesReader.readLine()) != null){
            //System.out.println("Writing filename: "+ line);
            line = line.trim();
                aimlFiles.add(line);
            unCompleteLine = null;
            previousLine = null;
            aimlFileCreator.setAimlFileName(line.replace(".srt", ".aiml"));
            //System.out.println("Writing filename: "+ line);
            aimlFileCreator.createFile(parseSubtitleFile(line));
                    }
        createAIMLFileNames();
    }

    private void createAIMLFileNames(){
        try{
            File file = new File("aiml.txt");
            if(file.canWrite()){
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            //System.out.println("createAIML");
            for(String s: aimlFiles){
                s = s.replace(".srt", ".aiml");
               // System.out.println(s);
                writer.write(s);
                writer.newLine();
                writer.flush();
            }
            }

        }catch (IOException e){
            System.err.println("Could not write to file: ");
        }
    }
    private HashMap<String,String> parseSubtitleFile(String filename){
        HashMap<String, String> list = new HashMap<String, String>();
        int i = 1;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(subtitleFolder+filename));
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
                if(!line.endsWith(".") && !line.endsWith("?") && !line.endsWith("!") && (line.length() <20)){
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
                        unCompleteLine = null;
                    }
                    line = line.replace('-', ' ').replace('"', ' ')
                            .replace("<i>", "").replace("</i>", "")
                            .replace("</font>", "").replace("<font>", "")
                            .replace("<", "").replace(">", "")
                            .trim();
                    if(previousLine != null){
                        String temp = line;
                        list.put(previousLine.toUpperCase().replace(".", " ").replace(",", " ")
                                .replace("!", " ").replace("?", " ").trim(),temp);
                        previousLine = line;
                    }else{
                        previousLine = line;
                    }

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
            parser.startParsing();
        }catch (IOException e){
            System.err.println("Could not read file");
        }
        //parser.writeWords();
    }
}
