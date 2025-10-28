package neo.bank.operazione.framwork.adapter.output.mongodb.impl;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
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
import neo.bank.operazione.domain.models.vo.RispostaPaginata;
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
        Transazione transazioneACC = operazione.getTransazioneIn();
        Transazione transazioneDEB = operazione.getTransazioneOut();
        
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

   @Override
    public RispostaPaginata<TransazioneView> recuperaTransazioni(
            Iban iban, DataCreazione dataInf, DataCreazione dataSup, Double importoMin, Double importoMax, TipologiaFlusso tipologiaFlusso, int numeroPagina, int dimensionePagina) {

        List<Bson> filtri = new ArrayList<>();
        filtri.add(eq("iban", iban.codice()));

        if (dataInf != null) filtri.add(gte("dataCreazione", dataInf.dataOra()));
        if (dataSup != null) filtri.add(lte("dataCreazione", dataSup.dataOra()));
        if (importoMin != null) filtri.add(gte("importo", importoMin));
        if (importoMax != null) filtri.add(lte("importo", importoMax));
        if (tipologiaFlusso != null) filtri.add(eq("tipologiaFlusso", tipologiaFlusso.name()));

        Bson filtroFinale = and(filtri);
        
        long totRisultati = mongoCollection().countDocuments(filtroFinale);
        List<TransazioneProjectionEntity> transazioni = mongoCollection()
            .find(filtroFinale)
            .sort(Sorts.descending("dataCreazione"))
            .skip(numeroPagina * dimensionePagina)
            .limit(dimensionePagina)
            .into(new ArrayList<>());
        
        return new RispostaPaginata<>(transazioni.stream()
            .map(e -> new TransazioneView(
                e.getIdTransazione(),
                e.getIdOperazione(),
                e.getImporto(),
                e.getIban(),
                e.getDataCreazione(),
                e.getCausale(),
                e.getTipologiaFlusso()))
            .toList(), numeroPagina, dimensionePagina, totRisultati);


        // return transazioni.stream()
        //     .map(e -> new TransazioneView(
        //         e.getIdTransazione(),
        //         e.getIdOperazione(),
        //         e.getImporto(),
        //         e.getIban(),
        //         e.getDataCreazione(),
        //         e.getCausale(),
        //         e.getTipologiaFlusso()))
        //     .toList();
    }

}
