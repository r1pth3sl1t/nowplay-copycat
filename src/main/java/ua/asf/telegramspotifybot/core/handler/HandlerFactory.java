package ua.asf.telegramspotifybot.core.handler;

import jakarta.annotation.PostConstruct;
import org.reflections.Reflections;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.asf.telegramspotifybot.commands.Request;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Component
public class HandlerFactory {

    @Autowired
    private Reflections reflections;

    private List<Handler> handlers;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void findImplementations() {
        Set<Class<? extends Handler>> handlers = reflections.getSubTypesOf(Handler.class);

        this.handlers = new ArrayList<>(handlers.size());

        for(var handler : handlers) {
            this.handlers.add(applicationContext.getBean(handler));
        }

    }


    public Handler getHandler(Update update) {
        return handlers
                .stream()
                .filter(handler -> handler.doesFitForUpdate(update)).
                findFirst()
                .orElse(null);
    }

}

