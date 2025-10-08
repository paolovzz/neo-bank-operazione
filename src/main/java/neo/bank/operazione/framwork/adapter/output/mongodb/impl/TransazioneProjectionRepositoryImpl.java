package neo.bank.operazione.framwork.adapter.output.mongodb.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import neo.bank.operazione.application.exceptions.TransazioneNonTrovataException;
import neo.bank.operazione.application.ports.output.TransazioneProjectionRepositoryPort;
import neo.bank.operazione.application.ports.output.dto.TransazioneView;
import neo.bank.operazione.domain.models.aggregates.Operazione;
import neo.bank.operazione.domain.models.entities.Transazione;
import neo.bank.operazione.domain.models.enums.TipologiaFlusso;
import neo.bank.operazione.domain.models.vo.DataCreazione;
import neo.bank.operazione.domain.models.vo.Iban;
import neo.bank.operazione.domain.models.vo.IdTransazione;
import neo.bank.operazione.framwork.adapter.output.mongodb.entities.TransazioneProjectionEntity;

@ApplicationScoped
@Slf4j
public class TransazioneProjectionRepositoryImpl implements PanacheMongoRepositoryBase<TransazioneProjectionEntity, String>, TransazioneProjectionRepositoryPort {
    
    @Override
    public void salva(Operazione operazione) {
        log.info("Registro la transazione creata nella projection");

        String idOperazione = operazione.getIdOperazione().id();
        double importo = operazione.getImporto();
        String causale = operazione.getCausale();
        LocalDateTime dataCreazione = operazione.getDataCreazione().dataOra();
        Transazione transazioneACC = Arrays.stream(operazione.getTransazioni()).filter( e -> e.getTipologiaFlusso().equals(TipologiaFlusso.ACCREDITO)).findFirst().get();
        Transazione transazioneDEB = Arrays.stream(operazione.getTransazioni()).filter( e -> e.getTipologiaFlusso().equals(TipologiaFlusso.ADDEBITO)).findFirst().get();
        TransazioneProjectionEntity accredito = new TransazioneProjectionEntity(transazioneACC.getIdTransazione().id(), idOperazione, importo, transazioneACC.getIban().codice(), dataCreazione, causale, transazioneACC.getTipologiaFlusso());
        TransazioneProjectionEntity addebito = new TransazioneProjectionEntity(transazioneDEB.getIdTransazione().id(), idOperazione, importo, transazioneDEB.getIban().codice(), dataCreazione, causale, transazioneDEB.getTipologiaFlusso());
        persist(accredito, addebito);
    }

    @Override
    public TransazioneView  findById(IdTransazione idTransazione) {

        TransazioneProjectionEntity entity = findById(idTransazione.id());
        if(entity == null)
            throw new TransazioneNonTrovataException(idTransazione.id());
        else
            return new TransazioneView(
                entity.getIdTransazione(), 
                entity.getIdOperazione(), 
                entity.getImporto(),
                entity.getIban(), 
                entity.getDataCreazione(), 
                entity.getCausale(), 
                entity.getTipologiaFlusso());

    }
    

    public List<TransazioneView> findBy(Iban iban, DataCreazione dataCreazioneInf, DataCreazione dataCreazioneSup, TipologiaFlusso tipologiaFlusso) {
        
        return find("iban = ?1 and dataCreazione >= ?2 and dataCreazione <= ?3 and tipologiaFlusso = ?4",
                iban.codice(),
                dataCreazioneInf.dataOra(),
                dataCreazioneSup.dataOra(),
                tipologiaFlusso
        ).list().stream().map(entity ->  new TransazioneView(
                entity.getIdTransazione(), 
                entity.getIdOperazione(), 
                entity.getImporto(),
                entity.getIban(), 
                entity.getDataCreazione(), 
                entity.getCausale(), 
                entity.getTipologiaFlusso())).toList();
    }

    @Override
    public double calcolaTotaleBonificiUscita(Iban iban, DataCreazione dataInf, DataCreazione dataSup) {

        List<Bson> pipeline = List.of(
            Aggregates.match(Filters.and(
                Filters.eq("iban", iban.codice()),
                Filters.eq("tipologiaFlusso", TipologiaFlusso.ADDEBITO), // tipologiaFlusso come String
                Filters.gte("dataCreazione", dataInf.dataOra()),
                Filters.lte("dataCreazione", dataSup.dataOra())
            )),
            Aggregates.group(null, Accumulators.sum("totale", "$importo"))
        );

        Document result = getCollection().aggregate(pipeline, Document.class).first();

        if (result != null && result.containsKey("totale")) {
            Object totale = result.get("totale");
            if (totale instanceof Number) {
                return ((Number) totale).doubleValue();  // ✅ conversione sicura
            }
        }
        return 0.0;
    }


    private MongoCollection<Document> getCollection() {
        return mongoDatabase()
            .getCollection("transazioni-projection");
    }
}
