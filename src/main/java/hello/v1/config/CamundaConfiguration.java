package hello.v1.config;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ResourceDefinition;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CamundaConfiguration {
    @Autowired
    private RepositoryService repositoryService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaConfiguration.class);

    @EventListener
    public void notify(final PostDeployEvent unused) {
        final List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();

        Assert.isTrue((processDefinitionList != null && !processDefinitionList.isEmpty()), "No process were found and deployed");
        LOGGER.info("Found und deploy following processes {}",
                processDefinitionList.stream().map(ResourceDefinition::getName).collect(Collectors.toList()));
    }

}
