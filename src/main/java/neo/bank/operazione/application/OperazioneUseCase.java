package neo.bank.operazione.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import neo.bank.operazione.application.ports.input.commands.AnnullaOperazioneCmd;
import neo.bank.operazione.application.ports.input.commands.AvviaOperazioneCmd;
import neo.bank.operazione.application.ports.input.commands.ConcludiOperazioneCmd;
import neo.bank.operazione.application.ports.input.commands.RecuperaDettaglioOperazioneCmd;
import neo.bank.operazione.application.ports.output.OperazioneOutputPort;
import neo.bank.operazione.domain.models.aggregates.Operazione;
import neo.bank.operazione.domain.services.GeneratoreIdOperazioneService;
import neo.bank.operazione.domain.services.GeneratoreIdTransazioneService;

@ApplicationScoped
@Slf4j
public class OperazioneUseCase {
    
    @Inject
    private GeneratoreIdOperazioneService generatoreIdOperazioneService;
    
    @Inject
    private GeneratoreIdTransazioneService generatoreIdTransazioneService;

    @Inject
    private OperazioneOutputPort opOutputPort;

    public void avviaOperazione(AvviaOperazioneCmd cmd) {
        log.info("Comando [avviaOperazione] in esecuzione...");
        Operazione cc = Operazione.avvia(generatoreIdOperazioneService, generatoreIdTransazioneService, cmd.getIbanMittente(), cmd.getIbanDestinatario(), cmd.getCausale(), cmd.getImporto());
        opOutputPort.salva(cc);
        log.info("Comando [avviaOperazione] terminato...");
    }

    public void concludiOperazione(ConcludiOperazioneCmd cmd) {
        log.info("Comando [concludiOperazione] in esecuzione...");
        Operazione operazione = opOutputPort.recuperaDaId(cmd.getIdOperazione());
        operazione.concludi();
        opOutputPort.salva(operazione);
        log.info("Comando [concludiOperazione] terminato...");
    }

    public void annullaOperazione(AnnullaOperazioneCmd cmd) {
        log.info("Comando [annullaOperazione] in esecuzione...");
        Operazione operazione = opOutputPort.recuperaDaId(cmd.getIdOperazione());
        operazione.annulla();
        opOutputPort.salva(operazione);
        log.info("Comando [annullaOperazione] terminato...");
    }

    public Operazione recuperaDaId(RecuperaDettaglioOperazioneCmd cmd) {
        log.info("Comando [recuperaDaId] in esecuzione...");
        Operazione operazione = opOutputPort.recuperaDaId(cmd.getIdOperazione());
        log.info("Comando [recuperaDaId] terminato...");
        return operazione;
    }

}