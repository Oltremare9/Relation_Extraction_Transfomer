package Const;

/**
 * Description: <br/>
 * date: 2021/1/5 16:35<br/>
 *
 * @author HaoNanWang<br />
 * @since JDK 11
 */
public enum TableHead {
    AUTHOR("author"),
    ISBN("isbn"),
    PUBLISHMENT("publishment"),
    DATE("date");

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    private TableHead(String value) {
        this.value = value;
    }


}
