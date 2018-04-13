package com.github.skywaterxxs.common;

/**
 * @author xuxiaoshuo 2018/4/11
 */

import java.io.Serializable;

/**
 * 货币类型
 */
public enum Currency implements Serializable {
    CNY(0, "人民币"), //
    USD(1, "美元"), //
    HKD(2, "港元"), //
    MOP(3, "澳门元"), //
    SGD(4, "新加坡元"), //
    JPY(5, "日元"), //
    EUR(6, "欧元"), //
    GBP(7, "英镑"), //
    RUB(8, "卢布"), //
    TWD(9, "台币"), //
    THB(10, "泰铢"), //
    CAD(11, "加拿大元"), //
    AUD(12, "澳大利亚元"), //
    KER(13, "韩元(KER)"), //
    PHP(14, "菲律宾比索"), //
    MYR(15, "马来西亚林吉特"), //
    IDR(16, "印度尼西亚盾"), //
    INR(17, "印度卢比"), //
    DKK(18, "丹麦克朗"), //
    NOK(19, "挪威克朗"), //
    SEK(20, "瑞典克朗"), //
    CHF(21, "瑞士法郎"), //
    NZD(22, "新西兰元"),//
    AFN(23, "阿富汗尼"),//
    ALL(24, "阿尔巴尼亚列克"),//
    DZD(25, "阿尔及利亚第纳尔"),//
    AOA(26, "安哥拉宽扎"),//
    ARS(27, "阿根廷比索"),//
    AMD(28, "亚美尼亚德拉姆"),//
    AWG(29, "阿鲁巴弗罗林"),//
    AZN(30, "阿塞拜疆马纳特"),//
    BSD(31, "巴哈马元"),//
    BHD(32, "巴林第纳尔"),//
    BDT(33, "孟加拉塔卡"),//
    BBD(34, "巴巴多斯元"),//
    BYR(35, "白俄罗斯卢布"),//
    BZD(36, "伯利兹元"),//
    BMD(37, "百慕大元"),//
    BTN(38, "不丹努尔特鲁姆"),//
    BOB(39, "玻利维亚币"),//
    BAM(40, "波斯尼亚马克"),//
    BWP(41, "博茨瓦纳普拉"),//
    BRL(42, "巴西雷亚尔"),//
    BND(43, "文莱元"),//
    BGN(44, "保加利亚列弗"),//
    BIF(45, "布隆迪法郎"),//
    XOF(46, "多哥非洲共同体法郎"),//
    XAF(47, "刚果中非共同体法郎"),//
    XPF(48, "太平洋法兰西共同体法郎"),//
    KHR(49, "柬埔寨瑞尔"),//
    CVE(50, "埃斯库多"),//
    KYD(51, "开曼群岛元"),//
    CLP(52, "智利比索"),//
    COP(53, "哥伦比亚比索"),//
    KMF(54, "科摩罗法郎"),//
    CDF(55, "刚果法郎"),//
    CRC(56, "哥斯达黎加科朗"),//
    HRK(57, "克罗地亚库纳"),//
    CUC(58, "古巴可兑换比索"),//
    CUP(59, "古巴比索"),//
    CYP(60, "塞浦路斯镑"),//
    CZK(61, "捷克克朗"),//
    DJF(62, "吉布提法郎"),//
    DOP(63, "多米尼加比索"),//
    XCD(64, "东加勒比元"),//
    EGP(65, "埃及镑"),//
    SVC(66, "萨尔瓦多科朗"),//
    EEK(67, "爱沙尼亚伦尼"),//
    ETB(68, "埃塞俄比亚比尔"),//
    FKP(69, "福克兰群岛镑"),//
    FJD(70, "斐济元"),//
    GMD(71, "冈比亚达拉西"),//
    GEL(72, "格鲁吉亚拉里"),//
    GHS(73, "加纳新赛地"),//
    GIP(74, "直布罗陀镑"),//
    XAU(75, "黄金(盎司)"),//
    GTQ(76, "危地马拉查尔"),//
    GNF(77, "几内亚法郎"),//
    GYD(78, "圭亚那元"),//
    HTG(79, "海地古德"),//
    HNL(80, "洪都拉斯皮拉"),//
    HUF(81, "匈牙利福林"),//
    ISK(82, "冰岛克朗"),//
    IRR(83, "伊朗里亚尔"),//
    IQD(84, "伊拉克第纳尔"),//
    ILS(85, "以色列新谢克尔"),//
    JMD(86, "牙买加元"),//
    JOD(87, "约旦第纳尔"),//
    KZT(88, "哈萨克斯坦腾格"),//
    KES(89, "肯尼亚先令"),//
    KWD(90, "科威特第纳尔"),//
    KGS(91, "吉尔吉斯斯坦索姆"),//
    LAK(92, "老挝基普"),//
    LVL(93, "拉脱维亚拉特"),//
    LBP(94, "黎巴嫩镑"),//
    LSL(95, "莱索托洛蒂"),//
    LRD(96, "利比里亚元"),//
    LYD(97, "利比亚第纳尔"),//
    LTL(98, "立陶宛立特"),//
    MKD(99, "马其顿第纳尔"),//
    MGA(100, "马达加斯加阿里亚里"),//
    MWK(101, "马拉维克瓦查"),//
    MVR(102, "马尔代夫拉菲亚"),//
    MTL(103, "马耳他镑"),//
    MRO(104, "毛里塔尼亚乌吉亚"),//
    MUR(105, "毛里求斯卢比"),//
    MXN(106, "墨西哥比索"),//
    MDL(107, "摩尔多瓦列伊"),//
    MNT(108, "蒙古图格里克"),//
    MAD(109, "摩洛哥迪拉姆"),//
    MZN(110, "莫桑比克美提卡"),//
    MMK(111, "缅甸元"),//
    ANG(112, "荷属安的列斯盾"),//
    NAD(113, "纳米比亚元"),//
    NPR(114, "尼泊尔卢比"),//
    NIO(115, "尼加拉瓜科多巴"),//
    NGN(116, "尼日利亚奈拉"),//
    KPW(117, "北韩元"),//
    OMR(118, "阿曼里亚尔"),//
    PKR(119, "巴基斯坦卢比"),//
    PAB(120, "巴拿马巴波亚"),//
    PGK(121, "巴布亚新几内亚基那"),//
    PYG(122, "巴拉圭瓜拉尼"),//
    PEN(123, "秘鲁新索尔"),//
    PLN(124, "波兰兹罗提"),//
    QAR(125, "卡塔尔里亚尔"),//
    RON(126, "罗马尼亚列伊"),//
    RWF(127, "卢旺达法郎"),//
    WST(128, "萨摩亚塔拉"),//
    STD(129, "圣多美普林西比"),//
    SAR(130, "沙特里亚尔"),//
    RSD(131, "塞尔维亚第纳尔"),//
    SCR(132, "塞舌尔卢比"),//
    SLL(133, "塞拉利昂利昂"),//
    XAG(134, "白银（盎司）"),//
    SKK(135, "斯洛伐克克朗"),//
    SIT(136, "斯洛文尼亚拉捷夫"),//
    SBD(137, "所罗门群岛元"),//
    SOS(138, "索马里先令"),//
    ZAR(139, "南非兰特"),//
    LKR(140, "斯里兰卡卢比"),//
    SHP(141, "圣赫勒拿群岛镑"),//
    SDG(142, "苏丹镑"),//
    SRD(143, "苏里南元"),//
    SZL(144, "斯威士兰里兰吉尼"),//
    SYP(145, "叙利亚镑"),//
    TZS(146, "坦桑尼亚先令"),//
    TOP(147, "汤加潘加"),//
    TTD(148, "特立尼达/多巴哥元"),//
    TND(149, "突尼斯第纳尔"),//
    TRY(150, "突尼斯新里拉"),//
    TMM(151, "土库曼斯坦纳特"),//
    UGX(152, "乌干达先令"),//
    UAH(153, "乌克兰赫夫米"),//
    UYU(154, "乌拉圭比索"),//
    AED(155, "阿联酋迪拉姆"),//
    VUV(156, "瓦努阿图瓦图"),//
    VEB(157, "委内瑞拉博利瓦"),//
    VND(158, "越南盾"),//
    YER(159, "也门里亚尔"),//
    ZMK(160, "赞比亚克瓦查"),//
    ZWD(161, "津巴布韦元"),//
    KRW(162, "韩元"),
    HNY(163, "蜂蜜");

    private final int code;

    private final String desc;

    private final String symbol;

    Currency(int code, String desc) {
        this.code = code;
        this.desc = desc;
        this.symbol = takeCurrencySymbol(this.name());
    }

    private static String takeCurrencySymbol(String name) {
        if ("CNY".equals(name)) {
            return "元";
        }
        return name;
    }

    private static boolean jdkCheck(String name) {
        String currencySymbol = "";
        try {
            java.util.Currency jdkCurrency = java.util.Currency.getInstance(name);
            if (jdkCurrency != null) {
                currencySymbol = jdkCurrency.getSymbol();
                return name.equals(currencySymbol);
            }
        }
        catch (Throwable e) {

        }
        return false;
    }

    public static Currency codeOf(int code) {
        for (Currency currencyCode : values()) {
            if (currencyCode.code == code) {
                return currencyCode;
            }
        }
        throw new IllegalArgumentException("Invalid currencyCode code: " + code);
    }

    public static void main(String[] args) {
        for (Currency currency : Currency.values()) {
            System.out.println(currency.name() + ":" + jdkCheck(currency.name()) + ":"
                    + takeCurrencySymbol(currency.name()));
        }
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getSymbol() {
        return symbol;
    }

}