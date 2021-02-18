package mesto.ridom.mestoridom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mesto.ridom.mestoridom.R;

public class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<String> states;
    public OnItemClickListener onItemClickListener;
    private Context context;

    public FloorAdapter(Context context, List<String> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public FloorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selector, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String state = states.get(position);
        holder.floorView.setText(state);
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final Button floorView;

        ViewHolder(View view) {
            super(view);
            floorView = view.findViewById(R.id.btn_floor);
            floorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
        }
    }
}
