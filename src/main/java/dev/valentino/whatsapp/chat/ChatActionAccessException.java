package dev.valentino.whatsapp.chat;

public class ChatActionAccessException extends ChatException {

    public ChatActionAccessException() {
        super("Chat action access denied");
    }
}
