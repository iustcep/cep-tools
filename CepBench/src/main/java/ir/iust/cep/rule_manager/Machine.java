package ir.iust.cep.rule_manager;

import java.util.List;

public class Machine {
    private Integer id;
    private Integer ramCapacity;
    private Integer cpuCapacity;
    private String ip;
    private Integer eventPort;
    private Integer ruleManagerPort;
    private List<RuleMetaData> assignedRules;
    private List<Integer> activeRulesId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRamCapacity() {
        return ramCapacity;
    }

    public void setRamCapacity(Integer ramCapacity) {
        this.ramCapacity = ramCapacity;
    }

    public Integer getCpuCapacity() {
        return cpuCapacity;
    }

    public void setCpuCapacity(Integer cpuCapacity) {
        this.cpuCapacity = cpuCapacity;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getEventPort() {
        return eventPort;
    }

    public void setEventPort(Integer eventPort) {
        this.eventPort = eventPort;
    }

    public Integer getRuleManagerPort() {
        return ruleManagerPort;
    }

    public void setRuleManagerPort(Integer ruleManagerPort) {
        this.ruleManagerPort = ruleManagerPort;
    }

    public List<RuleMetaData> getAssignedRules() {
        return assignedRules;
    }

    public void setAssignedRules(List<RuleMetaData> assignedRules) {
        this.assignedRules = assignedRules;
    }

    public List<Integer> getActiveRulesId() {
        return activeRulesId;
    }

    public void setActiveRulesId(List<Integer> activeRulesId) {
        this.activeRulesId = activeRulesId;
    }
}
