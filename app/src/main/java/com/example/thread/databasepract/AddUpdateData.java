package com.example.thread.databasepract;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.databinding.tool.DataBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.thread.databasepract.database.BaseDB;
import com.example.thread.databasepract.databinding.ActivityAddUpdateDataBinding;
import com.example.thread.databasepract.models.EmployeeModel;
import com.example.thread.databasepract.tables.TBL_Employee;

import java.util.ArrayList;

public class AddUpdateData extends BaseActivity {


    private boolean forEdit = false;
    private SQLiteDatabase empDatabase=null;
    private ActivityAddUpdateDataBinding activityAddUpdateDataBinding = null;
    private EmployeeModel employeeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddUpdateDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_update_data);
        forEdit = getIntent().getBooleanExtra("forEdit", false);

        if (forEdit) {
            activityAddUpdateDataBinding.btnAdd.setText(R.string.update);
            activityAddUpdateDataBinding.btnDelete.setText(R.string.delete);

            activityAddUpdateDataBinding.edtEmpNo.setEnabled(false);
            employeeModel = (EmployeeModel) getIntent().getSerializableExtra("row");

            activityAddUpdateDataBinding.edtEmpDesignation.setText(employeeModel.geteDesignation());
            activityAddUpdateDataBinding.edtEmpName.setText(employeeModel.geteName());
            activityAddUpdateDataBinding.edtEmpNo.setText(employeeModel.geteNo());

        } else {
            activityAddUpdateDataBinding.btnAdd.setText(R.string.add);
            activityAddUpdateDataBinding.btnDelete.setText(R.string.show_list);
        }
//        setContentView(R.layout.activity_add_update_data);

        empDatabase = baseDB.getWritableDatabase();

        activityAddUpdateDataBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    String eNo = activityAddUpdateDataBinding.edtEmpNo.getText().toString();
                    String eName = activityAddUpdateDataBinding.edtEmpName.getText().toString();
                    String eDesignation = activityAddUpdateDataBinding.edtEmpDesignation.getText().toString();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(TBL_Employee.EMP_ID, Integer.parseInt(eNo));
                    contentValues.put(TBL_Employee.EMP_NAME, eName);
                    contentValues.put(TBL_Employee.EMP_DESIGNATION, eDesignation);

                    if (forEdit) {
                        int rowEffected = empDatabase.update(TBL_Employee.TABLE_NAME, contentValues, TBL_Employee.EMP_ID + " = ?", new String[]{eNo});
                        Toast.makeText(AddUpdateData.this, "Row Effected=" + rowEffected, Toast.LENGTH_LONG).show();
                    } else {
                        if (empDatabase.insert(TBL_Employee.TABLE_NAME, null, contentValues) > 0) {
                            Toast.makeText(AddUpdateData.this, "Saved", Toast.LENGTH_LONG).show();

                            Intent intentListing = new Intent(AddUpdateData.this, EmployeeList.class);
                            startActivity(intentListing);

                        } else {
                            Toast.makeText(AddUpdateData.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(AddUpdateData.this, "Input error", Toast.LENGTH_LONG).show();
                }
            }
        });
        activityAddUpdateDataBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (forEdit) {
                        //Delete
                        empDatabase.execSQL(String.format("DELETE FROM " + TBL_Employee.TABLE_NAME
                                + " WHERE " + TBL_Employee.EMP_ID
                                + " IN (%s);", employeeModel.geteNo()));
                        Toast.makeText(AddUpdateData.this, "Record removed!!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Intent intentListing = new Intent(AddUpdateData.this, EmployeeList.class);
                        startActivity(intentListing);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isValid() {
        String eNo = activityAddUpdateDataBinding.edtEmpNo.getText().toString();
        String eName = activityAddUpdateDataBinding.edtEmpName.getText().toString();
        String eDesignation = activityAddUpdateDataBinding.edtEmpDesignation.getText().toString();

        return (eNo.trim().length() > 0 && eName.trim().length() > 0 && eDesignation.trim().length() > 0);
    }
}
