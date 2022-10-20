package com.qapital.savings.rule;

import com.qapital.savings.event.SavingsEvent;

import java.util.List;

public interface SavingsRulesService {

    /**
     * Fetches the active savings rules for the specified user
     * @param userId the user id
     * @return a list of savings rules
     */
    List<SavingsRule> activeRulesForUser(Long userId);

    /**
     * Executes the logic for given savings rule
     * @param savingsRule the configured savings rule
     * @return a list of savings events that are the result of the execution of the rule
     */
	List<SavingsEvent> executeRule(SavingsRule savingsRule);

}
