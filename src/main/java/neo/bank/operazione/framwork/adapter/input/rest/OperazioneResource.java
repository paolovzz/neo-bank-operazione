package neo.bank.operazione.framwork.adapter.input.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import neo.bank.operazione.application.OperazioneUseCase;
import neo.bank.operazione.application.TransazioneProjUseCase;
import neo.bank.operazione.application.ports.input.commands.RecuperaDettaglioOperazioneCmd;
import neo.bank.operazione.domain.models.aggregates.Operazione;
import neo.bank.operazione.domain.models.entities.Transazione;
import neo.bank.operazione.domain.models.vo.IdOperazione;
import neo.bank.operazione.framwork.adapter.input.rest.response.DettaglioOperazioneResponse;
import neo.bank.operazione.framwork.adapter.input.rest.response.TransazioneInfoResponse;

@Path("/operazioni")
@ApplicationScoped
public class OperazioneResource {
    
    @Inject
    private TransazioneProjUseCase app;

    @Inject
    private OperazioneUseCase operazioneUseCase;


    @Path("/{id}")
    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response recuperaOperazioneaId(@PathParam(value = "id") String idOperazione) {

        Operazione operazione = operazioneUseCase.recuperaDaId(new RecuperaDettaglioOperazioneCmd(new IdOperazione(idOperazione)));
        
        Transazione trAcc = operazione.getTransazioneIn();
        TransazioneInfoResponse transazioneInfoAcc = new TransazioneInfoResponse(trAcc.getIdTransazione().id(), operazione.getIdOperazione().id(), operazione.getImporto(), trAcc.getIban().codice(), operazione.getDataCreazione().dataOra(), operazione.getCausale(), trAcc.getTipologiaFlusso());
        Transazione trAdd= operazione.getTransazioneOut();
        TransazioneInfoResponse transazioneInfoAddebito = new TransazioneInfoResponse(trAdd.getIdTransazione().id(), operazione.getIdOperazione().id(), operazione.getImporto(), trAdd.getIban().codice(), operazione.getDataCreazione().dataOra(), operazione.getCausale(), trAdd.getTipologiaFlusso());
        DettaglioOperazioneResponse res = new DettaglioOperazioneResponse(transazioneInfoAcc, transazioneInfoAddebito);
        
        return Response.ok(res).build();
    }
}
