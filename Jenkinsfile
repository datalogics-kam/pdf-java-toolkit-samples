node {
    stage('Preparation') { // for display purposes
    checkout scm
    }
    stage('Build') {
        withMaven(
            jdk: "JDK 7",
            maven: "Maven 3.3",
            mavenSettingsConfig: "devauto-settings.xml",
            mavenLocalRepo: ".repository") {

            // Run the maven build
            if (isUnix()) {
                sh "mvn -B -V -U clean install"
            } else {
                bat(/mvn -B -V -U clean install/)
            }
            }
    }
    stage('Results') {
        archive 'target/*.jar'
    }
}
// vim: set et sts=4 sw=4 ts=4 ft=groovy :
