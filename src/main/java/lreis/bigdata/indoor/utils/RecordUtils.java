package lreis.bigdata.indoor.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Q on 2016/6/21.
 */
public class RecordUtils {

    public static long calcTimeStamp(String strTime) throws ParseException {

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long time = sdf.parse(strTime).getTime();
        return time;


    }

}
