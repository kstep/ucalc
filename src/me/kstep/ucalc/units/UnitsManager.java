/**
 * Units manager class is the main single registry class, which stores and controls
 * all units defined in program. Every unit created in program is registered into the class
 * instance. Usually you shouldn't even bother instantiating any unit with `new` operator,
 * consider using `UnitsManager.get()` method instead.
 */

package me.kstep.ucalc.units;

import java.lang.reflect.Constructor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

/**
 * We use hashmap to store unit name → instance mapping, array list to represent
 * array of units, collection and set to provide units collection and keys set.
 */
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Also we strive to support unit definitions loading from external files,
 * hence these imports.
 */
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Now to the core class itself.
 */
class UnitsManager {

    HashMap<String, Unit> units;

    /**
     * We implement singleton pattern below by defining private static instance
     * property and locking constructor into private scope.
     */
    private static UnitsManager instance;

    private UnitsManager() {
        units = new HashMap<String, Unit>();
    }

    /**
     * So we leave the only hole to get instance: this `getInstance()` method.
     */
    public static UnitsManager getInstance() {
        if (instance == null) {
            instance = new UnitsManager();
        }
        return instance;
    }

    /**
     * I directly delegate `units()` and `names()` calls to fetch collection of
     * defined units and their names to underlying hashmap storage.
     */
    public Collection units() {
        return units.values();
    }

    public Set names() {
        return units.keySet();
    }

    /**
     * This method adds unit to manager by the name, defined in the unit itself.
     */
    public void add(Unit unit) {
        /**
         * We don't allow units with the same names, as it leads to ambiguity
         * and confusion.
         */
        if (units.containsKey(unit.name)) {
            throw unit.new ExistsException(units.get(unit.name));
        }

        /**
         * Additional check for unit aliases is commented out for now.
         */
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
            throw new Unit.NotFoundException(name);
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

        return result.toArray(new Unit[result.size()]);
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
            throw unit.new ExistsException(alias, units.get(alias));
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

    public static Object[] listToArray(ArrayList<Object> list) {
        return list.toArray(new Object[list.size()]);
    }

    public static boolean isVarArgParameters(Class<?>[] paramTypes) {
        return paramTypes.length > 0 && paramTypes[paramTypes.length - 1].isArray();
    }

    public static Constructor<?> getCompatibleConstructor(Class<?> cls, Class<?>... argTypes) throws NoSuchMethodException {
        try {
            return cls.getDeclaredConstructor(argTypes);
        } catch (NoSuchMethodException e) {
        }

        Constructor<?>[] ctors = cls.getDeclaredConstructors();
        int argsNum = argTypes == null? 0: argTypes.length;

        SEARCH:
        for (Constructor<?> ctor: ctors) {
            Class<?>[] paramTypes = ctor.getParameterTypes();
            int paramsNum = paramTypes.length;
            boolean isVarArg = isVarArgParameters(paramTypes);

            if (argsNum != paramsNum && (!isVarArg || argsNum < paramsNum - 1))
                continue;

            Class lastParam = paramTypes[paramsNum - 1].getComponentType();

            for (int i = 0; i < argsNum; i++) {
                Class<?> param, arg;

                if (!isVarArg) {
                    param = paramTypes[i];
                    arg = argTypes[i];

                } else {
                    param = i >= paramsNum - 1? lastParam: paramTypes[i];
                    arg = argTypes[i];
                    if (argsNum == paramsNum && i == paramsNum - 1 && arg.isArray())
                        arg = arg.getComponentType();
                }

                if (param.isPrimitive()) {
                    param = param.equals(Double.TYPE)? Number.class:
                            param.equals(Float.TYPE)? Number.class:
                            param.equals(Integer.TYPE)? Number.class:
                            param.equals(Long.TYPE)? Number.class:
                            param.equals(Short.TYPE)? Number.class:
                            param.equals(Byte.TYPE)? Number.class:
                            param.equals(Boolean.TYPE)? Boolean.class:
                            param.equals(Character.TYPE)? Character.class:
                            null;
                }

                if (param == null || !param.isAssignableFrom(arg)) {
                    continue SEARCH;
                }
            }

            return ctor;
        }

        throw new NoSuchMethodException(cls.getName() + ".<init>(" + argTypes.toString() + ")");
    }



    /**
     * This is a "silver bullet" method to get/define units.
     * Use it instead of direct instantiation of any unit.
     * The call is:
     *
     *     Unit unita = uman.get(LinearUnit.class, 'unita', 100.0, unitb);
     * 
     * It is equivalent to:
     *
     *     LinearUnit unita = new LinearUnit('unita', 100.0, unitb);
     *
     * if `unita` doesn't exists, and is equivalent to:
     *
     *     Unit unita = uman.get('unita');
     *
     * otherwise. Also note, if `unita` already exists, its direct instantiation
     * will raise `UnitExistsException`.
     */
    public Unit get(Class<? extends Unit> cls, String name, Object... args) {
        if (units.containsKey(name)) {
            return units.get(name);
        }

        try {
            ArrayList<Class> argTypes = new ArrayList<Class>();
            ArrayList<Object> argValues = new ArrayList<Object>();

            argTypes.add(String.class);
            argValues.add(name);
            for (Object arg: args) {
                argTypes.add(arg.getClass());
                argValues.add(arg);
            }

            Constructor ctor = getCompatibleConstructor(cls, argTypes.toArray(new Class[argTypes.size()]));
            Class<?>[] paramTypes = ctor.getParameterTypes();

            if (isVarArgParameters(paramTypes)) {
                Class<?> varArgType = paramTypes[paramTypes.length - 1];
                ArrayList<?> varArgValues = new ArrayList<Object>(argValues.subList(paramTypes.length - 1, args.length + 1));

                argValues.subList(paramTypes.length - 1, args.length + 1).clear();
                argValues.add(varArgValues.toArray(new Unit[varArgValues.size()]));
            }

            return cls.cast(ctor.newInstance(listToArray(argValues)));

        } catch (NoSuchMethodException e) {
            System.out.println(e);
        } catch (InstantiationException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        } catch (InvocationTargetException e) {
            System.out.println(e);
        } catch (Unit.ExistsException e) {
            return e.getExistingUnit();
        }

        throw new Unit.NotFoundException(name);
    }

}

