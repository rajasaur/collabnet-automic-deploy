package hudson.plugins.collabnet;

import com.collabnet.ce.soap50.webservices.ClientSoapStubFactory;
import com.collabnet.ce.soap50.webservices.cemain.ICollabNetSoap;
import com.collabnet.ce.soap50.webservices.cemain.ProjectSoapList;
import com.collabnet.ce.soap50.webservices.cemain.ProjectSoapRow;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.util.ListBoxModel;
import org.kohsuke.stapler.DataBoundConstructor;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * UC4 ARA RM Component.
 * Defines the attributes that are required for a component in UC4 Release Manager.
 *
 * Added support for showing projects per ARA Component
 */
public class Component extends AbstractDescribableImpl<Component> {
    private String name;
    private String path;
    private String codeLink;
    private String project;

    @DataBoundConstructor
    public Component(String name, String path, String codeLink, String project) {
        this.name = name;
        this.path = path;
        this.codeLink = codeLink;
        this.project = project;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCodeLink() {
        return codeLink;
    }

    public void setCodeLink(String codeLink) {
        this.codeLink = codeLink;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<Component> {
        public String getDisplayName() {
            return "Component";
        }

        /**
         * Code that gets called from Jenkins to populate the select control
         * @return ListBoxModel containing the project name and id
         */
        public ListBoxModel doFillProjectItems() {
            ListBoxModel items = new ListBoxModel();
            String sessionId = null;
            Class<?> c = null;
            Method method = null;

            try {
                // Fetch the Session from the global configuration
                c = Hudson.getInstance().getPluginManager().uberClassLoader.loadClass("hudson.plugins.collabnet.auth.CNAuthentication");
                method = c.getMethod("getSessionId", new Class[] {});
                sessionId = (String) method.invoke(c.cast(Hudson.getAuthentication()));
            } catch(Exception e) {
                e.printStackTrace();
            }

            String url = null;
            try {
                // Fetch the URL from global configuration
                c = Hudson.getInstance().getPluginManager().uberClassLoader.loadClass("hudson.plugins.collabnet.auth.CollabNetSecurityRealm");
                method = c.getMethod("getCollabNetUrl", new Class[] {});
                url = (String) method.invoke(c.cast(Hudson.getInstance().getSecurityRealm()));
            } catch(Exception e) {
                e.printStackTrace();
            }

            if (sessionId != null) {
                String soapURL = url + "/ce-soap50/services/CollabNet?wsdl";
                ICollabNetSoap cnSoap =  (ICollabNetSoap) ClientSoapStubFactory.
                    getSoapStub(ICollabNetSoap.class, soapURL);
                try {
                    ProjectSoapList projectList = cnSoap.getProjectList(sessionId);
                    for(ProjectSoapRow row: projectList.getDataRows()) {
                        items.add(row.getTitle(), row.getId());
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            return items;
        }
    }
}
