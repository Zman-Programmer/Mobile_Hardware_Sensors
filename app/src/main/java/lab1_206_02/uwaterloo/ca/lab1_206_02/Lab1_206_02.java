package lab1_206_02.uwaterloo.ca.lab1_206_02;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.FloatRange;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import lab1_206_02.uwaterloo.ca.lab1_206_02.ca.uwaterloo.sensortoy.LineGraphView;


//main class where all the activities are called
public class Lab1_206_02 extends AppCompatActivity {

//declare all variables
    //declare a textview for the light sensor
    TextView lightSensorText;
    //declare the textview for the highest light sensor reading
    TextView highestLightSensor;
    //declare the textview for the accelerometer reading
    TextView accelerometerReading;
    //declare the textview for the highest accelerometer reading
    TextView highestAccelerometerReading;
    //declare the textview for the magnetic sensor reading
    TextView magneticSensorText;
    //declare the highest magneticSensor
    TextView highestMagneticReading;
    //declare the textview for the rotation vector
    TextView rotationSensorText;
    //declare the textview for the highest rotation vector
    TextView highestRotationVector;
    //this is the array of the 100 last points in the accelerometer reading
    float [][] accelarray = new float [100][3];
    //keeps trap of the location of the array and where to store it
    int location = 0;
    //button for clear record high data
    Button clearhighrecord;
    //button for CSV record accelerometer
    Button generateCSV;



//beginning of onCreate(sets the textViews and the Buttons and basic interface)============================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1_206_02);


        //declare the linear layout
        LinearLayout rl = (LinearLayout) findViewById(R.id.layout);
        //get it to be vertical
        rl.setOrientation(LinearLayout.VERTICAL);


        //add graph first to the very beginning
        LineGraphView graph = new LineGraphView(getApplicationContext(), 100, Arrays.asList("x", "y", "z"));
        //add the graph to the linear layout
        rl.addView(graph);
        //make it visible
        graph.setVisibility(View.VISIBLE);


        //create a button
        clearhighrecord = new Button(getApplicationContext());
        clearhighrecord.setText("Clear Record High Data");
        clearhighrecord.setBackgroundColor(Color.parseColor("#64B5F6"));
        //first add layout margins so it doesn't look too clutteredd
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        clearhighrecord.setLayoutParams(params);
        rl.addView(clearhighrecord);


        //create the next new button
        generateCSV = new Button(getApplicationContext());
        generateCSV.setText("Generate CSV Record for Acc. Sen.");
        generateCSV.setBackgroundColor(Color.parseColor("#64B5F6"));
        generateCSV.setLayoutParams(params);
        generateCSV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String test = "test";
                String filename = "tester.csv";
                File file = new File(getExternalFilesDir(test), filename);
                String absolutepath = file.getAbsolutePath();
                Log.v("File path: ", absolutepath);
                Toast.makeText(Lab1_206_02.this, absolutepath, Toast.LENGTH_LONG).show();
                try {
                    //where you declare new PrintWriter
                    PrintWriter writer = new PrintWriter(new FileWriter(absolutepath));
                    //write for loop to display the whole array
                    //get the value of location and the array
                    //write a for loop that decreases to 0 from location
                    //and then goes from that location to the original one, so :
                    for(int i =0; i<100; i++){
                        int start = location;
                        if(start == 0){
                            writer.println(String.format("%f, %f, %f", accelarray[start][0], accelarray[start][1], accelarray[start][2]));
                            //set the start time  to be 100
                            start =100;
                        }
                        else{
                            writer.println(String.format("%f, %f, %f", accelarray[start][0], accelarray[start][1], accelarray[start][2]));
                            start--;
                        }
                    }
                    //close the file
                    writer.close();
                }
                //in the case where the file does not exist
                catch (FileNotFoundException e){
                    Log.e("Error:" , "File not found!");
                }
                //or the event when the IO does not exist
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //add the button view for creating the CSV
        rl.addView(generateCSV);


