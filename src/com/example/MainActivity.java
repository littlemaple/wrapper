package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chart.GraphicView;
import com.example.demo.R;
import com.example.ui.ZoomButton;
import com.example.ui.ZoomButton.onCheckedValueChangedInstantly;

public class MainActivity extends Activity implements OnGestureListener {

	private GestureDetectorCompat	gesture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		gesture = new GestureDetectorCompat(MainActivity.this, this);

		new Thread(new Runnable() {

			@Override
			public void run() {

				String[] cmd = new String[] { "/system/bin/getevent" };
				try {
					String ret = executeCmd(cmd, "system/bin");
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}

	@Override
	public boolean dispatchGenericMotionEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		gesture.onTouchEvent(ev);
		return super.dispatchGenericMotionEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		gesture.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	static {
		System.loadLibrary("Launch");
	}

	/**
	 * 
	 * @param cmd
	 *            {"/system/bin/cat", "/proc/version"}��
	 * @param workdirectory
	 *            �����磺"system/bin/"��
	 * @return
	 * @throws IOException
	 */
	public static synchronized String executeCmd(String[] cmd, String workdirectory) throws IOException {
		StringBuffer result = new StringBuffer();
		try {
			// ��������ϵͳ��̣�Ҳ������Runtime.exec()������
			// Runtime runtime = Runtime.getRuntime();
			// Process proc = runtime.exec(cmd);
			// InputStream inputstream = proc.getInputStream();
			ProcessBuilder builder = new ProcessBuilder(cmd);

			InputStream in = null;
			if (workdirectory != null) {
				builder.directory(new File(workdirectory));
				builder.redirectErrorStream(true);
				Process process = builder.start();
				in = process.getInputStream();
				byte[] re = new byte[1024];
				while (in.read(re) != -1) {
					result = result.append(new String(re));
				}
			}
			if (in != null) {
				in.close();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return result.toString();
	}

	public static final String[]	TOP	= { "/system/bin/top", "-n", "1" };

	public static synchronized String runCmd(String[] cmd, String pkgName) {
		String line = null;
		InputStream is = null;
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(cmd);
			is = proc.getInputStream();

			// ����BufferedReader
			BufferedReader buf = new BufferedReader(new InputStreamReader(is));
			do {
				line = buf.readLine();
				// ��ȡ����ӦpkgName���ѭ��������δ�ҵ���
				if (null == line || line.endsWith(pkgName)) {
					break;
				}
			}
			while (true);

			if (is != null) {
				buf.close();
				is.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			final TextView tv = (TextView) rootView.findViewById(R.id.tv);
			final ZoomButton btn = (ZoomButton) rootView.findViewById(R.id.customToggleButton1);
			final GraphicView cv = (GraphicView) rootView.findViewById(R.id.customScrollerView1);
			btn.setLimite(0, 5);
			btn.setOnCheckedValueChangedInstantly(new onCheckedValueChangedInstantly() {

				@Override
				public void onValue(int value) {
					tv.setText("res:" + value);
				}
			});

			rootView.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					btn.setCurrent(3);
					Paint paint = new Paint();
					ImageView iv = (ImageView) rootView.findViewById(R.id.imageView1);
					Bitmap bp = Bitmap.createBitmap(200, 200, Config.ARGB_8888);
					Canvas canvas = new Canvas(bp);
					paint.setColor(Color.BLACK);
					canvas.drawCircle(12, 12, 40, paint);
					canvas.drawCircle(bp.getWidth() / 2, bp.getHeight() / 2, 40, paint);
					canvas.drawCircle(iv.getLeft(), iv.getTop(), 40, paint);
					canvas.drawColor(Color.CYAN);
					iv.setBackground(new BitmapDrawable(getResources(), bp));
					EditText et1 = (EditText) rootView.findViewById(R.id.editText1);
					EditText et2 = (EditText) rootView.findViewById(R.id.editText2);
					float x = 0f, y = 0f;
					try {
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			// tv.setText(new Launch().getNative());
			return rootView;
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
		}

	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("====onDown=====");
		try {
			View view = getWindow().getDecorView();
			View vv = view.findViewById(R.id.customToggleButton1);
			Rect rect = new Rect();
			vv.getLocalVisibleRect(rect);
			System.out.println(">>>bottom" + rect.bottom + ",top:" + rect.top + ",left:" + rect.left + ",right:" + rect.right);
			int location[] = new int[4];
			vv.getLocationOnScreen(location);
			System.out.println(">>>" + location[0] + ",:" + location[1] + ",:" + location[2] + ",:" + location[3]);

		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("====onShowPress=====");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("====onSingleTapUp=====");
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		System.out.println("====onScroll=====");
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

}
