package lecho.sample.rootshell;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
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
            // Create stream for sh shell
            out = new DataOutputStream(shell.getOutputStream());

            // Executing commands without root rights
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
            Log.e(LOG_TAG, "ShellRoot#shExecute() finished with error", e);
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

    // run command with su rights and return output of that command(inside su
    // shell)
    public static void suOutputExecute(String command) {
        try {
            int BUFF_LEN = 1024;
            Process p = Runtime.getRuntime().exec(new String[] { "su", "-c", "system/bin/sh" });
            DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
            // from here all commands are executed with su permissions
            stdin.writeBytes(command + "\n"); // \n executes the command
            InputStream stdout = p.getInputStream();
            byte[] buffer = new byte[BUFF_LEN];
            int read;
            String out = new String();
            // read method will wait forever if there is nothing in the stream
            // so we need to read it in another way than
            // while((read=stdout.read(buffer))>0)
            while (true) {
                read = stdout.read(buffer);
                out += new String(buffer, 0, read);
                if (read < BUFF_LEN) {
                    // we have read everything
                    break;
                }
            }
            stdout.close();
            Log.e("ROOT", out);
            p.waitFor();
        } catch (Exception e) {
            Log.e("ROOT", "Error", e);
        }
    }

    /**
     * Copy busybox binaries to /data/data/lecho.sample.rootshel and set x
     * permission
     * 
     * @param command
     *            for example /data/data/my.app/files/busybox ping -4 8.8.8.8
     */
    public static void busyboxExecute(String command) {
        try {
            int BUFF_LEN = 1024;
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
            char[] buffer = new char[BUFF_LEN];
            int read;
            StringBuilder out = new StringBuilder();
            while ((read = stdout.read(buffer)) > 0) {
                out.append(buffer, 0, read);
            }
            stdout.close();
            Log.e("ROOT", out.toString());
            p.waitFor();
        } catch (Exception e) {
            Log.e("ROOT", "Error", e);
        }
    }
}
