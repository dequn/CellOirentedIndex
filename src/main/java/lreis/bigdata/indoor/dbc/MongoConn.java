package lreis.bigdata.indoor.dbc;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec;

/**
 * Created by dq on 10/6/16.
 */
public class MongoConn {


    public static final String HOST = "192.168.6.131";

    public static final int PORT = 27017;

    public static final String DATABASE = "bigjoy";
    public static final String COLLECTION = "shop_semantic";

    public static MongoClient getConn() {
        return new MongoClient(HOST, PORT);
    }


    public static MongoCollection getShopSemanticCollection() {
        MongoClient mongo = MongoConn.getConn();
        MongoDatabase db = mongo.getDatabase(DATABASE);
        MongoCollection collection = db.getCollection(COLLECTION);

        return collection;
    }


}
