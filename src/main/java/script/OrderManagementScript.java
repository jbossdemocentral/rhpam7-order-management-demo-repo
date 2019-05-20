package script;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.example.OrderInfo;
import com.example.SupplierInfo;

import org.kie.api.runtime.process.ProcessContext;

/**
 * OrderManagementScript
 */
@SuppressWarnings("unchecked")
public class OrderManagementScript {

    public static void requestOfferExit(ProcessContext kcontext) {
        OrderInfo orderInfo = (OrderInfo) kcontext.getVariable("orderInfo");
        kcontext.setVariable("suppliersList", orderInfo.getSuppliersList());
        kcontext.setVariable("suppliersInfoList", new ArrayList<SupplierInfo>());
    }

    public static void prepareOfferExit(ProcessContext kcontext) {
        List<SupplierInfo> suppliersInfoList = (List<SupplierInfo>) kcontext.getVariable("suppliersInfoList");
        SupplierInfo supplierInfo = (SupplierInfo) kcontext.getVariable("supplierInfo");

        System.out.println("supplier offer: "+supplierInfo.getOffer());

        suppliersInfoList.add(supplierInfo);
        kcontext.setVariable("suppliersInfoList",suppliersInfoList);
        suppliersInfoList.stream().min(Comparator.comparing(SupplierInfo::getOffer)).ifPresent(s -> {
            kcontext.setVariable("supplierInfo", s);
        });
    }

    // Auto Approval Rules
    public static void autoApprovalRulesEntry(ProcessContext kcontext) {
        OrderInfo orderInfo = (OrderInfo) kcontext.getVariable("orderInfo");
        SupplierInfo supplierInfo = (SupplierInfo) kcontext.getVariable("supplierInfo");
        orderInfo.setPrice(supplierInfo.getOffer());
        kcontext.setVariable("orderInfo", orderInfo);
    }
}