package org.synrgy.setara.transaction.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.transaction.exception.JasperReportExceptions;

@ControllerAdvice(basePackages = "org.synrgy.setara.transaction")
public class JasperReportAdvice {
    private static final Logger log = LoggerFactory.getLogger(JasperReportAdvice.class);

    @ExceptionHandler(JasperReportExceptions.ReportFailedToLoadException.class)
    public ResponseEntity<BaseResponse<String>> handleReportFailedToLoadException(JasperReportExceptions.ReportFailedToLoadException ex) {
        log.error("Failed to load JasperReport template: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to load JasperReport template");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JasperReportExceptions.ReportFillOrExportException.class)
    public ResponseEntity<BaseResponse<String>> handleReportFillOrExportException(JasperReportExceptions.ReportFillOrExportException ex) {
        log.error("Failed to fill or export JasperReport: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fill or export JasperReport");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JasperReportExceptions.SavePdfException.class)
    public ResponseEntity<BaseResponse<String>> handleSavePdfException(JasperReportExceptions.SavePdfException ex) {
        log.error("Failed to save PDF file: {}", ex.getMessage(), ex);
        BaseResponse<String> response = BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save PDF file");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
