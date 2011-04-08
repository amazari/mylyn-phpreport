package com.igalia.phpreport.mylyn.internal.phpreport;

import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.tasks.core.ITask;

public interface TasksTracker {

	public abstract IStatus authenticate();

	public abstract boolean isConnected();

	public abstract IStatus addTask(ITask task, Date begin, Date end);

	public abstract IStatus getStatus();

}