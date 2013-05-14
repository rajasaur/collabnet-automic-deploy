package com.uc4.ara.collabnet;

import com.uc4.ara.collabnet.JArgs.CmdLineParser;
import com.uc4.ara.collabnet.Logging.ErrorCodes;
import com.uc4.ara.collabnet.Logging.Logger;
import com.uc4.ara.collabnet.rm.ImportExportServiceFactory;
import com.uc4.ara.rm.ImportExportServiceSoapProxy;
import com.uc4.ara.rm.Result;

public class CreatePackage extends AbstractFeature {

	private CmdLineParser.Option<String> url;
	private CmdLineParser.Option<String> username;
	private CmdLineParser.Option<String> password;
	private CmdLineParser.Option<String> application;
	private CmdLineParser.Option<String> name;
	private CmdLineParser.Option<String> type;
	private CmdLineParser.Option<String> folder;
	private CmdLineParser.Option<String> owner;
	
	
	@Override
	public int execute(CmdLineParser parser) {
		try {
			String urlValue = parser.getOptionValue(url);
			String usernameValue = parser.getOptionValue(username);
			String passwordValue = parser.getOptionValue(password);
			String applicationValue = parser.getOptionValue(application);
			String nameValue = parser.getOptionValue(name);
			String typeValue = parser.getOptionValue(type);
			String folderValue = parser.getOptionValue(folder);
			String ownerValue = parser.getOptionValue(owner);

			//if no owner is provided use user to connect to rm
			if(ownerValue == null)
				ownerValue = usernameValue;
			
			//connect to rm
			ImportExportServiceSoapProxy importExportService = 
					ImportExportServiceFactory.getImportExportServiceFromUrl(urlValue);
			
			//build package xml
			String xmlPackage = getPackageXML(nameValue, applicationValue, typeValue, folderValue, ownerValue);

			//import package 
			Result packageImportResult = 
					importExportService._import(usernameValue, passwordValue, "Package", "XML", true, xmlPackage);
			
			packageImportResult = handleResult(packageImportResult, importExportService);
			
			//export package-component assigns
			long packageId = packageImportResult.getStatus();
			Logger.logMsg("RESULT-ID: " + packageId);
			String[] conditions = new String[] {"system_package.system_id eq '" + packageId + "'"}; 
			Result packageComponentAssignsResult = 
					importExportService.export(usernameValue, passwordValue, "PackageComponentRelation", "", 0, 1000, "XML", new String[]{}, "", "", conditions);
			
			packageComponentAssignsResult = handleResult(packageComponentAssignsResult, importExportService);
			
			//import delete package-component assigns
			if(packageComponentAssignsResult.getData().contains("Entity")) {
				Result deleteComponentAssignments = importExportService.delete(usernameValue, passwordValue, "", "XML", true, packageComponentAssignsResult.getData());
				deleteComponentAssignments = handleResult(deleteComponentAssignments, importExportService);
			}
			
			Logger.logMsg("Created Package " + nameValue + " successfully.");
		} catch (IllegalStateException ex) {
			Logger.logMsg(ex.getMessage());
			parser.printUsage(this.getClass().getSimpleName());
			return ErrorCodes.ERROR;
		}
		catch (Exception ex) {
			parser.printUsage(this.getClass().getSimpleName());
			Logger.logException(ex);
			return ErrorCodes.EXCEPTION;
		}
		return ErrorCodes.OK;
	}

	private String getPackageXML(String name, String application, String type, String folder, String owner) {
		StringBuilder builder = new StringBuilder();
		builder.append("<Sync xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"sync.xsd\">");
		builder.append("<Entity mainType=\"Package\"");
		builder.append(" customType=\"");
		builder.append(type);
		builder.append("\">");
		builder.append(getProperty("system_name", name));
		builder.append(getProperty("system_application.system_name", application));
		builder.append(getProperty("system_folder.system_name", folder));
		builder.append(getProperty("system_owner.system_name", owner));
		builder.append("</Entity>");
		builder.append("</Sync>");
		
		
		return builder.toString();
	}

	@Override
	public void setParams(CmdLineParser parser) {
		url = parser.addHelp(
                parser.addStringOption("l", "url", true),
                "The URL of the UC4 Release Manager Application. Example: http://releasemanager.intranet.local");
		
		username = parser.addHelp(
                parser.addStringOption("u", "username", true),
                "Username used to authenticate with the UC4 Release Manager and used for the Package creation.");
		
		password = parser.addHelp(
                parser.addStringOption("p", "password", true),
                "Password used to authenticate with UC4 Release Manager. This can be encrypted using the encryption function."); 

		application = parser.addHelp(
                parser.addStringOption("a", "application", true),
                "Name of the application in UC4 RM to which the Deployment Package belongs to.");
		
		name = parser.addHelp(
                parser.addStringOption("n", "name", true),
                "Name of the Deployment Package which should be created.");
		
		type = parser.addHelp(
                parser.addStringOption("t", "type", true),
                "The type of the Deployment Package.");
		
		folder = parser.addHelp(
                parser.addStringOption("f", "folder", true),
                "The UC4 RM folder in which the Deployment Package should be placed.");
		
		owner = parser.addHelp(
                parser.addStringOption("o", "owner", false),
                "The owner (can be a user or team) of the Deployment Package. In case this argument was not given, the owner will be set to the executing user (-u).");
		
		parser.setExamples("\tjava -jar ARATeamForgeUtility.jar CreatePackage -l \"https://services.local/rm\" -u \"rmuser\" -p \"mypassword\" -a \"Sample Application\" -n \"AutoBuild 879\" -t \"Deployment\" -f \"myfolder\"" +
							"\r\n\r\n\tjava -jar ARATeamForgeUtility.jar CreatePackage --url \"http://myrm.local\" --username \"rmuser\" --password \"mypassword\" --application \"MyApplication\" --name \"123\" --type \"Deployment\" --folder \"Operations\"");
	}

}
