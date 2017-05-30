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
    //test

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
        //set the text to be clear record highs
        clearhighrecord.setText("Clear Record High Data");
        clearhighrecord.setBackgroundColor(Color.parseColor("#64B5F6"));
        //first add layout margins so it doesn't look too clutteredd
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        clearhighrecord.setLayoutParams(params);
        //now that all the textViews has been created we can now set what the clear data button does
        rl.addView(clearhighrecord);
        //look at the bottom of onCreate to see the onClick functionallity


        //create the next new button
        generateCSV = new Button(getApplicationContext());
        generateCSV.setText("Generate CSV Record for Acc. Sen.");
        generateCSV.setBackgroundColor(Color.parseColor("#64B5F6"));
        generateCSV.setLayoutParams(params);
        //add the button view for creating the CSV
        rl.addView(generateCSV);
        //look at the bottom of onCreate to see the onClick functionallity


        //Start of light sensor text view -----------------------------------------------------------
        //first two use the textview created in the xml with ids
        //links to the textview with highestLightSensor
        TextView lightSensortitle = new TextView(getApplicationContext());
        //set the text to be the Light sensor reading and set padding
        lightSensortitle.setText("The Light Sensor Reading is: ");
        lightSensortitle.setTextColor(Color.parseColor("#000000"));
        lightSensortitle.setPadding(15, 15, 0, 0 );
        //add the view fo r the title
        rl.addView(lightSensortitle);

        //now this textview is where the readings will be displayed
        lightSensorText = new TextView(getApplicationContext());
        rl.addView(lightSensorText);
        lightSensorText.setPadding(15, 5, 0, 20);
        lightSensorText.setTextColor(Color.parseColor("#000000"));

        //this is the textview for the highest light sensor reading title
        TextView HighestlightSensortitle = new TextView(getApplicationContext());
        HighestlightSensortitle.setText("The Record-High Light Sensor Reading is: ");
        rl.addView(HighestlightSensortitle);
        HighestlightSensortitle.setTextColor(Color.parseColor("#000000"));
        HighestlightSensortitle.setPadding(15, 15, 0, 0 );

        //now this is the textview for the highest reading value display
        highestLightSensor = new TextView(getApplicationContext());
        //add the view and set padding
        rl.addView(highestLightSensor);
        highestLightSensor.setPadding(15, 5, 0, 20);
        highestLightSensor.setTextColor(Color.parseColor("#000000"));

        //creates a SensorManager to get the Sensor_Services
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //creates a sensor that gets light
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //creates a new event listener and calls the class LightSensorEventListener

        //and connects it to display in the textview lightSensorText
        final SensorEventListener l = new LightSensorEventListener(lightSensorText, highestLightSensor);
        //set the SensorEventListener to be type LightSensorEventListener to get
        //values from the other class to reset values and get values
        final LightSensorEventListener z = (LightSensorEventListener) l;
        //z.resetHigh();
        //registers the listener to display in l and gets a delay normal
        sensorManager.registerListener(l, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);



        //Start of accelerometer sensor text view --------------------------------------------------
        //the rest create an textview object here in the java
        //for the accelerometer part
        //creates the title only
        TextView accelerometertitle = new TextView(getApplicationContext());
        accelerometertitle.setText("The Accelerometer Reading is: ");
        //add the title for the accelerometer and set padding and colour to black
        rl.addView(accelerometertitle);
        accelerometertitle.setTextColor(Color.parseColor("#000000"));
        accelerometertitle.setPadding(15, 15, 0, 0 );

        //now for the data textview
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
        //now add the data textview

        highestAccelerometerReading = new TextView(getApplicationContext());
        rl.addView(highestAccelerometerReading);
        highestAccelerometerReading.setPadding(15, 5, 0, 20);
        highestAccelerometerReading.setTextColor(Color.parseColor("#000000"));

        //now call the accelerometer class and pass in a lot of parameters such as graphs
        SensorEventListener a = new AccelerometerSensorEventListener(accelerometerReading,
                highestAccelerometerReading,
                graph, accelarray,location);
        //set the Sensor Event Listener to AcceleromterSensorEventListener to get values
        final AccelerometerSensorEventListener y = (AccelerometerSensorEventListener) a;
        //register the sensor
        sensorManager.registerListener(a, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);


        //Start of the magnetic sensor textview ----------------------------------------------------
        //This is the magnetic sensor reading
        //this is for creating the title for the magnetic sensor
        TextView magnetictitle = new TextView(getApplicationContext());
        magnetictitle.setText("The Magnetic Sensor Reading is:");
        rl.addView(magnetictitle);
        magnetictitle.setPadding(15, 15, 0, 0 );
        magnetictitle.setTextColor(Color.parseColor("#000000"));

        //now add the data textview
        magneticSensorText = new TextView(getApplicationContext());
        rl.addView(magneticSensorText);
        magneticSensorText.setPadding(15, 5, 0, 20);
        magneticSensorText.setTextColor(Color.parseColor("#000000"));
        //declare the magnetic sensor
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //creates the highest magnetic recorded title
        TextView magnetictitle2 = new TextView(getApplicationContext());
        magnetictitle2.setText("The Highest Magnetic Sensor Reading is: ");
        magnetictitle2.setPadding(15, 15, 0, 0 );
        rl.addView(magnetictitle2);
        magnetictitle2.setTextColor(Color.parseColor("#000000"));

        //now add the data textview
        highestMagneticReading = new TextView(getApplicationContext());
        rl.addView(highestMagneticReading);

        //set the event listener for the magnetic sensor
        SensorEventListener b = new MagneticSensorEventListener(magneticSensorText, highestMagneticReading);
        //set the sensoreventlistener to the magneticsensoreventlistener to get the values
        final MagneticSensorEventListener x = (MagneticSensorEventListener) b;
        highestMagneticReading.setPadding(15, 5, 0, 20);
        highestMagneticReading.setTextColor(Color.parseColor("#000000"));
        //now register the sensor
        sensorManager.registerListener(b, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);


        //Start of the rotation vector textview -----------------------------------------------------
        //add the rotations sensor title textview
        TextView roationtitle = new TextView(getApplicationContext());
        roationtitle.setText("The Rotation Sensor Reading is:");
        roationtitle.setPadding(15, 15, 0, 0 );
        rl.addView(roationtitle);
        roationtitle.setTextColor(Color.parseColor("#000000"));

        //now add the data textview
        rotationSensorText = new TextView(getApplicationContext());
        rl.addView(rotationSensorText);
        rotationSensorText.setPadding(15, 5, 0, 20);
        rotationSensorText.setTextColor(Color.parseColor("#000000"));
        //declare the rotationSensor sensor
        Sensor roationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        //creates the highest rotation recorded
        TextView rotationtitle2 = new TextView(getApplicationContext());
        rotationtitle2.setText("The Highest Rotation Sensor Reading is: ");
        rl.addView(rotationtitle2);
        rotationtitle2.setTextColor(Color.parseColor("#000000"));
        rotationtitle2.setPadding(15, 15, 0, 0 );

        //now add the data textview
        highestRotationVector = new TextView(getApplicationContext());
        rl.addView(highestRotationVector);
        highestRotationVector.setPadding(15, 5, 0, 20);
        highestRotationVector.setTextColor(Color.parseColor("#000000"));

        //set the sensoreventlistener and pass in params to the class
        SensorEventListener c = new RotationSensorEventListener(rotationSensorText, highestRotationVector);
        //set the SEL to be a RotationSensorEventListener to get values from the class
        final RotationSensorEventListener w = (RotationSensorEventListener) c;
        //regeister the sensor
        sensorManager.registerListener(c, roationSensor, SensorManager.SENSOR_DELAY_NORMAL);


        //Start of the onClick for Reset    -----------------------------------------------------------
        //this clears all the highest records to 0
        clearhighrecord.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //set the highest recorded data back to 0
                //reset the highest readings for the lightsensor
                z.resetHigh();
                //reset the accelorometer readings(all 3 vectors)
                y.resetHigh();
                //reset the magnetic highest readings
                x.resetHigh();
                //reset the highest rotation vector readings
                w.resetHigh();
                //toast and let the user know that all the highest data points has been wipped
                Toast.makeText(Lab1_206_02.this, "Highest Data has been cleared!", Toast.LENGTH_LONG).show();
                //need to call all the sensors to set the highest all back to 0
                //once this has all been done, maybe toast the message that the data has been cleared
                //so that the user knows
            }

        });



        //Start of the generate CSV file onClick method ------------------------------------------------
        //generates a CSV file that contains the last 100 readings from the accelerometer
        //the 100 points currently displayed on the graph
        generateCSV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //name of the folder for the CSV file
                String test = "test";
                //this is the file name that is to be created
                String filename = "tester.csv";
                //creates a new exteral folder and file named file and tester.csv
                File file = new File(getExternalFilesDir(test), filename);
                //the whole file path for the saved csv file on your mobile phone
                String absolutepath = file.getAbsolutePath();

                //log the whole file locaiton of the csv file
                Log.v("File path: ", absolutepath);
                //also toast the whole file locaiton to let the user know where it was saved
                Toast.makeText(Lab1_206_02.this, absolutepath, Toast.LENGTH_LONG).show();

                //try to do this, if fails got ot catch
                try {
                    //where you declare new PrintWriter
                    PrintWriter writer = new PrintWriter(new FileWriter(absolutepath));
                    //write for loop to display the whole array
                    //get the value of location and the array
                    //write a for loop that decreases to 0 from location
                    //and then goes from that location to the original one, so :
                    //get the current location in the array since we want to display new points first
                    int start = y.location();

                    //go through the first 100 points
                    for(int i =0; i<100; i++){

                        //if the start is equal to 0, then loop back to be 100 to display the most recent points
                        if(start == 0){
                            //write the data point into the csv file
                            writer.println(String.format("%f, %f, %f", accelarray[start][0], accelarray[start][1], accelarray[start][2]));
                            //set the start time  to be 99, because the array starts at 0
                            start =99;
                        }
                        //else, if the start locaiton is not 1 then just keep decreasing start by 1 to get the most recent points
                        else{
                            //write the data point into the file
                            writer.println(String.format("%f, %f, %f", accelarray[start][0], accelarray[start][1], accelarray[start][2]));
                            //decremet the start location to display all the points
                            start--;
                        }
                    }

                    //close the file
                    writer.close();
                }

                //in the case where the file does not exist
                catch (FileNotFoundException e){
                    //display the error messages into the log file
                    Log.e("Error:" , "File not found!");
                }

                //or the event when the IO does not exist
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    //End of all the text View ---------------------------------------------------------------------
}
//End of onCreate=====================================================================================





