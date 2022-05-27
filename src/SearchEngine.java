import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
// key --> word
// hashmap --> value (id --> key rank --> value)
public class SearchEngine implements ISearchEngine{
    class Int{
        public Integer x;
        Int(Integer x) {
            this.x = x;
        }
    }

    IBTree<String, HashMap<String, Integer>> tree = new Tree(4);
    // parse document content
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

            for(int c=0; c<contentArr.length; c++) {
                HashMap<String,Integer> map = (HashMap<String, Integer>) tree.tempSearch(contentArr[c].toLowerCase());
                if(map == null) {
                    HashMap<String,Integer> newMap = new HashMap<>();
                    newMap.put(id, 1);
                    tree.insert(contentArr[c].toLowerCase(), newMap);
                }
                else map.merge(id, 1, (a,b) -> a + b);
            }
        }
    }
    private void indexDelete(String filePath) {
        NodeList nodeList = parser(filePath);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element eElement = (Element) nodeList.item(i);
            String id = eElement.getAttribute("id");
            String content = eElement.getTextContent();
            String[] contentArr = content.split("\\W+");

            for(int c=0; c<contentArr.length; c++) {
                HashMap<String, Integer> map = (HashMap<String, Integer>) tree.tempSearch(contentArr[c].toLowerCase());
                if (map != null) {
                    for (String key : map.keySet()) {
                        if (key.equals(id)) {
                            map.remove(key);
                            break;
                        }
                    }
                    // if (map.size() == 0) tree.delete(contentArr[c].toLowerCase());
                }
            }
        }
    }

    private List<ISearchResult> wordSearch(String word) {
        List<ISearchResult> resultList = new LinkedList<>();
        HashMap<String, Integer> map = (HashMap<String, Integer>) tree.tempSearch(word.toLowerCase());
        for(String key : map.keySet()) {
            ISearchResult result = new SearchResult();
            result.setRank(map.get(key));
            result.setId(key);
            resultList.add(result);
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
            HashMap<String, Integer> map = (HashMap<String, Integer>) tree.tempSearch(word.toLowerCase());
            for(String key : map.keySet()) {
                ISearchResult result = new SearchResult();
                result.setRank(map.get(key));
                result.setId(key);
                resultList.add(result);
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
        //  res = searchEngine.searchByMultipleWordWithRanking("Konica Minolta");
        // searchEngine.indexWebPage("C:\\Users\\Dell\\Desktop\\Wikipedia Data Sample\\Wikipedia Data Sample\\wiki_00");
        res = searchEngine.wordSearch("Konica");
        searchEngine.indexDelete("C:\\Users\\Dell\\Desktop\\Wikipedia Data Sample\\Wikipedia Data Sample\\wiki_00");
        System.out.println("test");

    }
}
