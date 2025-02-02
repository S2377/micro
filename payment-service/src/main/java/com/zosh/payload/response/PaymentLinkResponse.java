package com.zosh.payload.response;


import lombok.Data;

@Data
public class PaymentLinkResponse {
    private String paymentLink_url;
    private  String getPayment_Link_id;

}
