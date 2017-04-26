package ir.iust.cep.rule_manager;

public class RuleMetaData {

    private Integer ruleId;
    private String ruleName;
    private String ruleStatement;

    public RuleMetaData(Integer ruleId, String ruleName, String ruleStatement) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.ruleStatement = ruleStatement;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleStatement() {
        return ruleStatement;
    }

    public void setRuleStatement(String ruleStatement) {
        this.ruleStatement = ruleStatement;
    }

    @Override
    public String toString() {
        return "RuleMetaData{" +
                "ruleId=" + ruleId +
                ", ruleName='" + ruleName + '\'' +
                ", ruleStatement='" + ruleStatement + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RuleMetaData)) return false;

        RuleMetaData that = (RuleMetaData) o;

        if (!ruleId.equals(that.ruleId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ruleId.hashCode();
    }
}
