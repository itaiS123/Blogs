package il.co.shivhit.blogs.ACTIVITIES;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import il.co.shivhit.blogs.R;
import il.co.shivhit.model.BlogPost;
import il.co.shivhit.helper.AlertUtil;
import il.co.shivhit.helper.DateUtil;
import il.co.shivhit.helper.inputValidators.DateRule;
import il.co.shivhit.helper.inputValidators.EntryValidation;
import il.co.shivhit.helper.inputValidators.NameRule;
import il.co.shivhit.helper.inputValidators.Rule;
import il.co.shivhit.helper.inputValidators.RuleOperation;
import il.co.shivhit.helper.inputValidators.TextRule;
import il.co.shivhit.helper.inputValidators.Validator;
import il.co.shivhit.viewmodel.BlogsViewModel;
import il.co.shivhit.viewmodel.GenericViewModelFactory;


public class BlogPostActivity extends BaseActivity implements EntryValidation {
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private ImageView iv1;
    private Button btn1;
    private Button btn2;
    private Calendar calendar;
    private BlogsViewModel blogsViewModel;
    private BlogPost oldBlogPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_post);
        initializeViews();
        getExtra();
        setObservers();
    }
    public void setObservers(){
        GenericViewModelFactory<BlogsViewModel> factory = new GenericViewModelFactory<>(getApplication(), BlogsViewModel::new);
        blogsViewModel = new ViewModelProvider(this, factory).get(BlogsViewModel.class);
        blogsViewModel.getSuccessOperation().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    Toast.makeText(BlogPostActivity.this, "Saved successfully !", Toast.LENGTH_SHORT).show();}  });
    }

    @Override
    protected void initializeViews() {
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        iv1 = findViewById(R.id.iv1);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);

        setListeners();
    }

    //Save Post btn
    @Override
    protected void setListeners() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogPost blogPost = new BlogPost();
                blogPost.setAuthor(et1.getText().toString());
                blogPost.setTitle(et2.getText().toString());
                blogPost.setDate(DateUtil.stringDateToLong(et3.getText().toString()));
                blogPost.setContent(et4.getText().toString());
                blogsViewModel.add(blogPost);

                Intent intent = new Intent();
                intent.putExtra("POST", blogPost);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        //Cancel Post btn
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        //Add date via Image View
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Select date");
                builder.setTextInputFormat(new SimpleDateFormat("dd/MM/yyyy"));

                // Limiting the date range to be selected
                    CalendarConstraints constraint = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    constraint = DateUtil.buidCalendarConstrains(LocalDate.now().minusDays(10), LocalDate.now());
                }
                builder.setCalendarConstraints(constraint);

                // If there is a date in the input box
                // The date will open on the registered date
                if (!et3.getText().toString().isEmpty())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDateTime date = LocalDate.parse(et3.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                        ZonedDateTime zdt = ZonedDateTime.of(date, ZoneId.systemDefault());
                        builder.setSelection(zdt.toInstant().toEpochMilli());
                    }

                MaterialDatePicker picker = builder.build();

                //Validate the date
                picker.addOnPositiveButtonClickListener(
                        new MaterialPickerOnPositiveButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick(Object selection) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    et3.setText(
                                            Instant.ofEpochMilli((long)selection)
                                                    .atZone(ZoneId.systemDefault())
                                                    .toLocalDate().format(
                                                            DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                                    et3.setError(null);
                                }
                                else
                                    et3.setText("ERROR !!!");
                            }
                        });

                        //Cancel the date
                        picker.addOnCancelListener(
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                AlertUtil.alertOk(BlogPostActivity.this,
                                        "Date",
                                        "Please enter post date",
                                        true,
                                        R.drawable.exclamation_mark);
                            }
                        });

                picker.setCancelable(true);
                picker.show(getSupportFragmentManager(), "DATE PICKER");
            }
        });
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra("POST")) {
                if (Build.VERSION.SDK_INT >=
                        Build.VERSION_CODES.TIRAMISU) {
                    oldBlogPost = getIntent().getSerializableExtra("POST", BlogPost.class);
                    if (oldBlogPost != null)
                        setData();
                }
            }
        }
    }

    private void setData(){
        et1.setText(oldBlogPost.getAuthor());
        et2.setText(oldBlogPost.getTitle());
        et4.setText(oldBlogPost.getContent());
        et3.setText(DateUtil.longDateToString(oldBlogPost.getDate()));
    }


    @Override
    public void setValidation() {

        Validator.add(new Rule(et1, RuleOperation.REQUIRED, "Please enter author name"));
        Validator.add(new NameRule(et1, RuleOperation.NAME, "Author name is wrong"));
        Validator.add(new Rule(et2, RuleOperation.REQUIRED, "Please enter title"));
        Validator.add(new TextRule(et2, RuleOperation.TEXT, "Title is wrong", 4, 50, true));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Validator.add(new DateRule(et3, RuleOperation.DATE, "Wrong date", LocalDate.now().minusDays(10), LocalDate.now()));
        };

        Validator.add(new Rule(et4, RuleOperation.REQUIRED, "Please enter content"));
        Validator.add(new TextRule(et4, RuleOperation.TEXT, "Wrong content", 10, 1000, true));
    }
    @Override
    public boolean validate() {
        return Validator.validate();
    }
}