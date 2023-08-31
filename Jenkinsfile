pipeline {
    agent any
    stages {
        stage('Scan') {

          steps {
                          sh './gradlew sonar \
                                  -Dsonar.projectKey=BaseMVVM_Android \
                                  -Dsonar.projectName='BaseMVVM_Android' \
                                  -Dsonar.host.url=https://sonar.rikkei.org/ \
                                  -Dsonar.token=sqp_2b9ad6ab47c01465c82ae978ac55b2c7a862b55a'
//               script {
//                 // requires SonarQube Scanner 2.8+
//                 // test
//                 scannerHome = tool 'Rikkei SonarQube'
//               }
//             withSonarQubeEnv(installationName: 'Rikkei SonarQube') {
//                 sh './gradlew sonarqube'
//
//             }
          }
        }
      }
}
