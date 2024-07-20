package com.coreApplication.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class DLModel {

    public DLModel() {
        setupVirtualEnvironment();
    }

    private void setupVirtualEnvironment() {
        String venvPath = "src/main/python/venv";
        File venvDir = new File(venvPath);
        if (!venvDir.exists()) {
            try {
                // Create the virtual environment
                ProcessBuilder venvBuilder = new ProcessBuilder("/bin/zsh", "-c", "python3 -m venv " + venvPath);
                venvBuilder.directory(new File("/"));
                venvBuilder.start().waitFor();

                // Install required packages
                String[] packages = {"torch", "transformers"}; // Example packages
                for (String pkg : packages) {
                    ProcessBuilder pipBuilder = new ProcessBuilder("/bin/zsh", "-c", venvPath + "/bin/pip install " + pkg);
                    pipBuilder.start().waitFor();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public float predict(String text) {
        try {
            String command = "/bin/zsh -c \"source src/main/python/venv/bin/activate && python src/main/python/modelPredictionScript.txt \\\"" + text.replace("\"", "\\\"") + "\\\"\"";

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