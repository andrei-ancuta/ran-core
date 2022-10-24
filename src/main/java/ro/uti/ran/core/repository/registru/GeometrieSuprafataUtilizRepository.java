package ro.uti.ran.core.repository.registru;

import ro.uti.ran.core.model.registru.GeometrieSuprafataUtiliz;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

public interface GeometrieSuprafataUtilizRepository extends RanRepository<GeometrieSuprafataUtiliz, Long> {

    List<GeometrieSuprafataUtiliz> findBySuprafataUtilizareIdSuprafataUtilizare(Long idSuprafataUtilizare);

}