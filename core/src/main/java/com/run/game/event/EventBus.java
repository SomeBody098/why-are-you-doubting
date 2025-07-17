package com.run.game.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

public class EventBus {

    private final Map<EventType, Stack<Consumer<EventType>>> subscribers = new HashMap<>();

    public void subscribe(EventType eventType, Consumer<EventType> handler) {
        subscribers.computeIfAbsent(eventType, k -> new Stack<>())
            .add(handler);
    }

    public void publish(EventType event) {
        Stack<Consumer<EventType>> handlers = subscribers.get(event);
        if (handlers != null) {
            while (!handlers.empty()){
                handlers.pop().accept(event);
            }
        }
    }
}
