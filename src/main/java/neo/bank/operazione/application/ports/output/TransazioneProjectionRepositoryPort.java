package neo.bank.operazione.application.ports.output;

import java.util.List;

import neo.bank.operazione.application.ports.output.dto.TransazioneView;
import neo.bank.operazione.domain.models.aggregates.Operazione;
import neo.bank.operazione.domain.models.enums.TipologiaFlusso;
import neo.bank.operazione.domain.models.vo.DataCreazione;
import neo.bank.operazione.domain.models.vo.Iban;
import neo.bank.operazione.domain.models.vo.IdTransazione;

public interface TransazioneProjectionRepositoryPort {
    
    public void salva(Operazione operazione);
    public TransazioneView findById(IdTransazione idTransazione);
    List<TransazioneView> findBy(Iban iban, DataCreazione dataCreazioneInf, DataCreazione dataCreazioneSup, TipologiaFlusso tipologiaFlusso);
    public double calcolaTotaleBonificiUscita(Iban iban, DataCreazione dataInf, DataCreazione dataSup);
}
