package com.igalia.phpreport.mylyn.internal.phpreport;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.time.DateFormatUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.commons.net.WebLocation;
import org.eclipse.mylyn.commons.net.WebUtil;
import org.eclipse.mylyn.tasks.core.ITask;

import com.igalia.phpreport.mylyn.Activator;
import com.igalia.phpreport.mylyn.Messages;

public class PHPReport implements TasksTracker {

	HttpClient httpClient;

	private static final String AUTHENTICATE_URL = "/web/services/loginService.php"; //$NON-NLS-1$
	private static final String CREATE_TASK_URL = "/web/services/createTasksService.php"; //$NON-NLS-1$

	WebLocation authentication_location;
	WebLocation create_task_location;
	IStatus status;

	String token;

	private boolean teleworking;

	public PHPReport(String url, String username, String password,
			boolean teleworking) {
		this.teleworking = teleworking;
		authentication_location = new WebLocation(String.format("%s%s", url, //$NON-NLS-1$
				AUTHENTICATE_URL), username, password);
		authentication_location.setCredentials(AuthenticationType.HTTP,
				username, password);
		create_task_location = new WebLocation(String.format("%s%s", url, //$NON-NLS-1$
				CREATE_TASK_URL), username, password);
		create_task_location.setCredentials(AuthenticationType.HTTP, username,
				password);
		this.httpClient = createHttpClient(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.igalia.phpreport.mylyn.internal.phpreport.TasksTracker#authenticate()
	 */
	@Override
	public IStatus authenticate() {

		GetMethod method = new GetMethod(authentication_location.getUrl());

		AuthenticationCredentials creds = authentication_location
				.getCredentials(AuthenticationType.REPOSITORY);
		method.setQueryString(new NameValuePair[] {
				new NameValuePair("login", creds.getUserName()),
				new NameValuePair("password", creds.getPassword()), });

		PHPReportAuthenticationResponseHandler handler = new PHPReportAuthenticationResponseHandler();

		status = sendRequest(method, create_task_location, handler,
				Messages.PHPReport_AUTH_FAILED, Messages.PHPReport_AUTH_SUCCESS);
		token = handler.getToken();

		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.igalia.phpreport.mylyn.internal.phpreport.TasksTracker#isConnected()
	 */
	@Override
	public boolean isConnected() {
		return token != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.igalia.phpreport.mylyn.internal.phpreport.TasksTracker#addTask(org
	 * .eclipse.mylyn.tasks.core.ITask, java.util.Date, java.util.Date)
	 */
	@Override
	public IStatus addTask(ITask task, Date begin, Date end) {

		if (isConnected() == false)
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					Messages.PHPReport_TASK_SYNC_FAILED
							+ Messages.PHPReport_NOT_AUTHENTICATED);

		PostMethod method = new PostMethod(create_task_location.getUrl());
		String taskXmlRepresentation = String.format(
				"<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>" + "<tasks>" //$NON-NLS-1$ //$NON-NLS-2$
						+ "<task sid=\"%s\">" + "<date>%s</date>" //$NON-NLS-1$ //$NON-NLS-2$
						+ "<initTime>%s</initTime>" + "<endTime>%s</endTime>" //$NON-NLS-1$ //$NON-NLS-2$
						+ "<story>%s</story>" + "<text>%s</text>" //$NON-NLS-1$ //$NON-NLS-2$
						+ "<telework>%b</telework>" //$NON-NLS-1$
						+ "<ttype>implementation</ttype>" //$NON-NLS-1$
						+ "</task>" + "</tasks>", //$NON-NLS-1$ //$NON-NLS-2$
				token, DateFormatUtils.ISO_DATE_FORMAT.format(begin),
				DateFormatUtils.format(begin, "HH:mm"), //$NON-NLS-1$
				DateFormatUtils.format(end, "HH:mm"), //$NON-NLS-1$
				task.getUrl(), task.getSummary(), teleworking);

		try {
			method.setRequestEntity(new StringRequestEntity(
					taskXmlRepresentation, "text/xml", "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		method.setQueryString(new NameValuePair[] { new NameValuePair(
				"sid", token), //$NON-NLS-1$
		});

		PHPReportMethodResponseHandler responseHandler = new PHPReportMethodResponseHandler();

		return sendRequest(method, create_task_location, responseHandler,
				Messages.PHPReport_TASK_SYNC_FAILED,
				Messages.PHPReport_TASK_SYNC_SUCCESS);

	}

	private IStatus sendRequest(HttpMethodBase method, WebLocation location,
			PHPReportErrorResponseHandler responseHandler, String failMessage,
			String successMessage) {
		HostConfiguration hostConfiguration = WebUtil.createHostConfiguration(
				httpClient, location, null);
		try {
			int httpStatus = WebUtil.execute(httpClient, hostConfiguration,
					method, null);
			if (httpStatus == HttpStatus.SC_OK) {
				InputStream response = WebUtil.getResponseBodyAsStream(method,
						null);
				SAXParserFactory factory = SAXParserFactory.newInstance();
				factory.setValidating(false);
				SAXParser parser = factory.newSAXParser();

				parser.parse(response, responseHandler);
			} else
				status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						failMessage + Messages.PHPReport_HTTP_ERROR
								+ httpStatus);

		} catch (Exception e) {
			status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					failMessage, e);
		} finally {
			if (status != null)
				return status;
			if (responseHandler.isSucceed() == false)
				status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						failMessage + Messages.PHPReport_SERVER_RESPONDED
								+ responseHandler.getErrorMessage());
			else
				status = new Status(IStatus.OK, Activator.PLUGIN_ID,
						successMessage);
			return status;
		}
	}

	private HttpClient createHttpClient(String userAgent) {
		HttpClient httpClient = new HttpClient();
		httpClient.setHttpConnectionManager(WebUtil.getConnectionManager());
		httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
		WebUtil.configureHttpClient(httpClient, userAgent);
		return httpClient;
	}

	@Override
	public IStatus getStatus() {
		return status;
	}
}
