package com.example.androidmachinelearningtest;

import java.io.File;
import java.io.IOException;

import weka.core.Instance;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.converters.CSVLoader;
import weka.core.matrix.Matrix;
import weka.filters.unsupervised.attribute.Remove;

public class WekaMachineLearning {

    // TODO : Research more built in algorithms, add some manually if necessary like KNN, SVM, Naive Bayes, Random Forest
    // TODO : Apparently it does not have automatic ONE HOT ENCODING but it has something similiar that is done automatically to text values so just add OHE method
    public static Instances LoadData(String path, int classIndex) throws IOException {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(path));
        Instances instances = loader.getDataSet();
        instances.setClassIndex(classIndex);
        return instances;
    }
    public static Instances LoadData(String path) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(path));
        Instances instances = loader.getDataSet();
        instances.setClassIndex(instances.numAttributes()-1);

        instances = RemoveAttributes(instances, new int[]{0});

        return instances;
    }

    public static Instances RemoveAttributes(Instances data, int[] indexes) throws Exception {
        Remove remove = new Remove();
        remove.setAttributeIndicesArray(indexes);
        remove.setInputFormat(data);
        return weka.filters.Filter.useFilter(data, remove);
    }

    public static String[][] GetTable(Instances data) {
        String[][] table = new String[data.numInstances()] [data.numAttributes()];
        for (int i = 0; i < data.numInstances(); i++) {
            for (int j = 0; j < data.numAttributes(); j++) {
                table[i][j] = Double.toString(data.instance(i).value(j));
            }
        }
        return table;
    }

    public static double PredictLinearRegression(Instances data, double[] values) throws Exception {
        Instance instance = data.firstInstance();
        instance.setDataset(data);

        LinearRegression model = new LinearRegression();

        if(values.length==data.numAttributes()-1){
            for (int i=0; i<data.numAttributes()-1;i++)
            {
                instance.setValue(i,values[i]);
            }
        }
        else{
            return -80085;
        }

        double prediction = model.classifyInstance(instance);
        return prediction;
    }

    public static String PredictJ48(Instances data, double[] values) throws Exception {
        Instance instance = data.firstInstance();
        instance.setDataset(data);

        Classifier classifier = new J48();
        classifier.buildClassifier(data);

        if(values.length==data.numAttributes()-1){
            for (int i=0; i<data.numAttributes()-1;i++)
            {
                instance.setValue(i,values[i]);
            }
        }
        else{
            return "Input count != Attribute count";
        }

        double predictedClass = classifier.classifyInstance(instance);
        String predictedClassLabel = data.classAttribute().value((int) predictedClass);

        return predictedClassLabel;
    }
}
