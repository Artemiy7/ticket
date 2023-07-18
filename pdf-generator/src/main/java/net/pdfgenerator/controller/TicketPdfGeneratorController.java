package net.pdfgenerator.controller;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.pdfgenerator.dto.ticketorder.TicketOrderDto;
import net.pdfgenerator.service.PdfTicketCreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api("Ticket Pdf")
@RestController
public class TicketPdfGeneratorController {
    private final PdfTicketCreatorService pdfGeneratorService;

    @Autowired
    public TicketPdfGeneratorController(PdfTicketCreatorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @ApiOperation("Create pdf ticket with QR code from TicketOrder")
    @PostMapping(value = "api/v1/pdf-generator", produces = {MediaType.APPLICATION_PDF_VALUE})
    public ResponseEntity<byte[]> createTicketPdf(@RequestBody TicketOrderDto ticketOrderDto) {
        HttpHeaders headers = new HttpHeaders();
        try {
            byte[] byteArray = pdfGeneratorService.createPdfTicket(ticketOrderDto);
            headers.add("Content-Disposition",
                    "inline; filename=" + pdfGeneratorService.createTicketName(ticketOrderDto.getCustomerTicketDto()));
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(byteArray);
        } catch (DocumentException npe) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
