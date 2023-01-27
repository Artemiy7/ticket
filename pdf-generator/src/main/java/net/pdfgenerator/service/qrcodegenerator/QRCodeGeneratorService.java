package net.pdfgenerator.service.qrcodegenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import net.pdfgenerator.enums.qrcode.QRCodePosition;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeGeneratorService {
    public Document addQRCode(Document document, String barcodeText, QRCodePosition position) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Image image = Image.getInstance(newQRCodeImage(barcodeText));
            image.setAbsolutePosition(position.getAbsoluteX(), position.getAbsoluteY());
            document.add(image);
            return document;
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error on writing QR code", e);
        }
    }

    /*
        Get new QR code image bytes for barcode text
    */
    private byte[] newQRCodeImage(String barcodeText) {
        try (ByteArrayOutputStream image = new ByteArrayOutputStream()) {
            QRCodeWriter barcodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 120, 120);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", image);
            return image.toByteArray();
        } catch (IOException | com.google.zxing.WriterException e) {
            throw new RuntimeException("Error on generating QR code", e);
        }
    }
}
