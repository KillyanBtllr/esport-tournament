package eventbus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventBus {
    private final Map<String, Set<EventListener<? extends Event>>> listeners = new HashMap<>();

    public <T extends Event> void publish(T event) {
        Set<EventListener<? extends Event>> eventListeners = listeners.get(event.getType());
        if (eventListeners != null) {
            for (EventListener<? extends Event> listener : eventListeners) {
                @SuppressWarnings("unchecked")
                EventListener<T> typedListener = (EventListener<T>) listener;
                typedListener.handle(event);
            }
        }
    }

    public <T extends Event> void subscribe(String eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, key -> new HashSet<>()).add(listener);
    }

    public <T extends Event> void unsubscribe(String eventType, EventListener<T> listener) {
        Set<EventListener<? extends Event>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
            if (eventListeners.isEmpty()) {
                listeners.remove(eventType);
            }
        }
    }
}
