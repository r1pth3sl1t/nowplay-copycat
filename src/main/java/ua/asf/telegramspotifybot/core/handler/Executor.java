package ua.asf.telegramspotifybot.core.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ua.asf.telegramspotifybot.commands.Command;

@Component
public class Executor {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    };

    public BotApiMethod<?> executeCommand() {
        return this.command.execute();
    }
}
