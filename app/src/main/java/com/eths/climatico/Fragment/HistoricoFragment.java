package com.eths.climatico.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.eths.climatico.R;
import com.eths.climatico.bo.Mensagem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HistoricoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    ArrayList<Mensagem> mensagem = new ArrayList<Mensagem>();
    ListView listView;
    View rootView;
    private OnHistoricoFragmentInteractionListener mListener;
    private ProgressDialog progress;

    public HistoricoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment HistoricoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoricoFragment newInstance() {
        HistoricoFragment fragment = new HistoricoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_historico, container, false);

        new GetClass().execute();
        listView = (ListView) rootView.findViewById(R.id.listView);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onHistoricoFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHistoricoFragmentInteractionListener) {
            mListener = (OnHistoricoFragmentInteractionListener) context;
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


    public interface OnHistoricoFragmentInteractionListener {
        // TODO: Update argument type and name
        void onHistoricoFragmentInteraction(Uri uri);
    }

    public class GetClass extends AsyncTask<String, String, String> {


        protected void onPreExecute(){
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Please wait");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader = null;
            try {

                URL url = new URL("http://www.unioeste-foz.br:3000/");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                return sb.toString();

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();

            JSONArray array = null;
            int id;
            try {
                array = new JSONArray(result);
                System.out.println(array);
                for(int i=0; i<array.length(); i++){
                    JSONObject jsonObj  = array.getJSONObject(i);
                    id = Integer.parseInt(jsonObj.getString("idEstacao"));
                    String date = jsonObj.getString("date");
                    date = date.replaceAll("T"," ").replaceAll(".000Z", " ");
                    mensagem.add(new Mensagem(id,date,jsonObj.getString("mensagem")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            HistoricoAdapter historicoAdapter = new HistoricoAdapter(rootView.getContext(),R.layout.item_fragment_historico,mensagem,getActivity().getSupportFragmentManager().beginTransaction());
            listView.setAdapter(historicoAdapter);
      }
    }

    public class HistoricoAdapter extends ArrayAdapter<Mensagem> {

        Context context;
        ArrayList<Mensagem> mensagem;
        private final LayoutInflater inflater;
        FragmentTransaction ft;
        TextView mensagem_ed;
        TextView data;
        String date;

        public HistoricoAdapter(Context context, int resource,ArrayList<Mensagem> mensagem,FragmentTransaction ft) {
            super(context, resource);
            this.context = context;
            this.ft = ft;
            inflater = LayoutInflater.from(context);
            this.mensagem = mensagem;
        }

        public int getCount() {
            return mensagem.size();
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final Mensagem mensagems = mensagem.get(position);
            view = inflater.inflate(R.layout.item_fragment_historico, null);
            mensagem_ed = (TextView) view.findViewById(R.id.ed_mensagem);
            mensagem_ed.setText(mensagems.getMensagem());
            data = (TextView) view.findViewById(R.id.ed_data);
            data.setText(mensagems.getData());
            return view;
        }
    }

}
