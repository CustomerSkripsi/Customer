package mobi.garden.bottomnavigationtest.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobi.garden.bottomnavigationtest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryPending extends Fragment {


    public HistoryPending() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_pending, container, false);

        return view;
    }

}
