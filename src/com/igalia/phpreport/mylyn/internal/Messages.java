package com.igalia.phpreport.mylyn.internal;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.igalia.phpreport.mylyn.messages"; //$NON-NLS-1$
	public static String PHPReport_NOT_AUTHENTICATED;
	public static String PHPReport_SERVER_RESPONDED;
	public static String PHPReport_AUTH_SUCCESS;
	public static String PHPReport_HTTP_ERROR;
	public static String PHPReport_TASK_SYNC_FAILED;
	public static String PHPReport_TASK_SYNC_SUCCESS;
	public static String PHPReport_AUTH_FAILED;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
