package de.hello.first.v1.firstBpm.process.delegate;

import de.hello.first.backend.v1.rest.MitarbeiterAendernService;
import de.hello.first.v1.mitarbeiteraendern.model.Funktion;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import de.hello.first.v1.mitarbeiteraendern.model.Mitarbeiter;
import de.hello.first.v1.mitarbeiteraendern.model.Benutzer;

@Service
public class SayHalloTask implements JavaDelegate {
    MitarbeiterAendernService service;

    public SayHalloTask(MitarbeiterAendernService service) {
        this.service = service;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Mitarbeiter mitarbeiter = new Mitarbeiter().funktoin(Funktion.NEU) //
                .id(11L) //
        .person(new Benutzer().nachname("Bauer").vorname("Max"));

        Boolean res = service.aendereMitarbeiter(mitarbeiter);
    }
}
