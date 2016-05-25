/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sisac.view;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author borduchi
 */
public class UpperCaseConverter implements Converter {

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (String) value; // Or (value != null) ? value.toString().toUpperCase() : null;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return (value != null) ? value.toUpperCase() : null;
    }

}