package org.synrgy.setara.transaction.exception;

import java.io.IOException;

public class JasperReportExceptions {

    private JasperReportExceptions() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class ReportFailedToLoadException extends RuntimeException {
        public ReportFailedToLoadException (String message) {
            super(message);
        }
    }

    public static class ReportFillOrExportException extends RuntimeException {
        public ReportFillOrExportException (String message) {
            super(message);
        }
    }

    public static class SavePdfException extends IOException {
        public SavePdfException (String message) {
            super(message);
        }
    }

}