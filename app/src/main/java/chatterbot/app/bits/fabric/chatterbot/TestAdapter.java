package chatterbot.app.bits.fabric.chatterbot;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<TestItem> data;

    public final int LEFT = 0;
    public final int RIGHT = 1;
    public final int CARD = 2;

    TestAdapter() {
        data = TestItem.getData();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (getItemViewType(i)) {
            case LEFT:
                return new ViewHolderLEFT(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_cardview, viewGroup, false));
            case RIGHT:
                return new ViewHolderRIGHT(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_text_out, viewGroup, false));
            default:
                return new ViewHolderCARD(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_text_in, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder instanceof ViewHolderCARD){
            ((ViewHolderCARD) viewHolder).bind(data.get(i));
        }
        else if(viewHolder instanceof ViewHolderRIGHT){
            ((ViewHolderRIGHT) viewHolder).bind(data.get(i));
        }
        else if(viewHolder instanceof ViewHolderLEFT){
            ((ViewHolderLEFT) viewHolder).bind(data.get(i));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolderLEFT extends RecyclerView.ViewHolder {
        TextView body;
        TextView time;
        ImageView picture;

        public ViewHolderLEFT(@NonNull View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.textView);
        }

        public void bind(TestItem testItem) {
//            body.setText(testItem.body);
        }
    }

    public class ViewHolderRIGHT extends RecyclerView.ViewHolder {
        TextView body;
        TextView time;

        public ViewHolderRIGHT(@NonNull View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.textView);
        }

        public void bind(TestItem testItem) {
           // body.setText(testItem.body);
        }
    }

    public class ViewHolderCARD extends RecyclerView.ViewHolder {
        TextView body;
        TextView time;

        public ViewHolderCARD(@NonNull View itemView) {
            super(itemView);
           // body = itemView.findViewById(R.id.textView);
        }

        public void bind(TestItem testItem) {
            body.setText(testItem.body);
        }
    }
}
