package de.hello.first.v1.firstBpm.process.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class AsyncAntowrtBewertenTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {

    }
}
