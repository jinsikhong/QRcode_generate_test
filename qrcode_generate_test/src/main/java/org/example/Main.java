package org.example;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String LOGO_PATH = "C:\\Users\\user\\Desktop\\GitHub Repos\\qrTesttest\\src\\main\\java\\org\\example\\logo.png";
    public static String createQR(String url) throws Exception {

        BitMatrix bitMatrix = null;
        String codeInformation = url;
        int onColor = 0xFF000000; // 바코드 색
        int offColor = 0xFFFFFFFF; // 배경 색

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(onColor, offColor);
        Map<EncodeHintType, String> hints = new HashMap<>();

        hints.put(EncodeHintType.ERROR_CORRECTION, "L");

        // 이미지 파일을 BufferedImage로 로드
        BufferedImage logoImage = loadImage(LOGO_PATH);

        // QRCode 전체 크기
        // 단위는 fixel
        int width = 200;
        int height = 200;


        try {
            // bitMatrix 형식으로 QRCode를 만든다.
            bitMatrix = qrCodeWriter.encode(codeInformation, BarcodeFormat.QR_CODE, width, height);
            // QRCode 중간에 빈공간을 만들고 색을 offColor로 바꿔주는 메소드
//             bitMatrix= emptyQR(bitMatrix,height,width); // QR내부에 빈 공간 만드는 메소드(사용할 경우 hint의 error_correction 을 반드시 높여줘야 합니다)
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);
            BufferedImage overly = logoImage;

            int deltaHeight = qrImage.getHeight() - overly.getHeight();
            int deltaWidth = qrImage.getWidth() - overly.getWidth();

            BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) combined.getGraphics();

            g.drawImage(qrImage, 0, 0, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            // QR코드 이미지의 정중앙 위치에 덮음.
            g.drawImage(overly, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(combined, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            String base64QRCode = Base64.getEncoder().encodeToString(imageBytes);
            return base64QRCode;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static BufferedImage loadImage(String filePath) {
        try {
            File file = new File(filePath);
            return ImageIO.read(file);
        } catch (IOException e) {
//            throw new IllegalArgumentException("업로드 실패");
            e.printStackTrace();
            return null; // 이미지 로드에 실패한 경우 null을 반환하거나 예외를 처리할 수 있습니다.
        }
    }


    public static void main(String[] args) throws Exception {
        String result = createQR("123");
        System.out.println(result);


    }
}