package com.qapital.savings.event;

import com.qapital.savings.rule.SavingsRule;
import com.qapital.savings.rule.SavingsRule.RuleType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * A Savings Event represents an event in the history of a Savings Goal.
 * Events can be either monetary (triggered by the application of Savings Rules,
 * manual transfers, interest payments or incentive payouts), or other events
 * of significance in the history of the goal, such as pausing or unpausing
 * Savings Rules or other users joining or leaving a shared goal.
 */
public class SavingsEvent {
	
	
	//private Long savingsGoalId;
	private Long savingsRuleId;
	private EventName eventName;
	private Double amount;
	private Long triggerId;
	private RuleType ruleType;
	private Long savingsTransferId;
	private Boolean cancelled;
	private Instant created;
	private LocalDate date;
	private String description;
	protected List<SavingsGoal> savingGoals; 
	

    
	public SavingsEvent() {}

	public SavingsEvent(Long userId, Long savingsGoalId, Long savingsRuleId, EventName eventName, LocalDate date, Double amount, Long triggerId, SavingsRule savingsRule) {
		//this.userId = userId;
		//this.savingsGoalId = savingsGoalId;
		this.savingsRuleId = savingsRuleId;
		this.eventName = eventName;
		this.setDate(date);
		this.amount = amount;
		this.triggerId = triggerId;
		this.ruleType = savingsRule.getRuleType();
		this.created = Instant.now();
	}
	
	public Long getSavingsRuleId() {
		return savingsRuleId;
	}
	
	public void setSavingsRuleId(Long savingsRuleId) {
		this.savingsRuleId = savingsRuleId;
	}

	public EventName getEventName() {
		return eventName;
	}
	
	public void setEventName(EventName eventName) {
		this.eventName = eventName;
	}
	
	public double getAmount() {
		if (amount == null) {
			return 0d;
		} else {
			return amount;
		}
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Long getTriggerId() {
		return triggerId;
	}
	
	public void setTriggerId(Long triggerTransactionId) {
		this.triggerId = triggerTransactionId;
	}
	
	public RuleType getRuleType() {
		return ruleType;
	}
	
	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}
	
	public Long getSavingsTransferId() {
		return savingsTransferId;
	}
	
	public void setSavingsTransferId(Long savingsTransferId) {
		this.savingsTransferId = savingsTransferId;
	}

	public boolean isCancelled() {
		if (cancelled == null) {
			return false;
		} else {
			return cancelled;
		}
	}
	
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Instant getCreated() {
		return created;
	}

	public void setCreated(Instant created) {
		this.created = created;
	}

	public enum EventName {
		manual, started, stopped, rule_application, ifttt_transfer, joined, withdrawal, internal_transfer, cancellation, incentive_payout, interest
	}	
	
	public List<SavingsGoal> getSavingGoals() {
		return savingGoals;
	}

	public void setSavingGoals(List<SavingsGoal> savingGoals) {
		this.savingGoals = savingGoals;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
