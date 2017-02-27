package com.app.hotgirlforbigo.Custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.hotgirlforbigo.R;
import com.app.hotgirlforbigo.Utils.Util;

/**
 * Created by nguyennam on 2/22/17.
 */

public class DialogSendMail extends CustomDialog {

    private EditText editSubjectMail, editContentMail;
    private Button btnSend, btnCancel;
    private Activity activity;

    public DialogSendMail(Context context) {
        super(context);
    }

    public DialogSendMail(Activity activity) {
        super(activity);
        this.activity = activity;
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public DialogSendMail(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_send_mail, null);

        editContentMail = (EditText) view.findViewById(R.id.editContentMail);
        editSubjectMail = (EditText) view.findViewById(R.id.editSubjectMail);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnSend = (Button) view.findViewById(R.id.btnSend);
        btnSend.setEnabled(false);

        editContentMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnSend.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editContentMail.getText().toString().length() > 0 && editSubjectMail.getText().toString().length() > 0) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{activity.getString(R.string.email_contact)});
                    i.putExtra(Intent.EXTRA_SUBJECT, editSubjectMail.getText().toString());
                    i.putExtra(Intent.EXTRA_TEXT, editContentMail.getText().toString());
                    try {
                        activity.startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Please fill full content.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setContentView(view);
        int screenW = Util.getScreenWidth(getContext());
        int screenH = Util.getScreenHeight(getContext());
        int width = screenW < screenH ? screenW : screenH;
        width *= 0.9;
        setDialogWidth(width, width);
    }

    @Override
    protected void setupAnimation() {
        getWindow().getAttributes().windowAnimations = R.style.SmileWindow;
    }

}
