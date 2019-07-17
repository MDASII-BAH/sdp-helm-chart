# Copyright © 2018 Booz Allen Hamilton. All Rights Reserved.
# This software package is licensed under the Booz Allen Public License. The license can be found in the License file or at http://boozallen.github.io/licenses/bapl

FROM jenkins/jenkins:2.176.1

ENV JAVA_OPTS "-Djenkins.install.runSetupWizard=false"
ENV JTE_RELEASE "1.1.1"

EXPOSE 8080
EXPOSE 50000

USER root

# install oc client
RUN mkdir -p /tmp/oc-client && cd /tmp/oc-client && \
    wget -O oc-client.tar.gz https://github.com/openshift/origin/releases/download/v3.11.0/openshift-origin-client-tools-v3.11.0-0cbc58b-linux-64bit.tar.gz && \
    tar -xvf oc-client.tar.gz && \
    mv */oc /usr/bin/oc

# install plugins
COPY resources/plugins/plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
COPY resources/plugins/twistlock-jenkins-plugin_19_03_321.hpi /var/jenkins_home/plugins/twistlock-jenkins-plugin.hpi
RUN chmod 777 /var/jenkins_home/plugins/twistlock-jenkins-plugin.hpi

# insert configuration script
COPY resources/scripts/configure.groovy /usr/share/jenkins/ref/init.groovy.d/configure.groovy
COPY resources/scripts/configure-twistlock.groovy /usr/share/jenkins/ref/init.groovy.d/configure-twistlock.groovy
RUN chmod -R 777 /usr/share/jenkins/ref/init.groovy.d/*

# insert job dsl scripts
COPY resources/jobs/* /usr/share/jenkins/ref/init.jobdsl.d/
RUN chmod -R 777 /usr/share/jenkins/ref/init.jobdsl.d/*

# copy in entry point scripts
COPY resources/scripts/jenkins-preboot.sh /usr/local/bin/jenkins_preboot
COPY resources/scripts/jenkins.sh /usr/local/bin/jenkins.sh
RUN chmod +x /usr/local/bin/jenkins_preboot && chmod 777 /usr/local/bin/jenkins_preboot /usr/local/bin/jenkins.sh

# modify permissions for OpenShift
RUN chgrp -R 0 $JENKINS_HOME && \
    chmod -R g=u $JENKINS_HOME /etc/passwd

