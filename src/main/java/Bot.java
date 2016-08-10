import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by tess on 4/9/14.
 */
public class Bot {

    private final AIMLFileParser fileParser;
    private final Normalizer normalizer;

    private final PatternMatcher matcher;

    public Bot(Boolean isSignificantBot)throws Exception{
        normalizer = new Normalizer();
        //System.out.println("creating parser");
        fileParser = new AIMLFileParser(isSignificantBot, normalizer);
        matcher= new PatternMatcher(fileParser.readAIMLFiles(), isSignificantBot);
    }

    public void run(){
        String userInput;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Hi, I'm Matilda. Nice to meet you!");
        try{
            while((userInput =reader.readLine()) != null){
                // Normalizing user input
                userInput = normalizer.normalizeToPattern(userInput);
		// FInd and print best response
                System.out.println(matcher.GetClosestMatch(userInput));
            }
        }catch (IOException e){

        }
    }

    public static void main(String args[]){
        try{
            Boolean isSignificantBot = true;
            Bot bot = new Bot(isSignificantBot);
            bot.run();
        }catch (Exception e){

        }
    }
}
