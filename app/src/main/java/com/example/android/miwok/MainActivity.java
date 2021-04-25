package com.example.android.miwok;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        //To open numbers list in a new activity
        TextView numbers = (TextView) findViewById(R.id.numbers);
        assert numbers != null;
        numbers.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent numbersIntent = new Intent(MainActivity.this, NumbersActivity.class);
                startActivity(numbersIntent);
            }
        });

        //To open Family list in a new activity
        TextView family = (TextView) findViewById(R.id.family);
        assert family != null;
        family.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent familyIntent = new Intent(MainActivity.this, FamilyActivity.class);
                startActivity(familyIntent);
            }
        });

        //To open Phrase list in a new activity
        TextView phrases = (TextView) findViewById(R.id.phrases);
        assert phrases != null;
        phrases.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent phrasesIntent = new Intent(MainActivity.this, PhrasesActivity.class);
                startActivity(phrasesIntent);
            }
        });

        //To open Colors list in a new activity
        TextView colors = (TextView) findViewById(R.id.colors);
        assert colors != null;
        colors.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent colorsIntent = new Intent(MainActivity.this, ColorsActivity.class);
                startActivity(colorsIntent);
            }
        });
    }
}
