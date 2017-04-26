package ir.iust.cep.rule_manager;

public class ResourceUsage {

    private RuleMetaData ruleMetaData;
    private Integer eventRate;
    private Integer cpuUsage;
    private Integer ramUsage;

    public ResourceUsage(RuleMetaData ruleMetaData, Integer eventRate, Integer cpuUsage, Integer ramUsage) {
        this.ruleMetaData = ruleMetaData;
        this.eventRate = eventRate;
        this.cpuUsage = cpuUsage;
        this.ramUsage = ramUsage;
    }

    public RuleMetaData getRuleMetaData() {
        return ruleMetaData;
    }

    public void setRuleMetaData(RuleMetaData ruleMetaData) {
        this.ruleMetaData = ruleMetaData;
    }

    public Integer getEventRate() {
        return eventRate;
    }

    public void setEventRate(Integer eventRate) {
        this.eventRate = eventRate;
    }

    public Integer getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Integer cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Integer getRamUsage() {
        return ramUsage;
    }

    public void setRamUsage(Integer ramUsage) {
        this.ramUsage = ramUsage;
    }
}
