package connector;

import com.roy.drisk.connector.http.HttpAsyncClient;
import com.roy.drisk.connector.http.HttpParam;
import com.roy.drisk.connector.http.HttpSyncClient;
import com.roy.drisk.connector.http.HttpUtil;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class HttpTest {
    @Test
    public void testHttp() throws IOException {
        HttpParam param = new HttpParam();
        param.setBodyType(HttpParam.BodyType.KEYVALUE);
        param.setMethod(HttpParam.Method.POST);
        param.setCharset("UTF-8");
        param.setUrl("http://172.16.49.91:15011/hjbpay/OURMPUB5/0015443.dow");
        Map<String, String> map = new HashMap<>();
        map.put("GWA.SYS_CNL", "SYS");
        map.put("GWA.BUS_CNL", "SYS");
        map.put("CAS_MBL_NO", "15116137640");
        map.put("START_TM", "");
        map.put("END_TM", "");
        map.put("SUCC_FLG", "");
        map.put("RES_MSG", "测试");
        map.put("AREA_CODE", "");
        param.setKeyValueData(map);

        HttpSyncClient client = DriskConnectorFactory.getHttpClient();
        String response = HttpUtil.sync(client, param);
        System.out.println(response);
    }

    @Test
    public void testAsyncHttp() throws Exception {
        HttpAsyncClient client = DriskConnectorFactory.getHttpAsyncClient();
        Future<HttpResponse> responseFuture = client.execute(new HttpPost("http://www.baidu.com"), null);
        int ret = responseFuture.get().getStatusLine().getStatusCode();
        Assert.assertEquals(ret, 302);
        client = DriskConnectorFactory.getHttpAsyncClient();
        responseFuture = client.execute(new HttpPost("http://www.baidu.com"), null);
        ret = responseFuture.get().getStatusLine().getStatusCode();
        Assert.assertEquals(ret, 302);
    }

    @After
    public void after() throws IOException {
        DriskConnectorFactory.close();
    }
}
