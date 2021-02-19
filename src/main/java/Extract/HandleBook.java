package Extract;

import Const.TableHead;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Description: <br/>
 * date: 2021/1/5 19:29<br/>
 *
 * @author HaoNanWang<br />
 * @since JDK 11
 */
public class HandleBook {
    public static ArrayList<String> list = new ArrayList<>();

    public void extractBook(CsvReader csvReader) {
        String id = "";
        String classification = "";
        String date = "";
        String name = "";
        TableHead s=TableHead.ISBN;
        try {
            id = csvReader.get(0);
            name = csvReader.get(1).replaceAll("\\\\","");
            classification = csvReader.get(7);
            date = csvReader.get(5);
        } catch (IOException e) {
            System.out.println("e:读取字段错误,isbn:" + id);
            e.printStackTrace();
        }
        if(name.equalsIgnoreCase("books")){
            return;
        }
        list.add(id + "$" + name + "$" + classification + "$" + date);
    }

    public void writeBookEntity(String outputPath){
        File bookEntityFile = new File(outputPath + "\\" + "book_entity.csv");
        if (bookEntityFile.exists()) {
            bookEntityFile.delete();
        }
        try {
            bookEntityFile.createNewFile();
        } catch (IOException e) {
            System.out.println("e:新建book实体book_entity失败");
            e.printStackTrace();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(bookEntityFile));
        } catch (IOException e) {
            System.out.println("e:创建book实体bufferWriter失败");
            e.printStackTrace();
        }
        CsvWriter cWriter = new CsvWriter(writer, ',');
        try {
            cWriter.writeRecord("id,name,classification,date".split(","), true);
        } catch (IOException e) {
            System.out.println("e:写入表头失败");
            e.printStackTrace();
        }
        for (String bookEntity:list) {
            try {
                cWriter.writeRecord((bookEntity).replaceAll("\\$",",").split(","), true);
            } catch (IOException e) {
                System.out.println("e:写入数据失败+key:" + bookEntity);
                e.printStackTrace();
            }
            cWriter.flush();//刷新数据
        }
    }

}
