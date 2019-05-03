package script;

import com.example.OrderInfo;

import org.kie.api.runtime.process.ProcessContext;

/**
 * OrderManagementScript
 */
public class OrderManagementScript {

    public static void requestOfferExit(ProcessContext kcontext) {
        OrderInfo orderInfo = (OrderInfo) kcontext.getVariable("orderInfo");
        kcontext.setVariable("suppliersList", orderInfo.getSuppliersList());
    }
}