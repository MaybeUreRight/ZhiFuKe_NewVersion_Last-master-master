package com.weilay.pos2.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.weilay.pos2.R;

public class ProgressDialogUtil {
	private static ProgressDialog progressDialog;

	public static ProgressDialog progressDialog(Context context, String message) {
		progressDialog = new ProgressDialog(context, R.style.loading_dialog);
		progressDialog.setIndeterminateDrawable(context.getResources()
				.getDrawable(R.drawable.loading1));
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(message);

		return progressDialog;
	}
}
