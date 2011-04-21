package com.igalia.phpreport.mylyn.internal.mylyn;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.mylyn.context.ui.IContextUiStartup;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskActivityManager;
import org.eclipse.mylyn.tasks.ui.TasksUi;

import com.igalia.phpreport.mylyn.Activator;
import com.igalia.phpreport.mylyn.internal.phpreport.TasksTracker;

public class ContextUiStartup implements IContextUiStartup {

	protected TaskActivationListener listener;

	public void setEnabled(boolean enabled) {
		System.out.println("setEnabled"+enabled);
		ITaskActivityManager activityManager = TasksUi.getTaskActivityManager();
		if (enabled) {
			if (listener == null)
				listener = new TaskActivationListener();

			TasksTracker tracker = Activator.getDefault().getTracker();
			ITask activeTask = activityManager.getActiveTask();
			tracker.setCurrentTask(activeTask);
			listener.setTracker(Activator.getDefault().getTracker());
			
			activityManager.addActivationListener(listener);
		} else {
			if (listener != null) {

				activityManager.removeActivationListener(listener);
				listener = null;
			}
		}
	}

	public boolean isEnabled() {
		return listener != null && listener.getTracker().isConnected();
	}

	@Override
	public void lazyStartup() {
		System.out.println("lazy");
		DataBindingContext bindingContext = new DataBindingContext();

		IObservableValue activatorEnabledValue = PojoProperties.value(
				Activator.class, "enabled").observe(Activator.getDefault());
		IObservableValue contextEnabledValue = PojoProperties.value(
				ContextUiStartup.class, "enabled").observe(this);

		bindingContext.bindValue(contextEnabledValue, activatorEnabledValue);
	}

}
