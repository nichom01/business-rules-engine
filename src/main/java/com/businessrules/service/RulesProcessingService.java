package com.businessrules.service;

import com.businessrules.model.ProcessingResult;
import com.businessrules.model.RuleContext;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class RulesProcessingService {

    private static final Logger log = LoggerFactory.getLogger(RulesProcessingService.class);
    private final KieContainer kieContainer;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public RulesProcessingService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public ProcessingResult processObjects(List<Map<String, Object>> objects) {
        List<CompletableFuture<RuleContext>> futures = objects.stream()
                .map(obj -> CompletableFuture.supplyAsync(() -> processObject(obj), executorService))
                .collect(Collectors.toList());

        List<RuleContext> contexts = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        return aggregateResults(contexts);
    }

    private RuleContext processObject(Map<String, Object> object) {
        // Determine rule group based on object type or metadata
        String ruleGroup = determineRuleGroup(object);
        
        RuleContext context = new RuleContext(object, ruleGroup);
        // Use default session - rules filter themselves based on ruleGroup property
        StatelessKieSession kieSession = kieContainer.newStatelessKieSession();
        
        try {
            kieSession.execute(context);
        } catch (Exception e) {
            log.error("Error executing rules for object: {}", object, e);
            context.addError("system", "rule-execution", "Failed to execute rules: " + e.getMessage());
        }
        
        return context;
    }

    private String determineRuleGroup(Map<String, Object> object) {
        // Default rule group - can be customized based on object properties
        Object type = object.get("type");
        if (type != null && type.toString().equals("order")) {
            return "validation-rules";
        }
        return "transformation-rules";
    }

    private ProcessingResult aggregateResults(List<RuleContext> contexts) {
        List<com.businessrules.model.ValidationError> allErrors = new ArrayList<>();
        List<Map<String, Object>> transformedData = new ArrayList<>();

        for (RuleContext context : contexts) {
            allErrors.addAll(context.getErrors());
            if (context.getErrors().isEmpty()) {
                transformedData.add(context.getTransformedData().isEmpty() 
                    ? context.getData() 
                    : context.getTransformedData());
            }
        }

        boolean success = allErrors.isEmpty();
        return ProcessingResult.builder()
                .success(success)
                .message(success ? "All objects processed successfully" : "Processing completed with errors")
                .errors(allErrors)
                .transformedData(transformedData)
                .build();
    }
}
