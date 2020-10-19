package de.hello.first.v1.firstBpm.process;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProzessStarter {
    public static final String PROZESS_IS = "de.hello.first.v1";
    public static final String ANFRAGE = "anfrage";

    private RuntimeService runtimeService;

    public ProzessStarter(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public ProcessInstance starteProzessHallo(String anfrage, String businessKey) {
        Map<String, Object> nutzdaten = new HashMap<>();
        nutzdaten.put(ANFRAGE, anfrage);
        return runtimeService.startProcessInstanceByKey(PROZESS_IS, businessKey, nutzdaten);
    }
}
