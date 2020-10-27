package de.hello.first.v1.firstBpm.process.delegate;

import de.hello.first.v1.firstBpm.process.config.HelloProcessConfig;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.autoMock;
import static org.camunda.bpm.extension.mockito.CamundaMockito.getJavaDelegateMock;
import static org.junit.Assert.*;

@Deployment(resources = {"bpmn/edmProzess.bpmn", "bpmn/halloProzess.bpmn"})
public class SayHalloProcessTest {

//    @Rule
//    public ProcessEngineRule processEngineRule = new ProcessEngineRule();
    @Rule
    @ClassRule
    public static ProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder.create().build();

    @Before
    public void  setUp() {
        Mocks.register("helloProcessConfig", new HelloProcessConfig("PT10S"));
    }

    private static final String PROCESS_RESOURCE = "bpmn/halloProzess.bpmn";
    private static final String HELLO_PROCESS_PROCESS_KEY = "de.hello.bpm.first.hello.v1";

    private static final String EDM_PROCESS_PROCESS_KEY = "de.home.bpm.edm.auftragaenderung.v1";

    private static final String ERROR_SUB_PROCESS = "Error_ErrorSubProcess";
    private static final String MESSAGE_SUB_PROCESS_STARTEN = "Message_SubBearbeitungStarten";
    private static final String MESSAGE_ASYC_ANTWORT_EMPFANGEN = "Message_RueckmeldungEmpfangen";

    @Test
    public void testHeppyPath() {
        autoMock(PROCESS_RESOURCE);
        ProcessInstance pi = runtimeService().startProcessInstanceByKey(HELLO_PROCESS_PROCESS_KEY);

        assertThat(pi).isActive();
        assertThat(pi).isWaitingAt("StartEvent_hello");
        execute(job());

        assertThat(pi).isWaitingAt("Task_sayHello");
        getJavaDelegateMock("sayHalloTask").onExecutionSetVariable("warteAufSubProcess", Boolean.FALSE);
        execute(job());

        assertThat(pi).isWaitingAt("Task_HelloBewerten");
        execute(job());

        assertThat(pi).hasPassed("Event_EndHello");
        assertThat(pi).isEnded();

    }

    @Test
    public void testWennAuftragAsyncVerarbeitetWird() {

        autoMock(PROCESS_RESOURCE);
        ProcessInstance pi = runtimeService().startProcessInstanceByKey(HELLO_PROCESS_PROCESS_KEY);

        assertThat(pi).isActive();
        assertThat(pi).isWaitingAt("StartEvent_hello");
        execute(job());

        assertThat(pi).isWaitingAt("Task_sayHello");
        getJavaDelegateMock("sayHalloTask").onExecutionSetVariable("warteAufSubProcess", Boolean.TRUE);
        execute(job());

        assertThat(pi).hasPassed("Event_SubProzessStarten");

        assertThat(pi).isWaitingAt("Event_SubBearbeitungStarten");
        execute(job(jobQuery().activityId("Event_SubBearbeitungStarten")));

        assertThat(pi).isWaitingAt("Activity_RueckmeldungEmpfangen");
        execute(job());

        assertThat(pi).isWaitingAt("Activity_RueckmeldungEmpfangen");
        execute(job());

        assertThat(pi).isWaitingFor(MESSAGE_ASYC_ANTWORT_EMPFANGEN);
        runtimeService().createMessageCorrelation(MESSAGE_ASYC_ANTWORT_EMPFANGEN) //
                .processInstanceId(pi.getRootProcessInstanceId()).correlate();
        assertThat(pi).hasPassed("Activity_RueckmeldungEmpfangen");

        assertThat(pi).isWaitingAt("Activity_AsyncAntwortBewerten");
        execute(job(jobQuery().activityId("Activity_AsyncAntwortBewerten")));

        assertThat(pi).isWaitingAt("Task_HelloBewerten");
        execute(job(jobQuery().activityId("Task_HelloBewerten")));

        assertThat(pi).hasPassed("Event_EndHello");
        execute(job());
        assertThat(pi).isEnded();

    }

