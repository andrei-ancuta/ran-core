package ro.uti.ran.core.service.utilizator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.uti.ran.core.audit.Audit;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.*;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.model.utils.RolEnum;
import ro.uti.ran.core.reporter.builders.ColumnBuilder;
import ro.uti.ran.core.reporter.builders.DynamicReportBuilder;
import ro.uti.ran.core.repository.base.AbstractRepositoryFilter;
import ro.uti.ran.core.repository.base.RepositoryWorker;
import ro.uti.ran.core.repository.base.filter.FilterBuilder;
import ro.uti.ran.core.repository.base.filter.FilterResult;
import ro.uti.ran.core.repository.base.filter.ListUtils;
import ro.uti.ran.core.repository.criteria.AndRepositoryCriteria;
import ro.uti.ran.core.repository.criteria.Operation;
import ro.uti.ran.core.repository.criteria.RepositoryCriteria;
import ro.uti.ran.core.repository.portal.*;
import ro.uti.ran.core.service.idm.CreateIdentityResult;
import ro.uti.ran.core.service.idm.Identity;
import ro.uti.ran.core.service.idm.IdmService;
import ro.uti.ran.core.service.parametru.ParametruService;
import ro.uti.ran.core.service.sistem.MailService;
import ro.uti.ran.core.service.sistem.MessageBundleService;
import ro.uti.ran.core.service.sistem.SistemService;
import ro.uti.ran.core.utils.*;
import ro.uti.ran.core.ws.internal.sesiune.EmailUrlTokenGeneratorService;
import ro.uti.ran.core.ws.internal.utilizator.RezultatVerificareUtilizator;
import ro.uti.ran.core.ws.utils.AuditInfo;
import ro.uti.ran.core.ws.utils.AuditInfoThreadLocal;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;

import java.text.MessageFormat;
import java.util.*;

import static ro.uti.ran.core.audit.AuditOpType.*;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 16:46
 */
@Service
@Transactional("portalTransactionManager")
public class UtilizatorServiceImpl implements UtilizatorService {

    public static final String NOTIFICARE_PAROLA_CREARE_CONT_SUBJECT_KEY = "notificareCerereCreareCont.subject";
    public static final String NOTIFICARE_PAROLA_CREARE_CONT_CONTENT_KEY = "notificareCerereCreareCont.content";

    private static final Logger logger = LoggerFactory.getLogger(UtilizatorServiceImpl.class);

    @Autowired
    UtilizatorRepository utilizatorRepository;

    @Autowired
    RolRepository rolRepository;

    @Autowired
    RolUtilizatorRepository rolUtilizatorRepository;

    @Autowired
    UatRepository uatRepository;

    @Autowired
    JudetRepository judetRepository;

    @Autowired
    InstitutieRepository institutieRepository;

    @Autowired
    @Qualifier(value = "idmService")
    IdmService idmService;

    @Autowired
    SistemService sistemService;

    @Autowired
    private MailService mailService;
    @Autowired
    private MessageBundleService messageBundleService;
    @Autowired
    private ParametruService parametruService;
    @Autowired
    private EmailUrlTokenGeneratorService emailUrlTokenGeneratorService;

    @Audit(opType = VERIFICARE_UTILIZATOR)
    @Override
    public RezultatVerificareUtilizator verificaUtilizator(String numeUtilizator) throws RanBusinessException {
        RezultatVerificareUtilizator rezultat = new RezultatVerificareUtilizator();

        RezultatVerificareUtilizator.Rezolutie rezolutie;

        Utilizator utilizator = getUtilizator(numeUtilizator);
        if (utilizator != null) {
            rezolutie = RezultatVerificareUtilizator.Rezolutie.EXISTA_RAN;
            //daca nu contine nif sau cnp atunci il voi adauga din IDM
            if ((utilizator.getCnp() == null || utilizator.getCnp().isEmpty())
                    && (utilizator.getNif() == null || utilizator.getNif().isEmpty())) {
                Utilizator utilizatorIdm = getUtilizatorFromIdm(numeUtilizator);
                rezultat.setUtilizatorIdm(utilizatorIdm);
            }
            rezultat.setUtilizator(utilizator);
        } else {
            utilizator = getUtilizatorFromIdm(numeUtilizator);
            if (utilizator != null) {
                rezolutie = RezultatVerificareUtilizator.Rezolutie.EXISTA_LDAP;
                rezultat.setUtilizator(utilizator);
            } else {
                rezolutie = RezultatVerificareUtilizator.Rezolutie.NU_EXISTA;
            }
        }

        rezultat.setRezolutie(rezolutie);

        return rezultat;
    }

    @Override
    public Utilizator getUtilizator(Long idUtilizator) {
        if (idUtilizator == null) {
            throw new IllegalArgumentException("Parametru idUtilizator nedefinit");
        }
        return utilizatorRepository.findOne(idUtilizator);
    }

    @Override
    public Utilizator getUtilizator(String numeUtilizator) throws RanBusinessException {
        if (StringUtils.isEmpty(numeUtilizator)) {
            throw new IllegalArgumentException("Parametru numeUtilizator nedefinit");
        }
        return utilizatorRepository.findByNumeUtilizatorIgnoreCase(numeUtilizator);
    }

    @Override
    public Utilizator getUtilizatorFromIdm(String numeUtilizator) {
        try {
            Identity identity = idmService.getIdentity(numeUtilizator);

            if (identity == null) {
                return null;
            }


            Utilizator utilizator = new Utilizator();
            utilizator.setNumeUtilizator(identity.getUsername());
            utilizator.setEmail(identity.getMail());
            utilizator.setPrenume(identity.getFirstName());
            utilizator.setNume(identity.getLastName());
            utilizator.setActiv(identity.isActive());
            utilizator.setCnp(identity.getCnp());
            utilizator.setNif(identity.getNif());

            return utilizator;
        } catch (Throwable th) {
            throw new RuntimeException("Eroare la verificare utilizator in idm", th);
        }
    }

    @Override
    public List<RolUtilizator> getRoluriUtilizator(Long idUtilizator) {
        if (idUtilizator == null) {
            throw new IllegalArgumentException("Parametru idUtilizator nedefinit");
        }
        return rolUtilizatorRepository.findByUtilizator_Id(idUtilizator);
    }

