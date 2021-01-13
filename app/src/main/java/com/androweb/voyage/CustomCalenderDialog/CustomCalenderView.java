package com.androweb.voyage.CustomCalenderDialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androweb.voyage.R;
import com.androweb.voyage.utils.ExpendableGridView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.List;
import java.util.Locale;

public class CustomCalenderView extends LinearLayout {
    private static final String TAG = CustomCalenderView.class.getSimpleName();

    // Components for Calender View
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView currentDate;

    public ExpendableGridView expendableGridView;
    private static final int MAX_CALENDER_COLUMNS = 42;
    private SimpleDateFormat format = new SimpleDateFormat("MMMM,yyyy", Locale.getDefault());
    private Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private List<EventObject> eventObjects = new ArrayList<>();

    public CustomCalenderView(Context context, List<EventObject> eventObjects) {
        super(context);
        this.eventObjects = eventObjects;
        this.context = context;

        // Initializing
        initializeUILayout();
        setCalenderAdapter();
        setPreviousBtnClick();
        setNextBtnClick();
        setGridClickEvents();
        setCurrentDateEvents();
    }

    public CustomCalenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        // Initializing
        initializeUILayout();
        setCalenderAdapter();
        setPreviousBtnClick();
        setNextBtnClick();
        setGridClickEvents();
        setCurrentDateEvents();
    }

    public CustomCalenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * UI Function
     */
    private void initializeUILayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_calendar, this);

        btnPrev = view.findViewById(R.id.calendar_prev_month);
        btnNext = view.findViewById(R.id.calendar_next_month);
        currentDate = view.findViewById(R.id.calendar_current_date);
        expendableGridView = view.findViewById(R.id.calendar_grid);
        expendableGridView.setExpandable(true);
    }

    /**
     * Calender Adapter Function
     */
    private void setCalenderAdapter() {
    }

    /**
     * Previous Button Function
     */
    private void setPreviousBtnClick() {
        btnPrev.setOnClickListener(v -> CustomCalenderView.this.previousMonths());
    }

    /**
     * Next Button Function
     */
    private void setNextBtnClick() {
        btnNext.setOnClickListener(v -> CustomCalenderView.this.nextMonth());
    }

    /**
     * Grid Click Function
     */
    private String setGridClickEvents() {
        String[] text = new String[1];
        return text[0];
    }

    /**
     * Current Date Function
     */
    private void setCurrentDateEvents() {
        currentDate.setOnClickListener(v -> {
            final Calendar currentDate = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(year, month, day);
                    setCalenderAdapter();
                }
            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
            datePickerDialog.show();
        });
    }

    /**
     * Return Previous Month
     */
    private void previousMonths() {
        calendar.add(Calendar.MONTH, -1);
        setCalenderAdapter();
    }

    /**
     * Return Next month
     */
    private void nextMonth() {
        calendar.add(Calendar.MONTH, 1);
        setCalenderAdapter();
    }
}
