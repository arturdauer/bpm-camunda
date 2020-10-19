package de.hello.first.v1.firstBpm.inbound;

import de.hello.first.v1.firstBpm.process.ProzessStarter;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private ProzessStarter prozessStarter;

    public HelloController(ProzessStarter prozessStarter) {
        this.prozessStarter = prozessStarter;
    }

    @PostMapping("/hallo")
    public ResponseEntity<String> starteHelloProzess(@RequestBody String anfrage) {
        ProcessInstance processInstance = prozessStarter.starteProzessHallo(anfrage, "12345");
        return ResponseEntity.ok(processInstance.getRootProcessInstanceId());
    }
}
