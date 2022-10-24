package ro.uti.ran.core.ws.internal.notificari;

/**
 * Created by Sache on 12/16/2015.
 */
public interface NotificariSistemService {

    void notificareRapoarteCentralizatoare(Integer sirutaUAT) throws Exception;

    void notificareCereriCompletareDate(Integer sirutaUAT, String identificatorGospodarie, String details) throws Exception;

    void notificareCereriCorectieDate(Integer sirutaUAT, String identificatorGospodarie, String details) throws Exception;

    void notificareRaportAdHoc(String username, String denumireRaport, String continutHtml, byte[] atasament) throws Exception;

    void notificareRaportAdHoc(String username, String denumireRaport, String continutHtml) throws Exception;
}
