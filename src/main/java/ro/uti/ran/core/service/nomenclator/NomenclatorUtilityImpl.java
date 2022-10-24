package ro.uti.ran.core.service.nomenclator;

import com.sun.java.util.jar.pack.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.audit.Audit;
import ro.uti.ran.core.audit.AuditOpType;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.HasId;
import ro.uti.ran.core.model.Versioned;
import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.model.portal.Judet;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.repository.criteria.AndRepositoryCriteria;
import ro.uti.ran.core.repository.portal.InstitutieRepository;
import ro.uti.ran.core.repository.portal.JudetRepository;
import ro.uti.ran.core.repository.portal.UatRepository;
import ro.uti.ran.core.repository.registru.NomenclatorRepository;
import ro.uti.ran.core.utils.GenericListResult;

import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static ro.uti.ran.core.audit.AuditOpType.*;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-02 15:25
 */
@Component
@Transactional("registruTransactionManager")
public class NomenclatorUtilityImpl implements NomenclatorService {

    @Autowired
    NomenclatorRepository nomenclatorRepository;

    @Autowired
    InstitutieRepository institutieRepository;

    @Autowired
    JudetRepository judetRepository;

    @Autowired
    UatRepository uatRepository;

    @Autowired
    private ExceptionUtil exceptionUtil;

    PagingInfo pagingInfoForOne = new PagingInfo();

    @Override
    public Nomenclator getNomenclator(NomenclatorSearchFilter searchFilter) {

        GenericListResult<Nomenclator> list = getListaNomenclator(searchFilter, pagingInfoForOne, null);

        if( list.getItems().size() == 0){
            return null;
        }

        if( list.getItems().size() > 1){
            throw new RuntimeException("Exista mai multe intrari de nomenclator pentru criteriile specificate, rafinati cautarea");
        }

        return list.getItems().get(0);
    }