        //Start of light sensor text view -----------------------------------------------------------
        //first two use the textview created in the xml with ids
        //links to the textview with highestLightSensor
        TextView lightSensortitle = new TextView(getApplicationContext());
        lightSensortitle.setText("The Light Sensor Reading is: ");
        lightSensortitle.setTextColor(Color.parseColor("#000000"));
        lightSensortitle.setPadding(15, 15, 0, 0 );
        rl.addView(lightSensortitle);
        lightSensorText = new TextView(getApplicationContext());
        rl.addView(lightSensorText);
        lightSensorText.setPadding(15, 5, 0, 20);
        lightSensorText.setTextColor(Color.parseColor("#000000"));

        TextView HighestlightSensortitle = new TextView(getApplicationContext());
        HighestlightSensortitle.setText("The Record-High Light Sensor Reading is: ");
        rl.addView(HighestlightSensortitle);
        HighestlightSensortitle.setTextColor(Color.parseColor("#000000"));
        HighestlightSensortitle.setPadding(15, 15, 0, 0 );

        highestLightSensor = new TextView(getApplicationContext());
        rl.addView(highestLightSensor);
        highestLightSensor.setPadding(15, 5, 0, 20);
        highestLightSensor.setTextColor(Color.parseColor("#000000"));

        //creates a SensorManager to get the Sensor_Services
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //creates a sensor that gets light
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //creates a new event listener and calls the class LightSensorEventListener
        //and connects it to display in the textview lightSensorText
        SensorEventListener l = new LightSensorEventListener(lightSensorText, highestLightSensor);
        //registers the listener to display in l and gets a delay normal
        sensorManager.registerListener(l, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);


        //Start of accelerometer sensor text view --------------------------------------------------
        //the rest create an textview object here in the java
        //for the accelerometer part
        //creates the title only
        TextView accelerometertitle = new TextView(getApplicationContext());
        accelerometertitle.setText("The Accelerometer Reading is: ");
        rl.addView(accelerometertitle);
        accelerometertitle.setTextColor(Color.parseColor("#000000"));
        accelerometertitle.setPadding(15, 15, 0, 0 );

        //now for the data
        accelerometerReading = new TextView(getApplicationContext());
        //add the view to the app
        rl.addView(accelerometerReading);
        accelerometerReading.setPadding(15, 5, 0, 20);
        accelerometerReading.setTextColor(Color.parseColor("#000000"));
        //add another sensor but this time a accelerometer
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //creates the title only
        TextView accelerometertitle2 = new TextView(getApplicationContext());
        accelerometertitle2.setText("The Record-High Accelerometer Reading is: ");
        accelerometertitle2.setPadding(15, 15, 0, 0 );
        rl.addView(accelerometertitle2);
        accelerometertitle2.setTextColor(Color.parseColor("#000000"));
        //now add the data
        highestAccelerometerReading = new TextView(getApplicationContext());
        rl.addView(highestAccelerometerReading);
        highestAccelerometerReading.setPadding(15, 5, 0, 20);
        highestAccelerometerReading.setTextColor(Color.parseColor("#000000"));
        //now call the accelerometer class
        SensorEventListener a = new AccelerometerSensorEventListener(accelerometerReading,
                highestAccelerometerReading,
                graph,
                accelarray,
                location);
        sensorManager.registerListener(a, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);


        //Start of the magnetic sensor textview ----------------------------------------------------
        //This is the magnetic sensor reading
        //this is for creating the title for the magnetic sensor
        TextView magnetictitle = new TextView(getApplicationContext());
        magnetictitle.setText("The Magnetic Sensor Reading is:");
        rl.addView(magnetictitle);
        magnetictitle.setPadding(15, 15, 0, 0 );
        magnetictitle.setTextColor(Color.parseColor("#000000"));
        //now add the data
        magneticSensorText = new TextView(getApplicationContext());
        rl.addView(magneticSensorText);
        magneticSensorText.setPadding(15, 5, 0, 20);
        magneticSensorText.setTextColor(Color.parseColor("#000000"));
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //creates the highest magnetic recorded
        TextView magnetictitle2 = new TextView(getApplicationContext());
        magnetictitle2.setText("The Highest Magnetic Sensor Reading is: ");
        magnetictitle2.setPadding(15, 15, 0, 0 );
        rl.addView(magnetictitle2);
        magnetictitle2.setTextColor(Color.parseColor("#000000"));
        //now add the data
        highestMagneticReading = new TextView(getApplicationContext());
        rl.addView(highestMagneticReading);
        SensorEventListener b = new MagneticSensorEventListener(magneticSensorText, highestMagneticReading);
        highestMagneticReading.setPadding(15, 5, 0, 20);
        highestMagneticReading.setTextColor(Color.parseColor("#000000"));
        sensorManager.registerListener(b, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);


