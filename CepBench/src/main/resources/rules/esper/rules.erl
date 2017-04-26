
RULES_NUMBER = 4

RULE_1 = select null from pattern [every (e1=event(Integer.parseInt(filedValueByTag('price')) > 30) -> e2=event(Integer.parseInt(filedValueByTag('price')) > Integer.parseInt(e1.filedValueByTag('price'))) -> e3=event(Integer.parseInt(filedValueByTag('price')) > Integer.parseInt(e3.filedValueByTag('price')))) where timer:within(60 sec)]

RULE_2 = select null from pattern [every (e1=event(Integer.parseInt(filedValueByTag('price')) > 30 and Integer.parseInt(filedValueByTag('weight')) < 60) -> e2=event(Integer.parseInt(filedValueByTag('price')) < 30 and Integer.parseInt(filedValueByTag('weight')) > 60)) where timer:within(60 sec)]

RULE_3 = select null from pattern [every (e1=event(Integer.parseInt(filedValueByTag('price')) > 30 or Integer.parseInt(filedValueByTag('weight')) < 60) -> e2=event(Integer.parseInt(filedValueByTag('price')) < 30 or Integer.parseInt(filedValueByTag('weight')) > 60)) where timer:within(60 sec)]

RULE_4 = select null from pattern [every (e1=event(Integer.parseInt(filedValueByTag('count')) > 10 and Integer.parseInt(filedValueByTag('price')) > 50 and Integer.parseInt(filedValueByTag('weight')) > 10 and Integer.parseInt(filedValueByTag('discount')) > 50) -> e2=event(Integer.parseInt(filedValueByTag('count')) < 90 and Integer.parseInt(filedValueByTag('price')) < 90 and Integer.parseInt(filedValueByTag('weight')) < 90 and Integer.parseInt(filedValueByTag('discount')) < 90)) where timer:within(60 sec)]


RULE_10 = select null from pattern [every event where timer:within(60 sec)]

RULE_0 = select null from pattern [every e1=event(filedValueByTag('s') = "s") where timer:within(60 sec)]
