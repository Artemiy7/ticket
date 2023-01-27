package net.pdfgenerator.service.pdfgenerator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import net.pdfgenerator.dto.ticketorder.CustomerTicketDto;
import net.pdfgenerator.enums.CurrencyType;
import net.pdfgenerator.enums.ticket.TicketConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class PdfGeneratorService {
    private final String siteUri;
    private final String siteSupportPhone;
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(TicketConstants.DATETIME_HEADER.toString());

    @Autowired
    public PdfGeneratorService(@Value("${spring.site.uri}") String siteUri,
                               @Value("${spring.site.support.phone}") String siteSupportPhone) {
        this.siteUri = siteUri;
        this.siteSupportPhone = siteSupportPhone;
    }

    public Document generateCustomerTicketPdfDocument(CustomerTicketDto customerTicketDto, Document document) throws DocumentException {
        Paragraph head = new Paragraph();
        head.add(new Paragraph(String.format(TicketConstants.HEAD_TEMPLATE.toString(),
                customerTicketDto.getTicketOrderDto().getTicketType().toString(),
                customerTicketDto.getTicketOrderDto().getOccasionAddress()),
                new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD)));

        head.add(new Paragraph(String.format(TicketConstants.HEAD_TEMPLATE_WITH_LINE.toString(),
                customerTicketDto.getTicketOrderDto().getOccasionName(),
                customerTicketDto.getTicketOrderDto().getOccasionDate().format(dateTimeFormatter)),
                new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD)));

        head.add(new Paragraph(TicketConstants.EMPTY_SPACE.toString()));

        Paragraph site = new Paragraph();
        site.add(new Paragraph(String.format(TicketConstants.SITE_AND_SUPPORT_PHONE.toString(), siteUri, siteSupportPhone),
                new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.NORMAL, BaseColor.BLUE)));

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 14);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 1, 1, 1});

        PdfPCell hcell = new PdfPCell(new Phrase(TicketConstants.CUSTOMER_NAME_HEADER.toString(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell.setPhrase(new Phrase(TicketConstants.SEAT_HEADER.toString(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell.setPhrase(new Phrase(TicketConstants.COST_HEADER.toString(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell.setPhrase(new Phrase(TicketConstants.SEAT_TYPE_HEADER.toString(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        PdfPCell cell = new PdfPCell(new Phrase(customerTicketDto.getFirstName().concat(" ").concat(customerTicketDto.getLastName()), dataFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(customerTicketDto.getSeat()), dataFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(customerTicketDto.getAmount())
                        .concat(CurrencyType.get(customerTicketDto.getTicketOrderDto().getCurrency())
                        .getCurrencySymbol()), dataFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(customerTicketDto.getSeatPlaceType().getSeatPlaceType(), dataFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        document.newPage();
        document.add(head);
        document.add(table);
        document.add(site);

        return document;
    }
}
