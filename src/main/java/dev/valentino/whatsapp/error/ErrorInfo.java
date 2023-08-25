package dev.valentino.whatsapp.error;

import java.time.LocalDateTime;

public record ErrorInfo(
        String error,
        String message,
        LocalDateTime timestamp
) {
}
