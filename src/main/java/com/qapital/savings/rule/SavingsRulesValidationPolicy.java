package com.qapital.savings.rule;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qapital.savings.utils.SavingsRuleConstants;

@Component
public class SavingsRulesValidationPolicy {

	/*
	 * LOGGER
	 */
	
	private static final Logger LOG = LoggerFactory.getLogger(SavingsRulesValidationPolicy.class);

	
	public String validateSavingsRule(SavingsRule savingsRule, StringBuilder errorFields) {
		
		LOG.debug("Start of validateSavingsRule()");
	
		if(Objects.nonNull(savingsRule)) {
			if(!Double.isFinite(savingsRule.getAmount())){
				errorFields.append(SavingsRuleConstants.VALIDATE_ERROR_MESSAGE +"Amount");
			}
			if(savingsRule.getUserId() == 0L && StringUtils.isBlank(errorFields)){
				errorFields.append(SavingsRuleConstants.VALIDATE_ERROR_MESSAGE +"UserId");
			}else if( savingsRule.getUserId() != null && StringUtils.isNotBlank(errorFields)) {
				errorFields.append(",UserId");
			}
				
		}
		
		LOG.debug("End of validateSavingsRule()");
		return StringUtils.isBlank(errorFields) ? SavingsRuleConstants.VALIDATE_SAVINGS_RULE_SUCCESS :
										SavingsRuleConstants.VALIDATE_SAVINGS_RULE_ERROR;
		
	}
	
	

}
