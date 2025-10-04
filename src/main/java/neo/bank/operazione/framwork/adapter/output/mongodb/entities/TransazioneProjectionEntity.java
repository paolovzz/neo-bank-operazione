package neo.bank.operazione.framwork.adapter.output.mongodb.entities;

import java.time.LocalDateTime;

import org.bson.codecs.pojo.annotations.BsonId;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MongoEntity(collection="iban-projection")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class TransazioneProjectionEntity extends PanacheMongoEntityBase {

    @BsonId
    private String idTransazione;
    private double importo;
    private String iban;
    private LocalDateTime dataCreazione;
    private String causale;
    public TransazioneProjectionEntity(String idTransazione, double importo, String iban, LocalDateTime dataCreazione,
            String causale) {
        this.idTransazione = idTransazione;
        this.importo = importo;
        this.iban = iban;
        this.dataCreazione = dataCreazione;
        this.causale = causale;
    }


    
}