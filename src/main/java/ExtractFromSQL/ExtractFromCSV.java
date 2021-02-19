package ExtractFromSQL;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ExtractFromCSV {
    //concept实体类
    HashMap<String, String> concept = new HashMap<>();
    //belong关系
    ArrayList<String> relation = new ArrayList<>();
    //subBooks实体
    HashMap<String, String> subBooks = new HashMap<>();
    //subBooks_r关系
    ArrayList<String> subBooks_r = new ArrayList<>();

    HashMap<String, String> nations = new HashMap<>();
    ArrayList<String> nations_r = new ArrayList<>();

    HashMap<String, HashMap<String,String>> authors = new HashMap<>();
    ArrayList<String> authors_r_book = new ArrayList<>();
    //共同作者关系
    ArrayList<String> both_authors_r =new ArrayList<>();
    public void readFile(String csvFilePath) throws IOException {
        // 读取的CSV文件
        File inFile = new File(csvFilePath);
        //输出的CSV文件
        File conceptFile = new File("entities\\concept.csv");
        //输出的CSV文件
        File concept_rFile = new File("relations\\concept_r.csv");
        //系列丛书关系
        File subBooks_rFile = new File("relations\\subBooks_r.csv");

        File authors_r_books = new File("relations\\authors_r_books.csv");

        File nations_r_authors=new File("relations\\nations_r_authors.csv");

        File bothAuthors_r=new File("relations\\bothAuthors_r.csv");
        if (conceptFile.exists()) {
            conceptFile.delete();
        }
        try {
            conceptFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(conceptFile));
        CsvReader csvReader = new CsvReader(csvFilePath, ',', Charset.forName("UTF-8"));
        while (csvReader.readRecord()) {
            String isbn = csvReader.get(0);
            String concept[] = csvReader.get(12).split("－");
            //丛书
            String subBook = csvReader.get(10);
            //国籍 朝代信息
            String nation = csvReader.get(2);
            String otherAuthors = csvReader.get(8);
            doConcept(concept, isbn);
            doSubBooks(subBook, isbn);
            doAuthorsAndNation(nation, isbn);
            doOtherAuthors(otherAuthors, isbn);

        }
        doBothAuthors();

        CsvWriter cwriter = new CsvWriter(writer, ',');
        cwriter.writeRecord("name".split(","), true);
        for (HashMap.Entry<String, String> entry : this.concept.entrySet()) {
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            System.out.println(mapKey + ":" + mapValue);
            cwriter.writeRecord((mapKey).split(","), true);
            cwriter.flush();//刷新数据
        }

        writer.newLine();
        writer.close();


        BufferedWriter writer2 = new BufferedWriter(new FileWriter(concept_rFile));
        CsvWriter cwriter2 = new CsvWriter(writer2, ',');
        cwriter2.writeRecord("id,concept".split(","), true);
        for (String string : this.relation) {
            String[] a = string.split("!");
            String mapKey = a[0];
            String mapValue = a[1];
            System.out.println(mapKey + ":" + mapValue);
            //第一个参数表示要写入的字符串数组，每一个元素占一个单元格，第二个参数为true时表示写完数据后自动换行
            cwriter2.writeRecord((mapKey + "," + mapValue).split(","), true);
            cwriter2.flush();//刷新数据
        }
        writer2.newLine();
        writer2.close();


        BufferedWriter bufferedWriterSubBook = new BufferedWriter(new FileWriter(subBooks_rFile));
        CsvWriter writerSubBookRelation = new CsvWriter(bufferedWriterSubBook, ',');
        writerSubBookRelation.writeRecord("id,subBooks".split(","), true);
        for (String string : this.subBooks_r) {
            String[] a = string.split("!");
            String mapKey = a[0];
            String mapValue = a[1];
            System.out.println(mapKey + ":" + mapValue);
            //第一个参数表示要写入的字符串数组，每一个元素占一个单元格，第二个参数为true时表示写完数据后自动换行
            writerSubBookRelation.writeRecord((mapKey + "," + mapValue).split(","), true);
            writerSubBookRelation.flush();//刷新数据
        }
        bufferedWriterSubBook.newLine();
        bufferedWriterSubBook.close();


        BufferedWriter authorRWriter = new BufferedWriter(new FileWriter(authors_r_books));
        CsvWriter author_r_csvWriter = new CsvWriter(authorRWriter, ',');
        author_r_csvWriter.writeRecord("id,author".split(","), true);
        for (String string : this.authors_r_book) {
            String[] a = string.split("!");
            String mapKey = a[0];
            String mapValue = a[1];
            System.out.println(mapKey + ":" + mapValue);
            //第一个参数表示要写入的字符串数组，每一个元素占一个单元格，第二个参数为true时表示写完数据后自动换行
            author_r_csvWriter.writeRecord((mapKey + "," + mapValue).split(","), true);
            author_r_csvWriter.flush();//刷新数据
        }
        authorRWriter.newLine();
        authorRWriter.close();

        BufferedWriter nationWriter = new BufferedWriter(new FileWriter(nations_r_authors));
        CsvWriter nation_r_csvWriter = new CsvWriter(nationWriter, ',');
        nation_r_csvWriter.writeRecord("author,nation".split(","), true);
        for (String string : this.nations_r) {
            System.out.println(string);
            String[] a = string.split("!");
            if(a.length>1) {
                String mapKey = a[0];
                String mapValue = a[1];
                System.out.println(mapKey + ":" + mapValue);
                //第一个参数表示要写入的字符串数组，每一个元素占一个单元格，第二个参数为true时表示写完数据后自动换行
                nation_r_csvWriter.writeRecord((mapKey + "," + mapValue).split(","), true);
                nation_r_csvWriter.flush();//刷新数据
            }
        }
        nationWriter.newLine();
        nationWriter.close();

        BufferedWriter bothWriter = new BufferedWriter(new FileWriter(bothAuthors_r));
        CsvWriter bothAuthors_csvWriter = new CsvWriter(bothWriter, ',');
        nation_r_csvWriter.writeRecord("author1,author2".split(","), true);
        for (String string : this.both_authors_r) {
            String[] a = string.split("!");
            String mapKey = a[0];
            String mapValue = a[1];
            System.out.println(mapKey + ":" + mapValue);
            //第一个参数表示要写入的字符串数组，每一个元素占一个单元格，第二个参数为true时表示写完数据后自动换行
            bothAuthors_csvWriter.writeRecord((mapKey + "," + mapValue).split(","), true);
            bothAuthors_csvWriter.flush();//刷新数据
        }
        bothWriter.newLine();
        bothWriter.close();

        reader.close();

    }

    public void doConcept(String concept[], String isbn) {
        //concept处理
        for (String cpt : concept) {
            if (cpt.trim().equals("") || cpt.equals("topic")) {
                continue;
            }
            //如果有组合词
            if (cpt.contains("；")) {
                String[] temp = cpt.split("；");
                for (String string : temp) {
                    if (string.trim().equals("")) {
                        continue;
                    }
                    if (!this.concept.containsKey(string)) {
                        this.concept.put(string, "");
                    }
                    relation.add(isbn + "!" + string);
                }
            } else {
                if (!this.concept.containsKey(cpt)) {
                    this.concept.put(cpt, "");
                }
                relation.add(isbn + "!" + cpt);
            }

        }
        //concept处理结束
    }

    public void doSubBooks(String subBook, String isbn) {
        if (null == subBook || subBook.equals("") || subBook.equals("congshuming")) {
            return;
        }
        if (!this.subBooks.containsKey(subBook)) {
            this.subBooks.put(subBook, "");
        }
        this.subBooks_r.add(isbn + "!" + subBook);
    }

    public void doAuthorsAndNation(String nation, String isbn) {
        if (nation.contains("author") || nation.trim().equals("")) {
            return;
        }
        nation=nation.replaceAll(" ", "");
        String author = nation;
        if (nation.trim().startsWith("(")) {
            author = nation.substring(nation.indexOf(")")+1);
            nation = nation.substring(1);
            int index = nation.indexOf(")");
            nation = nation.substring(0, index);
            if (!this.nations.containsKey(nation)) {
                this.nations.put(nation, "");
            }
            this.nations_r.add(author + "!" + nation);
        }
        if(this.authors.containsKey(isbn)){
            this.authors.get(isbn).put(author, "");
        }else{
            this.authors.put(isbn, new HashMap<>());
            this.authors.get(isbn).put(author, "");
        }
        this.authors_r_book.add(isbn + "!" + author);
    }

    public void doOtherAuthors(String otherAuthors, String isbn) {
        if (null == otherAuthors || otherAuthors.trim().equals("")) {
            return;
        }
        String[] authors = otherAuthors.split(",");
        for (String tempAuthor : authors) {
            tempAuthor=tempAuthor.replaceAll(" ", "");
            if (tempAuthor.trim().length() == 1) {
                continue;
            }
            if (tempAuthor.contains("编")
                    || tempAuthor.contains("注")
                    || tempAuthor.contains("译")
                    || tempAuthor.contains("著")
                    || tempAuthor.contains("绘")
                    || tempAuthor.contains("朗诵")
                    || tempAuthor.contains("写")
                    || tempAuthor.contains("校")
                    || tempAuthor.contains("整理")
                    || tempAuthor.contains("点")
                    || tempAuthor.contains("图")
                    || tempAuthor.contains("陈述")) {
                continue;
            }
            doAuthorsAndNation(tempAuthor, isbn);
        }
    }

    public void doBothAuthors(){
        for(String key:this.authors.keySet()){
            HashMap<String,String> map= this.authors.get(key);
            if(map.size()==1){
                continue;
            }
            Iterator iterator1=map.entrySet().iterator();
            while(iterator1.hasNext()){
                Iterator iterator2=map.entrySet().iterator();
                HashMap.Entry<String,String> entry1= (HashMap.Entry<String, String>) iterator1.next();
                String author1=entry1.getKey();
                while(iterator2.hasNext()){
                    HashMap.Entry<String,String> entry2= (HashMap.Entry<String, String>) iterator2.next();
                    String author2=entry2.getKey();
                    if(!author1.equals(author2)){
                        this.both_authors_r.add(author1+"!"+author2);
                    }
                }

            }


        }
    }


    public static void main(String args[]) {
        ExtractFromCSV extractFromCSV = new ExtractFromCSV();
        try {
            extractFromCSV.readFile("C:\\Users\\11346\\Desktop\\data.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
