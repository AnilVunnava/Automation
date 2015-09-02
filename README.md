# Automation
# Making code changes

Install the Git client for your operating system, and from your command line run

git clone ssh://55e6c4910c1e662bd1000060@jenkins-automationv1.rhcloud.com/~/git/jenkins.git/

cd jenkins/

This will create a folder with the source code of your application. After making a change, add, commit, and push your changes.

git add .

git commit -m 'My changes'

git push

When you push changes the OpenShift server will report back its status on deploying your code. The server will run any of your configured deploy hooks and then restart the application.
