package com.rcr.core_engine.utilities;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class CaptchaUtil {

    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // Omitted similar chars (0, O, 1, I)
    private static final int LENGTH = 6;
    private static final String HMAC_ALGO = "HmacSHA256";
    private static final String SECRET_KEY = "TalentFlowSecretKeyForCaptchaVerificationDoNotExpose"; // Keep in application.properties
    private static final long EXPIRATION_MS = 2 * 60 * 1000; // 2 minutes

    private final Random random = new SecureRandom();

    public String generateCaptchaText() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public String generateCaptchaImageBase64(String text) {
        int width = 160;
        int height = 50;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 1. Background
        g2d.setColor(new Color(245, 245, 245));
        g2d.fillRect(0, 0, width, height);

        // 2. Add noise lines
        for (int i = 0; i < 6; i++) {
            g2d.setColor(new Color(random.nextInt(150) + 50, random.nextInt(150) + 50, random.nextInt(150) + 50));
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g2d.drawLine(x1, y1, x2, y2);
        }

        // 3. Draw characters with slight rotation
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics fm = g2d.getFontMetrics();
        int charSpacing = width / (LENGTH + 1);

        for (int i = 0; i < text.length(); i++) {
            g2d.setColor(new Color(random.nextInt(100), random.nextInt(100), random.nextInt(100)));
            int x = (i + 1) * charSpacing - 10;
            int y = (height / 2) + (fm.getAscent() / 3) + random.nextInt(6) - 3;

            // Small rotation for distortion
            double angle = (random.nextDouble() - 0.5) * 0.4;
            g2d.rotate(angle, x, y);
            g2d.drawString(String.valueOf(text.charAt(i)), x, y);
            g2d.rotate(-angle, x, y); // reset rotation
        }

        g2d.dispose();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate CAPTCHA image", e);
        }
    }

    public String createSignedToken(String text) {
        long expiryTime = System.currentTimeMillis() + EXPIRATION_MS;
        String payload = text.toUpperCase() + ":" + expiryTime;
        String signature = sign(payload);
        return Base64.getEncoder().encodeToString((payload + ":" + signature).getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateCaptcha(String signedToken, String userInput) {
        if (signedToken == null || userInput == null) {
            return false;
        }

        try {
            String decoded = new String(Base64.getDecoder().decode(signedToken), StandardCharsets.UTF_8);
            String[] parts = decoded.split(":");
            if (parts.length != 3) {
                return false;
            }

            String originalText = parts[0];
            long expiryTime = Long.parseLong(parts[1]);
            String providedSignature = parts[2];

            // 1. Check expiration
            if (System.currentTimeMillis() > expiryTime) {
                return false;
            }

            // 2. Verify signature integrity
            String expectedSignature = sign(originalText + ":" + expiryTime);
            if (!expectedSignature.equals(providedSignature)) {
                return false;
            }

            // 3. Case-insensitive comparison
            return originalText.equalsIgnoreCase(userInput.trim());
        } catch (Exception e) {
            return false;
        }
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error signing captcha data", e);
        }
    }
}