import de.florianmichael.counterbot.CounterBot;

public class Start {
    public static void main(String[] args) {
        CounterBot.get().load();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                CounterBot.get().config().save();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
