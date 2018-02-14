package org.hssus.khel.hsskhel.util;

import java.util.List;

/**
 * Created by prasadkhandat on 1/3/18.
 */

public class Util {
    
    public static boolean isNotNullAndNotEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    public static boolean isNotNullAndNotEmpty(List list) {
        return list != null && !list.isEmpty();
    }
}
