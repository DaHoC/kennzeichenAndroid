/**
 * @author gawe design
 */
package org.me.androidapplication1;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class kennzeichendb {

    // Separator strings for CSV
    private static final String lineSeparator = "\n";
    private static final String itemSeparator = ";";
    private Hashtable places = new Hashtable();
    private Hashtable states = new Hashtable();
    private Hashtable discontinued = new Hashtable();
    private List<String> keys = new ArrayList<String>();

    // Constructor, takes the string read from the file and constructs an array out of it
    public kennzeichendb(String kennzeichenString) {
        // First split per line (=per row)
        int index = 0;
        int lastIndex = 0;
        /// @WARNING: autocompletion does not work with even one null-entry!!!
        while ((index = kennzeichenString.indexOf(lineSeparator, lastIndex)) >= 0) {
            this.extractItemsFromLine(kennzeichenString.substring(lastIndex, index));
            lastIndex = index + lineSeparator.length();
        }
        // Add the last line
        this.extractItemsFromLine(kennzeichenString.substring(lastIndex, kennzeichenString.length()));
    }

    // The CSV String has the form:
    // A   ;Augsburg;(Bay)
    private void extractItemsFromLine(String line) {
        int firstIndex = line.indexOf(itemSeparator);
        String index = line.substring(0, firstIndex).trim().toLowerCase();
        String place;
        String state = "";
        int secondIndex = line.indexOf(itemSeparator, firstIndex + 1);
        if (secondIndex > 0) {
            place = line.substring(firstIndex + 1, secondIndex).trim();
            int thirdIndex = line.indexOf(itemSeparator, secondIndex + 1);
            if (thirdIndex > 0)
                state = line.substring(secondIndex + 1, thirdIndex).trim();
            else
                state = line.substring(secondIndex + 1, line.length()).trim();
                /// @TODO: discontinued = yes if fourth index exists
        } else { // Special badges or no state given
            place = line.substring(firstIndex + 1, line.length()).trim();
        }
        this.keys.add(index);
        places.put(index, place);
        states.put(index, state);
    }

    // Retrieves the place from the given the badge
    public String getPlaceOfBadge(String badge) {
        badge = badge.trim().toLowerCase();
        return (this.places.containsKey(badge) ? this.places.get(badge).toString() : "");
    }

    // Retrieves the state from the given the badge
    public String getStateOfBadge(String badge) {
        badge = badge.trim().toLowerCase();
        return (this.states.containsKey(badge) ? this.states.get(badge).toString() : "");
    }

    // Returns the keys = badges
    public List<String> getKeysAsList() {
        return this.keys;
    }
}
