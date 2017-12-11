package finalproject.se.kmitl.findmythings.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import finalproject.se.kmitl.findmythings.R;

public class CreateNewPostActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private EditText postTitle;
    private Button btnNext;
    private String title;
    private String type;
    private String formatedDate;
    private DatePicker datePicker;
    private TextView tvTopic;
    private TextView tvDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);
        initInstance();
        setupTextView();
        setupSpinner();
    }


    private void initInstance() {
        tvTopic = findViewById(R.id.tvTopic);
        tvDate = findViewById(R.id.tvDate);
        spinner = findViewById(R.id.planets_spinner);
        postTitle = findViewById(R.id.etTitle);
        btnNext = findViewById(R.id.btnNext);
        datePicker = findViewById(R.id.datePicker);
        btnNext.setOnClickListener(this);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.things_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setupTextView() {
        if(getIntent().getStringExtra("fragmenttype").equals("find")){
            tvTopic.setText("สร้างหัวข้อหาสิ่งของ");
            tvDate.setText("วันที่ทำของหาย");
        }else if(getIntent().getStringExtra("fragmenttype").equals("found")){
            tvTopic.setText("สร้างหัวข้อเจอสิ่งของ");
            tvDate.setText("วันที่เจอสิ่งของ");

        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnNext) {
            String title = postTitle.getText().toString().trim();
            getDatePicker();
            Intent intent = new Intent(CreateNewPostActivity.this, NewPostActivity.class);
            intent.putExtra("fragmenttype", getIntent().getStringExtra("fragmenttype"));
            intent.putExtra("title", title);
            intent.putExtra("type", type);
            intent.putExtra("date", formatedDate);
            startActivity(intent);
        }
    }

    private void getDatePicker() {
        int   day  = datePicker.getDayOfMonth();
        int   month= datePicker.getMonth();
        int   year = datePicker.getYear()-1900;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        formatedDate = sdf.format(new Date(year, month, day));

        //You can parse the String back to Date object by calling
        //Date date = sdf.parse(formatedDate);
    }

    //เมื่อกดเลือกSpinner ประเภทสิ่งของที่หาย
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getItemAtPosition(i).toString().equals("มือถือ")) {
            type = "mobile";
        } else if (adapterView.getItemAtPosition(i).toString().equals("กุญแจ")) {
            type = "key";
        } else if (adapterView.getItemAtPosition(i).toString().equals("บัตรนักศึกษา")) {
            type = "student card";
        } else if (adapterView.getItemAtPosition(i).toString().equals("ิกระเป๋า")) {
            type = "bag";
        } else {
            type = "other";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
