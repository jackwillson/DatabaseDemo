package com.example.thread.databasepract;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.thread.databasepract.adapters.EmployeeListingAdapter;
import com.example.thread.databasepract.adapters.OnRowClicked;
import com.example.thread.databasepract.databinding.ActivityEmployeeListBinding;
import com.example.thread.databasepract.models.EmployeeModel;
import com.example.thread.databasepract.tables.TBL_Employee;

import java.util.ArrayList;

public class EmployeeList extends BaseActivity {

    private SQLiteDatabase empDatabase;

    private ArrayList<EmployeeModel> employeeModels = new ArrayList<>();
    private ActivityEmployeeListBinding activityEmployeeBinding = null;
    private EmployeeListingAdapter employeeListingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        empDatabase = baseDB.getWritableDatabase();

        activityEmployeeBinding = DataBindingUtil.setContentView(this, R.layout.activity_employee_list);


        employeeListingAdapter = new EmployeeListingAdapter(employeeModels, new OnRowClicked() {
            @Override
            public void onRowClicked(int pos) {
                Intent intent = new Intent(EmployeeList.this, AddUpdateData.class);
                intent.putExtra("forEdit", true);
                intent.putExtra("row", employeeModels.get(pos));
                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        activityEmployeeBinding.recyclerView.setLayoutManager(mLayoutManager);

        activityEmployeeBinding.recyclerView.setAdapter(employeeListingAdapter);
        activityEmployeeBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ids = "";
                for (int i = employeeModels.size() - 1; i >= 0; i--) {
                    if (employeeModels.get(i).isChecked()) {
                        if (ids.length() == 0) {
                            ids = employeeModels.get(i).geteNo();
                        } else {
                            ids += ", " + employeeModels.get(i).geteNo();
                        }
                        employeeModels.remove(i);
                    }
                }
                employeeListingAdapter.notifyDataSetChanged();
                empDatabase.execSQL(String.format("DELETE FROM " + TBL_Employee.TABLE_NAME + " WHERE " + TBL_Employee.EMP_ID + " IN (%s);", ids));
//                empDatabase.delete(TBL_Employee.TABLE_NAME ,TBL_Employee.EMP_ID+" IN (?, ?, ? )", new String[]{ids});
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        employeeModels.clear();
        employeeListingAdapter.notifyDataSetChanged();

        Cursor cursor = empDatabase.rawQuery("SELECT *FROM " + TBL_Employee.TABLE_NAME, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                EmployeeModel employeeModel = new EmployeeModel();
                employeeModel.seteNo(String.valueOf(cursor.getInt(cursor.getColumnIndex(TBL_Employee.EMP_ID))));
                employeeModel.seteName(cursor.getString(cursor.getColumnIndex(TBL_Employee.EMP_NAME)));
                employeeModel.seteDesignation(cursor.getString(cursor.getColumnIndex(TBL_Employee.EMP_DESIGNATION)));

                employeeModels.add(employeeModel);
            } while (cursor.moveToNext());
            employeeListingAdapter.notifyDataSetChanged();
        }
    }
}
