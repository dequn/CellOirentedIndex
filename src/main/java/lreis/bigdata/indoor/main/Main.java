package lreis.bigdata.indoor.main;


import lreis.bigdata.indoor.dbc.PhoenixConn;
import lreis.bigdata.indoor.factory.DbcFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dq on 10/14/16.
 */
public class Main {


    public static void main(String[] args) {


        try {
            countMacInMall();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static int countMacInMall() throws SQLException {


        PhoenixConn pConn = DbcFactory.getPhoenixConn();
        String sql = "SELECT COUNT(DISTINCT MAC) AS CONT FROM BIGJOY.STOPS WHERE entry_time > ? and entry_time < ? ";

        PreparedStatement pstmt = pConn.getConnection().prepareStatement(sql);

        long beingtime = 1396281600000L;

        for (int i = 0; i < 24; i++) {
            pstmt.setLong(1, beingtime);
            pstmt.setLong(2, beingtime + 60 * 60 * 1000);


            ResultSet rs = pstmt.executeQuery();

            rs.next();
            System.out.println(rs.getInt("cont"));

            beingtime += 60 * 60 * 1000;

        }


        return 0;

    }


}
