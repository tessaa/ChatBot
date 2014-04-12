
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tess on 3/1/14.
 */
public class SubtitleParser {

    private final String subtitleFolder = "Subtitles/";
    private final File subtitleFileNames;
    private BufferedReader subtitleFileNamesReader;
    private String line;
    private HashMap<String, Integer> wordMap;
    private String previousLine;
    private String unCompleteLine;
    private final AIMLFileCreator aimlFileCreator;
    private ArrayList<ArrayList<String>> dialogue;
    private ArrayList<String> aimlFiles = new ArrayList<String>();
    private final Normalizer normalizer;

    public SubtitleParser(Normalizer normalizer){
        subtitleFileNames = new File("Subtitles/Subtitles.txt");
        this.normalizer = normalizer;
        try{
            subtitleFileNamesReader = new BufferedReader(new FileReader(subtitleFileNames));
        }catch(FileNotFoundException e){
            System.err.println("File not found");
        }
        this.wordMap = new HashMap<String, Integer>();
        this.aimlFileCreator = new AIMLFileCreator();
        this.dialogue = new ArrayList<ArrayList<String>>();
    }

    public void startParsing()throws IOException{
        while((line = subtitleFileNamesReader.readLine()) != null){
            line = line.trim();
            aimlFiles.add(line);
            unCompleteLine = null;
            previousLine = null;
            aimlFileCreator.setAimlFileName(line.replace(".srt", ".aiml"));
            aimlFileCreator.createAIMLFiles(parseSubtitleFile(line));
        }
        createAIMLFileNamesFile();
    }

    private void createAIMLFileNamesFile(){
        try{
            File file = new File("aiml.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for(String s: aimlFiles){
                s = s.replace(".srt", ".aiml");
                writer.write(s);
                writer.newLine();
                writer.flush();
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
                if(!line.endsWith(".") && !line.endsWith("?") && !line.endsWith("!") && (line.length() <100)){
                    if(unCompleteLine == null){
                        unCompleteLine = line.replace("\n", " ");
                    }else{
                        unCompleteLine = unCompleteLine + " " + line;
                    }
                    continue;
                }
                else{
                    if(unCompleteLine != null){
                        line = unCompleteLine + " " +line;
                        unCompleteLine = null;
                    }
                    line = normalizer.normalizeToTemplate(line);
                    if(previousLine != null){
                        list.put(previousLine,line);
                        previousLine = normalizer.normalizeToPattern(line);
                    }else{
                        previousLine = normalizer.normalizeToPattern(line);
                    }

                }
            }
        }catch (IOException e){
            System.err.println("Could not read file: "+ filename);
        }
        return list;
    }

    public static void main(String args[]){
        SubtitleParser parser = new SubtitleParser(new Normalizer());
        try{
            parser.startParsing();
        }catch (IOException e){
            System.err.println("Could not read file");
        }
    }
}
