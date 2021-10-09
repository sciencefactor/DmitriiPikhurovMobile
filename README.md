Tests running
=============

- 4-Root_2023.cer and 5-Issuing_2023.cer should be installed
- Put your EPAM mobile cloud token in new 'src/test/resources/properties/private.properties' file with name 'epamMobileCloudToken'
- Manual install EPAMTestApp.apk or EPAMTestApp.ipa on cloud devices
- Run tests via maven commands:
  * mvn clean test -PcloudNativeAndroid
  * mvn clean test -PcloudNativeIOs
  * mvn clean test -PcloudWebAndroid
  * mvn clean test -cloudWebIOs
