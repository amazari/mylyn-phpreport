package com.igalia.phpreport.mylyn;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.BundleContext;

import com.igalia.phpreport.mylyn.internal.phpreport.PHPReport;
import com.igalia.phpreport.mylyn.internal.phpreport.TasksTracker;
import com.igalia.phpreport.mylyn.internal.preferences.PreferenceConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.igalia.phpreport.mylyn"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private TasksTracker tracker;
	private IStatus status;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		tracker = createPhpReportFromPreferences();

		final IPreferenceStore store = Activator.getDefault()
				.getPreferenceStore();

		boolean enabled = store
				.getBoolean(PreferenceConstants.PHPREPORT_ENABLED);
		setEnabled(enabled);

		store.addPropertyChangeListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {

				boolean enabled = false;
				if (arg0.getProperty().equals(
						PreferenceConstants.PHPREPORT_ENABLED))
					enabled = (Boolean) arg0.getNewValue();
				else
					enabled = true;

				setEnabled(enabled);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public void setEnabled(boolean enabled) {
		if (enabled) {
			tracker = createPhpReportFromPreferences();
			IWorkbench workbench = Activator.getDefault().getWorkbench();
			IProgressService progressService = workbench.getProgressService();
			try {
				progressService.busyCursorWhile(new IRunnableWithProgress() {

					@Override
					public void run(IProgressMonitor monitor)
							throws InvocationTargetException,
							InterruptedException {
						monitor.beginTask(
								"Authenticating to the time tracking server.",
								1);
						status = tracker.authenticate();
					}
				});
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			status = new Status(IStatus.INFO, PLUGIN_ID,
					"Time tracker deactivated.");
			tracker = null;
		}

		StatusManager.getManager().handle(status);
	}

	public IStatus getStatus() {
		return tracker.getStatus();
	}

	public boolean isEnabled() {
		return getPreferenceStore().getBoolean(
				PreferenceConstants.PHPREPORT_ENABLED);
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	private TasksTracker createPhpReportFromPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		TasksTracker phpReport = new PHPReport(
				store.getString(PreferenceConstants.PHPREPORT_URL),
				store.getString(PreferenceConstants.PHPREPORT_USERNAME),
				store.getString(PreferenceConstants.PHPREPORT_PASSWORD),
				store.getBoolean(PreferenceConstants.PHPREPORT_TELEWORKING));
		return phpReport;
	}

	public TasksTracker getTracker() {
		return tracker;
	}

}
