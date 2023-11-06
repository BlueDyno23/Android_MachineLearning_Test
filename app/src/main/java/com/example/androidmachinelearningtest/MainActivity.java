package com.example.androidmachinelearningtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import weka.core.Instances;
import weka.filters.unsupervised.attribute.Add;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    Button btnPredict;
    RadioGroup radioGroupDataset, radioGroupAlgorithm;
    ListView listView;
    LinearLayout linearLayoutInputs;
    TextView mainTextView, secondTextView;

    ArrayList<String> arrayList;
    ArrayAdapter<String> adapater;

    String selectedFilePath;
    int selectedAlgorithmIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitViews();
        // best test
    }

    private void InitViews() {
        btnPredict = findViewById(R.id.btnPredict);
        btnPredict.setOnClickListener(this);

        radioGroupDataset = findViewById(R.id.radioGroupDataset);
        radioGroupAlgorithm = findViewById(R.id.radioGroupAlgorithm);
        LoadFilesToRadioGroupDatasets();
        radioGroupDataset.setOnCheckedChangeListener(this);
        radioGroupAlgorithm.setOnCheckedChangeListener(this);

        listView = findViewById(R.id.listView);
        linearLayoutInputs = findViewById(R.id.linearLayoutInputs);
        mainTextView = findViewById(R.id.mainTextView);
        secondTextView = findViewById(R.id.secondTextView);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == btnPredict.getId()){
            double[] values = GetValues();
            switch(selectedAlgorithmIndex){
                case 1:
                    // TODO : J48
                    try {
                        mainTextView.setText(WekaMachineLearning.PredictJ48(WekaMachineLearning.LoadData(selectedFilePath),values));
                        Toast.makeText(this, mainTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2:
                    // TODO : Linear Regression
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == radioGroupDataset) {

            RadioButton radioButton = findViewById(i);
            String selectedFileName = radioButton.getText().toString();
            selectedFilePath = "res/raw/" + selectedFileName + ".csv";
            //AddToListView(selectedFilePath);
            try {
                AddInputs();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //runYourDatasetMethod(filePath);
        } else if (radioGroup == radioGroupAlgorithm) {
            selectedAlgorithmIndex = i;
        }
    }

    private void AddInputs() throws Exception {
        linearLayoutInputs.removeAllViews();
        Instances d = WekaMachineLearning.LoadData(selectedFilePath);
        for (int i = 0; i < d.numAttributes()-1; i++) {
            EditText editText = new EditText(this);
            editText.setHint(d.attribute(i).name());

            editText.setWidth(200);
            linearLayoutInputs.addView(editText);
        }
    }

    private double[] GetValues(){
        double[] values = new double[linearLayoutInputs.getChildCount()];
        for (int i = 0; i < linearLayoutInputs.getChildCount(); i++) {
            EditText editText = (EditText) linearLayoutInputs.getChildAt(i);
            values[i] = Double.parseDouble(editText.getText().toString());
        }
        return values;
    }

    private void AddToListView(String filepath) {
        try {
            arrayList = new ArrayList<String>();
            String[][] table = WekaMachineLearning.GetTable(WekaMachineLearning.LoadData(filepath));
            for (int i = 0; i < table.length; i++) {
                String a = "";
                for (int j = 0; j < table[i].length; j++) {
                    a+=table[i][j]+" , ";
                }
                arrayList.add(a);
            }
            adapater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapater);
        }
        catch (Exception e){
            mainTextView.setText(e.getLocalizedMessage());
        }
    }
    private void LoadFilesToRadioGroupDatasets(){
        Field[] fields = R.raw.class.getFields();

        // Create a RadioButton for each .csv file
        for (Field file : fields) {
            String fileName = file.getName();

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(fileName);
            radioGroupDataset.addView(radioButton);

        }
    }
}