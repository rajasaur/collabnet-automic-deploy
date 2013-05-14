package hudson.plugins.collabnet;

import com.uc4.ara.collabnet.AssignComponent;
import com.uc4.ara.collabnet.CreatePackage;
import com.uc4.ara.collabnet.JArgs.CmdLineParser;
import hudson.Extension;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.scm.ChangeLogSet;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Jenkins Post-build plugin that listens for build changes and notifies UC4 ARA to create a
 * new package when build is successful.
 *
 */
public class UC4CreatePackage extends Notifier {

    private String server;      // UC4 Server Location
    private String clientId;    // UC4 Client Id (for login)
    private String user;        // UC4 Username
    private String department;  // Department for user
    private String password;
    private String packageName;    // Package name to be created(can contain jenkins variables)
    private String applicationName; // Name of UC4 RM Application
    private List<Component> components = new ArrayList<Component>();  // Components that make up the application
    private String owner;           // Owner for the package
    private String folderName;      // Folder access level for the package
    private String packageType;     // Package Type
    private static Pattern mPattern = Pattern.compile("\\s*\\[(([a-z]{3,8}\\d{4,}\\s*,?\\s*)+)].*?");

    @DataBoundConstructor
    public UC4CreatePackage(String server, String clientId, String user, String department,
                            String password, String packageName, String applicationName,
                            List<Component> components, String owner, String folderName,
                            String packageType) {
        this.server = server;
        this.clientId = clientId;
        this.user = user;
        this.department = department;
        this.password = password;
        this.packageName = packageName;
        this.applicationName = applicationName;
        this.components = components;
        this.owner = owner;
        this.folderName = folderName;
        this.packageType = packageType;
    }

    public String getServer() {
        return server;
    }

    public String getClientId() {
        return clientId;
    }

    public String getUser() {
        return user;
    }

    public String getDepartment() {
        return department;
    }

