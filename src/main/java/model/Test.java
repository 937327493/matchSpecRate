package model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Test {
    void test() {
        List<FactorConfig> factorConfigList = new ArrayList<>();
        factorConfigList.add(new FactorConfig());

        RateItemDetailConfig rateItemDetailConfig = new RateItemDetailConfig();
        rateItemDetailConfig.setFactorConfigList(factorConfigList);

        List<RateItemDetailConfig> rateItemDetailConfigList = new ArrayList<>();
        rateItemDetailConfigList.add(rateItemDetailConfig);

        RateItemConfig rateItemConfig = new RateItemConfig();
        rateItemConfig.setRateItemDetailConfigList(rateItemDetailConfigList);

        RateItem rateItem = parseRateItem(rateItemConfig);

        rateItem.calculate(null);


    }

    private RateItem parseRateItem(RateItemConfig config) {

        RateFormula rateFormula = FormulaEnum.getFormula(config.getFormulaCode(), config.getFactorKey());
        List<RateItemDetailConfig> detailConfigList = config.getRateItemDetailConfigList();
        List<RateItemDetail> detailList = new ArrayList<>();
        for(RateItemDetailConfig detailConfig: detailConfigList) {
            detailList.add(parseRateItemDetail(detailConfig));
        }
        return new RateItem(rateFormula, detailList);
    }

    private RateItemDetail parseRateItemDetail(RateItemDetailConfig detailConfig) {
        List<FactorConfig> factorConfigList = detailConfig.getFactorConfigList();
        List<MatchSpec> matchSpecs = new ArrayList<>();
        for(FactorConfig factorConfig: factorConfigList) {
            matchSpecs.add(factorConfig.toMatchSpec());
        }
        MatchSpec[] array = matchSpecs.toArray(new MatchSpec[0]);
        MatchSpec matchSpec = MatchSpec.unionAnd(array);
        return new RateItemDetail(matchSpec, detailConfig.getValue());
    }



    public void queryEnumInfo(String code) {
        // 后期需要的话可以把枚举放到diamond读取
        try {
            String classname = "";
            Class aClass = Class.forName(classname);
            Object [] values = aClass.getEnumConstants();


            for(Object valueObject : values){
                String desc = (String) getEnumValueByMethod(valueObject, aClass, "getDesc");
                String name = (String) getEnumValueByMethod(valueObject, aClass, "getCode");
            }

        } catch (Exception e) {
        }
    }

    private Object getEnumValueByMethod(Object valueObject,Class clazz,String methodName) throws Exception {
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            return method.invoke(valueObject);
        } catch (NoSuchMethodException e) {
            if("name".equals(methodName)){
                return valueObject.toString();
            } else {
                throw e;
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
