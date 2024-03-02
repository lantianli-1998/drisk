import com.roy.drisk.connector.config.ConnectorConfiger;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ServiceLoader;

/**
 * @author roy
 * @date 2021/11/3
 * @desc
 */
public class ServiceTest {

    @Test
    public void serviceTest(){
//        System.setProperty("drisk.env","sit");
//        ServiceLoader<ConnectorConfiger> servicesLoader = ServiceLoader.load(ConnectorConfiger.class);
//        for (ConnectorConfiger configer : servicesLoader) {
//            System.out.println(configer);
//        }
        System.out.println(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()));
    }
}
