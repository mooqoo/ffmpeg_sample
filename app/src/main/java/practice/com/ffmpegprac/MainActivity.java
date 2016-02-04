package practice.com.ffmpegprac;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "ffmpeg";

    // view
    private TextView tv_log;
    private Button btn_run, btn_clear;
    private EditText et_cmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();

        loadFfmpegBinary();
    }

    private void setupView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // button
        btn_run = (Button) findViewById(R.id.btn_run);
        btn_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmd = et_cmd.getText().toString();
                Log.d(TAG, "cmd = " + cmd);
                ffmpegCmd(cmd);
            }
        });

        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearLog();
                Log.d(TAG, "btn_clear clicked");
            }
        });

        // editText
        et_cmd = (EditText) findViewById(R.id.et_cmd);

        // textView
        tv_log = (TextView) findViewById(R.id.tv_log);
        tv_log.setMovementMethod(new ScrollingMovementMethod()); //set the textview log scroll movement
    }

    private void clearLog() {
        Log.i(TAG, "clearLog(): tv_log=" + tv_log);
        if(tv_log!=null)
            tv_log.setText("Log: \n");
    }

    private void updateLog(String msg) {
        Log.i(TAG,"updateLog(): tv_log="+msg);
        if(tv_log!=null)
            tv_log.setText(tv_log.getText() + msg + "\n");
    }

    private void loadFfmpegBinary() {
        FFmpeg ffmpeg = FFmpeg.getInstance(this);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.d(TAG, "loadFFMPEG: onStart");
                    updateLog("start loading binary");
                }

                @Override
                public void onFailure() {
                    Log.d(TAG, "loadFFMPEG: onFailure");
                    updateLog("load binary fail");
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "loadFFMPEG: onSuccess");
                    updateLog("load binary success");
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "loadFFMPEG: onFinish");
                    updateLog("load binary finish");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
            Log.d(TAG, "loadFFMPEG: FFmpegNotSupportedException: " + e.toString());
            updateLog("ffmpeg not support");
        }
    }

    public void ffmpegCmd(String cmd) {
        FFmpeg ffmpeg = FFmpeg.getInstance(this);
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.d(TAG, "ffmpegCmd: onStart");
                    updateLog("start execute command");
                }

                @Override
                public void onProgress(String message) {
                    Log.d(TAG, "ffmpegCmd: onProgress");
                    updateLog("command in progress: " + message);
                }

                @Override
                public void onFailure(String message) {
                    Log.d(TAG, "ffmpegCmd: onFailure");
                    updateLog("command fail: " + message);
                }

                @Override
                public void onSuccess(String message) {
                    Log.d(TAG, "ffmpegCmd: onSuccess");
                    updateLog("command success: " + message);
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "ffmpegCmd: onFinish");
                    updateLog("command finish");
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
            Log.d(TAG, "ffmpegCmd: FFmpegCommandAlreadyRunningException: " + e.toString());
            updateLog("command already running...");
        }
    }
}
