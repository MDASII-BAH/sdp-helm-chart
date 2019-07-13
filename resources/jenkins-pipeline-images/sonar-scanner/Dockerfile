# Copyright © 2018 Booz Allen Hamilton. All Rights Reserved.
# This software package is licensed under the Booz Allen Public License. The license can be found in the License file or at http://boozallen.github.io/licenses/bapl

FROM openjdk:8-alpine

ARG RELEASE=4.0.0.1744

ENV SONAR_RUNNER_HOME=/root/sonar-scanner
ENV PATH=$PATH:$SONAR_RUNNER_HOME/bin
ENV NODE_PATH=/usr/lib/node_modules/

WORKDIR /root

RUN apk add --no-cache curl grep sed unzip nodejs npm && \
    npm install tslint typescript tslint-sonarts -g && \
    tslint --init && \
    curl --insecure -o ./sonarscanner.zip -L https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-${RELEASE}-linux.zip && \
    unzip sonarscanner.zip && \
    rm sonarscanner.zip && \
    rm -rf sonar-scanner-${RELEASE}-linux/jre && \
    mv ./sonar-scanner-${RELEASE}-linux ./sonar-scanner 

COPY sonar-runner.properties ./sonar-scanner/conf/sonar-scanner.properties

#   ensure Sonar uses the provided Java for must instead of a borked glibc one
RUN sed -i 's/use_embedded_jre=true/use_embedded_jre=false/g' /root/sonar-scanner/bin/sonar-scanner

# Use bash if you want to run the environment from inside the shell, otherwise use the command that actually runs the underlying stuff
#CMD /bin/bash
CMD sonar-scanner
