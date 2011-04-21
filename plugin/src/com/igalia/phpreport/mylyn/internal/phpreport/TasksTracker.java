package com.igalia.phpreport.mylyn.internal.phpreport;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.tasks.core.ITask;

public interface TasksTracker {

	public abstract IStatus authenticate();

	public abstract boolean isConnected();
	
	public void setCurrentTask (ITask task);

	public abstract IStatus saveCurrentTask();

	public abstract IStatus getStatus();

	public abstract boolean hasTaskPending();

}