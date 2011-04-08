package com.igalia.phpreport.mylyn.internal.preferences;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.igalia.phpreport.mylyn.Activator;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class PHPReportPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	private FieldEditor urlEditor;
	private FieldEditor usernameEditor;
	private StringFieldEditor passwordFieldEditor;
	private BooleanFieldEditor teleworkEditor;
	private BooleanFieldEditor enabledEditor;

	public PHPReportPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("PHPReport Preferences");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {
		final Composite parent = getFieldEditorParent();

		enabledEditor = new BooleanFieldEditor(
				PreferenceConstants.PHPREPORT_ENABLED,
				"&Send Tasks infos to PHPReport ?", parent);

		urlEditor = new StringFieldEditor(PreferenceConstants.PHPREPORT_URL,
				"&PHPReport URL:", parent);
		usernameEditor = new StringFieldEditor(
				PreferenceConstants.PHPREPORT_USERNAME, "&username:", parent);
		passwordFieldEditor = new StringFieldEditor(
				PreferenceConstants.PHPREPORT_PASSWORD, "password:", parent);
		Text passwordText = passwordFieldEditor
				.getTextControl(getFieldEditorParent());
		passwordText.setEchoChar('*');
		teleworkEditor = new BooleanFieldEditor(
				PreferenceConstants.PHPREPORT_TELEWORKING,
				"&Are you working remotely ?", parent);

		addField(enabledEditor);
		addField(urlEditor);
		addField(usernameEditor);
		addField(passwordFieldEditor);
		addField(teleworkEditor);

		boolean enabled = getPreferenceStore().getBoolean(
				PreferenceConstants.PHPREPORT_ENABLED);

		urlEditor.setEnabled(enabled, parent);
		usernameEditor.setEnabled(enabled, parent);
		passwordFieldEditor.setEnabled(enabled, parent);
		teleworkEditor.setEnabled(enabled, parent);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() != enabledEditor)
			return;
		final Composite parent = getFieldEditorParent();
		boolean enabled = (Boolean) event.getNewValue();

		urlEditor.setEnabled(enabled, parent);
		usernameEditor.setEnabled(enabled, parent);
		passwordFieldEditor.setEnabled(enabled, parent);
		teleworkEditor.setEnabled(enabled, parent);

		super.propertyChange(event);
	}

	@Override
	protected void performApply() {
		super.performApply();

		boolean enabled = getPreferenceStore().getBoolean(
				PreferenceConstants.PHPREPORT_ENABLED);

		if (enabled == false)
			return;

		Activator.getDefault().setEnabled(enabled);

		IStatus status = Activator.getDefault().getStatus();
		if (status.isOK())
			setMessage(status.getMessage());
		else
			setErrorMessage(status.getMessage());
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}
}