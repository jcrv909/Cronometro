package com.example.cronometro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button start, stop, reset;
    boolean isOn = false;
    TextView crono;
    Thread cronos;
    int mili = 0, seg = 0, minutos = 0;
    Handler h = new Handler();
    ListView listaTiempos;
    ArrayList<String> tiempos = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        reset = (Button) findViewById(R.id.reset);
        crono = (TextView) findViewById(R.id.crono);
        listaTiempos = (ListView) findViewById(R.id.listView);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        reset.setOnClickListener(this);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tiempos);
        listaTiempos.setAdapter(adapter);

        cronos = new Thread(new Runnable() {
            public void run() {

                while (true) {

                    if (isOn) {

                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mili++;

                        if (mili == 999) {
                            seg++;
                            mili = 0;
                        }
                        if (seg == 59) {
                            minutos++;
                            seg = 0;
                        }

                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                String m = "", s = "", mi = "";
                                if (mili < 10) {
                                    m = "00" + mili;
                                } else if (mili < 100) {
                                    m = "0" + mili;
                                } else {
                                    m = "" + mili;
                                }
                                if (seg < 10) {
                                    s = "0" + seg;
                                } else {
                                    s = "" + seg;
                                }
                                if (minutos < 10) {
                                    mi = "0" + minutos;
                                } else {
                                    mi = "" + minutos;
                                }
                                crono.setText(mi + ":" + s + ":" + m);
                            }
                        });
                    }
                }
            }
        });
        cronos.start();
    }

    int i = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                isOn = true;
                break;
            case R.id.stop:
                i++;
                isOn = true;
                String tiempo = crono.getText().toString();
                tiempos.add("Vuelta " + i + ":      " + tiempo);
                adapter.notifyDataSetChanged();
                break;
            case R.id.reset:
                i = 0;
                isOn = false;
                mili = 0;
                seg = 0;
                minutos = 0;
                crono.setText("00:00:000");
                tiempos.clear();
                adapter.notifyDataSetChanged();
                break;
        }
    }
}