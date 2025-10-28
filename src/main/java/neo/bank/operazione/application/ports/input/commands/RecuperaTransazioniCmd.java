package neo.bank.operazione.application.ports.input.commands;

import lombok.Value;
import neo.bank.operazione.domain.models.enums.TipologiaFlusso;
import neo.bank.operazione.domain.models.vo.DataCreazione;
import neo.bank.operazione.domain.models.vo.Iban;

@Value
public class RecuperaTransazioniCmd {
    
    private Iban iban;
    private DataCreazione dataInf;
    private DataCreazione dataSup;
    private Double importoMin;
    private Double importoMax;
    private TipologiaFlusso tipologiaFlusso;
    private int numeroPagina;
    private int dimensionePagina;
}
