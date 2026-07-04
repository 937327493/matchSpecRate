package model;

import lombok.Getter;
import lombok.Setter;

/**
 * 计量因子
 */
@Getter
@Setter
public class CalculateFactor {

    private String factorKey;

    CalculateFactor(String factorKey) {
        this.factorKey = factorKey;
    }
}
