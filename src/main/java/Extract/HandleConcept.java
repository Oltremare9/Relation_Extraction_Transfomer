package Extract;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description: 处理excel中概念<br/>
 * date: 2021/1/5 14:59<br/>
 *
 * @author HaoNanWang<br />
 * @since JDK 11
 */
public class HandleConcept {
    public static HashMap<String, String> concept_Entity = new HashMap<>();
    public static HashMap<String,String> write_Relation = new HashMap<>();

    public void extractConcepts(CsvReader csvReader) {
        String isbn = "";
        String[] concepts = new String[0];
        try {
            isbn = csvReader.get(0);
            concepts = csvReader.get(12).replaceAll("\\\\","").split("－");
        } catch (IOException e) {
            System.out.println("e:读取concept错误,isbn:" + isbn);
            e.printStackTrace();
        }
        for (String concept : concepts) {
            if (concept.trim().equals("") || concept.equals("topic")) {
                continue;
            }
            //如果有组合词
            if (concept.contains("；")) {
                String[] sun_concepts = concept.split("；");
                for (String sub_concept : sun_concepts) {
                    if (sub_concept.trim().equals("")) {
                        continue;
                    }
                    if (!concept_Entity.containsKey(sub_concept)) {
                        concept_Entity.put(sub_concept, "");
                    }
                    write_Relation.put(isbn + "!" + sub_concept,"");
                }
                //无组合词
            } else {
                if (!concept_Entity.containsKey(concept)) {
                    concept_Entity.put(concept, "");
                }
                write_Relation.put(isbn + "!" + concept,"");
            }

        }
    }

    public void writeConceptsEntity(String filePath) {
        File conceptEntityFile = new File(filePath + "\\" + "concept_entity.csv");
        if (conceptEntityFile.exists()) {
            conceptEntityFile.delete();
        }
        try {
            conceptEntityFile.createNewFile();
        } catch (IOException e) {
            System.out.println("e:新建concept实体concept_entity失败");
            e.printStackTrace();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(conceptEntityFile));
        } catch (IOException e) {
            System.out.println("e:创建concept实体bufferWriter失败");
            e.printStackTrace();
        }
        CsvWriter cWriter = new CsvWriter(writer, ',');
        try {
            cWriter.writeRecord("name".split(","), true);
        } catch (IOException e) {
            System.out.println("e:写入表头失败");
            e.printStackTrace();
        }
        for (HashMap.Entry<String, String> entry : concept_Entity.entrySet()) {
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            System.out.println(mapKey + ":" + mapValue);
            try {
                cWriter.writeRecord((mapKey).split(","), true);
            } catch (IOException e) {
                System.out.println("e:写入数据失败+key:" + mapKey);
                e.printStackTrace();
            }
            cWriter.flush();//刷新数据
        }
    }

    public void writeConceptsRelation(String filePath) {
        File conceptRelationFile = new File(filePath + "\\" + "belongTo_relation.csv");
        if (conceptRelationFile.exists()) {
            conceptRelationFile.delete();
        }
        try {
            conceptRelationFile.createNewFile();
        } catch (IOException e) {
            System.out.println("e:新建belongTo关系belongTo_relation失败");
            e.printStackTrace();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(conceptRelationFile));
        } catch (IOException e) {
            System.out.println("e:创建belongTo关系bufferWriter失败");
            e.printStackTrace();
        }
        CsvWriter cWriter = new CsvWriter(writer, ',');
        try {
            cWriter.writeRecord("book_id,concept_name".split(","), true);
        } catch (IOException e) {
            System.out.println("e:写入表头失败");
            e.printStackTrace();
        }
        for (HashMap.Entry<String, String> entry : write_Relation.entrySet()) {
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            System.out.println(mapKey + ":" + mapValue);
            try {
                cWriter.writeRecord((mapKey).split("!"), true);
            } catch (IOException e) {
                System.out.println("e:写入数据失败+key:" + mapKey);
                e.printStackTrace();
            }
            cWriter.flush();//刷新数据
        }
    }
}
