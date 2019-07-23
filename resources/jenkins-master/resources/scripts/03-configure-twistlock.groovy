import org.jenkinsci.plugins.twistlock.builder.BuildScannerDescriptor
import jenkins.model.Jenkins

import java.util.logging.Logger

def logger = Logger.getLogger("")
log = { message ->
  logger.info("${message}..")
}

log "Configure Twistlock"

// get Jenkins instance
def jenkins = Jenkins.getInstance()

def descriptor = jenkins.getDescriptorByType(BuildScannerDescriptor)

// get Twistlock plugin
descriptor.setAddress("http://twistlock.apps.mdas2-def.demo.us")
descriptor.setUsername("svc-jenkins-ci");
descriptor.setPassword("changeit");

descriptor.save()
