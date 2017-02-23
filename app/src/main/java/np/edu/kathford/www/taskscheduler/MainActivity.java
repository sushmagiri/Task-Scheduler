package np.edu.kathford.www.taskscheduler;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, CompoundButton.OnCheckedChangeListener,
        SeekBar.OnSeekBarChangeListener {

    TextInputEditText etTitle;
    TextInputEditText etDescription;
    TextInputEditText etNote;

    ImageButton ibAttachFile;
    TextView tvStartDate;
    TextView tvStartTime;
    TextView tvEndDate;
    TextView tvEndTime;
    TextView tvProgressValue;

    TextView tvTotalDayMonth;
    TextView tvTotalHourMinute;

    SeekBar sbProgress;
    TextView tvStatusValue;
    SwitchCompat swStatus;

    View viewStartDate;
    View viewEndDate;

    private final int START_DATE = 1;
    private final int END_DATE = 2;
    int currentlyPicking = START_DATE;

    Calendar startDate;
    Calendar endDate;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar supportActionBar = getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        initializeFields();
        initializeViews();
    }

    private void updateProgressValue(int i) {
        tvProgressValue.setText(String.format("%s%%", String.valueOf((float) i / 100)));
    }

    private void initializeFields() {
        etTitle = (TextInputEditText) findViewById(R.id.et_title);
        etDescription = (TextInputEditText) findViewById(R.id.et_description);
        etNote = (TextInputEditText) findViewById(R.id.et_note);

        ibAttachFile = (ImageButton) findViewById(R.id.ib_attach_file);

        tvStartDate = (TextView) findViewById(R.id.txt_start_date);
        tvStartTime = (TextView) findViewById(R.id.txt_start_time);

        tvEndDate = (TextView) findViewById(R.id.txt_end_date);
        tvEndTime = (TextView) findViewById(R.id.txt_end_time);

        tvTotalDayMonth = (TextView) findViewById(R.id.txt_total_day_month);
        tvTotalHourMinute = (TextView) findViewById(R.id.txt_total_hour_minute);

        tvProgressValue = (TextView) findViewById(R.id.txt_progress_value);
        sbProgress = (SeekBar) findViewById(R.id.seek_progress);

        tvStatusValue = (TextView) findViewById(R.id.txt_status_value);
        swStatus = (SwitchCompat) findViewById(R.id.switch_status);
        swStatus.setChecked(false);
        tvStatusValue.setText(getString(R.string.closed));

        viewStartDate = findViewById(R.id.ll_start_date);
        viewEndDate = findViewById(R.id.ll_end_date);

        sbProgress.setOnSeekBarChangeListener(this);
        viewStartDate.setOnClickListener(this);
        viewEndDate.setOnClickListener(this);
        swStatus.setOnCheckedChangeListener(this);
    }

    private void initializeViews() {
        startDate = Calendar.getInstance();
        endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.DAY_OF_MONTH, 1);

        updateDateTime(tvStartDate, tvStartTime, startDate);
        updateDateTime(tvEndDate, tvEndTime, endDate);
        updateDifference();

        updateProgressValue(sbProgress.getProgress());
    }

    private void updateDifference() {
        Calendar dif = getDateDifference(startDate, endDate);

        int year = dif.get(Calendar.YEAR) - 1;
        int month = dif.get(Calendar.MONTH);
        int day = dif.get(Calendar.DAY_OF_MONTH);
        int hr = dif.get(Calendar.HOUR);
        int min = dif.get(Calendar.MINUTE);

        tvTotalDayMonth.setText(getString(R.string.total_day_month_format, year, month, day));
        tvTotalHourMinute.setText(getString(R.string.total_hour_minute_format, hr, min));
    }

    private void updateDateTime(TextView tvDate, TextView tvTime, Calendar date) {
        tvDate.setText(dateFormat.format(date.getTime()));
        tvTime.setText(timeFormat.format(date.getTime()));
    }

    private void showTimePickerDialog(Calendar defaultDate) {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this,
                defaultDate.get(Calendar.HOUR_OF_DAY), defaultDate.get(Calendar.MINUTE), true);
        timePickerDialog.enableSeconds(false);
        timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
    }

    private void showDatePickerDialog(Calendar defaultDate) {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this,
                defaultDate.get(Calendar.YEAR), defaultDate.get(Calendar.MONTH), defaultDate.get(Calendar.DATE));
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

    private Calendar getDateDifference(Calendar from, Calendar to) {
        long difference = to.getTimeInMillis() - from.getTimeInMillis();
        Calendar differenceCalender = Calendar.getInstance();
        differenceCalender.setTimeInMillis(difference);
        differenceCalender.add(Calendar.YEAR, -1969);
        differenceCalender.add(Calendar.DAY_OF_MONTH, -1);
        differenceCalender.add(Calendar.HOUR, -5);
        differenceCalender.add(Calendar.MINUTE, -30);
        return differenceCalender;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                Toast.makeText(this, "Task Submitted", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                Toast.makeText(this, "Task Closed", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_start_date:
                currentlyPicking = START_DATE;
                showDatePickerDialog(startDate);
                break;
            case R.id.ll_end_date:
                currentlyPicking = END_DATE;
                showDatePickerDialog(endDate);
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        switch (currentlyPicking) {
            case START_DATE:
                startDate.set(year, monthOfYear, dayOfMonth);
                showTimePickerDialog(startDate);
                break;
            case END_DATE:
                endDate.set(year, monthOfYear, dayOfMonth);
                showTimePickerDialog(endDate);
                break;
        }
        updateDifference();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        switch (currentlyPicking) {
            case START_DATE:
                startDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                startDate.set(Calendar.MINUTE, minute);
                updateDateTime(tvStartDate, tvStartTime, startDate);
                break;
            case END_DATE:
                endDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                endDate.set(Calendar.MINUTE, minute);
                updateDateTime(tvEndDate, tvEndTime, endDate);
                break;
        }
        updateDifference();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switch_status:
                if (b)
                    tvStatusValue.setText(getString(R.string.open));
                else
                    tvStatusValue.setText(getString(R.string.closed));
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.seek_progress:
                updateProgressValue(i);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
