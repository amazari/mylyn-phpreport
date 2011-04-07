package com.igalia.phpreport.mylyn.internal.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import com.igalia.phpreport.mylyn.internal.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class PHPReportPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public PHPReportPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("PHPReport Preferences");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(
				new BooleanFieldEditor(
					PreferenceConstants.PHPREPORT_ENABLED,
					"&Send Tasks infos to PHPReport ?",
					getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PHPREPORT_URL, 
				"&PHPReport URL:", getFieldEditorParent()));
		addField(
			new StringFieldEditor(
				PreferenceConstants.PHPREPORT_USERNAME,
				"&username:",
				getFieldEditorParent()));

		addField(new StringFieldEditor(
				PreferenceConstants.PHPREPORT_PASSWORD,
			"password:",
			getFieldEditorParent()));
		addField(
				new BooleanFieldEditor(
					PreferenceConstants.PHPREPORT_TELEWORKING,
					"&Are you working remotely ?",
					getFieldEditorParent()));
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}