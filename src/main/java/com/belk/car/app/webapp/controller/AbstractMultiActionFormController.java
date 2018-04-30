/**
 * 
 */
package com.belk.car.app.webapp.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 * @author antoniog
 *
 */
public class AbstractMultiActionFormController extends
        BaseFormController {

    private MethodNameResolver methodNameResolver;
    private String formView;
    private String successView;

    public ModelAndView processFormSubmission(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {

        if (errors.hasErrors()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Data binding errors: " + errors.getErrorCount());
            }
            return showForm(request, response, errors);
        } else {
            String methodName = methodNameResolver
                    .getHandlerMethodName(request);
            Method method;
            try {
                method = getClass().getMethod(
                        methodName,
                        new Class[] { HttpServletRequest.class,
                                HttpServletResponse.class, getCommandClass(),
                                BindException.class });
            } catch (NoSuchMethodException e) {
                throw new NoSuchRequestHandlingMethodException(methodName,
                        getClass());
            }
            List<Object> params = new ArrayList<Object>(4);
            params.add(request);
            params.add(response);
            params.add(getCommandClass().cast(command));
            params.add(errors);
            return (ModelAndView) method.invoke(this, params
                    .toArray(new Object[params.size()]));
        }
    }

    /**
     * This method allows for a single MultiActionValidator to validate methods
     * from the multiaction controller separately.
     * 
     * @param request
     *            the request object
     * @param command
     *            the command object
     * @param errors
     *            errors that will be determined during validation
     */
    protected void onBindAndValidate(HttpServletRequest request,
            Object command, BindException errors) throws Exception {
        String methodName = methodNameResolver.getHandlerMethodName(request);
        Method method;
        try {
            method = getValidator().getClass().getMethod(
                    methodName + "Validate",
                    new Class[] { getCommandClass(),
                            org.springframework.validation.Errors.class });
        } catch (NoSuchMethodException e) {
            // there should be no problem. There is no validation neccessary for
            // this action
            // OR it requires a standard validation for of the several methods
            // to use/share
            if (this.getValidator() != null) {
                try {
                    method = getValidator()
                            .getClass()
                            .getMethod(
                                    "validate",
                                    new Class[] {
                                            getCommandClass(),
                                            org.springframework.validation.Errors.class });
                } catch (NoSuchMethodException ex) {
                    this.getValidator().validate(command, errors);
                    return;
                }
                List<Object> params = new ArrayList<Object>(2);
                params.add(getCommandClass().cast(command));
                params.add(errors);
                method.invoke(this.getValidator(), params
                        .toArray(new Object[params.size()]));
            }
            return;
        }
        List<Object> params = new ArrayList<Object>(2);
        params.add(getCommandClass().cast(command));
        params.add(errors);
        method.invoke(this.getValidator(), params.toArray(new Object[params
                .size()]));
    }

    protected ModelAndView showForm(HttpServletRequest request,
            BindException errors) throws Exception {
        return showForm(request, errors, getFormView());
    }

    protected ModelAndView showForm(HttpServletRequest request,
            HttpServletResponse response, BindException errors)
            throws Exception {
        return showForm(request, errors, getFormView());
    }

    public void setMethodNameResolver(MethodNameResolver methodNameResolver) {
        this.methodNameResolver = methodNameResolver;
    }

}