package org.project1.shopweb.component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.project1.shopweb.utils.WebUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class LocalizationUtils {

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public String getLocalizationMessages(String messageskey,Object ... params){
        HttpServletRequest httpServletRequest = WebUtils.getCurrentContext();
        Locale locale = localeResolver.resolveLocale(httpServletRequest);

        return messageSource.getMessage(messageskey,params,locale);
    }
}

