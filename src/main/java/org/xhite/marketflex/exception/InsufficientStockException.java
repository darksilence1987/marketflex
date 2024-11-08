package org.xhite.marketflex.exception;

public class InsufficientStockException extends RuntimeException {
        
            public InsufficientStockException(String message) {
                super(message);
            }
        
            public InsufficientStockException(String message, Throwable cause) {
                super(message, cause);
            }
}