package com.hexensemble.gbusb64mcartmac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents the command processor.
 * 
 * @author HexEnsemble
 * @author www.hexensemble.com
 * @version 1.0.3
 * @since 1.0.0
 */
public class Processor implements Runnable {

	private ReentrantLock lock;
	private ProcessBuilder processBuilder;
	private StringBuilder stringBuilder;

	/**
	 * Creates the command processor.
	 * 
	 * @param command
	 *            Command to execute.
	 */
	public Processor(String command) {
		lock = new ReentrantLock();

		StringBuilder commandBuilder = new StringBuilder();
		commandBuilder.append("cd EMSFlasher ; ./ems-flasher ");
		commandBuilder.append(command);
		String fullCommand = commandBuilder.toString();

		ArrayList<String> commands = new ArrayList<String>();
		commands.add("bash");
		commands.add("-c");
		commands.add(fullCommand);

		processBuilder = new ProcessBuilder(commands);
		processBuilder.redirectErrorStream(true);

		stringBuilder = new StringBuilder();
	}

	/**
	 * Runs the command processor in a thread.
	 */
	@Override
	public void run() {
		lock.lock();

		try {
			Process process = processBuilder.start();

			InputStream input = process.getInputStream();
			InputStreamReader reader = new InputStreamReader(input);
			BufferedReader buffer = new BufferedReader(reader);
			String line = buffer.readLine();

			while (line != null) {
				stringBuilder.append(line + "\n");
				line = buffer.readLine();
			}
		} catch (IOException e) {
			stringBuilder.append(e.getMessage());
		}

		lock.unlock();
	}

	/**
	 * Gets the command processor output.
	 * 
	 * @return Command processor output.
	 */
	public String getOutput() {
		return stringBuilder.toString();
	}

}
