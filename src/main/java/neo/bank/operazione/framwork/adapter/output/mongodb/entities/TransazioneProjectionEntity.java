package neo.bank.operazione.framwork.adapter.output.mongodb.entities;

import java.time.LocalDateTime;

import org.bson.codecs.pojo.annotations.BsonId;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.bank.operazione.domain.models.enums.TipologiaFlusso;

@MongoEntity(collection="transazioni-projection")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class TransazioneProjectionEntity extends PanacheMongoEntityBase {

    @BsonId
    private String idTransazione;
    private String idOperazione;
    private double importo;
    private String iban;
    private LocalDateTime dataCreazione;
    private String causale;
    private TipologiaFlusso tipologiaFlusso;

    public TransazioneProjectionEntity(String idTransazione, String idOperazione, double importo, String iban, LocalDateTime dataCreazione,
            String causale, TipologiaFlusso tipologiaFlusso) {
        this.idTransazione = idTransazione;
        this.idOperazione = idOperazione;
        this.importo = importo;
        this.iban = iban;
        this.dataCreazione = dataCreazione;
        this.causale = causale;
        this.tipologiaFlusso = tipologiaFlusso;
    }


    
}