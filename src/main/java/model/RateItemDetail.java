package model;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RateItemDetail {
    private MatchSpec matchSpec;

    private BigDecimal value;
}
