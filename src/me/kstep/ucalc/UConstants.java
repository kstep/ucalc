package me.kstep.ucalc;

import java.util.Hashtable;

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
        put("π", "Ratio of a circle's circumference to its diameter", Math.PI);
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
}
