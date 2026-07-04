package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 被匹配的资源
 */
public interface MatchableResource {
    String fetchBizCode(String key);

    String fetchStringList(String key);

    BigDecimal fetchBigDecimal(String key);

    LocalDateTime fetchLocalDateTime(String key);

    Double fetchDouble(String key);


    String fetchString(String key);


    Boolean fetchBoolean(String key);

}
