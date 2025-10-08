package neo.bank.operazione.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import neo.bank.operazione.application.ports.input.commands.AggiornaProiezioneCmd;
import neo.bank.operazione.application.ports.input.commands.RecuperaDettaglioTransazioneCmd;
import neo.bank.operazione.application.ports.input.commands.RecuperaTotaleBonificiUscitaCmd;
import neo.bank.operazione.application.ports.output.OperazioneOutputPort;
import neo.bank.operazione.application.ports.output.TransazioneProjectionRepositoryPort;
import neo.bank.operazione.application.ports.output.dto.TransazioneView;
import neo.bank.operazione.domain.models.aggregates.Operazione;

@ApplicationScoped
@Slf4j
public class TransazioneProjUseCase {
    
    @Inject
    private TransazioneProjectionRepositoryPort proiezioneRepo;

    @Inject
    private OperazioneOutputPort opOutputPort;
   
    public void aggiornaProiezione(AggiornaProiezioneCmd cmd) {
        log.info("Comando [aggiornaProiezione] in esecuzione...");
        Operazione operazione = opOutputPort.recuperaDaId(cmd.getIdOperazione());
        proiezioneRepo.salva(operazione);
        log.info("Comando [aggiornaProiezione] terminato...");
    }

    public TransazioneView recuperaTransazioneDaId(RecuperaDettaglioTransazioneCmd cmd) {
        log.info("Comando [recuperaTransazioneDaId] in esecuzione...");
        TransazioneView transazioneView = proiezioneRepo.findById(cmd.getIdTransazione());
        log.info("Comando [recuperaTransazioneDaId] terminato...");
        return transazioneView;
    }

    public double recuperaTotaleGiornarlieroBonificiUscita( RecuperaTotaleBonificiUscitaCmd cmd) {
        
        log.info("Comando [recuperaTotaleGiornarlieroBonificiUscita] in esecuzione...");
        double totale = proiezioneRepo.calcolaTotaleBonificiUscita(cmd.getIban(), cmd.getDataInf(), cmd.getDataSup());
        log.info("Comando [recuperaTotaleGiornarlieroBonificiUscita] terminato...");
        return totale;
    }

}