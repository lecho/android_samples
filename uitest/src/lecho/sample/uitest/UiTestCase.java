package lecho.sample.uitest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class UiTestCase extends UiAutomatorTestCase {

	public void test01() throws UiObjectNotFoundException {
		sleep(1000);
		shExecute(new String[] { "dumpsys battery", "dumpsys cpuinfo" });
		sleep(1000);
	}

	public static void shExecute(String[] commands) {
		Process shell = null;
		DataOutputStream out = null;
		BufferedReader in = null;
		log("Testing");
		try {
			// Acquire root
			shell = Runtime.getRuntime().exec("sh");
			// Create stream for root shell
			out = new DataOutputStream(shell.getOutputStream());

			in = new BufferedReader(new InputStreamReader(shell.getInputStream()));

			// Executing commands with root rights
			for (String command : commands) {
				out.writeBytes(command + "\n");
				out.flush();
			}

			out.writeBytes("exit\n");
			out.flush();
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = in.readLine()) != null) {
				sb.append(line).append("\n");
			}
			log(sb.toString());
			System.out.println(sb.toString());
			shell.waitFor();

		} catch (Exception e) {
			log("Exception: " + e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
				// shell.destroy();
			} catch (Exception e) {
				// hopeless
			}
		}
	}

	private static void log(String msg) {
		// Path is device dependent.
		File file = new File("/storage/sdcard0/", "mylog.txt");
		BufferedWriter bw = null;
		try {
			FileWriter fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);
			bw.write(msg);
			bw.write("/n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != bw) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