//Beginning of the LIGHT SENSOR===========================================================================
//class where light sensor is called for reading
class LightSensorEventListener implements SensorEventListener{
    //the local text view for outputCiew
    TextView output1;
    //the local text view for outputView2
    TextView output2;
    //local float variable for the highrest recorded light Sensor reading
    float highest = 0;

    //constructor of the class
    public LightSensorEventListener(TextView outputView, TextView outputView2){
        //set the local textview to be the first passed in textview
        output1 = outputView;
        //set the local textview output2 to be the second passed in textveiw
        output2 = outputView2;
    }

    //sets the accuracy of the readings, set as default
    public void onAccuracyChanged (Sensor s, int i){  }

    //method resets the highest reading to 0 onClick of the reset button
    public void resetHigh(){
        //set to 0
        highest = 0;
        //display the 0 to the textview
        output2.setText(Float.toString(highest));
    }

    //where the current sensor readings is updated and where the highest sensor is tracked
    public void onSensorChanged (SensorEvent se){

        //if the sensor type is indeed type light
        if (se.sensor.getType() == Sensor.TYPE_LIGHT){
            //them set the temporary variable temp equal to the current reading
            //from the sensor stored as the first element in the array values
            float temp = se.values[0];
            //set the reading to be the current recorded sensor reading
            output1.setText(Float.toString(se.values[0]));

            //if the current reading is bigger than the highest recorded reading then change it
            if(temp > highest){
                //set the highest reading to be the current one
                highest = temp;
                //set the textview reading for the highest value to change
                output2.setText(Float.toString(highest));
            }

        }
    }
}
//end of LIGTHSENSOR===============================================================================