    @Override
    public List<RolUtilizator> getRoluriUtilizator(String numeUtilizator, String codContext) throws RanBusinessException {
        if (StringUtils.isEmpty(numeUtilizator)) {
            throw new IllegalArgumentException("Parametru numeUtilizator nedefinit");
        }

        if (StringUtils.isEmpty(codContext)) {
            throw new IllegalArgumentException("Parametru codContext nedefinit");
        }

        return rolUtilizatorRepository.findByUtilizator_NumeUtilizatorAndRol_Context_CodAndRol_Activ(numeUtilizator, codContext, true);
    }

    @Override
    public List<Context> getContexteUtilizator(String numeUtilizator) throws RanBusinessException {
        if (StringUtils.isEmpty(numeUtilizator)) {
            throw new IllegalArgumentException("Parametru numeUtilizator nedefinit");
        }

        List<RolUtilizator> roluri = rolUtilizatorRepository.findByUtilizator_NumeUtilizator(numeUtilizator);

        Set<Context> contexte = new HashSet<Context>();
        for (RolUtilizator rolUtilizator : roluri) {
            Rol rol = rolUtilizator.getRol();
            if (rol != null && rol.getContext() != null) {
                contexte.add(rolUtilizator.getRol().getContext());
            }
        }
        return new ArrayList<Context>(contexte);
    }

    public void validareUtilizator(Utilizator utilizator) {
        if (utilizator == null) {
            throw new IllegalArgumentException("Parametru utilizator nedefinit");
        }

        if (StringUtils.isEmpty(utilizator.getNumeUtilizator())) {
            throw new IllegalArgumentException("Nume utilizator nedefinit");
        }

        if (StringUtils.isEmpty(utilizator.getEmail())) {
            throw new IllegalArgumentException("Email utilizator nedefinit");
        }
    }

    @Audit(opType = SALVARE_UTILIZATOR)
    @Override
    public Utilizator saveUtilizator(final Utilizator utilizator) throws RanBusinessException {

        //
        // Validare utilizator
        //
        validareUtilizator(utilizator);

        //
        // Adaugare utilizator
        //
        if (utilizator.getId() == null || utilizator.getId() <= 0) {
            //
            // Verificare daca exista utilizator cu acest username
            //
            Utilizator existent = getUtilizator(utilizator.getNumeUtilizator());
            if (existent != null) {
                throw new UserAlreadyExistsException("Exist&#259; deja  un utilizator cu acest email: " + utilizator.getNumeUtilizator());
            }


            //
            // Not exist in IDM System
            //
            if (null == idmService.getIdentity(utilizator.getNumeUtilizator())) {
                //
                // Salvare utilizator in IDM
                //
                final String newPassword = PasswordUtils.generatePassword();
                CreateIdentityResult result = idmService.createIdentity(new Identity() {
                    {
                        setUsername(utilizator.getNumeUtilizator());
                        setUserpassword(newPassword);
                        setMail(utilizator.getEmail());
                        setFirstName(utilizator.getPrenume());
                        setLastName(utilizator.getNume());
                    }
                });

                if (!result.isSuccess()) {
                    throw new IllegalStateException("Nu s-a reu&#351;it crearea utilizatorului " + utilizator.getNumeUtilizator());
                }
            }

            // se activeaza contul dupa setare parola
            utilizator.setActiv(false);

            Utilizator utilizatorSalvat = null;
            try {
                utilizatorSalvat = utilizatorRepository.save(utilizator);
            } catch (Exception e) {
                if (e.getMessage().contains("unique constraint")) {
                    throw new UserAlreadyExistsException("Exist&#259; deja  un utilizator cu acest email: " + utilizator.getNumeUtilizator());
                }

                throw new IllegalStateException("Nu s-a reu&#351;it crearea utilizatorului " + utilizator.getNumeUtilizator());
            }

            if (utilizatorSalvat != null) {
                trimiteMailActivareCont(utilizatorSalvat);
            }

            return utilizatorSalvat;
        } else {
            //
            // Editare utilizator
            //
            Utilizator edited = utilizatorRepository.getOne(utilizator.getId());
            if (edited == null) {
                throw new IllegalArgumentException("Nu exista utilizator avand id " + utilizator.getId());
            }


            //
            // Salvam campurile editabile
            //
            edited.setObservatii(utilizator.getObservatii());
            edited.setCnp(utilizator.getCnp());
            edited.setNif(utilizator.getNif());

            idmService.addUserToGroups(utilizator.getNumeUtilizator());
            return utilizatorRepository.save(edited);
        }
    }

    private void trimiteMailActivareCont(Utilizator utilizator) {
        Parametru parametru = parametruService.getParametru(RanConstants.URL_LOGIN_PARAM_KEY);
        Parametru urlBaseParam = parametruService.getParametru(RanConstants.URL_BASE_PARAM_KEY);
        if (utilizator != null) {
            String userToken = emailUrlTokenGeneratorService.generateTokenAndSaveToUser(utilizator);
            StringBuilder sb = new StringBuilder(30);
            sb.append(urlBaseParam.getValoare())
                    .append(parametru.getValoare())
                    .append(MessageFormat.format(RanConstants.ACTIVATE_USER_REDIRECT_QUERY, userToken));
            mailService.sendEmail(Arrays.asList(utilizator.getEmail()),
                    messageBundleService.getMessage(NOTIFICARE_PAROLA_CREARE_CONT_SUBJECT_KEY),
                    messageBundleService.getMessage(NOTIFICARE_PAROLA_CREARE_CONT_CONTENT_KEY, new String[]{sb.toString()}));
        }
    }

    @Audit(opType = SALVARE_GOSPODAR)
    @Override
    public Utilizator saveGospodar(Utilizator utilizatorGospodar) throws RanBusinessException {
        //
        // Validare utilizator
        //
        validareUtilizator(utilizatorGospodar);

        if (StringUtils.isEmpty(utilizatorGospodar.getCnp()) && StringUtils.isEmpty(utilizatorGospodar.getNif())) {
            throw new IllegalArgumentException("Camp CNP/NIF este obligatoriu de completat!");
        }


        Utilizator saved = saveUtilizator(utilizatorGospodar);

        //if the insert is lazy this will be the place where the error will apear.
        RolUtilizator rolUtilizatorGospodar = null;
        try {
            rolUtilizatorGospodar = rolUtilizatorRepository.findByUtilizator_IdAndRol_Cod(saved.getId(), RolEnum.GOSPODAR.getCod());
        } catch (Exception e) {
            if (e.getMessage().contains("unique constraint")) {
                throw new UserAlreadyExistsException("Exist&#259; deja  un utilizator cu acest email: " + utilizatorGospodar.getNumeUtilizator());
            }

            throw new IllegalStateException("Nu s-a reu&#351;it crearea utilizatorului " + utilizatorGospodar.getNumeUtilizator());
        }

        if (rolUtilizatorGospodar == null) {
            //
            // Nu are rol GOSPODAR, il asignam
            //

            Rol rolGospodar = rolRepository.findByCod(RolEnum.GOSPODAR.getCod());
            if (rolGospodar == null) {
                throw new IllegalStateException("Nu exista rol in baza de date avand codul " + RolEnum.GOSPODAR.getCod());
            }

            RolUtilizator rolUtilizator = new RolUtilizator();
            rolUtilizator.setUtilizator(saved);
            rolUtilizator.setRol(rolGospodar);

            asigneazaRolUtilizator(saved.getId(), rolUtilizator);
        }
        return saved;
    }

