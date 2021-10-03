Tests running
=============

- Create Android Virtual Device via Android studio (I use Pixel XL API 29 Android 10.0 x86)
- Run emulator of this device
- Run Appium server with standard properties http://localhost:4723/wd/hub
- Run tests via maven commands:
  * mvn clean test -Pnative
  * mvn clean test -Pweb  
