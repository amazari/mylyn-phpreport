package com.igalia.phpreport.mylyn;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.mylyn.context.ui.IContextUiStartup;
import org.eclipse.mylyn.tasks.core.ITaskActivityManager;
import org.eclipse.mylyn.tasks.ui.TasksUi;

import com.igalia.phpreport.mylyn.preferences.PreferenceConstants;

public class ContextUiStartup implements IContextUiStartup {
	
	protected TaskActivationListener listener;

	@Override
	public void lazyStartup() {

		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		store.addPropertyChangeListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
			
				if (arg0.getProperty() == PreferenceConstants.PHPREPORT_ENABLED) {
					boolean enabled = (Boolean) arg0.getNewValue();

					if (enabled) {
						PHPReport phpReport = createPhpReport(store);
						listener = new TaskActivationListener(phpReport);
						ITaskActivityManager activityManager = TasksUi
								.getTaskActivityManager();
						activityManager.addActivationListener(listener);

					} else {
						ITaskActivityManager activityManager = TasksUi
								.getTaskActivityManager();
						activityManager.removeActivationListener(listener);
						listener = null;
					}
				}
				else 
				{
					PHPReport phpReport = createPhpReport(store);
					listener.setPhpReport (phpReport);
					
				}
			}

			private PHPReport createPhpReport(final IPreferenceStore store) {
				PHPReport phpReport = new PHPReport(
						store.getString(PreferenceConstants.PHPREPORT_URL),
						store.getString(PreferenceConstants.PHPREPORT_USERNAME),
						store.getString(PreferenceConstants.PHPREPORT_PASSWORD),
						store.getBoolean(PreferenceConstants.PHPREPORT_TELEWORKING));
				return phpReport;
			}
		});

		boolean enabled = store
				.getBoolean(PreferenceConstants.PHPREPORT_ENABLED);
		store.firePropertyChangeEvent(PreferenceConstants.PHPREPORT_ENABLED,
				!enabled, enabled);


	}

}
