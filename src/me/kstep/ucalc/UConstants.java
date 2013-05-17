package me.kstep.ucalc;

import java.util.Hashtable;

import me.kstep.ucalc.numbers.UUnitNum;
import me.kstep.ucalc.units.UnitsManager;
import me.kstep.ucalc.units.Unit;

final class UConstants extends Hashtable<CharSequence,UConstants.Item> {

    class Item {
        final public String name;
        final public String description;
        final public Number value;

        public Item(String name, Number value) {
            this(name, name, value);
        }

        public Item(String name, String description, Number value) {
            this.name = name;
            this.description = description;
            this.value = value;
        }

        public String toString() {
            return name;
        }
    }

    private static final long serialVersionUID = 0L;

    public UConstants() {
        UnitsManager u = UnitsManager.getInstance();

        put("π", "Ratio of a circle's circumference to its diameter", Math.PI);
        put("e", "Base of natural logarithm", Math.E);

        put("G", "Gravitational constant", 6.6738480e-11, u.get("N").mul(u.get("m").pow(2)).mul(u.get("kg").pow(-2)));
    }

    public void put(Item item) {
        put(item.name, item);
    }

    public void put(String name, Number value) {
        put(new Item(name, value));
    }

    public void put(String name, String description, Number value) {
        put(new Item(name, description, value));
    }

    public void put(String name, String description, Number value, Unit unit) {
        put(name, description, new UUnitNum(value, unit));
    }

    public void put(String name, Number value, Unit unit) {
        put(name, new UUnitNum(value, unit));
    }
}
