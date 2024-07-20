package com.coreApplication.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DLModel {

    public DLModel() {
    }

    public float predict(String text) {
        try {
            // Create Virtual Environment and install required packages if not already installed
            String command = "/bin/zsh -c \"source /Users/user/PycharmProjects/pythonProject/venv/bin/activate && python /Users/user/PycharmProjects/pythonProject/example.py \\\"" + text.replace("\"", "\\\"") + "\\\"\"";

            ProcessBuilder pb = new ProcessBuilder("/bin/zsh", "-c", command);
            Process p = pb.start();

            int exitCode = p.waitFor();
            if (exitCode == 0) {
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String output = in.readLine();
                if (output != null) {
                    return Float.parseFloat(output.trim());
                } else {
                    System.err.println("No output from the Python script.");
                    return -1;
                }
            } else {
                System.err.println("Python script execution failed with exit code " + exitCode);
                return -1;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }
}