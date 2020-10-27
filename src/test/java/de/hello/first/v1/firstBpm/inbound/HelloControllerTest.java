package de.hello.first.v1.firstBpm.inbound;

import de.hello.first.v1.firstBpm.process.ProzessStarter;
import de.hello.first.v1.mitarbeiteraendern.model.Funktion;
import org.camunda.bpm.extension.mockito.process.ProcessInstanceFake;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static de.hello.FileContentUtil.getContentAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {HelloController.class, HelloControllerTest.NoCamundaApplication.class},
//excludeAutoConfiguration = {CamundaConfiguration.class}
        properties = {"camunda.bpm.enabled=false"})
public class HelloControllerTest {

    @SpringBootApplication
    public static class NoCamundaApplication {
    }

    @MockBean
    ProzessStarter prozessStarter;

    @Autowired
    private MockMvc mockMvc;

    private static final String ENDPOINT = "/hallo";

    @Test
    public void setProzessStarter_shouldReturnOK() throws Exception {
        String auftrag = getContentAsString("helloAuftrag/anafrage_ok.json");
        ArgumentCaptor<de.hello.first.v1.mitarbeiteraendern.model.Mitarbeiter> mitarbeiterCaptor = //
                ArgumentCaptor.forClass(de.hello.first.v1.mitarbeiteraendern.model.Mitarbeiter.class);

        given(prozessStarter.starteProzessHallo(any(), anyString())) //
                .willReturn(ProcessInstanceFake.builder().id("id").businessKey("123").build());

        //when
        mockMvc.perform(post(ENDPOINT).content(auftrag).contentType(MediaType.APPLICATION_JSON) //
                .header("x-correlation-id", "123")) //
                .andExpect(status().isOk());

        // then
        verify(prozessStarter).starteProzessHallo(mitarbeiterCaptor.capture(), anyString());
        assertThat(mitarbeiterCaptor.getValue()).isNotNull();
        assertThat(mitarbeiterCaptor.getValue().getFunktoin()).isEqualTo(Funktion.NEU);

    }
}