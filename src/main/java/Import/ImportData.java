package Import;

import org.neo4j.driver.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * Description: <br/>
 * date: 2021/1/5 22:06<br/>
 *
 * @author HaoNanWang<br />
 * @since JDK 11
 */
public class ImportData implements AutoCloseable {
    private final Driver driver;

    public ImportData(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void importEntity(File entityFile) {
        String fileName = entityFile.getName();
        String[] splitName = fileName.split("_");
        if (splitName.length != 2) {
            System.out.println("文件命名错误");
            return;
        }
        String entityName = splitName[0];
        String typeName = splitName[1];
        if (!typeName.startsWith("entity")) {
            System.out.println("文件命名错误，不确定是否为entity");
            return;
        }
        importDataForEntity(entityFile, entityName);
    }

    public void importRelation(File relationFile) {
        String fileName = relationFile.getName();
        String[] splitName = fileName.split("_");
        if (splitName.length != 2) {
            System.out.println("文件命名错误");
            return;
        }
        String relationName = splitName[0];
        String typeName = splitName[1];
        if (!typeName.startsWith("relation")) {
            System.out.println("文件命名错误，不确定是否为relation");
            return;
        }
        importDataForRelation(relationFile, relationName);
    }

    public void importDataForRelation(File relationFile, String relationName) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(relationFile.toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("读取csv文件错误，path：" + relationFile.getAbsolutePath());
            e.printStackTrace();
        }
        String header = lines.get(0);
        String headers[] = header.split(",");
        String label1 = headers[0].split("_")[0];
        String property1 = headers[0].split("_")[1];
        String label2 = headers[1].split("_")[0];
        String property2 = headers[1].split("_")[1];
        String cypher = "MATCH (e:" + label1 + "{" + property1 + ":\'value0\'}),(q:" + label2 + "{" + property2 + ":\'value1\'})" +
                "CREATE (e)-[r:" + relationName + "]->(q)";

        for (int transactionNum = 0; transactionNum <= lines.size() / 20; transactionNum++) {
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    for (int i = transactionNum * 20 + 1; i <= Math.min((transactionNum + 1) * 20, lines.size() - 1); i++) {

                        String[] array = lines.get(i).split(",");
                        String execute = cypher;
                        if(array.length<2){
                            continue;
                        }
                        for (int j = 0; j < 2; j++) {
                            execute = execute.replace("value" + j, array[j]);
                        }
                        System.out.println(i + "  " + execute);
                        tx.run(execute);
                    }
                    tx.commit();
                }
            }
        }
    }

    public void importDataForEntity(File entityFile, String entityName) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(entityFile.toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("读取csv文件错误，path：" + entityFile.getAbsolutePath());
            e.printStackTrace();
        }
        String header = lines.get(0);
        String headers[] = header.split(",");
        int propertyNum = headers.length;
        //+" property1:value, property2:value, property3:value} )";
        String cypher = "CREATE (:" + entityName + "{";
        for (int i = 0; i < propertyNum; i++) {
            if (i != 0) {
                cypher += ",";
            }
            cypher += headers[i] + ":value" + i;
        }
        cypher += "})";


        for (int transactionNum = 0; transactionNum <= lines.size() / 2000; transactionNum++) {
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    for (int i = transactionNum * 2000 + 1; i <= Math.min((transactionNum + 1) * 2000, lines.size() - 1); i++) {
                        String[] array = lines.get(i).split(",");
                        String execute = cypher;
                        for (int j = 0; j < propertyNum; j++) {
                            execute = execute.replace("value" + j, "'" + array[j] + "'");
                        }
                        tx.run(execute);
                    }
                    tx.commit();
                }
                System.out.println(transactionNum * 2000 + 1);
            }
        }
    }

    public void printGreeting(final String message) {
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {

                tx.run("LOAD CSV WITH HEADERS FROM \"file:///book_entity.csv\" AS line \n" +
                        "MERGE (p:book{id:line.id,name:line.name,classification:line.classification,date:line.date})");

                tx.commit();
            }
        }
    }

    public static void main(String... args) throws Exception {
        long startTime = System.currentTimeMillis();

        /*
        File folder = new File("C:\\Users\\11346\\AppData\\Local\\Neo4j\\Relate\\Data\\dbmss" +
                "\\dbms-d34df8d3-2b43-47d6-a934-3cff261ff771\\import\\");
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.getName().contains("_entity")) {
                System.out.println(file.getName());
                try (ImportData importData = new ImportData("bolt://localhost:7687", "neo4j", "root")) {
                    importData.importEntity(file);
                }
            }
        }

        for (File file : files) {
            if (file.getName().contains("_relation")) {
                System.out.println(file.getName());
                try (ImportData importData = new ImportData("bolt://localhost:7687", "neo4j", "root")) {
                    importData.importRelation(file);
                }
            }
        }


         */
        try (ImportData importData = new ImportData("bolt://localhost:7687", "neo4j", "root")) {
            importData.importEntity(new File("C:\\Users\\11346\\AppData\\Local\\Neo4j\\Relate\\Data\\dbmss" +
                    "\\dbms-d34df8d3-2b43-47d6-a934-3cff261ff771\\import\\year_entity.csv"));

            importData.importRelation(new File("C:\\Users\\11346\\AppData\\Local\\Neo4j\\Relate\\Data\\dbmss" +
                    "\\dbms-d34df8d3-2b43-47d6-a934-3cff261ff771\\import\\belongTo_relation.csv"));
        }


        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
    }
}