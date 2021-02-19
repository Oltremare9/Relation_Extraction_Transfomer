package Extract;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description: <br/>
 * date: 2021/1/5 20:25<br/>
 *
 * @author HaoNanWang<br />
 * @since JDK 11
 */
public class HandleInstitute {
    public static HashMap<String, String> map = new HashMap<>();
    public static ArrayList<String> relation = new ArrayList<>();

    public void extractInstitute(CsvReader csvReader) {
        String id = "";
        String publishment = "";
        try {
            id = csvReader.get(0);
            publishment = csvReader.get(4);
        } catch (IOException e) {
            System.out.println("e:读取字段错误,isbn:" + id);
            e.printStackTrace();
        }
        if (publishment.equalsIgnoreCase("publishment")) {
            return;
        }
        if (!map.containsKey(publishment)) {
            map.put(publishment, "");
        } else {
            //todo 消歧
        }
        relation.add(publishment + "!" + id);
    }

    public void writeInstituteEntity(String outputPath) {
        File instituteEntityFile = new File(outputPath + "\\" + "institute_entity.csv");
        if (instituteEntityFile.exists()) {
            instituteEntityFile.delete();
        }
        try {
            instituteEntityFile.createNewFile();
        } catch (IOException e) {
            System.out.println("e:新建institute实体institute_entity失败");
            e.printStackTrace();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(instituteEntityFile));
        } catch (IOException e) {
            System.out.println("e:创建institute实体bufferWriter失败");
            e.printStackTrace();
        }
        CsvWriter cWriter = new CsvWriter(writer, ',');
        try {
            cWriter.writeRecord("name".split(","), true);
        } catch (IOException e) {
            System.out.println("e:写入表头失败");
            e.printStackTrace();
        }
        for (HashMap.Entry<String, String> entry : map.entrySet()) {
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

    public void writePublishRelation(String filePath) {
        File publishRelationFile = new File(filePath + "\\" + "publish_relation.csv");
        if (publishRelationFile.exists()) {
            publishRelationFile.delete();
        }
        try {
            publishRelationFile.createNewFile();
        } catch (IOException e) {
            System.out.println("e:新建publish关系publish_relation失败");
            e.printStackTrace();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(publishRelationFile));
        } catch (IOException e) {
            System.out.println("e:创建publish关系bufferWriter失败");
            e.printStackTrace();
        }
        CsvWriter cWriter = new CsvWriter(writer, ',');
        try {
            cWriter.writeRecord("institute_name,book_id".split(","), true);
        } catch (IOException e) {
            System.out.println("e:写入表头失败");
            e.printStackTrace();
        }
        for (String relation : relation) {
            String[] a = relation.split("!");
            String mapKey = a[0];
            String mapValue = a[1];
            System.out.println(mapKey + ":" + mapValue);
            try {
                cWriter.writeRecord((mapKey + "," + mapValue).split(","), true);
            } catch (IOException e) {
                System.out.println("e:写入数据失败+data:" + mapKey + "," + mapValue);
                e.printStackTrace();
            }
            cWriter.flush();//刷新数据
        }
    }
}
