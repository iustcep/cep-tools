
RULES_NUMBER = 10

RULE_1 = select null from pattern [(every e1=event(eventType=iust.cep.model.EventType.A, value>50) -> e2=event(eventType=iust.cep.model.EventType.B, value>50) where timer:within(5 sec))]

RULE_2 = select null from pattern [every e1=event(eventType = iust.cep.model.EventType.A)]

RULE_3 = select null from pattern [every e1=event(eventType = iust.cep.model.EventType.A)]

RULE_4 = select null from pattern [every e1=event(eventType = iust.cep.model.EventType.A)]

RULE_5 = select null from pattern [every e1=event(eventType = iust.cep.model.EventType.A)]

RULE_6 = select null from pattern [every e1=event(eventType = iust.cep.model.EventType.A)]

RULE_7 = select null from pattern [every e1=event(eventType = iust.cep.model.EventType.A)]

RULE_8 = select null from pattern [every e1=event(eventType = iust.cep.model.EventType.A)]

RULE_9 = select null from pattern [every e1=event(eventType = iust.cep.model.EventType.A)]

RULE_10 = select null from pattern [every e1=event(eventType = iust.cep.model.EventType.A)]

