package ro.uti.ran.core.repository.criteria;

/**
* Author: vitalie.babalunga@greensoft.com.ro
* Date: 2015-09-29 10:29
*/
public enum Operation {
    /**
     * Equals
     */
    EQ,

    /**
     * Less than
     */
    LT,

    /**
     * Less than or equals to
     */
    LTE,

    /**
     * Greater than
     */
    GT,

    /**
     * Greater than or equals to
     */
    GTE,

    /**
     * Like
     */
    LIKE,

    /**
     * Insensitive like
     */
    ILIKE,

    /**
     * Starts with
     */
    START_WITH,


    /**
     * Ends with
     */
    END_WITH,


    /**
     *
     */
    IN,
    /**
     * isNotNull
      */
    IS_NOT_NULL,
    /**
     * isNull
      */
    IS_NULL
}
