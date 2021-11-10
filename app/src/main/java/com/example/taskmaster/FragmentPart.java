package com.example.taskmaster;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPart extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "body";
    private static final String ARG_PARAM3 = "state";

    // TODO: Rename and change types of parameters
    private String Title;
    private String Body;
    private String State;


    public FragmentPart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Title Parameter 1.
     * @param Body Parameter 2.
     * @param State Parameter 2.
     * @return A new instance of fragment FragmentPart.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPart newInstance(String Title, String Body, String State) {
        FragmentPart fragment = new FragmentPart();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, Title);
        args.putString(ARG_PARAM2, Body);
        args.putString(ARG_PARAM3, State);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Title = getArguments().getString(ARG_PARAM1);
            Body = getArguments().getString(ARG_PARAM2);
            State = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_part, container, false);
    }
}