    @Test
    public void testWennAuftragMitTimeOutAsyncVerarbeitetWird() {

        autoMock(PROCESS_RESOURCE);
        ProcessInstance pi = runtimeService().startProcessInstanceByKey(HELLO_PROCESS_PROCESS_KEY);

        assertThat(pi).isActive();
        assertThat(pi).isWaitingAt("StartEvent_hello");
        execute(job());

        assertThat(pi).isWaitingAt("Task_sayHello");
        getJavaDelegateMock("sayHalloTask").onExecutionSetVariable("warteAufSubProcess", Boolean.TRUE);
        execute(job());

        assertThat(pi).hasPassed("Event_SubProzessStarten");

        assertThat(pi).isWaitingAt("Event_SubBearbeitungStarten");
        execute(job(jobQuery().activityId("Event_SubBearbeitungStarten")));

        assertThat(pi).isWaitingAt("Activity_RueckmeldungEmpfangen");
        execute(job());

        assertThat(pi).isWaitingAt("Activity_RueckmeldungEmpfangen");
        execute(job());

        assertThat(pi).isWaitingFor(MESSAGE_ASYC_ANTWORT_EMPFANGEN);

        // TimeOut fire
        assertThat(pi).isWaitingAt("Event_TimeOut");
        execute(job(jobQuery().activityId("Event_TimeOut")));

        assertThat(pi).hasPassed("Activity_AmInformieren");
        assertThat(pi).isActive();
        // Ende TimeOut fire

        runtimeService().createMessageCorrelation(MESSAGE_ASYC_ANTWORT_EMPFANGEN) //
                .processInstanceId(pi.getRootProcessInstanceId()).correlate();
        assertThat(pi).hasPassed("Activity_RueckmeldungEmpfangen");

        assertThat(pi).isWaitingAt("Activity_AsyncAntwortBewerten");
        execute(job(jobQuery().activityId("Activity_AsyncAntwortBewerten")));

        assertThat(pi).isWaitingAt("Task_HelloBewerten");
        execute(job(jobQuery().activityId("Task_HelloBewerten")));

        assertThat(pi).hasPassed("Event_EndHello");
        assertThat(pi).isEnded();

    }

    @Test
    public void testWennAuftragInEdmVerteiltWird() {

        autoMock(PROCESS_RESOURCE);
        ProcessInstance pi = runtimeService().startProcessInstanceByKey(HELLO_PROCESS_PROCESS_KEY);

        assertThat(pi).isActive();
        assertThat(pi).isWaitingAt("StartEvent_hello");
        execute(job());

        assertThat(pi).isWaitingAt("Task_sayHello");
        getJavaDelegateMock("sayHalloTask").onExecutionSetVariable("warteAufSubProcess", Boolean.TRUE);
        execute(job());

        assertThat(pi).hasPassed("Event_SubProzessStarten");

        assertThat(pi).isWaitingAt("Event_SubBearbeitungStarten");
        execute(job(jobQuery().activityId("Event_SubBearbeitungStarten")));

        assertThat(pi).isWaitingAt("Activity_RueckmeldungEmpfangen");
        execute(job());

        assertThat(pi).isWaitingAt("Activity_RueckmeldungEmpfangen");
        execute(job());

        assertThat(pi).isWaitingFor(MESSAGE_ASYC_ANTWORT_EMPFANGEN);
        runtimeService().createMessageCorrelation(MESSAGE_ASYC_ANTWORT_EMPFANGEN) //
                .processInstanceId(pi.getRootProcessInstanceId()).correlate();
        assertThat(pi).hasPassed("Activity_RueckmeldungEmpfangen");

        // BPMN Error werfen
        assertThat(pi).isWaitingAt("Activity_AsyncAntwortBewerten");
        getJavaDelegateMock("asyncAntowrtBewertenTask").onExecutionThrowBpmnError( //
                new BpmnError(ERROR_SUB_PROCESS));
        execute(job(jobQuery().activityId("Activity_AsyncAntwortBewerten")));
        // End BPMN Error werfen

        assertThat(pi).hasPassed("Activity_AsyncAntwortBewerten", "Event_BewertungError");

        assertThat(pi).isWaitingAt("ErrorStartEvent_AuftragArchivieren");
        execute(job());

        assertThat(pi).isWaitingAt("CallActivity_EdmProcessStarten");
        execute(job());
//        execute(job(jobQuery().activityId("CallActivity_EdmProcessStarten")));

        // Aufruf Subprozess EDM
        ProcessInstance piEdm = calledProcessInstance(EDM_PROCESS_PROCESS_KEY);
        assertThat(piEdm).isActive();

        assertThat(piEdm).isWaitingAt("StartEvent_1");
        execute(job());

        assertThat(piEdm).isEnded();

        // Zurück im Hauptprozess
        assertThat(pi).hasPassed("Event_EdmEnd");
        assertThat(pi).isEnded();

    }
}