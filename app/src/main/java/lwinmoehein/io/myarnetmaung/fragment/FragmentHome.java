package lwinmoehein.io.myarnetmaung.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import lwinmoehein.io.myarnetmaung.R;
import lwinmoehein.io.myarnetmaung.acitivity.ReminderActivity;
import lwinmoehein.io.myarnetmaung.Singleton.CurrentUser;
import lwinmoehein.io.myarnetmaung.Singleton.References;
import lwinmoehein.io.myarnetmaung.acitivity.PrivacyActivity;
import lwinmoehein.io.myarnetmaung.model.AnniDate;
import lwinmoehein.io.myarnetmaung.model.Lover;

public class FragmentHome extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    TextView textViewNextBirthdayMonths;
    TextView textViewNextBirthdayDays;
    TextView textViewFinalYears;
    TextView textViewFinalMonths;
    TextView textViewFinalDays;
    TextView textViewCurrentDay;
    TextView textViewCalculate;
    TextView textViewClear;

    ImageView imageViewCalenderFirst;
    ImageView imageViewCalenderSecond;


    EditText editTextBirthDay;
    EditText editTextBirthMonth;
    EditText editTextBirthYear;
    EditText editTextCurrentDay;
    EditText editTextCurrentMonth;
    EditText editTextCurrentYear;

    int anniday,annimonth,anniyear;
    String rsid="";

    LinearLayout noanni;

    CardView btnReminder,btnPrivacy;




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize
        editTextBirthDay=view.findViewById(R.id.editTextBirthDay);
        editTextBirthMonth=view.findViewById(R.id.editTextBirthMonth);
        editTextBirthYear=view.findViewById(R.id.editTextBirthYear);
        editTextCurrentDay=view.findViewById(R.id.editTextCurrentDay);
        editTextCurrentMonth=view.findViewById(R.id.editTextCurrentMonth);
        editTextCurrentYear=view.findViewById(R.id.editTextCurrentYear);

        imageViewCalenderFirst=view.findViewById(R.id.imageViewCalenderFirst);
        imageViewCalenderSecond=view.findViewById(R.id.imageViewCalenderSecond);

        textViewNextBirthdayMonths=view.findViewById(R.id.textViewNextBirthdayMonths);
        textViewNextBirthdayDays=view.findViewById(R.id.textViewNextBirthdayDays);
        textViewFinalDays=view.findViewById(R.id.textViewFinalDays);
        textViewFinalMonths=view.findViewById(R.id.textViewFinalMonths);
        textViewFinalYears=view.findViewById(R.id.textViewFinalYears);

        textViewCurrentDay=view.findViewById(R.id.textViewCurrentDay);

        noanni=view.findViewById(R.id.no_anni);


        btnReminder=view.findViewById(R.id.cardview_reminders);
        btnPrivacy=view.findViewById(R.id.cardview_privacy);

        //end initialize
        setupCurrentDate(); // setup today's date


        imageViewCalenderSecond.setOnClickListener(this);
        imageViewCalenderFirst.setOnClickListener(this);


        References.loverDatabaseRef.child(CurrentUser.currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Lover lover=dataSnapshot.getValue(Lover.class);
                    if(lover.getRsid()!=null){
                        noanni.setVisibility(View.GONE);
                        rsid=lover.getRsid();
                        if(!(rsid.equals(""))) {
                            References.anniDate.child(rsid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    AnniDate anniDate = dataSnapshot.getValue(AnniDate.class);
                                    if(anniDate!=null) {
                                        editTextBirthDay.setText(addZero(anniDate.getDay()));
                                        editTextBirthMonth.setText(addZero(anniDate.getMonth()));
                                        editTextBirthYear.setText(String.valueOf(anniDate.getYear()));
                                        calculateAge();
                                        nextBirthday();
                                        noanni.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }else {
                        noanni.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //card listeners
        btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(rsid.equals(""))){
                Intent intent=new Intent(getActivity(), PrivacyActivity.class);
                intent.putExtra("rsid",rsid);
                startActivity(intent);}else {
                    Toast.makeText(getActivity(),"Please wait",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(rsid.equals(""))){
                    Intent intent=new Intent(getActivity(), ReminderActivity.class);
                    intent.putExtra("rsid",rsid);
                    startActivity(intent);}else {
                    Toast.makeText(getActivity(),"Please wait",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);

    }



    private void setupCurrentDate() {
        final Calendar c = Calendar.getInstance();
        editTextCurrentYear.setText(String.valueOf(c.get(Calendar.YEAR)));
        editTextCurrentMonth.setText(addZero(c.get(Calendar.MONTH) + 1));
        editTextCurrentDay.setText(addZero(c.get(Calendar.DAY_OF_MONTH)));

        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date date = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) - 1);
        String dayOfWeek = simpledateformat.format(date);
        textViewCurrentDay.setText(dayOfWeek);
        textViewCurrentDay.setVisibility(View.VISIBLE);


    }

    private String addZero(int number) {
        String n;
        if (number < 10) {
            n = "0" + number;
        } else {
            n = String.valueOf(number);
        }
        return n;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageViewCalenderSecond) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            Calendar now = Calendar.getInstance();

            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    FragmentHome.this,
                    now.get(Calendar.YEAR), // Initial year selection
                    now.get(Calendar.MONTH), // Initial month selection
                    now.get(Calendar.DAY_OF_MONTH) // Inital day selection
            );
            dpd.show(getFragmentManager(), "Datepickerdialog");

        } else if (view == textViewCalculate) {
            if (!TextUtils.isEmpty(editTextBirthDay.getText()) && !TextUtils.isEmpty(editTextBirthMonth.getText()) && !TextUtils.isEmpty(editTextBirthYear.getText())) {
                calculateAge();
                nextBirthday();
            } else {
                Toasty.warning(getActivity(), "All fields are required", Toast.LENGTH_SHORT, true).show();
            }
        } else if (view == textViewClear) {
            editTextBirthDay.setText("");
            editTextBirthMonth.setText("");
            editTextBirthYear.setText("");
            Toasty.success(getActivity(), "Successfully reset", Toast.LENGTH_SHORT, true).show();
        }
    }



    private void nextBirthday() {
        int currentDay = Integer.valueOf(editTextCurrentDay.getText().toString());
        int currentMonth = Integer.valueOf(editTextCurrentMonth.getText().toString());
        int currentYear = Integer.valueOf(editTextCurrentYear.getText().toString());

        Calendar current = Calendar.getInstance();
        current.set(currentYear, currentMonth, currentDay);

        int birthDay = Integer.valueOf(editTextBirthDay.getText().toString());
        int birthMonth = Integer.valueOf(editTextBirthMonth.getText().toString());
        int birthYear = Integer.valueOf(editTextBirthYear.getText().toString());

        Calendar birthday = Calendar.getInstance();
        birthday.set(birthYear, birthMonth, birthDay);

        long difference = birthday.getTimeInMillis() - current.getTimeInMillis();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(difference);

        textViewNextBirthdayMonths.setText(String.valueOf(cal.get(Calendar.MONTH)));
        textViewNextBirthdayDays.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
    }

    private void calculateAge() {
        int currentDay = Integer.valueOf(editTextCurrentDay.getText().toString());
        int currentMonth = Integer.valueOf(editTextCurrentMonth.getText().toString());
        int currentYear = Integer.valueOf(editTextCurrentYear.getText().toString());

        Date now = new Date(currentYear, currentMonth, currentDay);

        int birthDay = Integer.valueOf(editTextBirthDay.getText().toString());
        int birthMonth = Integer.valueOf(editTextBirthMonth.getText().toString());
        int birthYear = Integer.valueOf(editTextBirthYear.getText().toString());

        Date dob = new Date(birthYear, birthMonth, birthDay);

        if (dob.after(now)) {
            Toasty.error(getActivity(), "Birthday can't in future", Toast.LENGTH_SHORT, true).show();
            return;
        }
        // days of every month
        int month[] = {31, 28, 31, 30, 31, 30, 31,
                31, 30, 31, 30, 31};

        // if birth date is greater then current birth
        // month then do not count this month and add 30
        // to the date so as to subtract the date and
        // get the remaining days
        if (birthDay > currentDay) {
            currentDay = currentDay + month[birthMonth - 1];
            currentMonth = currentMonth - 1;
        }

        // if birth month exceeds current month, then do
        // not count this year and add 12 to the month so
        // that we can subtract and find out the difference
        if (birthMonth > currentMonth) {
            currentYear = currentYear - 1;
            currentMonth = currentMonth + 12;
        }

        // calculate date, month, year
        int calculated_date = currentDay - birthDay;
        int calculated_month = currentMonth - birthMonth;
        int calculated_year = currentYear - birthYear;

        textViewFinalDays.setText(String.valueOf(calculated_date));
        textViewFinalMonths.setText(String.valueOf(calculated_month));
        textViewFinalYears.setText(String.valueOf(calculated_year));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        References.anniDate.child(rsid).setValue(new AnniDate(dayOfMonth,Integer.parseInt(addZero(monthOfYear+1)),year)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                calculateAge();
                nextBirthday();
            }
        });
    }
}