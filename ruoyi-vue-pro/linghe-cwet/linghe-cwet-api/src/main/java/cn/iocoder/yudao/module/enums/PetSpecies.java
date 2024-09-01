package cn.iocoder.yudao.module.enums;

/**
 * 宠物种类，中英文对比
 *
 * @author hjp
 */
public enum PetSpecies {
    /**
     * 猫
     */
    CAT_1(1, "cat", 1, "英国短毛猫"),
    CAT_2(1, "cat", 2, "美国短毛猫"),
    CAT_3(1, "cat", 3, "布偶猫"),
    CAT_4(1, "cat", 4, "暹罗猫"),
    CAT_5(1, "cat", 5, "苏格兰折耳猫"),
    CAT_6(1, "cat", 6, "狸花猫"),
    CAT_7(1, "cat", 7, "波斯猫"),
    CAT_8(1, "cat", 8, "加菲猫"),
    CAT_9(1, "cat", 9, "缅因猫"),
    CAT_10(1, "cat", 10, "无毛猫"),
    CAT_11(1, "cat", 11, "狮子猫"),
    CAT_12(1, "cat", 12, "金吉拉猫"),
    CAT_13(1, "cat", 13, "中华田园猫"),
    CAT_14(1, "cat", 14, "橘猫"),
    CAT_15(1, "cat", 15, "三花猫"),
    CAT_16(1, "cat", 16, "挪威森林猫"),
    CAT_17(1, "cat", 17, "四川简州猫"),
    CAT_18(1, "cat", 18, "奶牛猫"),
    CAT_19(1, "cat", 19, "土耳其安哥拉猫"),
    CAT_20(1, "cat", 20, "俄罗斯蓝猫"),
    /**
     * 狗
     */
    DOG_1(2, "dog", 1, "边境牧羊犬"),
    DOG_2(2, "dog", 2, "金毛犬"),
    DOG_3(2, "dog", 3, "博美犬"),
    DOG_4(2, "dog", 4, "哈士奇"),
    DOG_5(2, "dog", 5, "比熊犬"),
    DOG_6(2, "dog", 6, "萨摩耶"),
    DOG_7(2, "dog", 7, "松狮"),
    DOG_8(2, "dog", 8, "拉布拉多犬"),
    DOG_9(2, "dog", 9, "阿拉斯加犬"),
    DOG_10(2, "dog", 10, "吉娃娃"),
    DOG_11(2, "dog", 11, "柯基犬"),
    DOG_12(2, "dog", 12, "雪纳瑞"),
    DOG_13(2, "dog", 13, "贵宾犬"),
    DOG_14(2, "dog", 14, "西施犬"),
    DOG_15(2, "dog", 15, "德牧"),
    DOG_16(2, "dog", 16, "法国斗牛犬"),
    DOG_17(2, "dog", 17, "巴哥犬"),
    DOG_18(2, "dog", 18, "柴犬"),
    DOG_19(2, "dog", 19, "藏獒"),
    DOG_20(2, "dog", 20, "泰迪犬");

    /**
     * 对应种类的code码
     */
    public final int SORT_CODE;
    /**
     * 对应种类名称(狗，猫)
     */
    public final String SORT;
    /**
     * 对应品种的code码
     */
    public final int BREED_CODE;
    /**
     * 对应种类的品种(泰迪，藏獒，布偶猫)
     */
    public final String BREED;

    PetSpecies(int sortCode, String sort, int breedCode, String breed) {
        this.SORT_CODE = sortCode;
        this.SORT = sort;
        this.BREED_CODE = breedCode;
        this.BREED = breed;
    }

    public int getSortCode() {
        return SORT_CODE;
    }

    public String getSort() {
        return SORT;
    }

    public int getBreedCode() {
        return BREED_CODE;
    }

    public String getBreed() {
        return BREED;
    }

    /**
     * 根据sortCode 获取种类名称
     */
    public static String getSortFromSortCode(int sortCode) {
        for (PetSpecies petSpecies : PetSpecies.values()) {
            if (petSpecies.getSortCode() == sortCode) {
                return petSpecies.getSort();
            }
        }
        throw new IllegalArgumentException("No enum constant with code " + sortCode);
    }
}
