package demo;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.OrderInfo;
import com.example.SupplierInfo;
import com.thoughtworks.xstream.XStream;

import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.UserTaskService;
import org.jbpm.services.api.service.ServiceRegistry;
import org.kie.api.runtime.process.ProcessContext;

/**
 * OFDemoInit
 */
@SuppressWarnings("unchecked")
public class OFDemoInit {

    final static private int PROBABILITY = 60;
    private static final String userId = "Administrator";
    private static String processId = "OrderManagement";
    private static Random random = new Random(System.currentTimeMillis());
    private static String deploymentId = "order-management_1.1-SNAPSHOT";

    public static void startProcesses(ProcessContext kcontext) {
        ProcessService processService = (ProcessService) ServiceRegistry.get().service(ServiceRegistry.PROCESS_SERVICE);

        InputStream res = OFDemoInit.class.getClassLoader().getResourceAsStream("demo/order-info-list.xml");
        XStream xStream = new XStream();
        xStream.setClassLoader(OFDemoInit.class.getClassLoader());
        Collection<OrderInfo> list = (Collection<OrderInfo>) xStream.fromXML(res);

        Map<String, Object> params = new HashMap<>();
        List<Long> processInstanceList = new ArrayList<>(list.size());

        for (OrderInfo orderInfo : list) {
            params.clear();
            params.put("orderInfo", orderInfo);
            Long processInstanceId = processService.startProcess(deploymentId, processId, params);
            processInstanceList.add(processInstanceId);
        }

        kcontext.setVariable("processInstanceList", processInstanceList);
    }

    public static void performTasksRequestOffer(ProcessContext kcontext) {
        RuntimeDataService runtimeDataService = (RuntimeDataService) ServiceRegistry.get()
                .service(ServiceRegistry.RUNTIME_DATA_SERVICE);

        List<Long> processInstanceList = (List<Long>) kcontext.getVariable("processInstanceList");

        List<Long> piUpdated = new ArrayList<>();
        for (Long id : processInstanceList) {
            if (random.nextInt(100) < PROBABILITY) {
                piUpdated.add(id);
                List<Long> taskIdList = runtimeDataService.getTasksByProcessInstanceId(id);

                for (Long taskId : taskIdList) {
                    UserTaskService userTaskService = (UserTaskService) ServiceRegistry.get()
                            .service(ServiceRegistry.USER_TASK_SERVICE);

                    Map<String, Object> inputParams = userTaskService.getTaskInputContentByTaskId(taskId);
                    OrderInfo orderInfo = (OrderInfo) inputParams.get("orderInfo");
                    OrderInfo orderInfo2 = new OrderInfo();
                    orderInfo2.copy(orderInfo);
                    orderInfo2.setTargetPrice(60 * random.nextInt(10));
                    orderInfo2.setCategory("basic");
                    List<String> suppliers;
                    if (random.nextInt(1) == 0)
                        suppliers =  new ArrayList<>(Arrays.asList("supplier1", "supplier3"));
                    else
                        suppliers =  new ArrayList<>(Arrays.asList("supplier2", "supplier3"));

                    orderInfo2.setSuppliersList(suppliers);
                    
                    Map<String,Object> outputParams = new HashMap<>();
                    outputParams.put("orderInfo", orderInfo2);
                    userTaskService.completeAutoProgress(taskId, userId, outputParams);
                }
            }
        }
        kcontext.setVariable("processInstanceList", piUpdated);
    }

    public static void performTasksPrepareOffer(ProcessContext kcontext) {
        RuntimeDataService runtimeDataService = (RuntimeDataService) ServiceRegistry.get()
                .service(ServiceRegistry.RUNTIME_DATA_SERVICE);

        UserTaskService userTaskService = (UserTaskService) ServiceRegistry.get()
                .service(ServiceRegistry.USER_TASK_SERVICE);

        List<Long> processInstanceList = (List<Long>) kcontext.getVariable("processInstanceList");

        List<Long> piUpdated = new ArrayList<>();

        for (Long id : processInstanceList) {
            if (random.nextInt(100) < PROBABILITY) {
                piUpdated.add(id);
                List<Long> taskIdList = runtimeDataService.getTasksByProcessInstanceId(id);

                for (Long taskId : taskIdList) {
                    Map<String, Object> iomap = userTaskService.getTaskInputContentByTaskId(taskId);
                    OrderInfo orderInfo = (OrderInfo) iomap.get("orderInfo");
                    SupplierInfo supplierInfo = new SupplierInfo();
                    supplierInfo.setDeliveryDate(
                            new Date(LocalDateTime.now().plusDays(random.nextInt(15)).toEpochSecond(ZoneOffset.UTC)));
                    supplierInfo.setOffer(orderInfo.getTargetPrice() + 10 * random.nextInt(10));
                    supplierInfo.setUser((String) iomap.get("supplier"));
                    iomap.put("supplierInfo", supplierInfo);
                    userTaskService.completeAutoProgress(taskId, userId, iomap);
                }
            }
        }
        kcontext.setVariable("processInstanceList", piUpdated);
    }

    public static void main(String[] args) {
        // XStream xStream = new XStream();
        // xStream.setClassLoader(OFDemoInit.class.getClassLoader());
        // List<PerformTask> performTasks = new ArrayList<>();
        // performTasks.add(task);
        // System.out.println(xStream.toXML(performTasks));

        // BeanUtilsBean util = new BeanUtilsBean() {
        // @Override
        // public void copyProperty(Object obj, String name, Object value)
        // throws IllegalAccessException, InvocationTargetException {
        // if (value == null)
        // return;
        // if (value instanceof Integer && ((Integer) value).intValue() == 0)
        // return;
        // if (value instanceof Long && ((Long) value).longValue() == 0)
        // return;
        // if (value instanceof Double && ((Double) value).doubleValue() == 0)
        // return;
        // super.copyProperty(obj, name, value);
        // }
        // };
    }

}