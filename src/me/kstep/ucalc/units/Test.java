package me.kstep.ucalc.units;
import java.util.Arrays;

import me.kstep.ucalc.numbers.UUnitNum;

class Test {
    public static void main(String argv[]) {
        UnitsManager uman = UnitsManager.getInstance();

        // === Масса ===

        // Один грамм — базовая единица (немножно не по СИ, но за то не требует
        // никакой кратности).
        Unit g = uman.get(BaseUnit.class, "g");

        // В одном килограмме 1000 граммов.
        Unit kg = uman.get(UnitPrefix.class, "kg", "k", g);

        // Фунт через килограммы. На самом деле фунтов огромное количество,
        // здесь приведён метрический фунт.
        Unit lb = uman.get(LinearUnit.class, "lb", 0.45359237, kg);

        // === Температура ===

        // Градусы Цельсия.
        Unit C = uman.get(BaseUnit.class, "°C");

        // Градусы Фаренгейта через градусы Цельсия.
        // На самом деле формула tF = 9/5*tC + 32, так что здесь
        // использован конструктор с обратными величинами,
        // т.к. внутри вычисления идут по принципу "сколько градусов
        // Цельсия в одном градусе Фаренгейта".
        Unit F = uman.get(LinearUnit.class, "°F", 9.0/5.0, +32.0, C);
        Unit K = uman.get(LinearUnit.class, "°K", C, -273.15);

        // === Время ===

        // Секунда — базовая единица времени в СИ.
        Unit sec = uman.get(BaseUnit.class, "sec");

        // В минуте 60 секунд.
        Unit min = uman.get(LinearUnit.class, "min", 60, sec);
        // В часе 60 минут.
        Unit hour = uman.get(LinearUnit.class, "hour", 60, min);
        // В дне 24 часа.
        Unit day = uman.get(LinearUnit.class, "day", 24, hour);
        // В неделе 7 дней.
        Unit week = uman.get(LinearUnit.class, "week", 7, day);

        // === Расстояние и длина ===

        // Метры — базовая единица длины по СИ.
        Unit m = uman.get(BaseUnit.class, "m");
        // В километре 1000 метров.
        Unit km = uman.get(UnitPrefix.class, "km", "k", m);
        // В сантиметре 0.01 метра
        Unit cm = uman.get(UnitPrefix.class, "cm", "c", m);
        // В миллиметре 0.001 метра
        Unit mm = uman.get(UnitPrefix.class, "mm", "m", m);

        // Сухопутная Имперская миля.
        Unit mile = uman.get(LinearUnit.class, "mile", 1.609344, km);
        // Дюйм
        Unit inch = uman.get(LinearUnit.class, "inch", 2.54, cm);
        // Фут
        Unit foot = uman.get(LinearUnit.class, "foot", 12, inch);

        // === Скорость ===

        // Обратная секунде величина, частота, герцы.
        Unit Hz = uman.get(PowerUnit.class, "Hz", sec, -1);
        Unit per_sec = uman.get(PowerUnit.class, "sec¯¹", sec, -1);
        Unit per_hour = uman.get(PowerUnit.class, "hour¯¹", hour, -1);

        // Километры в час.
        Unit kmh = uman.get(MultipleUnit.class, "km/h", km, per_hour);
        // Мили в час
        Unit mph = uman.get(MultipleUnit.class, "miles/h", mile, per_hour);
        // Метры в секунду
        Unit mps = uman.get(MultipleUnit.class, "m/s", m, per_sec);
        // Футы в секунду
        Unit fts = uman.get(MultipleUnit.class, "ft/s", foot, per_sec);

        // === Площадь ===

        // Квадратные метры и километры
        Unit sq_m = uman.get(PowerUnit.class, "m²", m, 2);
        Unit sq_km = uman.get(PowerUnit.class, "km²", km, 2);

        Unit cu_m = uman.get(PowerUnit.class, "m³", m, 3);
        Unit cu_dm = uman.get(PowerUnit.class, "dm³", uman.get(UnitPrefix.class, "dm", "d", m), 3);
        Unit litre = uman.get(LinearUnit.class, "l", 0.001, cu_m);



        //println(sq_m.to(1000, sq_km));
        Unit x = new BaseUnit("x");

        Unit t;
        
        //t = new PowerUnit(new PowerUnit(x, 2), 3);
        //println(t);
        //println(t.simplify());

        t = new UnitPrefix("k", new LinearUnit("yy", 6, new LinearUnit("y", 10, x, 5), 9));
        println(((LinearUnit) t).represent());
        println(((LinearUnit) t.simplify()).represent());

        t = new MultipleUnit(x, x, x);
        println(t.represent());
        println(t.simplify().represent());

        //println(new UUnitNum(1000, sq_m).convert(cu_m));
        //sq_km.from(1000, sq_m);

        //println(cm);
        //println(
        //uman.convert(35, litre, "dm³")
        //(new PowerUnit(m, 3)).to(200, new PowerUnit(inch, 3))
        //(m.to(200, inch))
        //);

        //uman.addAlias("km3", "km³");
        //println(Arrays.toString(uman.findAliases("Hz")));

        //println(mile.to(100, m));

        //println(uman.convert(60, mph, sec));
        //println(mph.to(100, mps));
        //println(mps.to(100, kmh));
        //println(mph.to(10, kmh));
    }

    private static void println(Object o) {
        System.out.println(o.toString());
    }
}
