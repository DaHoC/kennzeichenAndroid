package org.me.androidapplication1;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author DaHoC
 */
public class MainActivity extends Activity {

    private static List<String> BADGES;
    private static kennzeichendb kdb;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        kdb = new kennzeichendb(this.loadDatabase());
        BADGES = kdb.getKeysAsList();
        final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.edit);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, BADGES);
        textView.setAdapter(adapter);

        textView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    String badge = textView.getListSelection(); // ListView.INVALID_POSITION
                    // Perform action on key press, popup text
                    String badge = textView.getText().toString();
                    /// @TODO: Differentiate if found or not
                    String place = kdb.getPlaceOfBadge(badge);
                    String state = kdb.getStateOfBadge(badge);
                    String toShow = (place.equals("") ? "Nicht gefunden!" : place)
                                    + (state.equals("") ? "" : " (" + state + ")");
                    Toast.makeText(MainActivity.this, toShow, Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStop() {
       super.onStop();
    }

    private String loadDatabase() {
        // Reading resources of JAR file
        Context c = this.getBaseContext();
        Resources r = c.getResources();
        try {
            InputStream inStream = r.openRawResource(R.raw.kfzliste);
            if (inStream == null) {
                throw new Error("Failed to load database: Not found!");
            }
            byte[] buf = new byte[15*1000]; // csv file is 14198 Bytes
            if (inStream != null) {
                try {
                    int total = 0;
                    while (true) {
                        int numRead = inStream.read(buf, total, buf.length - total);
                        if (numRead <= 0) {
                            break;
                        }
                        total += numRead;
                    }
                    byte[] bufferWithCorrectLength = new byte[total];
                    System.arraycopy(buf, 0, bufferWithCorrectLength, 0, total);
                    return new String(bufferWithCorrectLength);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Resources.NotFoundException rnf) {
            rnf.printStackTrace();
        }
        return new String();
    }
}
