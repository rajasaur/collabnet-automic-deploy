####Jenkins Plugin for Continuous Deployment using Collabnet Teamforge and UC4


This plugin allows a developer to connect their [Collabnet Teamforge](http://www.collab.net/products/teamforge) project to a [UC4 Deployment Manager](http://www.uc4.com/solutions/application-release-automation) system. 

Once a Jenkins job has been configured with this plugin as a post-build action along with a Collabnet Teamforge(CTF) Repository, every successful build  on that repository will create a UC4 Deployment Package along with its components. The packages, components and other configuration information can all be fed through the plugin (See screenshot below). This allows for continuous deployment feature between a CTF repository and UC4's deployment manager. 

Please refer to http://www.collab.net/deploy for the complete details on the integration.

#### Screenshots
<div>
<b>Jenkins Configuration of a Job with the Collabnet UC4 Deploy plugin</b>
<img src="http://rajasaur.github.com/rb-extension-pack/screenshots/jenkins_plugin.png" width=800 height=500" /> 
</div>


#### Dependencies
* Collabnet Plugin (https://wiki.jenkins-ci.org/display/JENKINS/CollabNet+Plugin)
* Collabnet Teamforge 
* UC4 Deployment Manager 
