package com.igalia.phpreport.mylyn.internal.mylyn;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

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
	private Date currentTaskBegin;
	private TasksTracker phpReport;

	public TaskActivationListener(TasksTracker phpReport) {
		this();
		this.phpReport = phpReport;
	}

	public TaskActivationListener() {
		currentTaskBegin = new Date();
	}

	@Override
	public void taskDeactivated(final ITask task) {
		if (phpReport == null)
			return;

		IWorkbench workbench = Activator.getDefault().getWorkbench();
		IProgressService progressService = workbench.getProgressService();
		try {
			progressService.busyCursorWhile(new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					IStatus status = phpReport.addTask(task, currentTaskBegin,
							new Date());
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

		currentTaskBegin = null;
	}

	@Override
	public void taskActivated(ITask task) {
		currentTaskBegin = new Date();
	}

	@Override
	public void preTaskDeactivated(ITask task) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preTaskActivated(ITask task) {
		// TODO Auto-generated method stub

	}

	public void setPhpReport(TasksTracker phpReport) {
		this.phpReport = phpReport;

	}

	public TasksTracker getTracker() {
		// TODO Auto-generated method stub
		return phpReport;
	}
}