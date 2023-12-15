package my.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Employee> list = parseXML("src/dataFiles/data.xml");
        String json = listToJson(list);
        jsonToFail(json, "src/dataFiles/data.json");
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list);
    }

    private static void jsonToFail(String json, String jsonNameFile) {
        try (FileWriter fileWriter = new FileWriter(jsonNameFile)) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Employee> parseXML(String pathFile) {
        Employee employee = new Employee();
        List<Employee> employeesList = new ArrayList<>();
        Document doc;
        try {
            doc = buildDocument(pathFile);
        } catch (Exception e) {
            System.out.println("Ошибка парсинга" + e.toString());
            return null;
        }

        // получить первый дочерний элемент в документе
        Node rootNode = doc.getFirstChild();
        // получение из rootNode все Дочерние узлы(getChildNodes())
        NodeList rootChilds = rootNode.getChildNodes();
        System.out.println("<------------->");
        Node employeeNode = null;

        for (int i = 0; i < rootChilds.getLength(); i++) {
            if (rootChilds.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (!rootChilds.item(i).getNodeName().equals("employee")) {
                continue;
            }
            employeeNode = rootChilds.item(i);
            //
            if (employeeNode == null) { // если пусто то уходим
                return null;
            }

            NodeList employeeChilds = employeeNode.getChildNodes();
            long id = 0;
            String firstName = "";
            String lastName = "";
            String country = "";
            int age = 0;
            for (int j = 0; j < employeeChilds.getLength(); j++) {
                if (employeeChilds.item(j).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                switch (employeeChilds.item(j).getNodeName()) {
                    case "id": {
                        id = Long.valueOf(employeeChilds.item(j).getTextContent());
                        break;
                    }
                    case "firstName": {
                        firstName = employeeChilds.item(j).getTextContent();
                        break;
                    }
                    case "lastName": {
                        lastName = employeeChilds.item(j).getTextContent();
                        break;
                    }
                    case "country": {
                        country = employeeChilds.item(j).getTextContent();
                        break;
                    }
                    case "age": {
                        age = Integer.valueOf(employeeChilds.item(j).getTextContent());
                        break;
                    }
                }

            }
            employeesList.add(new Employee(id, firstName, lastName, country, age));
        }

        System.out.println(employeesList.toString()+"\n***************");
        return employeesList;
    }

    private static Document buildDocument(String pathFile) throws Exception {
        // ------------- открытие файла для парсинга
        File file = new File(pathFile);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        return dbf.newDocumentBuilder().parse(file);
    }
}