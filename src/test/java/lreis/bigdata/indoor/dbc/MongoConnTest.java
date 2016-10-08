package lreis.bigdata.indoor.dbc;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Filters.regex;
import static org.junit.Assert.*;

/**
 * Created by dq on 10/6/16.
 */
public class MongoConnTest {

    @Test
    public void getConnTest() throws Exception {

        MongoClient mongo = MongoConn.getConn();


        MongoDatabase db = mongo.getDatabase("bigjoy");

        MongoCollection table = db.getCollection("shop_semantic");


        String name = "baleno";

//        FindIterable<Document> iterator = table.find(doc);
        FindIterable<Document> iterator = table.find(or(regex("name", name, "i"), regex("name_cn", name, "i"), regex("name_en", name, "i")));


        List<Document> list = new ArrayList<>();
        iterator.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {

                list.add(document);
                System.out.println(document
                );
            }
        });

        System.out.println(list);


    }


    @Test
    public void updateTest() {

        MongoClient mongo = MongoConn.getConn();
        MongoDatabase db = mongo.getDatabase("bigjoy");
        MongoCollection table = db.getCollection("shop_semantic");

        String name = "baleno";

        UpdateResult res = table.updateOne(or(regex("name", name, "i"), regex("name_cn", name, "i"), regex("name_en", name, "i")), new Document("$set", new Document("sem_cell", 20010032)));

        if (res.getModifiedCount() > 0) {
            System.out.println("update 20010032 ok.");
        }

    }

}