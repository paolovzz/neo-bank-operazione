package neo.bank.operazione.domain.models.vo;

import neo.bank.operazione.domain.exceptions.ValidazioneException;
import neo.bank.operazione.domain.models.enums.CodiceErrore;

public record IdTransazione(String id) {
    public IdTransazione {
        if (id == null || id.isBlank()) {
            throw new ValidazioneException(
                    IdTransazione.class.getSimpleName(),
                    CodiceErrore.ID_NON_PUO_ESSERE_NULL.getCodice());
        }
    }
}
