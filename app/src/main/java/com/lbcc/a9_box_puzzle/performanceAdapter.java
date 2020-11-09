package com.lbcc.a9_box_puzzle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class performanceAdapter extends ArrayAdapter<performanceDetails> {

    public performanceAdapter(Activity context ,ArrayList<performanceDetails> word){
        super(context,0,word);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.performancelistview,parent,false);
        }

        performanceDetails cur = getItem(position);

        String playername , totalwins, totalscore,totalgamesplayes,rank;
        playername = cur.mPlayerName;
        totalwins = Long.toString(cur.getmTotalWins());
        totalscore = Long.toString(cur.getmTotalScore());
        totalgamesplayes =  Long.toString(cur.getmTotalGamesPlayed());
        rank = Integer.toString(position+1);

        TextView Trank = view.findViewById(R.id.rank);
        Trank.setText(rank);

        TextView name = view.findViewById(R.id.playername);
        name.setText(playername.replaceAll("\\s", "\n"));

        TextView Ttotalwins = view.findViewById(R.id.totalwins);
        Ttotalwins.setText(totalwins);

        TextView Ttotalscore = view.findViewById(R.id.totalscore);
        Ttotalscore.setText(totalscore);

        TextView Ttotalgamesplayed = view.findViewById(R.id.gamesplayed);
        Ttotalgamesplayed.setText(totalgamesplayes);

        return view;
    }
}
