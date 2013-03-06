package lecho.sample.rootshell;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import android.util.Log;

/**
 * Sample root shell interface
 * 
 * @author lecho
 * 
 */
public class RootShell {
    private static final String LOG_TAG = RootShell.class.getSimpleName();

    public static void shExecute(String[] commands) {
        Process shell = null;
        DataOutputStream out = null;

        try {
            // Acquire shell - no root
            Log.i("RootShell", "Starting exec of sh");
            shell = Runtime.getRuntime().exec("sh");
            // Create stream for root shell
            out = new DataOutputStream(shell.getOutputStream());

            // Executing commands with root rights
            Log.i(LOG_TAG, "Executing commands...");
            for (String command : commands) {
                Log.i(LOG_TAG, "Executing: " + command);
                out.writeBytes(command + "\n");
                out.flush();
            }

            out.writeBytes("exit\n");
            out.flush();
            shell.waitFor();

        } catch (Exception e) {
            Log.d(LOG_TAG, "ShellRoot#suExecute() finished with error", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                // shell.destroy();
            } catch (Exception e) {
                // hopeless
            }
        }
    }

    public static void suExecute(String[] commands) {
        Process shell = null;
        DataOutputStream out = null;
        BufferedReader in = null;

        try {
            // Acquire root
            Log.i(LOG_TAG, "Starting exec of su");
            shell = Runtime.getRuntime().exec("su");
            // Create stream for root shell
            out = new DataOutputStream(shell.getOutputStream());

            in = new BufferedReader(new InputStreamReader(shell.getInputStream()));

            // Executing commands with root rights
            Log.i(LOG_TAG, "Executing commands...");
            for (String command : commands) {
                Log.i(LOG_TAG, "Executing: " + command);
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
            Log.i(LOG_TAG, sb.toString());
            shell.waitFor();

        } catch (Exception e) {
            Log.d(LOG_TAG, "ShellRoot#suExecute() finished with error", e);
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
}
