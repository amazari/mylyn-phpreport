package com.igalia.phpreport.mylyn.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.igalia.phpreport.mylyn.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.PHPREPORT_ENABLED, false);
		store.setDefault(PreferenceConstants.PHPREPORT_TELEWORKING, false);
		store.setDefault(PreferenceConstants.PHPREPORT_URL, "https://phpreport.igalia.com/");
		store.setDefault(PreferenceConstants.PHPREPORT_USERNAME, "igalian");
		store.setDefault(PreferenceConstants.PHPREPORT_ENABLED, "acoruna");
	}

}
