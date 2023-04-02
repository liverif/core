package it.liverif.core.auth;

import it.liverif.core.auth.beans.UserToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
public abstract class AUserAuth {
    protected UserToken getUser() {
        UserToken userToken=new UserToken();
        Authentication currentAuth= SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth!=null){
            Object principal = currentAuth.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetail=(UserDetails) principal;
                userToken.setUsername(userDetail.getUsername());
                for(GrantedAuthority ga: userDetail.getAuthorities()){
                    userToken.getRoles().add(ga.getAuthority().substring("ROLE_".length()));
                }
            } else {
                userToken.setUsername(principal.toString());
            }
        }
        return userToken;
    }

}