    @Override
    public void deleteUtilizator(Long idUtilizator) {
        if (idUtilizator == null) {
            throw new IllegalArgumentException("Parametru idUtilizator nedefinit");
        }

        Utilizator utilizator = utilizatorRepository.findOne(idUtilizator);
        if (utilizator == null) {
            throw new IllegalArgumentException("Nu exista utilizator cu id-ul specificat, " + idUtilizator);
        }

        utilizator.setActiv(false);
        utilizatorRepository.save(utilizator);
    }


    @Override
    public GenericListResult<Utilizator> getListaUtilizatori(UtilizatoriSearchFilter utilizatoriSearchFilter, PagingInfo pagingInfo, SortInfo sortInfo) {
        final UtilizatoriSearchFilter searchFilter = utilizatoriSearchFilter == null ? new UtilizatoriSearchFilter() : utilizatoriSearchFilter;

        final FilterResult filterResult = filterResult(searchFilter);


        return getListaUtilizatori(filterResult, pagingInfo, sortInfo);
    }


    @Override
    public byte[] getListaUtilizatoriExport(UtilizatoriSearchFilter utilizatoriSearchFilter, SortInfo sortInfo, String format) {
        final UtilizatoriSearchFilter searchFilter = utilizatoriSearchFilter == null ? new UtilizatoriSearchFilter() : utilizatoriSearchFilter;

        final FilterResult filterResult = filterResult(searchFilter);

        GenericListResult<Utilizator> listaUtilizatori = getListaUtilizatori(filterResult, null, sortInfo);


        List<UtilizatorRowItem> items = new LinkedList<>();
        if (listaUtilizatori != null && listaUtilizatori.getItems() != null) {

            for (Utilizator utilizator : listaUtilizatori.getItems()) {

                UtilizatorRowItem item = new UtilizatorRowItem();
                Set<String> listaPerspective = new LinkedHashSet<String>();
                Set<String> listaRoluri = new LinkedHashSet<String>();

                item.setId(utilizator.getId());
                item.setEmail(StringUtils.defaultIfEmpty(utilizator.getEmail(), ""));
                item.setNume(StringUtils.defaultIfEmpty(utilizator.getNume(), ""));
                item.setPrenume(StringUtils.defaultIfEmpty(utilizator.getPrenume(), ""));

                List<RolUtilizator> listaRoluriUtilizator = getRoluriUtilizator(item.getId());

                for (RolUtilizator rol : listaRoluriUtilizator) {
                    listaRoluri.add(rol.getRol().getDenumire());
                    listaPerspective.add(rol.getRol().getContext().getDenumire());
                }

                item.setPerspective(fromListToString(new ArrayList<String>(listaPerspective)));
                item.setRoluri(fromListToString(new ArrayList<String>(listaRoluri)));

                items.add(item);
            }
        }


        DynamicReportBuilder utilizatoriReport = new DynamicReportBuilder(items, UtilizatorRowItem.class);

        utilizatoriReport.setConcreteColumnList("nume", "prenume", "email", "perspective", "roluri");
        Map<String, ColumnBuilder> columns = utilizatoriReport.showReportByColumnName();
        columns.get("nume").setName("Nume");
        columns.get("prenume").setName("Prenume");
        columns.get("email").setName("Email");
        columns.get("perspective").setName("Tip cont");
        columns.get("roluri").setName("Roluri");

        if (utilizatoriReport.XLS.equalsIgnoreCase(format)) {
            return utilizatoriReport.buildReport("Raport utilizatori", utilizatoriReport.XLS);
        } else if (utilizatoriReport.PDF.equalsIgnoreCase(format)) {
            return utilizatoriReport.buildReport("Raport utilizatori", utilizatoriReport.PDF);
        } else if (utilizatoriReport.HTML.equalsIgnoreCase(format)) {
            return utilizatoriReport.buildReport("Raport utilizatori", utilizatoriReport.HTML);
        } else if (utilizatoriReport.JPEG.equalsIgnoreCase(format)) {
            return utilizatoriReport.buildReport("Raport utilizatori", utilizatoriReport.JPEG);
        } else {
            //default
            return utilizatoriReport.buildReport("Raport utilizatori", utilizatoriReport.PDF);
        }


    }

