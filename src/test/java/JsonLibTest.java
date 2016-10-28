import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.factory.DaoFactory;
import lreis.bigdata.indoor.utils.TraceUtils;
import lreis.bigdata.indoor.vo.SemStop;
import net.sf.json.JSONArray;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by dq on 10/6/16.
 */
public class JsonLibTest {


    @Test
    public void testJSONArray() {

        IPOIDao dao = DaoFactory.getPhoenixPOIDao();

        List<SemStop> list = null;
        try {
            list = dao.getStops("00037F000000");
            TraceUtils.fixTrace(list);


//            JSONArray arr = JSONArray.fromObject(list);
            JSONArray arr = new JSONArray();
            arr.addAll(list);


            System.out.println(arr.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


}
