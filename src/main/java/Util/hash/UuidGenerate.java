package Util.hash;

import java.util.UUID;

public class UuidGenerate {
    public static String getUuid() {

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;

    }
}
