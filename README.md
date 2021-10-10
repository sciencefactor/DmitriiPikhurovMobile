Tests running
=============
-------------
Remote running
--------------
- 4-Root_2023.cer and 5-Issuing_2023.cer should be installed in cacerts \
  IMPORTANT 'EpamMobileCloudApi.java (line 27)' uses standard relative path to your carerts file, based on 'java.home' folder.
  
- Put your cacerts password in new 'src/test/resources/properties/private.properties' file with tag name "cacertsPassword"
  
- Put your EPAM mobile cloud token in 'src/test/resources/properties/private.properties' file with tag name 'epamMobileCloudToken' (it is private data, so .gitignore will not let upload this file).
  
- No need for manual install applications.
  
- Run tests via maven commands:
  * mvn clean test -PcloudNativeAndroid
  * mvn clean test -PcloudNativeIOs
  * mvn clean test -PcloudWebAndroid
  * mvn clean test -PcloudWebIOs

Local running
---------
- Create Android Virtual Device via Android studio (I use Pixel XL API 29 Android 10.0 x86)
- Run emulator of this device
- Run Appium server with standard properties http://localhost:4723/wd/hub
- Run tests via maven commands:
  * mvn clean test -Pnative
  * mvn clean test -Pweb  