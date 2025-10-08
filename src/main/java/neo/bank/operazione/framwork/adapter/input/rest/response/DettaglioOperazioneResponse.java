package neo.bank.operazione.framwork.adapter.input.rest.response;

import lombok.Value;

@Value
public class DettaglioOperazioneResponse {
   
    private TransazioneInfoResponse transazioneAccredito;
    private TransazioneInfoResponse transazioneAddebito;

}
