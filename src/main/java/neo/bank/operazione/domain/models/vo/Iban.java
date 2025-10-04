package neo.bank.operazione.domain.models.vo;

import neo.bank.operazione.domain.exceptions.ValidazioneException;
import neo.bank.operazione.domain.models.enums.CodiceErrore;

public record Iban(String codice) {
    public Iban {
        if (codice == null || codice.isBlank()) {
            throw new ValidazioneException(
                Iban.class.getSimpleName(),
                CodiceErrore.IBAN_NON_PUO_ESSERE_NULL.getCodice()
            );
        }
    }
}
