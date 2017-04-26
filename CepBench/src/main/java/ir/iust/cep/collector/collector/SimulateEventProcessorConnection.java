package ir.iust.cep.collector.collector;

import ir.iust.cep.collector.event.CollectorEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class SimulateEventProcessorConnection extends Thread {

    static Map<Integer, SimulateEventProcessorConnection> EVENT_PROCESSOR_CONNECTIONS = Collections.synchronizedMap(new HashMap<Integer, SimulateEventProcessorConnection>());

    public static void dumpStats(int statSec) {
        long totalCount = 0;
        SimulateEventProcessorConnection any = null;
        for (SimulateEventProcessorConnection m : EVENT_PROCESSOR_CONNECTIONS.values()) {
            totalCount += m.countLast10sLast;
            any = m;
        }
        if (any != null) {
            System.out.printf("Throughput %.0f (active %d pending %d)\n",
                    (float) totalCount / statSec,
                    any.executor == null ? 0 : any.executor.getCorePoolSize(),
                    any.executor == null ? 0 : any.executor.getQueue().size()
            );
        }
    }

    private int simulationRate;
    private ThreadPoolExecutor executor;
    private final int statSec;
    private long countLast10sLast = 0;
    private long countLast10s = 0;
    private long lastThroughputTick = System.currentTimeMillis();
    private int myID;
    private static int ID = 0;

    public SimulateEventProcessorConnection(int simulationRate, ThreadPoolExecutor executor, int statSec) {
        super("EsperCollector-cnx-" + ID++);
        this.simulationRate = simulationRate;
        this.executor = executor;
        this.statSec = statSec;
        myID = ID - 1;

        // simulationRate event / s
        // 10ms ~ simulationRate / 1E2
        EVENT_PROCESSOR_CONNECTIONS.put(myID, this);

    }

    public void run() {
        System.out.println("Event per    s = " + simulationRate);
        int eventPer10Millis = (int) (simulationRate / 100);
        System.out.println("Event per 10ms = " + Math.max(eventPer10Millis, 1));
        try {
            do {
                long ms = System.currentTimeMillis();
                for (int i = 0; i < eventPer10Millis; i++) {
                    CollectorEvent.Event.Builder builder = CollectorEvent.Event.newBuilder();
                    builder.setEventId(Collector.RULE_ID);
                    builder.setTimestamp(System.nanoTime());
                    builder.setEventId((int) Collector.lastReceivedEventId.getAndIncrement());
                    final CollectorEvent.Event simulatedEvent = builder.build();
                    if (executor == null) {
                        long ns = System.nanoTime();
                        StatsHolder.getEngine().update(System.nanoTime() - ns);
                    } else {
                        executor.execute(new Runnable() {
                            public void run() {
                                long ns = System.nanoTime();
                                long nsDone = System.nanoTime();
                                long msDone = System.currentTimeMillis();
                                StatsHolder.getEngine().update(nsDone - ns);
                                StatsHolder.getCollector().update(nsDone - System.nanoTime());
                                StatsHolder.getEndToEnd().update(msDone - simulatedEvent.getTimestamp());
                            }
                        });
                    }
                    //stats
                    countLast10s++;
                }
                if (System.currentTimeMillis() - lastThroughputTick > statSec * 1E3) {
                    //System.out.println("Avg["+myID+"] " + countLast10s/10 + " active " + executor.getPoolSize() + " pending " + executor.getQueue().size());
                    countLast10sLast = countLast10s;
                    countLast10s = 0;
                    lastThroughputTick = System.currentTimeMillis();
                }
                // going to fast compared to target rate
                if (System.currentTimeMillis() - ms < 10) {
                    Thread.sleep(Math.max(1, 10 - (System.currentTimeMillis() - ms)));
                }
            } while (true);
        } catch (Throwable t) {
            t.printStackTrace();
            System.err.println("Error receiving data from market. Did market disconnect?");
        } finally {
            EVENT_PROCESSOR_CONNECTIONS.remove(myID);
        }
    }
}
