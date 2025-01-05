package eventbus;

@FunctionalInterface
public interface  EventListener<T extends Event> {
    void handle(T event);
}