    @Override
    public GenericListResult<Nomenclator> getListaNomenclator(NomenclatorSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo)  {
        if (searchFilter == null) {
            throw new IllegalArgumentException("NomenclatorSearchFilter nedefinit");
        }

        if (searchFilter.getType() == null) {
            throw new IllegalArgumentException("NomenclatorSearchFilter#type nedefinit, vezi enum TipNomenclator");
        }

        /*cautare accent-insesitiv in nom_uat*/
        if (searchFilter.getType() != null &&  searchFilter.getType().equals(TipNomenclator.NomUat)) {
            alterSessionNlsSortComp();
        }

        GenericListResult<Nomenclator> retValue= nomenclatorRepository.getList(
                searchFilter,
                pagingInfo,
                sortInfo
        );

        if (retValue.getItems().size()>0){
            if (retValue.getItems().get(0) instanceof Versioned){
                for(Nomenclator item : retValue.getItems()) {
                    try {
                        Field fieldLatest = item.getClass().getDeclaredField("isLatestVersion");
                        fieldLatest.setAccessible(true);
                        fieldLatest.set(item,checkIsLatest(searchFilter.getType().getClazz(), ((Versioned) item).getBaseId(), ((Versioned) item).getDataStop()));
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        }



        return retValue;


    }

    private boolean checkIsLatest(Class<? extends Nomenclator> clazz,Long baseId, Date dataStop) {

        return nomenclatorRepository.chekLatestVersion(clazz, baseId, dataStop);
    }

    @Audit(opType = SALVARE_INREGISTRARE_NOMENCLATOR)
    @Override
    public Nomenclator saveNomenclator(Nomenclator nomenclator) throws RanBusinessException, RanException, RanRuntimeException {
        if (nomenclator == null) {
            throw exceptionUtil.buildException(new RanRuntimeException("Instanta Nomenclator nedefinita"));

        }

        //
        // TODO: Validarile de bussines intrare nomenclator
        //





        //
        // Logica la adaugare nomenclator
        //
        if( nomenclator.getId() == null || nomenclator.getId() <= 0){

            nomenclator.setId(null);
            //Se veririca corectitudinea
            //
            String isOk = nomenclatorRepository.checkInsert(nomenclator.getClass(),nomenclator);
            if (!isOk.equals("Ok")){
                throw exceptionUtil.buildException(new RanRuntimeException(isOk));
            }
            // Nomenclatoare versionate
            //
            if( nomenclator instanceof Versioned){


                //Verificare existenta

                //
                // data start este camp obligatoriu
                //
                Date dataStart = ((Versioned) nomenclator).getDataStart();
                if( dataStart == null){
                    throw exceptionUtil.buildException(new RanException(new NomenclatorException(NomenclatorExceptionCodes.NOMENCLATOR_WITHOUT_DATA_START)));
                }

                procesareReferinte(nomenclator);


                Long baseId=((Versioned) nomenclator).getBaseId();

                //NomenclatorInsertChecked temp = NomenclatorCheck.buildInsertInfo(nomenclator.getClass(),nomenclator);

                if(baseId != null){

                    Nomenclator nomenclatorLast = nomenclatorRepository.getLastVersion(nomenclator.getClass(),baseId);
                    if (!nomenclatorRepository.chekLatestVersion(nomenclator.getClass(),((Versioned) nomenclator).getBaseId(),((Versioned) nomenclator).getDataStart())){
                        throw exceptionUtil.buildException(new RanException(new NomenclatorException(NomenclatorExceptionCodes.NOMENCLATOR_INTERSECTION_VERSION)));
                    }
                    Nomenclator saved  = nomenclatorRepository.save(nomenclator);
                    return saved;
                }else {

                    //
                    // pentru shuntare not null
                    //
                    ((Versioned) nomenclator).setBaseId(0L);

                    // salvare
                    Nomenclator saved = nomenclatorRepository.save(nomenclator);

                    //
                    // La adaugare inregistrare noua base_id ia valoarea id
                    //
                    ((Versioned) saved).setBaseId(saved.getId());

                    //salvam inca o data pentur base_id
                    saved = nomenclatorRepository.save(saved);

                    return saved;

                }
            }else{

                //
                return nomenclatorRepository.save(nomenclator);
            }


        }else{

            //
            // Logica la modificare intrare nomenclator
            //

            Nomenclator existing = nomenclatorRepository.findOne(nomenclator.getClass(), nomenclator.getId());
            if( existing == null){
                throw exceptionUtil.buildException(new RanRuntimeException("Nu exista in nomenclator intrare de tip "+nomenclator.getClass()+" cu ID " + nomenclator.getId()));

            }


            //update values
            BeanUtils.copyProperties(nomenclator, existing, HasId.ID, Versioned.BASE_ID, Versioned.LAST_MODIFIED_DATE);


            procesareReferinte(existing);


            if( existing instanceof Versioned){

                //todo: versionare
                /**
                 Irinel:
                    eu vad asa
                     pentru fiecare adaugare trebuie completata data de incepere valabilitate
                     adica data_start
                     apoi la modificare se completeaza data inceperii valabilitatii pt noua valoare
                     si se face update pe data_sop vechea valoare si insert cu noua valoare

                     si la stergere:
                     de fapt dezactivare valoare
                     se completeaza doar data_stop pt val curenta

                 */

                return nomenclatorRepository.save(existing);

            }else{
                // doar un update al inregistrarii
                return nomenclatorRepository.save(existing);
            }
        }
    }

    @Override
    @Audit(opType = CREARE_VERSIUNE_NOMENCLATOR)
    public Nomenclator pregatireVersiuneIntrareNomenclator(TipNomenclator tipNomenclator, Long idIntrareNomenclator) throws RanBusinessException, RanException, RanRuntimeException {

        //todo: verificare existenta intrare nomenclator dupa id
        //todo: verificare intrare daca este versionabil
        //todo: verificare intrare daca indeplineste conditiile de versinoare (daca data stop este completata)
        //todo: pregatire versiune: data stop trece in data start, data stop devine null

        Nomenclator entry = nomenclatorRepository.findOne(tipNomenclator.getClazz(), idIntrareNomenclator, true);

        if( entry == null){
            throw exceptionUtil.buildException(new RanRuntimeException("Nu exista in nomenclator intrare de tip "+tipNomenclator.getClass()+" cu ID " + idIntrareNomenclator));
        }

        if(!(entry instanceof Versioned)) {
            throw exceptionUtil.buildException(new RanException(new NomenclatorException(NomenclatorExceptionCodes.NOMENCLATOR_NOT_VERSIONED)));

        }

        Versioned versioned = (Versioned) entry;

        if( versioned.getDataStop() == null){
            throw exceptionUtil.buildException(new RanException(new NomenclatorException(NomenclatorExceptionCodes.NOMENCLATOR_WITHOUT_DATA_STOP)));

        }
        if (!nomenclatorRepository.checkNewVersion(entry.getClass(),versioned.getBaseId(),versioned.getDataStop())){
            throw exceptionUtil.buildException(new RanException(new NomenclatorException(NomenclatorExceptionCodes.NOMENCLATOR_INTERSECTION_VERSION)));
        }
        Calendar plusData = Calendar.getInstance();
        plusData.setTime(((Versioned) entry).getDataStop());
        plusData.add(Calendar.DATE,1);
                ((Versioned) entry).setDataStart(plusData.getTime());
        ((Versioned) entry).setDataStop(null);

        return entry;
    }

    @Override
    @Audit(opType =SALVARE_VERSIUNE_NOMENCLATOR)
            public Nomenclator salveazaVersiuneIntrareNomenclator(Nomenclator intrareNomenclator) throws RanBusinessException, RanException, RanRuntimeException {

        //todo: identificare intrare noua de versionat dupa id intrareNomenclator
        //todo: creare instanta noua de acelasi tip
        //todo: copiere proprietati din parametru intrareNomenclator in instanta noua, exclusiv ID, BASE_ID
        //todo: pentru id se va seta null, pentru inserare, pentru base_id se va seta base_id al inregistrarii de versionat
        //todo:

        Nomenclator entry = nomenclatorRepository.findOne(intrareNomenclator.getClass(), intrareNomenclator.getId(), true);

        if( entry == null){
            throw exceptionUtil.buildException(new RanRuntimeException("Nu exista in nomenclator intrare de tip "+intrareNomenclator.getClass()+" cu ID " + intrareNomenclator));

        }

        if(!(entry instanceof Versioned)) {
            throw exceptionUtil.buildException(new RanException(new NomenclatorException(NomenclatorExceptionCodes.NOMENCLATOR_NOT_VERSIONED)));


        }

        Versioned versioned = (Versioned) entry;

        if( versioned.getDataStop() == null){
            throw exceptionUtil.buildException(new RanException(new NomenclatorException(NomenclatorExceptionCodes.NOMENCLATOR_WITHOUT_DATA_STOP)));

        }

        Nomenclator newEntry;
        try {
            newEntry = entry.getClass().newInstance();
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException("Instatiere esuata, intrare de tip "+intrareNomenclator.getClass()+ " cu ID "+intrareNomenclator));

        }

        newEntry = intrareNomenclator;

        newEntry.setId(null);

        return saveNomenclator(newEntry);
    }


    //
    // procesare referinte nomenclatoare
    // se verifica existenta si suprascrierea referintelor nomenclator cu instante context bound
    //
    private void procesareReferinte(Nomenclator nomenclator){

        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(nomenclator.getClass());
        for (PropertyDescriptor descriptor : descriptors) {
            try {
                Class<? extends Nomenclator> referencedNomenclatorClass = descriptor.getPropertyType().asSubclass(Nomenclator.class);

                Nomenclator referenced = (Nomenclator) descriptor.getReadMethod().invoke(nomenclator);

                if( referenced != null) {
                    Nomenclator referencedFromDb = nomenclatorRepository.findOne(referencedNomenclatorClass, referenced.getId());
                    if( referencedFromDb == null){
                        throw exceptionUtil.buildException(new RanRuntimeException("Nu exista in baza de date intrare de tip " + referencedNomenclatorClass + " cu id " + referenced.getId()));

                    }

                    //referinta nomenclator exista, setam obiectului care se salveaza
                    descriptor.getWriteMethod().invoke(nomenclator, referencedFromDb);
                }

            }catch(ClassCastException e){
                //este ok eroarea, nu este de tip Nomencaltor, se ignora
                //e.printStackTrace();
            } catch (Throwable e) {
                 throw new RuntimeException("Eroare la procesare referinta nomenclator proprietate " + descriptor.getName() + " nomencaltor " + nomenclator.getClass());
            }
        }

    }




    @Audit(opType = INACTIVARE_INREGISTRARE_NOMENCLATOR)
    @Override
    public void deleteNomenclator(TipNomenclator tipNomenclator, Long idIntrareNomenclator) throws RanBusinessException, RanRuntimeException, RanException {
        if (tipNomenclator == null) {
            throw exceptionUtil.buildException(new RanRuntimeException("Parametru tipNomenclator nedefinit"));

        }

        if (idIntrareNomenclator == null) {
            throw exceptionUtil.buildException(new RanRuntimeException("Parametru idIntrareNomenclator nedefinit"));

        }

        Nomenclator entry = nomenclatorRepository.findOne(tipNomenclator.getClazz(), idIntrareNomenclator);
        if (entry == null) {
            throw exceptionUtil.buildException(new RanRuntimeException("Nu exista intrare in nomenclatorul " + tipNomenclator.getClazz() + " avand id " + idIntrareNomenclator));

        }

        //
        // La categoria ”Nomenclatoare de sistem” – nu pot fi șterse elemente
        //
        if( entry instanceof NomSistem){
            throw exceptionUtil.buildException(new RanException(new NomenclatorException(NomenclatorExceptionCodes.NOMENCLATOR_SYSTEM_CANT_BE_DELETED)));

        }

        //
        // La categoria ” Nomenclatoare privind organizarea teritorială” – pot fi șterse elemente dacă nu există date care referă aceste elemente
        //
        if( entry instanceof NomOrganizareTeritoriala){
            nomenclatorRepository.delete(entry);
            return;
        }

        //
        // La categoria ”Structurile capitolelor din registru agricol” – poate fi ștearsă, de fiecare dată, ultima versiune,
        // dacă nu există date care referă acea versiune.
        //
        if( entry instanceof NomCapRegistrulAgricol){
            //
            // Stergerea nomenclatoarelor versionate este permisa doar pentru "ultima versiune" si daca nu sunt referentiate
            //

            if( !isLastVersion(entry)){
                throw exceptionUtil.buildException(new RanException(new NomenclatorException(NomenclatorExceptionCodes.NOMENCLATOR_IS_NOT_LAST_VERSION)));

            }
            nomenclatorRepository.delete(entry);
            return;
        }

        //
        // La categoria ”Nomenclatoare utilizate în registrul agricol”
        //     -	Dacă nu sunt versionabile: pot fi șterse elemente dacă nu au există date care referă aceste elemente
        //     -	Dacă sunt versionabile: poate fi ștearsă, de fiecare dată, ultima versiune, dacă nu există date care referă acea versiune.
        //

        if( entry instanceof NomRegistrulAgricol){

            if( entry instanceof Versioned) {
                if( isLastVersion(entry)){
                    nomenclatorRepository.delete(entry);
                    return;
                }else{
                    throw exceptionUtil.buildException(new RanException(new NomenclatorException(NomenclatorExceptionCodes.NOMENCLATOR_IS_NOT_LAST_VERSION)));

                }
            }else{
                nomenclatorRepository.delete(entry);
                return;
            }
        }

        //
        //La categoria ”Nomenclatoare de termeni agricoli (plante de cultură, specii animale etc.)”
        //    -	pot fi șterse elemente dacă nu au există date care referă aceste elemente
        //
        if( entry instanceof NomTermeniAgricoli){
            nomenclatorRepository.delete(entry);
            return;
        }

        //
        //
        //
        if( entry instanceof NomProductieNivelUat){
            nomenclatorRepository.delete(entry);
            return;
        }

    }

    @Audit(opType=INCHIDERE_VERSIUNE_NOMENCLATOR)
    @Override
    public Nomenclator inchidereVersiune(Nomenclator nomenclator) throws RanBusinessException, RanRuntimeException, RanException {
        if (nomenclator == null) {
            throw exceptionUtil.buildException(new RanRuntimeException("Instanta Nomenclator nedefinita"));

        }

        if( nomenclator instanceof Versioned){

            procesareReferinte(nomenclator);


            Long baseId=((Versioned) nomenclator).getBaseId();

            if(baseId != null) {
                Nomenclator entry= nomenclatorRepository.findOne(nomenclator.getClass(),nomenclator.getId());
                Date dataStart = ((Versioned) entry).getDataStart();
                if( dataStart == null){
                    throw exceptionUtil.buildException(new RanException(new NomenclatorException(NomenclatorExceptionCodes.NOMENCLATOR_WITHOUT_DATA_START)));

                }
                ((Versioned) nomenclator).setDataStart(dataStart);
                Date dataStop = ((Versioned) entry).getDataStop();
                if( dataStop != null){
                    throw exceptionUtil.buildException(new RanRuntimeException("Versiunea este deja inchisa"));

                }
                Nomenclator saved = nomenclatorRepository.save(nomenclator);
                return saved;
            }
                else{
                throw exceptionUtil.buildException(new RanRuntimeException("Instanta Nomenclator nedefinita"));

            }
            }else {
            throw exceptionUtil.buildException(new RanRuntimeException("Instanta Nomenclator nu e versionabila"));

            }
    }

    /**
     * Verifica daca intrarea de nomenclator este ultima vesiune
     * @param entry
     * @return
     */
    private boolean isLastVersion(Nomenclator entry) {
        if(!(entry instanceof Versioned)){
            throw new ClassCastException("Íntrare nomenclator  nu este de tip Versioned, " + entry.getClass());
        }

        Versioned versioned = (Versioned) entry;
        Long baseID = versioned.getBaseId();

        Nomenclator last = nomenclatorRepository.getLastVersion(entry.getClass(), baseID);

        return last != null && last.getId().equals(entry.getId());
    }

    @Override
    public GenericListResult<Institutie> getListaInstitii(NomenclatorSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo) {
        return institutieRepository.getListResult(new AndRepositoryCriteria(), pagingInfo, sortInfo);
    }

    @Override
    public GenericListResult<UAT> getListaUat(NomenclatorSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo) {
        return uatRepository.getListResult(new AndRepositoryCriteria(), pagingInfo, sortInfo);
    }

    @Override
    public GenericListResult<Judet> getListaJudete(NomenclatorSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo) {
        return judetRepository.getListResult(new AndRepositoryCriteria(), pagingInfo, sortInfo);
    }


    private void alterSessionNlsSortComp() {
        nomenclatorRepository.alterSessionNlsSortComp();
    }

    //??? Proba
    private Object clone(Object object)
    {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
