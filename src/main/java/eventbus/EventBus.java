package eventbus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventBus {
    private final Map<String, List<Consumer<Event>>> subscribers;

    public EventBus() {
        // Utilisation d'une ConcurrentHashMap pour la gestion thread-safe
        this.subscribers = new ConcurrentHashMap<>();
    }

    public void subscribe(String eventType, Consumer<Event> listener) {
        subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public void publish(Event event) {
        List<Consumer<Event>> listeners = subscribers.get(event.getType());
        if (listeners != null) {
            // Notifier tous les abonnés
            listeners.forEach(listener -> listener.accept(event));
        }
    }

    public void unsubscribe(String eventType, Consumer<Event> listener) {
        List<Consumer<Event>> listeners = subscribers.get(eventType);
        if (listeners != null) {
            listeners.remove(listener);
            // Supprimer l'entrée si la liste est vide
            if (listeners.isEmpty()) {
                subscribers.remove(eventType);
            }
        }
    }
}