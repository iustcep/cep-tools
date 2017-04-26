package ir.iust.cep.rule_manager;

import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

public class EventProcessorRequest extends Thread {

    static Map<Integer, EventProcessorRequest> EVENT_PROCESSOR_REQUESTS = Collections.synchronizedMap(new HashMap<Integer, EventProcessorRequest>());

    private SocketChannel socketChannel;
    private ThreadPoolExecutor executor;//this guy is shared
    private int myID;
    private static int ID = 0;

    public EventProcessorRequest(SocketChannel socketChannel, ThreadPoolExecutor executor) {
        super("EsperRuleManager-cnx-" + ID++);
        this.socketChannel = socketChannel;
        this.executor = executor;
        myID = ID - 1;

        EVENT_PROCESSOR_REQUESTS.put(myID, this);
    }

    public void run() {
        try {
            do {
                final EPRequest.Event theEvent = EPRequest.Event.parseDelimitedFrom(socketChannel.socket().getInputStream());
                if (executor == null) {
                    continue;
                } else {
                    executor.execute(() -> {

                        List<EPRequest.Event.RuleRate> ruleRatesList = theEvent.getRuleRatesList();

                        //get id of machine from received packet using ip address or add id filed to event request
                        Machine targetMachine = RuleManager.getMachineHashMap().get(1);
                        Integer cpuCapacity = targetMachine.getCpuCapacity();
                        Integer ramCapacity = targetMachine.getRamCapacity();

                        Integer cpuUsed = 0;
                        Integer ramUsed = 0;

                        for (EPRequest.Event.RuleRate ruleRate : ruleRatesList) {
                            int rule_id = ruleRate.getRuleId();
                            int rate = ruleRate.getRuleRate();

                            List<ResourceUsage> resourceUsages = RuleManager.getCostModelHashMap().get(new RuleMetaData(rule_id, null, null));

                            for (ResourceUsage resourceUsage : resourceUsages) {
                                //TODO get from static cost model or compute resource usage from metadata
                                if (resourceUsage.getEventRate() == rate) {
                                    cpuUsed += resourceUsage.getCpuUsage();
                                    ramUsed += resourceUsage.getRamUsage();
                                }
                            }
                        }

                        if (cpuUsed > cpuCapacity || ramUsed > ramCapacity) {
                            System.out.println("critical load on machine " + 1);

                            //update data - request for resource usage in other machines
                            //better idea - add a module for monitor rate of each event type - single point failure maybe

                            HashSet<Machine> machinesForUpdate = new HashSet<Machine>();
                            for (EPRequest.Event.RuleRate ruleRate : ruleRatesList) {
                                machinesForUpdate.addAll(RuleManager.getRuleMetaDataHashMap().get(ruleRate.getRuleId()));
                            }

                        }

                        //TODO send for EP
                    });
                }
            } while (true);
        } catch (Throwable t) {
            t.printStackTrace();
            System.err.println("Error receiving data from event processor. Did ep disconnect?");
        } finally {
            EVENT_PROCESSOR_REQUESTS.remove(myID);
        }
    }
}