        //Start of the rotation vector textview -----------------------------------------------------
        TextView roationtitle = new TextView(getApplicationContext());
        roationtitle.setText("The Rotation Sensor Reading is:");
        roationtitle.setPadding(15, 15, 0, 0 );
        rl.addView(roationtitle);
        roationtitle.setTextColor(Color.parseColor("#000000"));
        //now add the data
        rotationSensorText = new TextView(getApplicationContext());
        rl.addView(rotationSensorText);
        rotationSensorText.setPadding(15, 5, 0, 20);
        rotationSensorText.setTextColor(Color.parseColor("#000000"));
        Sensor roationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        //creates the highest rotation recorded
        TextView rotationtitle2 = new TextView(getApplicationContext());
        rotationtitle2.setText("The Highest Rotation Sensor Reading is: ");
        rl.addView(rotationtitle2);
        rotationtitle2.setTextColor(Color.parseColor("#000000"));
        rotationtitle2.setPadding(15, 15, 0, 0 );
        //now add the data
        highestRotationVector = new TextView(getApplicationContext());
        rl.addView(highestRotationVector);
        highestRotationVector.setPadding(15, 5, 0, 20);
        highestRotationVector.setTextColor(Color.parseColor("#000000"));
        SensorEventListener c = new RotationSensorEventListener(rotationSensorText, highestRotationVector);
        sensorManager.registerListener(c, roationSensor, SensorManager.SENSOR_DELAY_NORMAL);


        //now that all the textViews has been created we can now set what the clear data button does
        clearhighrecord.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //set the highest recorded data back to 0
                //need to call all the sensors to set the highest all back to 0
                //at the very end manually set all the Textviews for the highest back to 0
                //once this has all been done, maybe toast the message that the data has been cleared
                //so that the user knows

            }

        });

    }
    //End of all the text View ---------------------------------------------------------------------
}
//End of onCreate=====================================================================================



//Beginning of the LIGHT SENSOR===========================================================================
//class where light sensor is called for reading
class LightSensorEventListener implements SensorEventListener{
    TextView output1;
    TextView output2;
    float highest = 0;

    public LightSensorEventListener(TextView outputView, TextView outputView2){
        output1 = outputView;
        output2 = outputView2;
    }

    public void onAccuracyChanged (Sensor s, int i){  }

    public void onSensorChanged (SensorEvent se){
        if (se.sensor.getType() == Sensor.TYPE_LIGHT){
            float temp = se.values[0];
            output1.setText(Float.toString(se.values[0]));
            if(temp > highest){
                highest = temp;
                output2.setText(Float.toString(highest));
            }

        }
    }
}
//end of LIGTHSENSOR===============================================================================



//Beginning of the ACCELEROTMER CLASS =============================================================
//class where accelerometer sensor is called for reading
class AccelerometerSensorEventListener implements SensorEventListener{
    TextView output1;
    TextView output2;
    float highestx=0;
    float highesty=0;
    float highestz=0;
    String highestcordinates;
    float tempx =0;
    float tempy =0;
    float tempz = 0;
    float [][] accArray;
    int increment;
    LineGraphView graph1;

    public AccelerometerSensorEventListener(TextView outputView, TextView outputView2,
                                            LineGraphView graph, float [][] array, int location){
        output1= outputView;
        output2= outputView2;
        graph1 = graph;
        accArray = array;
        increment = location;
    }

    public void onAccuracyChanged (Sensor s, int i){  }

