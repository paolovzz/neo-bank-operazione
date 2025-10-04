package neo.bank.operazione.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import neo.bank.operazione.application.ports.input.commands.AvviaOperazioneCmd;
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
    private OperazioneOutputPort ccOutputPort;

    public void avviaOperazione(AvviaOperazioneCmd cmd) {
        log.info("Comando [avviaOperazione] in esecuzione...");
        Operazione cc = Operazione.avvia(generatoreIdOperazioneService, generatoreIdTransazioneService, cmd.getIbanMittente(), cmd.getIbanDestinatario(), cmd.getCausale(), cmd.getImporto());
        ccOutputPort.salva(cc);
        log.info("Comando [avviaOperazione] terminato...");
    }


}