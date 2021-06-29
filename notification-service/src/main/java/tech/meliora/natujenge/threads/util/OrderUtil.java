package tech.meliora.natujenge.threads.util;

import tech.meliora.natujenge.threads.domain.Order;

public class OrderUtil {

    public static String generateSMS(Order order) {
        String message = "Dear " + order.getCustomerName() + ", your order id " + order.getId()
                + ", product: " + order.getProduct() + ", quantity: " + order.getQuantity()
                + ", total: " + order.getTotal()+". You can pay via M-PESA pay bill number XXXXXXX.";

        return message;
    }
}
