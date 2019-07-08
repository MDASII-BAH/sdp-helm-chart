libraryRepo = "https://github.com/MDASII-BAH/sdp-libraries.git"
libraryRepoCredId = "github"
pipelineConfigRepo = "https://github.com/MDASII-BAH/Challenge2BackEnd.git"
pipelineConfigRepoCredId = "github"
configBaseDir = ""

multibranchPipelineJob('backend') {
  // branchSources {
  //   git {
  //     remote('https://github.com/MDASII-BAH/Challenge2BackEnd.git')
  //     credentialsId pipelineConfigRepoCredId
  //   }
  // }

  configure { 

    it / sources / 'data' / 'jenkins.branch.BranchSource' << {
      source(class: 'org.jenkinsci.plugins.github_branch_source.GitHubSCMSource') {
        // id("4acd63de-a105-11e9-a2a3-2a2ae2dbcce4")
        // id(UUID.randomUUID())
        credentialsId("github")
        repoOwner("MDASII-BAH")
        repository("Challenge2BackEnd")

        traits {
            'org.jenkinsci.plugins.github__branch__source.BranchDiscoveryTrait'() {
              strategyId(3)
            }
            'org.jenkinsci.plugins.github__branch__source.ForkPullRequestDiscoveryTrait'() {
              strategyId(2)
              trust(class: "org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait\$TrustContributors")
            }
            'org.jenkinsci.plugins.github__branch__source.OriginPullRequestDiscoveryTrait'() {
              strategyId(2)
            }
        }
      }
      // default strategy when sourcing from a branch
      strategy(class: "jenkins.branch.DefaultBranchPropertyStrategy") {
        defaultProperties(class: "java.util.Arrays\$ArrayList") {
          a(class: "jenkins.branch.BranchProperty-array") {
            props(class: "empty-list")
          }
        }
      }
    }  


    it.remove(it / factory)

    it / factory(class: "org.boozallen.plugins.jte.job.TemplateBranchProjectFactory", plugin: "jte") {
      owner(class: "org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject", reference: "../..")
      scriptPath('Jenkinsfile')
    }

    def job_properties = it / 'properties'

    job_properties << 'org.jenkinsci.plugins.pipeline.modeldefinition.config.FolderConfig'(plugin: 'pipeline-model-definition@1.3.4'){
      dockerLabel ""
      registry(plugin: 'docker-commons@1.13')
    }

    job_properties << 'org.boozallen.plugins.jte.config.TemplateConfigFolderProperty'(plugin: 'jte'){
      tier {
        baseDir configBaseDir
        scm(class: "hudson.plugins.git.GitSCM", plugin: "git@3.9.1"){
          configVersion 2
          userRemoteConfigs{
            'hudson.plugins.git.UserRemoteConfig'{
              url pipelineConfigRepo
              credentialsId pipelineConfigRepoCredId
            }
          }
          branches{
            'hudson.plugins.git.BranchSpec'{
              name '*/cliang'
            }
          }
          doGenerateSubmoduleConfigurations false
          submoduleCfg(class: list)
          extensions ''
        }
        librarySources{
          'org.boozallen.plugins.jte.config.TemplateLibrarySource'{
            scm(class: "hudson.plugins.git.GitSCM", plugin: "git@3.9.1"){
              configVersion 2
              userRemoteConfigs{
                'hudson.plugins.git.UserRemoteConfig'{
                  url libraryRepo
                  credentialsId pipelineConfigRepoCredId
                }
              }
              branches{
                'hudson.plugins.git.BranchSpec'{
                  name '*/mdas2'
                }
              }
              doGenerateSubmoduleConfigurations false
              submoduleCfg(class: list)
              extensions ''
            }
          }
        }
      }
    }
  }

  orphanedItemStrategy {
    discardOldItems {
      daysToKeep(5)
      numToKeep(10)
    }
  }
}