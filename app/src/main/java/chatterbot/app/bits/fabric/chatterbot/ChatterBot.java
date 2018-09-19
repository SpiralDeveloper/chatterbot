package chatterbot.app.bits.fabric.chatterbot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.maxwell.speechrecognition.OnSpeechRecognitionListener;
import com.maxwell.speechrecognition.OnSpeechRecognitionPermissionListener;
import com.maxwell.speechrecognition.RecognitionProgressView;
import com.maxwell.speechrecognition.SpeechRecognition;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bits.fabric.chatterbot.SublimePickerFragment;
import bits.fabric.chatterbot.Time;
import fabric.bits.api.rdk.RdkManager;
import fabric.bits.api.rdk.annotations.RClick;
import fabric.bits.api.rdk.annotations.RDefine;
import fabric.bits.api.rdk.annotations.RTextWatcher;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ChatterBot extends Fragment {

    @RDefine(R.id.recyclerView)
    RecyclerView recyclerView;

    @RDefine(R.id.imageView2)
    ImageView actionInput;

    @RDefine(R.id.imageView3)
    ImageView emojiButton;

    @RDefine(R.id.editText)
    EmojiconEditText editText;

    @RDefine(R.id.footer)
    ViewFlipper viewFlipper;

    @RDefine(R.id.buttonsContainer)
    LinearLayout buttonsContainer;

    @RDefine(R.id.recognition_view)
    RecognitionProgressView recognition_view;




    private SpeechRecognition speechRecognition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chatterbot_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RdkManager.bind(this, view);
        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setStackFromEnd(true);
        recyclerView.setLayoutManager(ll);
        editText.addTextChangedListener(searchTextWatcher);
        init();
    }

    public void setRecyclerViewAdapter(RecyclerView.Adapter adapter){
        recyclerView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }


    @RClick(R.id.imageView2)
    void onInputClick() {
        if (editText.getText().toString().trim().isEmpty()) {
            recognition_view.play();
            speechRecognition.startSpeechRecognition();
            viewFlipper.setDisplayedChild(1);
        } else {
            String text = editText.getText().toString();
            onFormattedInputSubmit(wrap(text, null, null, null));
            editText.setText(null);
        }
    }

    @RTextWatcher(R.id.editText)
    void onInputChange(EditText editText, String text) {
        if (text.trim().isEmpty()) {
            actionInput.setImageResource(R.drawable.microphone);
        } else {
            actionInput.setImageResource(R.drawable.send);
        }
    }


    public void onFormattedInputSubmit(String text) {

    }


    public void onStandardButtonClick(StandardButtons button) {

    }


    @RClick(R.id.imageView4)
    void keyBtn(){
        viewFlipper.setDisplayedChild(0);
        recognition_view.stop();
        speechRecognition.stopSpeechRecognition();
    }

    public void putButtons(List<StandardButtons> buttons) {
        if (buttons != null) {
            clearButtons();
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            for (final StandardButtons button : buttons) {
                View parent = inflater.inflate(R.layout.simple_button, null);
                Button realBtn = parent.findViewById(R.id.button);
                realBtn.setText(button.text);
                realBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (button.input != null) {
                            clearButtons();
                            onFormattedInputSubmit(button.input);
                        } else if (button.slot != null) {
                            switch (button.slot) {
                                case "date":
                                    break;
                                case "datetime":
                                    break;
                                case "duration":
                                    break;
                                default:
                                    onStandardButtonClick(button);
                            }
                        } else {
                            onStandardButtonClick(button);
                        }
                    }
                });
                buttonsContainer.addView(parent);
            }
        }
    }

    private void pickDate() {
        showDatePicker(false);
    }

    private void pickDateTime() {
        showDatePicker(true);
    }

    private void pickDuration() {

    }


    public void clearButtons() {
        buttonsContainer.removeAllViews();
    }


    public String wrap(String body, Object value, String slot, String terminal) {
        if (terminal != null)
            return String.format("<input><body>%s<body><terminal name=\"%s\" /></input>", body, terminal);
        else if (value != null && slot != null)
            return String.format("<input><body>%s<body><value slot=\"%s\" string=\"%s\"/></input>", body, slot, String.valueOf(value));
        else
            return String.format("<input><body>%s<body></input>", body);
    }


    private void init() {
        int[] colors = {
                ContextCompat.getColor(getActivity(), R.color.color1),
                ContextCompat.getColor(getActivity(), R.color.color2),
                ContextCompat.getColor(getActivity(), R.color.color3),
                ContextCompat.getColor(getActivity(), R.color.color4),
                ContextCompat.getColor(getActivity(), R.color.color5)
        };
        recognition_view.setColors(colors);
        recognition_view.setBarMaxHeightsInDp( new int[]{20, 30, 20, 30, 20});
        recognition_view.setCircleRadiusInDp(3);


        EmojIconActions  emojIcon=new EmojIconActions(getActivity(),getView(),editText,
                emojiButton,
                "#495C66",
                "#DCE1E2",
                "#E6EBEF");
        emojIcon.setIconsIds(R.drawable.keyboard,R.drawable.smile);
        emojIcon.setUseSystemEmoji(true);
        emojIcon.ShowEmojIcon();

        speechRecognition = new SpeechRecognition(getActivity(), recognition_view);
        speechRecognition.setSpeechRecognitionPermissionListener(new OnSpeechRecognitionPermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied() {

            }
        });
        speechRecognition.setSpeechRecognitionListener(new OnSpeechRecognitionListener() {
            @Override
            public void OnSpeechRecognitionStarted() {
            }

            @Override
            public void OnSpeechRecognitionStopped() {

            }

            @Override
            public void OnSpeechRecognitionFinalResult(String data) {
                viewFlipper.setDisplayedChild(0);
                recognition_view.stop();
                onFormattedInputSubmit(wrap(data, null, null, null));
            }

            @Override
            public void OnSpeechRecognitionCurrentResult(String data) {
                //textView.setText(data);
            }

            @Override
            public void OnSpeechRecognitionError(int i, String s) {
                viewFlipper.setDisplayedChild(0);
            }
        });
    }


    public class StandardButtons {
        public String input;
        public String text;
        public String slot;

        public StandardButtons(String text, String input, String slot) {
            this.input = input;
            this.text = text;
            this.slot = slot;
        }
    }


    public StandardButtons getSimpleButtonInstance(String text, String slot) {
        return new StandardButtons(text, null, slot);
    }

    public StandardButtons getInputButtonInstance(String text, String input) {
        return new StandardButtons(text, input, null);
    }




    private Pair<Boolean, SublimeOptions> getOptions(boolean withTime) {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;
        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
        if (withTime)
            displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;
        //displayOptions |= SublimeOptions.ACTIVATE_RECURRENCE_PICKER;
        options.setDisplayOptions(displayOptions);
        options.setCanPickDateRange(false);
        Calendar cal = Calendar.getInstance();
        options.setDateParams(new SelectedDate(cal));
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    private void showDatePicker(final boolean withTime) {
        SublimePickerFragment pickerFrag = new SublimePickerFragment();
        pickerFrag.setCallback(new SublimePickerFragment.Callback() {
            @Override
            public void onCancelled() {

            }

            @Override
            public void onDateTimeRecurrenceSet(SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
                Date date = selectedDate.getFirstDate().getTime();
                if (withTime) {
                    onFormattedInputSubmit(wrap(Time.loads(date).dumps("MMM dd, yyyy hh:mm a"), Time.loads(date).dump(Time.STANDARD_DATE_TIME_FORMAT), "datetime", null));
                } else {
                    onFormattedInputSubmit(wrap(Time.loads(date).dumps("MMM dd, yyyy"), Time.loads(date).dump(Time.STANDARD_DATE_FORMAT), "date", null));
                }
            }
        });
        Pair<Boolean, SublimeOptions> optionsPair = getOptions(withTime);
        Bundle bundles = new Bundle();
        bundles.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
        pickerFrag.setArguments(bundles);
        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        pickerFrag.show(getFragmentManager(), "SUBLIME_PICKER");

    }


    public void delayedSearchInput(String text){
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }

    private Timer timer;
    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable arg0) {
            // user typed: start the timer
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // do your actual work here
                    final String text=editText.getText().toString();
                    if(!text.trim().isEmpty()){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                delayedSearchInput(text);
                            }
                        });

                    }
                }
            }, 600); // 600ms delay before the timer executes the „run“ method from TimerTask
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // nothing to do here
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // user is typing: reset already started timer (if existing)
            if (timer != null) {
                timer.cancel();
            }
        }
    };

}
