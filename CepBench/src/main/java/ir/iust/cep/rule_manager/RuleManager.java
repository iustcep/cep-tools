package ir.iust.cep.rule_manager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by ASUS on 3/18/2017.
 */
public class RuleManager extends Thread {

    private static int port;
    private static HashMap<RuleMetaData,List<ResourceUsage>> costModelHashMap;
    private static HashMap<Integer,Machine> machineHashMap;

    private static HashMap<Integer,List<Machine>> ruleMetaDataHashMap;

    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
        RuleManager.port = port;
    }

    public static HashMap<RuleMetaData, List<ResourceUsage>> getCostModelHashMap() {
        return costModelHashMap;
    }

    public static void setCostModelHashMap(HashMap<RuleMetaData, List<ResourceUsage>> costModelHashMap) {
        RuleManager.costModelHashMap = costModelHashMap;
    }

    public static HashMap<Integer, Machine> getMachineHashMap() {
        return machineHashMap;
    }

    public static void setMachineHashMap(HashMap<Integer, Machine> machineHashMap) {
        RuleManager.machineHashMap = machineHashMap;
    }

    public static HashMap<Integer, List<Machine>> getRuleMetaDataHashMap() {
        return ruleMetaDataHashMap;
    }

    public static void setRuleMetaDataHashMap(HashMap<Integer, List<Machine>> ruleMetaDataHashMap) {
        RuleManager.ruleMetaDataHashMap = ruleMetaDataHashMap;
    }

    public static final int DEFAULT_PORT = 6789;

    private ThreadPoolExecutor executor;//can be null

    public RuleManager(int port) {
        super("RuleManager-main");
        this.port = port;
    }

    public synchronized void start() {
            // executor
//            System.out.println("Using ThreadPoolExecutor, cpu#" + Runtime.getRuntime().availableProcessors() + ", threadCore#" + threadCore + " queue#" + queueMax);
            BlockingQueue<Runnable> queue;
            queue = new SynchronousQueue<>(true);//enforce fairness

            executor = new ThreadPoolExecutor(
                    8,
                    Runtime.getRuntime().availableProcessors() * 8,
                    10, TimeUnit.SECONDS,
                    queue,
                    new ThreadFactory() {
                        long count = 0;

                        public Thread newThread(Runnable r) {
                            System.out.println("Create EsperRuleManager thread " + (count + 1));
                            return new Thread(r, "EsperRuleManager-" + count++);
                        }
                    },
                    new ThreadPoolExecutor.CallerRunsPolicy() {
                        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                            super.rejectedExecution(r, e);
                        }
                    }
            );
            executor.prestartAllCoreThreads();


        super.start();
    }

    public void run() {
        runRuleManager();
    }

    public void runRuleManager() {
        try {
            System.out.println((new StringBuilder("Collector accepting connections on port ")).append(port).append(".").toString());
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            do {
                SocketChannel socketChannel = serverSocketChannel.accept();
                System.out.println("Event Processor connected to collector.");
                (new EventProcessorRequest(socketChannel, executor)).start();
            } while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String argv[]) throws IOException {

        int port = DEFAULT_PORT;

        //TODO initialize fields

        RuleManager bs = new RuleManager(port);
        bs.start();
        try {
            bs.join();
        } catch (InterruptedException e) {
            ;
        }
    }

    private static void printUsage() {
        System.err.println("usage: RuleManager <-port #> <-thread #> <-queue #> <-sleep #> <-stat #> <-rate #x#> <-mode xyz>");
        System.err.println("defaults:");
        System.err.println("  -port:    " + DEFAULT_PORT);
        System.exit(1);
    }
}
