package com.eths.climatico.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eths.climatico.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClimaFragment.OnClimaFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClimaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClimaFragment extends Fragment {

    private OnClimaFragmentInteractionListener mListener;

    View rootView;

    MqttAndroidClient mqttAndroidClient;

    final String serverUri = "tcp://iot.eclipse.org:1883";

    int randomNum = new Random().nextInt((500 - 100) + 1) + 100;
    String randomString = Integer.toString(randomNum);
    final String clientId = "ExampleAndsdadroidClient" + randomString;


    final String subscriptionTopic = "envia";
    final String publishTopic = "recebe";

    public ClimaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *gment ClimaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClimaFragment newInstance() {
        ClimaFragment fragment = new ClimaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView  = inflater.inflate(R.layout.fragment_clima1, container, false);

        final TextView lb_clima = (TextView) rootView.findViewById(R.id.lb_clima);
      /*  final TextView ed_temperatura = (TextView) rootView.findViewById(R.id.ed_temperatura);
        final TextView ed_chuva = (TextView) rootView.findViewById(R.id.ed_chuva);
        final TextView ed_umidadear = (TextView) rootView.findViewById(R.id.ed_umidadear);
        final TextView ed_umidadesolo = (TextView) rootView.findViewById(R.id.ed_umidadesolo);
        final TextView ed_status = (TextView) rootView.findViewById(R.id.ed_estado);
        final TextView ed_data = (TextView) rootView.findViewById(R.id.ed_verifica);


        ed_temperatura.setText("null");
        ed_chuva.setText("null");
        ed_umidadear.setText("null");
        ed_umidadesolo.setText("null");
        ed_status.setText("Estaçao: Offline");
        ed_data.setText("null");
*/
        lb_clima.setText(" 30 C ");
        mqttAndroidClient = new MqttAndroidClient(getContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    addToHistory("Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();
                } else {
                    addToHistory("Connected to: " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                addToHistory("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                addToHistory("Incoming message: " + new String(message.getPayload()));
              /*  String Mensagem = new String(message.getPayload());
                String parts[] = Mensagem.split(" ");
                ed_status.setText("Estaçao: Online");
                ed_status.setTextColor(Color.parseColor("#42f471"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                ed_data.setText(currentDateandTime);
                if(parts[1].equals("T")){
                    ed_temperatura.setText(parts[0] + " Graus");
                }
                if(parts[1].equals("C")){
                    float valor = Float.parseFloat(parts[0]);
                    //addToHistory("incoming measea" + parts[0]);
                    if(valor > 0 && valor < 400) {
                        ed_chuva.setText("Intensidade chuva Alta");
                    }else if(valor > 400 && valor < 700){
                        ed_chuva.setText("Intensidade chuva Moderada");
                    }else if(valor > 700){
                        ed_chuva.setText("Intensidade chuva Baixa");
                    }
                }
                if(parts[1].equals("HT")){
                    float valor = Float.parseFloat(parts[0]);
                    if(valor > 0 && valor < 400){
                        ed_umidadesolo.setText("Solo Umido");
                    }else if(valor > 400 && valor <700){
                        ed_umidadesolo.setText("Solo Modelara");
                    }else if(valor > 700){
                        ed_umidadesolo.setText("Solo Seco");
                    }
                }
                if(parts[1].equals("HA")){
                    ed_umidadear.setText(parts[0] + " %");
                }*/
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            //addToHistory("Connecting to " + serverUri);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to connect to: " + serverUri);
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }
        return rootView;
    }

    private void addToHistory(String mainText){
        System.out.println("LOG: " + mainText);
        //mAdapter.add(mainText);
        //Snackbar.make(findViewById(android.R.id.content), mainText, Snackbar.LENGTH_LONG)
        //    .setAction("Action", null).show();

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onClimaFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClimaFragmentInteractionListener) {
            mListener = (OnClimaFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnClimaFragmentInteractionListener {
        // TODO: Update argument type and name
        void onClimaFragmentInteraction(Uri uri);
    }
    public void publishMessage(String pMessage){

        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(pMessage.getBytes());
            mqttAndroidClient.publish(publishTopic, message);

            if(!mqttAndroidClient.isConnected()){

            }
        } catch (MqttException e) {
            System.err.println("Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void subscribeToTopic(){
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //addToHistory("Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //addToHistory("Failed to subscribe");
                }
            });

        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }
}
