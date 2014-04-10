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

    public void writeAIMLFile(ArrayList<String> dialogueList){
        try{
        BufferedWriter writer = new BufferedWriter(new FileWriter(aimlFolder + aimlFileName));
            writer.write(aimlOpeningTag+ "\n");
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
            writer.write(aimlClosingTag + "\n");
        }catch (IOException e){
            System.err.println("Could not write to file: "+ aimlFileName);
        }

    }

    public void createFile(HashMap<String,String> dialogueList){
        // System.out.println("Create aimlfile " + aimlFileName);
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("AIML");
            doc.appendChild(rootElement);

            Set<String> set = dialogueList.keySet();

            for(String s : set){
            // staff elements
            Element staff = doc.createElement("category");
            rootElement.appendChild(staff);

            // firstname elements
            Element pattern = doc.createElement("pattern");
            pattern.appendChild(doc.createTextNode(s));
            staff.appendChild(pattern);

            // lastname elements
            Element template = doc.createElement("template");
            template.appendChild(doc.createTextNode(dialogueList.get(s)));
            staff.appendChild(template);
            }


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(aimlFolder+aimlFileName));
            //System.out.println("file Created "+ aimlFileName);
            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

           //System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    public static void main(String args[]){
        AIMLFileCreator fileCreator = new AIMLFileCreator();
        //fileCreator.createFile();
    }
}
