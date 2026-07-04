package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FactorConfig implements Matchable {

    private String code;

    private String name;

    private String config;

    private String operator;


    @Override
    public MatchSpec toMatchSpec() {
        OperatorEnum operatorEnum = OperatorEnum.fromValue(operator);
        if (operatorEnum == null) {
            return null;
        }
        MatchSpec matchSpec;
        switch (operatorEnum) {
            case EQUAL_LITERAL:
            case EQUAL: {
                matchSpec = new SingleValueMatchSpec(code, name, config, RequiredEnum.REQUIRED);
                break;
            }
            case CONTAIN_LITERAL: {
                String[] split = config.split(",");
                List<String> valueList = new ArrayList<>(split.length);
                for (String s : split) {
                    valueList.add(s.trim());
                }
                matchSpec = new MultipleValueMatchSpec(code, name, valueList, RequiredEnum.REQUIRED);
                break;
            }
            case LEFT_OPEN_RIGHT_CLOSE: {
                matchSpec = parseSectionMatchSpec(false, true, RequiredEnum.REQUIRED);
                break;
            }
            case LEFT_CLOSE_RIGHT_OPEN: {
                matchSpec = parseSectionMatchSpec(true, false, RequiredEnum.REQUIRED);
                break;
            }
            case LEFT_CLOSE_RIGHT_CLOSE: {
                matchSpec = parseSectionMatchSpec(true, true, RequiredEnum.REQUIRED);
                break;
            }
            default:
                return null;
        }

        return matchSpec;
    }

    private MatchSpec parseSectionMatchSpec(boolean b, boolean b2, RequiredEnum required) {
        MatchSpec matchSpec;
        if (RequiredEnum.REQUIRED.equals(required)) {
            return null;
        }
        //如果可以为空，且值为空
        if (RequiredEnum.NULLABLE.equals(required) && config == null) {
            //构建特殊的区间匹配规范 重写匹配isSatisfiedBy方法直接返回true
            return billingElement -> true;
        }
        String[] split = config.split(",");
        double start = Double.parseDouble(split[0].trim());
        double end = Double.parseDouble(split[1].trim());
        matchSpec = new SectionValueMatchSpec(code, name, start, end, b, b2);
        return matchSpec;
    }
}
