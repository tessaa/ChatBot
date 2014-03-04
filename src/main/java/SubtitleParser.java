
import java.io.*;
import java.util.HashMap;

/**
 * Created by tess on 3/1/14.
 */
public class SubtitleParser {

    private final String subtitleFolder = "../Documents/Subtitles/";
    private final String movieNames = "../Documents/Subtitles/Subtitles.txt";
    private BufferedReader movieNamesReader;
    private String line;
    private HashMap<String, Integer> wordMap;


    public SubtitleParser(){
        try{
            movieNamesReader = new BufferedReader(new FileReader(movieNames));
        }catch(FileNotFoundException e){
            System.err.println("File not found");
        }
        wordMap = new HashMap<String, Integer>();
    }

    public void readMovieNames()throws IOException{
        while((line = movieNamesReader.readLine()) != null){
            parseSubtitleFile(line.trim());
        }
    }

    private void parseSubtitleFile(String filename){
        int i = 1;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(subtitleFolder+filename));
            BufferedWriter writer = new BufferedWriter(new FileWriter(subtitleFolder+2+filename));
            while ((line = reader.readLine()) != null){
                if(line.isEmpty()){
                    continue;
                }
                if(line.trim().matches("[0-9]+")){
                   System.out.println("number");
                    continue;
                }
                if(line.trim().equals(i+"")||line.trim().equals((i-1)+"")){
                    i++;
                    continue;
                }
                if(line.trim().equals(1+"")){
                    continue;
                }
                if(line.startsWith("0")||line.startsWith("(")){
                    continue;
                }
                if(line.contains("[") || line.contains("]")){
                    continue;
                }
                else{
                    line = line.replace('-', ' ').replace('"', ' ')
                            .replace("<i>", " ").replace("</i>", " ")
                            .replace("</font>", " ").replace("<font>", " ")
                            .replace("<", " ").replace(">", " ").trim();
                    //saveLine(line);
                    writer.write(line + "\n");
                    writer.write("\n");
                }
            }
        }catch (IOException e){
            System.err.println("Could not read file: "+ filename);
        }
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
