package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.dbc.PostgreConn;
import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.SemanticCell;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Q on 2016/7/5.
 */
public class PostgreCellDaoImpl implements ICellDao {


    private PostgreConn pConn = null;

    public PostgreCellDaoImpl(PostgreConn pConn) {
        this.pConn = pConn;
    }

    @Override
    public List<PositioningPoint> getPOIsByCell(SemanticCell semanticCell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {

        if (semanticCell == null || semanticCell.equals("")) {
            return null;
        }
        if (beginTimeStamp > endTimeStamp) {
            return null;
        }


        List<PositioningPoint> result = new ArrayList<PositioningPoint>();


        Statement stat = this.pConn.getConnection().createStatement();


        String sql = String.format("SELECT * FROM imo WHERE rowkey LIKE '%04d%%' AND " +
                "time BETWEEN '%s' AND '%s'", semanticCell.getPolygonNum(), new Timestamp
                (beginTimeStamp), new Timestamp(endTimeStamp));
        ResultSet rs = stat.executeQuery(sql);

        while (rs.next()) {
            PositioningPoint positioningPoint = new PositioningPoint();

            positioningPoint.setX(((float) rs.getInt("x") / 1000));
            positioningPoint.setY(((float) rs.getInt("y") / -1000));
            positioningPoint.setMac(rs.getString("mac"));
            positioningPoint.setFloorNum(rs.getString("floor"));
            positioningPoint.setTime(rs.getTimestamp("time").getTime());

            result.add(positioningPoint);
        }


        return result;
    }

    @Override
    public int countMacInCell(SemanticCell semanticCell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        List<PositioningPoint> positioningPoints = this.getPOIsByCell(semanticCell, beginTimeStamp, endTimeStamp);
        if (positioningPoints == null || positioningPoints.size() == 0) {
            return 0;
        }
        HashSet<String> macs = new HashSet<String>();

        for (PositioningPoint positioningPoint : positioningPoints) {
            macs.add(positioningPoint.getMac());
        }

        return macs.size();

    }

    @Override
    public List<PositioningPoint> getPOISBeenToAllCells(List<SemanticCell> semanticCells, Long beginTimeStamp, Long endTimeStamp) throws IOException {

        if (semanticCells == null || semanticCells.size() == 0) {
            return null;
        }

        List<PositioningPoint> result = null;


        return null;
    }
}
