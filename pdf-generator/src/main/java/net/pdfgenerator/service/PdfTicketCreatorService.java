package net.pdfgenerator.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import net.pdfgenerator.dto.ticketorder.CustomerTicketDto;
import net.pdfgenerator.dto.ticketorder.TicketOrderDto;
import net.pdfgenerator.enums.qrcode.QRCodePosition;
import net.pdfgenerator.enums.ticket.TicketConstants;
import net.pdfgenerator.service.pdfgenerator.PdfGeneratorService;
import net.pdfgenerator.service.qrcodegenerator.QRCodeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PdfTicketCreatorService {
    private final PdfGeneratorService pdfGeneratorService;
    private final QRCodeGeneratorService qrCodeGeneratorService;

    @Autowired
    public PdfTicketCreatorService(PdfGeneratorService pdfGeneratorService, QRCodeGeneratorService qrCodeGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
        this.qrCodeGeneratorService = qrCodeGeneratorService;
    }

    public byte[] createPdfTicket(TicketOrderDto ticketOrderDto) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        for (CustomerTicketDto customerTicketDto : ticketOrderDto.getCustomerTicketDto()) {
            pdfGeneratorService.generateCustomerTicketPdfDocument(customerTicketDto, document);
            qrCodeGeneratorService.addQRCode(document, String.valueOf(customerTicketDto.getCustomerTicketId()), QRCodePosition.LEFT_CENTER);
        }
        document.close();

        return outputStream.toByteArray();
    }

    public String createTicketName(Set<CustomerTicketDto> customerTicketDtoSet) {
        return customerTicketDtoSet.stream()
                .map(customerTicketDto -> customerTicketDto.getFirstName().concat(TicketConstants.UNDERLINE.toString()).concat(customerTicketDto.getLastName()))
                .collect(Collectors.joining("-"));
    }
}
