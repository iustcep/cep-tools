package ir.iust.cep.collector.collector;

import ir.iust.cep.collector.event.CollectorEvent;

import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class EventProcessorConnection extends Thread {

    static Map<Integer, EventProcessorConnection> EVENT_PROCESSOR_CONNECTIONS = Collections.synchronizedMap(new HashMap<Integer, EventProcessorConnection>());

    public static void dumpStats(int statSec) {
        long totalCount = 0;
        int cnx = 0;
        EventProcessorConnection any = null;
        for (EventProcessorConnection m : EVENT_PROCESSOR_CONNECTIONS.values()) {
            cnx++;
            totalCount += m.countForStatSecLast;
            any = m;
        }
        if (any != null) {
            System.out.printf("Throughput %.0f (active %d pending %d cnx %d)\n",
                    (float) totalCount / statSec,
                    any.executor == null ? 0 : any.executor.getCorePoolSize(),
                    any.executor == null ? 0 : any.executor.getQueue().size(),
                    cnx
            );
        }
    }

    private SocketChannel socketChannel;
    private ThreadPoolExecutor executor;//this guy is shared
    private final int statSec;
    private long countForStatSec = 0;
    private long countForStatSecLast = 0;
    private long lastThroughputTick = System.currentTimeMillis();
    private int myID;
    private static int ID = 0;

    public EventProcessorConnection(SocketChannel socketChannel, ThreadPoolExecutor executor, int statSec) {
        super("EsperCollector-cnx-" + ID++);
        this.socketChannel = socketChannel;
        this.executor = executor;
        this.statSec = statSec;
        myID = ID - 1;

        EVENT_PROCESSOR_CONNECTIONS.put(myID, this);
    }

    public void run() {
        try {
            do {
                final CollectorEvent.Event theEvent = CollectorEvent.Event.parseDelimitedFrom(socketChannel.socket().getInputStream());
                if (theEvent.getEventId() > Collector.lastReceivedEventId.intValue()) {
                    Collector.lastReceivedEventId.incrementAndGet();
                    if (executor == null) {
                        long ns = System.nanoTime();
                        long nsDone = System.nanoTime();
                        long msDone = System.currentTimeMillis();
                        StatsHolder.getEngine().update(nsDone - ns);
                        StatsHolder.getEndToEnd().update(msDone - theEvent.getTimestamp());
                    } else {
                        executor.execute(new Runnable() {
                            public void run() {
                                long ns = System.nanoTime();
                                long nsDone = System.nanoTime();
                                long msDone = System.currentTimeMillis();
                                StatsHolder.getEngine().update(nsDone - ns);
                                StatsHolder.getCollector().update(nsDone - System.nanoTime());
                                StatsHolder.getEndToEnd().update(msDone - theEvent.getTimestamp());
                                //TODO send for subscribers
                            }
                        });
                    }
                    //stats
                    countForStatSec++;
                }

                //TODO
                if (System.currentTimeMillis() - lastThroughputTick > statSec * 1E3) {
                    countForStatSecLast = countForStatSec;
                    countForStatSec = 0;
                    lastThroughputTick = System.currentTimeMillis();
                }
            } while (true);
        } catch (Throwable t) {
            t.printStackTrace();
            System.err.println("Error receiving data from event processor. Did ep disconnect?");
        } finally {
            EVENT_PROCESSOR_CONNECTIONS.remove(myID);
            StatsHolder.remove(StatsHolder.getEngine());
            StatsHolder.remove(StatsHolder.getCollector());
            StatsHolder.remove(StatsHolder.getEndToEnd());
        }
    }
}