    public String getPassword() {
        return password;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public List<Component> getComponents() {
        return components;
    }

    public String getOwner() {
        return owner;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getPackageType() {
        return packageType;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    /**
     * Utility method to get a comma separated list of artifacts that make up a build
     *
     * @param artifactIds Set Of artifactids
     * @param delimiter Delimiter when joining to a string
     * @return comma separated list of artifact ids
     */
    private String join(Iterable<? extends CharSequence> artifactIds, String delimiter) {
        Iterator<? extends CharSequence> iter = artifactIds.iterator();
        if (!iter.hasNext()) return "";
        StringBuilder buffer = new StringBuilder(iter.next());
        while (iter.hasNext()) buffer.append(delimiter).append(iter.next());
        return buffer.toString();
    }

    /**
     * Jenkins callback for post-build notification.
     */
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
                               BuildListener listener) throws IOException, InterruptedException {

        Set<String> artifactIdsSet = new HashSet<String>();
        ChangeLogSet logs = build.getChangeSet();
        for (Iterator iterator = logs.iterator(); iterator.hasNext(); ) {
            ChangeLogSet.Entry entry = (ChangeLogSet.Entry) iterator.next();
            Matcher matches = mPattern.matcher(entry.getMsg());
            if (matches.find()) {
                if (matches.groupCount() >= 2) {
                    StringTokenizer idTokenizer = new StringTokenizer(matches.group(1), ",");
                    while (idTokenizer.hasMoreTokens()) {
                        String nextToken = idTokenizer.nextToken();
                        if (nextToken.startsWith("artf")) {
                            artifactIdsSet.add(nextToken);
                        }
                    }
                }
            }
        }

        String artifactIds = null;
        if (artifactIdsSet.size() != 0) {
            artifactIds = join(artifactIdsSet, ",");
        }
        listener.getLogger().println("ArtifactIds Involved: " + artifactIds);

        String resolvedPackageName = Util.replaceMacro(
                    this.packageName, build.getEnvironment(listener));

        listener.getLogger().println("Home: " + System.getProperty("user.home"));
        listener.getLogger().println("Build ID: " + build.getId());
        listener.getLogger().println("Build Number: " + build.getNumber());
        listener.getLogger().println("Server: " + this.server);
        String user = this.clientId + "/" + this.user + "/" + this.department;
        listener.getLogger().println("User: " + user);
        listener.getLogger().println("Package Name: " + this.packageName);
        listener.getLogger().println("Resolved Package Name: " + resolvedPackageName);
        listener.getLogger().println("App Name: " + this.applicationName);
        listener.getLogger().println("Owner: " + this.owner);
        listener.getLogger().println("Folder Name: " + this.folderName);
        for(Component comp: getComponents()) {
            listener.getLogger().println("Name: " + comp.getName() + ", Path: " + comp.getPath());
        }

        if (this.owner == null) {
            this.owner = user;
        }

        // Create Package
        CreatePackage createPackage = new CreatePackage();

        CmdLineParser parser = new CmdLineParser();
        createPackage.setParams(parser);

        try {
            parser.parse(new String[] {
                    "-l", this.server,
                    "-u", user,
                    "-p", this.password,
                    "-a", this.applicationName,
                    "-n", resolvedPackageName,
                    "-t", this.packageType,
                    "-f", this.folderName,
                    "-o", this.owner
            });
        } catch (CmdLineParser.OptionException e) {
            listener.getLogger().println("Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        LoggedPrintStream lpsOut = LoggedPrintStream.create(System.out);
        LoggedPrintStream lpsErr = LoggedPrintStream.create(System.err);

        // Redirect stdout and stderr to LoggedPrintStream
        System.setOut(lpsOut);
        System.setErr(lpsErr);
        createPackage.execute(parser);

        Pattern pattern = Pattern.compile("RESULT-ID: (\\d+)");
        Matcher match = pattern.matcher(lpsOut.buf);
        String packageId = null;
        if (match.find()) {
            packageId = match.group(1);
            listener.getLogger().println("Package ID:" + packageId);
        }
        System.setOut(lpsOut.underlying);
        System.setErr(lpsErr.underlying);

        // Run AssignComponent for each of the components
        for (Component comp: this.getComponents()) {
            List<String> commands = new ArrayList<String>();
            commands.add("-l");
            commands.add(this.server);
            commands.add("-u");
            commands.add(user);
            commands.add("-p");
            commands.add(this.password);
            commands.add("-i");
            commands.add(packageId);
            commands.add("-c");
            commands.add(comp.getName());
            commands.add("-sp");
            commands.add(Util.replaceMacro(comp.getPath(),build.getEnvironment(listener)));
            if (comp.getCodeLink() != null && !"".equals(comp.getCodeLink())) {
                commands.add("-cl");
                commands.add(comp.getCodeLink());
            }
            if (comp.getProject() != null && !"".equals(comp.getProject())) {
                commands.add("-pid");
                commands.add(comp.getProject());
            }
            if (artifactIds != null) {
                commands.add("-aid");
                commands.add(artifactIds);
            }
            commands.add("-s");
            String[] assignComponentParams = new String[commands.size()];
            commands.toArray(assignComponentParams);

            AssignComponent assignComponent = new AssignComponent();
            parser = new CmdLineParser();
            assignComponent.setParams(parser);

            try {
                parser.parse(assignComponentParams);
            } catch (CmdLineParser.OptionException e) {
                listener.getLogger().println("Error: " + e.getMessage());
                e.printStackTrace();
                return false;
            }

            lpsOut = LoggedPrintStream.create(System.out);
            lpsErr = LoggedPrintStream.create(System.err);
            System.setOut(lpsOut);
            System.setErr(lpsErr);

            assignComponent.execute(parser);
            listener.getLogger().println("o/p Assign component:" + lpsOut.buf.toString());

            System.setOut(lpsOut.underlying);
            System.setErr(lpsErr.underlying);
        }

        return true;
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        public FormValidation doCheckServer(@QueryParameter String server) {
            if (server.length() == 0)
                return FormValidation.error("Please enter a server url");

            if (server.indexOf("://") == -1)
                return FormValidation.error("Enter a url of the format http(s)://<server>");

            return FormValidation.ok();
        }

        public FormValidation doCheckClientId(@QueryParameter String clientId) {
            return FormValidation.validatePositiveInteger(clientId);
        }

        public FormValidation doCheckUser(@QueryParameter String user) {
            return FormValidation.validateRequired(user);
        }

        public FormValidation doCheckDepartment(@QueryParameter String department) {
            return FormValidation.validateRequired(department);
        }

        public FormValidation doCheckPassword(@QueryParameter String password) {
            return FormValidation.validateRequired(password);
        }

        public FormValidation doCheckPackageName(@QueryParameter String packageName) {
            return FormValidation.validateRequired(packageName);
        }

        public FormValidation doCheckApplicationName(@QueryParameter String applicationName) {
            return FormValidation.validateRequired(applicationName);
        }

        public FormValidation doCheckFolderName(@QueryParameter String folderName) {
            return FormValidation.validateRequired(folderName);
        }

        public FormValidation doCheckPackageType(@QueryParameter String packageType) {
            return FormValidation.validateRequired(packageType);
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Set UC4 RM Properties";
        }
    }
}

/**
 * Capture the output of ARATeamforgeUtility.jar and get the contents. The JAR file seems to
 * be outputting values to Stdout, so we need to capture it by wrapping the outputstream
 */
class LoggedPrintStream extends PrintStream {

    final StringBuilder buf;
    final PrintStream underlying;

    LoggedPrintStream(StringBuilder sb, OutputStream os, PrintStream ul) {
        super(os);
        this.buf = sb;
        this.underlying = ul;
    }

    public static LoggedPrintStream create(PrintStream toLog) {
        try {
            final StringBuilder sb = new StringBuilder();
            Field f = FilterOutputStream.class.getDeclaredField("out");
            f.setAccessible(true);
            OutputStream psout = (OutputStream) f.get(toLog);
            return new LoggedPrintStream(sb, new FilterOutputStream(psout) {
                public void write(int b) throws IOException {
                    super.write(b);
                    sb.append((char) b);
                }
            }, toLog);
        } catch (NoSuchFieldException shouldNotHappen) {
        } catch (IllegalArgumentException shouldNotHappen) {
        } catch (IllegalAccessException shouldNotHappen) {
        }
        return null;
    }
}

