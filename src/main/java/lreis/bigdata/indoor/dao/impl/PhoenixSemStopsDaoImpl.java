package lreis.bigdata.indoor.dao.impl;

import com.vividsolutions.jts.geom.Geometry;
import lreis.bigdata.indoor.dao.ISemStopsDao;
import lreis.bigdata.indoor.dbc.IConnection;
import lreis.bigdata.indoor.vo.SemStop;
import lreis.bigdata.indoor.vo.SemanticCell;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by dq on 10/8/16.
 */
public class PhoenixSemStopsDaoImpl implements ISemStopsDao {

    public IConnection conn;


    public PhoenixSemStopsDaoImpl(IConnection conn) {
        this.conn = conn;
    }


    @Override
    public void upsert(String mac, List<SemStop> stops) throws SQLException, ClassNotFoundException {

        if (stops == null || stops.size() == 0) {
            return;
        }

        String sql = String.format("UPSERT INTO BIGJOY.STOPS(mac,sem_cell,entry_time,exit_time)  VALUES ('%s',?,?,?)", mac);

        PreparedStatement pstmt = this.conn.getConnection().prepareStatement(sql);

        for (SemStop stop : stops) {
            pstmt.setString(1, stop.getPolygonNum());
            pstmt.setLong(2, stop.getEntryTime());
            pstmt.setLong(3, stop.getExitTime());
            pstmt.executeUpdate();

        }

        this.conn.getConnection().commit();
        this.conn.getConnection().close();

    }


    @Override
    public void upsertTraj(String mac, String date, List<SemStop> stops) throws SQLException, ClassNotFoundException {


        String sql = String.format("UPSERT INTO BIGJOY.TRAJS(mac,length,day,traj) VALUES ('%s',?,?,?)", mac);

        PreparedStatement pstmt = this.conn.getConnection().prepareStatement(sql);


        JsonConfig config = new JsonConfig();
        config.setJsonPropertyFilter(new PropertyFilter() {
            @Override
            public boolean apply(Object o, String s, Object o1) {
                return o instanceof SemanticCell && !s.equalsIgnoreCase("name");
            }
        });
        JSONArray arr = JSONArray.fromObject(stops.toArray(), config);
        pstmt.setString(1, date);
        pstmt.setInt(2, stops.size());
        pstmt.setString(3, arr.toString());
        pstmt.executeUpdate();

        this.conn.getConnection().commit();


    }


}
