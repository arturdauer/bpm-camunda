package de.hello.first.v1.firstBpm.process.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class HelloProcessConfig {

    private String timeOutSubProcess;

    public HelloProcessConfig(@Value("${prozess.helloProcess.warteTimeOut}") String timeOutSubProcess) {
        this.timeOutSubProcess = timeOutSubProcess;
    }

    public String getTimeOutSubProcess() {
        return timeOutSubProcess;
    }
}
