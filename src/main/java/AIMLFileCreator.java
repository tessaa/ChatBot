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

    public AIMLFileCreator(){
        this.aimlOpeningTag = "<aiml version ='1.0.1' encoding 'UTF-8'?>";
        this.aimlClosingTag = "</aiml>";
        this.categoryOpeningTag = "<category>";
        this.categoryClosingTag = "</category>";
        this.patternOpeningTag = "<pattern>";
        this.patternClosingTag = "</pattern>";
        this.templateOpeningTag = "<template>";
        this.templateClosingTag = "</template>";
    }

    public void setAimlFileName(String aimlFileName) {
        this.aimlFileName = aimlFileName;
    }
}
