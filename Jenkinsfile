pipeline {

  environment {
    NEXUS = credentials('350ecaca-7d1f-4acf-9602-8791a4a866b6')
  }
  agent {
    docker {
      image 'maven:3'
      args '-v ./settings.xml:/root/.m2/settings.xml'
    }
  }
  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }
    stage('Maven Build') {
      steps {
        sh 'sed -i "s/\${nexusUser}/${env.NEXUS_USR}/g" /root/.m2/settings.xml'
        sh 'sed -i "s/\${nexusPassword}/${env.NEXUS_PSW}/g" /root/.m2/settings.xml'
	sh 'url="https://repository.membrane-soa.org" && sed -i "s@\${nexusBaseUrl}@$url@g" /root/.m2/settings.xml'
        sh 'mvn clean install'
      }
    }
    stage('Maven Deploy') {
      steps {
        sh 'mvn deploy -N -DskipTests -D altDeploymentRepository=snapshots::default::https://repository.membrane-soa.org/content/repositories/snapshots'
      }
    }
  }
}
