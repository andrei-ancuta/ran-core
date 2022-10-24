package ro.uti.ran.core.service.index;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-08-31
 */
public interface IndexGenerator {

    /**
     * @return urmatorul index
     */
    String getNextIndex(Index index);
}
