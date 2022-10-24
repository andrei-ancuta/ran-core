package ro.uti.ran.core.repository.incarcari;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.model.portal.RegistruPortal;
import ro.uti.ran.core.model.portal.RegistruPortalDetails;
import ro.uti.ran.core.repository.base.RepositoryFilter;
import ro.uti.ran.core.repository.portal.RegistruPortalDetailsRepository;
import ro.uti.ran.core.utils.GenericListResult;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by Anastasia cea micuta on 10/21/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                PortalPersistenceLayerConfig.class,
                RegistruPortalRepositoryTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class RegistruPortalRepositoryTest {

    @ContextConfiguration
    @ComponentScan({"ro.uti.ran.core.repository.portal"})
    static class ImportConfiguration {

    }

    @Autowired
    private RegistruPortalDetailsRepository registruPortal;

    @Test
    public void getGetList() {

        final long idIncarcare = 11L;



//        Page<RegistruPortal> page = registruPortal.findAll(new Specification<RegistruPortal>() {
//
//            @Override
//            public Predicate toPredicate(Root<RegistruPortal> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//
//                List<Predicate> predicates = new LinkedList<Predicate>();
//
//                if (idIncarcare > 0) {
//                    predicates.add(criteriaBuilder.isNotNull(root.get("indexRegistru")));
//                }
//
//                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
//            }
//        }, pageRequest);

        RegistruPortalDetails registruPortal1 = registruPortal.findOne(1561L);


        GenericListResult<RegistruPortalDetails> listResult = registruPortal.getListResult(new RepositoryFilter<RegistruPortalDetails>() {

            @Override
            public Predicate[] buildFilter(CriteriaBuilder cb, CriteriaQuery<RegistruPortalDetails> cq, Root<RegistruPortalDetails> from) {
                return new Predicate[]{cb.equal(from.join("incarcare").get("id"), idIncarcare)};
            }
        }, null, null);


        System.out.println(ReflectionToStringBuilder.toString(null, ToStringStyle.MULTI_LINE_STYLE));
    }
}
