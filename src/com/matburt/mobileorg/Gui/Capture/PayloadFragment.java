package com.matburt.mobileorg.Gui.Capture;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.content.ContentResolver;


import com.matburt.mobileorg.R;
import com.matburt.mobileorg.Gui.ViewFragment;
import com.matburt.mobileorg.OrgData.OrgNode;
import com.matburt.mobileorg.OrgData.OrgNodePayload;
import com.matburt.mobileorg.util.OrgNodeNotFoundException;

public class PayloadFragment extends ViewFragment {
	private static final String PAYLOAD = "payload";
	
	private RelativeLayout payloadView;
	private EditText payloadEdit;
	private OrgNodePayload payload;
	
	private ImageButton editButton;
	private ImageButton cancelButton;
	private ImageButton saveButton;

        private ContentResolver resolver;	

	private OnPayloadModifiedListener mListener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
            mListener = (OnPayloadModifiedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPayloadModifiedListener");
        }
                resolver = activity.getContentResolver();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.payloadView = (RelativeLayout) inflater.inflate(
				R.layout.edit_payload, container, false);
		this.webView = (WebView) payloadView
				.findViewById(R.id.edit_payload_webview);
		this.webView.setBackgroundColor(0x00000000);
		this.webView.setWebViewClient(new InternalWebViewClient());
		this.webView.setOnClickListener(editListener);

		this.payloadEdit = (EditText) payloadView
				.findViewById(R.id.edit_payload_edittext);
		
		this.editButton = (ImageButton) payloadView
				.findViewById(R.id.edit_payload_edit);
		editButton.setOnClickListener(editListener);
		
		this.cancelButton = (ImageButton) payloadView
				.findViewById(R.id.edit_payload_cancel);
		cancelButton.setOnClickListener(cancelListener);
		
		this.saveButton = (ImageButton) payloadView
				.findViewById(R.id.edit_payload_save);
		saveButton.setOnClickListener(saveListener);
		
		return payloadView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		EditActivity editActivity = (EditActivity) getActivity();

                try {
                    OrgNode node = new OrgNode(editActivity.getOrgNode().id, resolver);
                    this.payload = node.getOrgNodePayload();
                } catch( OrgNodeNotFoundException e ) { }

		if(savedInstanceState != null)
			restoreInstanceState(savedInstanceState);
		else
			switchToView();
		
		setModifiable(editActivity.isPayloadEditable());
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		if(this.payloadEdit.getVisibility() == View.VISIBLE)
			outState.putString(PAYLOAD, this.payloadEdit.getText().toString());
	}
	
	public void restoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState != null) {
			String payloadString = savedInstanceState.getString(PAYLOAD);
			
			if(payloadString != null)
				switchToEdit(payloadString);
			else
				switchToView();
		}
	}
	
	public void setModifiable(boolean enabled) {
		if(enabled)
			this.editButton.setVisibility(View.VISIBLE);
		else
			this.editButton.setVisibility(View.GONE);
	}

	public void setPayload(String payload) {
		this.payload.set(payload);
		
		mListener.onPayloadModified();
	}
	
	public String getPayload() {
		return this.payload.get();
	}
	
	private void switchToEdit(String payloadString) {
		webView.setVisibility(View.GONE);
		editButton.setVisibility(View.GONE);

		if(payloadString != null)
			payloadEdit.setText(payloadString);
		cancelButton.setVisibility(View.VISIBLE);
		saveButton.setVisibility(View.VISIBLE);
		payloadEdit.setVisibility(View.VISIBLE);
		
		mListener.onPayloadStartedEdit();
	}
	
	private void switchToEdit() {
		switchToEdit(this.payload.get());
	}
	
	public void switchToView() {
		payloadEdit.setVisibility(View.GONE);
		cancelButton.setVisibility(View.GONE);
		saveButton.setVisibility(View.GONE);
		
		display(this.payload.getCleanedPayload());
		webView.setVisibility(View.VISIBLE);
		editButton.setVisibility(View.VISIBLE);
	}
	
	private OnClickListener editListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switchToEdit();
			payloadEdit.requestFocus();
			payloadEdit.setSelection(payloadEdit.length());
			InputMethodManager keyboard = (InputMethodManager)
                   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(payloadEdit, 0);
		}
	};
	
	private OnClickListener saveListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
                    savePayload();
		}
	};

        public void savePayload() {
                setPayload(payloadEdit.getText().toString());
                switchToView();
                mListener.onPayloadEndedEdit();
                webView.requestFocus();
        }
	
	private OnClickListener cancelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switchToView();
			mListener.onPayloadEndedEdit();
			webView.requestFocus();
		}
	};
	
	
	public interface OnPayloadModifiedListener {
		public void onPayloadStartedEdit();
		public void onPayloadEndedEdit();
		public void onPayloadModified();
	}
}
