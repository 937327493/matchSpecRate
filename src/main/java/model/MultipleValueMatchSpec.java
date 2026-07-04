package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MultipleValueMatchSpec implements MatchSpec {

    private String code;

    private String name;

    private List<String> value;

    private RequiredEnum requiredEnum;

    @Override
    public boolean isSatisfiedBy(MatchableResource rateableResource) {
        //如果值为空，并且允许配置为空，则永远返回true
        if(value.isEmpty() && RequiredEnum.NULLABLE == requiredEnum) {
            return true;
        }
        String stringValue = rateableResource.fetchString(code);
        //只要有值匹配，就表示满足规范
        for(String value: value) {
            if(value.equals(stringValue)) {
                return true;
            }
        }
        return false;
    }
}
