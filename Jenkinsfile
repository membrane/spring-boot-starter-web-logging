node {

    def server = Artifactory.server "Membrane-SOA"
    server.credentialsId='350ecaca-7d1f-4acf-9602-8791a4a866b6'

    def rtMaven = Artifactory.newMavenBuild()
    def buildInfo

    stage("Checkout") {
        git url: "https://github.com/membrane/spring-boot-starter-web-logging.git"
    }

    stage("Artifactory configuration") {
        rtMaven.tool = "Maven-3.6.3"
        rtMaven.deployer releaseRepo:'releases', snapshotRepo:'snapshots', server: server
    }

    stage('Maven build') {
        buildInfo = rtMaven.run pom: 'pom.xml', goals: 'clean install'
    }

    stage('Publish build info') {
        server.publishBuildInfo buildInfo
    }

}