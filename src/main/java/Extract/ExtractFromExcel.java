package Extract;

import com.csvreader.CsvReader;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Description: 从excel中读取特定字段 进行组合<br/>
 * date: 2021/1/5 14:48<br/>
 *
 * @author HaoNanWang<br />
 * @since JDK 11
 */
public class ExtractFromExcel {
    /**
     * 处理概念concept
     */
    HandleConcept handleConcepts = new HandleConcept();

    HandleAuthor handleAuthor = new HandleAuthor();

    HandleBook handleBook = new HandleBook();

    HandleBookSeries handleBookSeries = new HandleBookSeries();

    HandleInstitute handleInstitute = new HandleInstitute();

    HandleCity handleCity = new HandleCity();

    HandleDate handleDate = new HandleDate();


    public void extractFromExcel(File excelFile, String outputPath) {
        try {
            int count=10001;
            int i=0;
            CsvReader csvReader = new CsvReader(excelFile.getAbsolutePath(), ',', Charset.forName("UTF-8"));
            while (i++<count) {
                try {
                    if (!csvReader.readRecord()) {
                        break;
                    }
                } catch (IOException e) {
                    System.out.println("e:文件IO读取错误");
                    e.printStackTrace();
                }
                handleBook.extractBook(csvReader);
//
                handleConcepts.extractConcepts(csvReader);
                handleAuthor.extractAuthor(csvReader);

                handleBookSeries.extractBookSeries(csvReader);
                handleInstitute.extractInstitute(csvReader);
                handleCity.extractCity(csvReader);
                handleDate.extractDate(csvReader);


            }

            handleConcepts.writeConceptsEntity(outputPath);
            handleConcepts.writeConceptsRelation(outputPath);


            handleAuthor.writeNationEntity(outputPath);
            handleAuthor.writeAuthorsEntity(outputPath);
            handleAuthor.writeAssistRelation(outputPath);
            handleAuthor.writeHumanOfRelation(outputPath);
            handleAuthor.writeWriteRelation(outputPath);



            handleBook.writeBookEntity(outputPath);


            handleBookSeries.writeBookSeriesEntity(outputPath);
            handleBookSeries.writeSubBookOfRelation(outputPath);

            handleInstitute.writeInstituteEntity(outputPath);
            handleInstitute.writePublishRelation(outputPath);

            handleCity.writeCityEntity(outputPath);
            handleCity.writeLocateInRelation(outputPath);

            handleDate.writeMonthEntity(outputPath);
            handleDate.writeYearEntity(outputPath);
            handleDate.writePublishMonthRelation(outputPath);
            handleDate.writePublishYearRelation(outputPath);



        } catch (FileNotFoundException e) {
            System.out.println("e:未找到指定excel文件");
            e.printStackTrace();
        }

    }


}
