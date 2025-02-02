package com.zosh.service.impl;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.zosh.domain.PaymentMethod;
import com.zosh.domain.PaymentOrderStatus;
import com.zosh.dto.BookingDTO;
import com.zosh.dto.UserDTO;
import com.zosh.modal.PaymentOrder;
import com.zosh.payload.response.PaymentLinkResponse;
import com.zosh.repository.PaymentOrderRepository;
import com.zosh.service.PaymentService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;

//    @Value("${stripe.api.key}")
//    private String stripeSecretKey;
//
//    @Value("${razorpay.api.key}")
//    private  String razorpayAPIKey;
//
//    @Value("${razorpay.api.secret}")
//    private  String razorpayAPISecret;


    @Override
    public PaymentLinkResponse createOrder(UserDTO user,
                                           BookingDTO booking,
                                           PaymentMethod paymentMethod) throws RazorpayException {

        Long amount = (long) booking.getTotalPrice();

        PaymentOrder order = new PaymentOrder();
        order.setAmount(amount);
        order.setPaymentMethod(paymentMethod);
        order.setBookingId(booking.getId());
        order.setSalonId(booking.getSalonId());
        order.setUserId(user.getId()); // Add this line

        PaymentOrder  savedOrder = paymentOrderRepository.save(order);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        // for Razorpay
        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            PaymentLink payment = createRazorpayPaymentLink(user,
                    savedOrder.getAmount(),
                    savedOrder.getId());

            String paymentUrl = payment.get("short_url");
            String paymentUrlId = payment.get("id");
            paymentLinkResponse.setPaymentLink_url(paymentUrl);
            paymentLinkResponse.setGetPayment_Link_id(paymentUrlId);

            savedOrder.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(savedOrder);
        }

        // for stripe
        else {
            String paymentUrl = createStripePaymentLink(user,
                    savedOrder.getAmount(),
                    savedOrder.getId());
            paymentLinkResponse.setPaymentLink_url(paymentUrl);
            paymentOrderRepository.save(savedOrder);
        }
        return paymentLinkResponse;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {

        PaymentOrder paymentOrder  = paymentOrderRepository.findById(id).orElse(null);

        if(paymentOrder == null){
            throw new Exception("payment order not found");
        }
        return paymentOrder;
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {
        return paymentOrderRepository.findByPaymentLinkId(paymentId);

    }

    // These are two main method
    @Override
    public PaymentLink createRazorpayPaymentLink(UserDTO user,
                                                 Long Amount,
                                                 Long orderId) throws RazorpayException {

        Long amount = Amount*100;

        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_rPSVLRBqTAYREg","zWD1XYfaMoRvCqVg3mb5oMRA");
        JSONObject paymentLinkRequest = new JSONObject();

        paymentLinkRequest.put("amount",amount);
        paymentLinkRequest.put("currency","INR");

        JSONObject customer = new JSONObject();
        customer.put("name",user.getFullName());
        customer.put("email",user.getEmail());

        paymentLinkRequest.put("customer",customer);

        JSONObject notify = new JSONObject();
        notify.put("email",true);
        paymentLinkRequest.put("notify",notify);

        paymentLinkRequest.put("reminder_enable",true);

        //CallBack url means after successfull paymment where you want ot redirect your user
        paymentLinkRequest.put("callback_url","http://localhost:3000/payment-success"+orderId);

        paymentLinkRequest.put("callback_method","get");

        PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

        return paymentLink;
    }



    @Override
    public String createStripePaymentLink(UserDTO user, Long amount, Long orderId) {
        try {
//            Stripe.apiKey = stripeSecretKey;

            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://your-domain.com/success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl("https://your-domain.com/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount(amount * 100) // in cents
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Booking Payment")
                                                                    .build())
                                                    .build())
                                    .setQuantity(1L)
                                    .build())
                    .putMetadata("orderId", orderId.toString())
                    .build();

            Session session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create Stripe payment link", e);
        }
    }

    @Override
    public Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId)
            throws RazorpayException {
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {
                RazorpayClient razorpay = new RazorpayClient("rzp_test_rPSVLRBqTAYREg", "zWD1XYfaMoRvCqVg3mb5oMRA");
                Payment payment = razorpay.payments.fetch(paymentId);
                Integer amount = payment.get("amount");
                String status = payment.get("status");

                if (status.equals("captured")) {
                    // here when we will integrate kafka then will trigger booking status..
                    // and some other task like notification
                    // Update payment order status to success
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    paymentOrderRepository.save(paymentOrder);
                    return true;
                } else {
                    // Update payment order status to failed
                    paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                    paymentOrderRepository.save(paymentOrder);
                    return false;
                }
            }else{
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;
            }
        }
        return false;
    }

}