//Beginning of the ACCELEROTMER CLASS =============================================================
//class where accelerometer sensor is called for reading
class AccelerometerSensorEventListener implements SensorEventListener{
    //local variable for the first textview
    TextView output1;
    //local variable for the second variable
    TextView output2;
    //highest recorded x value is stored here
    float highestx=0;
    //highest recorded y value is stroed here
    float highesty=0;
    //highest recorded z value is stored here
    float highestz=0;
    //String for the highest recorded value reading
    String highestcordinates;
    //array for the most recent 100 readings from the sensor
    float [][] accArray = new float[100][3];
    //keeps track of where in the array it is currently at
    int increment=0;
    //local variable for the passed in graph
    LineGraphView graph1;


    //class contructor
    public AccelerometerSensorEventListener(TextView outputView, TextView outputView2,
                                            LineGraphView graph, float [][] array, int location){
        //set the local textview to be equal to the passed in textview
        output1= outputView;
        //set the local textview2 be be equal to be the second passed in textview
        output2= outputView2;
        //set the local graph to be equal to the passed in graph
        graph1 = graph;
        //set the passed in array equal to the lcoal arrya
        accArray = array;
        //set teh local increment variable equal to the passed in location
        increment = location;
    }


    //sets the sensor reading accuracy, set as default
    public void onAccuracyChanged (Sensor s, int i){  }

