package org.synrgy.setara.transaction.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.synrgy.setara.transaction.dto.ReceiptDTO;
import org.synrgy.setara.transaction.dto.TransferResponse;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.transaction.model.TransactionType;

import java.io.InputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    @Override
    public byte[] generateReceipt(Transaction transaction, TransferResponse response) {
        try {
            // Define formatters
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH.mm 'WIB'", new Locale("id", "ID"));
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            // Format fields
            String formattedDateTime = transaction.getTime().format(dateTimeFormatter);
            String formattedAmount = currencyFormatter.format(response.getAmount());
            String formattedAdminFee = currencyFormatter.format(response.getAdminFee());
            String formattedTotal = currencyFormatter.format(response.getTotalAmount());

            // Create DTO
            ReceiptDTO receiptDTO = ReceiptDTO.builder()
                    .referenceNumber(transaction.getReferenceNumber())
                    .dateTime(formattedDateTime)
                    .transactionType(transaction.getType().equals(TransactionType.TRANSFER) ? "TRANSFER ANTAR BCA" : transaction.getType().toString())
                    .recipientName(response.getDestinationUser().getName().toUpperCase())
                    .recipientNumber(response.getDestinationUser().getAccountNumber())
                    .amount(formattedAmount)
                    .adminFee(formattedAdminFee)
                    .total(formattedTotal)
                    .status("Sukses")
                    .build();

            // Load the JasperReport template
            InputStream jasperStream = getClass().getResourceAsStream("/reports/Receipt.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperStream);

            // Set parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("referenceNumber", receiptDTO.getReferenceNumber());
            parameters.put("dateTime", receiptDTO.getDateTime());
            parameters.put("transactionType", receiptDTO.getTransactionType());
            parameters.put("recipientName", receiptDTO.getRecipientName());
            parameters.put("recipientNumber", receiptDTO.getRecipientNumber());
            parameters.put("amount", receiptDTO.getAmount());
            parameters.put("adminFee", receiptDTO.getAdminFee());
            parameters.put("total", receiptDTO.getTotal());
            parameters.put("status", receiptDTO.getStatus());

            // Create a data source from the DTO
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(receiptDTO));

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Export to PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (JRException e) {
            throw new RuntimeException("Failed to generate receipt", e);
        }
    }
}
