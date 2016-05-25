package br.com.sisac.view;

import java.util.Map;

import javax.faces.context.FacesContext;

public class ManagedBeanUtil {
    public static final int ACAO_EXCLUIR = 1;
    public static final int ACAO_IR_PARA_ALTERAR = 2;
    public static final int ACAO_IR_PARA_INCLUIR = 3;
    public static final int ACAO_ATUALIZAR = 4;
    public static final int ACAO_IR_PARA_PESQUISAR = 5;
    public static final int ACAO_PESQUISAR = 6;
    public static final int ACAO_IR_PARA_LISTAR = 100;
    public static final int ACAO_VISITAR = 500;
    public static final String USUARIO_SESSAO = "USUARIO_SESSAO";
    
    public static long getIdParam() {
        Map<String,String> parametros = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        return Long.parseLong(parametros.get("idParam"));
    }
    
    public static int getAcaoParam() {
        Map<String,String> parametros = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        return Integer.parseInt(parametros.get("acaoParam"));
    }
}
    
