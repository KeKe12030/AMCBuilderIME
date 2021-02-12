package com.nullatom.amcbuilderime.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nullatom.amcbuilderime.R;


public class AMCBuilderService extends InputMethodService {
	private BroadcastReceiver cmdBroadcastReceiver = null;
	public static String cmd = "";
	public TextView input_method_textview = null;
	public SeekBar speedBar = null;

	@Override
	public View onCreateInputView() {
		View mInputView = getLayoutInflater().inflate(R.layout.view, null);
//		input_method_textview = (TextView) mInputView.findViewById(R.id.input_method_textview);
//		speedBar = mInputView.findViewById(R.id.importSpeedBar);
//		input_method_textview.setText("AMCBuilder专用输入法(开启状态)");
		if (cmdBroadcastReceiver == null) {
			IntentFilter filter = new IntentFilter("AMC_MSG");
			filter.addAction("AMC_KEYEVENT");
			cmdBroadcastReceiver = new CmdBroadcastReceiver();
			registerReceiver(cmdBroadcastReceiver, filter);
		}
//		speedBar.setProgress(SettingsActivity.speed);
//		speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//			int speed = SettingsActivity.speed;
//			@Override
//			public void onProgressChanged(SeekBar seekBar, int speed1, boolean b) {
//				this.speed = speed1;
//				SettingsActivity.speed = speed;
//			}
//
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar) {
//				// 刚刚触碰进度条
//			}
//
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar) {
//				// 松开进度条
//				AMCBuilderService.this.showToast("已设置速率：" + speed + "%");
//				SettingsActivity.config.setSpeed(speed);
//				SettingsActivity.speed = speed;
//				SettingsActivity.setSpeeds();
//			}
////		});
//		// 停止按钮的ClickListener
//		mInputView.findViewById(R.id.inputStop).setOnClickListener(new View.OnClickListener() {
//			//停止导入
//			@Override
//			public void onClick(View view) {
//				try {
//					Process process = MainActivity.runtime.exec("input text \"hello,world\"");
//					System.out.println("输入完成，等待wait");
//					process.waitFor();
//					System.out.println("已经导入，wait解除");
//					DataInputStream dataOutputStream = new DataInputStream(process.getInputStream());
//					String temp = null;
//					StringBuilder msg = new StringBuilder();
//					while((temp = dataOutputStream.readLine()) != null){
//						msg.append(temp);
//					}
//					System.out.println("命令行结果："+msg.toString());
//				} catch (IOException | InterruptedException e) {
//					e.printStackTrace();
//				}
////				MainActivity.flag = false;
////				NetWorkThread.cmdss = new String[0][0];
////				ImportBuildingsThread.haveNext = 0;
////				AMCBuilderService.this.showToast("已暂停导入！");
////				JsonInfo setCmd = new JsonInfo();
////				setCmd.type = "setCid";
////				setCmd.cid = MainActivity.nowCid-20 < 0 ? 0 : MainActivity.nowCid-20;
////				SocketUtils.sendJson(setCmd);//发送设置ID
//			}
//		});
//
//		mInputView.findViewById(R.id.inputContinue).setOnClickListener(new View.OnClickListener() {
//			//停止导入
//			@Override
//			public void onClick(View view) {
//				Thread importThread = new Thread(MainActivity.importBuilding);
//				MainActivity.importBuilding.inputContinue = true;
//				MainActivity.flag = true;
//
//				importThread.start();
//
//				AMCBuilderService.this.showToast("已恢复导入！");
//			}
//
//		});
			// 设置按钮的ClickListener，点击转到MainActivity
//		mInputView.findViewById(R.id.inputSetting).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				startActivity(new Intent(AMCBuilderService.this, MainActivity.class));
//			}
//		});

			// TODO 打算做个启动的，发现需要MainActivity，有点麻烦，等会在改
			return mInputView;
		}

	private class CmdBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("AMC_MSG")) {
				String msg = intent.getStringExtra("msg");
				if (msg != null) {
					InputConnection ic = getCurrentInputConnection();
					if (ic != null) {
						ic.commitText(msg, 1);
					}
				}
				return;
			}

			if (intent.getAction().equals("AMC_KEYEVENT")) {				
				int code = intent.getIntExtra("code", -1);				
				if (code != -1) {
					InputConnection ic = getCurrentInputConnection();
					if (ic != null) {
						ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, code));
					}
				}
				return;
			}
			if(intent.getAction().equals("AMC_CLOSE")) {
				return;
			}

			if (intent.getAction().equals("AMC_GET_MSG")) {
				InputConnection ic = getCurrentInputConnection();
				String msg = intent.getStringExtra("msg");
				if (msg != null) {
					if (ic != null) {
						cmd = ic.getTextBeforeCursor(msg.length(), 0).toString();
						System.out.println("获取到的文本："+cmd);
						System.out.println("比对结果："+cmd.equals(msg));
					}
				}
			}
		}
	}
	public void onDestroy() {
		if (cmdBroadcastReceiver != null) {
			unregisterReceiver(cmdBroadcastReceiver);
		}
		super.onDestroy();    	
	}
	private void showToast(String msg) {//发布toast
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}