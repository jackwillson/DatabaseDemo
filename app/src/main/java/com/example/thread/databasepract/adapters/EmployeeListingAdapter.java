package com.example.thread.databasepract.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thread.databasepract.R;
import com.example.thread.databasepract.models.EmployeeModel;

import java.util.List;

public class EmployeeListingAdapter extends RecyclerView.Adapter<EmployeeListingAdapter.EmployeeHolder> {

    private List<EmployeeModel> employeeModels;
    private OnRowClicked onRowClicked;

    public class EmployeeHolder extends RecyclerView.ViewHolder {
        public TextView tvENo, tvEDesignation, tvEName;
        private CheckBox chkRow;
        private LinearLayout linRowItem;

        public EmployeeHolder(View view) {
            super(view);
            linRowItem = (LinearLayout) view.findViewById(R.id.linRowItem);
            chkRow = (CheckBox) view.findViewById(R.id.chkRow);
            tvENo = (TextView) view.findViewById(R.id.tvENo);
            tvEName = (TextView) view.findViewById(R.id.tvEName);
            tvEDesignation = (TextView) view.findViewById(R.id.tvEDesignation);
        }
    }


    public EmployeeListingAdapter(List<EmployeeModel> employeeModels,OnRowClicked onRowClicked) {
        this.employeeModels = employeeModels;
        this.onRowClicked = onRowClicked;
    }

    @Override
    public EmployeeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_item, parent, false);

        return new EmployeeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EmployeeHolder holder, final int position) {
        final EmployeeModel movie = employeeModels.get(position);

        holder.chkRow.setChecked(employeeModels.get(position).isChecked());
        holder.tvENo.setText(movie.geteNo());
        holder.tvEName.setText(movie.geteName());
        holder.tvEDesignation.setText(movie.geteDesignation());

        holder.linRowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowClicked.onRowClicked(position);
            }
        });
        holder.chkRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employeeModels.get(position).setChecked(!employeeModels.get(position).isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeModels.size();
    }
}