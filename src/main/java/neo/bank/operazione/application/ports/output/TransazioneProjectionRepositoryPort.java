package neo.bank.operazione.application.ports.output;


import neo.bank.operazione.application.ports.output.dto.TransazioneView;
import neo.bank.operazione.domain.models.aggregates.Operazione;
import neo.bank.operazione.domain.models.enums.TipologiaFlusso;
import neo.bank.operazione.domain.models.vo.DataCreazione;
import neo.bank.operazione.domain.models.vo.Iban;
import neo.bank.operazione.domain.models.vo.IdTransazione;
import neo.bank.operazione.domain.models.vo.RispostaPaginata;

public interface TransazioneProjectionRepositoryPort {
    
    public void salva(Operazione operazione);
    public TransazioneView findById(IdTransazione idTransazione);
    public double calcolaTotaleBonificiUscita(Iban iban, DataCreazione dataInf, DataCreazione dataSup);
    public RispostaPaginata<TransazioneView> recuperaTransazioni(Iban iban, DataCreazione dataInf, DataCreazione dataSup, Double importoMin, Double importoMax, TipologiaFlusso tipologiaFlusso, int numeroPagina, int dimensionePagina);
}
