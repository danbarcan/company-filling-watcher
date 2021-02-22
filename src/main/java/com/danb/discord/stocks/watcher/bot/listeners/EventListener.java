package com.danb.discord.stocks.watcher.bot.listeners;

import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

public interface EventListener<T extends Event> {

    Class<T> getEventType();
    Mono<Void> execute(T event);

    default Mono<Void> handleError(Throwable error) {
        System.err.println("Unable to process " + getEventType().getSimpleName());
        error.printStackTrace();
        return Mono.empty();
    }
}
