package com.uc4.ara.collabnet.rm;

import java.net.MalformedURLException;
import java.net.URL;


import com.uc4.ara.rm.ImportExportServiceSoapProxy;

/**
 * Service Factory for talking to UC4 Deployment Manager. This is an .NET application that
 * allows components and packages to be created in Deployment manager.
 */
public class ImportExportServiceFactory {
	/**
	 * The Constant IMPORTEXPORTSERVICE.
	 */
	public static final String IMPORTEXPORTSERVICE = "/service/ImportExportService.asmx?wsdl";
	
	public static ImportExportServiceSoapProxy getImportExportServiceFromUrl(String url) throws MalformedURLException {
		ImportExportServiceSoapProxy importExportService = new ImportExportServiceSoapProxy(new URL(url + IMPORTEXPORTSERVICE).toString());
		 
		return importExportService;
	}
}