    //
    // Nu merge versiunea
    // Posibil bug OpenJPA
    //
    /*
    Caused by: org.apache.openjpa.persistence.PersistenceException: null
	at org.apache.openjpa.kernel.QueryImpl.createExecutor(QueryImpl.java:763) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.kernel.QueryImpl.compileForDataStore(QueryImpl.java:706) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.kernel.QueryImpl.compileForExecutor(QueryImpl.java:688) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.kernel.QueryImpl.getOperation(QueryImpl.java:1528) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.kernel.DelegatingQuery.getOperation(DelegatingQuery.java:123) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.persistence.QueryImpl.execute(QueryImpl.java:268) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.persistence.QueryImpl.getResultList(QueryImpl.java:290) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.persistence.QueryImpl.getSingleResult(QueryImpl.java:318) ~[openjpa-2.4.0.jar:2.4.0]
	at ro.uti.ran.core.repository.portal.TestRepository.getList(TestRepository.java:104) ~[classes/:na]
	at ro.uti.ran.core.repository.portal.TestRepository$$FastClassBySpringCGLIB$$4efea465.invoke(<generated>) ~[spring-core-4.2.0.RELEASE.jar:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) ~[spring-core-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:717) ~[spring-aop-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) ~[spring-aop-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) ~[spring-tx-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) ~[spring-aop-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:653) ~[spring-aop-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at ro.uti.ran.core.repository.portal.TestRepository$$EnhancerBySpringCGLIB$$71a37f0d.getList(<generated>) ~[spring-core-4.2.0.RELEASE.jar:na]
	at ro.uti.ran.core.service.utilizator.UtilizatorServiceImpl.getListaUtilizatori(UtilizatorServiceImpl.java:271) ~[classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.7.0-internal]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57) ~[na:1.7.0-internal]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.7.0-internal]
	at java.lang.reflect.Method.invoke(Method.java:601) ~[na:1.7.0-internal]
	at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:302) ~[spring-aop-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:190) ~[spring-aop-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) ~[spring-aop-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at org.springframework.transaction.interceptor.TransactionInterceptor$1.proceedWithInvocation(TransactionInterceptor.java:99) ~[spring-tx-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:281) ~[spring-tx-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:96) ~[spring-tx-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) ~[spring-aop-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:207) ~[spring-aop-4.2.0.RELEASE.jar:4.2.0.RELEASE]
	at $Proxy142.getListaUtilizatori(Unknown Source) ~[na:na]
	at ro.uti.ran.core.ws.internal.utilizator.AdminUtilizatorImpl.getListaUtilizatori(AdminUtilizatorImpl.java:100) ~[classes/:na]
	... 64 common frames omitted
Caused by: java.util.EmptyStackException: null
	at java.util.Stack.peek(Stack.java:102) ~[na:1.7.0-internal]
	at org.apache.openjpa.persistence.criteria.SubqueryImpl.toValue(SubqueryImpl.java:287) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.persistence.criteria.Expressions.toValue(Expressions.java:68) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.persistence.criteria.Expressions$In.toKernelExpression(Expressions.java:1481) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.persistence.criteria.PredicateImpl.toKernelExpression(PredicateImpl.java:166) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.persistence.criteria.CriteriaExpressionBuilder.evalFilter(CriteriaExpressionBuilder.java:215) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.persistence.criteria.CriteriaExpressionBuilder.getQueryExpressions(CriteriaExpressionBuilder.java:75) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.persistence.criteria.CriteriaQueryImpl.getQueryExpressions(CriteriaQueryImpl.java:423) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.persistence.criteria.CriteriaBuilderImpl.eval(CriteriaBuilderImpl.java:81) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.kernel.ExpressionStoreQuery$DataStoreExecutor.<init>(ExpressionStoreQuery.java:763) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.kernel.ExpressionStoreQuery.newDataStoreExecutor(ExpressionStoreQuery.java:179) ~[openjpa-2.4.0.jar:2.4.0]
	at org.apache.openjpa.kernel.QueryImpl.createExecutor(QueryImpl.java:748) ~[openjpa-2.4.0.jar:2.4.0]
	... 95 common frames omitted


         */
    public GenericListResult<Utilizator> getListaUtilizatori1(UtilizatoriSearchFilter utilizatoriSearchFilter, PagingInfo pagingInfo, SortInfo sortInfo) {
        final UtilizatoriSearchFilter searchFilter = utilizatoriSearchFilter == null ? new UtilizatoriSearchFilter() : utilizatoriSearchFilter;

        return utilizatorRepository.getListResult(new AbstractRepositoryFilter() {
            @Override
            public Predicate[] buildFilter(CriteriaBuilder cb, CriteriaQuery cq, Root from) {

                if (StringUtils.isNotEmpty(searchFilter.getNumeUtilizator())) {
                    addLikePredicate(cb, from.get("numeUtilizator"), searchFilter.getNumeUtilizator());
                }

                if (StringUtils.isNotEmpty(searchFilter.getNume())) {
                    addLikePredicate(cb, from.get("nume"), searchFilter.getNume());
                }

                if (StringUtils.isNotEmpty(searchFilter.getPrenume())) {
                    addLikePredicate(cb, from.get("prenume"), searchFilter.getPrenume());
                }

                if (StringUtils.isNotEmpty(searchFilter.getCnp())) {
                    addLikePredicate(cb, from.get("cnp"), searchFilter.getCnp());
                } else if (StringUtils.isNotEmpty(searchFilter.getNif())) {
                    addLikePredicate(cb, from.get("nif"), searchFilter.getNif());
                }

                if (StringUtils.isNotEmpty(searchFilter.getEmail())) {
                    addLikePredicate(cb, from.get("email"), searchFilter.getEmail());
                }

                boolean filterByRolUtilizator =
                        searchFilter.getIdUat() != null && searchFilter.getIdUat().size() > 0
                                ||
                                searchFilter.getIdInstitutie() != null && searchFilter.getIdInstitutie().size() > 0
                                ||
                                searchFilter.getIdJudet() != null && searchFilter.getIdJudet().size() > 0;

                if (filterByRolUtilizator) {
                    Subquery<RolUtilizator> subquery = cq.subquery(RolUtilizator.class);
                    Root fromRolUtilizator = subquery.from(RolUtilizator.class);
                    subquery.select(fromRolUtilizator.join("utilizator").get("id"));


                    List<Predicate> rolUtilizatorPredicates = new LinkedList<Predicate>();

                    //
                    // Daca are roluri pentru UAT dat
                    //
                    if (searchFilter.getIdUat() != null && searchFilter.getIdUat().size() > 0) {
                        rolUtilizatorPredicates.add(
                                cb.in(fromRolUtilizator.join("uat").get("id")).value(searchFilter.getIdUat())
                        );
                    }

                    //
                    // Daca are roluri pentru Institutie data
                    //
                    if (searchFilter.getIdInstitutie() != null && searchFilter.getIdInstitutie().size() > 0) {
                        rolUtilizatorPredicates.add(
                                fromRolUtilizator.join("institutie").get("id").in(searchFilter.getIdInstitutie())
                        );
                    }

                    //
                    // Daca are roluri pentru JUDET dat sau pentru UAT care apartine de JUDET dat
                    //
                    if (searchFilter.getIdJudet() != null && searchFilter.getIdJudet().size() > 0) {
                        rolUtilizatorPredicates.add(
                                cb.or(
                                        fromRolUtilizator.join("judet").get("id").in(searchFilter.getIdJudet()),
                                        fromRolUtilizator.join("uat").join("judet").get("id").in(searchFilter.getIdJudet())
                                )
                        );
                    }

                    if (rolUtilizatorPredicates.size() == 0) {
                        throw new IllegalStateException("Predicate rolUtilizator nedefinite");
                    }

                    subquery.where(rolUtilizatorPredicates.toArray(new Predicate[rolUtilizatorPredicates.size()]));

                    addPredicate(cb.in(from.get("id")).value(subquery));
                }

                return predicatesArray();
            }
        }, pagingInfo, sortInfo);
    }

