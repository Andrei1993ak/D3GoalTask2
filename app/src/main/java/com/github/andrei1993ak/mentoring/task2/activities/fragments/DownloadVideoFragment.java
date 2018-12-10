package com.github.andrei1993ak.mentoring.task2.activities.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.andrei1993ak.mentoring.task2.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadVideoFragment extends Fragment implements ITitled {

    public static final String FILE_NAME = "audio.mp3";
    public static final String URI_STRING = "https://www.androidtutorialpoint.com/wp-content/uploads/2016/09/AndroidDownloadManager.mp3";

    @BindView(R.id.start_loading_button)
    Button mStartLoadingButton;

    @BindView(R.id.start_delete_button)
    Button mDeleteButton;

    private DownloadManager mDownloadManager;
    private SharedPreferences mDefaultSharedPreferences;
    private String mPath;
    private long mLoadingId;

    @Override
    public int getTitleResId() {
        return R.string.app_name;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final FragmentActivity activity = getActivity();

        mDownloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        mDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        mLoadingId = mDefaultSharedPreferences.getLong(getPreferenceKey(), 0L);
        mPath = String.valueOf(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + File.separator + FILE_NAME));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_download_video, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStartLoadingButton.setOnClickListener(new StartLoadingClickListener());
        mDeleteButton.setOnClickListener(new DeleteLoadingClickListener());
    }

    @Override
    public void onResume() {
        super.onResume();


        final File file = new File(mPath);

        if (!file.exists() || mLoadingId == 0L) {
            mStartLoadingButton.setVisibility(View.VISIBLE);
        } else {
            showState(mLoadingId);
        }
    }


    private long startLoading() {
        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URI_STRING));
        request.setDescription("Load and go!");
        request.setTitle("Loading intro video");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "video.ext");

        return mDownloadManager.enqueue(request);
    }

    private void showState(final long downloadId) {
        final DownloadManager.Query ImageDownloadQuery = new DownloadManager.Query();
        ImageDownloadQuery.setFilterById(downloadId);

        final Cursor cursor = mDownloadManager.query(ImageDownloadQuery);

        if (cursor.moveToFirst()) {
            showStateToast(cursor);
        } else {
            mStartLoadingButton.setVisibility(View.VISIBLE);
        }
    }

    private void showStateToast(final Cursor pCursor) {

        final int status = pCursor.getInt(pCursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
        final int reason = pCursor.getInt(pCursor.getColumnIndex(DownloadManager.COLUMN_REASON));

        String statusText = "";
        String reasonText = "";

        switch (status) {
            case DownloadManager.STATUS_FAILED:
                mDeleteButton.setVisibility(View.VISIBLE);

                statusText = "STATUS_FAILED";
                switch (reason) {
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                switch (reason) {
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "STATUS_RUNNING";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "STATUS_SUCCESSFUL";
                reasonText = "Filename:\n" + FILE_NAME;
                break;
        }


        final Toast toast = Toast.makeText(getActivity(),
                "Download Status:" + "\n" + statusText + "\n" + reasonText, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
    }


    private class StartLoadingClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            final long loadingId = startLoading();

            final SharedPreferences.Editor edit = mDefaultSharedPreferences.edit();
            edit.putLong(getPreferenceKey(), loadingId);
            edit.apply();

            mLoadingId = loadingId;
        }
    }

    private String getPreferenceKey() {
        return getClass().getSimpleName();
    }

    private class DeleteLoadingClickListener implements View.OnClickListener {

        @Override
        public void onClick(final View v) {
            mDownloadManager.remove(mLoadingId);
            mDeleteButton.setVisibility(View.GONE);
            mStartLoadingButton.setVisibility(View.VISIBLE);
        }
    }
}
