package edu.ou.paymentcommandservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class MomoConfig {
    @Value("${spring.momo.accessKey}")
    private String accessKey;
    @Value("${spring.momo.partnerCode}")
    private String partnerCode;
    @Value("${spring.momo.secretKey}")
    private String secretKey;
    @Value("${spring.momo.api}")
    private String api;

    private final ObjectMapper objectMapper;

    /**
     * Create momo order
     *
     * @param price price
     * @param orderInfo order info
     * @param notifyURL notify url
     * @param returnURL return url
     * @return order info
     * @throws InvalidKeyException InvalidKeyException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws JsonProcessingException JsonProcessingException
     * @throws ExecutionException ExecutionException
     * @throws InterruptedException InterruptedException
     * @author Nguyen Trung Kien - OU
     */
    public Map<String, String> createOrder(
            BigDecimal price,
            String orderInfo,
            String notifyURL,
            String returnURL
    ) throws InvalidKeyException,
            NoSuchAlgorithmException,
            JsonProcessingException,
            ExecutionException,
            InterruptedException {

        final String requestId = UUID.randomUUID().toString();
        final String orderId = UUID.randomUUID().toString();
        final boolean autoCapture = true;
        final String requestType = "captureWallet";
        final String extraData = "";
        final String rawSignature = "accessKey=" + accessKey +
                "&amount=" + price.longValue() +
                "&extraData=" + extraData +
                "&ipnUrl=" + notifyURL +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + returnURL +
                "&requestId=" + requestId +
                "&requestType=" + requestType;

        final SecretKeySpec secretKeySpec = new SecretKeySpec(
                Objects.requireNonNull(secretKey)
                        .getBytes(),
                "HmacSHA256"
        );

        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        final String signature = bytesToHex(mac.doFinal(rawSignature.getBytes()));

        final Map<String, String> map = new HashMap<>() {
            {
                put("partnerCode", partnerCode);
                put("partnerName", "Habolabu");
                put("storeId", partnerCode);
                put("requestType", requestType);
                put("ipnUrl", notifyURL);
                put("redirectUrl", returnURL);
                put("orderId", orderId);
                put("amount", String.valueOf(price.longValue()));
                put("autoCapture", String.valueOf(autoCapture));
                put("orderInfo", orderInfo);
                put("requestId", requestId);
                put("extraData", extraData);
                put("signature", signature);
            }
        };

        final String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(map);

        final HttpRequest request =
                HttpRequest
                        .newBuilder(URI.create(Objects.requireNonNull(api)))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

        final CompletableFuture<Map<String, String>> future =
                HttpClient
                        .newHttpClient()
                        .sendAsync(
                                request,
                                HttpResponse.BodyHandlers.ofString()
                        )
                        .thenApply(HttpResponse::body)
                        .thenApply(content -> {
                            try {
                                return objectMapper.readValue(content, new TypeReference<>() {
                                });
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        });

        return future.get();
    }

    /**
     * Convert byte array to hex string
     *
     * @param hash byte array
     * @return hex string
     * @author Nguyen Trung Kien - OU
     */
    private static String bytesToHex(byte[] hash) {
        final StringBuilder hexString = new StringBuilder(2 * hash.length);

        for (byte h : hash) {
            final String hex = Integer.toHexString(0xff & h);
            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }
}