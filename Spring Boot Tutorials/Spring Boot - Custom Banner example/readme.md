This article shows you how to replace the default Spring’s banner below with your custom banner.

    .   ____          _            __ _ _
     /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
    ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
     \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
      '  |____| .__|_| |_|_| |_\__, | / / / /
     =========|_|==============|___/=/_/_/_/
     :: Spring Boot ::        (v1.5.1.RELEASE)

## Solution

1\. To add a custom banner in Spring Boot application, create a `banner.txt` file and put it into the `resources` folder.

![](http://www.mkyong.com/wp-content/uploads/2017/02/spring-boot-custom-banner-example-directory.png)

2\. Review the content of `banner.txt`, this ASCII Art is created by this “[ASCII Art Java example](https://www.mkyong.com/java/ascii-art-java-example/)“, and the ANSI colors are added manually.

src/main/resources/banner.txt

    ${Ansi.RED}                  $$$        $$$$$      $$$$         $$$$     $$$$$
    ${Ansi.GREEN}                  $$$       $$$$$$$     $$$$         $$$$    $$$$$$$
    ${Ansi.BLUE}                  $$$       $$$$$$$     $$$$$       $$$$$    $$$$$$$
    ${Ansi.RED}                  $$$       $$$$$$$      $$$$       $$$$     $$$$$$$
    ${Ansi.GREEN}                  $$$      $$$$ $$$$     $$$$$     $$$$$    $$$$ $$$$
    ${Ansi.BLUE}                  $$$      $$$$ $$$$      $$$$     $$$$     $$$$ $$$$
    ${Ansi.RED}                  $$$     $$$$$ $$$$$     $$$$     $$$$    $$$$$ $$$$$
    ${Ansi.GREEN}                  $$$     $$$$   $$$$     $$$$$   $$$$$    $$$$   $$$$
    ${Ansi.BLUE}                  $$$     $$$$   $$$$      $$$$   $$$$     $$$$   $$$$
    ${Ansi.RED}                  $$$    $$$$$   $$$$$     $$$$$ $$$$$    $$$$$   $$$$$
    ${Ansi.GREEN}                  $$$    $$$$$$$$$$$$$      $$$$ $$$$     $$$$$$$$$$$$$
    ${Ansi.BLUE}          $$$$   $$$$    $$$$$$$$$$$$$      $$$$ $$$$     $$$$$$$$$$$$$
    ${Ansi.RED}          $$$$   $$$$   $$$$$$$$$$$$$$$      $$$$$$$     $$$$$$$$$$$$$$$
    ${Ansi.GREEN}          $$$$$ $$$$$   $$$$       $$$$      $$$$$$$     $$$$       $$$$
    ${Ansi.BLUE}          $$$$$$$$$$$  $$$$$       $$$$$     $$$$$$$    $$$$$       $$$$$
    ${Ansi.RED}           $$$$$$$$$   $$$$         $$$$      $$$$$     $$$$         $$$$
    ${Ansi.GREEN}            $$$$$$$    $$$$         $$$$      $$$$$     $$$$         $$$$

    ${Ansi.RED} :: Spring Boot${spring-boot.formatted-version} :: ${Ansi.DEFAULT}

3\. Start Spring Boot, the following output will be displayed :

![](http://www.mkyong.com/wp-content/uploads/2017/02/spring-boot-custom-banner-example.png)

[http://www.mkyong.com/spring-boot/spring-boot-custom-banner-example/](http://www.mkyong.com/spring-boot/spring-boot-custom-banner-example/)
