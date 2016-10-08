package lreis.bigdata.indoor.dao;

import lreis.bigdata.indoor.vo.SemStop;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by dq on 10/8/16.
 */
public interface ISemStopsDao {

    void upsert(String mac, List<SemStop> stops) throws SQLException, ClassNotFoundException;


    void upsertTraj(String mac, String date, List<SemStop> stops) throws SQLException, ClassNotFoundException;
}