    @Override
    public void schimbaParolaUtilizator(String username, String newPassword) throws RanBusinessException {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("Nume utilizator nespecificat");
        }

        if (StringUtils.isEmpty(newPassword)) {
            throw new IllegalArgumentException("Parola noua utilizator nespecificata");
        }

        idmService.changeUserPassword(username, newPassword);
    }

    @Override
    public void reseteazaParolaUtilizator(String username) throws RanBusinessException {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("Nume utilizator nespecificat");
        }

        idmService.resetUserPassword(username, RanConstants.DEFAULT_PASSWORD);
    }

    @Override
    public List<Utilizator> getUtilizatoriActiviByRolAndUatCodSiruta(String codRol, Boolean rolActiv, Integer codSiruta, Boolean utilizatorActiv) {
        if (codSiruta == null) {
            throw new IllegalArgumentException("Cod siruta nespecificat");
        }
        if (StringUtils.isEmpty(codRol)) {
            throw new IllegalArgumentException("Cod rol nespecificat");
        }
        List<RolUtilizator> rolUtilizatorList = rolUtilizatorRepository.findByRol_CodAndRol_ActivAndUat_CodSirutaAndUtilizator_Activ(codRol, true, codSiruta, true);

        if (rolUtilizatorList != null && !rolUtilizatorList.isEmpty()) {
            List<Utilizator> utilizatorList = new ArrayList<Utilizator>();
            for (RolUtilizator rolUtilizator : rolUtilizatorList) {
                if (rolUtilizator != null && rolUtilizator.getUtilizator() != null) {
                    utilizatorList.add(rolUtilizator.getUtilizator());
                }
            }
            return utilizatorList.isEmpty() ? null : utilizatorList;
        }
        return null;
    }

    @Override
    public List<RolUtilizator> getUtilizatorByCNPAndRol(String codRol, String CNP) {
        return rolUtilizatorRepository.findByRol_CodAndRol_ActivAndUtilizator_CnpAndUtilizator_Activ(codRol, true, CNP, true);
    }

    @Override
    public List<RolUtilizator> getUtilizatorByNIFAndRol(String codRol, String NIF) {
        return rolUtilizatorRepository.findByRol_CodAndRol_ActivAndUtilizator_NifAndUtilizator_Activ(codRol, true, NIF, true);
    }

    @Override
    public Rol getRol(Long idRol) {
        if (idRol == null) {
            throw new IllegalArgumentException("Parametru idRol nedefinit");
        }
        return rolRepository.findOne(idRol);
    }

    @Override
    public Rol getRol(String codRol) {
        if (StringUtils.isEmpty(codRol)) {
            throw new IllegalArgumentException("Parametru cod rol nespecificat");
        }
        return rolRepository.findByCod(codRol);
    }

    @Override
    public Rol saveRol(Rol rol) {
        if (rol == null) {
            throw new IllegalArgumentException("Parametru rol nedefinit");
        }

        //todo: validari business rol

        return rolRepository.save(rol);
    }

    @Override
    public void deleteRol(Long idRol) {
        if (idRol == null) {
            throw new IllegalArgumentException("Parametru idRol nedefinit");
        }

        Rol rol = rolRepository.findOne(idRol);
        if (rol == null) {
            throw new IllegalArgumentException("Nu exista rol cu id-ul specificat, " + idRol);
        }

        rol.setActiv(false);
        rolRepository.save(rol);
    }

    @Override
    public GenericListResult<Rol> getListaRoluri(final RoluriSearchFilter roluriSearchFilter, final PagingInfo pagingInfo, final SortInfo sortInfo) {
        return rolRepository.getListResult(
                roluriSearchFilter != null ? new AndRepositoryCriteria() {
                    {
                        if (StringUtils.isNotEmpty(roluriSearchFilter.getCod()) || (roluriSearchFilter.getCoduri() != null && roluriSearchFilter.getCoduri().size() > 0)) {

                            List<String> coduri = new LinkedList<String>();
                            if (StringUtils.isNotEmpty(roluriSearchFilter.getCod())) {
                                coduri.add(roluriSearchFilter.getCod());
                            }

                            if (roluriSearchFilter.getCoduri() != null && roluriSearchFilter.getCoduri().size() > 0) {
                                coduri.addAll(roluriSearchFilter.getCoduri());
                            }

                            add(new RepositoryCriteria<List<String>>("cod", Operation.IN, coduri));
                        }

                        if (StringUtils.isNotEmpty(roluriSearchFilter.getDenumire())) {
                            add(new RepositoryCriteria<String>("denumire", Operation.ILIKE, roluriSearchFilter.getDenumire()));
                        }

                        if (StringUtils.isNotEmpty(roluriSearchFilter.getCodContext())) {
                            add(new RepositoryCriteria<String>("context.cod", Operation.EQ, roluriSearchFilter.getCodContext()));
                        }
                    }
                } : null, pagingInfo, sortInfo
        );
    }

    private void validareUnicitateAdaugareRol(Long idUtilizator, RolUtilizator rolUtilizator) {
        List<RolUtilizator> lstRoluriDB = getRoluriUtilizator(idUtilizator);
        if (lstRoluriDB != null) {
            for (RolUtilizator dbRol : lstRoluriDB) {
                if (suntEgale(dbRol, rolUtilizator)) {
                    throw new IllegalArgumentException("Rolul avand codul '" + rolUtilizator.getRol().getCod() + "' este deja asociat utilizatorului!");
                }
            }
        }
    }

    private void validareUnicitateAdaugareRoluri(Long idUtilizator, List<RolUtilizator> lstNewRols) {
        List<RolUtilizator> lstRoluriDB = getRoluriUtilizator(idUtilizator);
        if (lstRoluriDB != null) {
            for (RolUtilizator dbRol : lstRoluriDB) {
                for (RolUtilizator newRol : lstNewRols) {
                    if (suntEgale(dbRol, newRol)) {
                        throw new IllegalArgumentException("Rolul avand codul '" + newRol.getRol().getCod() + "' este deja asociat utilizatorului!");
                    }
                }
            }
        }
    }


    private boolean suntEgale(RolUtilizator rolUnu, RolUtilizator rolDoi) {
        //aceasi instanta
        if (rolUnu == rolDoi) {
            return true;
        }
        //unul din ele este null
        if (rolUnu == null || rolDoi == null) {
            return false;
        }
        //roluri diferite
        if (!rolUnu.getRol().getId().equals(rolDoi.getRol().getId())) {
            return false;
        }
        //uat diferit
        if (rolUnu.getUat() != null && rolDoi.getUat() == null) {
            return false;
        }
        if (rolUnu.getUat() == null && rolDoi.getUat() != null) {
            return false;
        }
        if (rolUnu.getUat() != null && rolDoi.getUat() != null && !rolUnu.getUat().getId().equals(rolDoi.getUat().getId())) {
            return false;
        }
        //judet diferit
        if (rolUnu.getJudet() != null && rolDoi.getJudet() == null) {
            return false;
        }
        if (rolUnu.getJudet() == null && rolDoi.getJudet() != null) {
            return false;
        }
        if (rolUnu.getJudet() != null && rolDoi.getJudet() != null && !rolUnu.getJudet().getId().equals(rolDoi.getJudet().getId())) {
            return false;
        }
        //institutie diferita
        if (rolUnu.getInstitutie() != null && rolDoi.getInstitutie() == null) {
            return false;
        }
        if (rolUnu.getInstitutie() == null && rolDoi.getInstitutie() != null) {
            return false;
        }
        if (rolUnu.getInstitutie() != null && rolDoi.getInstitutie() != null && !rolUnu.getInstitutie().getId().equals(rolDoi.getInstitutie().getId())) {
            return false;
        }
        return true;
    }

    @Audit(opType = ASIGNARE_ROL)
    @Override
    public void asigneazaRolUtilizator(Long idUtilizator, RolUtilizator rolUtilizator) {
        if (idUtilizator == null) {
            throw new IllegalArgumentException("Parametru idUtilizator nedefinit");
        }

        if (rolUtilizator == null) {
            throw new IllegalArgumentException("Parametru rolUtilizator nedefinit");
        }

        if (rolUtilizator.getRol() == null) {
            throw new IllegalArgumentException("Campul rolUtilizator.rol nedefinit");
        }

        Utilizator utilizator = utilizatorRepository.findOne(idUtilizator);
        if (utilizator == null) {
            throw new IllegalArgumentException("Nu exista utilizator cu id-ul specificat, " + idUtilizator);
        }

        //
        rolUtilizator.setUtilizator(utilizator);

        boolean valid = false;
        if (rolUtilizator.getUat() != null) {
            UAT uat = uatRepository.getOne(rolUtilizator.getUat().getId());
            if (uat == null) {
                throw new IllegalArgumentException("Referinta UAT invalida, nu exista UAT avand id " + rolUtilizator.getUat().getId());
            }
            rolUtilizator.setUat(uat);
            valid = true;
        }

        if (rolUtilizator.getJudet() != null) {
            Judet judet = judetRepository.getOne(rolUtilizator.getJudet().getId());
            if (judet == null) {
                throw new IllegalArgumentException("Referinta Judet invalida, nu exista Judet avand id " + rolUtilizator.getJudet().getId());
            }
            rolUtilizator.setJudet(judet);
            valid = true;
        }

        if (rolUtilizator.getInstitutie() != null) {
            Institutie institutie = institutieRepository.getOne(rolUtilizator.getInstitutie().getId());
            if (institutie == null) {
                throw new IllegalArgumentException("Referinta Institutie invalida, nu exista Institutie avand id " + rolUtilizator.getInstitutie().getId());
            }
            rolUtilizator.setInstitutie(institutie);
            valid = true;
        }

        validareUnicitateAdaugareRol(idUtilizator, rolUtilizator);

        //
        // TODO: Validarile de business la salvare rol
        //

        rolUtilizatorRepository.save(rolUtilizator);
    }

    @Audit(opType = REVOCARE_ROL)
    @Override
    public void revocaRoluriUtilizator(Long idUtilizator, List<Long> idRoluriUtilizator) {
        if (idUtilizator == null) {
            throw new IllegalArgumentException("Parametru idUtilizator nedefinit");
        }

        if (idRoluriUtilizator == null || idRoluriUtilizator.size() == 0) {
            throw new IllegalArgumentException("Nu au fost specificate rolurile utilizator pentru revocare ");
        }

        Utilizator utilizator = utilizatorRepository.findOne(idUtilizator);
        if (utilizator == null) {
            throw new IllegalArgumentException("Nu exista utilizator cu id-ul specificat, " + idUtilizator);
        }


        List<RolUtilizator> roluri = rolUtilizatorRepository.findByUtilizator_IdAndIdRolUtilizator(idUtilizator, idRoluriUtilizator);
        if (roluri == null || roluri.size() == 0) {
            throw new IllegalArgumentException("Nu exista roluri utilizator cu parametri specificati");
        }

        for(RolUtilizator rolUtilizator : roluri) {
            if(RolEnum.GOSPODAR.getCod().equals(rolUtilizator.getRol().getCod())) {
                utilizator.setCnp(null);
                utilizator.setNif(null);
                utilizatorRepository.save(utilizator);
            }
        }

        //
        // TODO: Validarile de business
        //
        rolUtilizatorRepository.delete(roluri);
    }

    @Audit(opType = ASIGNARE_ROL)
    @Override
    public void updateRoluriUtilizatorTipUATLocalJudetean(Long idUtilizator, boolean stergRoluriVechiTipUat, List<RolUtilizator> listaRoluri) throws RanBusinessException {
        if (idUtilizator == null) {
            throw new IllegalArgumentException("Parametru idUtilizator nedefinit");
        }
        if (listaRoluri == null || listaRoluri.isEmpty()) {
            throw new IllegalArgumentException("Parametru listaRoluri nedefinit");
        }
        //
        Utilizator utilizator = getUtilizator(idUtilizator);
        if (utilizator == null) {
            throw new IllegalArgumentException("Nu exista utilizator cu id-ul specificat, " + idUtilizator);
        }
        //sterg vechile roluri daca este cazul
        if (stergRoluriVechiTipUat) {
            List<RolUtilizator> oldRoluri = getRoluriUtilizator(utilizator.getNumeUtilizator(), "UAT");
            for (RolUtilizator oldRol : oldRoluri) {
                rolUtilizatorRepository.delete(oldRol);
            }
        }
        //initializez noile roluri
        for (RolUtilizator newRol : listaRoluri) {
            //utilizator
            newRol.setUtilizator(utilizator);
            //judet
            if (newRol.getJudet() != null) {
                Judet judet = judetRepository.getOne(newRol.getJudet().getId());
                if (judet == null) {
                    throw new IllegalArgumentException("Referinta Judet invalida, nu exista Judet avand id " + newRol.getJudet().getId());
                }
                newRol.setJudet(judet);
            }
            //uat
            if (newRol.getUat() != null) {
                UAT uat = uatRepository.getOne(newRol.getUat().getId());
                if (uat == null) {
                    throw new IllegalArgumentException("Referinta UAT invalida, nu exista UAT avand id " + newRol.getUat().getId());
                }
                newRol.setUat(uat);
            }
        }

        validareUnicitateAdaugareRoluri(idUtilizator, listaRoluri);

        //adaug noile roluri
        for (RolUtilizator newRol : listaRoluri) {
            rolUtilizatorRepository.save(newRol);
        }
    }


    @Audit(opType = ASIGNARE_ROL)
    @Override
    public void updateRoluriUtilizatorTipInstitutieExt(Long idUtilizator, boolean stergRoluriVechiTipInstitutie, List<RolUtilizator> listaRoluri) throws RanBusinessException {
        if (idUtilizator == null) {
            throw new IllegalArgumentException("Parametru idUtilizator nedefinit");
        }
        if (listaRoluri == null || listaRoluri.isEmpty()) {
            throw new IllegalArgumentException("Parametru listaRoluri nedefinit");
        }
        //
        Utilizator utilizator = getUtilizator(idUtilizator);
        if (utilizator == null) {
            throw new IllegalArgumentException("Nu exista utilizator cu id-ul specificat, " + idUtilizator);
        }
        //sterg vechile roluri daca este cazul
        if (stergRoluriVechiTipInstitutie) {
            List<RolUtilizator> oldRoluri = getRoluriUtilizator(utilizator.getNumeUtilizator(), "INSTITUTIE");
            for (RolUtilizator oldRol : oldRoluri) {
                rolUtilizatorRepository.delete(oldRol);
            }
        }

        //initializez noile roluri
        for (RolUtilizator newRol : listaRoluri) {
            //utilizator
            newRol.setUtilizator(utilizator);
            //institutie
            if (newRol.getInstitutie() != null) {
                Institutie institutie = institutieRepository.getOne(newRol.getInstitutie().getId());
                if (institutie == null) {
                    throw new IllegalArgumentException("Referinta Institutie invalida, nu exista Institutie avand id " + newRol.getInstitutie().getId());
                }
                newRol.setInstitutie(institutie);
            }
        }

        validareUnicitateAdaugareRoluri(idUtilizator, listaRoluri);

        //adaug noile roluri
        for (RolUtilizator newRol : listaRoluri) {
            rolUtilizatorRepository.save(newRol);
        }
    }

    @Audit(opType = ASIGNARE_ROL)
    @Override
    public void adaugaRoluriUtilizator(Long idUtilizator, List<RolUtilizator> listaRoluri) throws RanBusinessException {
        if (idUtilizator == null) {
            throw new IllegalArgumentException("Parametru idUtilizator nedefinit");
        }
        if (listaRoluri == null || listaRoluri.isEmpty()) {
            throw new IllegalArgumentException("Parametru listaRoluri nedefinit");
        }
        //
        Utilizator utilizator = getUtilizator(idUtilizator);
        if (utilizator == null) {
            throw new IllegalArgumentException("Nu exista utilizator cu id-ul specificat, " + idUtilizator);
        }

        //initializez noile roluri
        for (RolUtilizator newRol : listaRoluri) {
            //utilizator
            newRol.setUtilizator(utilizator);
            //judet
            if (newRol.getJudet() != null) {
                Judet judet = judetRepository.getOne(newRol.getJudet().getId());
                if (judet == null) {
                    throw new IllegalArgumentException("Referinta Judet invalida, nu exista Judet avand id " + newRol.getJudet().getId());
                }
                newRol.setJudet(judet);
            }
            //uat
            if (newRol.getUat() != null) {
                UAT uat = uatRepository.getOne(newRol.getUat().getId());
                if (uat == null) {
                    throw new IllegalArgumentException("Referinta UAT invalida, nu exista UAT avand id " + newRol.getUat().getId());
                }
                newRol.setUat(uat);
            }
            //institutie
            if (newRol.getInstitutie() != null) {
                Institutie institutie = institutieRepository.getOne(newRol.getInstitutie().getId());
                if (institutie == null) {
                    throw new IllegalArgumentException("Referinta Institutie invalida, nu exista Institutie avand id " + newRol.getInstitutie().getId());
                }
                newRol.setInstitutie(institutie);
            }
        }

        validareUnicitateAdaugareRoluri(idUtilizator, listaRoluri);

        //adaug noile roluri
        for (RolUtilizator newRol : listaRoluri) {
            rolUtilizatorRepository.save(newRol);
        }
    }

    /**
     * @param idUtilizator
     * @param idUat
     * @param cnp
     * @param nif
     */
    @Audit(opType = ASIGNARE_ROL)
    @Override
    public void adaugaRolGospodar(Long idUtilizator, Long idUat, String cnp, String nif) throws RanBusinessException {
        if (idUtilizator == null) {
            throw new IllegalArgumentException("Parametru idUtilizator nedefinit");
        }
        AuditInfo auditInfo = AuditInfoThreadLocal.get();
        if (!"ANCPI".equals(auditInfo.getContext())) {
            if (idUat == null) {
                throw new IllegalArgumentException("Parametru idUat nedefinit");
            }
        }
        if (StringUtils.isEmpty(cnp) && StringUtils.isEmpty(nif)) {
            throw new IllegalArgumentException("Parametrii cnp/nif nedefiniti");
        }
        Utilizator utilizator = getUtilizator(idUtilizator);
        if (utilizator == null) {
            throw new IllegalArgumentException("Nu exista utilizator cu id-ul specificat, " + idUtilizator);
        }
        UAT uat = null;
        if (!"ANCPI".equals(auditInfo.getContext())) {
            uat = uatRepository.getOne(idUat);
            if (uat == null) {
                throw new IllegalArgumentException("Referinta UAT invalida, nu exista UAT avand id " + idUat);
            }
        }


        //CNP/NIF
        utilizator.setCnp(cnp);
        utilizator.setNif(nif);
        utilizatorRepository.save(utilizator);
        //
        RolUtilizator newRol = new RolUtilizator();
        //rol de gospodar
        newRol.setRol(rolRepository.findByCod(RolEnum.GOSPODAR.getCod()));
        //utilizator
        newRol.setUtilizator(utilizator);
        //uat
        newRol.setUat(uat);
        //
        validareUnicitateAdaugareRol(idUtilizator, newRol);
        //
        rolUtilizatorRepository.save(newRol);
    }


    private GenericListResult<Utilizator> getListaUtilizatori(final FilterResult filterResult, PagingInfo pagingInfo, SortInfo sortInfo) {

        return utilizatorRepository.getListResult(new RepositoryWorker<Utilizator>() {

            private String getOrder(SortInfo sortInfo) {
                if (sortInfo != null && sortInfo.getCriterias() != null && sortInfo.getCriterias().size() > 0) {
                    StringBuilder builder = new StringBuilder(" order by ");
                    for (SortCriteria sortCriteria : sortInfo.getCriterias()) {
                        if (sortCriteria.getPath() != null && sortCriteria.getOrder() != null) {
                            builder.append(" u.").append(sortCriteria.getPath()).append(" ").append(sortCriteria.getOrder());
                        }
                    }
                    return builder.toString();
                }
                return "";
            }

            @Override
            public List<Utilizator> getList(EntityManager entityManager, PagingInfo pagingInfo, SortInfo sortInfo) {

                //todo: sort criteria

                String jpaql = "select u from Utilizator u where 0=0 " + filterResult.getFilter() + " " + getOrder(sortInfo);

                logger.debug("Runing query {}", jpaql);

                Query query = entityManager.createQuery(jpaql)
                        .setFirstResult(pagingInfo.getFirstResult())
                        .setMaxResults(pagingInfo.getMaxResults());

                filterResult.setParameters(query);

                return query.getResultList();
            }

            @Override
            public Long getCount(EntityManager entityManager) {

                String jpaql = "select count(u.id) from Utilizator u where 0=0 " + filterResult.getFilter();

                logger.debug("Runing query {}", jpaql);

                Query query = entityManager.createQuery(jpaql);

                filterResult.setParameters(query);

                Object count = query.getSingleResult();

                return new Long(count.toString());
            }
        }, pagingInfo, sortInfo);


    }

    private FilterResult filterResult(final UtilizatoriSearchFilter searchFilter) {


        return new FilterBuilder() {

            private String likeUpperValue(String value) {
                return "%" + value.trim().toUpperCase() + "%";
            }

            private String likeValue(String value) {
                return "%" + value.trim() + "%";
            }

            @Override
            public FilterResult buildFilter() {

                FilterResult result = new FilterResult();

                if (StringUtils.isNotEmpty(searchFilter.getNumeUtilizator())) {
                    result.addFiler("and upper(u.numeUtilizator) like :numeUtlizator", "numeUtlizator", likeUpperValue(searchFilter.getNumeUtilizator()));
                }

                if (StringUtils.isNotEmpty(searchFilter.getNume())) {
                    result.addFiler("and upper(u.nume) like :nume", "nume", likeUpperValue(searchFilter.getNume()));
                }

                if (StringUtils.isNotEmpty(searchFilter.getPrenume())) {
                    result.addFiler("and upper(u.prenume) like :prenume", "prenume", likeUpperValue(searchFilter.getPrenume()));
                }

                // search by CNP or NIF
                if (StringUtils.isNotEmpty(searchFilter.getCnp())) {
                    result.addFiler("and u.cnp like :cnp", "cnp", likeValue(searchFilter.getCnp()));
                } else if (StringUtils.isNotEmpty(searchFilter.getNif())) {
                    result.addFiler("and u.nif like :nif", "nif", likeValue(searchFilter.getNif()));
                }

                if (StringUtils.isNotEmpty(searchFilter.getEmail())) {
                    result.addFiler("and upper(u.email) like :email", "email", likeUpperValue(searchFilter.getEmail()));
                }

                if (searchFilter.getIdContext() != null && searchFilter.getIdContext().size() > 0) {
                    result.addFiler(" and u.id in (select ru.utilizator.id from RolUtilizator ru where ru.rol.context.id in (" + ListUtils.csv(searchFilter.getIdContext()) + "))");
                }

                if (searchFilter.getIdRol() != null && searchFilter.getIdRol().size() > 0) {
                    result.addFiler(" and u.id in (select ru.utilizator.id from RolUtilizator ru where ru.rol.id in (" + ListUtils.csv(searchFilter.getIdRol()) + "))");
                }

                boolean filterByRolUtilizator =
                        searchFilter.getIdUat() != null && searchFilter.getIdUat().size() > 0
                                ||
                                searchFilter.getIdInstitutie() != null && searchFilter.getIdInstitutie().size() > 0
                                ||
                                searchFilter.getIdJudet() != null && searchFilter.getIdJudet().size() > 0;

                if (filterByRolUtilizator) {

                    result.addFiler(" and u.id in (select ru.utilizator.id from RolUtilizator ru where 1=0 ");


                    //
                    // Daca are roluri pentru UAT dat
                    //
                    if (searchFilter.getIdUat() != null && searchFilter.getIdUat().size() > 0) {
                        result.addFiler(" OR ru.uat.id IN (" + ListUtils.csv(searchFilter.getIdUat()) + ")");
                    }

                    //
                    // Daca are roluri pentru Institutie data
                    //
                    if (searchFilter.getIdInstitutie() != null && searchFilter.getIdInstitutie().size() > 0) {
                        result.addFiler(" OR ru.institutie.id IN (" + ListUtils.csv(searchFilter.getIdInstitutie()) + ")");
                    }

                    //
                    // Daca are roluri pentru JUDET dat sau pentru UAT care apartine de JUDET dat
                    //
                    if (searchFilter.getIdJudet() != null && searchFilter.getIdJudet().size() > 0) {
                        result.addFiler(" OR ( " +
                                "ru.judet.id IN (" + ListUtils.csv(searchFilter.getIdJudet()) + ") OR " +
                                "ru.uat.judet.id IN (" + ListUtils.csv(searchFilter.getIdJudet()) + ") " +
                                ")");
                    }

                    result.addFiler(")");
                }

                return result;
            }
        }.buildFilter();

    }


    private String fromListToString(List<String> values) {
        //Concatinam lista si fie in String
        StringBuilder sb = new StringBuilder();
        for (String strValues : values) {
            System.out.println(strValues);
            sb.append(strValues);
            sb.append("/ ");


        }

        return sb.toString();
    }

}
