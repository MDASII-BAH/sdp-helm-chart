import java.util.logging.Logger

import jenkins.*
import jenkins.model.*

import org.jenkinsci.plugins.github.GitHubPlugin
import org.jenkinsci.plugins.github.config.GitHubServerConfig

def logger = Logger.getLogger("")
log = { message ->
  logger.info("${message}..")
}

log "Configure Github"

GitHubServerConfig server = new GitHubServerConfig("github-token")
server.setName("Github")
server.setManageHooks(true)

GitHubPlugin.configuration().getConfigs().add(server)
