package notificationservice;

import eventbus.EventBus;
import notificationservice.events.MatchResultNotification;
import notificationservice.events.TeamCreatedNotification;

public class NotificationService {
    private final EventBus eventBus;

    public NotificationService(EventBus eventBus) {
        this.eventBus = eventBus;

        // Abonnement aux événements en utilisant leur type retourné par getType()
        eventBus.subscribe(MatchResultNotification.class.getName(), this::handleMatchResultNotification);
        eventBus.subscribe(TeamCreatedNotification.class.getName(), this::handleTeamCreatedNotification);
    }

    private void handleMatchResultNotification(MatchResultNotification notification) {
        System.out.println("Notification: Match Result - " + notification.getMessage());
    }

    private void handleTeamCreatedNotification(TeamCreatedNotification notification) {
        System.out.println("Notification: New Team Created - " + notification.getMessage());
    }


}