    //mehtod called when reset highest values is clicked
    public void resetHigh(){
        //set the string to be all 0
        highestcordinates = "(0, 0, 0)";
        //set the highest x value reading to be 0
        highestx =0;
        //set the highest y vlaue reading to be 0
        highesty=0;
        //set the highest z value reading to be 0
        highestz= 0;
        //set the highest out put textview to be 0, 0, 0
        output2.setText(highestcordinates);
    }

    //method to find the current location of the array when creating the csv file
    public int location(){
        //returns the current location of the array
        return increment;
    }

    //method for updating the values on the graph, array, and textview
    public void onSensorChanged (SensorEvent se) {

        //add a point to the graph of the current value
        graph1.addPoint(se.values);

        //if the sensor is of type accelerometer then continuie
        if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //set the temp variable cordinatesx to be the current x value reading
            String cordinatesx= Float.toString(se.values[0]);
            //set for y
            String cordinatesy = Float.toString(se.values[1]);
            //and z
            String cordinatesz = Float.toString(se.values[2]);

            //this is the case where the the length of the array has
            //been surpassed and must start rewriting elements in the array
            if(increment == 99){
                //set the location of where we are in the array to the current reading and save
                //for x
                accArray[increment][0] = se.values[0];
                //for y
                accArray[increment][1] = se.values[1];
                // and z
                accArray[increment][2] = se.values[2];
                //so once at 99, then loop back to 0 and overwrite the data there
                increment=0;
            }
            //else if it isn't 99, then keep going until 99
            else{
                //set the value for the current readings from the sensor to be the array currentl location
                //for x
                accArray[increment][0] = se.values[0];
                //for y
                accArray[increment][1] = se.values[1];
                //and for z
                accArray[increment][2] = se.values[2];
                //then increment
                increment++;
            }

            //store the current readings from the sensor into the string cordinates to be displayed
            String cordinates = "(" + cordinatesx + ", " + cordinatesy + ", " + cordinatesz + ")";

            //display the current readings to the textview
            output1.setText(cordinates);

            // now check if any of the readings were a record high reading
            if(highestx < se.values[0] || highesty < se.values[1] || highestz < se.values[2]){
                //for x check if it was the highest reading
                if(highestx < se.values[0]){
                    highestx = se.values[0];
                }
                //then for y
                if(highesty < se.values[1]){
                    highesty = se.values[1];
                }
                //and finally for z
                if(highestz < se.values[2]){
                    highestz = se.values[2];
                }
                //save all the highest reading to seperate string
                //because only one of them could have been updated
                //for x
                String temp1 = Float.toString(highestx);
                //for y
                String temp2 = Float.toString(highesty);
                //and fianlly for z
                String temp3 = Float.toString(highestz);
                //now concatinate the strings to be one string to b edisplayed
                highestcordinates = "(" + temp1 + ", " + temp2 + ", " + temp3 + ")";
                //dispaly the highest reading on the the textview
                output2.setText(highestcordinates);

            }
        }
    }
}
//End of ACCELEROMETER CLASS ======================================================================





//Beginning of the MAGNETIC CLASS =================================================================
//class where magnetic sensor is called for reading
class MagneticSensorEventListener implements SensorEventListener{
    //local textview 1
    TextView output1;
    //local textview 2
    TextView output2;
    //highest reading for x
    float highestx=0;
    //highest reading fo ry
    float highesty=0;
    //hihgest reading for z
    float highestz=0;
    //stores all teh highest readings and displays as a string
    String highestcordinates;

    //class constructor
    public MagneticSensorEventListener(TextView outputView, TextView outputView2){
        //set the first passed in textview to be equal to the local textview
        output1= outputView;
        //again the same for the second
        output2= outputView2;
    }

    //set the accuracy of teh sensor readings, set as default
    public void onAccuracyChanged (Sensor s, int i){  }


    //method called when to reset the highest readings for the magnetic sensor
    public void resetHigh(){
        //set the string equal to be 000
        highestcordinates = "(0, 0, 0)";
        //now set the float to be 0
        //for x
        highestx =0;
        //y and
        highesty=0;
        //z variables
        highestz= 0;
        //update the textveiw to to be 0,0,0
        output2.setText(highestcordinates);
    }

