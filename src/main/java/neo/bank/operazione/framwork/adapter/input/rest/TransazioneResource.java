package neo.bank.operazione.framwork.adapter.input.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import neo.bank.operazione.application.TransazioneProjUseCase;
import neo.bank.operazione.application.ports.input.commands.RecuperaDettaglioTransazioneCmd;
import neo.bank.operazione.application.ports.input.commands.RecuperaTotaleBonificiUscitaCmd;
import neo.bank.operazione.application.ports.input.commands.RecuperaTransazioniCmd;
import neo.bank.operazione.application.ports.output.dto.TransazioneView;
import neo.bank.operazione.domain.models.enums.TipologiaFlusso;
import neo.bank.operazione.domain.models.vo.DataCreazione;
import neo.bank.operazione.domain.models.vo.Iban;
import neo.bank.operazione.domain.models.vo.IdTransazione;
import neo.bank.operazione.domain.models.vo.RispostaPaginata;
import neo.bank.operazione.framwork.adapter.input.rest.response.SommaBonificiUscitaInfoResponse;
import neo.bank.operazione.framwork.adapter.input.rest.response.TransazioneInfoResponse;

@Path("/transazioni")
@ApplicationScoped
public class TransazioneResource {
    
    @Inject
    private TransazioneProjUseCase app;


    @Path("/{id}")
    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response recuperaTransazioneDaId(@PathParam(value = "id") String idTransazione) {

        TransazioneView transazioneView = app.recuperaTransazioneDaId(new RecuperaDettaglioTransazioneCmd(new IdTransazione(idTransazione)));
        TransazioneInfoResponse bodyResponse = new TransazioneInfoResponse(transazioneView);
        return Response.ok(bodyResponse).build();
    }

    @Path("/iban/{iban}")
    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response recuperaTransazioni(
        @PathParam(value = "iban") String iban, 
        @QueryParam(value = "dataCreazioneMin" ) LocalDate dataCreazioneMin,
        @QueryParam(value = "dataCreazioneMax" ) LocalDate dataCreazioneMax,
        @QueryParam(value = "tipologiaFlusso" ) TipologiaFlusso tipologiaFlusso,
        @QueryParam(value = "importoMin" ) Double importoMin,
        @QueryParam(value = "importoMax" ) Double importoMax,
        @QueryParam(value = "numeroPagina") @DefaultValue("0") Integer numeroPagina,
        @QueryParam(value = "dimensionePagina") @DefaultValue("10") Integer dimensionePagina
    ) {
        RispostaPaginata<TransazioneView> risultato = app.recuperaTransazioni(new RecuperaTransazioniCmd(new Iban(iban), dataCreazioneMin != null ? new DataCreazione(dataCreazioneMin.atStartOfDay()) : null, dataCreazioneMax != null ? new DataCreazione(dataCreazioneMax.atTime(LocalTime.MAX)): null, importoMin, importoMax, tipologiaFlusso, numeroPagina, dimensionePagina));
        return Response.ok(risultato).build();
    }
    @Path("/{iban}/totale/bonifici/uscita")
    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response recuperaTotaleBonificiUscita(@PathParam(value = "iban") String iban) {

        LocalDate now = LocalDate.now();
        LocalDateTime inizioGiorno = now.atStartOfDay();
        LocalDateTime fineGiorno = now.atTime(LocalTime.MAX);
        double totaleGiornaliero = app.recuperaTotaleGiornarlieroBonificiUscita(new RecuperaTotaleBonificiUscitaCmd(new Iban(iban), new DataCreazione(inizioGiorno), new DataCreazione(fineGiorno)));
        
        YearMonth meseCorrente = YearMonth.now();
        LocalDate primoGiorno = meseCorrente.atDay(1);
        LocalDate ultimoGiorno = meseCorrente.atEndOfMonth();
        LocalDateTime inizioMese = primoGiorno.atStartOfDay();
        LocalDateTime fineMese = ultimoGiorno.atTime(LocalTime.MAX);
        
        double totaleMensile = app.recuperaTotaleGiornarlieroBonificiUscita(new RecuperaTotaleBonificiUscitaCmd(new Iban(iban), new DataCreazione(inizioMese), new DataCreazione(fineMese)));
        
        SommaBonificiUscitaInfoResponse bodyResponse = new SommaBonificiUscitaInfoResponse(totaleGiornaliero, totaleMensile);
        return Response.ok(bodyResponse).build();
    }
}
