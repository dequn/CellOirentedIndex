package lreis.bigdata.indoor;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.factory.DaoFactory;
import lreis.bigdata.indoor.vo.POI;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Q on 2016/7/6.
 */
public class QueryEfficiencyTest {

    //2014-04-01 00:00:00 1396281600
    //2014-04-02 00:00:00 1396368000

    static String[] macs = {
            "ACF7F348B123",
            "C86F1D788244",
            "207D74003C28",
            "C46AB70F43AE",
            "2002AF13B070",
            "54E43A616296",
            "D46E5C70656E",
            "88CB871F5E7A",
            "54E43A599247",
            "54E43A10D04D",
            "2C282DD2F0A7",
            "18AF612A130F",
            "3423BA8B62C5",
            "9E8890B4722E",
            "F4F15A69CE2E",
            "78F5FDE2AB88",
            "3CD0F8985915",
            "80EA96546E17",
            "885395E2F7C6",
            "14F42AFBAFF8",
            "F8A45F102F85",
            "D022BE36E4E8",
            "189EFC7BCB76",
            "38484C78BBC3",
            "5C969D87D553",
            "5C0A5B5DF336",
            "F0F61C5335FC",
            "A0821F1B482D",
            "0C74C28504D5",
            "F4F15A6F5CBA",
            "D89E3F8E419C",
            "FC253F437072",
            "8C0EE3CAEBF5",
            "8C006D8D7B97",
            "80EA9656C704",
            "148FC67A16ED",
            "701124BB90EE",
            "90B9316D9165",
            "24AB81662A68",
            "CC08E0A60528",
            "4860BC7068D3",
            "88308A5F072C",
            "38484C9C0002",
            "E8D4E0B140D8",
            "50EAD6E5F4F6",
            "AC3C0B94328E",
            "581FAA28AD3A",
            "A0F45078398C",
            "980D2EA05C5B",
            "A0E4535DC279",
            "98D6BBBF428E",
            "848E0CC2AF75",
            "444C0C9F603C",
            "309BAD5D7935",
            "8CBEBEF40442",
            "80CF419F74D1",
            "6C3E6D284567",
            "000822343A6A",
            "D89E3F8ECB3F",
            "54724F3BF8B8",
            "38AA3CF23A79",
            "0C771A3DAD7B",
            "087A4C4359F4",
            "F0F61C7714D1",
            "145A0582ABC3",
            "00F4B964E830",
            "00E06F27FDF7",
            "98D6BBAA2A5B",
            "145A05D80261",
            "D0E140063E41",
            "0C3021B9D3B4",
            "34E2FD22885C",
            "90187CF219E0",
            "8C006DB6FFEE",
            "9803D8747D28",
            "FC253F02A735",
            "DC9B9CBA49AA",
            "B0358DFCAAE4",
            "28E14C1C640E",
            "48746E899F94",
            "78A3E4CBDB28",
            "BC72B183CEA6",
            "7C11BE716438",
            "ACF7F34919CF",
            "4C8D796458C4",
            "D022BEC28383",
            "A0EDCD369C06",
            "149FE8DA3A8B",
            "F8A45FFE98CF",
            "40A6D9DD1961",
            "48746E867D0D",
            "189EFC497A67",
            "2064320476C4",
            "847A88A167B7",
            "24DEC6CBFF20",
            "3CD0F86E9663",
            "D8969555F50B",
            "D8D1CBC20BA8",
            "EC89F586338F",
            "7CFADF5846C6"
    };

    static long beginTime = 1396396800;

    static long[] endTimes = new long[13];

    static {
        for (int i = 0; i < 13; i++) {
            endTimes[i] = new Timestamp(beginTime * 1000 + (i + 1) * 3600 * 1000)
                    .getTime();
        }

    }

    @Ignore
    @Test
    public void comparePOIInCells() {

        try {
            Writer writer = new FileWriter(new File("d:\\big_joy\\query_pois_in_cell.txt"));
            writer.write("cell,begin_time,end_time,hbase_time_consume," +
                    "postgre_time_consume,count_um\n");//mean

            ICellDao dao = DaoFactory.getPostgreCellDao();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void compareGetTrace() {
        try {
            Writer writer = new FileWriter(new File("d:\\big_joy\\get_trace.txt"));
            writer.write("mac,begin_time,end_time,hbase_time_consume," +
                    "hbase_trace_length," +
                    "postgre_time_consume,postgre_trace_length\n");

            IPOIDao hDao = DaoFactory.getHBasePOIDao();
            IPOIDao pDao = DaoFactory.getPostgrePOIDao();
            for (String mac : QueryEfficiencyTest.macs) {

                for (long endTime : QueryEfficiencyTest.endTimes) {
                    long tb = System.currentTimeMillis();
                    List<POI> list = hDao.getTraceByMac(mac, beginTime, endTime);
                    long te = System.currentTimeMillis();
                    writer.write(String.format("%s,%s,%s,%s", mac, new Timestamp
                            (beginTime * 1000).toString(), new Timestamp
                            (endTime * 1000).toString(), te - tb, list != null ? list
                            .size() : 0));

                    tb = System.currentTimeMillis();
                    list = pDao.getTraceByMac(mac, beginTime, endTime);
                    te = System.currentTimeMillis();

                    writer.write(String.format(",%s,%s\n", te - tb, list != null ? list
                            .size() : 0));
                }
            }
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
