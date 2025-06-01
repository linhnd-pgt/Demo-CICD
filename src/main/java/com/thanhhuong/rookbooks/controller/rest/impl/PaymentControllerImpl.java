package com.thanhhuong.rookbooks.controller.rest.impl;

import com.thanhhuong.rookbooks.config.VNPayConfig;
import com.thanhhuong.rookbooks.controller.common.BaseController;
import com.thanhhuong.rookbooks.dto.OrderPerson;
import com.thanhhuong.rookbooks.dto.PaymentResDTO;
import com.thanhhuong.rookbooks.dto.VNPayRequestDTO;
import com.thanhhuong.rookbooks.entity.Order;
import com.thanhhuong.rookbooks.entity.User;
import com.thanhhuong.rookbooks.service.CartService;
import com.thanhhuong.rookbooks.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/v1/payment")
@AllArgsConstructor
public class PaymentControllerImpl extends BaseController {

    private final CartService cartService;

    private final HttpSession session;

    private final OrderService orderService;

    @ResponseBody
    @PostMapping("/create_payment")
    public ResponseEntity<?> createPayment(HttpServletRequest req, @RequestBody VNPayRequestDTO vnPayRequestDTO) throws UnsupportedEncodingException {

        long amount = (long) vnPayRequestDTO.getTotalAmount() * 100L;
//        String bankCode = req.getParameter("bankCode");

        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);

        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", vnPayRequestDTO.getBankCode());

//        if (bankCode != null && !bankCode.isEmpty()) {
//            vnp_Params.put("vnp_BankCode", bankCode);
//        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", VNPayConfig.orderType);
        vnp_Params.put("vnp_Locale", "vn");

//        String locate = req.getParameter("language");
//        if (locate != null && !locate.isEmpty()) {
//            vnp_Params.put("vnp_Locale", locate);
//        } else {
//            vnp_Params.put("vnp_Locale", "vn");
//        }
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
//        com.google.gson.JsonObject job = new JsonObject();
//        job.addProperty("code", "00");
//        job.addProperty("message", "success");
//        job.addProperty("data", paymentUrl);
//        Gson gson = new Gson();
//        resp.getWriter().write(gson.toJson(job));

        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setStatus("Ok");
        paymentResDTO.setMessage("Payment handled successfully");
        paymentResDTO.setUrl(paymentUrl);

        OrderPerson orderPerson = new OrderPerson();
        orderPerson.setFullName(vnPayRequestDTO.getFullName());
        orderPerson.setAddress(vnPayRequestDTO.getAddress());
        orderPerson.setEmail(vnPayRequestDTO.getEmail());
        orderPerson.setPhoneNumber(vnPayRequestDTO.getPhoneNumber());

        User curUser = getCurrentUser();

        orderService.createVNPayOrder(cartService.getCart(session), curUser, orderPerson);

        cartService.clearCart(session);

        return ResponseEntity.ok().body(paymentResDTO);
    }

    @GetMapping("/vnpay-return")
    public String handleVnPayReturn(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String key : parameterMap.keySet()) {
            params.put(key, parameterMap.get(key)[0]);
        }

        String vnp_TxnRef = params.get("vnp_TxnRef");
        boolean isPaymentSuccess = "00".equals(params.get("vnp_ResponseCode"));

        if (isPaymentSuccess) {

            Order order = orderService.getOrderByVnpTxnRef(vnp_TxnRef);
            if (order != null) {
                orderService.updateUnpaidToPending(order);
            }

        }
        return "redirect:/orders";
    }


}
