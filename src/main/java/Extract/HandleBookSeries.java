package Extract;

import Const.TableHead;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import javax.swing.text.Keymap;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description: <br/>
 * date: 2021/1/5 20:09<br/>
 *
 * @author HaoNanWang<br />
 * @since JDK 11
 */
public class HandleBookSeries {
    public static HashMap<String,String> map=new HashMap<>();
    public static ArrayList<String> relation=new ArrayList<>();
    public void extractBookSeries(CsvReader csvReader) {
        String id = "";
        String series = "";
        try {
            id = csvReader.get(0);
            series = csvReader.get(10).replaceAll("\\\\","");
        } catch (IOException e) {
            System.out.println("e:读取字段错误,isbn:" + id);
            e.printStackTrace();
        }
        if(null==series || series.trim().equalsIgnoreCase("")){
            return;
        }
        if (series.equalsIgnoreCase("series")) {
            return;
        }
        if(!map.containsKey(series)){
            map.put(series, "");
        }else{
            //todo 消歧
        }
        relation.add(id+"!"+series);
    }

    public void writeBookSeriesEntity(String outputPath){
        File bookSeriesEntityFile = new File(outputPath + "\\" + "bookSeries_entity.csv");
        if (bookSeriesEntityFile.exists()) {
            bookSeriesEntityFile.delete();
        }
        try {
            bookSeriesEntityFile.createNewFile();
        } catch (IOException e) {
            System.out.println("e:新建bookSeries实体bookSeries_entity失败");
            e.printStackTrace();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(bookSeriesEntityFile));
        } catch (IOException e) {
            System.out.println("e:创建bookSeries实体bufferWriter失败");
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

    public void writeSubBookOfRelation(String filePath) {
        File subBookOfRelationFile = new File(filePath + "\\" + "subBookOf_relation.csv");
        if (subBookOfRelationFile.exists()) {
            subBookOfRelationFile.delete();
        }
        try {
            subBookOfRelationFile.createNewFile();
        } catch (IOException e) {
            System.out.println("e:新建subBookOf关系subBookOf_relation失败");
            e.printStackTrace();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(subBookOfRelationFile));
        } catch (IOException e) {
            System.out.println("e:创建subBookOf关系bufferWriter失败");
            e.printStackTrace();
        }
        CsvWriter cWriter = new CsvWriter(writer, ',');
        try {
            cWriter.writeRecord("book_id,bookSeries_name".split(","), true);
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
