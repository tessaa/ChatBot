/**
 * Created by Therese Askling on 4/11/14.
 */
public class Normalizer {

    public Normalizer(){

    }

    public String normalizeToPattern(String userInput){
        userInput = normalizeToTemplate(userInput);
        userInput = userInput.replace(".", " ").replace(",", " ")
                .replace("!", " ").replace("?", " ")
                .trim().toUpperCase();
        return userInput;
    }

    public String normalizeToTemplate(String userInput){
        userInput = userInput.replace('-', ' ').replace('"', ' ')
                .replace("<i>", " ").replace("</i>", " ")
                .replace("</font>", " ").replace("<font>", " ")
                .replace("<", " ").replace(">", " ").trim();
        return userInput;
    };
}
