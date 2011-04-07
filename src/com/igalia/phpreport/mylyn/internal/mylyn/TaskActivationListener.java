package com.igalia.phpreport.mylyn.internal.mylyn;

import java.util.Date;

import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskActivationListener;

import com.igalia.phpreport.mylyn.internal.phpreport.PHPReport;

final class TaskActivationListener implements
		ITaskActivationListener {
	private Date currentTaskBegin;
	private PHPReport phpReport;
	
	public TaskActivationListener(PHPReport phpReport) {
		this.phpReport = phpReport;
		currentTaskBegin = new Date ();
	}

	@Override
	public void taskDeactivated(ITask task) {		
		if (phpReport == null)
			return;
		phpReport.addTask (task, currentTaskBegin, new Date());
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

	public void setPhpReport(PHPReport phpReport) {
		this.phpReport = phpReport;
		
	}
}