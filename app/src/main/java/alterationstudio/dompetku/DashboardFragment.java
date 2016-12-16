package alterationstudio.dompetku;


import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    TextView saldoKamuText, greetingText, saldoText;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard,container,false);
        greetingText = (TextView) view.findViewById(R.id.greetingText);
        saldoText = (TextView) view.findViewById(R.id.saldo);
        saldoKamuText = (TextView) view.findViewById(R.id.saldoKamuText);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        /**
         * Greeting
         */
        String greetingStr = "Hai, "+sharedPref.getString("userName","");
        greetingText.setText(greetingStr);
        saldoKamuText.setText("Saldo Kamu Saat Ini");
        String s = (String.format("%,d", sharedPref.getInt("userSaldo",0))).replace(',', ' ');
        saldoText.setText("Rp"+s);

        return view;
    }

}
