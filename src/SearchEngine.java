import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SearchEngine implements ISearchEngine{
    private String id;
    private int rank;
    IBTree<String, Integer> tree = new Tree(3);
    HashMap<String, IBTree> map = new HashMap<>();

    private String parser(String filePath) {
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("doc");

            //now XML is loaded as Document in memory
            for (int i = 0; i < nodeList.getLength(); i++) {
                System.out.println(nodeList.item(i).getNodeName());
                Element eElement = (Element) nodeList.item(i);
                System.out.println("Student roll no : "
                        + eElement.getAttribute("id"));

                System.out.println("Marks : "
                        + eElement
                        .getTextContent());
            }

        }
        catch (IOException | SAXException | ParserConfigurationException e1) {
            e1.printStackTrace();
        }

        return "";
    }

    public static void main(String[] args) {
        SearchEngine searchEngine = new SearchEngine();
        searchEngine.parser("C:\\Users\\Dell\\Desktop\\Wikipedia Data Sample\\Wikipedia Data Sample\\wiki_00");
    }

    @Override
    public void indexWebPage(String filePath) {

    }

    @Override
    public void indexDirectory(String directoryPath) {

    }

    @Override
    public void deleteWebPage(String filePath) {

    }

    @Override
    public List<ISearchResult> searchByWordWithRanking(String word) {
        return null;
    }

    @Override
    public List<ISearchResult> searchByMultipleWordWithRanking(String sentence) {
        return null;
    }
}
