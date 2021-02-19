package Extract;

import Const.TableHead;
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
 * date: 2021/1/5 20:58<br/>
 *
 * @author HaoNanWang<br />
 * @since JDK 11
 */
public class HandleDate {
    HashMap<String, String> year_Entity = new HashMap<>();
    HashMap<String, String> month_Entity = new HashMap<>();
    ArrayList<String> publishMonth_Relation = new ArrayList<>();
    ArrayList<String> publishYear_Relation = new ArrayList<>();

    public void extractDate(CsvReader csvReader) {
        String date = "";
        String isbn = "";
        try {
            isbn = csvReader.get(0);
            date = csvReader.get(5);
        } catch (IOException e) {
            System.out.println("e:读取字段错误,isbn:" + isbn);
            e.printStackTrace();
        }
        if (TableHead.DATE.name().equalsIgnoreCase(date)) {
            return;
        }
        String year = "";
        String month = "";
        if (date.trim().equalsIgnoreCase("")) {
            return;
        } else if (date.contains(".")) {
            String dates[] = date.split("\\.");
            System.out.println(isbn);
            year = dates[0];
            if (dates.length == 2) {
                month = dates[1];
            }
        } else {
            year = date;
        }
        if (!year.equalsIgnoreCase("")) {
            year_Entity.put(year, "");
            publishYear_Relation.add(year + "!" + isbn);
        }
        if (!month.equalsIgnoreCase("")) {
            month_Entity.put(month, "");
            publishMonth_Relation.add(month + "!" + isbn);
        }
    }

    public void writeYearEntity(String filePath) {
        {
            File yearEntityFile = new File(filePath + "\\" + "year_entity.csv");
            if (yearEntityFile.exists()) {
                yearEntityFile.delete();
            }
            try {
                yearEntityFile.createNewFile();
            } catch (IOException e) {
                System.out.println("e:新建year实体year_entity失败");
                e.printStackTrace();
            }
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(yearEntityFile));
            } catch (IOException e) {
                System.out.println("e:创建year实体bufferWriter失败");
                e.printStackTrace();
            }
            CsvWriter cWriter = new CsvWriter(writer, ',');
            try {
                cWriter.writeRecord("name".split(","), true);
            } catch (IOException e) {
                System.out.println("e:写入表头失败");
                e.printStackTrace();
            }
            for (HashMap.Entry<String, String> entry : year_Entity.entrySet()) {
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
    }

    public void writeMonthEntity(String filePath) {
        {
            File monthEntityFile = new File(filePath + "\\" + "month_entity.csv");
            if (monthEntityFile.exists()) {
                monthEntityFile.delete();
            }
            try {
                monthEntityFile.createNewFile();
            } catch (IOException e) {
                System.out.println("e:新建month实体month_entity失败");
                e.printStackTrace();
            }
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(monthEntityFile));
            } catch (IOException e) {
                System.out.println("e:创建month实体bufferWriter失败");
                e.printStackTrace();
            }
            CsvWriter cWriter = new CsvWriter(writer, ',');
            try {
                cWriter.writeRecord("name".split(","), true);
            } catch (IOException e) {
                System.out.println("e:写入表头失败");
                e.printStackTrace();
            }
            for (HashMap.Entry<String, String> entry : month_Entity.entrySet()) {
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
    }

    public void writePublishYearRelation(String filePath) {
        File publishYearRelationFile = new File(filePath + "\\" + "publishYear_relation.csv");
        if (publishYearRelationFile.exists()) {
            publishYearRelationFile.delete();
        }
        try {
            publishYearRelationFile.createNewFile();
        } catch (IOException e) {
            System.out.println("e:新建publishYear关系publishYear_relation失败");
            e.printStackTrace();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(publishYearRelationFile));
        } catch (IOException e) {
            System.out.println("e:创建publishYear关系bufferWriter失败");
            e.printStackTrace();
        }
        CsvWriter cWriter = new CsvWriter(writer, ',');
        try {
            cWriter.writeRecord("year_name,book_id".split(","), true);
        } catch (IOException e) {
            System.out.println("e:写入表头失败");
            e.printStackTrace();
        }
        for (String relation : publishYear_Relation) {
            String[] a = relation.split("!");
            String mapKey = a[0];
            String mapValue = a[1];
            System.out.println(mapKey + ":" + mapValue);
            //第一个参数表示要写入的字符串数组，每一个元素占一个单元格，第二个参数为true时表示写完数据后自动换行
            try {
                cWriter.writeRecord((mapKey + "," + mapValue).split(","), true);
            } catch (IOException e) {
                System.out.println("e:写入数据失败+data:" + mapKey + "," + mapValue);
                e.printStackTrace();
            }
            cWriter.flush();//刷新数据
        }
    }

    public void writePublishMonthRelation(String filePath) {
        File publishMonthRelationFile = new File(filePath + "\\" + "publishMonth_relation.csv");
        if (publishMonthRelationFile.exists()) {
            publishMonthRelationFile.delete();
        }
        try {
            publishMonthRelationFile.createNewFile();
        } catch (IOException e) {
            System.out.println("e:新建publishMonth关系publishMonth_relation失败");
            e.printStackTrace();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(publishMonthRelationFile));
        } catch (IOException e) {
            System.out.println("e:创建publishMonth关系bufferWriter失败");
            e.printStackTrace();
        }
        CsvWriter cWriter = new CsvWriter(writer, ',');
        try {
            cWriter.writeRecord("month_name,book_id".split(","), true);
        } catch (IOException e) {
            System.out.println("e:写入表头失败");
            e.printStackTrace();
        }
        for (String relation : publishMonth_Relation) {
            String[] a = relation.split("!");
            String mapKey = a[0];
            String mapValue = a[1];
            System.out.println(mapKey + ":" + mapValue);
            //第一个参数表示要写入的字符串数组，每一个元素占一个单元格，第二个参数为true时表示写完数据后自动换行
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
