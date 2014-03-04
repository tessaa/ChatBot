import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by tess on 3/3/14.
 */
public class AIMLFileCreator {

    private final String aimlOpeningTag;
    private final String aimlClosingTag;
    private final String categoryOpeningTag;
    private final String categoryClosingTag;
    private final String patternOpeningTag;
    private final String patternClosingTag;
    private final String templateOpeningTag;
    private final String templateClosingTag;
    private String aimlFileName;
    private final String aimlFolder;

    public AIMLFileCreator(){
        this.aimlOpeningTag = "<aiml version ='1.0.1' encoding 'UTF-8'?>";
        this.aimlClosingTag = "</aiml>";
        this.categoryOpeningTag = "<category>";
        this.categoryClosingTag = "</category>";
        this.patternOpeningTag = "<pattern>";
        this.patternClosingTag = "</pattern>";
        this.templateOpeningTag = "<template>";
        this.templateClosingTag = "</template>";
        this.aimlFolder = "AIML/";
    }

    public void setAimlFileName(String aimlFileName) {
        this.aimlFileName = aimlFileName;
    }

    public void writeAIMLFile(ArrayList<String> dialogueList){
        try{
        BufferedWriter writer = new BufferedWriter(new FileWriter(aimlFolder + aimlFileName));
            for(int i = 0; i< dialogueList.size()-1; i++){
                writer.write(categoryOpeningTag +"\n");
                writer.write(patternOpeningTag);
                writer.write(dialogueList.get(i));
                writer.write(patternClosingTag+"\n");
                writer.write(templateOpeningTag+"\n");
                writer.write(dialogueList.get(i+1));
                writer.write(templateClosingTag+"\n");
                writer.write(categoryClosingTag+"\n");
            }
        }catch (IOException e){
            System.err.println("Could not write to file: "+ aimlFileName);
        }
    }
}