    //method for when the app is running to update the sensor readings
    public void onSensorChanged (SensorEvent se) {
        //if the sensor type is magnetic field, then continue
        if (se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            //set the temp variable cordinatesx to the current sensor reading for x
            String cordinatesx= Float.toString(se.values[0]);
            //set the temp variable cordinatesy to the current sensor reading for y
            String cordinatesy = Float.toString(se.values[1]);
            //set the temp variable cordinatesz to the current sensor reading for z
            String cordinatesz = Float.toString(se.values[2]);

            //set the cordinates of the current reading into a string
            String cordinates = "(" + cordinatesx + ", " + cordinatesy + ", " + cordinatesz + ")";

            //output the cordinates string to the textview
            output1.setText(cordinates);

            //now chick if the current reading is greater than the previous highest readings
            if(highestx < se.values[0] || highesty < se.values[1] || highestz < se.values[2]){
                //for x
                if(highestx < se.values[0]){
                    highestx = se.values[0];
                }
                //y and
                if(highesty < se.values[1]){
                    highesty = se.values[1];
                }
                //z highest for any one of them
                if(highestz < se.values[2]){
                    highestz = se.values[2];
                }
                //now convert the floats to stings seperately
                //for x
                String temp1 = Float.toString(highestx);
                //y and
                String temp2 = Float.toString(highesty);
                //z because it could be that only one of them need ot be updated
                String temp3 = Float.toString(highestz);
                //update the highest reading textview
                highestcordinates = "(" + temp1 + ", " + temp2 + ", " + temp3 + ")";
                //output it to the textview
                output2.setText(highestcordinates);
            }
        }
    }
}
//END of MAGNETIC CLASS============================================================================




//Beginnning of the ROTATION CLASS ================================================================
//class where rotation sensor is called for reading
class RotationSensorEventListener implements SensorEventListener{
    //local text view for 1
    TextView output1;
    //locat text view for 2
    TextView output2;
    //highest x reading
    float highestx=0;
    //highest y reading
    float highesty=0;
    //highest z reading
    float highestz=0;
    //string that contains the highest reading
    String highestcordinates;

    //constructor for the class
    public RotationSensorEventListener(TextView outputView, TextView outputView2){
        //set the local textview equal to teh passed in textview 1
        output1= outputView;
        //set the locat textview 2 equal to the passed in textview 2
        output2= outputView2;
    }

    //set the accuracy of the reading to be default
    public void onAccuracyChanged (Sensor s, int i){  }

    //called onClick to reset the highest readings
    public void resetHigh(){
        //set the string to be all 0
        highestcordinates = "(0, 0, 0)";
        //set the hihgest readings to be 0
        //for x
        highestx =0;
        //y and
        highesty=0;
        //z
        highestz= 0;
        //out put 000 to the textview
        output2.setText(highestcordinates);
    }

    //for each and everytime this runs to update values to current readings
    public void onSensorChanged (SensorEvent se) {
        //if it is of type rotation sensor
        if (se.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            //set the temp variable cordinatesx to be the current reading of x
            String cordinatesx= Float.toString(se.values[0]);
            //set the temp variable cordinatesy to be the current reading of y
            String cordinatesy = Float.toString(se.values[1]);
            //set the temp variable cordinatesz to be the current reading of z
            String cordinatesz = Float.toString(se.values[2]);

            //set the string of the cordinates to the the current reading
            String cordinates = "(" + cordinatesx + ", " + cordinatesy + ", " + cordinatesz + ")";

            //output the current readings to be the string of the current readings
            output1.setText(cordinates);

            //check to see any of the current readings is largest than the previous highest reading
            if(highestx < se.values[0] || highesty < se.values[1] || highestz < se.values[2]){

                //check for x
                if(highestx < se.values[0]){
                    highestx = se.values[0];
                }
                //y and
                if(highesty < se.values[1]){
                    highesty = se.values[1];
                }
                //z
                if(highestz < se.values[2]){
                    highestz = se.values[2];
                }
                //convert the highest reading for x to be a string
                String temp1 = Float.toString(highestx);
                //and for y to a string , must all be seperate
                String temp2 = Float.toString(highesty);
                //and for z
                String temp3 = Float.toString(highestz);
                //save them all together to a single string with the highest reading
                highestcordinates = "(" + temp1 + ", " + temp2 + ", " + temp3 + ")";
                //finally output the highest reading
                output2.setText(highestcordinates);
            }
        }
    }
}
//END of ROTATION Class ==========================================================================
//end of program