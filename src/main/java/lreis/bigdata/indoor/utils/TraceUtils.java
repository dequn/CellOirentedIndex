package lreis.bigdata.indoor.utils;

import lreis.bigdata.indoor.vo.SemStop;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by dq on 10/5/16.
 */
public class TraceUtils {


    // if custome in a semantic's time is less than 1 minute , we can treat it as a error.
    public static final int MIN_TIME_SPAN = 60 * 1000;


    public static Set<String> MEANING_LESS_NAMES = new HashSet<String>();

    static {
        MEANING_LESS_NAMES.add("自动扶梯");
        MEANING_LESS_NAMES.add("电梯");
        MEANING_LESS_NAMES.add("过道");
        MEANING_LESS_NAMES.add("未开放区域");
        MEANING_LESS_NAMES.add("卫生间");
        MEANING_LESS_NAMES.add("楼梯");
        MEANING_LESS_NAMES.add("天井");
        MEANING_LESS_NAMES.add("出入口");
        MEANING_LESS_NAMES.add("问讯处");
        MEANING_LESS_NAMES.add("收银台");

    }


    public static void fixTrace(List<SemStop> trace) {

        if (trace == null) {
            return;
        }

        for (int i = 1; i < trace.size(); ) {
            SemStop nodeI = trace.get(i);

            if (nodeI.getExitTime() - nodeI.getEntryTime() < MIN_TIME_SPAN) {
                if (i < trace.size() - 1 && trace.get(i + 1).getPolygonNum().equals(trace.get(i - 1).getPolygonNum())) {
                    trace.get(i - 1).setExitTime(trace.get(i + 1).getExitTime());
                    trace.remove(i + 1);
                    trace.remove(i);
                } else {
                    trace.get(i - 1).setExitTime(trace.get(i).getExitTime());
                    trace.remove(i);
                }

            } else {
                i++;
            }

        }


        if (trace.size() == 1 && trace.get(0).getExitTime() - trace.get(0).getEntryTime() < MIN_TIME_SPAN) {
            trace.remove(0);
        }

        removeMeaningLessStops(trace);

    }


    public static void removeMeaningLessStops(List<SemStop> trace) {

        Iterator<SemStop> iterator = trace.iterator();

        while (iterator.hasNext()) {
            SemStop stop = iterator.next();
            if (MEANING_LESS_NAMES.contains(stop.getSemanticCell().getName())) {
                iterator.remove();
            }
        }


    }


}
