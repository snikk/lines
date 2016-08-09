package com.intuit.linesexperiment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.intuit.linesexperiment.graph.GraphActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String[] NAMES= new String[] {"Line Pattern", "Spirograph", "Graph"};
    public static final Class<?>[] CLASSES = new Class[] {LinePatternActivity.class, SpirographActivity.class, GraphActivity.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, NAMES);

        ListView lv = (ListView) findViewById(R.id.items);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getApplicationContext(), CLASSES[position]);
        startActivity(i);
    }
}
