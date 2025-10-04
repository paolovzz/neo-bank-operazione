package neo.bank.operazione.domain.services;

import neo.bank.operazione.domain.models.vo.IdTransazione;

public interface GeneratoreIdTransazioneService {

    public IdTransazione genera();
}