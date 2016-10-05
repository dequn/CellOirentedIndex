package lreis.bigdata.indoor.utils;

import lreis.bigdata.indoor.vo.TraceNode;
import org.apache.commons.math.stat.descriptive.rank.Min;

import java.util.List;

/**
 * Created by dq on 10/5/16.
 */
public class TraceUtils {


    // if custome in a semantic's time is less than 1 minute , we can treat it as a error.
    public static final int MIN_TIME_SPAN = 60 * 1000;

    public static void fixTrace(List<TraceNode> trace) {

        if (trace == null) {
            return;
        }

        for (int i = 1; i < trace.size(); i++) {
            TraceNode nodeI = trace.get(i);

            if (nodeI.getExitTime() - nodeI.getEntryTime() < MIN_TIME_SPAN) {
                if (i < trace.size() - 1 && trace.get(i + 1).getPolygonNum().equals(trace.get(i - 1).getPolygonNum())) {
                    trace.get(i - 1).setExitTime(trace.get(i + 1).getExitTime());
                    trace.remove(i);
                    trace.remove(i + 1);
                } else {
                    trace.get(i - 1).setExitTime(trace.get(i).getExitTime());
                    trace.remove(i);
                }

            }

        }

        if (trace.size() == 1 && trace.get(0).getExitTime() - trace.get(0).getEntryTime() < MIN_TIME_SPAN) {
            trace.remove(0);
        }

    }
}
