import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dq on 4/25/16.
 */
public class SemanticCellTest {

    @Test
    public void getName() throws Exception {

    }

    @Test
    public void setName() throws Exception {

    }

    @Test
    public void getNodeNum() throws Exception {

    }

    @Test
    public void setNodeNum() throws Exception {

    }

    @Test
    public void getFloorNum() throws Exception {

    }

    @Test
    public void setFloorNum() throws Exception {

    }

    @Test
    public void getCategory() throws Exception {

    }

    @Test
    public void setCategory() throws Exception {

    }

    @Test
    public void getGeom() throws Exception {

    }

    @Test
    public void setGeom() throws Exception {

    }


    @Test
    public void test() {

        Set<String> MEANING_LESS_NAMES = new HashSet<String>();

        try (BufferedReader reader = new BufferedReader(new FileReader("/resources/MEANING_LESS_NAMES"))) {


            String line = null;
            while ((line = reader.readLine()) != null) {
                MEANING_LESS_NAMES.add(line.trim());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}