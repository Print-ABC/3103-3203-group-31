package models;

public class Session {
    private static User user;
    private static String cardId;
    private static long millis = 0;

    public static void deleteSession(){
        user = null;
        cardId = null;
        millis = 0;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Session.user = user;
    }

    public static String getCardId() {
        return cardId;
    }

    public static void setCardId(String cardId) {
        Session.cardId = cardId;
    }

    public static long getMillis() {
        return millis;
    }

    public static void setMillis(long millis) {
        Session.millis = millis;
    }
}
