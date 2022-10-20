package com.qapital.savings.rule;

import com.qapital.bankdata.transaction.Transaction;
import com.qapital.bankdata.transaction.TransactionsService;
import com.qapital.savings.event.SavingsEvent;
import com.qapital.savings.event.SavingsEvent.EventName;
import com.qapital.savings.event.SavingsGoal;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class StandardSavingsRulesService implements SavingsRulesService {

	@Autowired
    private TransactionsService transactionsService;
    
    private static final Logger LOG = LoggerFactory.getLogger(StandardSavingsRulesService.class);

    
    
    @Override
    public List<SavingsRule> activeRulesForUser(Long userId) {
        
    	SavingsRule guiltyPleasureRule = SavingsRule.createGuiltyPleasureRule(1l, userId, "Starbucks", 3.00d);
        guiltyPleasureRule.addSavingsGoal(1l);
        guiltyPleasureRule.addSavingsGoal(2l);
        SavingsRule roundupRule = SavingsRule.createRoundupRule(2l, userId, 2.00d);
        roundupRule.addSavingsGoal(1l);

        return List.of(guiltyPleasureRule, roundupRule);
    }
    
    /**
     * Executes the logic for given savings rule
     * @param savingsRule the configured savings rule
     * @return a list of savings events that are the result of the execution of the rule
     */

	@Override
    public List<SavingsEvent> executeRule(SavingsRule savingsRule) {
		
		LOG.info("Start of executeRule()");
		
		List<SavingsEvent> result = new ArrayList<>();
		
		List<Transaction> transactionDetails = transactionsService.latestTransactionsForUser(savingsRule.getUserId());
			this.generateSavingsEvent(transactionDetails,result,savingsRule);
		
		LOG.info("End of executeRule()");

		return result;
    }

	/**
     * Applies the logic based on savings rule
     * @param savingsRule the configured savings rule
     * @param savingsevent list
     * @param transaction details
     * @return a list of savings events that are the result of the execution of the rule
     * @author Dhanashree
     */

	private void generateSavingsEvent(List<Transaction> transactionDetails, List<SavingsEvent> result, SavingsRule savingsRule) {
		
		LOG.debug("Start of generateSavingsEvent()");

		if(!CollectionUtils.isEmpty(transactionDetails)) {
				switch(savingsRule.getRuleType()) {
				case roundup :  
					for(Transaction transaction:transactionDetails) {
						//From the problem statment it was left for assumption that rule will be applied 
						//for all transactions so leaving this in commented section
						//&& StringUtils.equals(transaction.getDescription(), savingsRule.getPlaceDescription())//
						if(transaction.getAmount() < 0 ) {
							SavingsEvent savingsEvent = new SavingsEvent();
							List<SavingsGoal> savingGoalsList = new ArrayList<>();
							this.applyRoundUpRule(transaction.getAmount(),savingsRule,savingsEvent);
							
							savingsEvent.setDate(transaction.getDate());
							savingsEvent.setDescription(transaction.getDescription());
							for(int indx =0; indx < savingsRule.getSavingsGoalIds().size();indx++) {
								SavingsGoal savingsGoal = new SavingsGoal();

								savingsGoal.setGoalId(savingsRule.getSavingsGoalIds().get(indx));
								savingsGoal.setAmount(savingsEvent.getAmount()/savingsRule.getSavingsGoalIds().size());
								savingGoalsList.add(savingsGoal);
							}
							savingsEvent.setSavingGoals(savingGoalsList);
							result.add(savingsEvent);
						}
					}
						break;
						
				case guiltypleasure:
					for(Transaction transaction:transactionDetails) {
							if(transaction.getAmount() < 0 && StringUtils.equals(transaction.getDescription(), savingsRule.getPlaceDescription())) {
								SavingsEvent savingsEvent = new SavingsEvent();
								List<SavingsGoal> savingGoalsList = new ArrayList<>();	
							    this.applyGuiltyPleasure(transaction.getDescription(),savingsRule,savingsEvent);
							    
								savingsEvent.setDate(transaction.getDate());
								savingsEvent.setDescription(transaction.getDescription());

								for(int indx =0; indx < savingsRule.getSavingsGoalIds().size();indx++) {
									SavingsGoal savingsGoal = new SavingsGoal();

									savingsGoal.setGoalId(savingsRule.getSavingsGoalIds().get(indx));
									savingsGoal.setAmount(savingsEvent.getAmount()/savingsRule.getSavingsGoalIds().size());
									savingGoalsList.add(savingsGoal);
								}
								savingsEvent.setSavingGoals(savingGoalsList);
								result.add(savingsEvent);
							}
											
					}
					break;
				default:
					break;
				
				}
				
		}
		
		LOG.debug("End of generateSavingsEvent()");

	}

	private void applyGuiltyPleasure(String transactionDesc, SavingsRule savingsRule, SavingsEvent savingsEvent) {
		LOG.debug("Start of applyGuiltyPleasure()");
		DecimalFormat fomatter = new DecimalFormat("###.##");
		savingsEvent.setAmount(Double.valueOf(fomatter.format(savingsRule.getAmount())));
		savingsEvent.setCreated(Instant.now());
		savingsEvent.setRuleType(savingsRule.getRuleType());
		savingsEvent.setSavingsRuleId(savingsRule.getId());
		savingsEvent.setEventName(EventName.rule_application);
		savingsEvent.setSavingsTransferId(1L);
		savingsEvent.setTriggerId(1L);
	}

	private void applyRoundUpRule(Double transactionAmt, SavingsRule savingsRule, SavingsEvent savingsEvent) {
		
		LOG.debug("Start of applyRoundUpRule()");
		
		DecimalFormat fomatter = new DecimalFormat("###.##");
		Double transAmt = Math.abs(transactionAmt);
		Double savingsAmt = Math.abs(savingsRule.getAmount());
		Double roundoffAmt = Math.ceil(transAmt/savingsAmt) * savingsAmt;
		Double savedAmt = roundoffAmt - transAmt;
		savingsEvent.setAmount(Double.valueOf(fomatter.format(savedAmt)));
		savingsEvent.setCreated(Instant.now());
		savingsEvent.setRuleType(savingsRule.getRuleType());
		savingsEvent.setSavingsRuleId(savingsRule.getId());
		savingsEvent.setEventName(EventName.rule_application);
		savingsEvent.setSavingsTransferId(1L);
		savingsEvent.setTriggerId(1L);
		
		LOG.debug("End of applyRoundUpRule()");
		
	}

}


