package demo;

import java.util.HashMap;
import java.util.Map;

import com.example.OrderInfo;

import org.kie.api.runtime.KieRuntime;
import org.kie.api.runtime.process.ProcessContext;

/**
 * OFDemoInit
 */
public class OFDemoInit {

    private static String processId = "OrderManagement";

    // demo.OFDemoInit.initDemo(kcontext);
    public static void initDemo(ProcessContext kcontext) {
        KieRuntime runtime = kcontext.getKieRuntime();
        
        Map<String, Object> params = new HashMap<>();

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setItem("Phone Huawei P10");
        orderInfo.setUrgency("low");
        params.put("orderInfo", orderInfo);
        runtime.startProcess(processId, params);
        orderInfo.setItem("Laptop Dell XPS 15");
        orderInfo.setUrgency("low");
        runtime.startProcess(processId, params);
    }
}