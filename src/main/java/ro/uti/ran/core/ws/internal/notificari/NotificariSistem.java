package ro.uti.ran.core.ws.internal.notificari;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;

/**
 * Created by Anastasia cea micuta on 11/22/2015.
 */
@WebService
public interface NotificariSistem {
    @WebMethod
    void notificareRapoarteCentralizatoare(
            @WebParam(name = "sirutaUAT") Integer sirutaUAT
    )throws RanException, RanRuntimeException;

    @WebMethod
    void notificareCereriCompletareDate(
            @WebParam(name = "sirutaUAT") Integer sirutaUAT,
            @WebParam(name = "identificatorGospodarie") String identificatorGospodarie,
            @WebParam(name = "details") String details
    )throws RanException, RanRuntimeException;

    @WebMethod
    void notificareCereriCorectieDate(
            @WebParam(name = "sirutaUAT") Integer sirutaUAT,
            @WebParam(name = "identificatorGospodarie") String identificatorGospodarie,
            @WebParam(name = "details") String details
    )throws RanException, RanRuntimeException;

    /**
     * Valoarea de business a notificarii este urmatoarea: un utilizator al RAN care solicita generarea unui raport
     * (va avea o interfata specifica pentru a face aceasta solicitare) va trebui sa primeasca pe email raportul solicitat.
     *
     * @param username       numele utilizatorului caruia trebuie sa-i trimitem email-ul; Prin nume utilizator
     *                       trebuie vazut orice utilizator al sistemului RAN (nu doar gospodari,
     *                       pot fi utilizatori din primarii, utilizatori din alte institutii care ajung la aceasta pagina de portal).
     * @param subiect     sau putem sa-l redenumim in ‘subiect’ -  este subiectul mailului
     * @param continutHtml   fisier html in body-ul email-ului
     * @param atasament      fisier jpg in atasamentele email-ului
     */
    @WebMethod
    void notificareRaportAdHoc (
            @WebParam(name = "username") String username,
            @WebParam(name = "denumireRaport") String subiect,
            @WebParam(name = "continutHtml") String continutHtml,
            @WebParam(name = "atasament") byte[] atasament
    ) throws RanException, RanRuntimeException;



    /**
     * Valoarea de business a notificarii este urmatoarea: un utilizator al RAN care solicita generarea unui raport
     * (va avea o interfata specifica pentru a face aceasta solicitare) va trebui sa primeasca pe email raportul solicitat.
     *
     * @param username       numele utilizatorului caruia trebuie sa-i trimitem email-ul; Prin nume utilizator
     *                       trebuie vazut orice utilizator al sistemului RAN (nu doar gospodari,
     *                       pot fi utilizatori din primarii, utilizatori din alte institutii care ajung la aceasta pagina de portal).
     * @param subiect     sau putem sa-l redenumim in ‘subiect’ -  este subiectul mailului
     * @param continutHtml   fisier html in body-ul email-ului
     */
//    @WebMethod
//    @Oneway
//    void notificareSimplaRaportAdHoc(
//            @WebParam(name = "username") String username,
//            @WebParam(name = "denumireRaport") String subiect,
//            @WebParam(name = "continutHtml") String continutHtml
//
//    );
}
