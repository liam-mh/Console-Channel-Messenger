package Server;

public class Clock {
    private long t;

    public Clock() { t = 0; }

    public synchronized long tick()
    { return ++t; }
}
