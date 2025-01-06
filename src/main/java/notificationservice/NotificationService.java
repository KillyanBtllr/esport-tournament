package notificationservice;

import eventbus.EventBus;
import notificationservice.events.MatchResultNotification;
import notificationservice.events.TeamCreatedNotification;
import teamservice.events.TeamCreated;

public class NotificationService {
    private final EventBus eventBus;

    public NotificationService(EventBus eventBus) {
        this.eventBus = eventBus;

        eventBus.subscribe("TeamCreated", event -> {
            TeamCreated teamCreated = (TeamCreated) event;
            System.out.println("New team created: ID = " + teamCreated.getId() + ", Name = " + teamCreated.getName());
        });

    }




}
