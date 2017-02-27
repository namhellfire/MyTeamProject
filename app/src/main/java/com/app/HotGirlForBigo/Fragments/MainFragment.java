package com.app.hotgirlforbigo.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.app.hotgirlforbigo.API.ListManager;
import com.app.hotgirlforbigo.Adapter.OfflineAdapter;
import com.app.hotgirlforbigo.Adapter.SpacesItemDecoration;
import com.app.hotgirlforbigo.MainActivity;
import com.app.hotgirlforbigo.Model.Profile;
import com.app.hotgirlforbigo.R;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String TAG = "MainFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView mListview;
    public static String ApiUrl;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GridLayoutManager gridLayoutManager;
    private OfflineAdapter offlineAdapter;
    private ArrayList<Profile> arrayList;

    private ActionBar actionBar;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String api, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d(TAG, "oncreate");
        arrayList = new ArrayList<>();
        arrayList = getArguments().getParcelableArrayList(ListManager.DATA_CONTENT);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "oncreate view ");
        View v = inflater.inflate(R.layout.fragment_top, container, false);
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            // Portrait
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        }
        mListview = (RecyclerView) v.findViewById(R.id.lvOffline);
        mListview.addItemDecoration(new SpacesItemDecoration(10));
        mListview.setLayoutManager(gridLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });


        offlineAdapter = new OfflineAdapter(arrayList, getActivity());
        mListview.setItemAnimator(new SlideInDownAnimator(new OvershootInterpolator(1f)));
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(offlineAdapter);
        mListview.setAdapter(alphaInAnimationAdapter);

        mListview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int currentFirstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
                final int currentLastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                if (currentFirstVisibleItem > this.mLastFirstVisibleItem) {
//                    getActivity().getSupportActionBar().hide();
                    actionBar.hide();
                } else if (currentFirstVisibleItem < this.mLastFirstVisibleItem) {
//                    getActivity().getSupportActionBar().show();
                    actionBar.show();
                }

                if (currentLastVisibleItem == (offlineAdapter.getItemCount()-1)){
                    mListview.setPadding(0,0,0,150);
                    mListview.requestLayout();
                }else if (currentLastVisibleItem == (offlineAdapter.getItemCount()-3)){
                    mListview.setPadding(0,0,0,0);
                    mListview.requestLayout();
                }

                this.mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });


        Log.d(TAG, "Url api : " + MainActivity.ApiUrl);

        return v;
    }

    public void refreshList() {
        MainActivity ac = (MainActivity) getActivity();
        ac.reLoadPage();
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

//    private void hideViews() {
//        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
//
//        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabButton.getLayoutParams();
//        int fabBottomMargin = lp.bottomMargin;
//        mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
//    }
//
//    private void showViews() {
//        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
//        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
//    }

}
