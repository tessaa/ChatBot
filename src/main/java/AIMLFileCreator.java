import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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


    public void createAIMLFiles(HashMap<String,String> dialogueList){
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("AIML");
            doc.appendChild(rootElement);

            Set<String> set = dialogueList.keySet();

            for(String s : set){
            // category elements
            Element staff = doc.createElement("category");
            rootElement.appendChild(staff);

            // pattern elements
            Element pattern = doc.createElement("pattern");
            pattern.appendChild(doc.createTextNode(s));
            staff.appendChild(pattern);

            // template elements
            Element template = doc.createElement("template");
            template.appendChild(doc.createTextNode(dialogueList.get(s)));
            staff.appendChild(template);
            }


            // write the content into aiml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(aimlFolder+aimlFileName));

            transformer.transform(source, result);


        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

}
