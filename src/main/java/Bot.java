import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by tess on 4/9/14.
 */
public class Bot {

    private final AIMLFileParser fileParser;

    private final PatternMatcher matcher = new PatternMatcher();

    public Bot()throws Exception{
        //System.out.println("creating parser");
        fileParser = new AIMLFileParser(true);
    }

    public void run(){
        String userInput;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Hi, I'm Matilda. Nice to meet you!");
        try{
            while((userInput =reader.readLine()) != null){
                userInput = normalize(userInput);
                //System.out.println("match userinput: "+ userInput);
                fileParser.Match(userInput);
            }
        }catch (IOException e){

        }
    }

    private String normalize(String userInput){
        userInput = userInput.replace('-', ' ').replace('"', ' ')
                .replace("<i>", " ").replace("</i>", " ")
                .replace("</font>", " ").replace("<font>", " ")
                .replace("<", " ").replace(">", " ").replace(".", " ").replace(",", " ")
                .replace("!", " ").replace("?", " ")
                .trim().toUpperCase();
        return userInput;
    }

    public static void main(String args[]){
        try{
            Bot bot = new Bot();
            bot.run();
        }catch (Exception e){

        }
    }
}
