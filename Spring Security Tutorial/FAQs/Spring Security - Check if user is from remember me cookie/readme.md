This Spring Security example shows you how to check if a user is login from a “remember me” cookie.

    private boolean isRememberMeAuthenticated() {

    Authentication authentication =
    	SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
    	return false;
    }

        return RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass());
      }

      @RequestMapping(value = "/admin/update**", method = RequestMethod.GET)
      public ModelAndView updatePage() {

    ModelAndView model = new ModelAndView();

    if (isRememberMeAuthenticated()) {
    	model.setViewName("/login");
    } else {
    	model.setViewName("update");
    }

    return model;

      }

In Spring Security tag, you can code like this :

    <%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
    <%@page session="true"%>
    <html>
    <body>

    	<sec:authorize access="isRememberMe()">
    		<h2># This user is login by "Remember Me Cookies".</h2>
    	</sec:authorize>

    	<sec:authorize access="isFullyAuthenticated()">
    		<div class="ads-in-post hide_if_width_less_800">
    <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
    <!-- new 728x90 - After1stH4 -->
    <ins class="adsbygoogle hide_if_width_less_800"
         style="display:inline-block;width:728px;height:90px"
         data-ad-client="ca-pub-2836379775501347"
         data-ad-slot="7391621200"
         data-ad-region="mkyongregion"></ins>
    <script>
    (adsbygoogle = window.adsbygoogle || []).push({});
    </script>
    </div><h2># This user is login by username / password.</h2>
    	</sec:authorize>

    </body>
    </html>

**Note**  
isRememberMe() – Returns true if the current principal is a remember-me user  
isFullyAuthenticated() – Returns true if the user is not an anonymous or a remember-me user

[http://www.mkyong.com/spring-security/spring-security-check-if-user-is-from-remember-me-cookie/](http://www.mkyong.com/spring-security/spring-security-check-if-user-is-from-remember-me-cookie/)
