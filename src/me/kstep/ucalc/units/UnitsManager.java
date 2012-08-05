package me.kstep.ucalc.units;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

class UnitsManager {
    HashMap<String, Unit> units;
    static UnitsManager instance;

    private UnitsManager() {
        units = new HashMap<String, Unit>();
    }

    public static UnitsManager getInstance() {
        if (instance == null) {
            instance = new UnitsManager();
        }
        return instance;
    }

    public Collection units() {
        return units.values();
    }

    public Set names() {
        return units.keySet();
    }

    public void add(Unit unit) {
        if (units.containsKey(unit.name)) {
            throw new UnitExistsException(unit, units.get(unit.name));
        }

        /*
        for (Unit item: units.values()) {
            if (item.equals(unit)) {
                throw new UnitException("Unit with name `" + unit.toString() + "' is identical to unit `" + item.toString() "'");
            }
        }
        */

        units.put(unit.name, unit);
    }

    public Unit get(String name) {
        Unit unit = units.get(name);
        if (unit == null) {
            throw new UnitNotFoundException(name);
        }
        return unit;
    }

    public boolean exists(String name) {
        return units.containsKey(name);
    }

    public boolean exists(Unit unit) {
        return units.containsValue(unit);
    }

    public Unit[] findAliases(Unit unit) {
        ArrayList<Unit> result = new ArrayList<Unit>();

        for (Unit item: units.values()) {
            if (item.equals(unit)) {
                result.add(item);
            }
        }

        Unit[] a = {};
        return result.toArray(a);
    }

    public Unit[] findAliases(String name) {
        return findAliases(units.get(name));
    }

    public void removeAliases(Unit unit) {
        Unit[] aliases = findAliases(unit);

        for (int i = 0; i < aliases.length - 1; i++) {
            if (aliases[i].name != unit.name)
                units.remove(aliases[i].name);
        }
    }

    public void removeAliases(String name) {
        removeAliases(units.get(name));
    }

    public void addAlias(String alias, Unit unit) {
        if (units.containsKey(alias)) {
            throw new UnitExistsException(alias, unit, units.get(alias));
        }

        units.put(alias, unit);
    }

    public void addAlias(String alias, String unit) {
        addAlias(alias, units.get(unit));
    }

    public double convert(double value, Unit from, Unit to) {
        return from.to(value, to);
    }
    public double convert(double value, String from, Unit to) {
        return convert(value, get(from), to);
    }
    public double convert(double value, Unit from, String to) {
        return convert(value, from, get(to));
    }
    public double convert(double value, String from, String to) {
        return convert(value, get(from), get(to));
    }

    public void clear() {
        units.clear();
    }

    // Experimental
    public void loadUnits(String fileName) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(fileName));

            String line = null;
            while ((line = file.readLine()) != null) {
                // Remove comments and trailing spaces
                line = line.replaceFirst("\\s*#.+$", "");

                // Skip line continuations for now
                if (line.startsWith(" ")) continue;

                line = line.trim();

                // We are not interested in empty lines whatsoever
                if (line.equals("")) continue;

                String[] definition = line.split("\\s+", 2);
                /*try{
                System.out.println(definition[1]);
                System.out.println(definition[1].equals("!"));
                }catch(ArrayIndexOutOfBoundsException e){}*/

                if (definition.length != 2) {
                    continue;

                // Skip prefixes
                } else if (definition[0].endsWith("-")) {
                    continue;

                // Skip functional definitions for now
                } else if (definition[0].endsWith(")")) {
                    continue;

                // Skip table definitions as well
                } else if (definition[0].endsWith("]")) {
                    continue;

                // This is a base unit
                } else if (definition[1].equals("!") || definition[1].equals("!dimensionless")) {
                    try {
                        new BaseUnit(definition[0]);
                    } catch (UnitExistsException e) {}

                // Skip special directives
                } else if (definition[0].startsWith("!")) {
                    continue;

                // Alias
                } else if (exists(definition[1])) {
                    try {
                        addAlias(definition[0], definition[1]);
                    } catch (UnitExistsException e) {}
                }
            }

        } catch (IOException e) {
            return;
        }
    }
}

