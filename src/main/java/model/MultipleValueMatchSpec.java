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
        // 修复：value 可能为 null，原 value.isEmpty() 会 NPE
        if ((value == null || value.isEmpty()) && RequiredEnum.NULLABLE == requiredEnum) {
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
