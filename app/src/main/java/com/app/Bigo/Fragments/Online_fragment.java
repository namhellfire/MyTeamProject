package com.app.Bigo.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.app.Bigo.API.ListManager;
import com.app.Bigo.Adapter.OnlineAdapter;
import com.app.Bigo.Adapter.SpacesItemDecoration;
import com.app.Bigo.Model.ListAPI;
import com.app.Bigo.R;
import com.app.Bigo.Utils.UtilConnect;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Online_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Online_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Online_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "Online fragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ArrayList<ListAPI> listAPIs;

    RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    public Online_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Online_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Online_fragment newInstance(String param1, String param2) {
        Online_fragment fragment = new Online_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        listAPIs = new ArrayList<>();
        listAPIs = getArguments().getParcelableArrayList(ListManager.LIST_API);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_online, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.lvOnline);

        OnlineAdapter onlineAdapter = new OnlineAdapter(getActivity(), UtilConnect.getCountry(listAPIs));
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new SlideInDownAnimator(new OvershootInterpolator(1f)));
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(onlineAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setAdapter(alphaInAnimationAdapter);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
