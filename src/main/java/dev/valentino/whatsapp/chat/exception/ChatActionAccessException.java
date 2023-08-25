package dev.valentino.whatsapp.chat.exception;

public class ChatActionAccessException extends ChatException {

    public ChatActionAccessException() {
        super("Chat action access denied");
    }
}
