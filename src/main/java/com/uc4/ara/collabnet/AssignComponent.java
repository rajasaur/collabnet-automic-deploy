package com.uc4.ara.collabnet;

import com.uc4.ara.collabnet.JArgs.CmdLineParser;
import com.uc4.ara.collabnet.Logging.ErrorCodes;
import com.uc4.ara.collabnet.Logging.Logger;
import com.uc4.ara.collabnet.rm.ImportExportServiceFactory;
import com.uc4.ara.rm.ImportExportServiceSoapProxy;
import com.uc4.ara.rm.Result;

public class AssignComponent extends AbstractFeature {

	private CmdLineParser.Option<String> url;
	private CmdLineParser.Option<String> username;
	private CmdLineParser.Option<String> password;
	private CmdLineParser.Option<String> packageArg;
	private CmdLineParser.Option<String> component;
	private CmdLineParser.Option<String> projectid;
	private CmdLineParser.Option<String> sourcepath;
	private CmdLineParser.Option<String> releaseid;
	private CmdLineParser.Option<String> sourcelink;
	private CmdLineParser.Option<String> codelink;
	private CmdLineParser.Option<String> artifactid;
	private CmdLineParser.Option<Boolean> silent;

	
	@Override
	public int execute(CmdLineParser parser) {
		try {
			String urlValue = parser.getOptionValue(url);
			String usernameValue = parser.getOptionValue(username);
			String passwordValue = parser.getOptionValue(password);
			String packageValue = parser.getOptionValue(packageArg);
			String componentValue = parser.getOptionValue(component);
			String projectIdValue = parser.getOptionValue(projectid);
			String sourcePathValue = parser.getOptionValue(sourcepath);
			String releaseIdValue = parser.getOptionValue(releaseid);
			String sourceLinkValue = parser.getOptionValue(sourcelink);
			String codeLinkValue = parser.getOptionValue(codelink);
			String artifactIdValue = parser.getOptionValue(artifactid);
			Boolean silentValue = parser.getOptionValue(silent);
			
			if(silentValue == null)
				silentValue = false;
			
			//export package
			ImportExportServiceSoapProxy importExportService = 
					ImportExportServiceFactory.getImportExportServiceFromUrl(urlValue);
			String[] properties = new String[]{};
			String[] conditions = new String[]{"system_id eq '" + packageValue + "'"};
			Result exportPackageResult = importExportService.export(usernameValue, passwordValue, "Package", "", 0, 10, "XML", properties, "", "", conditions);
			exportPackageResult = handleResult(exportPackageResult, importExportService);
			
			//find application id
			String applicationId = getNodeValue(exportPackageResult, "Property", "name", "system_application.system_id");
			
			//export component id
			properties = new String[]{};
			conditions = new String[]{"system_name eq '" + componentValue + "'", "system_application.system_id eq '" + applicationId + "'"};
			Result exportComponenteResult = importExportService.export(usernameValue, passwordValue, "Component", "", 0, 10, "XML", properties, "", "", conditions);
			exportComponenteResult = handleResult(exportComponenteResult, importExportService);

			//find component id			
			String componentId = getNodeValue(exportComponenteResult, "Property", "name", "system_id");
			
			//import packagecomponentrelation
			String data = getPackageComponentRelation(packageValue, componentId);
			Result importResult = importExportService._import(usernameValue, passwordValue, "PackageComponentRelation", "XML", true, data);
			importResult = handleResult(importResult, importExportService);
			
			
			//if dynamic properties set - then overwrite on package
			///integration/teamforge/project_id
			if(projectIdValue != null) {
				handleDynamicProperty(usernameValue, passwordValue, projectIdValue, "/integration/teamforge/project_id", silentValue, importExportService, componentId, packageValue);
			}
			
			///integration/teamforge/source_path			
			if(sourcePathValue != null) {
				handleDynamicProperty(usernameValue, passwordValue, sourcePathValue, "/integration/teamforge/source_path", silentValue, importExportService, componentId, packageValue);
			}

			///integration/teamforge/file_release_id
			if(releaseIdValue != null) {
				handleDynamicProperty(usernameValue, passwordValue, releaseIdValue, "/integration/teamforge/file_release_id", silentValue, importExportService, componentId, packageValue);
			}

			///integration/teamforge/source_link
			if(sourceLinkValue != null) {
				handleDynamicProperty(usernameValue, passwordValue, sourceLinkValue, "/integration/teamforge/source_link", silentValue, importExportService, componentId, packageValue);
			}

			///integration/teamforge/source_code_link
			if(codeLinkValue != null) {
				handleDynamicProperty(usernameValue, passwordValue, codeLinkValue, "/integration/teamforge/source_code_link", silentValue, importExportService, componentId, packageValue);
			}
			
			///integration/teamforge/artefact_id
			if(artifactIdValue != null) {
				handleDynamicProperty(usernameValue, passwordValue, artifactIdValue, "/integration/teamforge/artifact_id", silentValue, importExportService, componentId, packageValue);
			}
			
			if(!silentValue)
				Logger.logMsg("Assigned Component " + componentValue + " to Package " + packageValue + " successfully.");
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

	private void handleDynamicProperty(String user, 
			String password, 
			String value, 
			String name, 
			boolean isSilent, 
			ImportExportServiceSoapProxy importExportService, 
			String componentId, 
			String packageId) throws Exception {
		if(!isSilent){
			Logger.logMsg("Overwriting DynamicProperty " + name + " with value '" + value + "'");
		}
		
		String dynamicPropertyXml = getDynamicPropertyXmlForOverrideComponentOnPackage(packageId, componentId, name, value);
		Result importResult = importExportService._import(user, password, "DynamicProperty", "XML", true, dynamicPropertyXml);
		importResult = handleResult(importResult, importExportService);
	}

	private String getDynamicPropertyXmlForOverrideComponentOnPackage(String packageId, 
			String componentId, 
			String name,
			String value) {
		StringBuilder builder = new StringBuilder();
		builder.append("<Sync xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"sync.xsd\">");
		builder.append("<Entity mainType=\"DynamicProperty\">");
		builder.append(getProperty("system_on_entity.system_id", packageId, true));
		builder.append(getProperty("system_on_maintype", "Package", true));
		builder.append(getProperty("system_overrides_entity.system_id", componentId));
		builder.append(getProperty("system_overrides_maintype", "Component"));
		builder.append(getProperty("system_full_name", name, true));
		builder.append(getProperty("system_value", value));
		builder.append(getProperty("system_type", "SingleLineText"));
		builder.append("</Entity>");
		builder.append("</Sync>");
		
		return builder.toString();
	}

	private String getPackageComponentRelation(String packageId, String componentId) {
		StringBuilder builder = new StringBuilder();
		builder.append("<Sync xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"sync.xsd\">");
		builder.append("<Entity mainType=\"PackageComponentRelation\">");
		builder.append(getProperty("system_package.system_id", packageId, true));
		builder.append(getProperty("system_component.system_id", componentId, true));
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
                "Username used to authenticate with the UC4 Release Manager and used for the assign of components.");
		
		password = parser.addHelp(
                parser.addStringOption("p", "password", true),
                "Password used to authenticate with UC4 Release Manager. This can be encrypted using the encryption function."); 
                		
		packageArg = parser.addHelp(
				parser.addStringOption("i", "package", true),
                "The ID of the Deployment Package to which a Component shall be assigned.");
		
		component = parser.addHelp(
                parser.addStringOption("c", "component", true),
                "The name of the Component which shall be assigned to the Deployment Package.");
		
		projectid = parser.addHelp(
                parser.addStringOption("pid", "projectid", false),
                "ID of the TeamForge Project to which this Component belongs to in the Deployment Package context.");
		
		sourcepath = parser.addHelp(
                parser.addStringOption("sp", "sourcepath", false),
                "Physical path to where the package source files can be found.");
		
		releaseid = parser.addHelp(
                parser.addStringOption("rid", "releaseid", false),
                "ID of the TeamForge File Release which contains the package source files. This attribute has priority over the physical source path property.");
		
		sourcelink = parser.addHelp(
                parser.addStringOption("sl", "sourcelink", false),
                "Link to the source files which will be shown in TeamForge.");
		
		codelink = parser.addHelp(
                parser.addStringOption("cl", "codelink", false),
                "Link to the source code which will be shown in TeamForge.");
		
		artifactid = parser.addHelp(
                parser.addStringOption("aid", "artifactid", false),
                "The artifact ID from TeamForge.");
		
		silent = parser.addHelp(
                parser.addBooleanOption("s", "silent", false),
                "Causes the function to not produce any output. Errors will still be shown. In case this argument is not set, the function will output success messages.");
	
		parser.setExamples("\tjava -jar ARATeamForgeUtility.jar AssignComponent -l \"https://services.local/rm\" -u \"rmuser\" -p \"mypassword\" -i 8378 -c \"Frontend\"\r\n\r\n" + 
		"\tjava -jar ARATeamForgeUtility.jar AssignComponent -l \"https://services.local/rm\" -u \"rmuser\" -p \"mypassword\" -i 8378 -c \"Frontend\" -pid 8347 -rid \"rel:2009\" -sl \"sf/frs/do/viewRelease/projects.collabnet_agile_baseline/frs.product_1.release_1\"\r\n\r\n" +
		"\tjava -jar ARATeamForgeUtility.jar AssignComponent -l \"https://services.local/rm\" -u \"rmuser\" -p \"mypassword\" -i 8378 -c \"Frontend\" -pid 8347 -sp \"\\\\buildshare\\builds\\myapp\\daily\\82\\\" -s\r\n\r\n" +
		"\tjava -jar ARATeamForgeUtility.jar AssignComponent --url \"https://services.local/rm\" --username \"rmuser\" --password \"mypassword\" --package 8378 --component \"Frontend\" -projectid 8347 --sourcepath \"\\\\buildshare\\builds\\myapp\\daily\\82\\\"");


	}

}

