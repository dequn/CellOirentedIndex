import org.junit.Test;

import java.sql.*;

/**
 * Created by zdq on 7/13/16.
 */
public class TestPhoenix {




    @Test
    public void ConnectionTest() throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        ResultSet rset = null;

        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        Connection con = DriverManager.getConnection("jdbc:phoenix:hadoop-master:2181");
        stmt = con.createStatement();


        stmt.executeUpdate("create table test (mykey VARCHAR not null primary key, mycolumn varchar)");
        stmt.executeUpdate("upsert into test values ('1','Hello')");
        stmt.executeUpdate("upsert into test values ('2','World!')");

        PreparedStatement statement = con.prepareStatement("select * from test");
        rset = statement.executeQuery();
        while (rset.next()) {
            System.out.println(rset.getString("mycolumn"));
        }
        statement.close();
        con.commit();
        con.close();
    }
}
