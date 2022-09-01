package com.example.family;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.PersistableBundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    private List<CostBean> mCostBeanList;
    private DatabaseHelper mDatabaseHelper;
    private CostListAdapter mAdapter;
    private TextView costTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDatabaseHelper = new DatabaseHelper(this);
        mCostBeanList = new ArrayList<>();
        ListView costList = (ListView) findViewById(R.id.lv_main);
        initCostData();
        System.out.println("**********"+mCostBeanList);
        mAdapter =new CostListAdapter(this,mCostBeanList);
        costList.setAdapter(mAdapter);

        Button btn_add_in =findViewById(R.id.btn_add_in);
       btn_add_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
                View viewDialog=inflater.inflate(R.layout.new_cost_data,null);
                final EditText title=(EditText)viewDialog.findViewById(R.id.et_cost_title);
                final EditText money=(EditText)viewDialog.findViewById(R.id.et_cost_money);
                final DatePicker date=(DatePicker) viewDialog.findViewById(R.id.dp_cost_date);
                builder.setView(viewDialog);
                builder.setTitle("添加收入");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final CostBean costBean=new CostBean();
                        costBean.costTitle=title.getText().toString();
                        costBean.costDate=date.getYear() + "-" + (date.getMonth()+1) + "-" +
                                date.getDayOfMonth();
                        costBean.costMoney="+"+money.getText().toString();
                        mDatabaseHelper.insertCost(costBean);
                        mCostBeanList.add(costBean);
                        mAdapter.notifyDataSetChanged();
                        String string_total_cost="账户余额:"+mDatabaseHelper.countTotalCost();
                        costTotal =findViewById(R.id.tv_cost_total);
                        costTotal.setText(string_total_cost);
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });

        /**
         * 支出
         */
        Button btn_add_out =findViewById(R.id.btn_add_out);
        btn_add_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
                View viewDialog=inflater.inflate(R.layout.new_cost_data,null);
                final EditText title=(EditText)viewDialog.findViewById(R.id.et_cost_title);
                final EditText money=(EditText)viewDialog.findViewById(R.id.et_cost_money);
                final DatePicker date=(DatePicker) viewDialog.findViewById(R.id.dp_cost_date);
                builder.setView(viewDialog);
                builder.setTitle("添加支出");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final CostBean costBean=new CostBean();
                        costBean.costTitle=title.getText().toString();
                        costBean.costDate=date.getYear() + "-" + (date.getMonth()+1) + "-" +
                                date.getDayOfMonth();
                        costBean.costMoney="-"+money.getText().toString();
                        mDatabaseHelper.insertCost(costBean);
                        mCostBeanList.add(costBean);
                        mAdapter.notifyDataSetChanged();
                        String string_total_cost="账户余额:"+mDatabaseHelper.countTotalCost();
                        costTotal =findViewById(R.id.tv_cost_total);
                        costTotal.setText(string_total_cost);
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });

        /*
        删除
         */
        Button btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
                View viewDialog=inflater.inflate(R.layout.delete_cost_data,null);
                final EditText title=(EditText)viewDialog.findViewById(R.id.et_cost_title);
                builder.setView(viewDialog);
                builder.setTitle("删除账单");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CostBean costBean =new CostBean();
                        costBean.costTitle=title.getText().toString();
                        mDatabaseHelper.deleteCost(costBean);
                        mCostBeanList.clear();
                        initCostData();
                        mAdapter.notifyDataSetChanged();
                        String string_total_cost="账户余额:"+mDatabaseHelper.countTotalCost();
                        costTotal =findViewById(R.id.tv_cost_total);
                        costTotal.setText(string_total_cost);
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });

        /*
        清除账单
         */
        Button btn_delete_all =findViewById(R.id.btn_delete_all);
        btn_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabaseHelper.deleteAllData();
                        mCostBeanList.clear();
                        mAdapter.notifyDataSetChanged();
                        String string_total_cost="账户余额:"+mDatabaseHelper.countTotalCost();
                        costTotal =findViewById(R.id.tv_cost_total);
                        costTotal.setText(string_total_cost);
                    }
                });
               builder.setNegativeButton("取消",null);
               builder.setMessage("确定要清除所有账单吗？");
               builder.setTitle("提示");
               builder.create().show();
            }
        });

        /*
        计算余额
         */

    }

    /*
    初始化表单
     */
    private void initCostData() {
        Cursor cursor=mDatabaseHelper.getAllCostData();
        if(cursor!=null){
            while(cursor.moveToNext()){
                CostBean costBean=new CostBean();
                costBean.costTitle=cursor.getString(cursor.getColumnIndex("cost_title"));
                costBean.costDate=cursor.getString(cursor.getColumnIndex("cost_date"));
                costBean.costMoney=cursor.getString(cursor.getColumnIndex("cost_money"));
                mCostBeanList.add(costBean);
                String string_total_cost="账户余额:"+mDatabaseHelper.countTotalCost();
                costTotal =findViewById(R.id.tv_cost_total);
                costTotal.setText(string_total_cost);
            }
            cursor.close();
        }
    }
    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_chart) {
            Intent intent=new Intent(MainActivity.this,ChartsActivity.class);
            intent.putExtra("cost_list", (Serializable) mCostBeanList);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
