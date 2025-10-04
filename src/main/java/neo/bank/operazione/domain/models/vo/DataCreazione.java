package neo.bank.operazione.domain.models.vo;

import java.time.LocalDateTime;

import neo.bank.operazione.domain.exceptions.ValidazioneException;
import neo.bank.operazione.domain.models.enums.CodiceErrore;

public record DataCreazione(LocalDateTime dataOra){

    public DataCreazione(LocalDateTime dataOra) {
        if (dataOra == null) {
            throw new ValidazioneException(DataCreazione.class.getSimpleName(), CodiceErrore.DATA_CREAZIONE_NON_PUO_ESSERE_NULL.getCodice());
        }
        this.dataOra = dataOra;
    }
}
