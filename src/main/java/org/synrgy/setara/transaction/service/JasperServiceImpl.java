package org.synrgy.setara.transaction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.synrgy.setara.transaction.dto.ReceiptResponse;
import org.synrgy.setara.transaction.exception.JasperReportExceptions;
import org.synrgy.setara.transaction.exception.TransactionExceptions;
import org.synrgy.setara.transaction.model.Transaction;
import org.synrgy.setara.transaction.repository.TransactionRepository;
import org.synrgy.setara.user.exception.UserExceptions;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.user.repository.UserRepository;
import org.synrgy.setara.vendor.model.Merchant;
import org.synrgy.setara.vendor.repository.MerchantRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JasperServiceImpl implements JasperService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EwalletUserRepository ewalletUserRepository;
    private final MerchantRepository merchantRepository;
    private static final String UNKNOWN = "Unknown";
    private final TransactionService transactionService;

    @Override
    public boolean generateReceipt(User user, UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionExceptions.TransactionNotFoundException("Transaction with ID " + transactionId + " not found"));

        if (!user.equals(transaction.getUser())) {
            throw new TransactionExceptions.TransactionNotOwnedByUser("Transaction is not owned by user");
        }

        String recipientNumber;
        String recipientName;
        String transactionType;

        switch (transaction.getType()) {
            case TRANSFER:
                User recipient = userRepository.findByAccountNumber(transaction.getDestinationAccountNumber())
                        .orElseThrow(() -> new UserExceptions.UserNotFoundException("Recipient user not found with account number: " + transaction.getDestinationAccountNumber()));
                recipientName = recipient.getName().toUpperCase();
                recipientNumber = transaction.getDestinationAccountNumber();
                transactionType = "TRANSFER ANTAR BCA";
                break;

            case TOP_UP:
                EwalletUser ewalletUser = ewalletUserRepository.findByEwalletIdAndPhoneNumber(
                                transaction.getEwallet().getId(),
                                transaction.getDestinationPhoneNumber())
                        .orElseThrow(() -> new UserExceptions.UserNotFoundException("E-wallet user not found with phone number: " + transaction.getDestinationPhoneNumber()));
                recipientName = ewalletUser.getName().toUpperCase();
                recipientNumber = transaction.getDestinationPhoneNumber();
                transactionType = "TOP UP SALDO";
                break;

            case QRPAYMENT:
                Merchant merchant = merchantRepository.findById(transaction.getDestinationIdQris().getId())
                        .orElseThrow(() -> new TransactionExceptions.MerchantNotFoundException("Merchant with ID " + transaction.getDestinationIdQris().getId() + " not found"));
                recipientName = merchant.getName();
                recipientNumber = merchant.getNmid();
                transactionType = "PEMBAYARAN QR";
                break;

            default:
                recipientName = UNKNOWN;
                recipientNumber = UNKNOWN;
                transactionType = UNKNOWN;
                break;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH.mm 'WIB'", new Locale("id", "ID"));
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        String formattedDateTime = transaction.getTime().format(dateTimeFormatter);
        String formattedAmount = currencyFormatter.format(transaction.getAmount());
        String formattedAdminFee = currencyFormatter.format(transaction.getAdminFee());
        String formattedTotal = currencyFormatter.format(transaction.getTotalamount());

        ReceiptResponse receipt = ReceiptResponse.builder()
                .referenceNumber(transaction.getReferenceNumber())
                .dateTime(formattedDateTime)
                .transactionType(transactionType)
                .recipientName(recipientName)
                .recipientNumber(recipientNumber)
                .amount(formattedAmount)
                .adminFee(formattedAdminFee)
                .total(formattedTotal)
                .status("Sukses")
                .build();

        JasperReport jasperReport;
        try {
            jasperReport = (JasperReport) JRLoader.loadObject(ResourceUtils.getFile("Receipt.jasper"));
        } catch (FileNotFoundException | JRException e) {
            try {
                File file = ResourceUtils.getFile("classpath:reports/Receipt.jrxml");
                jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(jasperReport, "Receipt.jasper");
            } catch (FileNotFoundException | JRException ex) {
                throw new JasperReportExceptions.ReportFailedToLoadException("Failed to load or compile the JasperReport template");
            }
        }

        Map<String, Object> parameters = getMapReceipt(receipt);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(receipt));
        JasperPrint jasperPrint;
        byte[] reportContent;
        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new JasperReportExceptions.ReportFillOrExportException("Failed to fill report or export to PDF");
        }

        try {
            String pdfFileName = "transaction_receipt_" + transaction.getReferenceNumber() + ".pdf";
            Path pdfPath = Paths.get(System.getProperty("user.home"), "Downloads", pdfFileName);

            Files.write(pdfPath, reportContent);

            log.info("PDF saved to: {}", pdfPath.toAbsolutePath());

            return true;

        } catch (IOException e) {
            log.error("Failed to save the PDF file", e);
            return false;
        }
    }

    @NotNull
    private static Map<String, Object> getMapReceipt(ReceiptResponse receipt) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("referenceNumber", receipt.getReferenceNumber());
        parameters.put("dateTime", receipt.getDateTime());
        parameters.put("transactionType", receipt.getTransactionType());
        parameters.put("recipientName", receipt.getRecipientName());
        parameters.put("recipientNumber", receipt.getRecipientNumber());
        parameters.put("amount", receipt.getAmount());
        parameters.put("adminFee", receipt.getAdminFee());
        parameters.put("total", receipt.getTotal());
        parameters.put("status", receipt.getStatus());
        return parameters;
    }

    @Override
    public byte[] generateAllMutationReport(User user) {
        JasperReport jasperReport;
        try {
            jasperReport = (JasperReport) JRLoader
                    .loadObject(ResourceUtils.getFile("MutationReport.jasper"));
        } catch (JRException | FileNotFoundException e) {
            try {
                File file = ResourceUtils.getFile("classpath:reports/MutationReport.jrxml");
                jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(jasperReport, "MutationReport.jasper");
            } catch (FileNotFoundException | JRException ex) {
                throw new RuntimeException(ex); // TODO: change exception
            }
        }

        JRBeanCollectionDataSource mutationDataset = new JRBeanCollectionDataSource(transactionService.getMutationDataset(user));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("noRek", String.valueOf(user.getAccountNumber()));
        parameters.put("name", String.valueOf(user.getName()));
        parameters.put("currency", "IDR");
        parameters.put("mutationDataset", mutationDataset);

        JasperPrint jasperPrint;
        byte[] reportContent;
        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new RuntimeException("Failed to generate report", e); // TODO: change exception
        }

        return reportContent;
    }
}
