package ro.uti.ran.core.repository.registru.view;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.view.ViewUatGospodarie;
import ro.uti.ran.core.model.registru.view.ViewUatGospodariePK;

/**
 * Created by mihai.plavichianu
 */
public interface ViewUatGospodarieRepository extends JpaRepository<ViewUatGospodarie, ViewUatGospodariePK> {

	@Query(nativeQuery=true, value="SELECT ID_NOM_UAT, ID_NOM_JUDET, ?1 AS AN, NVL(SUM(TOTAL_UAT_GOSPODARIE), 0) AS TOTAL_UAT_GOSPODARIE, SUM(TOTAL_UAT_DECLARATIE) AS TOTAL_UAT_DECLARATIE FROM A_VW_UAT_GOSPODARIE WHERE AN = ?1 GROUP BY ID_NOM_JUDET,ID_NOM_UAT")
	public List<ViewUatGospodarie> findByJudet(Integer year);

	@Query(nativeQuery = true, value = "SELECT null AS ID_NOM_UAT, ID_NOM_JUDET, ?1 AS AN, NVL (SUM (TOTAL_UAT_GOSPODARIE), 0) AS TOTAL_UAT_GOSPODARIE, SUM (TOTAL_UAT_DECLARATIE) AS TOTAL_UAT_DECLARATIE FROM A_VW_UAT_GOSPODARIE WHERE AN = ?1 GROUP BY ID_NOM_JUDET")
	public List<ViewUatGospodarie> findTotalByJudet(Integer year);
	
	@Override
	@Query(nativeQuery=true, value="SELECT DISTINCT ID_NOM_JUDET, ID_NOM_UAT, 2016 AS AN, 0 AS TOTAL_UAT_GOSPODARIE, 0 AS TOTAL_UAT_DECLARATIE FROM VW_UAT_GOSPODARIE")
	public List<ViewUatGospodarie> findAll();

	@Query("SELECT a FROM ViewUatGospodarie a where a.uat = :idNomUat AND a.an = :an")
	ViewUatGospodarie findByUatAndAn(@Param("idNomUat") Long idNomUat, @Param("an") Integer an);

	@Query(nativeQuery = true,value = " SELECT COUNT(*) FROM   nom_uat nu INNER JOIN inventar_gosp_uat igu ON nu.id_nom_uat = igu.fk_nom_uat " +
            " where fk_nom_judet = ?2 and igu.an = ?1 " +
            " GROUP BY nu.fk_nom_judet, igu.an ")
    Long totalUatCareADeclaratInventarAnual(Integer year,Long idJudet);



    @Query(value = "" +
            " SELECT COUNT(*) " +
            "FROM nom_uat nu " +
            "INNER JOIN nom_judet nj " +
            "ON nu.fk_nom_judet = nj.ID_NOM_JUDET\n" +
            "WHERE  ((nu.data_stop  IS NULL)  OR (nu.DATA_STOP <= TRUNC(to_date(?1,'dd-mm-yyyy')))) " +
            "AND (nj.data_stop IS NULL or nj.DATA_STOP <= TRUNC(to_date(?1,'dd-mm-yyyy'))) " +
            "AND id_nom_judet        = ?2 " +
            "GROUP BY nj.id_nom_judet  ",

            nativeQuery = true)
    Long totalUatDinJudet(String dataStopStr,Long idNomJudet);

	@Query(nativeQuery=true, value="SELECT AN, ID_NOM_UAT, ID_NOM_JUDET, TOTAL_UAT_DECLARATIE, TOTAL_UAT_GOSPODARIE  FROM A_VW_UAT_GOSPODARIE WHERE (ID_NOM_JUDET = ?1 AND AN = ?2)")
	List<ViewUatGospodarie> findByJudetAndAn( Long idJudet, int year);
}
