package de.rosaschulz.spiele.solitair.model;

import java.util.Date;

/**
 * @author robert
 *
 */
public class ProgressWatcher implements Runnable {
	
	/**
	 * 
	 */
	private final ProgressReportingSolver progressReportingSolver;

	/**
	 * @param iterativeSolver
	 */
	ProgressWatcher(ProgressReportingSolver iterativeSolver) {
		progressReportingSolver = iterativeSolver;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(1000);
				// System.out.println("Move Nr." + i + ": " + move.toString());
				long currentNanos=System.nanoTime();
				Date currentDate = new Date();
				long runningSeconds = (currentDate.getTime()-progressReportingSolver.startDate.getTime());
				long estimatedSeconds = (long)(runningSeconds / progressReportingSolver.progress);
				Date etaDate = new Date(progressReportingSolver.startDate.getTime()+estimatedSeconds);
				
				System.out.println("solution progress: " + progressReportingSolver.progress 
						+ " stepCounter="+progressReportingSolver.stepCounter
						+ " eta="+etaDate
						);
			}
		} catch (InterruptedException e) {
			System.out.println("ProgressWatcher interrupted --> exiting...");			
		}
	}
	
}