package com.example.demo_iot_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
public class MainActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    TextView txtTemp, txtHumi;
    LabeledSwitch btnLED1;
    LabeledSwitch btnLED2;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTemp = findViewById(R.id.textTemperature);
        txtHumi = findViewById(R.id.textHumidity);
        btnLED1 = findViewById(R.id.btnLED1);
        btnLED2 = findViewById(R.id.btnLED2);

        btnLED1.setOnToggledListener(new OnToggledListener(){
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn == true){
                    sendDataMQTT("khanhhuy03/feeds/nutnhan1", "1");
                }
                else{
                    sendDataMQTT("khanhhuy03/feeds/nutnhan1", "0");

                }

            }
        });
        btnLED2.setOnToggledListener(new OnToggledListener(){
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn == true){
                    sendDataMQTT("khanhhuy03/feeds/nutnhan2", "1");
                }
                else{
                    sendDataMQTT("khanhhuy03/feeds/nutnhan2", "0");

                }

            }
        });

//      btnLED2.setOnToggledListener(new OnToggledListener() {
//            @Override
//            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
//                if(isOn == true){
//                    sendDataMQTT("khanhhuy03/feeds/nutnhan2", "1");
//                }
//                else{
//                    sendDataMQTT("khanhhuy03/feeds/nutnhan2", "0");
//
//                }
//            }
//        });

//        btnLED1.setOnToggledListener(new OnToggledListener() {
//            @Override
//            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
//                if (internetConnected) {
//                    sendDataMQTT("khanhhuy03/feeds/nutnhan1", isOn ? "1" : "0");
//                } else {
//                    // Internet is not connected, revert toggle state
//                    btnLED1.setOn(!isOn);
//                    Toast.makeText(MainActivity.this, "Internet connection lost", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        btnLED2.setOnToggledListener(new OnToggledListener() {
//            @Override
//            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
//                if (internetConnected) {
//                    sendDataMQTT("khanhhuy03/feeds/nutnhan2", isOn ? "1" : "0");
//                } else {
//                    // Internet is not connected, revert toggle state
//                    btnLED2.setOn(!isOn);
//                    Toast.makeText(MainActivity.this, "Internet connection lost", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });




        startMQTT();
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check internet connection when activity starts
//        internetConnected = isInternetConnected();
//    }

    public void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        }catch (MqttException e){
        }
    }
    public void startMQTT(){
        mqttHelper = new MQTTHelper(this);
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override


            public void connectComplete(boolean reconnect, String serverURI) {
                Log.d("TEST", "Subcribed");
            }

            @Override
            public void connectionLost(Throwable cause) {


            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST", topic + "***" + message.toString());
                if(topic.contains("cambien1")){
                    txtTemp.setText(message.toString() + "*C");
                }
                else if(topic.contains("cambien2")){
                    txtHumi.setText(message.toString() + "%");
                }
                else if(topic.contains("nutnhan1")){
                    if(message.toString().equals("1")){
                        btnLED1.setOn(true);
                    }
                    else{
                        btnLED1.setOn(false);
                    }
                }
                else if(topic.contains("nutnhan2")){
                    if(message.toString().equals("1")){
                        btnLED2.setOn(true);
                    }
                    else{
                        btnLED2.setOn(false);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
//    private boolean isInternetConnected() {
//        // Implement logic to check if internet is connected
//        // This could be done using network connectivity manager or any other method
//        return true; // For demo purpose, always returning true
//    }

}
