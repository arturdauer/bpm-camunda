package de.hello.first.v1.firstBpm.process.delegate;

import de.hello.first.backend.v1.rest.MitarbeiterAendernService;
import de.hello.first.backend.v1.rest.VsnrAendernService;
import de.hello.first.v1.mitarbeiteraendern.model.Funktion;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import de.hello.first.v1.mitarbeiteraendern.model.Mitarbeiter;
import de.hello.first.v1.mitarbeiteraendern.model.Benutzer;

@Service
public class SayHalloTask implements JavaDelegate {
    MitarbeiterAendernService service;
    VsnrAendernService vsnrAendernService;

    public SayHalloTask(MitarbeiterAendernService service, VsnrAendernService vsnrAendernService) {
        this.service = service;
        this.vsnrAendernService = vsnrAendernService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Mitarbeiter mitarbeiter = new Mitarbeiter().funktoin(Funktion.NEU) //
                .id(11L) //
        .person(new Benutzer().nachname("Bauer").vorname("Max"));

        Boolean res = service.aendereMitarbeiter(mitarbeiter);

        execution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("Message_SubBearbeitungStarten")
                .processInstanceId(execution.getProcessInstanceId())
                .correlate();

        String result = vsnrAendernService.aendereVsnr(mitarbeiter);
    }
}
