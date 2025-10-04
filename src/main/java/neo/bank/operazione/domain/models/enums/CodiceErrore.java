package neo.bank.operazione.domain.models.enums;

public enum CodiceErrore {
    

    ID_NON_PUO_ESSERE_NULL("ID_NON_PUO_ESSERE_NULL"),
    CODICE_CLIENTE_NON_VASERE_NULL("CODICE_CAB_NON_PUO_ESSERE_NULL"),
    DATA_CREAZIONE_NON_PUO_ESSERE_NULL("DATA_CREAZIONE_NON_PUO_ESSERE_NULL"),
    IBAN_NON_PUO_ESSERE_NULL("IBAN_NON_PUO_ESSERE_NULL");

    private String codice;

    private CodiceErrore(String codice) {
        this.codice = codice;
    }

    public String getCodice() {
        return codice;
    }
    
    
}
