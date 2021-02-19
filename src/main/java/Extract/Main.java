package Extract;

import java.io.File;

/**
 * Description: <br/>
 * date: 2021/1/5 19:12<br/>
 *
 * @author HaoNanWang<br />
 * @since JDK 11
 */
public class Main {
    public static void main(String args[]) {
        ExtractFromExcel extractFromExcel = new ExtractFromExcel();
        extractFromExcel.extractFromExcel(new File("C:\\Users\\11346\\Desktop\\origin.csv"),
                "C:\\Users\\11346\\Desktop\\temp切分");
    }
}
