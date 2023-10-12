package fi.arcada.projekt_chi2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Deklarera 4 Button-objekt
    Button btn1, btn2, btn3, btn4, btnReset;
    // Deklarera 4 heltalsvariabler för knapparnas värden
    int val1, val2, val3, val4;


    TextView output, startCount, twColumn1, twColumn2, twRow1, twRow2, twStatement,
    twPercent1, twPercent2, msgOutput;



    //Någå
    SharedPreferences sharedPref;
    SharedPreferences.Editor prefEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Koppla samman Button-objekten med knapparna i layouten
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btnReset = findViewById(R.id.btnReset);
        output = findViewById(R.id.output);
        startCount = findViewById(R.id.startCount);
        twColumn1 = findViewById(R.id.twColumn1);
        twColumn2 = findViewById(R.id.twColumn2);
        twRow1 = findViewById(R.id.twRow1);
        twRow2 = findViewById(R.id.twRow2);
        twStatement = findViewById(R.id.twStatement);
        twPercent1 = findViewById(R.id.twPercent1);
        twPercent2 = findViewById(R.id.twPercent2);
        msgOutput = findViewById(R.id.msgOutput);



        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = sharedPref.edit();

        int appStart = sharedPref.getInt("count",0);
        appStart++;
        prefEditor.putInt("count", appStart);
        prefEditor.apply();

        startCount.setText(String.format("You've opened the app %d time(s)",
                appStart
        ));

        twColumn1.setText(sharedPref.getString("column_1", "Column 1"));
        twColumn2.setText(sharedPref.getString("column_2", "Column 2"));
        twRow1.setText(sharedPref.getString("row_1", "Row 1"));
        twRow2.setText(sharedPref.getString("row_2", "Row 2"));
        twStatement.setText(sharedPref.getString("row_1", "Statement"));


        val1 = sharedPref.getInt("val1",0);
        val2 = sharedPref.getInt("val2",0);
        val3 = sharedPref.getInt("val3",0);
        val4 = sharedPref.getInt("val4",0);

        btn1.setText(String.valueOf(sharedPref.getInt("val1",0)));
        btn2.setText(String.valueOf(sharedPref.getInt("val2",0)));
        btn3.setText(String.valueOf(sharedPref.getInt("val3",0)));
        btn4.setText(String.valueOf(sharedPref.getInt("val4",0)));




    }

    public void openSettings(View view) {

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     *  Klickhanterare för knapparna
     */
    public void buttonClick(View view) {

        // Skapa ett Button-objekt genom att type-casta (byta datatyp)
        // på det View-objekt som kommer med knapptrycket
        Button btn = (Button) view;

        // Kontrollera vilken knapp som klickats, öka värde på rätt vaiabel
        if (view.getId() == R.id.button1) {
            val1++;
            prefEditor.putInt("val1",val1);

        }

        if (view.getId() == R.id.button2) {
            val2++;
            prefEditor.putInt("val2",val2);
        }

        if (view.getId() == R.id.button3) {
            val3++;
            prefEditor.putInt("val3",val3);
        }

        if (view.getId() == R.id.button4) {
            val4++;
            prefEditor.putInt("val4",val4);
        }

        prefEditor.apply();



        // Slutligen, kör metoden som ska räkna ut allt!
        calculate();
    }

    /**
     * Metod som uppdaterar layouten och räknar ut själva analysen.
     */
    public void calculate() {

        // Uppdatera knapparna med de nuvarande värdena
        btn1.setText(String.valueOf(val1));
        btn2.setText(String.valueOf(val2));
        btn3.setText(String.valueOf(val3));
        btn4.setText(String.valueOf(val4));



        // Mata in värdena i Chi-2-uträkningen och ta emot resultatet
        // i en Double-variabel
        double chi2 = Significance.chiSquared(val1, val2, val3, val4);

        // Mata in chi2-resultatet i getP() och ta emot p-värdet
        double pValue = Significance.getP(chi2);




        double percentage1 = Significance.getPercent1(val1, val3);
        double percentage2 = Significance.getPercent2(val2, val4);

        System.out.println(percentage1);
        System.out.println(percentage2);

        twPercent1.setText(sharedPref.getString("column_1", "Column 1") + " " + percentage1 + "%");
        twPercent2.setText(sharedPref.getString("column_2", "Column 2") + " " + percentage2 + "%");

        String significanceLevel = sharedPref.getString("valueList","Error");

        output.setText(String.format("Chi2 result: %.2f\nSignificance level: %s\nP-value: %.2f",
                chi2,
                significanceLevel,
               pValue
                ));


        if (pValue <= 0.99) {
            msgOutput.setText(String.format("The result is 100%% significant" ));
        }

        if (pValue <= 0.2) {
            msgOutput.setText(String.format("If p-value = 0.2, there is a 20%% chance that the null hypothesis is correct (significant)"));
        }

        if (pValue <= 0.1) {
            msgOutput.setText(String.format("If p-value = 0.1, there is a 10%% chance that the null hypothesis is correct (significant)"));
        }

        if (pValue <= 0.05) {

            msgOutput.setText(String.format("A statistically significant test result (P ≤ 0.05) means that the test hypothesis is false or should be rejected. A P value greater than 0.05 means that no effect was observed." ));

        }

        if (pValue <= 0.025) {
            msgOutput.setText(String.format("If p-value = 0.025, there is only 2.5%% chance that the null hypothesis is correct (not significant)"));
        }

        if (pValue <= 0.02) {
            msgOutput.setText(String.format("If p-value = 0.02, there is only a 2%% chance that the null hypothesis is correct (not significant)"));
        }

        if (pValue <= 0.01) {
            msgOutput.setText(String.format("A P-value of 0.01 infers, assuming the postulated null hypothesis is correct, any difference seen (or an even bigger “more extreme” difference) in the observed results would occur 1 in 100 (or 1%%) of the times a study was repeated."));
        }

        if (pValue <= 0.005) {
            msgOutput.setText(String.format("If p-value = 0.005, there is only a 0.5%% chance that the null hypothesis is correct (not significant)"));
        }

        if (pValue <= 0.002) {
            msgOutput.setText(String.format("If p-value = 0.002, there is a 0.2%% chance that the null hypothesis is correct (not significant)"));
        }


    }



    public void ResetBtn(View view) {

        val1 = 0;
        val2 = 0;
        val3 = 0;
        val4 = 0;

        prefEditor.putInt("val1",val1);
        prefEditor.putInt("val2",val2);
        prefEditor.putInt("val3",val3);
        prefEditor.putInt("val4",val4);
        prefEditor.apply();

        btn1.setText(String.valueOf(val1));
        btn2.setText(String.valueOf(val2));
        btn3.setText(String.valueOf(val3));
        btn4.setText(String.valueOf(val4));
        output.setText("");
        msgOutput.setText("");

    }


}