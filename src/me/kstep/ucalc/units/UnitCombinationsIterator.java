package me.kstep.ucalc.units;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

class UnitCombinationsIterator implements Iterator<Unit> {
    public void remove() {
        removeFromAgenda();
        removeFromOrigin();

        if (current.length > 1) {
            nextAgendaItem();
            index = -1;
        }
    }

    private boolean arrayContains(Unit[] array, Unit item) {
        for (Unit c : array) {
            if (c == item) {
                return true;
            }
        }
        return false;
    }

    private void removeFromOrigin() {
        int unitsLeft = origin.length;
        for (int i = 0; i < origin.length; i++) {
            if (arrayContains(current, origin[i])) {
                origin[i] = null;
                unitsLeft--;
            }
        }

        if (origin.length != unitsLeft) {
            Unit[] neworigin = new Unit[unitsLeft];
            int j = 0;
            for (int i = 0; i < origin.length; i++) {
                if (origin[i] != null) {
                    neworigin[j++] = origin[i];
                }
            }
            origin = neworigin;
        }
    }

    private void removeFromAgenda() {
        ListIterator<Unit[]> restAgendaIter = restAgenda.listIterator();
        ListIterator<Unit[]> baseAgendaIter = baseAgenda.listIterator();

        while (restAgendaIter.hasNext() && baseAgendaIter.hasNext()) {
            Unit[] base = baseAgendaIter.next();
            Unit[] rest = restAgendaIter.next();
            boolean remove = false;

            // If base contains one of current set's items, remove this item from agenda
            for (int i = 0; i < base.length - 1; i++) {
                if (arrayContains(current, base[i])) {
                    remove = true;
                    break;
                }
            }

            int unitsLeft = rest.length;

            // Remove all items in current set from agenda's rest
            if (!remove) {
                for (int i = 0; i < rest.length; i++) {
                    if (arrayContains(current, rest[i])) {
                        rest[i] = null;
                        unitsLeft--;
                    }
                }

                // If agenda's rest is empty now, remove whole item from agenda
                remove = unitsLeft == 0;
            }

            if (remove) {
                baseAgendaIter.remove();
                restAgendaIter.remove();

            } else if (unitsLeft != rest.length) {
                // Update agenda's rest
                Unit[] newrest = new Unit[unitsLeft];
                int i = 0;

                for (Unit c : rest) {
                    if (c != null) {
                        newrest[i++] = c;
                    }
                }

                restAgendaIter.set(newrest);
            }
        }
    }

    private Unit[] current;
    private Unit[] rest;
    private int index;

    public Unit next() {
        if (++index == rest.length) {
            if (restAgenda.size() == 0) {
                throw new NoSuchElementException();
            }

            nextAgendaItem();
        }

        current[current.length - 1] = rest[index];
        addAgendaItem();

        return current.length == 1? current[0]: product;
    }

    private void nextAgendaItem() {
        if (restAgenda.size() == 0) {
            return;
        }

        rest = restAgenda.remove(0);
        current = baseAgenda.remove(0);
        product.targetUnits = current;

        index = 0;
    }

    private void addAgendaItem() {
        if (index < rest.length - 1) {
            baseAgenda.add(Arrays.copyOf(current, current.length + 1));
            restAgenda.add(Arrays.copyOfRange(rest, index + 1, rest.length));
        }
    }

    public boolean hasNext() {
        return index < rest.length - 1 || restAgenda.size() > 0;
    }

    private ArrayList<Unit[]> baseAgenda;
    private ArrayList<Unit[]> restAgenda;
    private ProductUnit product;
    private Unit[] origin;
    final private Unit[] singleUnit;

    public Unit[] getOrigin() {
        return origin;
    }

    UnitCombinationsIterator(Unit... units) {
        singleUnit = new Unit[1];
        origin = Arrays.copyOf(units, units.length);
        rest = origin;
        current = singleUnit;
        index = -1;

        baseAgenda = new ArrayList<Unit[]>();
        restAgenda = new ArrayList<Unit[]>();
        product = new ProductUnit("", current);
    }

    public void rewind() {
        baseAgenda.clear();
        restAgenda.clear();

        current = singleUnit;
        product.targetUnits = current;
        rest = origin;
        index = -1;
    }
}

