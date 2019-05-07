package demo;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.OrderInfo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.UserTaskService;
import org.jbpm.services.api.service.ServiceRegistry;
import org.kie.api.runtime.KieRuntime;
import org.kie.api.runtime.process.ProcessContext;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.model.Task;

/**
 * OFDemoInit
 */
public class OFDemoInit {

    final static private int PROBABILITY = 30;
    private static String processId = "OrderManagement";
    private static Random random = new Random(System.currentTimeMillis());

    // demo.OFDemoInit.initDemo(kcontext);
    public static void initDemo(ProcessContext kcontext) {
        KieRuntime runtime = kcontext.getKieRuntime();

        InputStream res = OFDemoInit.class.getClassLoader().getResourceAsStream("demo/order-info-list.xml");
        XStream xStream = new XStream();
        xStream.setClassLoader(OFDemoInit.class.getClassLoader());
        Collection<OrderInfo> list = (Collection<OrderInfo>) xStream.fromXML(res);

        Map<String, Object> params = new HashMap<>();
        List<Long> processInstanceList = new ArrayList<>(list.size());

        for (OrderInfo orderInfo : list) {
            params.clear();
            params.put("orderInfo", orderInfo);
            ProcessInstance processInstance = runtime.startProcess(processId, params);
            processInstanceList.add(processInstance.getId());
        }

        kcontext.setVariable("processInstanceList", processInstanceList);
        performTasksTest(kcontext);
    }

    public static void performTasksTest(ProcessContext kcontext) {
        RuntimeDataService runtimeDataService = (RuntimeDataService) ServiceRegistry.get()
                .service(ServiceRegistry.RUNTIME_DATA_SERVICE);

        UserTaskService userTaskService = (UserTaskService) ServiceRegistry.get()
                .service(ServiceRegistry.USER_TASK_SERVICE);

        List<Long> processInstanceList = (List<Long>) kcontext.getVariable("processInstanceList");

        for (Long id : processInstanceList) {
            if (random.nextInt(100) > PROBABILITY)
                break;

            List<Long> taskIdList = runtimeDataService.getTasksByProcessInstanceId(id);

            for (Long taskId : taskIdList) {
                userTaskService.claim(taskId, null);
            }
        }
    }

    public static void performTasks(ProcessContext kcontext) {
        RuntimeDataService runtimeDataService = (RuntimeDataService) ServiceRegistry.get()
                .service(ServiceRegistry.RUNTIME_DATA_SERVICE);

        UserTaskService userTaskService = (UserTaskService) ServiceRegistry.get()
                .service(ServiceRegistry.USER_TASK_SERVICE);

        List<Long> processInstanceList = (List<Long>) kcontext.getVariable("processInstanceList");

        for (Long id : processInstanceList) {
            if (random.nextInt(100) > PROBABILITY)
                break;

            List<Long> taskIdList = runtimeDataService.getTasksByProcessInstanceId(id);

            for (Long taskId : taskIdList) {
                userTaskService.claim(taskId, null);
            }
        }
    }

    public static void main(String[] args) {
        // XStream xStream = new XStream();
        // xStream.setClassLoader(OFDemoInit.class.getClassLoader());

        Map<String, Object> params = new HashMap<>();
        OrderInfo orderInfo = new OrderInfo();
        params.put("orderInfo", orderInfo);

        PerformTask task = new PerformTask();
        task.setName("Request Offer");
        task.setUser("adminUser");
        task.setLikelyhood(80);
        task.setParams(params);

        // List<PerformTask> performTasks = new ArrayList<>();
        // performTasks.add(task);
        // System.out.println(xStream.toXML(performTasks));

        // BeanUtilsBean util = new BeanUtilsBean() {
        //     @Override
        //     public void copyProperty(Object obj, String name, Object value)
        //             throws IllegalAccessException, InvocationTargetException {
        //         if (value == null)
        //             return;
        //         if (value instanceof Integer && ((Integer) value).intValue() == 0)
        //             return;
        //         if (value instanceof Long && ((Long) value).longValue() == 0)
        //             return;
        //         if (value instanceof Double && ((Double) value).doubleValue() == 0)
        //             return;
        //         super.copyProperty(obj, name, value);
        //     }
        // };
    }

    public static class PerformTask {
        private String name;
        private String user;
        private int likelyhood;
        private Map<String, Object> params;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the user
         */
        public String getUser() {
            return user;
        }

        /**
         * @param user the user to set
         */
        public void setUser(String user) {
            this.user = user;
        }

        /**
         * @return the likelyhood
         */
        public int getLikelyhood() {
            return likelyhood;
        }

        /**
         * @param likelyhood the likelyhood to set
         */
        public void setLikelyhood(int likelyhood) {
            this.likelyhood = likelyhood;
        }

        /**
         * @return the params
         */
        public Map<String, Object> getParams() {
            return params;
        }

        /**
         * @param params the params to set
         */
        public void setParams(Map<String, Object> params) {
            this.params = params;
        }

    }
}