package me.kstep.ucalc.units;
import java.util.Arrays;

class Test {
    public static void main(String argv[]) {
        UnitsManager uman = UnitsManager.getInstance();

        // === Масса ===

        // Один грамм — базовая единица (немножно не по СИ, но за то не требует
        // никакой кратности).
        BaseUnit g = new BaseUnit("g");

        // В одном килограмме 1000 граммов.
        LinearUnit kg = new UnitPrefix("k", g);

        // Фунт через килограммы. На самом деле фунтов огромное количество,
        // здесь приведён метрический фунт.
        LinearUnit lb = new LinearUnit("lb", 0.45359237, kg);

        // === Температура ===

        // Градусы Цельсия.
        BaseUnit C = new BaseUnit("°C");

        // Градусы Фаренгейта через градусы Цельсия.
        // На самом деле формула tF = 9/5*tC + 32, так что здесь
        // использован конструктор с обратными величинами,
        // т.к. внутри вычисления идут по принципу "сколько градусов
        // Цельсия в одном градусе Фаренгейта".
        LinearUnit F = new LinearUnit("°F", 9.0/5.0, +32.0, C);
        LinearUnit K = new LinearUnit("°K", C, -273.15);

        // === Время ===

        // Секунда — базовая единица времени в СИ.
        BaseUnit sec = new BaseUnit("sec");

        // В минуте 60 секунд.
        LinearUnit min = new LinearUnit("min", 60, sec);
        // В часе 60 минут.
        LinearUnit hour = new LinearUnit("hour", 60, min);
        // В дне 24 часа.
        LinearUnit day = new LinearUnit("day", 24, hour);
        // В неделе 7 дней.
        LinearUnit week = new LinearUnit("week", 7, day);

        // === Расстояние и длина ===

        // Метры — базовая единица длины по СИ.
        BaseUnit m = new BaseUnit("m");
        // В километре 1000 метров.
        LinearUnit km = new UnitPrefix("k", m);
        // В сантиметре 0.01 метра
        LinearUnit cm = new UnitPrefix("c", m);
        // В миллиметре 0.001 метра
        LinearUnit mm = new UnitPrefix("m", m);

        // Сухопутная Имперская миля.
        LinearUnit mile = new LinearUnit("mile", 1.609344, km);
        // Дюйм
        LinearUnit inch = new LinearUnit("inch", 2.54, cm);
        // Фут
        LinearUnit foot = new LinearUnit("foot", 12, inch);

        // === Скорость ===

        // Обратная секунде величина, частота, герцы.
        PowerUnit Hz = new PowerUnit("Hz", sec, -1);
        PowerUnit per_sec = new PowerUnit(sec, -1);
        PowerUnit per_hour = new PowerUnit(hour, -1);

        // Километры в час.
        MultipleUnit kmh = new MultipleUnit(km, per_hour);
        // Мили в час
        MultipleUnit mph = new MultipleUnit(mile, per_hour);
        // Метры в секунду
        MultipleUnit mps = new MultipleUnit(m, per_sec);
        // Футы в секунду
        MultipleUnit fts = new MultipleUnit(foot, per_sec);

        // === Площадь ===

        // Квадратные метры и километры
        PowerUnit sq_m = new PowerUnit(m, 2);
        PowerUnit sq_km = new PowerUnit(km, 2);

        PowerUnit cu_m = new PowerUnit(m, 3);
        LinearUnit litre = new LinearUnit("l", 0.001, cu_m);


        //sq_m.to(1000, sq_km);
        //sq_km.from(1000, sq_m);

        System.out.println(cm);
        System.out.println(
        uman.convert(35, litre, new PowerUnit(cm, 3))
        //(new PowerUnit(m, 3)).to(200, new PowerUnit(inch, 3))
        //(m.to(200, inch))
        );

        //uman.addAlias("km3", "km³");
        //System.out.println(Arrays.toString(uman.findAliases("Hz")));

        //System.out.println(mile.to(100, m));

        //System.out.println(uman.convert(60, mph, sec));
        //System.out.println(mph.to(100, mps));
        //System.out.println(mps.to(100, kmh));
        //System.out.println(mph.to(10, kmh));

    }
}
