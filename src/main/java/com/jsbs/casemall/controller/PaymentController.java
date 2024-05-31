package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.OrderDto;
import com.jsbs.casemall.service.OrderService;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;
@Log4j2
@Controller
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final OrderService orderService; // 주문 서비스 Di 준비

    @PostMapping(value = "/confirm")
    public String confirmPayment(@RequestBody String jsonBody) throws Exception {
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
                throw new IllegalArgumentException("결제 정보가 잘못되었씁니다");
            }

        } catch (ParseException e) {
            log.error("Error parsing JSON request body", e);
            throw new RuntimeException("Invalid JSON format", e);
        }

        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(obj.toString().getBytes(StandardCharsets.UTF_8));
        }

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        try (
            InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();
             Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            if (isSuccess) {
                JSONObject easyPayObject = (JSONObject) jsonObject.get("easyPay");
                paymentMethod = (String) easyPayObject.get("provider");
                payInfo = (String)jsonObject.get("method");
                orderService.updateOrderWithPaymentInfo(orderId, paymentMethod,payInfo);
                return "redirect:/success?orderId=" + orderId + "&amount=" + amount + "&paymentKey=" + paymentKey;
            } else {
                orderService.failOrder(orderId);
                return "redirect:/fail?message=" + jsonObject.get("message") + "&code=" + jsonObject.get("code");
            }
        }
    }

    @GetMapping(value = "/success")
    public String paymentRequest(HttpServletRequest request, Model model) {
        model.addAttribute("orderId", request.getParameter("orderId"));
        model.addAttribute("amount", request.getParameter("amount"));
        model.addAttribute("paymentKey", request.getParameter("paymentKey"));
        return "pay/success";
    }


    // 주문요청
    @GetMapping(value = "/pay")
    public String payment(@RequestParam Long prId, @RequestParam int count, Model model, Principal principal) throws Exception {
        // 주문하기를 누르면 해당 페이지의 상품 ID와 수량을 가져와 서비스로 넘겨 해당 상품의 정보를 가져오고 model에 뿌려준다
        OrderDto dto = orderService.getOrder(prId, principal.getName(), count);
        model.addAttribute("order", dto);
        return "pay/checkout";
    }

    /**
     * 인증실패처리
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/fail")
    public String failPayment(HttpServletRequest request, Model model) throws Exception {
        String failCode = request.getParameter("code");
        String failMessage = request.getParameter("message");

        model.addAttribute("code", failCode);
        model.addAttribute("message", failMessage);

        return "pay/fail";
    }
}
