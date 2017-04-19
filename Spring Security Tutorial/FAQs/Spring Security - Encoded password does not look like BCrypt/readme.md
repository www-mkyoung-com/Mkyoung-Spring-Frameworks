In Spring Security, database authentication with _bcrypt_ password hashing.

<pre>  import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
  import org.springframework.security.crypto.password.PasswordEncoder;
  //...
	String password = "123456";
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	String hashedPassword = passwordEncoder.encode(password);
</pre>

spring-security.xml

    <authentication-manager>
    <authentication-provider>
        <password-encoder hash="bcrypt" />
        //...
    </authentication-provider>
      </authentication-manager>

    CREATE  TABLE users (
      username VARCHAR(45) NOT NULL ,
      password VARCHAR(45) NOT NULL ,
      enabled TINYINT NOT NULL DEFAULT 1 ,
      PRIMARY KEY (username));

Review the debug output, it’s always said “**Encoded password does not look like BCrypt**“, even the correct password is provided.

    //...
    12:56:31.868 DEBUG o.s.jdbc.datasource.DataSourceUtils - Returning JDBC Connection to DataSource
    12:56:31.868 WARN  o.s.s.c.bcrypt.BCryptPasswordEncoder - Encoded password does not look like BCrypt
    12:56:31.868 DEBUG o.s.s.a.d.DaoAuthenticationProvider - Authentication failed: password does not match stored value

## Solution

In _bcrypt_ hashing algorithm, each time, a different hash value of length 60 is generated, for example

    $2a$10$LOqePml/koRGsk2YAIOFI.1YNKZg7EsQ5BAIuYP1nWOyYRl21dlne

A common mistake, the length of the “password” column (users table) is less than 60, for example, `password VARCHAR(45)`, and some databases will truncate the data automatically. So, you always get the warning “Encoded password does not look like BCrypt”.

**To solve it**, make sure the length of “password” column is at least 60.

[http://www.mkyong.com/spring-security/spring-security-encoded-password-does-not-look-like-bcrypt/](http://www.mkyong.com/spring-security/spring-security-encoded-password-does-not-look-like-bcrypt/)
