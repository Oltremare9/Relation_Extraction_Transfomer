package ExtractFromTuple;

import com.csvreader.CsvWriter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CsvOperation {
    /**
     *
     * @param file
     * @param map
     * @param type 1为entity 2为relation
     */
    public static void writeCSV(String file, HashMap<String, String> map,int type) {
        File outFile = new File(file);//输出的CSV文件
        if (outFile.exists()) {
            outFile.delete();
        }
        try {
            outFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            CsvWriter cwriter = new CsvWriter(writer, ',');
            if(type==1){
                cwriter.writeRecord("name,id".split(","), true);
            }else if(type==2){
                cwriter.writeRecord("id1,id2".split(","), true);
            }
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String mapKey = entry.getKey();
                String mapValue = entry.getValue();
                System.out.println(mapKey + ":" + mapValue);
                //第一个参数表示要写入的字符串数组，每一个元素占一个单元格，第二个参数为true时表示写完数据后自动换行
                cwriter.writeRecord((mapKey + "," + mapValue).split(","), true);
                cwriter.flush();//刷新数据
            }
            cwriter.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
