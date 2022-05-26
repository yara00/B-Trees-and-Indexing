import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SearchEngine implements ISearchEngine{
    class Int{
        public Integer x;
        Int(Integer x) {
            this.x = x;
        }
    }
    HashMap<String, IBTree> map = new HashMap<>();

    private NodeList parser(String filePath) {
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        NodeList nodeList = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName("doc");
        }
        catch (IOException | SAXException | ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        return nodeList;
    }

    private void indexPage(String filePath) {
        NodeList nodeList = parser(filePath);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element eElement = (Element) nodeList.item(i);
            String id = eElement.getAttribute("id");
            String content = eElement.getTextContent();
            String[] contentArr = content.split("\\W+");
            IBTree<String, Int> tree = new Tree(3);
            for(int c=0; c<contentArr.length; c++) {
                Int rank = (Int) tree.tempSearch(contentArr[c]);
                if(rank == null) tree.insert(contentArr[c], new Int(1));
                else rank.x++;
            }
            map.put(id, tree);
        }
    }
    private void indexDelete(String filePath) {
        NodeList nodeList = parser(filePath);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element eElement = (Element) nodeList.item(i);
            String id = eElement.getAttribute("id");
            map.remove(id);
        }
    }

    private List<ISearchResult> wordSearch(String word) {
        List<ISearchResult> resultList = new LinkedList<>();
        for(String key : map.keySet()) {
            ISearchResult result = new SearchResult();
            Int rank = (Int) map.get(key).tempSearch(word);
            if(rank != null) {
                result.setRank(rank.x);
                result.setId(key);
                resultList.add(result);
            }
        }
        return resultList;
    }
    @Override
    public void indexWebPage(String filePath) {
        indexPage(filePath);
    }

    @Override
    public void indexDirectory(String directoryPath) {
        File dir = new File(directoryPath);
        File[] directoryListing = dir.listFiles();
        for (File file : directoryListing) {
                indexPage(file.getPath());
        }
    }

    @Override
    public void deleteWebPage(String filePath) {
        indexDelete(filePath);
    }

    @Override
    public List<ISearchResult> searchByWordWithRanking(String word) {
        return wordSearch(word);
    }

    @Override
    public List<ISearchResult> searchByMultipleWordWithRanking(String sentence) {
        List<ISearchResult> resultList = new LinkedList<>();
        String[] wordArr = sentence.split("\\W+");
        for(String word : wordArr) {
            for(String key : map.keySet()) {
                ISearchResult result = new SearchResult();
                Int rank = (Int) map.get(key).tempSearch(word);
                if(rank != null) {
                    result.setRank(rank.x);
                    result.setId(key);
                    resultList.add(result);
                }
            }
        }
        return resultList;
    }

    public static void main(String[] args) {
        SearchEngine searchEngine = new SearchEngine();
        searchEngine.parser("C:\\Users\\Dell\\Desktop\\Wikipedia Data Sample\\Wikipedia Data Sample\\wiki_00");
        searchEngine.indexWebPage("C:\\Users\\Dell\\Desktop\\Wikipedia Data Sample\\Wikipedia Data Sample\\wiki_00");
        List<ISearchResult> res= new LinkedList<>();
      // res = searchEngine.wordSearch("The");
        res = searchEngine.searchByMultipleWordWithRanking("Konica Minolta");
        System.out.println("test");

    }
}
