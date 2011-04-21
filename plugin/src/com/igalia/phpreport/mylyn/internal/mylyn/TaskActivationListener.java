package com.igalia.phpreport.mylyn.internal.mylyn;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskActivationListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.statushandlers.StatusManager;

import com.igalia.phpreport.mylyn.Activator;
import com.igalia.phpreport.mylyn.internal.phpreport.TasksTracker;

final class TaskActivationListener implements ITaskActivationListener {
	private TasksTracker tracker;

	public TaskActivationListener(TasksTracker phpReport) {
		this();
		this.tracker = phpReport;
	}

	public TaskActivationListener() {
	}

	@Override
	public void taskDeactivated(final ITask task) {
		if (tracker == null)
			return;

		IWorkbench workbench = Activator.getDefault().getWorkbench();
		IProgressService progressService = workbench.getProgressService();
		try {
			progressService.busyCursorWhile(new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					IStatus status = tracker.saveCurrentTask ();
					StatusManager.getManager().handle(status);

				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void taskActivated(ITask task) {
		tracker.setCurrentTask(task);
	}

	@Override
	public void preTaskDeactivated(ITask task) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preTaskActivated(ITask task) {
		// TODO Auto-generated method stub

	}

	public void setTracker(TasksTracker phpReport) {
		this.tracker = phpReport;

	}

	public TasksTracker getTracker() {
		// TODO Auto-generated method stub
		return tracker;
	}
}