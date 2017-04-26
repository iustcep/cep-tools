package ir.iust.cep.collector.collector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Collector extends Thread {

    private int port;
    private int threadCore;
    private int queueMax;
    private int sleepListenerMillis;
    private int statSec;
    private int simulationRate;
    private int simulationThread;
    private String mode;

    public static final int DEFAULT_PORT = 6789;
    public static final int DEFAULT_THREADCORE = Runtime.getRuntime().availableProcessors();
    public static final int DEFAULT_QUEUEMAX = -1;
    public static final int DEFAULT_SLEEP = 0;
    public static final int DEFAULT_SIMULATION_RATE = -1;//-1: no simulation
    public static final int DEFAULT_SIMULATION_THREAD = -1;//-1: no simulation
    public static final int DEFAULT_STAT = 1;
    public static final String DEFAULT_MODE = "NOOP";
    public static final Properties MODES = new Properties();

    public static final int RULE_ID = 1;
    public static AtomicLong lastReceivedEventId = new AtomicLong(0);

    private ThreadPoolExecutor executor;//can be null

    public Collector(String mode, int port, int threads, int queueMax, int sleep, final int statSec, int simulationThread, final int simulationRate) {
        super("Collector-main");
        this.mode = mode;
        this.port = port;
        this.threadCore = threads;
        this.queueMax = queueMax;
        this.sleepListenerMillis = sleep;
        this.statSec = statSec;
        this.simulationThread = simulationThread;
        this.simulationRate = simulationRate;

        // turn on stat dump
        Timer t = new Timer("Collector-stats", true);
        t.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                StatsHolder.dump("engine");
                StatsHolder.dump("collector");
                StatsHolder.dump("endToEnd");
                StatsHolder.reset();
                if (simulationRate <= 0) {
                    EventProcessorConnection.dumpStats(statSec);
                } else {
                    SimulateEventProcessorConnection.dumpStats(statSec);
                }
            }
        }, 0L, statSec * 1000);
    }

    public synchronized void start() {
        if ("NOOP".equals(mode)) {
        } else {
            String stmtString = Collector.MODES.getProperty(mode);
            System.out.println("Using " + mode + " : " + stmtString);
        }

        // start thread pool if any
        if (queueMax < 0) {
            executor = null;
            System.out.println("Using direct handoff, cpu#" + Runtime.getRuntime().availableProcessors());
        } else {
            // executor
            System.out.println("Using ThreadPoolExecutor, cpu#" + Runtime.getRuntime().availableProcessors() + ", threadCore#" + threadCore + " queue#" + queueMax);
            BlockingQueue<Runnable> queue;
            if (queueMax == 0) {
                queue = new SynchronousQueue<Runnable>(true);//enforce fairness
            } else {
                queue = new LinkedBlockingQueue<Runnable>(queueMax);
            }

            executor = new ThreadPoolExecutor(
                    threadCore,
                    Runtime.getRuntime().availableProcessors() * threadCore,
                    10, TimeUnit.SECONDS,
                    queue,
                    new ThreadFactory() {
                        long count = 0;

                        public Thread newThread(Runnable r) {
                            System.out.println("Create EsperCollector thread " + (count + 1));
                            return new Thread(r, "EsperCollector-" + count++);
                        }
                    },
                    new ThreadPoolExecutor.CallerRunsPolicy() {
                        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                            super.rejectedExecution(r, e);
                        }
                    }
            );
            executor.prestartAllCoreThreads();
        }

        super.start();
    }

    public void run() {
        if (simulationRate <= 0) {
            runCollector();
        } else {
            runSimulation();
        }
    }

    public void runCollector() {
        try {
            System.out.println((new StringBuilder("Collector accepting connections on port ")).append(port).append(".").toString());
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            do {
                SocketChannel socketChannel = serverSocketChannel.accept();
                System.out.println("Event Processor connected to collector.");
                (new EventProcessorConnection(socketChannel, executor, statSec)).start();
            } while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void runSimulation() {
        System.out.println("Collector in sumulation mode with event/s "
                + simulationThread + " x " + simulationRate
                + " = " + simulationThread * simulationRate
        );
        SimulateEventProcessorConnection[] sims = new SimulateEventProcessorConnection[simulationThread];
        for (int i = 0; i < sims.length; i++) {
            sims[i] = new SimulateEventProcessorConnection(simulationRate, executor, statSec);
            sims[i].start();
        }

        try {
            for (SimulateEventProcessorConnection sim : sims) {
                sim.join();
            }
        } catch (InterruptedException e) {
            ;
        }
    }

    public static void main(String argv[]) throws IOException {
        // load modes
//        MODES.load(Collector.class.getClassLoader().getResourceAsStream("statements.properties"));
        MODES.put("NOOP", "");

        int port = DEFAULT_PORT;
        int threadCore = DEFAULT_THREADCORE;
        int queueMax = DEFAULT_QUEUEMAX;
        int sleep = DEFAULT_SLEEP;
        int simulationRate = DEFAULT_SIMULATION_RATE;
        int simulationThread = DEFAULT_SIMULATION_THREAD;
        String mode = DEFAULT_MODE;
        int stats = DEFAULT_STAT;
        for (int i = 0; i < argv.length; i++)
            if ("-port".equals(argv[i])) {
                i++;
                port = Integer.parseInt(argv[i]);
            } else if ("-thread".equals(argv[i])) {
                i++;
                threadCore = Integer.parseInt(argv[i]);
            } else if ("-queue".equals(argv[i])) {
                i++;
                queueMax = Integer.parseInt(argv[i]);
            } else if ("-sleep".equals(argv[i])) {
                i++;
                sleep = Integer.parseInt(argv[i]);
            } else if ("-stat".equals(argv[i])) {
                i++;
                stats = Integer.parseInt(argv[i]);
            } else if ("-mode".equals(argv[i])) {
                i++;
                mode = argv[i];
                if (MODES.getProperty(mode) == null) {
                    System.err.println("Unknown mode");
                    printUsage();
                }
            } else if ("-rate".equals(argv[i])) {
                i++;
                int xIndex = argv[i].indexOf('x');
                simulationThread = Integer.parseInt(argv[i].substring(0, xIndex));
                simulationRate = Integer.parseInt(argv[i].substring(xIndex + 1));
            } else {
                printUsage();
            }

        Collector bs = new Collector(mode, port, threadCore, queueMax, sleep, stats, simulationThread, simulationRate);
        bs.start();
        try {
            bs.join();
        } catch (InterruptedException e) {
            ;
        }
    }

    private static void printUsage() {
        System.err.println("usage: Collector <-port #> <-thread #> <-queue #> <-sleep #> <-stat #> <-rate #x#> <-mode xyz>");
        System.err.println("defaults:");
        System.err.println("  -port:    " + DEFAULT_PORT);
        System.err.println("  -thread:  " + DEFAULT_THREADCORE);
        System.err.println("  -queue:   " + DEFAULT_QUEUEMAX + "(-1: no executor, 0: SynchronousQueue, n: LinkedBlockingQueue");
        System.err.println("  -sleep:   " + DEFAULT_SLEEP + "(no sleep)");
        System.err.println("  -stat:   " + DEFAULT_STAT + "(s)");
        System.err.println("  -rate:    " + DEFAULT_SIMULATION_RATE + "(no standalone simulation, else <n>x<evt/s> such as 2x1000)");
        System.err.println("  -mode:    " + "(default " + DEFAULT_MODE + ", choose from " + MODES.keySet().toString() + ")");
        System.err.println("Modes are read from statements.properties in the classpath");
        System.exit(1);
    }

}
