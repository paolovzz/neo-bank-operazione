package neo.bank.operazione.domain.models.vo;

import java.util.List;

import lombok.Value;

@Value
public class RispostaPaginata<T> {
    
    private List<T> result;
    private int numeroPagina;
    private int dimensionePagina;
    private long totaleRisultati;
}
