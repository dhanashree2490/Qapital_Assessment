package com.qapital.savings.rule;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.qapital.savings.event.SavingsEvent;
import com.qapital.savings.event.SavingsEventResponse;

import com.qapital.savings.utils.SavingsRuleConstants;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("${app.service.path}")
public class SavingsRulesController {

	@Autowired
    private SavingsRulesService savingsRulesService;
	
	@Autowired
	private SavingsRulesValidationPolicy validationPolicy;
	
	private static final Logger LOG = LoggerFactory.getLogger(SavingsRulesController.class);

   

    @GetMapping("/active/{userId}")
    public List<SavingsRule> activeRulesForUser(@PathVariable Long userId) {
        return savingsRulesService.activeRulesForUser(userId);
    }

    
    @PostMapping(path = "/applySavingsRule", consumes="application/json", produces="application/json")
    public <T> ResponseEntity<SavingsEventResponse> executeRule(@RequestBody SavingsRule savingsRule){
    	LOG.info("Start of executeRule method");
    	
    	SavingsEventResponse resp = new SavingsEventResponse();
    	ResponseEntity<SavingsEventResponse> response = null;
    	
    	try {
    		
    		
    		StringBuilder errorFields = new StringBuilder();
            String errorMessage =  this.validationPolicy.validateSavingsRule(savingsRule, errorFields);
    		
            resp.setDate(LocalDate.now());
    		resp.setErrorCode(errorMessage);
    		resp.setErrorMessage(StringUtils.isBlank(errorFields.toString())? SavingsRuleConstants.SUCCESS
    				:errorFields.toString());
    		resp.setId(savingsRule.getId());
    		resp.setUserId(savingsRule.getUserId());
    		
    		if(StringUtils.isBlank(errorFields) && StringUtils.equals(errorMessage, 
    				SavingsRuleConstants.VALIDATE_SAVINGS_RULE_SUCCESS)) {
        	List<SavingsEvent> savingsEventList = this.savingsRulesService.executeRule(savingsRule);
        	if(CollectionUtils.isEmpty(savingsEventList)) {
        		resp.setErrorMessage(SavingsRuleConstants.NO_RESULT_MESSAGE);
        	}else {
       		 resp.setEvents(savingsEventList);

        	}
    		}
        	
        	response = new ResponseEntity<SavingsEventResponse>(resp,HttpStatus.OK);
    	}catch(Exception e) {
    		resp.setDate(LocalDate.now());
    		resp.setErrorCode(SavingsRuleConstants.VALIDATE_SAVINGS_RULE_ERROR);
    		resp.setErrorMessage(SavingsRuleConstants.INTERNAL_SERVER_EXCEPTION);
    		resp.setId(savingsRule.getId());
    		resp.setUserId(savingsRule.getUserId());
    		response = new ResponseEntity<SavingsEventResponse>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
		return response;
    	
    }


}
