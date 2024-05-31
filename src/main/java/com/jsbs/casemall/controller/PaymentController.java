package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.OrderDto;
import com.jsbs.casemall.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final OrderService orderService; // 주문 서비스 Di 준비

    @PostMapping(value = "/confirm")
    public String confirmPayment(@RequestBody String jsonBody) {
        JSONParser parser = new JSONParser();
        String orderId;
        String amount;
        String paymentKey;
        String paymentMethod;
        String payInfo;

        try {
            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) requestData.get("paymentKey");
            orderId = (String) requestData.get("orderId");
            amount = (String) requestData.get("amount");

            if (paymentKey == null || paymentKey.isEmpty() || orderId == null || orderId.isEmpty() || amount == null || amount.isEmpty()) {
                throw new IllegalArgumentException("결제 정보가 잘못되었습니다.");
            }

        } catch (ParseException e) {
            log.error("Error parsing JSON request body", e);
            return "redirect:/fail?message=Invalid JSON format&code=400";
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment information", e);
            return "redirect:/fail?message=" + e.getMessage() + "&code=400";
        }

        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        URL url;
        HttpURLConnection connection;
        try {
            url = new URL("https://api.tosspayments.com/v1/payments/confirm");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", authorizations);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(obj.toString().getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            log.error("Error connecting to payment API", e);
            return "redirect:/fail?message=Payment API connection failed&code=500";
        }

        int code;
        try {
            code = connection.getResponseCode();
        } catch (Exception e) {
            log.error("Error getting response from payment API", e);
            return "redirect:/fail?message=Payment API response failed&code=500";
        }

        boolean isSuccess = code == 200;
        try (InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();
             Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            if (isSuccess) {
                JSONObject easyPayObject = (JSONObject) jsonObject.get("easyPay");
                paymentMethod = (String) easyPayObject.get("provider");
                payInfo = (String) jsonObject.get("method");

                if (orderService.validatePayment(orderId, Integer.parseInt(amount))) {
                    orderService.updateOrderWithPaymentInfo(orderId, paymentMethod, payInfo);
                    return "redirect:/success?orderId=" + orderId + "&amount=" + amount + "&paymentKey=" + paymentKey;
                } else {
                    log.error("Payment validation failed for orderId: {}", orderId);
                    return "redirect:/fail?message=Payment validation failed&code=400";
                }
            } else {
                String errorMessage = (String) jsonObject.get("message");
                String errorCode = (String) jsonObject.get("code");
                log.error("Payment API error: {} - {}", errorCode, errorMessage);
                orderService.failOrder(orderId);
                return "redirect:/fail?message=" + errorMessage + "&code=" + errorCode;
            }
        } catch (ParseException e) {
            log.error("Error parsing JSON response from payment API", e);
            return "redirect:/fail?message=Invalid JSON response&code=500";
        } catch (Exception e) {
            log.error("Unexpected error during payment confirmation", e);
            return "redirect:/fail?message=Unexpected error&code=500";
        }
    }

    @GetMapping(value = "/success")
    public String paymentRequest(HttpServletRequest request, Model model) {
        model.addAttribute("orderId", request.getParameter("orderId"));
        model.addAttribute("amount", request.getParameter("amount"));
        model.addAttribute("paymentKey", request.getParameter("paymentKey"));
        return "pay/success";
    }

    @GetMapping(value = "/pay")
    public String payment(@RequestParam Long prId, @RequestParam int count, Model model, Principal principal) {
        try {
            OrderDto dto = orderService.getOrder(prId, principal.getName(), count);
            model.addAttribute("order", dto);
            return "pay/checkout";
        } catch (Exception e) {
            log.error("Error creating order", e);
            return "redirect:/fail?message=Order creation failed&code=500";
        }
    }

    @GetMapping(value = "/fail")
    public String failPayment(HttpServletRequest request, Model model) {
        String failCode = request.getParameter("code");
        String failMessage = request.getParameter("message");

        model.addAttribute("code", failCode);
        model.addAttribute("message", failMessage);

        return "pay/fail";
    }
}