    public void onSensorChanged (SensorEvent se) {

        graph1.addPoint(se.values);

        if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            String cordinatesx= Float.toString(se.values[0]);
            String cordinatesy = Float.toString(se.values[1]);
            String cordinatesz = Float.toString(se.values[2]);

            //this is the case where the the length of the array has
            //been surpassed and must start rewriting elements in the array
            if(increment == 100){
                accArray[increment][0] = se.values[0];
                accArray[increment][1] = se.values[1];
                accArray[increment][2] = se.values[2];
                increment=0;
            }
            else{
                accArray[increment][0] = se.values[0];
                accArray[increment][1] = se.values[1];
                accArray[increment][2] = se.values[2];
                increment++;
            }

            String cordinates = "(" + cordinatesx + ", " + cordinatesy + ", " + cordinatesz + ")";

            output1.setText(cordinates);

            if(highestx < se.values[0] || highesty < se.values[1] || highestz < se.values[2]){
                if(highestx < se.values[0]){
                    highestx = se.values[0];
                }
                if(highesty < se.values[1]){
                    highesty = se.values[1];
                }
                if(highestz < se.values[2]){
                    highestz = se.values[2];
                }
                String temp1 = Float.toString(highestx);
                String temp2 = Float.toString(highesty);
                String temp3 = Float.toString(highestz);
                highestcordinates = "(" + temp1 + ", " + temp2 + ", " + temp3 + ")";
                output2.setText(highestcordinates);

            }
        }
    }
}
//End of ACCELEROMETER CLASS ======================================================================




//Beginning of the MAGNETIC CLASS =================================================================
//class where magnetic sensor is called for reading
class MagneticSensorEventListener implements SensorEventListener{
    TextView output1;
    TextView output2;
    float highestx=0;
    float highesty=0;
    float highestz=0;
    String highestcordinates;

    public MagneticSensorEventListener(TextView outputView, TextView outputView2){
        output1= outputView;
        output2= outputView2;
    }

    public void onAccuracyChanged (Sensor s, int i){  }

    public void onSensorChanged (SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            String cordinatesx= Float.toString(se.values[0]);
            String cordinatesy = Float.toString(se.values[1]);
            String cordinatesz = Float.toString(se.values[2]);

            String cordinates = "(" + cordinatesx + ", " + cordinatesy + ", " + cordinatesz + ")";

            output1.setText(cordinates);

            if(highestx < se.values[0] || highesty < se.values[1] || highestz < se.values[2]){
                if(highestx < se.values[0]){
                    highestx = se.values[0];
                }
                if(highesty < se.values[1]){
                    highesty = se.values[1];
                }
                if(highestz < se.values[2]){
                    highestz = se.values[2];
                }
                String temp1 = Float.toString(highestx);
                String temp2 = Float.toString(highesty);
                String temp3 = Float.toString(highestz);
                highestcordinates = "(" + temp1 + ", " + temp2 + ", " + temp3 + ")";
                output2.setText(highestcordinates);
            }
        }
    }
}
//END of MAGNETIC CLASS============================================================================



//Beginnning of the ROTATION CLASS ================================================================
//class where rotation sensor is called for reading
class RotationSensorEventListener implements SensorEventListener{
    TextView output1;
    TextView output2;
    float highestx=0;
    float highesty=0;
    float highestz=0;
    String highestcordinates;

    public RotationSensorEventListener(TextView outputView, TextView outputView2){
        output1= outputView;
        output2= outputView2;
    }

    public void onAccuracyChanged (Sensor s, int i){  }

    public void onSensorChanged (SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            String cordinatesx= Float.toString(se.values[0]);
            String cordinatesy = Float.toString(se.values[1]);
            String cordinatesz = Float.toString(se.values[2]);

            String cordinates = "(" + cordinatesx + ", " + cordinatesy + ", " + cordinatesz + ")";

            output1.setText(cordinates);

            if(highestx < se.values[0] || highesty < se.values[1] || highestz < se.values[2]){
                if(highestx < se.values[0]){
                    highestx = se.values[0];
                }
                if(highesty < se.values[1]){
                    highesty = se.values[1];
                }
                if(highestz < se.values[2]){
                    highestz = se.values[2];
                }
                String temp1 = Float.toString(highestx);
                String temp2 = Float.toString(highesty);
                String temp3 = Float.toString(highestz);
                highestcordinates = "(" + temp1 + ", " + temp2 + ", " + temp3 + ")";
                output2.setText(highestcordinates);
            }
        }
    }
}
//END of ROTATION Class ==========================================================================
//end of program