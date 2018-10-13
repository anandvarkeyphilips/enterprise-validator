#!groovy
node {

    // Get Artifactory server instance, defined in the Artifactory Plugin administration page.
    def server = Artifactory.server "varkeys-artifactory"
    // Create an Artifactory Maven instance.
    def rtMaven = Artifactory.newMavenBuild()
    def buildInfo

    stage('Clone sources') {
        checkout scm
    }

    stage('Artifactory configuration') {
        // Tool name from Jenkins configuration
        rtMaven.tool = "Maven-3.5.4"
        // Set Artifactory repositories for dependencies resolution and artifacts deployment.
        rtMaven.deployer releaseRepo:'libs-release-local', snapshotRepo:'libs-snapshot-local', server: server
        rtMaven.resolver releaseRepo:'libs-release', snapshotRepo:'libs-snapshot', server: server
    }

    stage('Maven build') {
        buildInfo = rtMaven.run pom: 'pom.xml', goals: 'clean install'
    }

    stage('Publish build info') {
        server.publishBuildInfo buildInfo
    }

    stage('Deploy'){
        def pom = readMavenPom file: "pom.xml"
        def buildType,artifactId=pom.artifactId,groupIdFormatted=(pom.groupId).replace(".", "/"), artifactVersion=pom.version
        if(pom.version.contains(".RELEASE")){
            buildType = "libs-release"
        }else{
            buildType = "libs-snapshot"
        }
        def packaging=pom.packaging, artifactPath="${buildType}/${groupIdFormatted}/${artifactId}/${artifactVersion}"

        withEnv(["ANSIBLE_HOST_KEY_CHECKING=False"]) {
          ansiblePlaybook credentialsId: 'varkeys-rhel-jenkins.westus.cloudapp.azure.com',
          installation: 'ansible 2.7.0',
          inventory: 'provision/inventory.ini',
          playbook: 'provision/playbook.yml',
          extras: "--become-user=anandvarkeyphilips -vvv -e ansible_host_key_checking=False -e artifactPath=${artifactPath} -e artifactId=${artifactId} -e artifactVersion=${artifactVersion} -e packaging=${packaging}"
        }
    }
}

def getCommitSha() {
  sh "git rev-parse HEAD > .git/current-commit"
  return readFile(".git/current-commit").trim()
}

def updateGithubCommitStatus(build) {
  repoUrl = getRepoURL()
  commitSha = getCommitSha()

  step([
      $class: 'GitHubCommitStatusSetter',
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: repoUrl],
      commitShaSource: [$class: "ManuallyEnteredShaSource", sha: commitSha],
      errorHandlers: [[$class: 'ShallowAnyErrorHandler']],
      statusResultSource: [
        $class: 'ConditionalStatusResultSource',
        results: [
          [$class: 'BetterThanOrEqualBuildResult', result: 'SUCCESS', state: 'SUCCESS', message: build.description],
          [$class: 'BetterThanOrEqualBuildResult', result: 'FAILURE', state: 'FAILURE', message: build.description],
          [$class: 'AnyBuildResult', state: 'FAILURE', message: 'Loophole']
        ]
      ]
  ])
}

def getRepoURL() {
  sh "git config --get remote.origin.url > .git/remote-url"
  return readFile(".git/remote-url").trim()
}