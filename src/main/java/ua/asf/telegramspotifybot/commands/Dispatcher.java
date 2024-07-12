package ua.asf.telegramspotifybot.commands;

import jakarta.annotation.PostConstruct;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.asf.telegramspotifybot.core.handler.ResponseCreator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;


@Component
public class Dispatcher {

    @Autowired
    private ResponseCreator responseCreator;

    @Autowired
    private Reflections reflections;

    private final Map<String, BiFunction<Long, ResponseCreator, Command>> constructors = new HashMap<>();

    @PostConstruct
    public void fillConstructorsTable() {
        Set<Class<?>> commands = reflections.getTypesAnnotatedWith(Request.class);
        for (var c : commands) {

            constructors.put(c.getAnnotation(Request.class).request(),
                    (Long chatId, ResponseCreator responseCreator) -> {
                        try {
                            return (Command) c.getDeclaredConstructor(Long.class, ResponseCreator.class)
                                    .newInstance(chatId, responseCreator);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                 NoSuchMethodException e) {
                            return null;
                        }
                    }
                    );

        }


    }

    public Command commandFromRequest(String request, Long chatId) {
        Command command = this.constructors.get(request).apply(chatId, responseCreator);

        if(command == null) command = new NullCommand(chatId, responseCreator);

        return command;
    }

}
